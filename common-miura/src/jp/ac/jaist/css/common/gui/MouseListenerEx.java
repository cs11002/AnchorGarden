package jp.ac.jaist.css.common.gui;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.JOptionPane;

/**
 * 長押しなどのイベントに対応した，高機能マウスリスナー
 * @author miuramo
 *
 */
public class MouseListenerEx implements MouseListener, MouseMotionListener, MouseWheelListener {
	public static int PRESSED = 1;
	public static int DRAGGED = 2;
	public static int TPASSED = 3;
	public static int RELEASED = 0;
	
	public static Cursor handcursor = new Cursor(Cursor.HAND_CURSOR);
	public static Cursor defaultcursor = new Cursor(Cursor.DEFAULT_CURSOR);
	
	
	public int mouseMode = 0;
	public int lastMouseButton;
	public ArrayList<MouseEvent> buffer;
	public ArrayList<Point2D> points;
	public int dragCount;
	public ArrayList<Integer> modeTransition;
	public Hashtable<String,MouseDelegator> leftmap;
	public Hashtable<String,MouseDelegator> rightmap;
	public Hashtable<String,MouseDelegator> middlemap;
	public Hashtable<Integer,Hashtable<String,MouseDelegator>> mousemap;
	public MouseDelegator mouseMoved = null;
	public MouseDelegator mouseEntered = null;
	public MouseDelegator mouseExited = null;
	public MouseWheelDelegator wheelRotated = null;
	public long pressedTime;
	public long releasedTime;
	public long acceptClickMaxMsec = 600;
	
	private Hashtable<String,MLEXTimerTask> leftMLEXTasks;
	private Hashtable<String,MLEXTimerTask> rightMLEXTasks;
	private Hashtable<String,MLEXTimerTask> middleMLEXTasks;
	private Hashtable<Integer,Hashtable<String,MLEXTimerTask>> mlexTasksMap;
	
	private ArrayList<java.util.Timer> leftDaemons;
	private ArrayList<java.util.Timer> rightDaemons;
	private ArrayList<java.util.Timer> middleDaemons;
	private Hashtable<Integer,ArrayList<java.util.Timer>> daemonsMap;
	
	public MouseListenerEx(){
		buffer = new ArrayList<MouseEvent>();
		points = new ArrayList<Point2D>();
		modeTransition = new ArrayList<Integer>();
		leftmap = new Hashtable<String,MouseDelegator>();
		rightmap = new Hashtable<String,MouseDelegator>();
		middlemap = new Hashtable<String,MouseDelegator>();
		mousemap = new Hashtable<Integer,Hashtable<String,MouseDelegator>>();
		mousemap.put(MouseEvent.BUTTON1, leftmap);
		mousemap.put(MouseEvent.BUTTON2, middlemap);
		mousemap.put(MouseEvent.BUTTON3, rightmap);
		
		leftMLEXTasks = new Hashtable<String,MLEXTimerTask>();
		rightMLEXTasks = new Hashtable<String,MLEXTimerTask>();
		middleMLEXTasks = new Hashtable<String,MLEXTimerTask>();
		mlexTasksMap = new Hashtable<Integer,Hashtable<String,MLEXTimerTask>>();
		mlexTasksMap.put(MouseEvent.BUTTON1, leftMLEXTasks);
		mlexTasksMap.put(MouseEvent.BUTTON2, middleMLEXTasks);
		mlexTasksMap.put(MouseEvent.BUTTON3, rightMLEXTasks);
		
		leftDaemons = new ArrayList<java.util.Timer>();
		rightDaemons = new ArrayList<java.util.Timer>();
		middleDaemons = new ArrayList<java.util.Timer>();
		daemonsMap = new Hashtable<Integer,ArrayList<java.util.Timer>>();
		daemonsMap.put(MouseEvent.BUTTON1, leftDaemons);
		daemonsMap.put(MouseEvent.BUTTON2, middleDaemons);
		daemonsMap.put(MouseEvent.BUTTON3, rightDaemons);
	}
	
	/**
	 * 長押しタイマーイベントの仕込み用
	 * @param button 反応するボタン番号(1,2,3)
	 * @param msec 待つミリ秒数
	 * @param md イベント発生時のリスナー
	 * @param desc 管理用の名前 例："timepass_b1_600"
	 */
	public void addMLEXTimer(int button, int msec, MouseDelegator md, String desc){
		Hashtable<String,MLEXTimerTask> daemons = mlexTasksMap.get(button);
		if (daemons != null){
			daemons.put(desc, new MLEXTimerTask(button, msec, this, md, desc));
		}
	}
	/**
	 * 仕込んだ長押しタイマーイベントの削除用
	 * @param button 反応するボタン番号(1,2,3)
	 * @param desc 管理用の名前
	 */
	public void removeMLEXTimer(int button, String desc){
		Hashtable<String,MLEXTimerTask> daemons = mlexTasksMap.get(button);
		if (daemons != null){
			daemons.remove(desc);
		}
	}
	/**
	 * イベントアクションデレゲータ登録
	 * @param button 反応するボタン番号(1,2,3)
	 * @param md イベント発生時のリスナー
	 * @param desc "press","drag","release","click"
	 * "move","enter", "exit"は直接追加する
	 */
	public void setDelegator(int button, MouseDelegator md, String desc){
		if (desc.equals("move")){
			warning("move は setDelegator() 経由ではなく，MouseDelegatorの参照 mouseMoved に直接接続して使います．");
		} else if (desc.equals("enter")){
			warning("enter は setDelegator() 経由ではなく，MouseDelegatorの参照 mouseEntered に直接接続して使います．");
		} else if (desc.equals("exit")){
			warning("exit は setDelegator() 経由ではなく，MouseDelegatorの参照 mouseExited に直接接続して使います．");
		}
		Hashtable<String,MouseDelegator> map = mousemap.get(button);
		if (map != null) {
			map.put(desc, md);
		}
	}
	/**
	 * エラーメッセージ
	 * @param s
	 */
	void warning(String s){
		String err = "MouseListenerEx 使用法に間違いがあります";
		System.out.println(err+"\n"+s);
		JOptionPane.showMessageDialog(null,err+"\n"+s,err,JOptionPane.ERROR_MESSAGE);
	}
	
