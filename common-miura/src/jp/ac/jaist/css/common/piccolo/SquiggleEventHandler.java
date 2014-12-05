package jp.ac.jaist.css.common.piccolo;

import java.awt.BasicStroke;
import java.awt.geom.Point2D;

import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.event.PDragSequenceEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PPath;

public class SquiggleEventHandler extends PDragSequenceEventHandler {
	protected PPath squiggle;

	protected PLayer layer;

	public SquiggleEventHandler(PLayer l) {
		layer = l;
	}

	public void startDrag(PInputEvent e) {
		super.startDrag(e);

		Point2D p = e.getPosition();

		squiggle = new PPath();
		squiggle.moveTo((float) p.getX(), (float) p.getY());
		squiggle.setStroke(new BasicStroke((float) (1 / e.getCamera()
				.getViewScale()),BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 90.0f));
		layer.addChild(squiggle);
	}

	public void drag(PInputEvent e) {
		super.drag(e);
		updateSquiggle(e);
	}

	public void endDrag(PInputEvent e) {
		super.endDrag(e);
		updateSquiggle(e);
		squiggle = null;
	}

	public void updateSquiggle(PInputEvent aEvent) {
		Point2D p = aEvent.getPosition();
		squiggle.lineTo((float) p.getX(), (float) p.getY());
	}
}
