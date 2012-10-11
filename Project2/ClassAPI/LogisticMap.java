// LogisticMap.java
// Thanasi's Logistic Map Class

import javax.swing.*;
import P251.*;


public class LogisticMap extends P251Applet {
   
    // data stuffs
    double R;
    double rStep=1e-5;
    double R0 = 1;
    double rMax = 4;
    int nIter=1000;
    int nKeep=100;
    double[] X = new double[nIter];
    double[] Y = new double[nIter];
    double[] XX = new double[nKeep];
    double[] YY = new double[nKeep];
    double[] RR = new double[nKeep];

    // panel stuffs
    private inputPanel ip;
    private graphPanel gp1, gp2;

    double f (double x)
    {
	return R * x * (1 - x);
	// return R * (-1 * Math.abs(x-.5) + 1);
    }
      
    public void fillPanels() {
	ip = new inputPanel();
	ip.addField("rStep", 1e-3);
	ip.addField("r0", 1);
	ip.addField("nIter", 1000);
	ip.addField("nKeep", 100);
	addPanel(ip);

	gp1 = new graphPanel(400, 400, false);
	gp1.setXLabel("x");
	gp1.setYLabel("f(x)");
	gp1.setTitle("Iterated Function");
	addPanel(gp1); // adds graph panel to applet window

	gp2 = new graphPanel(800, 400, false);
	gp2.setXLabel("R");
	gp2.setYLabel("f(X) [Last " + nKeep + " points]");
	gp2.setTitle("Logistic Map");
	addPanel(gp2);
    }
      
    public void initValues() {
	R0 = 1.0;
	R = 1.0;
	nIter = 1000;
	nKeep = 100;
	
	X[0] =0;
	Y[0] = Math.random();

	plotFunc(0,1);
    }
      
    public void readValues()
    {
	rStep = ip.getValue(0);
	R0 = ip.getValue(1);
	nIter = (int) ip.getValue(2);
	nKeep = (int) ip.getValue(3);

	X = new double[nIter];
	Y = new double[nIter];
	
	XX = new double[nKeep];
	YY = new double[nKeep];
	RR = new double[nKeep];

	R = R0;

        X[0] = 0;
	Y[0] = Math.random();

	gp1.clear();
	gp2.clear();

	plotFunc(0,1);

    }
      
    public void compute()
    {

	R = R0;
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

	    gp1.setDotSize(3);
	    gp1.addData(XX,YY, "DataPoints");

	    gp2.setDotSize(1);
	    gp2.addData(RR,XX, "rData");

	    R += rStep;

	    if (Thread.interrupted()) return;

	}
    }


    public void plotFunc(double xmin, double xmax) {
        int resolution = 10000;
	double step = (xmax - xmin) / resolution;
	double [] x = new double[resolution];
	double [] y = new double[resolution];
	x[0] = 0;
	y[0] = f(x[0]);
	for (int i=1; i<resolution; i++) {
	    x[i] = x[i-1] + step;
	    y[i] = f(x[i]);
	}
	
	gp1.clear();
	gp1.setDotSize(1);
	gp1.addData(x, x, "xxplot");
	gp1.addData(x, y, "fplot");
    }
    
}