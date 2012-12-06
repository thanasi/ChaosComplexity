/*
<html>
<head>
<title> Ising Model </title>
</head>
<body>

<applet
   code="IsingCheckMetropolis.class",
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

// check that Metropolis properly samples on a 2x2 grid

import javax.swing.*;
import java.awt.Color;
import P251.*;

public class IsingCheckMetropolis extends P251Applet {

    /****** Global Variables ******/
    drawPanel dp1;
    graphPanel gp1;
    
    double rnd;
    double [] range16 = new double [16]; // numbers 1-16
    double [] microstates = new double [16];  // possible microstates 
    int idx; // index for states
    
    int L = 2;  // linear dimension of system
    int N = 4;  // total system size

    int E;  // total system energy
    int dE; // energy cost of a move
    int M;  // total system magnetization
    int T;  // bath temperature

    double accept; // counter of accepted flips
    
    int up, down, left, right; // indecese of neighbors

    int [] w = new int [5];        // dE values for the 5 possible transitions
    int [][] spin = new int[L][L]; // system of spins
    
    /****** Ising Model Methods ******/  
    public void initializeLattice() {
	
	for (int i=0; i<L; i++) {
	    for (int j=0; j<L; j++) {
		rnd = Math.random();
		if (rnd<0.5) spin[i][j] = 1;
		else spin[i][j] = -1;
		M += system[i][j];
	    } // end for..j
	} // end for..i
    } // end initializeLattice()
      
    public void initial() {
	// generate initial random lattice
	M = 0;
	initializeLattice();
	
	// compute initial energy
	E = 0;
	for (int i = 0; i<L; i++) {
	    if (i==L-1) up=0; // toroidal boundaries
	    else up = i+1;
	    for (int j=0; j<L; j++) {
		if (j==L-1) right=0;
		else right = j+1;
		int sum = spin[i][right] + spin[up][j];
		E -= spin[i][j] * sum;  // total energy, without double counting
	    } // end for..j
	} // end for..i

	// compute Boltzmann probability ratios
	for (int t=0; t<5; t++) {
	    int dE = -8 + t * 4;
	    w[t] =  Math.exp(-1.0 * dE / T);
	} // end probability calculation

    } // end initial()

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
		M += 2*spin[i][j];
		E += dE;
		accept += 1;
	    } // end if
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
    } // end DeltaE()

    public void initialize () {
	accept = 0;
    } // end initialize()


    /****** Applet Methods ******/
    public void fillPanels() {
	// initial method to be run
	dp1 = new drawPanel(256,256);
	gp1 = new graphPanel(400,400,false);
	gp1.setDotSize(20);
	addPanel(gp1);

	for (int i = 0; i<16; i++) range16[i] = i+1;
	readValues();

    } // end fillPanels
  
    public void compute() {

	for (int i=0; i<16; i++) microstates[i] = 0;

	for (int i=0; i<1600000; i++) {
	    idx = 0;
	    initializeLattice();

	    // binary math to determine index
	    if (spin[0][0] == 1) idx += 1;
	    if (spin[0][1] == 1) idx += 2;
	    if (spin[1][0] == 1) idx += 4;
	    if (spin[1][1] == 1) idx += 8;

	    // count the appropriate microstructure
	    microstates[idx] += 1;
	    	    
	    
	} // end check
	
	gp1.addData(range16, microstates, "i");
	System.out.print("\n");
	for (int i=0; i<16; i++) System.out.print(microstates[i] + "\t");
	System.out.print("\n");
    } // end compute 
    
    
    
} // end IsingCheckMetropolis
