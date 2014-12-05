package jp.ac.jaist.css.common.geom;

import java.awt.geom.Point2D;

/**
 * 線
 * @author miuramo
 *
 */
public class MyLine {
	private static final long serialVersionUID = 2708106376203095524L;
	Point2D p1, p2;
	double a,b,c,py;
	Point2D origin;
	double len;
	public MyLine(Point2D mp1, Point2D mp2){
		p1 = mp1;
		p2 = mp2;
		origin = new Point2D.Double(mp2.getX()-mp1.getX(), mp2.getY()-mp1.getY());
		len = p2.distance(p1);
	}
	public double angleWith(MyLine ml){
		double cos = (this.origin.getX()*ml.origin.getX()+this.origin.getY()*ml.origin.getY())/(this.len*ml.len);
		return cos;
	}
	
	/**
	 * p=nullならget(0)のノードを返す．pが一方のノードなら，他方のノードを帰す．pがどちらにも存在しなければnullを返す．
	 * @param p
	 * @return
	 */	
	public Point2D otherTerminatePoint(Point2D p){
		if (p == p1) return p2; else return p1;
	}
	public void update(){
		
		c = p1.getX() * p2.getY() - p2.getX() * p1.getY();
		b = p2.getX() - p1.getX();
		a = p1.getY() - p2.getY();
		py = p1.getY(); // a == 0 のときのみ使う．
	}
	
	/**
	 * 引数のpから，この直線におろした垂線との交点
	 * @param p
	 * @return
	 */
	public Point2D foot(Point2D p){
		update(); 
		return calcfootPoint(a,b,c,py,p);
	}
	/**
	 * 直線 la x + lb y + lc = 0 に，点p から降ろした垂線の交点を返す．ただし，lpy は元の直線の端点のy座標（どちらでもよい）
	 * @param la
	 * @param lb
	 * @param lc
	 * @param lpy
	 * @param p
	 * @return
	 */
	public Point2D calcfootPoint(double la, double lb, double lc, double lpy, Point2D p){
		double x,y;
		if (la == 0){
			x = p.getX();
			y = lpy;
		} else {
			double ld = lb * p.getX() - la * p.getY();
			double bumbo = la * la + lb * lb;
			x = (lb * ld - la * lc)/bumbo;
			y = (lb * x - ld)/ la;
		}
		return new Point2D.Double(x,y);
	}
	/**
	 * この直線に，pからおろした垂線の距離を返す．
	 * @param p
	 * @return
	 */
	public double distance(Point2D p){
		update();
		double temp1 = a * p.getX() + b * p.getY() + c;
		double temp2 = Math.sqrt(a*a + b*b);
		double dist = Math.abs(temp1)/ temp2;
		return dist;
	}
	/**
	 * 任意の点pを通り，このMyLineと平行な直線と，MyLine上の点onpを通るMyLineの垂線との交点を返す．
	 * @param p
	 * @param onp
	 * @return
	 */
	public Point2D parallelMap(Point2D p, Point2D onp){
		update();
		double d = -a * p.getX() -b * p.getY();
		return calcfootPoint(a,b,d,p.getY(),onp);
	}

	public Point2D getPointOnLine(Point2D.Double origP){
		return foot(origP); // 線の場合は，foot()を使えば一発でできる．
	}
}
