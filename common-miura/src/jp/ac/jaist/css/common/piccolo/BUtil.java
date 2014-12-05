package jp.ac.jaist.css.common.piccolo;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.TreeMap;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.activities.PActivity;
import edu.umd.cs.piccolo.util.PBounds;

public class BUtil {
	public static int getArea(PBounds pb) {
		double w = pb.getWidth();
		double h = pb.getHeight();
		return (int) (w * h);
	}

	public static PBounds zoomBounds(PBounds pb, double rate) {
		double x = pb.getX();
		double y = pb.getY();
		double w = pb.getWidth();
		double h = pb.getHeight();
		double nw = w * rate;
		double nh = h * rate;
		double nx = x - (nw - w) / 2;
		double ny = y - (nh - h) / 2;
		return new PBounds(nx, ny, nw, nh);
	}

	public static PBounds zoomBoundsX(PBounds pb, double rate) {
		double x = pb.getX();
		double y = pb.getY();
		double w = pb.getWidth();
		double h = pb.getHeight();
		double nw = w * rate;
		double nh = h;
		double nx = x - (nw - w) / 2;
		double ny = y - (nh - h) / 2;
		return new PBounds(nx, ny, nw, nh);
	}

	public static PBounds plusBounds(PBounds pb, int plusx, int plusy) {
		double x = pb.getX();
		double y = pb.getY();
		double w = pb.getWidth();
		double h = pb.getHeight();
		double nw = w + plusx;
		double nh = h + plusy;
		double nx = x - (nw - w) / 2;
		double ny = y - (nh - h) / 2;
		return new PBounds(nx, ny, nw, nh);
	}

	public static PBounds plusBounds(Rectangle2D pb, int plusx, int plusy) {
		double x = pb.getX();
		double y = pb.getY();
		double w = pb.getWidth();
		double h = pb.getHeight();
		double nw = w + plusx;
		double nh = h + plusy;
		double nx = x - (nw - w) / 2;
		double ny = y - (nh - h) / 2;
		return new PBounds(nx, ny, nw, nh);
	}

	// pos = {0, 1, 2, 3 } = {full, top, middle, bottom};
	public static PBounds halfBounds(PBounds pb, int pos) {
		double x = pb.getX();
		double y = pb.getY();
		double w = pb.getWidth();
		double h = pb.getHeight();
		if (pos == 0) {
			return pb;
		}
		if (pos == 1) {
			double nx = x;
			double ny = y;
			double nw = w;
			double nh = h / 2;
			return new PBounds(nx, ny, nw, nh);
		}
		if (pos == 2) {
			double nx = x;
			double ny = y + h / 4;
			double nw = w;
			double nh = h / 2;
			return new PBounds(nx, ny, nw, nh);
		}
		if (pos == 3) {
			double nx = x;
			double ny = y + h / 2;
			double nw = w;
			double nh = h / 2;
			return new PBounds(nx, ny, nw, nh);
		}
		return null;
	}

	public static PBounds explainBounds(PBounds firstpb, PBounds panelpb,
			PBounds cameraview, double width) {
		double fpbw = firstpb.width;
		double zoom = width / fpbw;
		PBounds tempb = zoomBoundsX(firstpb, zoom);

		double preferredheight = width * cameraview.height / cameraview.width;

		double tx = tempb.x;
		double ty = tempb.y;
		double tw = tempb.width;
		double th = tempb.height;
		double px = panelpb.x;
		double py = panelpb.y;
		double pw = panelpb.width;
		double ph = panelpb.height;
		if (tx < px) {
			tx = px;
		}
		if (px + pw < tx + tw) {
			tx = (px + pw) - tw;
		}
		double tcy = ty + th / 2;
		ty = tcy - preferredheight / 2;
		th = preferredheight;
		if (ty < py) {
			ty = py;
		}
		if (py + ph < ty + th) {
			ty = (py + ph) - th;
		}
		return new PBounds(tx, ty, tw, th);
	}

