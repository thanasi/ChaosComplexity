// LogisticMap.java
// Thanasi's Logistic Map Class
// cleaned up a bit

import javax.swing.*;
import P251.*;

public class LogisticMap2 extends P251Applet {
    
    /****** Global Variables ******/
    double defaultTolerance = 1e-9;  // tolerance for N-R convergence
    double derivDelta = 1e-6;        // delta for use in numerical deriv
    int iterMax = (int) 1e6;         // max number of iterations in N-R

    double rMax = 4;    // maximum value for r to be checked against

    /****** Initial values for user-input variables ******/
    double rStep = 1e-3; // step size for iterations through r
    double R0 = 1;       // 
    double nIter = 1000; // number of iterations of f(x) to run for each r
    double nKeep = 100;  // number of iterations of f(x) to plot for each r

    int iterate = 1;     // iterate of f
    double rGuess = 0;   // initial guess for N-R  

    /****** Calculation variables and arrays ******/
    double R;
    double [] X;
    double [] Y;

    double [] XX;
    double [] YY;
    double [] RR;

    /****** Panels ******/
    private inputPanel ip;
    private graphPanel gp1, gp2;


    /**************************************************/
    /***** Custom Math Functions *****/


    double f (double x) {
	// return f(x)
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

    


    /****** Custom plotting functions ******/




    /****** P251Applet Methods ******/



}