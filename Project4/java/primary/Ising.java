/*
<html>
<head>
<title> Ising Model </title>
</head>
<body>

<applet
   code="Ising.class",
   archive="jcommon.jar, jfreechart.jar, P251Applet.jar",
   height=400, width=750>
</applet>
<hr/>

</body>
</html>
*/

// Ising.java
// 2D ising model on a square lattice
// using Metropolis algorithm
// translated from BASIC as presented in 
// 
// Gould & Tobochnik 
// _Intro to Computer Simulation Methods_
// Ch 17

import javax.swing.*;
import java.awt.Color;
import P251.*;

public class Ising extends P251Applet {
    
    /****** Global Variables ******/

    int NAC = 4;  // number of interesting parameters to track in the accumulator

    inputPanel ip1,ip2;
    drawPanel dp1;
    graphPanel gp1;
    Color c;

    double rnd; // hold a random number

    int E;      // system energy
    int dE;     // energy change
    int L;      // linear lattice size
    int N;      // number of spins (LxL)
    int T;      // bath temperature
    int M;      // magnetization

    int [][] system;    // 2D Ising lattice

    // possibilities for spin-flip probabilities
    // only 5 in 2D
    double [] w = new double[5];

    int LatticeFill; // how to initialize the lattice - +1, -1, or 0->random
    int [] accum = new int [NAC];    // data accumulator
    String [] accumTitle = new String [NAC];
    int mcs;         // number of mc steps for data
    int nequil;      // number of mc steps for equilibrium
    double accept;   // counter of accepted flips
    double norm;     // normalization factor

    int up, down, left, right;   // indeces of neighbors
                                 // note that in this java class, the origin is bottom left

    /****** Ising Model Methods ******/
    public void initializeLattice (int fill) {
	// fill the system of spins based on user selection

	// if the choice is to randomly fill, do that
	if (fill==0) {
 	    for (int i=0; i<L; i++) {
		for (int j=0; j<L; j++) {	    
		    rnd = Math.random();
		    if (rnd<0.5) system[i][j] = 1;
		    else system[i][j] = -1;
		    M += system[i][j];
		}
	    }
	}
	// if a specific value given, fill with it
	else {
	    if (fill>0) fill = 1;
	    if (fill<0) fill = -1; // just in case somebody puts in silly values
	    
	    for (int i=0; i<L; i++) {
		for (int j=0; j<L; j++) {	    
		    system[i][j] = fill;
		    M += fill;
		}
	    } 
	}
    }

    public void initial () {
	// initialize the lattice
	system = new int[L][L];
	dp1.setDrawBounds(0,0,L,L);

	N = L * L;
	M = 0;
	initializeLattice(LatticeFill);

	// compute initial energy
	E = 0;
	for (int i = 0; i<L; i++) {
	    if (i==L-1) up=0; // toroidal boundaries
	    else up = i+1;
	    for (int j=0; j<L; j++) {
		if (j==L-1) right=0;
		else right = j+1;
		int sum = system[i][right] + system[up][j];
		E -= system[i][j] * sum;  // total energy, without double counting
	    }
	} // end computing energy

	// compute Boltzmann probability ratios
	for (int t=0; t<5; t++) {
	    int dE = -8 + t * 4;
	    w[t] =  Math.exp(-1.0 * dE / T);
	} // end probability calculation
	
    } // end initial



    public int getIndex(int dE) {
	if (dE == -8) return 0;
	else if (dE == -4) return 1;
	else if (dE == 0) return 2;
	else if (dE == 4) return 3;
	else if (dE == 8) return 4;
	else {
	    System.out.println("\n ERROR with getIndex() - dE = " + dE);
	    return -1;
	}
    }

    public void Metropolis () {
	for (int t = 0; t<N; t++) {
	    int i = (int) (Math.random() * L);	
	    int j = (int) (Math.random() * L);

	    dE = DeltaE(i,j,L,system);
	    rnd = Math.random();
	    if (rnd <= w[getIndex(dE)]) {
		system[i][j] *= -1;
		M += 2*system[i][j];
		E += dE;
		accept += 1;
	    }
	} // end for..i
	
    } // end Metropolis

