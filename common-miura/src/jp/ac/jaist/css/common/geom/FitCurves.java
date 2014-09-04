package jp.ac.jaist.css.common.geom;

import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import edu.umd.cs.piccolo.nodes.PPath;

public class FitCurves{
	private Vector<double[]> result = new Vector<double[]>();
	private double zoom = 1;
	
	public FitCurves(){
		//	result = new Vector();
	}
	
	public double[][] getPoints(List<Point2D> pary, double error){
		double[][] temp = new double[pary.size()][2];
		for(int i=0;i<pary.size();i++){
			temp[i][0] = pary.get(i).getX();
			temp[i][1] = pary.get(i).getY();
		}
		return getPoints(temp,error);
	}
	/** 
	 * getPoints : 近似ベジエ曲線点列を取得
	 */
	public double[][] getPoints(Vector<double[]> v, double error){
		double[][] temp = new double[v.size()][2];
		int i=0;
		for(Enumeration<double[]> e = v.elements(); e.hasMoreElements();){
			double[] t = (double[])(e.nextElement());
			temp[i][0] = t[0];
			temp[i][1] = t[1];
			//	    System.out.println("i: "+i+"  x : "+t[0]+"  y : "+t[1]);
			i++;
		}
		return getPoints(temp, error);
	}
	public double[][] getPoints(double[][] points, double error){
		result.removeAllElements();
		FitCurve(points, points.length, error);
		
		double[][] d;
		d = new double[result.size()][2];
		int i=0;
		for(Enumeration<double[]> e = result.elements(); e.hasMoreElements();){
			double[] t = (e.nextElement());
			d[i][0] = t[0];
			d[i][1] = t[1];
			i++;
		}
		return d;
	}
	
	public java.awt.Rectangle getBounds(){
		double lx, ly, rx, ry;
		double[] t;
		t = (result.get(0));
		lx = rx = t[0];
		ly = ry = t[1];
		int i;
		for(i=4; i<result.size(); i+=4){
			t = (result.get(i));
			if(lx > t[0]){lx = t[0];}
			if(ly > t[1]){ly = t[1];}
			if(rx < t[0]){rx = t[0];}
			if(ry < t[1]){ry = t[1];}
		}
		t = (result.get(i-1));
		if(lx > t[0]){lx = t[0];}
		if(ly > t[1]){ly = t[1];}
		if(rx < t[0]){rx = t[0];}
		if(ry < t[1]){ry = t[1];}
		return new java.awt.Rectangle((int)lx, (int)ly, (int)(rx-lx), (int)(ry-ly));
	}
	
//	getBezier : 近似ベジエ曲線を取得
	public PPath getBezier(Vector<double[]> v, double error){
		double[][] curve = getPoints(v, error);
		//	GeneralPath gp = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
		PPath gp = new PPath();
		gp.moveTo((float)curve[0][0], (float)curve[0][1]);
		//	gp.moveTo((float)x, (float)y);
		
		for(int i=1; i<curve.length; i+=4){
			gp.curveTo((float)curve[i][0], (float)curve[i][1], 
					(float)curve[i+1][0], (float)curve[i+1][1],
					(float)curve[i+2][0], (float)curve[i+2][1]);
		}
		//	System.out.println("length = "+curve.length/4);
		return gp;
	}
	
	public PPath getBezierFromPointsArray(ArrayList<Point2D> v, double error){
		double[][] curve = getPoints(v, error);
		PPath gp = new PPath();
		gp.moveTo((float)curve[0][0], (float)curve[0][1]);
		//	gp.moveTo((float)x, (float)y);
		
		for(int i=1; i<curve.length; i+=4){
			gp.curveTo((float)curve[i][0], (float)curve[i][1], 
					(float)curve[i+1][0], (float)curve[i+1][1],
					(float)curve[i+2][0], (float)curve[i+2][1]);
		}
		//	System.out.println("length = "+curve.length/4);
		return gp;
	}
	
