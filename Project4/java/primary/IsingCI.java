/*
<html>
<head>
<title> Ising Model </title>
</head>
<body>

<applet
   code="IsingCI.class",
   archive="jcommon.jar, jfreechart.jar, P251Applet.jar",
   height=450, width=500>
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

// CI
// check that initialization is properly sampling the space using a 2x2 grid

import javax.swing.*;
import java.awt.Color;
import P251.*;

public class IsingCI extends P251Applet {
    
    /****** Global Variables ******/

    // inputPanel ip1;
    // drawPanel dp1;
    graphPanel gp1;
    Color c;

    double rnd;          // hold a random number
    double [] range16 = new double [16];
    
    int L; // system edge size
    int N; // total number of spins
    int E; // energy
    int dE; // energy difference
    int T; // bath temp

    int AO = 0;
    double [] configs;

    int right,left,up,down;
    
    double [] w = new double[5];  // only 5 possible spin flip configurations

    int [][] spin;               // system of spins

    double accept;  // counter of accepted flips
    double norm;    // normalization factor

    /****** Ising Model Methods ******/
    public void initializeLattice() {
	
	for (int i=0; i<L; i++) {
	    for (int j=0; j<L; j++) {
		rnd = Math.random();
		if (rnd<0.5) spin[i][j] = 1;
		else spin[i][j] = -1;
		// System.out.print(spin[i][j] + "\t"); // debugging

	    } // end for..j
	} // end for..i
	// System.out.print("\n");
    } // end initializeLattice()

    public void initial() {
	spin = new int[L][L];
	// dp1.setDrawBounds(0,0,L,L);
	
	N = L*L;
	initializeLattice(); // randomly initialize
	
	// calculate initial energy
	E = 0; 
	for (int i = 0; i<L; i++) {
	    if (i==L-1) up=0; // toroidal boundaries
	    else up = i+1;
	    for (int j=0; j<L; j++) {
		if (j==L-1) right=0;
		else right = j+1;
		int sum = spin[i][right] + spin[up][j];
		E -= spin[i][j] * sum;  // total energy, without double counting
	    }
	} // end computing energy

	// compute Boltzmann probability ratios
	for (int t=0; t<5; t++) {
	    dE = -8 + t * 4;
	    w[t] =  Math.exp(-1.0 * dE / T);
	} // end probability calculation
    } // end initial()

    // map dE to the corresponding index of w
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
    } // end getIndex()

    public void Metropolis () {
	for (int t = 0; t<N; t++) {
	    int i = (int) (Math.random() * L);	
	    int j = (int) (Math.random() * L);

	    dE = DeltaE(i,j,L,spin);
	    rnd = Math.random();
	    if (rnd <= w[getIndex(dE)]) {
		spin[i][j] *= -1;
		E += dE;
		accept += 1;
	    }
	} // end for..i
	
    } // end Metropolis

    // calculate the energy for a swap at (i,j) using periodic BC
    public int DeltaE (int i, int j, int L, int [][] spin) {
	if (j==0) left = spin[i][L-1];
	else left = spin[i][j-1];
	if (j==L-1) right = spin[i][0];
	else right = spin[i][j+1];
		
	if (i==0) down = spin[L-1][j];
        else down = spin[i-1][j];
	if (i==L-1) up = spin[0][j];
	else up = spin[i+1][j];

	return 2 * spin[i][j] * (left + right + up + down);
    } // end DeltaE

    public void initialize () {
	accept = 0;
    } // end initialize

    /****** Applet Methods ******/
    public void fillPanels() {
	L = 2;
	T = 2;

	gp1 = new graphPanel(400,400,false);
	gp1.setDotSize(10);
	addPanel(gp1);

	for (int i = 0; i<16; i++) range16[i] = i+1;
	readValues();

    } // end fillPanels

    public void initValues() {}


    public void readValues() {
 	configs = new double [16];
	initial();
	initialize();

    } // end readValues

    public void compute() {
	int configIdx = 0;
	for (int i=0; i<16; i++) configs[i] = 0;

	for (int i=0; i<100000; i++) {
	    configIdx = 0;
	    initializeLattice();

	    // binary math to determine index
	    if (spin[0][0] == 1) configIdx += 1;
	    if (spin[0][1] == 1) configIdx += 2;
	    if (spin[1][0] == 1) configIdx += 4;
	    if (spin[1][1] == 1) configIdx += 8;

	    configs[configIdx] += 1;

	} // end check
	
	gp1.addData(range16, configs, AO+"Hi");
	AO += 1;
	System.out.print("\n");
	for (int i=0; i<16; i++) System.out.print(configs[i] + "\t");
	System.out.print("\n");
    } // end compute 

} // end Ising
