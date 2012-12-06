/*
<html>
<head>
<title> Ising Model </title>
</head>
<body>

<applet
   code="IsingCheckInit.class",
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

// check that initialization is properly sampling the space using a 2x2 grid

import javax.swing.*;
import java.awt.Color;
import P251.*;

public class IsingCheckInit extends P251Applet {

    /****** Global Variables ******/
    graphPanel gp1;
    
    double rnd;
    double [] range16 = new double [16]; // numbers 1-16
    double [] microstates = new double [16];  // possible microstates 
    int idx; // index for states
    
    int L = 2;  // linear dimension of system
    int N = 4;  // total system size
    
    int [][] spin = new int[L][L];       // system of spins
    
    
    
    

    /****** Ising Model Methods ******/  
    public void initializeLattice() {
	
	for (int i=0; i<L; i++) {
	    for (int j=0; j<L; j++) {
		rnd = Math.random();
		if (rnd<0.5) spin[i][j] = 1;
		else spin[i][j] = -1;
	    } // end for..j
	} // end for..i
    } // end initializeLattice()
      
    /****** Applet Methods ******/
    public void fillPanels() {
	// initial method to be run
	gp1 = new graphPanel(400,400,false);
	gp1.setDotSize(20);
	addPanel(gp1);

	for (int i = 0; i<16; i++) range16[i] = i+1;
	readValues();

    } // end fillPanels
  
    public void compute() {
	gp1.clear();
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
    
    
    
} // end IsingCheckInit
