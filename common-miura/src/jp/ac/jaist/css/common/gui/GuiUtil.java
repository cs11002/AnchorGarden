package jp.ac.jaist.css.common.gui;

import java.awt.Component;

import javax.swing.JFrame;

public class GuiUtil {
	/**
	 * 再帰的に親を捜し，JFrameを探す
	 * @param c コンポーネント
	 * @return 親JFrame
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
