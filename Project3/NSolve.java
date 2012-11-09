// NSolve.java
// Newton's Method Numerical Solver
// for one dimensional functions, looks for single root at a time
// just change f and xguess and recompile for different functions

import javax.swing.*;
import P251.*;

public class NSolve extends P251Applet {
    
    // Global Variables

    double defaultTolerance = 1e-9;  // tolerance for N-R convergence
    double derivDelta = 1e-6;        // delta for use in numerical deriv
    int iterMax = 1000000;           // max number of iterations in N-R

    double xg;  // x guess for solver

    private inputPanel ip1;

    // functions

    
    /**************** Customize Function Here **/

    double F(double x) {
	// function to solve for root
	
	return 4 * Math.pow(x,3) - 9 * ( Math.pow(x,2) + 2 * x + 1 );

    }
    
    /*******************************************/


    double dFdx (double x) {
	// numerical first derivative of F
	return (F(x+derivDelta) - F(x)) / derivDelta;
    }

    double root (double x0) {
	// Newton's method for root finding
	double x = x0;
	double delta;
	int i = 0;
	
	do {
	    delta = - F(x) / dFdx(x);
	    x += delta;
	    
	    if (i>=iterMax) break;
	    i++;
	} while (Math.abs(delta)>defaultTolerance);

	if (i==iterMax) x = Double.NaN;

	return x;
    }



    // Applet Methods

    public void fillPanels() {
	ip1 = new inputPanel();
	ip1.addField("xg", 0);
	
	addPanel(ip1);
	
    }

    public void initValues() {
	xg = 0;
    }

    public void readValues() {
	xg = ip1.getValue(0);
    }

    public void compute() {
	double r = root(xg);
	System.out.println("\nroot: " + r);
    }


}