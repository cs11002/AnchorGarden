package jaist.css.covis.cls;

import jaist.css.covis.CoVisBuffer;

import java.awt.Color;

public class Covis_UnCapAccount_Dammy extends Covis_Object {

	private static final long serialVersionUID = -3931864299098001177L;
	public static Color defaultColor = Covis_UnCapAccount.defaultColor;
	public Covis_UnCapAccount_Dammy(CoVisBuffer buf, boolean isAuto){
		super(buf, isAuto);
		color = defaultColor;
		setPaint(color);
		setStroke(basicStroke);
	}
	public Covis_UnCapAccount_Dammy(Color c, CoVisBuffer buf, boolean isAuto){
		super(c, buf, isAuto);
	}

	public void init(boolean isAuto){
		//ëÂÇ´Ç≥ÅEå`éwíË
		setPathToRectangle(0, 0, 100,50);
	}

}