    // calculate the energy for a swap at (i,j) using periodic BC
    public int DeltaE (int i, int j, int L, int [][] system) {
	if (j==0) left = system[i][L-1];
	else left = system[i][j-1];
	if (j==L-1) right = system[i][0];
	else right = system[i][j+1];
		
	if (i==0) down = system[L-1][j];
        else down = system[i-1][j];
	if (i==L-1) up = system[0][j];
	else up = system[i+1][j];

	return 2 * system[i][j] * (left + right + up + down);
    } // end DeltaE

    public void initialize () {
	for (int t=0; t<NAC; t++) accum[t] = 0;
	accept = 0;
    } // end initialize

    public void data () {
	// calculate the various system properties

	accumTitle[0] = "1";
	accumTitle[1] = "2";
	accumTitle[2] = "3";
	accumTitle[3] = "4";

	accum[0] = system[0][0]; // 
	accum[1] = system[0][1]; //
	accum[2] = system[1][0]; //
	accum[3] = system[1][1]; //

    } // end data

    public void output () {
	// print out accumulator
	for (int i = 0; i<NAC; i++) {
	    System.out.print(String.format("%4.3f\t", 1.0*accum[i]));
	}

	System.out.print("\b\n");
	
    } // end output


    public void drawSystem() {
	// cycle through the lattice
	for (int i=0; i<L; i++) {
	    for (int j=0; j<L; j++) {
		// cycle through the display
		if (system[i][j] == 1) c = Color.DARK_GRAY;
		else if (system[i][j] == -1) c = Color.CYAN;
		else c = Color.RED;
		// for (int i2=0; i2<gfxstep/2; i2++) {
		    //  for (int j2=0; j2<gfxstep/2; j2++) {
			// dp1.addLine(j*gfxstep/2+j2, i*gfxstep/2+i2, j*gfxstep/2+j2, i*gfxstep/2+i2, c);
		dp1.addArc(j, i, .5,.5, 0, 360, c);
		// }
		//}
	    }
	}

	dp1.repaint();
    } // end drawSystem

    /****** P251Applet Methods ******/
    public void fillPanels() {
	ip1 = new inputPanel();
	ip2 = new inputPanel();

	ip1.addField("L", 16);
	ip1.addField("T", 2);
	ip1.addField("LatticeFill", 0);
	ip2.addField("nequil", 10);
	ip2.addField("mcs", 10);
	
	dp1 = new drawPanel(256,256);

	addPanel(ip2);	
	addPanel(ip1);
	addPanel(dp1);

    } // end fillPanels

    public void initValues() {
	ip1.setValue(0, 16);
	ip1.setValue(1, 2);
	ip1.setValue(2, 0);
	ip2.setValue(0, 10);
	ip2.setValue(1, 10);

	System.out.print("\n");
	for (int i=0; i<NAC; i++) {
	    System.out.print(accumTitle[i] + "\t");
	}
	System.out.print("\b\n");	
	for (int i=0; i<NAC; i++) {
	    System.out.print("-----\t");
	}
	System.out.print("\b\n");

	readValues();
	
    } // end initValues

    public void readValues() {
	L = (int) ip1.getValue(0);
	T = (int) ip1.getValue(1);
	LatticeFill = (int) ip1.getValue(2);
	nequil = (int) ip2.getValue(0);
	mcs = (int) ip2.getValue(1);

	initial();
	
    } // end readValues

    public void compute() {
	for (int i = 0; i<nequil; i++) Metropolis();  // run Metropolis building up to equilibrium
	initialize();
	for (int i = 0; i<mcs; i++) {   // run the Metropolis for data
	    Metropolis();
	    data();
	}
	
	output();       // write out the results to the console
	drawSystem();   // draw the Ising system

    } // end compute

} // end Ising
