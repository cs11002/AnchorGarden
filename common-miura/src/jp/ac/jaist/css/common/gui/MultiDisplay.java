package jp.ac.jaist.css.common.gui;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.util.ArrayList;

public class MultiDisplay {
	public static ArrayList<Rectangle> getMultiDisplayRectList(){
		ArrayList<Rectangle> displaySizeList = new ArrayList<Rectangle>();
		
		GraphicsDevice[] devices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
		for(GraphicsDevice gd: devices){
			System.out.println(gd.getIDstring());
			GraphicsConfiguration gc = gd.getDefaultConfiguration();
			Rectangle rec = gc.getBounds();
			displaySizeList.add(rec);
		}
		return displaySizeList;
	}
	
	
}
