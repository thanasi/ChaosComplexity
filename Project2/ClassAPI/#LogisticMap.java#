// LogisticMap.java
// Thanasi's Logistic Map Class

import javax.swing.*;
import P251.*;


public class LogisticMap extends P251Applet {
   
    // defaults for numerics
    double defaulttol = 1e-9;
    double derivDelta = 1e-6;

    int iterate = 1;    // iterate of f to work with
    double rGuess = 0;  // initial guess for Newton-Raphson
    int iterMax = (int) 1e6;  // max number of iterations for N.R.

    // variables to iterate through R
    double R;          // R variable to use during calculations
    double rStep=1e-3; // step size
    double R0 = 1;     // default initial value
    double rMax = 4;   // limit r for the iterative map
    int nIter=1000;    // number of iterations to calculate
    int nKeep=100;     // keep last nIter-nKeep iterations

    // X,Y arrays to calculate and plot every iteration
    double[] X = new double[nIter];
    double[] Y = new double[nIter];

    // XX,YY arrays to hold the last nKeep iterations
    // RR aray for plotting these iterations vs R
    double[] XX = new double[nKeep];
    double[] YY = new double[nKeep];
    double[] RR = new double[nKeep];

    // panel stuff
    private inputPanel ip1, ip2, ip3;
    private graphPanel gp1, gp2;

    /*********************************************************
     * 
     * Custom method definitions (for this project)
     *
     *********************************************************/

    double f (double x) {
	return R * x * (1 - x);
	// return R * (-1 * Math.abs(x-.5) + .5);
	// return R * .25 * x * (1 - x) * (2 - x) * (3 - x);
    }

    double iterf (double x, int n) {
	// return n-th iterate of f
	if (n>1) return iterf(x,n-1);
	else return f(x);
    }

    double dfdx (double x, int n) {
	// take the numerical 1st derivative 
	// of the n-th iterate of f
	// at the point x, using small delta
	return (iterf(x+derivDelta, n)-iterf(x,n)) / (derivDelta);
    }


    double root (double x0, int n, double tol) {
	// use Newton-Raphson to find the fixed points of the
	// n-th iterate of f
	// iterate until tolerance of tol is reached
	double x = x0;
	double delta;
	int i = 0;
	do {
	    // subtract x and 1 from the function and the derivative
	    // because for fixed points, we want to find the roots of
	    // iterf(x, n) = x
	    delta = -(iterf(x, n)-x) / (dfdx(x, n)-1); 

	    // update x (we'd be here a while if we didn't)
	    x += delta;

	    if (i >= iterMax) break;  // if we've tried too hard, then give up
	    i++;
	} while (Math.abs(delta)>tol);

	if (i==iterMax) x = Double.NaN;

	System.out.println("Found fixed point of " + n + "-th iterate of f: " + x);

	return x;
    }
    
    double root (double x0, int n) {
	// overload with default tolerance
	return root(x0, n, defaulttol);
    }

    /*********************************************************
     * 
     * Custom plotting functions
     *
     *********************************************************/

    public void vLine(double x, double ymin, double ymax, graphPanel gp) {
	// draw a vertical line on graph panel gp
	for (int i=0; i<1000; i++) {
	    double [] xx = {x};
	    double [] yy = {ymin + i*(ymax-ymin)/1000};
	    gp.addData(xx, yy, "vline");
	}
	
    }

    public void hLine(double y, double xmin, double xmax, graphPanel gp) {
	// draw a horizontal line on graph panel gp
	for (int i=0; i<1000; i++) {
	    double [] yy = {y};
	    double [] xx = {xmin + i*(xmax-xmin)/1000};
	    gp.addData(xx, yy, "hline");
	}

    }

