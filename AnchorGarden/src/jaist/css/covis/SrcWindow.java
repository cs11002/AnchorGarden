package jaist.css.covis;

import jaist.css.covis.cls.Covis_Animal;
import jaist.css.covis.cls.Covis_BTree;
import jaist.css.covis.cls.Covis_Bus;
import jaist.css.covis.cls.Covis_CapAccount;
import jaist.css.covis.cls.Covis_Car;
import jaist.css.covis.cls.Covis_Cat;
import jaist.css.covis.cls.Covis_Dog;
import jaist.css.covis.cls.Covis_Frac;
import jaist.css.covis.cls.Covis_Truck;
import jaist.css.covis.cls.Covis_UnCapAccount;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JViewport;

public class SrcWindow extends JFrame implements ActionListener {
	//	public static boolean doFollowMainWindow = true;

	private static final long serialVersionUID = 1526947556907544070L;
	JTextAreaToggleAntiAlias jta;
	JScrollPane scroll;
	JButton process;
	JButton loadNow;
	CoVisBuffer buffer;
	CoVisWindow window;

	public JCheckBoxMenuItem doFollowSrcWin;//ソースコードウィンドウを追従させる？
	public JCheckBoxMenuItem doFollowSrcWinLeft;//ソースコードウィンドウを追従させる？

	JTabbedPane tab;
	List<JTextAreaToggleAntiAlias> tabValue;
	public static Font sans14 = new Font("sansserif", Font.BOLD, 14);
	public static Font sans18 = new Font("sansserif", Font.BOLD, 18);
	public static Font sans22 = new Font("sansserif", Font.BOLD, 22);
	public static Font sans26 = new Font("sansserif", Font.BOLD, 26);
	public static Font sans30 = new Font("sansserif", Font.BOLD, 30);
	public static Font sans34 = new Font("sansserif", Font.BOLD, 34);
	public static Font sans38 = new Font("sansserif", Font.BOLD, 38);
	public static Font sans42 = new Font("sansserif", Font.BOLD, 42);
	public static Font sans46 = new Font("sansserif", Font.BOLD, 46);

	public static Font code14 = new Font("sansserif", Font.PLAIN, 14);
	//	public static Font code14 = new Font("DialogInput", Font.BOLD, 14);

