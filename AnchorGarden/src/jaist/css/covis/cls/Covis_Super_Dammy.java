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

import edu.umd.cs.piccolo.nodes.PText;

public class Covis_Super_Dammy extends Covis_Object {

	private static final long serialVersionUID = 8131819427660903628L;
	public static Color defaultColor = Covis_Super.defaultColor;
	public Covis_Super_Dammy(CoVisBuffer buf, boolean isAuto){
		super(buf, isAuto);
		color = defaultColor;
		setPaint(color);
		setStroke(basicStroke);
	}
	public Covis_Super_Dammy(Color c, CoVisBuffer buf, boolean isAuto){
		super(c, buf, isAuto);
	}

	public void init(boolean isAuto){
		//ëÂÇ´Ç≥ÅEå`éwíË
		setPathToRectangle(0, 0, 100,50);
	}

}