    public void plotFunc(double xmin, double xmax) {
	// calculate and plot f over its domain

	int resolution = 10000;
	double step = (xmax - xmin) / resolution;
	double [] x = new double[resolution];
	double [] y = new double[resolution];

	// initialize arrays
	x[0] = 0;
	y[0] = f(x[0]);
	
	// calculate f
	for (int i=1; i<resolution; i++) {
	    x[i] = x[i-1] + step;
	    y[i] = iterf(x[i], iterate);
	}
	
	gp1.clear();
	gp1.setDotSize(1);
	gp1.addData(x, y, "fplot");   // plot f
	gp1.addData(x, x, "xxplot");  // plot y=x for comparison	
    }
      
    /*********************************************************
     * 
     * P251 Methods
     *
     *********************************************************/

    public void fillPanels() {
	// set up input list
	ip1 = new inputPanel();
	ip2 = new inputPanel();
	ip3 = new inputPanel();
	ip1.addField("rStep", rStep);  // step size for R
	ip1.addField("r0", 1);         // minimum for x vs R plot
	ip2.addField("nIter", 1000);
	ip2.addField("nKeep", 100);
	ip3.addField("iterate", 1);
	ip3.addField("rGuess", 0);
	addPanel(ip1);
	addPanel(ip2);
	addPanel(ip3);

	// set up function graph panel
	gp1 = new graphPanel(400, 400, false);
	gp1.setXLabel("x");
	gp1.setYLabel("f(x)");
	gp1.setTitle("Iterated Function");
	addPanel(gp1);

	// set up (x vs r) graph panel
	gp2 = new graphPanel(800, 400, false);
	gp2.setXLabel("R");
	gp2.setYLabel("f(X) [Last " + nKeep + " points]");
	gp2.setTitle("Logistic Map");
	addPanel(gp2);
    }
      
    public void initValues() {
	// set up initial values 
	// for when the button is pressed
	R0 = 1.0;
	R = 1.0;
	nIter = 1000;
	nKeep = 100;
	rStep = 1e-3;
	
	iterate = 1;
	rGuess = 0;

	X[0] =0;
	Y[0] = Math.random();

	plotFunc(0,1);
    }
      
    public void readValues() {
	// get input values
	rStep = ip1.getValue(0);
	R0 = ip1.getValue(1);
	nIter = (int) ip2.getValue(0);
	nKeep = (int) ip2.getValue(1);
	iterate = (int) ip3.getValue(0);
	rGuess = ip3.getValue(1);

	// re-initialize arrays to new sizes
	X = new double[nIter];
	Y = new double[nIter];
	
	XX = new double[nKeep];
	YY = new double[nKeep];
	RR = new double[nKeep];

	// re-initialize R variable to restart plot
	R = R0;

        X[0] = 0;
	Y[0] = Math.random();

	// clear plots
	gp1.clear();
	gp2.clear();

	plotFunc(0,1);
    }
      
    public void compute(){
	// run the calculations

	// make sure plot parameters are initialized properly
	gp1.setDotSize(1);
	gp2.setDotSize(1);
	R = R0;

	// iterate f and create x vs r plot
	while (R < rMax) {
	    
	    for (int n=1; n<nIter; n++) {
		X[n] = Y[n-1];
		Y[n] = f(X[n]);
	    }
	
	    for (int i=0; i<nKeep; i++) {
		XX[i] = X[nIter - nKeep + i];
		YY[i] = Y[nIter - nKeep + i];
		RR[i] = R;
	    }

	    gp1.addData(XX,YY, "DataPoints");
	    gp2.addData(RR,XX, "rData");

	    R += rStep;

	    if (Thread.interrupted()) return;
	}

	// for the given rGuess, find a fixed point and plot it as a vertical line
	// on the x vs r plot
	// at the moment, we're not looking for the proper fixed points.
	// figure this out later.....
	double [] rt = {root(rGuess,iterate)};
	double [] frt = {iterf(rt[0],iterate)};
	vLine(rt[0],0,1, gp2);
	hLine(frt[0], R0, rMax, gp2);
	gp1.addData(rt, frt, "fixedpoint");
    }    
    
}