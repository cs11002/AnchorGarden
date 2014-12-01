package jaist.css.covis.cls;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.geom.RoundRectangle2D;

import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;

public class MessageBoard extends PPath {

	private static final long serialVersionUID = 8131819427660903628L;
	public static Color defaultColor = new Color(135,206,250);
	
	public MessageBoard(String labeltxet){
		init(0,0,labeltxet);
	}
	
	public MessageBoard(double x, double y, String labeltxet){
		init(x,y,labeltxet);
	}
	
	PPath triangle;
	PText label;
	
	public void init(double x, double y, String labeltxet){
		//表示するテキスト作成
		String labeltxet2[];
		//labeltxetから無駄な部分を切り取る
		labeltxet2 = labeltxet.split(".* ");
		//必要な部分を先頭に
		if(labeltxet2.length >= 2) {
			labeltxet2[0] = labeltxet2[1];
		}
		label = new PText(labeltxet2[0]);
		label.scale(2.8f);
		label.offset(20, 20);
		addChild(label);
		
		//吹き出しの四角い部分作成
		setOffset(x, y);
		setPathTo(new RoundRectangle2D.Double(0, 0, 30+labeltxet2[0].getBytes().length*18, 100, 30, 30));
		setPaint(defaultColor);
		setStrokePaint(Color.lightGray);
		setStroke(new BasicStroke(1));

		//吹き出しの三角形を作成
		int x2 = (int)x;
		int y2 = (int)y;
		
		triangle = new PPath();
		int[] pX = { x2 + 40, x2 + 40, x2 + 40 + 40 };
		int[] pY = { y2 + 100 - 2, y2 + 100 + 40 - 2, y2 + 100 - 2};
		triangle.setPathTo(new Polygon(pX, pY, 3));
		triangle.setPaint(defaultColor);
		triangle.setStrokePaint(Color.lightGray);
		triangle.setStroke(new BasicStroke(1));
		addChild(triangle);
	}

}