	public SrcWindow(CoVisBuffer buf, CoVisWindow win){
		super("Source Code Window");
		buffer = buf;
		window = win;


		setLayout(new BorderLayout());
		jta = new JTextAreaToggleAntiAlias("");
		//jta.setLineWrap(true);
		scroll = new JScrollPane(jta);

		jta.setFont(sans22);


		JMenuBar menuBar = new JMenuBar();
		JMenu fontM = new JMenu("FontSize");
		JMenuItem jmi = new JMenuItem("14");
		fontM.add(jmi);
		jmi.addActionListener(this);
		jmi = new JMenuItem("18");
		fontM.add(jmi);
		jmi.addActionListener(this);
		jmi = new JMenuItem("22");
		fontM.add(jmi);
		jmi.addActionListener(this);
		jmi = new JMenuItem("26");
		fontM.add(jmi);
		jmi.addActionListener(this);
		jmi = new JMenuItem("30");
		fontM.add(jmi);
		jmi.addActionListener(this);
		jmi = new JMenuItem("34");
		fontM.add(jmi);
		jmi.addActionListener(this);
		jmi = new JMenuItem("38");
		fontM.add(jmi);
		jmi.addActionListener(this);
		jmi = new JMenuItem("42");
		fontM.add(jmi);
		jmi.addActionListener(this);
		jmi = new JMenuItem("46");
		fontM.add(jmi);
		jmi.addActionListener(this);
		menuBar.add(fontM);

		JMenu windowMenu = new JMenu("Window");
		doFollowSrcWin = new JCheckBoxMenuItem("Snap to MainWindow");
		doFollowSrcWin.setSelected(true);
		windowMenu.add(doFollowSrcWin);
		menuBar.add(windowMenu);

		doFollowSrcWinLeft = new JCheckBoxMenuItem("Align to Left of MainWindow");
		doFollowSrcWinLeft.setSelected(true);
		doFollowSrcWinLeft.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				follow();
			}
		});
		windowMenu.add(doFollowSrcWinLeft);


		setJMenuBar(menuBar);
		//tabの生成
		tabValue = new ArrayList<JTextAreaToggleAntiAlias>();
		JTextAreaToggleAntiAlias t;

		tab = new JTabbedPane();
		tab.addTab("(operation code)", scroll);
		//クラスソースコード用タブ

		t = new JTextAreaToggleAntiAlias(Covis_Frac.classdef);
		t.setEditable(false);
		t.setFont(code14);
		tabValue.add(t);
		//tab.addTab("class Frac", new JScrollPane(t));

		t = new JTextAreaToggleAntiAlias(Covis_BTree.classdef);
		t.setEditable(false);
		t.setFont(code14);
		tabValue.add(t);
		//tab.addTab("class BTree", new JScrollPane(t));

		t = new JTextAreaToggleAntiAlias(Covis_Car.classdef);
		t.setEditable(false);
		t.setFont(code14);
		tabValue.add(t);
		//tab.addTab("class Car", new JScrollPane(t));

		t = new JTextAreaToggleAntiAlias(Covis_Bus.classdef);
		t.setEditable(false);
		t.setFont(code14);
		tabValue.add(t);
		//tab.addTab("class Bus", new JScrollPane(t));

		t = new JTextAreaToggleAntiAlias(Covis_Truck.classdef);
		t.setEditable(false);
		t.setFont(code14);
		tabValue.add(t);
		//tab.addTab("class Truck", new JScrollPane(t));

		t = new JTextAreaToggleAntiAlias(Covis_CapAccount.classdef);
		t.setEditable(false);
		t.setFont(code14);
		tabValue.add(t);
		//tab.addTab("class CapsuledAccount", new JScrollPane(t));

		t = new JTextAreaToggleAntiAlias(Covis_UnCapAccount.classdef);
		t.setEditable(false);
		t.setFont(code14);
		tabValue.add(t);
		//tab.addTab("class UnCapsuledAccount", new JScrollPane(t));

		t = new JTextAreaToggleAntiAlias(Covis_Animal.classdef);
		t.setEditable(false);
		t.setFont(code14);
		tabValue.add(t);
		//tab.addTab("class Animal", new JScrollPane(t));

		t = new JTextAreaToggleAntiAlias(Covis_Dog.classdef);
		t.setEditable(false);
		t.setFont(code14);
		tabValue.add(t);
		//tab.addTab("class Cat", new JScrollPane(t));

		t = new JTextAreaToggleAntiAlias(Covis_Cat.classdef);
		t.setEditable(false);
		t.setFont(code14);
		tabValue.add(t);
		//tab.addTab("class Dog", new JScrollPane(t));

		/*
		tab = new JTabbedPane();
		tab.addTab("(operation code)", scroll);
		JTextAreaToggleAntiAlias fracclass = new JTextAreaToggleAntiAlias(Covis_Frac.classdef);
		fracclass.setEditable(false);
		fracclass.setFont(code14);
		tab.addTab("class Frac", new JScrollPane(fracclass));
		JTextAreaToggleAntiAlias btreeclass = new JTextAreaToggleAntiAlias(Covis_BTree.classdef);
		btreeclass.setEditable(false);
		btreeclass.setFont(code14);
		tab.addTab("class BTree", new JScrollPane(btreeclass));
		 */
		//tab.addTab("String", new JTextArea());

		getContentPane().add(tab, BorderLayout.CENTER);
		//		pack();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize((int)(400),(int)(window.frame.getHeight()));
		follow();
	}
	public void actionPerformed(ActionEvent e){
		int fsize = Integer.parseInt(e.getActionCommand());
		JScrollPane jsp = (JScrollPane)tab.getComponentAt(tab.getSelectedIndex());
		//System.out.println(jsp.getComponent(0).getClass().toString());
		JViewport v = (JViewport) jsp.getViewport();
		JTextAreaToggleAntiAlias target = (JTextAreaToggleAntiAlias) v.getView();
		target.setFontSize(fsize);
		validate();
		repaint();
	}

	public String lastLine(){
		String[] lines = jta.getText().split("\n");
		return lines[lines.length-1];
	}
	public void follow() {
		if (doFollowSrcWin.isSelected()){
			if (doFollowSrcWinLeft.isSelected()){
				setLocation((int)(window.frame.getLocation().getX()-this.getWidth()),(int)(window.frame.getLocation().getY()));
				setSize((int)(this.getWidth()),(int)(window.frame.getHeight()));
			} else {
				setLocation((int)(window.frame.getLocation().getX()+window.frame.getWidth()),(int)(window.frame.getLocation().getY()));
				setSize((int)(this.getWidth()),(int)(window.frame.getHeight()));
			}
		}
	}

	public void changeTab(int type) {
		tab.removeAll();
		tab.addTab("(operation code)", scroll);
		switch (type) {
		case CoVisBuffer.ADVANCEMODE:
			tab.addTab("class Frac", new JScrollPane(tabValue.get(0)));
			tab.addTab("class BTree", new JScrollPane(tabValue.get(1)));
			break;
		case CoVisBuffer.NOMALMODE:
			tab.addTab("class Car", new JScrollPane(tabValue.get(2)));
			break;
		case CoVisBuffer.OOP_INHERMODE:
			tab.addTab("class Car", new JScrollPane(tabValue.get(2)));
			tab.addTab("class Bus", new JScrollPane(tabValue.get(3)));
			tab.addTab("class Truck", new JScrollPane(tabValue.get(4)));
			break;
		case CoVisBuffer.OOP_CAPSMODE:
			tab.addTab("class CapsuledAccount", new JScrollPane(tabValue.get(5)));
			tab.addTab("class UnCapsuledAccount", new JScrollPane(tabValue.get(6)));
			break;
		case CoVisBuffer.OOP_POLYMODE:
			tab.addTab("class Animal", new JScrollPane(tabValue.get(7)));
			tab.addTab("class Dog", new JScrollPane(tabValue.get(8)));
			tab.addTab("class Cat", new JScrollPane(tabValue.get(9)));
			break;
		default:
			break;
		}
	}
}
