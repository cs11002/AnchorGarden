package jp.ac.jaist.css.common.piccolo;

import java.awt.Component;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

import jp.ac.jaist.css.common.gui.menu.FramePopup;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PNode;

public class PNodeMenu extends JPopupMenu implements FramePopup {
	public PNode target;
	public JFrame frame;
	public PCanvas canvas;
	private static final long serialVersionUID = -3662668322301800275L;

	public PNodeMenu(PNode _v) {
		this.target = _v;

		JMenuItem menuItem;

		setLightWeightPopupEnabled(false);

		menuItem = new JMenuItem("cancel");
		add(menuItem);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
	}

	public void showWithFrame(Component c, int x, int y, JFrame _f) {
		frame = _f;
		if (c instanceof PCanvas){
			canvas = (PCanvas)c;
		}
		show(c, x, y);
	}
	
	public void remove(){
	}
	public void setTransparency(float f){
		target.setTransparency(f);
	}

}
