package jaist.css.covis.cls;

import jaist.css.covis.CoVisBuffer;
import jaist.css.covis.JLabelW;
import jaist.css.covis.SrcWindow;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;

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