package jp.ac.jaist.css.common.geom;

import java.awt.geom.Point2D;

/**
 * ��
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
	 * p=null�Ȃ�get(0)�̃m�[�h��Ԃ��Dp������̃m�[�h�Ȃ�C�����̃m�[�h���A���Dp���ǂ���ɂ����݂��Ȃ����null��Ԃ��D
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
		py = p1.getY(); // a == 0 �̂Ƃ��̂ݎg���D
	}
	
	/**
	 * ������p����C���̒����ɂ��낵�������Ƃ̌�_
	 * @param p
	 * @return
	 */
	public Point2D foot(Point2D p){
		update(); 
		return calcfootPoint(a,b,c,py,p);
	}
	/**
	 * ���� la x + lb y + lc = 0 �ɁC�_p ����~�낵�������̌�_��Ԃ��D�������Clpy �͌��̒����̒[�_��y���W�i�ǂ���ł��悢�j
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
	 * ���̒����ɁCp���炨�낵�������̋�����Ԃ��D
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
	 * �C�ӂ̓_p��ʂ�C����MyLine�ƕ��s�Ȓ����ƁCMyLine��̓_onp��ʂ�MyLine�̐����Ƃ̌�_��Ԃ��D
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
		return foot(origP); // ���̏ꍇ�́Cfoot()���g���Έꔭ�łł���D
	}
}
