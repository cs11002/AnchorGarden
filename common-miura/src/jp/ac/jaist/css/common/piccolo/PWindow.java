package jp.ac.jaist.css.common.piccolo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEventFilter;
import edu.umd.cs.piccolo.util.PBounds;

public class PWindow implements ActionListener {
	public static ArrayList<PWindow> windows = new ArrayList<PWindow>();
	MyPFrame frame;
	MyPCanvas canvas;
	
	PBuffer buffer;
	JMenu bufferMenu;
	
	public static int id;
	
	public PWindow(){
		PBuffer b = new PBuffer();
		constructFrame();
		switchBuffer(b);
		frame.setVisible(true);
	}
	public PWindow(PBuffer b){
		constructFrame();
		switchBuffer(b);
		frame.setVisible(true);
	}
	public void constructFrame(){
		frame = new MyPFrame("Window "+String.valueOf(windows.size())+" ("+String.valueOf(PWindow.id)+")", false, null);
		PWindow.id++;
		windows.add(this);
		
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowListener(){
			public void windowOpened(WindowEvent arg0) {}
			public void windowClosing(WindowEvent arg0) {
				tobeclosed();
			}
			public void windowClosed(WindowEvent arg0) {}
			public void windowIconified(WindowEvent arg0) {}
			public void windowDeiconified(WindowEvent arg0) {}
			public void windowActivated(WindowEvent arg0) {}
			public void windowDeactivated(WindowEvent arg0) {}
		});
		
		initializeMenu();
	}
	protected void initializeMenu(){
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		bufferMenu = new JMenu("Buffers");
		menuBar.add(bufferMenu);
		
		JMenuItem newBuffer = new JMenuItem("new buffer");
		newBuffer.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				newBuffer();
			}		
		});
		fileMenu.add(newBuffer);
		
		JMenuItem newFrame = new JMenuItem("new window");
		newFrame.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				newFrame();
			}		
		});
		fileMenu.add(newFrame);
		
		fileMenu.addSeparator();
		
		JMenuItem printpreview = new JMenuItem("set preview frame");
		printpreview.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
//				canvas.setPreferredSize(new Dimension(584,816));
				frame.setSize(592,876);
				frame.validate();
			}		
		});
		fileMenu.add(printpreview);
		
		JMenuItem print = new JMenuItem("print");
		print.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				PrintPrintable.print(canvas.cam);
			}		
		});
		fileMenu.add(print);
		
		
		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if (JOptionPane.showConfirmDialog(PWindow.this.frame, "本当に全部終了してよろしいですか？", "確認", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
					System.exit(0);
				}
			}		
		});
		fileMenu.add(exit);
		updateBufferMenu();
		
		JMenu viewMenu = new JMenu("View");
		JMenuItem full = new JMenuItem("Full Fit");
		full.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				canvas.cam.animateViewToCenterBounds(
						buffer.layer.getUnionOfChildrenBounds(new PBounds()),
						true, 1000);
			}		
		});
		viewMenu.add(full);
		menuBar.add(viewMenu);
		
		frame.setJMenuBar(menuBar);
	}
	
	public void updateBufferMenu(){
		bufferMenu.removeAll();
		for(PBuffer b: PBuffer.buffers){
			JMenuItem jm = new JMenuItem(b.name);
			jm.addActionListener(this);
			bufferMenu.add(jm);
		}
	}

	public void switchBuffer(PBuffer b){
		PCamera cam = new PCamera();
		b.addCamera(cam);
		
		canvas = frame.getCanvas();
		canvas.setCamera(cam);
		initialize(canvas, b);
		buffer = b;
	}
	
	public void initialize(MyPCanvas canvas, PBuffer buffer){
		MySelectionEventHandler selectionEventHandler = new MySelectionEventHandler(buffer.layer, buffer.layer, canvas.getCamera());
		buffer.root.getDefaultInputManager().setKeyboardFocus(selectionEventHandler);
		selectionEventHandler.setEventFilter(new PInputEventFilter(InputEvent.BUTTON1_MASK));
		canvas.addInputEventListener(selectionEventHandler);
		canvas.addKeyListener(selectionEventHandler);
		
		PBasicInputEventHandler squiggleEventHandler = new SquiggleEventHandler(buffer.layer);
		squiggleEventHandler.setEventFilter(new PInputEventFilter(InputEvent.BUTTON3_MASK));
		canvas.addInputEventListener(squiggleEventHandler);
		
		canvas.initialize();
		
	}
	public void tobeclosed(){
		if (windows.size()==1){
			if (JOptionPane.showConfirmDialog(this.frame,"最後のウィンドウです．\n本当に閉じてよろしいですか？", "確認", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
				System.exit(0);
			} else {
				
			}
		} else {
			windows.remove(this);
			this.frame.setVisible(false);
		}
	}
	
	public void newFrame(){
		new PWindow(this.buffer);
	}
	
	public void newBuffer(){
		switchBuffer(new PBuffer());
	}
	
	public static void main(String[] arg){
		new PWindow();
	}
	/** 
	 * EventListener for switching Buffer
	 */
	public void actionPerformed(ActionEvent arg0) {
		PBuffer b = PBuffer.find(arg0.getActionCommand());
		if (b != null) switchBuffer(b);
	}
}