	/**
	 * 引数で与えられた同じサイズの箱を，大きな箱の縦横比をみながら
	 * レイアウトする
	 */
	public static int fitBoxesToBounds(ArrayList<PNode> boxes, PBounds parent, double xgap, double ygap){
		int num = boxes.size();
		int sqrtnum = (int) Math.sqrt(num);
		double ratediff = Double.MAX_VALUE;
		double parentrate = parent.getWidth()/parent.getHeight();
//		System.out.println("Parent "+parentrate);
		TreeMap<Double,Integer> resultRates = new TreeMap<Double,Integer>();

		while(true){
			double fitrate = fitrate(boxes, sqrtnum, xgap, ygap);
			resultRates.put(Math.abs(parentrate - fitrate), sqrtnum);
//			System.out.println(" "+fitrate);
			if (ratediff < Math.abs(parentrate - fitrate)) break;
			if (boxes.size()<2) break;
			ratediff = Math.abs(parentrate - fitrate);
			if (fitrate < parentrate){
				sqrtnum++;
			} else {
				sqrtnum--;
			}
		}
		ArrayList<Integer> intres = new ArrayList<Integer>(resultRates.values());
		return intres.get(0);

	}
	static double fitrate(ArrayList<PNode> boxes, int horizontalNum, double xgap, double ygap){	
		double x = 0, y = 0, w = 0, h = 0;
		int horizonCount = 0;
//		System.out.print("Try "+horizontalNum);
		for(PNode pn: boxes){
			x += pn.getFullBoundsReference().getWidth() + xgap;
			if (y < pn.getFullBoundsReference().getHeight()) y = pn.getFullBoundsReference().getHeight();
			horizonCount++;
			if (horizonCount == horizontalNum){
				h += y + ygap;
				if (w < x) w = x;
				horizonCount = 0;
				y = 0;
				x = 0;
			}
		}
		h += y + ygap;
		if (w < x) w = x;
		return w/h;	
	}
	public static ArrayList<PActivity> layout(ArrayList<PNode> boxes, int horizontalNum, double xgap, double ygap, int animsec, PBounds returnPB){
		ArrayList<PActivity> palist = new ArrayList<PActivity>();

		double x = 0, y = 0, ymax = 0, h = 0;
		int horizonCount = 0;
		for(PNode pn: boxes){
			PActivity pa = pn.animateToPositionScaleRotation(x, y, 1, 0, animsec);
//			System.out.println("x y "+x+" "+y);
			palist.add(pa);
			if (returnPB != null) {
				PBounds aPB = new PBounds(pn.getFullBoundsReference());
				aPB.setOrigin(x,y);
				returnPB.add(aPB);
			}
			x += pn.getFullBoundsReference().getWidth() + xgap;
			if (ymax < pn.getFullBoundsReference().getHeight()) ymax = pn.getFullBoundsReference().getHeight();
			horizonCount++;
			if (horizonCount == horizontalNum){
				horizonCount = 0;
				h = h + ymax + ygap;
				y = h;
				x = 0;
				ymax = 0;
			}
		}
		return palist;
	}

	public static ArrayList<PActivity> layout(ArrayList<PNode> boxes, int horizontalNum, double xgap, double ygap, int animsec, PBounds returnPB, int xoffset, int yoffset){
		ArrayList<PActivity> palist = new ArrayList<PActivity>();

		double x = xoffset, y = yoffset, ymax = 0, h = 0;
		int horizonCount = 0;
		for(PNode pn: boxes){
			PActivity pa = pn.animateToPositionScaleRotation(x, y, 1, 0, animsec);
//			System.out.println("x y "+x+" "+y);
			palist.add(pa);
			if (returnPB != null) {
				PBounds aPB = new PBounds(pn.getFullBoundsReference());
				aPB.setOrigin(x,y);
				returnPB.add(aPB);
			}
			x += pn.getFullBoundsReference().getWidth() + xgap;
			if (ymax < pn.getFullBoundsReference().getHeight()) ymax = pn.getFullBoundsReference().getHeight();
			horizonCount++;
			if (horizonCount == horizontalNum){
				horizonCount = 0;
				h = h + ymax + ygap;
				y = h + yoffset;
				x = xoffset;
				ymax = 0;
			}
		}
		return palist;
	}	
	public static Point2D addPoints(Point2D... origins){
		double x = 0, y = 0;
		for(Point2D p : origins){
			x += p.getX();
			y += p.getY();
		}
		return new Point2D.Double(x,y);
	}

	public static Point2D getTopRight(PBounds pb) {
		double x = pb.getX();
		double y = pb.getY();
		x += pb.getWidth();
		return new Point2D.Double(x,y);
	}

	public static Point2D getBottomLeft(PBounds pb, double xyplus) {
		double x = pb.getX();
		double y = pb.getY();
		y += pb.getHeight();
		x -= xyplus;
		y += xyplus;
		return new Point2D.Double(x,y);
	}

