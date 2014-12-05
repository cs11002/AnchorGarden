package jp.ac.jaist.css.common.piccolo;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Stroke;
import java.io.Serializable;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;

public class PNodeStyle implements Serializable {

	private static final long serialVersionUID = -8647155879026117213L;

	String name;

	// PText
	Paint textPaint; // setTextPaint

	String fontName;

	int fontStyle;

	int fontSize;

	Font font;

	boolean isTextApplicable;

	// PPath
	Paint strokePaint;

	Stroke stroke;

	boolean isPathApplicable;

	/**
	 * ノードにスタイルを適用
	 * 
	 * @param node
	 */
	public void apply(PNode node) {
		if (node instanceof PText && isTextApplicable) {
			PText text = (PText) node;
			text.setFont(font);
			text.setTextPaint(textPaint);
		} else if (node instanceof PPath && isPathApplicable) {
			PPath path = (PPath) node;
			path.setStroke(stroke);
			path.setStrokePaint(strokePaint);
		}
	}

	public PNodeStyle() {
		name = "style1";
		textPaint = Color.BLACK;
		fontName = "SansSerif";
		fontSize = 30;
		fontStyle = Font.PLAIN;
		strokePaint = Color.BLACK;
		isPathApplicable = true;
		isTextApplicable = true;
	}
}
