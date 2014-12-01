package jaist.css.covis.cls;

import jaist.css.covis.CoVisBuffer;

import java.awt.Color;

import edu.umd.cs.piccolo.nodes.PPath;

public class Covis_Cat_Dammy extends Covis_Object {

	private static final long serialVersionUID = -4640109089745115102L;
	public static Color defaultColor = Covis_Cat.defaultColor;
	public Covis_Cat_Dammy(CoVisBuffer buf, boolean isAuto){
		super(buf, isAuto);
		color = defaultColor;
		setPaint(color);
		setStroke(basicStroke);
	}
	public Covis_Cat_Dammy(Color c, CoVisBuffer buf, boolean isAuto){
		super(c, buf, isAuto);
	}

	PPath sup;
	PPath sub;
	public void init(boolean isAuto){
		// ëÂÇ´Ç≥ÅEå`éwíË
		sup = new PPath();
		sup.setPathToRectangle(0, 0, 100, 25);
		sup.setPaint(Covis_Animal.defaultColor);
		sup.setStroke(basicStroke);
		addChild(sup);
		
		sub = new PPath();
		sub.setPathToRectangle(0,25,100,25);
		sub.setPaint(defaultColor);
		sub.setStroke(basicStroke);
		addChild(sub);
	}
}
