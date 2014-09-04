package jp.ac.jaist.css.common.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.Timer;

public class ButtonInvokeLater implements ActionListener {

	AbstractButton button;
	Timer timer;
	boolean repeat;
	public ButtonInvokeLater(AbstractButton ab, int msec, boolean _repeat) {
		button = ab;
		repeat = _repeat;
		timer = new Timer(msec, this);
		timer.start();
	}

	public void actionPerformed(ActionEvent e) {
		button.doClick();
		System.out.println("Click "+button.getText());
		if (!repeat) timer.stop();
	}
}