	public GeneralPath getGPBezierFromPointsArray(List<Point2D> v, double error){
		double[][] curve = getPoints(v, error);
		GeneralPath gp = new GeneralPath();
		gp.moveTo((float)curve[0][0], (float)curve[0][1]);
		//	gp.moveTo((float)x, (float)y);
		
		for(int i=1; i<curve.length; i+=4){
			gp.curveTo((float)curve[i][0], (float)curve[i][1], 
					(float)curve[i+1][0], (float)curve[i+1][1],
					(float)curve[i+2][0], (float)curve[i+2][1]);
		}
		//	System.out.println("length = "+curve.length/4);
		return gp;
	}
	 
	
	/*
	 * これから下は
	 * http://www1.acm.org/pubs/tog/GraphicsGems/gems/FitCurves.c
	 * http://www1.acm.org/pubs/tog/GraphicsGems/gems/GraphicsGems.h
	 * http://www1.acm.org/pubs/tog/GraphicsGems/gems/GGVecLib.c
	 * をjavaに移植したものである。
	 */
	private void DrawBezierCurve(double[][] ps){
		int i;
		double[] t;
		for(i=0; i<ps.length; i++){
			t = new double[2];
			t[0] = ps[i][0]*zoom;
			t[1] = ps[i][1]*zoom;
			result.add(t);
		}
	}
	
