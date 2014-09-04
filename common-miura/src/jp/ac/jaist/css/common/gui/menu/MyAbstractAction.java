package jp.ac.jaist.css.common.gui.menu;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;

public class MyAbstractAction extends AbstractAction {
	boolean isToggle;
	public boolean isSelected;
	private static final long serialVersionUID = -2253928255968178345L;
	/**
	 * isToggleを省略すると，falseになる
	 * @param text
	 * @param icon
	 * @param desc
	 * @param mnemonic
	 */
	
	public MyAbstractAction(String text, ImageIcon icon,
            String desc, Integer mnemonic) {
		this(text,icon,desc,mnemonic,false);
	}

	public MyAbstractAction(String text, ImageIcon icon,
                      String desc, Integer mnemonic, boolean isToggle) {
        super(text, icon);
        putValue(SHORT_DESCRIPTION, desc);
        putValue(MNEMONIC_KEY, mnemonic);
        if (isToggle) {
        	putValue("toggle","true");
        	this.isToggle = isToggle;
        }
    }
    
	public void actionPerformed(ActionEvent e) {
    	System.out.println("super.action");
    	if (isToggle){
    		if (e.getSource() instanceof JCheckBoxMenuItem){
    			JCheckBoxMenuItem jmi = (JCheckBoxMenuItem) e.getSource();
    			setSelected(jmi.isSelected());
    		}
    		if (e.getSource() instanceof JToggleButton){
    			JToggleButton jtb = (JToggleButton) e.getSource();
    			setSelected(jtb.isSelected());		
    		}
    	}    		
    }
	@SuppressWarnings("unchecked")
    public void setSelected(boolean b){
    	isSelected = b;
    	
    	JToggleButton jtb = (JToggleButton) getValue("togglebutton");
		if (jtb != null) jtb.setSelected(isSelected);
		
		JCheckBoxMenuItem jmi = (JCheckBoxMenuItem) getValue("togglemenu");
		if (jmi != null) jmi.setSelected(isSelected);
		
//		 更新対象がリストの場合
		ArrayList<JToggleButton> jtbal = (ArrayList<JToggleButton>) getValue("togglebuttonList");
		if (jtbal != null) for(JToggleButton jtb0: jtbal) if (jtb0!=null) jtb0.setSelected(isSelected);
		ArrayList<JCheckBoxMenuItem> jmial = (ArrayList<JCheckBoxMenuItem>) getValue("togglemenuList");
		if (jmial != null) for(JCheckBoxMenuItem jmi0: jmial) if(jmi0!=null) jmi0.setSelected(isSelected);
    }
    /**
     * 未使用
     * @param e
     */
    public void itemStateChanged(ChangeEvent e){
    }
	public boolean isSelected() {		
		return isSelected;
	}
    
}