	public void mouseClicked(MouseEvent arg0) {
		if ((releasedTime - pressedTime) <= acceptClickMaxMsec){
//			System.out.println("clicked "+arg0.getClickCount());
			Hashtable<String,MouseDelegator> map = mousemap.get(lastMouseButton);
			if (map != null) {
				MouseDelegator md = map.get("click");
				if (md != null) md.mouseActionPerformed("click", arg0, this);			
			}
		}
	}
	
	public void mousePressed(MouseEvent arg0) {
		mouseMode = MouseListenerEx.PRESSED;
		lastMouseButton = arg0.getButton();
		System.out.println(lastMouseButton + " pressed ");
		pressedTime = System.currentTimeMillis();
		buffer.clear();
		points.clear();
		modeTransition.clear();
		dragCount = 0;
		buffer.add(arg0);
		points.add(arg0.getPoint());
		modeTransition.add(MouseListenerEx.PRESSED);
		Hashtable<String,MouseDelegator> map = mousemap.get(arg0.getButton());
		if (map != null) {
			MouseDelegator md = map.get("press");
			if (md != null) md.mouseActionPerformed("press", arg0, this);			
		}
		// daemon starts;
		Hashtable<String,MLEXTimerTask> mlexTasks = mlexTasksMap.get(arg0.getButton());
		ArrayList<java.util.Timer> daemons = daemonsMap.get(arg0.getButton());
		daemons.clear();
		Enumeration<?> enu = mlexTasks.elements();
		while(enu.hasMoreElements()){
			MLEXTimerTask tt = (MLEXTimerTask) enu.nextElement();
			java.util.Timer daemon = new java.util.Timer();
			daemon.schedule(tt, tt.msec);
		}
	}
	
	public void mouseReleased(MouseEvent arg0) {
		// タイマーリセット
		ArrayList<java.util.Timer> daemons = daemonsMap.get(arg0.getButton());
		for(java.util.Timer d : daemons){
			d.cancel();
		}
		daemons.clear();
		mouseMode = MouseListenerEx.RELEASED;
		modeTransition.add(MouseListenerEx.RELEASED);
//		lastMouseButton = arg0.getButton();
		releasedTime = System.currentTimeMillis();
		buffer.add(arg0);
		points.add(arg0.getPoint());
		Hashtable<String,MouseDelegator> map = mousemap.get(lastMouseButton);
		if (map != null) {
			MouseDelegator md = map.get("release");
			if (md != null) md.mouseActionPerformed("release",arg0, this);			
		}
		System.out.println("released  (dragCount = "+dragCount+" )");
		for(int i: modeTransition){
			System.out.print(" -> "+i);
		}
		System.out.println("");
	}
	public void timePassed(int button, int msec, MouseDelegator md, String desc){
		System.out.println("time passed ["+desc+"] "+msec);
		mouseMode = MouseListenerEx.TPASSED;
		modeTransition.add(MouseListenerEx.TPASSED);
		if (md != null) md.mouseActionPerformed(desc, buffer.get(buffer.size()-1), this);			
	}
	public void mouseDragged(MouseEvent arg0) {
		if (mouseMode != MouseListenerEx.DRAGGED){
			modeTransition.add(MouseListenerEx.DRAGGED);
			mouseMode = MouseListenerEx.DRAGGED;
		}
		if (dragCount == 0){
			System.out.println("drag started");
		}
		dragCount++;
		buffer.add(arg0);
		points.add(arg0.getPoint());
		Hashtable<String,MouseDelegator> map = mousemap.get(lastMouseButton);
		if (map != null) {
			MouseDelegator md = map.get("drag");
			if (md != null) md.mouseActionPerformed("drag", arg0, this);
		}
	}
	
	public void mouseMoved(MouseEvent arg0) {
		if (mouseMoved != null) mouseMoved.mouseActionPerformed("move",arg0, this);
	}
	public void mouseEntered(MouseEvent arg0) {
		if (mouseEntered != null) mouseEntered.mouseActionPerformed("enter",arg0, this);
	}
	
	public void mouseExited(MouseEvent arg0) {
		if (mouseExited != null) mouseExited.mouseActionPerformed("exit",arg0, this);
	}
	
	
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		System.out.println("wheel rotation "+arg0.getWheelRotation());
		if (wheelRotated != null) wheelRotated.wheelActionPerformed(arg0, this);
	}
	
}
