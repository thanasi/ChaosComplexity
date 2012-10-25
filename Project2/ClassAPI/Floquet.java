// Floquet.java
// find Floquet Multiplier as function of r
// for the N-th iterate of f
// remember floquet multiplier is:
// L_i = [f(N)]'(x*)



import javax.swing.*;
import P251.*;

public class Floquet extends P251Applet {

    /***********************/
    /****** VARIABLES ******/
    /***********************/

    /****** Global Variables ******/
    double defaultTolerance = 1e-9;  // tolerance for N-R convergence
    double derivDelta = 1e-6;        // delta for use in numerical deriv
    int iterMax = 1000000;           // max number of iterations in N-R
    int nIter = 1000;

    
    /****** Initial values for user-input variables ******/
    double rStep = 1e-3;       // step size for iterations through r
    double R0 = 1;             // 
    double rMax = 4;    // maximum value for r to be checked against


    int nR = (int) ((rMax-R0) / rStep);
    int iterate = 1;           // iterate of f

    /****** Calculation variables and arrays ******/
    double R;     // to calculate the map
    double X;

    /****** Panels ******/
    private inputPanel ip1;
    private graphPanel gp1;


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

    double [] getLastValues(int n, int num, int nit) {
	// get the last num values of the nth iterate of f
	// after iterating nit times
	double [] output = new double[num];
	X = Math.random();

	for (int i=0; i<nit; i++) {
	    X = iterf(X, n);
	    if (i>nit-num-1) {
		output[i + num - nit] = X;
	    }
	}

	return output;	
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

    /****** P251Applet Methods ******/
    
    public void fillPanels() {

	// set up input list
	ip1 = new inputPanel();
	ip1.addField("iterate", 1);
	ip1.addField("rStep", rStep);   // step size for R
	ip1.addField("r0", 2.5);        //  minimum for x vs R plot
	ip1.addField("rMax", 4);
	addPanel(ip1);

	// set up function graph panel
	gp1 = new graphPanel(600, 600, false);
	gp1.setXLabel("r");
	gp1.setYLabel("df_r(x)/dx");
	gp1.setTitle("Iterated Function");
	addPanel(gp1);
    }

    public void initValues() {
	// set up initial values 
     	// for when the button is pressed
	R0 = 2.5;
	rMax = 4;
	R = 2.5;

	rStep = 1e-3;
	
	iterate = 1;

	nR = (int) ((rMax-R0) / rStep);

	// re-initialize arrays to new sizes
     	gp1.clear();
	gp1.setTitle("Floquet Multiplier for N=" + iterate);

    }
      
    public void readValues() {
	// get input values
	iterate = (int) ip1.getValue(0);
	rStep = ip1.getValue(1);
	R0 = ip1.getValue(2);
	rMax = ip1.getValue(3);
	nR = (int) ((rMax-R0) / rStep);

	// re-initialize R variable to restart plot
	R = R0;

	// clear plots
	gp1.clear();
	gp1.setTitle("Floquet Multiplier for N=" + iterate);
    }
      
    public void compute(){

	double [] RR = new double[nR];
	double [] LL = new double[nR];
	R = R0;
	for (int i = 0; i<nR; i++) {
	    X = getLastValues(iterate, 1, nIter)[0]; // iterate the function until convergence
	    RR[i] = R;             
	    LL[i] = dfdx(X, iterate); // get floquet multiplier
	    // System.out.println(String.format("%02d: %3.4f %3.4f", iterate, R, LL[i]));

	    R += rStep;   

	}
	
	
	gp1.addData(RR,LL, "Floquet");
	hLine(-1, R0, rMax, gp1);

   }    

    
}