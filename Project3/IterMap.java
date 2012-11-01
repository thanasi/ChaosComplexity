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
    double [] roots;        // discovered roots

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

    }

    public void initValues() {
	// set up initial values
	
	nR = 10;
	roots = new double[nR];
	// set roots to 1 so as not to cause overflow on divide
	for (int i=0; i<nR; i++) roots[i] = 1;


    }

    public void readValues() {
	// read input panel values
	nR = (int) ip1.getValue(0);
	roots = new double[nR];
	// set roots to 1 so as not to cause overflow on divide
	for (int i=0; i<nR; i++) roots[i] = 1;
	
    }

    public void compute() {

	double rg;

	// solved for first two analytically
	roots[0] = 2;
	roots[1] = 1 + Math.sqrt(5);
	int k = 0;
	System.out.println(String.format("k= %02d\tr_sk= %4.4f", k+1, roots[k]));
	k = 1;
	System.out.println(String.format("k= %02d\tr_sk= %4.4f", k+1, roots[k]));


	for (k=2; k<nR; k++) {
	    rg = roots[k-1] + .1 * (roots[k-1] - roots[k-2]);
	    roots[k] = rootR(rg, k);
	    System.out.println(String.format("k= %02d\trg= %4.4f\tr_sk= %4.4f", k+1, rg, roots[k]));

	    if (Thread.interrupted()) return;
	}
	
    }

}