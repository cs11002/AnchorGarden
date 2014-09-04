package jp.ac.jaist.css.common.gui.menu;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import jp.ac.jaist.css.common.data.KeepOrderHash;

public class AbstractMenuActions {
	protected KeepOrderHash<String,ArrayList<Action>> menurepos; // "File"=> ArrayList
	protected Hashtable<String,Action> name2act;
	public AbstractMenuActions(){
		menurepos = new KeepOrderHash<String,ArrayList<Action>>();
		name2act = new Hashtable<String, Action>();
	}
	
	public void addAction(String type, Action a){
		ArrayList<Action> al = menurepos.get(type);
		if (al == null){
			al = new ArrayList<Action>();
			al.add(a);
			menurepos.put(type, al);
		} else {
			al.add(a);
		}
		name2act.put((String)a.getValue(Action.NAME), a);
	}
	
	public JMenuBar getMenuBar(){
		JMenuItem menuItem = null;
        JMenuBar menuBar;

        //Create the menu bar.
        menuBar = new JMenuBar();

        for(String type: menurepos.getIterator()){
        	JMenu mainMenu = new JMenu(type);
        	for(Action a: menurepos.get(type)){
        		if (a.getValue("toggle") != null){
        			menuItem = new JCheckBoxMenuItem(a);
        			a.putValue("togglemenu", menuItem);
				} else {
					menuItem = new JMenuItem(a);
				}
                menuItem.setIcon(null); //arbitrarily chose not to use icon
                mainMenu.add(menuItem);
        	}
            menuBar.add(mainMenu);
        }
        return menuBar;
	}
	public JToolBar getToolBar() {
		AbstractButton button = null;
		
		//Create the toolbar.
		JToolBar toolBar = new JToolBar();
		
		for(String type: menurepos.getIterator()){
			for(Action a: menurepos.get(type)){
				if (a.getValue("toggle") != null){
					button = new JToggleButton(a);
					a.putValue("togglebutton", button);
				} else {
					button = new JButton(a);
				}
				if (button.getIcon() != null) {
//					button.setText(""); //an icon-only button
				}
				toolBar.add(button);
			}
		}
		return toolBar;
	}
	
	/** Returns an ImageIcon, or null if the path was invalid. */
    public static ImageIcon createNavigationIcon(String imageName) {
        String imgLocation = "toolbarButtonGraphics/"
                             + imageName
                             + ".gif";
        
//        java.net.URL imageURL = maincls.getResource(imgLocation);
        ClassLoader cl = AbstractMenuActions.class.getClassLoader();

        if (cl == null) {
            System.err.println("Resource not found: "
                               + imgLocation);
            return null;
        } else {
            return new ImageIcon(cl.getResource(imgLocation));
        }
    }

}

