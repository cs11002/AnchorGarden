package jp.ac.jaist.css.common.geom;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Geometry {
	/**
	 * 内分点・中点
	 * @param m1
	 * @param m2
	 * @param m
	 * @param n
	 * @return
	 */
	public static Point2D internally_dividing_point(Point2D m1, Point2D m2, int m, int n){
		double dm = (double)m;
		double dn = (double)n;
		double mplusn = dm+dn;
		double x = m1.getX() + ((m2.getX() - m1.getX()) * dm)/mplusn;
		double y = m1.getY() + ((m2.getY() - m1.getY()) * dm)/mplusn;
		return new Point2D.Double(x,y);
	}
	
	/* pts = { x1 y1 x2 y2 x3 y3 } of control points */
	public static Point2D getPointOnCurve(double rate, float[] pts){
		Point2D m1 = internally_dividing_point(pts[0], pts[1], pts[2], pts[3], rate);
		Point2D m2 = internally_dividing_point(pts[2], pts[3], pts[4], pts[5], rate);
		return internally_dividing_point(m1.getX(), m1.getY(), m2.getX(), m2.getY(), rate);
	}
	public static Point2D internally_dividing_point(double x1, double y1, double x2, double y2, double rate){
		double x = x1 + (x2-x1)*rate;
		double y = y1 + (y2-y1)*rate;
		return new Point2D.Double(x,y);
	}
	//8方位、上下左右斜めのradius離れた点列を、basketに追加するメソッド
	public static void addSurroundingPoints(Point2D centerP, double radius, ArrayList<Point2D> basket){
		double squareradius = radius / Math.sqrt(2.0);
		basket.add(new Point2D.Double(centerP.getX(), centerP.getY()-radius));//ue
		basket.add(new Point2D.Double(centerP.getX()-radius, centerP.getY()));//hidari
		basket.add(new Point2D.Double(centerP.getX(), centerP.getY()+radius));//sita
		basket.add(new Point2D.Double(centerP.getX()+radius, centerP.getY()));//migi
		basket.add(new Point2D.Double(centerP.getX()-squareradius, centerP.getY()-squareradius));
		basket.add(new Point2D.Double(centerP.getX()-squareradius, centerP.getY()+squareradius));
		basket.add(new Point2D.Double(centerP.getX()+squareradius, centerP.getY()+squareradius));
		basket.add(new Point2D.Double(centerP.getX()+squareradius, centerP.getY()-squareradius));
	}
	public static Point2D addToPoint(Point2D orig, Point2D parent, Point2D p1, Point2D p2){
		return new Point2D.Double(orig.getX()+parent.getX()+p2.getX()-p1.getX(), orig.getY()+parent.getY()+p2.getY()-p1.getY());
	}
	public static ArrayList<Point2D> filterOut(ArrayList<Point2D> pts, double limitDist){
		ArrayList<Point2D> ret = new ArrayList<Point2D>();
		for(Point2D p: pts){
			boolean fflag = false;
			for(int i=0; i<ret.size();i++){
				double dist = ret.get(i).distance(p);
				if (dist < limitDist){
					fflag = true;
				}
			}
			if (!fflag) ret.add(p);
		}
		return ret;
	}
	
	/**
	 * ２つの点を結ぶ線のなす角度（点の順番によっては，マイナスの角度になる）
	 * @param m1
	 * @param m2
	 * @return ラジアン
	 */
	public static double getAngle(Point2D m1, Point2D m2){
		double dx = m2.getX()-m1.getX();
		double dy = m2.getY()-m1.getY();
		return -Math.atan2(dy, dx);
	}
	
	//なす角のcosを求める．1なら反復，-1なら直進
	public static double getAngle(Point2D m1, Point2D m2, Point2D m3){
		double a = m1.distance(m2);
		double b = m2.distance(m3);
		double c = m3.distance(m1);
		double cosC = (a*a+b*b-c*c)/(2*a*b);
//		System.out.println("a "+a+"  b "+b+"  c "+c+"  cosC "+cosC);
		return cosC;
	}
	public static double getAngle(double x1,double y1,double x2,double y2,double x3,double y3){
		Point2D p1 = new Point2D.Double(x1,y1);
		Point2D p2 = new Point2D.Double(x2,y2);
		Point2D p3 = new Point2D.Double(x3,y3);
		return getAngle(p1,p2,p3);
	}
	public static double rad2deg(double rad){
		double deg = rad * 180 / Math.PI;
		return deg;
	}
	
	public static double checkClockwise(Point2D m1, Point2D m2, Point2D m3){
		double ret = (m2.getX()-m1.getX())*(m3.getY()-m2.getY())-(m2.getY()-m1.getY())*(m3.getX()-m2.getX());
		return ret;
	}
	
	//取り消し線（反復線）かどうかの判別
	public static int calcWWShape(float[] x, float[] y){
		ArrayList<Double> vcount = new ArrayList<Double>();
		int len = x.length;
		for(int i=1;i<len-1;i++){
			double d = getAngle(x[i-1], y[i-1], x[i], y[i], x[i+1], y[i+1]);
			if (d > 0.88){
				vcount.add(d);
//				System.out.println("["+vcount.size()+"] "+d+" at i="+i+" / "+len);
			}
		}		
		return vcount.size();
	}
	public static void main(String[] s){
		Point2D p1 = new Point2D.Double(0,0);
		Point2D p2 = new Point2D.Double(1,0);
		Point2D p3 = new Point2D.Double(0,0.4);
		System.out.println(getAngle(p1,p2,p3));
	}
}
