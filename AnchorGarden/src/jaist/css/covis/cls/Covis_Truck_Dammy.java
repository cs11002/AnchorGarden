package jaist.css.covis.cls;

import jaist.css.covis.CoVisBuffer;

import java.awt.Color;

import edu.umd.cs.piccolo.nodes.PPath;

public class Covis_Truck_Dammy extends Covis_Object {

	private static final long serialVersionUID = -6598210781722242878L;
	public static Color defaultColor = Covis_Truck.defaultColor;
	public Covis_Truck_Dammy(CoVisBuffer buf, boolean isAuto){
		super(buf, isAuto);
		color = defaultColor;
		setPaint(color);
		setStroke(basicStroke);
	}
	public Covis_Truck_Dammy(Color c, CoVisBuffer buf, boolean isAuto){
		super(c, buf, isAuto);
	}

	PPath sup;
	PPath sub;
	public void init(boolean isAuto){
		// 大きさ・形指定
		sup = new PPath();
		sup.setPathToRectangle(0, 0, 100, 25);
		sup.setPaint(Covis_Car.defaultColor);
		sup.setStroke(basicStroke);
		addChild(sup);
		
		sub = new PPath();
		sub.setPathToRectangle(0,25,100,25);
		sub.setPaint(defaultColor);
		sub.setStroke(basicStroke);
		addChild(sub);
	}
}
