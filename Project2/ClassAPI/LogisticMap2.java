// LogisticMap.java
// Thanasi's Logistic Map Class
// cleaned up a bit

import javax.swing.*;
import P251.*;

public class LogisticMap2 extends P251Applet {

    /***********************/
    /****** VARIABLES ******/
    /***********************/

    /****** Global Variables ******/
    double defaultTolerance = 1e-9;  // tolerance for N-R convergence
    double derivDelta = 1e-6;        // delta for use in numerical deriv
    int iterMax = 1000000;           // max number of iterations in N-R

    double rMax = 4;    // maximum value for r to be checked against
    
    /****** Initial values for user-input variables ******/
    double rStep = 1e-3;       // step size for iterations through r
    double R0 = 1;             // 
    double R1,R2;              // for use in finding fixed points of cycles
    int nIter = 1000;          // number of iterations of f(x) to run for each r
    int nKeep = 100;           // number of iterations of f(x) to plot for each r

    int iterate = 1;           // iterate of f
    double rGuess = 0;         // initial guess for N-R  

    /****** Calculation variables and arrays ******/
    double R;     // to calculate the map
    double X;
    double X0 = Math.random();

    double [] XX; // to calculate X vs R plot
    double [] YY;
    double [] RR;
    double [] RR2 = new double [2];
    double [] RR4 = new double [4];

    /****** Panels ******/
    private inputPanel ip1, ip2, ip3;
    private graphPanel gp1, gp2;


    /*********************/
    /****** METHODS ******/
    /*********************/

    /***** Custom Math Functions *****/

    double f (double x) {
	return R * x * (1 - x);
	// return R * (-1 * Math.abs(x-.5) + .5);
	// return R * .25 * x * (1 - x) * (2 - x) * (3 - x);
    }

    double iterf (double x, int n) {
	// return n-th iterate of f
	if (n>1) return f(iterf(x,n-1));
	else return f(x);
    }

    double dfdx (double x, int n, int i) {
	// take the numerical i-th derivative 
	// of the n-th iterate of f
	// at the point x, using small (globally set) delta

	if (i>1) return (dfdx(x+derivDelta,n,i-1)-dfdx(x,n,i-1)) / derivDelta;
	else return (iterf(x+derivDelta, n)-iterf(x,n)) / (derivDelta);
    }

    double dfdx (double x, int n) {
	// overload with default i=1
	return dfdx(x,n,1);
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

	System.out.println(String.format("Found fixed point of the %d iterate of f for R=%4.3f:\t%6.4f --> %6.4f", n, R, x0, x));

	return x;
    }
    
    double root (double x0, int n) {
	// overload with default tolerance
	return root(x0, n, defaultTolerance);
    }

    double [] getLastValues(int n, int num, int nit) {
	// get the last num values of the nth iterate of f
	// after iterating nit times
	double [] output = new double[num];
	X = X0;
	for (int i=0; i<nit; i++) {
	    X = iterf(X, n);
	    if (i>nit-num-1) {
		output[i + num - nit] = X;
		//		System.out.println("getLastValues " + n + " " + num + " " + nit + " >> " + X);
	    }
	}
	return output;	
    }

    double [] findFixedPoints(double [] guess) {
	// find the fixed points of an iterative of f
	// the iterative is determined by the length of the guess array
	int N = guess.length;
	double [] ans = new double[N];
	for (int i = 0; i<N; i++) {
	    ans[i] = root(guess[i], N);
	}

	return ans;
    }

    /****** Custom plotting functions ******/
    
    public void vLine(double x, double ymin, double ymax, graphPanel gp) {
	// draw a vertical line on graph panel gp
	for (int i=0; i<800; i++) {
	    double [] xx = {x};
	    double [] yy = {ymin + i*(ymax-ymin)/800};
	    gp.addData(xx, yy, "vline");
	}
    }

    public void hLine(double y, double xmin, double xmax, graphPanel gp) {
	// draw a horizontal line on graph panel gp
	for (int i=0; i<800; i++) {
	    double [] yy = {y};
	    double [] xx = {xmin + i*(xmax-xmin)/800};
	    gp.addData(xx, yy, "hline");
	}
    }