	private void FitCurve(double[][] d, int nPts, double error)
	{
		double[] tHat1, tHat2; /*  Unit tangent vectors at endpoints */
		
		tHat1 = ComputeLeftTangent(d, 0);
		tHat2 = ComputeRightTangent(d, nPts - 1);
		FitCubic(d, 0, nPts - 1, tHat1, tHat2, error);
	}
	
	
	/*
	 *  FitCubic : Fit a Bezier curve to a (sub)set of digitized points
	 */
	private void FitCubic(double[][] d, int first, int last, double[] tHat1, double[] tHat2, double error)
	{
		double[][]  bezCurve;   /* Control points of fitted Bezier curve */
		double[]    u;          /* Parameter values for point */
		double[]    uPrime;     /* Improved parameter values */
		double      maxError;   /* Maximum fitting error */
		int[]       splitPoint; /* Point to split point set at */
		int         nPts;       /* Number of points in subset */
		double      iterationError;          /* Error below which you try iterating */
		int         maxIterations = 4;      /* Max times to try iterating */
		double[]    tHatCenter;              /* Unit tangent vector at splitPoint */
		int         i;  
		double err = error;
		
		error = error*(double)(last-first+1)/10;
		splitPoint = new int[1];
		
		iterationError = error * error;
		nPts = last - first + 1;
		
		/*  Use heuristic if region only has two points in it */
		if (nPts == 2) {
			double dist = V2DistanceBetween2Points(d[last], d[first]) / 3.0;
			
			bezCurve = new double[4][2];
			bezCurve[0][0] = d[first][0];
			bezCurve[0][1] = d[first][1];
			bezCurve[3][0] = d[last][0];
			bezCurve[3][1] = d[last][1];
			V2Add(bezCurve[0], V2Scale(tHat1, dist), bezCurve[1]);
			V2Add(bezCurve[3], V2Scale(tHat2, dist), bezCurve[2]);
			DrawBezierCurve(bezCurve);
			return;
		}
		
		/*  Parameterize points, and attempt to fit curve */
		u = ChordLengthParameterize(d, first, last);
		bezCurve = GenerateBezier(d, first, last, u, tHat1, tHat2);
		
		/*  Find max deviation of points to fitted curve */
		
		maxError = ComputeMaxError(d, first, last, bezCurve, u, splitPoint);
		//System.out.println("maxError: "+maxError);
		if (maxError < error) {
			DrawBezierCurve(bezCurve);
			return;
		}
		
		/*  If error not too large, try some reparameterization  */
		/*  and iteration */
		if (maxError < iterationError) {
			for (i = 0; i < maxIterations; i++) {
				uPrime = Reparameterize(d, first, last, u, bezCurve);
				bezCurve = GenerateBezier(d, first, last, uPrime, tHat1, tHat2);
				maxError = ComputeMaxError(d, first, last,
						bezCurve, uPrime, splitPoint);
				
				//System.out.println("maxError: "+maxError);
				if (maxError < error) {
					DrawBezierCurve(bezCurve);
					return;
				}
				u = uPrime;
			}
		}
		
		/* Fitting failed -- split at max error point and fit recursively */
		boolean flag = searchSplitPoint(d, first, last, splitPoint);
		
		//tHatCenter = ComputeCenterTangent(d, splitPoint[0]);
		tHatCenter = ComputeRightTangent(d, splitPoint[0]);
		
		//System.out.println("thatcenter :"+tHatCenter[0]+" "+tHatCenter[1]);
		FitCubic(d, first, splitPoint[0], tHat1, tHatCenter, err);
		//FitCubic(d, first, splitPoint[0], tHat1, tHatCenter, error);
		
		if(flag){
			tHatCenter = ComputeLeftTangent(d, splitPoint[0]);
		}
		else{
			V2Negate(tHatCenter);
		}
		
		//System.out.println("thatcenter :"+tHatCenter[0]+" "+tHatCenter[1]);
		FitCubic(d, splitPoint[0], last, tHatCenter, tHat2, err);
		//FitCubic(d, splitPoint[0], last, tHatCenter, tHat2, error);
	}
	
	
	private boolean searchSplitPoint(double[][] d, int first, int last, int s[])
	{
		double[] tan = new double[last-first];
		for(int i=first; i<last; i++){
			double[] temp = ComputeLeftTangent(d, i);
			tan[i-first] = Math.atan(temp[1]/temp[0]);
			if(temp[0] < 0){
				tan[i-first] += Math.PI;
			}
			tan[i-first] += Math.PI / 2;
		}
		double[] diff = new double[last-first-1];
		for(int i=0; i<last-first-1; i++){
			diff[i] = Math.abs(tan[i+1]-tan[i]);
		}
		double max = 0;
		int mi = (last-first+1)/2;
		int temp = s[0];
		for(int i=0; i<diff.length; i++){
			if(max < diff[i]){
				max = diff[i];
				mi = i;
			}
		}
		if(Math.PI/12*11 < max  && max < Math.PI / 12*13){
			s[0] = mi+first+1;
			return true;
		}
		else{
			//System.out.println("max: "+mi+" "+max);
			s[0] = temp;
			return false;
		}
		
		
	}
	
	
	/*
	 *  GenerateBezier :
	 *  Use least-squares method to find Bezier control points for region.
	 *
	 */
	private  double[][]  GenerateBezier(double[][] d, int first, int last, double[] uPrime, double[] tHat1, double[] tHat2)
	{
		int          i;
		double[][][] A;               /* Precomputed rhs for eqn */
		int          nPts;            /* Number of pts in sub-curve */
		double[][]   C;               /* Matrix C */
		double[]     X;               /* Matrix X */
		double       det_C0_C1,det_C0_X,det_X_C1;        /* Determinants of matrices */
		double       alpha_l, alpha_r;                 /* Alpha values, left and right */
		double[]     tmp;             /* Utility variable */
		double[][]   bezCurve;        /* RETURN bezier curve ctl pts */
		
		A = new double[last-first+1][2][2];
		C = new double[2][2];
		X = new double[2];
		
		bezCurve = new double[4][2];
		nPts = last - first + 1;
		
		
		/* Compute the A's*/
		for (i = 0; i < nPts; i++) {
			double[]  v1, v2;
			v1 = new double[2];
			v2 = new double[2];
			v1[0] = tHat1[0];
			v1[1] = tHat1[1];
			v2[0] = tHat2[0];
			v2[1] = tHat2[1];
			//System.out.println("tHat1 "+tHat1[0]+" "+tHat1[1]);
			//System.out.println("tHat2 "+tHat2[0]+" "+tHat2[1]);
			v1 = V2Scale(v1, B1(uPrime[i]));
			v2 = V2Scale(v2, B2(uPrime[i]));
			//System.out.println("uPrime "+uPrime[i]);
			A[i][0][0] = v1[0];
			A[i][0][1] = v1[1];
			A[i][1][0] = v2[0];
			A[i][1][1] = v2[1];
		}
		
		/* Create the C and X matrices */
		C[0][0] = 0.0;
		C[0][1] = 0.0;
		C[1][0] = 0.0;
		C[1][1] = 0.0;
		X[0]    = 0.0;
		X[1]    = 0.0;
		
		for (i = 0; i < nPts; i++) {
			C[0][0] += V2Dot(A[i][0], A[i][0]);
			C[0][1] += V2Dot(A[i][0], A[i][1]);
			/*     C[1][0] += V2Dot(&A[i][0], &A[i][1]);*/ 
			C[1][0] = C[0][1];
			C[1][1] += V2Dot(A[i][1], A[i][1]);
			//System.out.println("C[0][0]: "+C[0][0]+" C[0][1]: "+C[0][1]);
			//System.out.println("C[1][0]: "+C[1][0]+" C[1][1]: "+C[1][1]);
			//System.out.println("A[0][0]: "+A[i][0][0]+" A[0][1]: "+A[i][0][1]);
			//System.out.println("A[1][0]: "+A[i][1][0]+" A[1][1]: "+A[i][1][1]);
			
			tmp = V2SubII(d[first + i],
					V2AddII(
							V2ScaleIII(d[first], B0(uPrime[i])),
							V2AddII(
									V2ScaleIII(d[first], B1(uPrime[i])),
									V2AddII(
											V2ScaleIII(d[last], B2(uPrime[i])),
											V2ScaleIII(d[last], B3(uPrime[i]))))));
			
			
			//System.out.println("tmp: "+tmp[0]+" "+tmp[1]);
			X[0] += V2Dot(A[i][0], tmp);
			X[1] += V2Dot(A[i][1], tmp);
		}
		
		/* Compute the determinants of C and X */
		det_C0_C1 = C[0][0] * C[1][1] - C[1][0] * C[0][1];
		det_C0_X  = C[0][0] * X[1]    - C[0][1] * X[0];
		det_X_C1  = X[0]    * C[1][1] - X[1]    * C[0][1];
		
		/* Finally, derive alpha values */
		if (det_C0_C1 < 10e-12) {
			det_C0_C1 = (C[0][0] * C[1][1]) * 10e-12;
		}
		//System.out.println(C[0][0]+" "+C[0][1]+" "+C[1][0]+" "+C[1][1]);
		//System.out.println("det:"+det_C0_C1+" "+det_C0_X+" "+det_X_C1+"\n");
		
		alpha_l = det_X_C1 / det_C0_C1;
		alpha_r = det_C0_X / det_C0_C1;
		
		/*  If alpha negative, use the Wu/Barsky heuristic (see text) */
		/* (if alpha is 0, you get coincident control points that lead to
		 * divide by zero in any subsequent NewtonRaphsonRootFind() call. */
		if (alpha_l < 1.0e-6 || alpha_r < 1.0e-6) {
			double dist = V2DistanceBetween2Points(d[last], d[first]) /
			3.0;
			bezCurve[0][0] = d[first][0];
			bezCurve[0][1] = d[first][1];
			bezCurve[3][0] = d[last][0];
			bezCurve[3][1] = d[last][1];
			V2Add(bezCurve[0], V2Scale(tHat1, dist), bezCurve[1]);
			V2Add(bezCurve[3], V2Scale(tHat2, dist), bezCurve[2]);
			return (bezCurve);
		}
		
		/*  First and last control points of the Bezier curve are */
		/*  positioned exactly at the first and last data points */
		/*  Control points 1 and 2 are positioned an alpha distance out */
		/*  on the tangent vectors, left and right, respectively */
		bezCurve[0][0] = d[first][0];
		bezCurve[0][1] = d[first][1];
		bezCurve[3][0] = d[last][0];
		bezCurve[3][1] = d[last][1];
		V2Add(bezCurve[0], V2Scale(tHat1, alpha_l), bezCurve[1]);
		V2Add(bezCurve[3], V2Scale(tHat2, alpha_r), bezCurve[2]);
		//System.out.println("bez "+bezCurve[1][0]+" "+bezCurve[1][1]+" "+alpha_l);
		//System.out.println("bez "+bezCurve[2][0]+" "+bezCurve[2][1]+" "+alpha_r);
		return (bezCurve);
	}
	
	
	/*
	 *  Reparameterize:
	 * Given set of points and their parameterization, try to find
	 *   a better parameterization.
	 *
	 */
	private  double[] Reparameterize(double[][] d, int first, int last, double[] u, double[][] bezCurve)
	{
		int  nPts = last-first+1; 
		int  i;
		double[] uPrime;  /*  New parameter values */
		
		uPrime = new double[nPts];
		for (i = first; i <= last; i++) {
			uPrime[i-first] = NewtonRaphsonRootFind(bezCurve, d[i], u[i-first]);
		}
		return (uPrime);
	}
	
	
	
