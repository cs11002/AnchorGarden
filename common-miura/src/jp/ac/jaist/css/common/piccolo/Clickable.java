package jp.ac.jaist.css.common.piccolo;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.event.PInputEvent;

public interface Clickable {
	public void clicked(PInputEvent e, PCanvas _canvas);
	public void doubleClicked(PInputEvent e, PCanvas _canvas);
}