	public static Point2D getBottomRight(PBounds pb, double xyplus) {
		double x = pb.getX();
		double y = pb.getY();
		x += pb.getWidth();
		y += pb.getHeight();
		x += xyplus;
		y += xyplus;
		return new Point2D.Double(x,y);
	}
	public static Point2D getBottomCenter(PBounds pb, double yplus) {
		double x = pb.getX();
		double y = pb.getY();
		x += pb.getWidth()/2;
		y += pb.getHeight() + yplus;
		return new Point2D.Double(x,y);
	}
	public static Point2D getBottom1_3(PBounds pb, double yplus) {
		double x = pb.getX();
		double y = pb.getY();
		x += pb.getWidth()/3;
		y += pb.getHeight() + yplus;
		return new Point2D.Double(x,y);
	}
	public static Point2D getBottom2_3(PBounds pb, double yplus) {
		double x = pb.getX();
		double y = pb.getY();
		x += pb.getWidth()*2/3;
		y += pb.getHeight() + yplus;
		return new Point2D.Double(x,y);
	}
	public static Point2D getLeftPart(PBounds pb, double rate, double xminus) {
		double x = pb.getX();
		double y = pb.getY();
		x -= xminus;
		y += pb.getHeight()*rate;
		return new Point2D.Double(x,y);
	}
	public static Point2D getRightPart(PBounds pb, double rate, double xplus) {
		double x = pb.getX();
		double y = pb.getY();
		x += pb.getWidth() + xplus;
		y += pb.getHeight()*rate;
		return new Point2D.Double(x,y);
	}

	public static ArrayList<Point2D> matrixPointsInDirection(double unitw, double unith, 
			double topcx, double topcy,
			int xcol, int yrow, int direction /*0,1,2,3*/){
		ArrayList<Point2D> ret = new ArrayList<Point2D>();

		double topw = unitw * xcol;
		double toph = unith * yrow;
		double topx = topcx - topw/2;
		double topy = topcy - toph/2;
		
		if (direction%2==1){
			int tmp = yrow;
			yrow = xcol;
			xcol = tmp;
//			double tmpd = toph;
//			toph = topw;
//			topw = tmpd;
			topx = topcx - (toph/2);
			topy = topcy - (topw/2);
//			tmpd = unith;
//			unith = unitw;
//			unitw = tmpd;
		}

		if (direction==0){
			for(int j=0;j<yrow;j++){
				for(int i=0;i<xcol;i++){
					Point2D p = new Point2D.Double(topx + unitw * i, topy + unith * j);
					ret.add(p);
					System.out.println(p.toString());
				}
			}
		} else if (direction==2){
			for(int j=yrow-1;j>-1;j--){
				for(int i=xcol-1;i>-1;i--){
					Point2D p = new Point2D.Double(topx + unitw * i, topy + unith * j);
					ret.add(p);
					System.out.println(p.toString());
				}
			}
		} else if (direction==1){
			for(int i=xcol-1;i>-1;i--){
				for(int j=0;j<yrow;j++){
					Point2D p = new Point2D.Double(topx + unitw * i, topy + unith * j);
					ret.add(p);
					System.out.println(p.toString());
				}	
			}
		} else if (direction==3){
			for(int i=0;i<xcol;i++){
				for(int j=yrow-1;j>-1;j--){
					Point2D p = new Point2D.Double(topx + unitw * i, topy + unith * j);
					ret.add(p);
					System.out.println(p.toString());
				}	
			}
		}
		return ret;
	}
	public static ArrayList<PActivity> layoutMatrixPointsInDirection(
			ArrayList<PNode> boxes, int direction,
			int xcol, int yrow, double topx, double topy, int animsec, PBounds returnPB){
		ArrayList<PActivity> palist = new ArrayList<PActivity>();

		PNode pn2 = boxes.get(0);
		ArrayList<Point2D> points = matrixPointsInDirection(pn2.getWidth(), pn2.getHeight(),
				topx, topy, xcol, yrow, direction);
		
		int count = 0;
		for(PNode pn: boxes){
			Point2D p = points.get(count);
			double x = p.getX();
			double y = p.getY();
			pn.setOffset(x,y); // なぜか，アニメーションがうまく動作しないため，直接位置を変更
			
			PActivity pa = pn.animateToPositionScaleRotation(x, y, 1, 0, animsec);
			palist.add(pa);
			if (returnPB != null) {
				PBounds aPB = new PBounds(pn.getFullBoundsReference());
				aPB.setOrigin(x,y);
				returnPB.add(aPB);
			}
			count++;
		}
		return palist;
	}
}