	/*
	 *  NewtonRaphsonRootFind :
	 * Use Newton-Raphson iteration to find better root.
	 */
	private  double NewtonRaphsonRootFind(double[][] Q, double[] P, double u)
	{
		double   numerator, denominator;
		double[][] Q1;
		double[][] Q2; /*  Q' and Q''   */
		double[]   Q_u, Q1_u, Q2_u;  /*u evaluated at Q, Q', & Q'' */
		double   uPrime;  /*  Improved u   */
		int   i;
		
		Q1= new double[3][2];
		Q2= new double[2][2]; 
		Q_u  = new double[2]; 
		Q1_u = new double[2]; 
		Q2_u = new double[2];
		
		/* Compute Q(u) */
		Q_u = BezierII(3, Q, u);
		
		/* Generate control vertices for Q' */
		for (i = 0; i <= 2; i++) {
			Q1[i][0] = (Q[i+1][0] - Q[i][0]) * 3.0;
			Q1[i][1] = (Q[i+1][1] - Q[i][1]) * 3.0;
		}
		
		/* Generate control vertices for Q'' */
		for (i = 0; i <= 1; i++) {
			Q2[i][0] = (Q1[i+1][0] - Q1[i][0]) * 2.0;
			Q2[i][1] = (Q1[i+1][1] - Q1[i][1]) * 2.0;
		}
		
		/* Compute Q'(u) and Q''(u) */
		Q1_u = BezierII(2, Q1, u);
		Q2_u = BezierII(1, Q2, u);
		
		/* Compute f(u)/f'(u) */
		numerator = (Q_u[0] - P[0]) * (Q1_u[0]) + (Q_u[1] - P[1]) * (Q1_u[1]);
		denominator = (Q1_u[0]) * (Q1_u[0]) + (Q1_u[1]) * (Q1_u[1]) +
		(Q_u[0] - P[0]) * (Q2_u[0]) + (Q_u[1] - P[1]) * (Q2_u[1]);
		
		/* u = u - f(u)/f'(u) */
		uPrime = u - (numerator/denominator);
		return (uPrime);
	}
	
	
	