    public void plotFunc(double xmin, double xmax) {
	// calculate and plot the desired iterate of f over its domain,
	// along with the line y=x

	int resolution = 10000;
	double step = (xmax - xmin) / resolution;
	double [] x = new double[resolution];
	double [] y = new double[resolution];

	// initialize arrays
	x[0] = 0;
	y[0] = f(x[0]);

	int [] it = {1,2,4}; // iterates to plot

	// calculate and plot f
	gp1.clear();
	for (int j=0; j<3; j++){
	    for (int i=1; i<resolution; i++) {
		x[i] = x[i-1] + step;
		y[i] = iterf(x[i], it[j]);
	    }
	
	    gp1.setDotSize(1);
	    gp1.addData(x, y, "fplot"+it[j]);   // plot f
	    gp1.addData(x, x, "xxplot");  // plot y=x for comparison	
	}
    }    
    
    void plotXvsR() {
	// plot the last nKeep values of f(X) iterated nIter times
	double X = X0;
	for (int i=0; i<nIter; i++){
	    X = iterf(X, iterate);
	    if (i>nIter-nKeep){
		// once we're in the 'keepable' number of iterations
		// save them to the keeping array
		RR[i + nKeep - nIter - 1] = R;
		XX[i + nKeep - nIter - 1] = X;
	    }
	}
	gp2.setDotSize(1);
	gp2.addData(RR,XX,"xvsr");
    }

    /****** P251Applet Methods ******/
    
    public void fillPanels() {

	// set up input list
	ip1 = new inputPanel();
	ip2 = new inputPanel();
	ip3 = new inputPanel();
	ip1.addField("rStep", rStep);   // step size for R
	ip1.addField("r0", 2.5);        //  minimum for x vs R plot
	ip1.addField("r1", 3.2);
	ip1.addField("r2", 3.5);

	ip2.addField("nIter", 1000);
	ip2.addField("nKeep", 100);
	ip3.addField("iterate", 1);
	ip3.addField("rGuess", 0);

	// set up function graph panel
	gp1 = new graphPanel(400, 400, false);
	gp1.setXLabel("x");
	gp1.setYLabel("f(x)");
	gp1.setTitle("Iterated Function");
	addPanel(gp1);

	// set things up to look good
	addPanel(ip1);
	addPanel(ip2);
	addPanel(ip3);

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
	R0 = 2.5;
	R1 = 3.2;
	R2 = 3.5;
	R = 2.5;
	nIter = 1000;
     	nKeep = 100;
	rStep = 1e-3;
	
	X0 = Math.random();
	iterate = 1;
	rGuess = 0;

	// re-initialize arrays to new sizes

	XX = new double[nKeep];
	YY = new double[nKeep];
	RR = new double[nKeep];

     	gp1.clear();
     	gp2.clear();
     	plotFunc(0,1);
    }
      
    public void readValues() {
	// get input values
	rStep = ip1.getValue(0);
	R0 = ip1.getValue(1);
	R1 = ip1.getValue(2);
	R2 = ip1.getValue(3);
	nIter = (int) ip2.getValue(0);
	nKeep = (int) ip2.getValue(1);
	iterate = (int) ip3.getValue(0);
	rGuess = ip3.getValue(1);

	// re-initialize arrays to new sizes
	XX = new double[nKeep];
	YY = new double[nKeep];
	RR = new double[nKeep];

	// re-initialize R variable to restart plot
	R = R0;
	X0 = Math.random();


	// clear plots
	gp1.clear();
	gp2.clear();

	plotFunc(0,1);
    }
      
    public void compute(){

	// first task
	// iterate through R to create x vs r plot for desired range of r
	R = R0;
	
	while (R<rMax) {
	    plotXvsR();
	    R += rStep;	    pp
	    if (Thread.interrupted()) return;
	}

	// second task
	// find stable values of 2-cycles and 4-cycles
	// determine guesses for X by iterating the function nIter times
	// then run it through Newton-Raphson and plot to verify
	R = R1;
	double [] cycle2Guess = getLastValues(1,2,10);
	double [] cycle2 = findFixedPoints(cycle2Guess);
	
	R = R2;
	double [] cycle4Guess = getLastValues(1,4,10);
	double [] cycle4 = findFixedPoints(cycle4Guess);

	// vLine(R1, 0,1, gp2);
	// vLine(R2, 0,1, gp2);


	// fourth task



	// fifth task
   }    

    
}