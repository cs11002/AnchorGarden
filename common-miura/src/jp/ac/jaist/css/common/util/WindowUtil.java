package jp.ac.jaist.css.common.util;

import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.JFrame;

import jp.ac.jaist.css.common.gui.MultiDisplay;

public class WindowUtil {
	public static void snappingWindow(JFrame f, int pos){
		ArrayList<Rectangle> rects = MultiDisplay.getMultiDisplayRectList();
		Rectangle rmain = rects.get(0);
		switch(pos){
		case 1://ç∂è„
			f.setLocation(0,0);
			break;
		case 2://âEè„
			f.setLocation(rmain.width - f.getWidth(), 0);
			break;
		case 3://ç∂â∫
			f.setLocation(0, rmain.height - f.getHeight() -30);
			break;
		case 4://âEâ∫
			f.setLocation(rmain.width - f.getWidth(), rmain.height - f.getHeight() - 30);
			break;
		case 5://è„
			f.setLocation((rmain.width - f.getWidth())/2, 0);
			break;
		case 6://âE
			f.setLocation(rmain.width - f.getWidth(), (rmain.height - f.getHeight() - 30)/2);
			break;
		case 7://â∫
			f.setLocation((rmain.width - f.getWidth())/2, rmain.height - f.getHeight() - 30);
			break;
		case 8://ç∂
			f.setLocation(0, (rmain.height - f.getHeight() - 30)/2);
			break;
		}
	}
}