	/*
	 *  Bezier :
	 *   Evaluate a Bezier curve at a particular parameter value
	 * 
	 */
	private  double[] BezierII(int degree, double[][] V, double t)
	{
		int  i, j;  
		double[]  Q;        /* Point on curve at parameter t */
		double[][]  Vtemp;  /* Local copy of control points  */
		
		Q = new double[2];
		/* Copy array */
		Vtemp = new double[degree+1][2];
		
		for (i = 0; i <= degree; i++) {
			Vtemp[i][0] = V[i][0];
			Vtemp[i][1] = V[i][1];
		}
		
		/* Triangle computation */
		for (i = 1; i <= degree; i++) { 
			for (j = 0; j <= degree-i; j++) {
				Vtemp[j][0] = (1.0 - t) * Vtemp[j][0] + t * Vtemp[j+1][0];
				Vtemp[j][1] = (1.0 - t) * Vtemp[j][1] + t * Vtemp[j+1][1];
			}
		}
		
		Q[0] = Vtemp[0][0];
		Q[1] = Vtemp[0][1];
		return Q;
	}
	
	
	/*
	 *  B0, B1, B2, B3 :
	 * Bezier multipliers
	 */
	private  double B0(double u)
	{
		double tmp = 1.0 - u;
		return (tmp * tmp * tmp);
	}
	
	
	private  double B1(double u)
	{
		double tmp = 1.0 - u;
		return (3 * u * (tmp * tmp));
	}
	
	private  double B2(double u)
	{
		double tmp = 1.0 - u;
		return (3 * u * u * tmp);
	}
	
	private  double B3(double u)
	{
		return (u * u * u);
	}
	
	
	
	/*
	 * ComputeLeftTangent, ComputeRightTangent, ComputeCenterTangent :
	 *Approximate unit tangents at endpoints and "center" of digitized curve
	 */
	private  double[] ComputeLeftTangent(double[][] d, int end)
	{
//		System.out.println("d length "+d.length);
//		System.out.println("end "+end);
		double[] tHat1;
		tHat1 = V2SubII(d[end+1], d[end]);
		V2Normalize(tHat1);
		return tHat1;
	}
	
