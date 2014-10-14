package jaist.css.covis.cls;

import jaist.css.covis.CoVisBuffer;
import java.awt.Color;
import edu.umd.cs.piccolo.nodes.PPath;

public class Covis_Sub_Dammy extends Covis_Object {

	private static final long serialVersionUID = 8131819427660903628L;
	public static Color defaultColor = Covis_Sub.defaultColor;
	public Covis_Sub_Dammy(CoVisBuffer buf, boolean isAuto){
		super(buf, isAuto);
		color = defaultColor;
		setPaint(color);
		setStroke(basicStroke);
	}
	public Covis_Sub_Dammy(Color c, CoVisBuffer buf, boolean isAuto){
		super(c, buf, isAuto);
	}

	PPath sup;
	PPath sub;
	public void init(boolean isAuto){
		// ëÂÇ´Ç≥ÅEå`éwíË
		sup = new PPath();
		sup.setPathToRectangle(0, 0, 100, 25);
		sup.setPaint(Covis_Super.defaultColor);
		addChild(sup);
		
		sub = new PPath();
		sub.setPathToRectangle(0,25,100,25);
		sub.setPaint(defaultColor);
		addChild(sub);
	}

}