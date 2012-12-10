/*
<html>
<head>
<title> Ising Model </title>
</head>
<body>

<applet
   code="IsingEq.class",
   archive="jcommon.jar, jfreechart.jar, P251Applet.jar",
   height=450, width=750>
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

public class IsingEq extends P251Applet {

	// Easy access to default values
	int defneq = 0;
	int defmcs = 1;
	int defrep = 50;
	int defL = 2;
	int defT = 2;
	int defF = 0;
	int defR = 5;
	
    
    /****** Global Variables ******/

    inputPanel ip1,ip2;
    drawPanel dp1;
    graphPanel gp1, gp2;

    Color c;

    double rnd; // hold a random number
	int repeat; // number of times to repeat the compute loop

    int E;      // system energy
    int dE;     // energy change
    int L;      // linear lattice size
    int N; 		// number of spins LxL
    double T;      // bath temperature
    int M;      // magnetization

	int R;		// radius to calculate spatial correlations

    int [][] system;    // 2D Ising lattice

    // possibilities for spin-flip probabilities
    // only 5 in 2D
    double [] w = new double[5];
    
    int LatticeFill; // how to initialize the lattice - +1, -1, or 0->random
    int [] accum = new int [5];    // data accumulator for interesting parameters
    int mcs;         // number of mc steps for data
    int nequil;      // number of mc steps for equilibrium
    int accept;   // counter of accepted flips
    double norm;     // normalization factor

	int st;      // number of monte carLo steps since last reset
 	double[] configsx,configsy;	// the possible configurations of the system
								// described by the number of spin-up lattice sites

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
				// System.out.print(system[i][j] + " "); // debugging
				} // end for..j
		    } // end for..i
			// System.out.print("\n");
		}

		// if a specific value given, fill with it
		else {
		    if (fill>0) fill = 1;
		    if (fill<0) fill = -1; // just in case somebody puts in silly values
	    
		    for (int i=0; i<L; i++) {
				for (int j=0; j<L; j++) {	    
				    system[i][j] = fill;
				    M += fill;
				} // end for..j
		    } // end for..i
		}
	} // end initializeLattice

    public void initial () {
		// initialize the lattice
		dp1.setDrawBounds(0,0,L,L);
		system = new int[L][L];
		
		st = 0;
		gp1.clear();
		// gp2.clear();
	
		N = L * L;
		M = 0;
		configsx = new double[N+1];
		configsy = new double[N+1];
		for (int i=0; i<N+1; i++) {
			configsx[i] = i;
			configsy[i] = 0;
		}
		
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
    } // end getIndex

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
	    	} // end if
		} // end for..i
		st += 1;
	
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
		for (int t=0; t<5; t++) accum[t] = 0;
		accept = 0;
	    } // end initialize

	public void data () {
		accum[0] += E;
		accum[1] += E*E;
		accum[2] += M;
		accum[3] += M*M;
		accum[4] += Math.abs(M);
		
		configsy[getNUp()] += 1;
		
    } // end data

    public void output () {
		norm = 1.0 / (mcs * N);

		double a2 = accept * norm;
		double Eave = accum[0] * norm;
		double Mave = accum[2] * norm;
				
		double [] temp1 = new double [1];
		double [] temp2 = new double [1];

		temp1[0] = st;
		temp2[0] = Eave;
		gp1.addData(temp1, temp2, "Eave");
		temp2[0] = Mave;
		gp1.addData(temp1, temp2, "Mave");
		// temp2[0] = a2;
		// gp1.addData(temp1, temp2, "accept");
		
		double fracUp = 1.0*getNUp() / N;
		System.out.println(String.format("%3.2f\t%4.3f\t%4.3f\t%4.3f\t%4.3f", T, a2, Eave, Mave, fracUp));
		
    } // end output


    public void drawSystem() {
		dp1.clear(); // clear from the previous drawing

		// cycle through the lattice
		for (int i=0; i<L; i++) {
		    for (int j=0; j<L; j++) {
				// cycle through the display
				if (system[i][j] == 1) c = Color.DARK_GRAY;
				else if (system[i][j] == -1) c = Color.CYAN;
				else c = Color.RED;
				dp1.addArc(j+.1,i+1, .5,.5, 0,360, c);
		    }
		}

		dp1.repaint();
    } // end drawSystem


    /****** Analysis Methods ******/
	public double S(int R) {
		// calculate spatial correlations by measuring the average  
		int s = 0;
		R = Math.min(R,L/2);
		R = Math.max(R,1);
		for (int i = L/2-R; i<L/2+R; i++) {
			for (int j = L/2-R; j<L/2+R; j++) {
				s += system[i][j];
			}
		}
		
		return s / ((R+1) * (R+1)); // normalize to get average
						
	}
	
	public int getNUp() {
		// calculates the number of lattice sites with spin of +1
		return (N + M) / 2;
	}
	
    /****** P251Applet Methods ******/
    public void fillPanels() {
		ip1 = new inputPanel();
		ip2 = new inputPanel();

		ip1.addField("L", defL);
		ip1.addField("T", defT);
		ip1.addField("LatticeFill", defF);
		ip1.addField("R", defR);
		ip2.addField("nequil", defneq);
		ip2.addField("mcs", defmcs);
		ip2.addField("repeat", defrep);
			
		dp1 = new drawPanel(256,256, 0,0, 256,256);
		
		gp1 = new graphPanel(400,250, true);
		gp1.setDotSize(5);
		gp1.setXLabel("Number of MC steps");
		gp1.setYLabel("Average Quantities");
		gp1.setTitle("Equilibration Plot");

		// gp2 = new graphPanel(400,250, true);
		// gp2.setDotSize(5);
		// gp2.setXLabel("Number of Spin Ups");
		// gp2.setYLabel("P(N)");
		// gp2.setTitle("Configuration Probabilities");

		addPanel(ip2);	
		addPanel(ip1);
		addPanel(dp1);
		addPanel(gp1);
		// addPanel(gp2);

    } // end fillPanels

    public void initValues() {
		ip1.setValue(0, defL);
		ip1.setValue(1, defT);
		ip1.setValue(2, defF);
		ip1.setValue(3, defR);
		ip2.setValue(0, defneq);
		ip2.setValue(1, defmcs);
		ip2.setValue(2, defrep);

		System.out.println("\nT\taccept\tEave\tMave\tFracUp");
		System.out.println("--\t-----\t-----\t-----\t-----");

		readValues();
	
    } // end initValues

    public void readValues() {
		L = (int) ip1.getValue(0);
		T = ip1.getValue(1);
		LatticeFill = (int) ip1.getValue(2);
		R = (int) ip1.getValue(3);
		nequil = (int) ip2.getValue(0);
		mcs = (int) ip2.getValue(1);
		repeat = (int) ip2.getValue(2);
		initial();
		drawSystem();
	
    } // end readValues

    public void compute() {
	
		for (int t=0; t<repeat; t++) {
			for (int i = 0; i<nequil; i++) Metropolis();  // equilibriate the system
			initialize();
			for (int i = 0; i<mcs; i++) {   // run the Metropolis for data
		    	Metropolis();
		    	data();
			}
			output();       // write out the results to the console
		} // end repeat loop
		
		drawSystem();   // draw the Ising system
		// gp2.addData(configsx, configsy, "Equilibrated");

    } // end compute

} // end Ising