	private  double[] ComputeRightTangent(double[][] d, int end)
	{
		double[] tHat2;
		tHat2 = V2SubII(d[end-1], d[end]);
		V2Normalize(tHat2);
		return tHat2;
	}
	
	
	@SuppressWarnings("unused")
	private  double[] ComputeCenterTangent(double[][] d, int center)
	{
		double[] V1, V2, tHatCenter;
		
		tHatCenter = new double[2];
		
		V1 = V2SubII(d[center-1], d[center]);
		V2 = V2SubII(d[center], d[center+1]);
		//System.out.println("center V: "+V1[0]+" "+V1[1]+" "+V2[0]+" "+V2[1]);
		tHatCenter[0] = (V1[0] + V2[0])/2.0;
		tHatCenter[1] = (V1[1] + V2[1])/2.0;
		if(tHatCenter[0] == 0.0 && tHatCenter[1] == 0.0){
			tHatCenter[0] = V1[0];
			tHatCenter[1] = V1[1];
		} 
		V2Normalize(tHatCenter);
		return tHatCenter;
	}
	
	
	/*
	 *  ChordLengthParameterize :
	 * Assign parameter values to digitized points 
	 * using relative distances between points.
	 */
	private  double[] ChordLengthParameterize(double[][] d, int first, int last)
	{
		int  i; 
		double[] u;   /*  Parameterization  */
		
		u = new double[last-first+1];
		
		u[0] = 0.0;
		for (i = first+1; i <= last; i++) {
			u[i-first] = u[i-first-1] +
			V2DistanceBetween2Points(d[i], d[i-1]);
		}
		
		for (i = first + 1; i <= last; i++) {
			u[i-first] = u[i-first] / u[last-first];
		}
		
		return(u);
	}
	
	
	
	
	/*
	 *  ComputeMaxError :
	 * Find the maximum squared distance of digitized points
	 * to fitted curve.
	 */
	private  double ComputeMaxError(double[][] d, int first, int last, double[][] bezCurve, double[] u, int[] splitPoint)
	{
		int  i;
		double maxDist;  /*  Maximum error  */
		double dist;  /*  Current error  */
		double[] P;   /*  Point on curve  */
		double[] v;   /*  Vector from point to curve */
		double error;
		
		splitPoint[0] = (last - first + 1)/2;
		maxDist = 0.0;
		error = 0.0; 
		for (i = first + 1; i < last; i++) {
			P = BezierII(3, bezCurve, u[i-first]);
			v = V2SubII(P, d[i]);
			dist = V2SquaredLength(v);
			error += dist;
			if (dist >= maxDist) {
				maxDist = dist;
				splitPoint[0] = i;
			}
		}
		//return error;
		return (maxDist);
	}
	
	
	private  double[] V2AddII(double[] a, double[] b)
	{
		double[] c = new double[2];
		c[0] = a[0] + b[0];
		c[1] = a[1] + b[1];
		return (c);
	}
	private  double[] V2ScaleIII(double[] v, double s)
	{
		double[] result = new double[2];
		result[0] = v[0] * s;
		result[1] = v[1] * s;
		return (result);
	}
	
	private  double[] V2SubII(double[] a, double[] b)
	{
		double[] c = new double[2];
		c[0] = a[0] - b[0];
		c[1] = a[1] - b[1];
		return (c);
	}
	
	private  double[] V2Scale(double[] v, double newlen) 
	{
		double len = V2Length(v);
		double[] vv = new double[2];
		//System.out.println("len: "+len);
		//System.out.println("v: "+v[0]+" "+v[1]);
		if (len != 0.0) { vv[0] = v[0]*newlen/len;   vv[1] = v[1]*newlen/len; }
		return(vv);
	}
	
	private  double[] V2Normalize(double[] v) 
	{
		double len = V2Length(v);
		if (len != 0.0) { v[0] /= len;  v[1] /= len; }
		return v;
	}
	
	private  double V2Length(double[] a) 
	{
		return(Math.sqrt(V2SquaredLength(a)));
	}
	
	private  double V2SquaredLength(double[] a) 
	{
		return((a[0] * a[0])+(a[1] * a[1]));
	}
	
	private  double V2DistanceBetween2Points(double[] a, double[] b)
	{
		double dx = a[0] - b[0];
		double dy = a[1] - b[1];
		return(Math.sqrt((dx*dx)+(dy*dy)));
	}
	
	private  double[] V2Add(double[] a, double[] b, double[] c)
	{
		c[0] = a[0]+b[0];
		c[1] = a[1]+b[1];
		return c;
	}
	
	private  double V2Dot(double[] a, double[] b) 
	{
		return((a[0]*b[0])+(a[1]*b[1]));
	}
	
	private  double[] V2Negate(double[] v) 
	{
		v[0] = -v[0];
		v[1] = -v[1];
		return(v);
	}
}

