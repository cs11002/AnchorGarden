package jaist.css.covis.cls;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import jp.ac.jaist.css.common.gui.menu.FramePopup;

public class ClassVarMenu extends JPopupMenu implements FramePopup {

	private static final long serialVersionUID = 2231783590340700929L;
	Covis_primitive v;
	Covis_Object obj;
	String varname;
	JFrame f;
	JMenuItem menuItem;

	public ClassVarMenu(String _varname, Covis_primitive _v,Covis_Object _obj){
		this.v = _v;
		this.obj = _obj;
		this.varname = _varname;

		setLightWeightPopupEnabled(false);

		menuItem = new JMenuItem("cancel");
		add(menuItem);
		menuItem = null;
		
		addSeparator();

	}

	public void showWithFrame(Component c, int x, int y, JFrame _f) {
		f = _f;

		if(obj.anchors_incoming.size()>0 ) {
			if (menuItem != null) remove(menuItem);
			
			menuItem = new JMenuItem("edit value");
			add(menuItem);

			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String input;
					input = JOptionPane.showInputDialog(f, "Input Value", v.getValue());
					if (input == null) return;
					if (!v.setValue(input)){
						JOptionPane.showMessageDialog(f,"Value is not accepted.","Error",JOptionPane.WARNING_MESSAGE);
						return;
					}
					v.buffer.putHistoryEditClassValue(varname,v,obj); //変更したらソースコードに追加
					if(obj instanceof Covis_UnCapAccount) {
						((Covis_UnCapAccount)obj).setValue();
					}
					Informer.playSound("Pop.wav");
				}
			});
		}
		show(c, x, y);
	}
}
