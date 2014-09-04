package jp.ac.jaist.css.common.gui;

import java.awt.Component;

import javax.swing.JFrame;

public class GuiUtil {
	/**
	 * �ċA�I�ɐe��{���CJFrame��T��
	 * @param c �R���|�[�l���g
	 * @return �eJFrame
	 */
	public static JFrame getFrame(Component c){
		Component target = c.getParent();
		while(true){
			if (target instanceof JFrame){
				return (JFrame)target;
			} else {
				target = c.getParent();
			}
		}
	}
}
