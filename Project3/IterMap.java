// IterMap.java
// Study the convergence of r_k in the iterative map

import javax.swing.*;
import P251.*;

public class IterMap extends P251Applet {
    
    /***********************/
    /****** VARIABLES ******/
    /***********************/

    double tol = 1e-8;      // tolerance for N-R convergence
    int iterMax = 1000000;  // max number of iterations in N-R
    double epsilon = 1e-6;  // delta for use in numerical deriv

    int nR;              // number of rsk to look for
    double delta;        // scaling parameter for r
    double alpha;        // scaling parameter for y

    double [] roots;     // discovered roots
    double [] y;         // value of iterf half way through a 2^k cycle

    inputPanel ip1;

    /*********************/
    /****** METHODS ******/
    /*********************/

    /***** Custom Math Functions *****/

    double f (double x, double r) {
	// logistic map
	return r * x * (1 - x);
    }

    double iterf (double x, double r, int n) {
	// return n-th iterate of f
	if (n>1) return f(iterf(x,r,n-1), r);
	else return f(x,r);
    }

    double F (double r, int k) {
	// function used for optimization of r when x=.5
        // 
	double ans = iterf(.5, r, (int) Math.pow(2,k)) - .5;
	for (int i=0; i<nR; i++){
	    ans /= (r-roots[i]);
	}

	return ans;
	    
    }

    double dFdr (double r, int k){
	// take the derivative of F WRT r
	return (F(r+epsilon, k) - F(r, k)) / epsilon;
    }

    double rootR (double rg, int k){
	// use Newton's method to find r_{si}

	double r = rg;
	double delta;
	int i = 0;

	do {
	    // find derivative towards zero
	    delta = -F(r,k) / dFdr(r,k);
	    // update r
	    r += delta;

	    if (i>=iterMax) break;
	    i++;
	} while(Math.abs(delta)>tol);
	
	// if we timed out, then return NaN
	if (i==iterMax) {
	    r = Double.NaN;
	    System.out.println("Tried too hard to find rootR");
	}


	return r;
    }


    /****** P251Applet Methods ******/
    public void fillPanels() {
	// define the panels for human interaction
	ip1 = new inputPanel();

	ip1.addField("nR", 10);
	addPanel(ip1);
	initValues();

    }

    public void initValues() {
	// set up initial values
	
	nR = 10;
	roots = new double[nR];
	y = new double[nR];
	// set roots to 1 so as not to cause overflow on divide
	for (int i=0; i<nR; i++) roots[i] = 1;


    }

    public void readValues() {
	// read input panel values
	nR = (int) ip1.getValue(0);
	roots = new double[nR];
	y = new double[nR];
	// set roots to 1 so as not to cause overflow on divide
	for (int i=0; i<nR; i++) roots[i] = 1;
	
    }

    public void compute() {

	double rg; // r guess for root finding

	// solved for first two analytically
	roots[0] = 2;
	roots[1] = 1 + Math.sqrt(5);

	System.out.println("k\tr_sk\td\ty\ta");

	// solve details of k=0,1 cases outside of the loop
	int k = 0;
	y[k] = iterf(.5, roots[k], (int) Math.pow(2,k-1));
	System.out.println(String.format("%02d\t%4.4f\t--\t%4.4f\t", k+1, roots[k], y[k]));

	k = 1;
	y[k] = iterf(.5, roots[k], (int) Math.pow(2,k-1));
	System.out.println(String.format("%02d\t%4.4f\t--\t%4.4f\t", k+1, roots[k], y[k]));
	
	// for each k, figure out r_sk, delta, y, alpha
	for (k=2; k<nR; k++) {
	    rg = roots[k-1] + .1 * (roots[k-1] - roots[k-2]);
	    roots[k] = rootR(rg, k);
	    delta = (roots[k-1] - roots[k-2]) / (roots[k]-roots[k-1]);
	    y[k] = iterf(.5, roots[k], (int) Math.pow(2,k-1));
	    alpha = - (y[k-1] - y[k-2]) / (y[k] - y[k-1]);
	    System.out.println(String.format("%02d\t%4.4f\t%4.4f\t%4.4f\t%4.4f", k+1, roots[k], delta, y[k], alpha));

	    if (Thread.interrupted()) return;
	}
	
    }

}