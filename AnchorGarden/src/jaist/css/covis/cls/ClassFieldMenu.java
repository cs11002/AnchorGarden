package jaist.css.covis.cls;

import jaist.css.covis.CoVisBuffer;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import jp.ac.jaist.css.common.gui.menu.FramePopup;

public class ClassFieldMenu extends JPopupMenu implements FramePopup, ActionListener {
	ClassField f;
	JFrame frame;
	private static final long serialVersionUID = -3662668322301800275L;
//	public static String[] modes = {"Novice mode", "Class/Instance mode", "Inheritance mode", "OOP inhe mode", "OOP caps mode", "OOP poly mode"};
	public static String[] modes = {"Novice mode", "Nomal mode", "OOP inheri mode", "OOP caps mode", "OOP poly mode", "Class/Instance mode", "Inheritance mode"};
	CoVisBuffer buffer;
	public ClassFieldMenu(ClassField _f, CoVisBuffer buf) {
		this.f = _f;
		buffer = buf;

		JMenuItem menuItem;

		setLightWeightPopupEnabled(false);

		menuItem = new JMenuItem("cancel");
		add(menuItem);
		
		addSeparator();
		
		for(int i=0;i<modes.length;i++){
			menuItem = new JMenuItem(modes[i]);
			add(menuItem);
			menuItem.addActionListener(this);
		}


//		addSeparator();
//
//		menuItem = new JMenuItem("レイアウト");
//		add(menuItem);
//		menuItem.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				f.layout(500);
//			}
//		});

	}
	public void showWithFrame(Component c, int x, int y, JFrame _f) {
		frame = _f;
		show(c, x, y);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		String act = e.getActionCommand();
		for(int i=0;i<modes.length;i++){
			if (act.equals(modes[i])){
				buffer.showAdvancedClasses(i);
				buffer.getWindow().srcWindow.changeTab(i);
			}
		}
		
	}

}
