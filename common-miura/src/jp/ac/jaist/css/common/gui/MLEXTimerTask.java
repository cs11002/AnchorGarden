package jp.ac.jaist.css.common.gui;

import java.util.TimerTask;

public class MLEXTimerTask extends TimerTask{
	int button;
	MouseListenerEx ex;
	int msec;
	MouseDelegator md;
	String desc;
	public MLEXTimerTask(int _button, int _msec, MouseListenerEx _ex, MouseDelegator _md, String _desc){
		button = _button;
		ex = _ex;
		msec = _msec;
		md = _md;
		desc = _desc;
	}
	public void run(){
		ex.timePassed(button, msec, md, desc);
	}
}
