/*
<html>
<head>
<title> Ising Model </title>
</head>
<body>

<applet
   code="IsingCk.class",
   archive="jcommon.jar, jfreechart.jar, P251Applet.jar",
   height=300, width=900>
</applet>
<hr/>

</body>
</html>
*/


// IsingCk.java
// 2D ising model on a square lattice
// using Metropolis algorithm
// translated from BASIC as presented in 
// 
// Gould & Tobochnik 
// _Intro to Computer Simulation Methods_
// Ch 17

// check sampling probabilities for Ising code

import javax.swing.*;
import java.awt.Color;
import P251.*;

public class IsingCk extends P251Applet {

	// Easy access to default values
	int defneq = 4;			
	int defmcs = 10;		
	int defrep = 10000;		
	int defL = 2;			
	double defT = 2;		
	int defF = 0;			
	
    /****** Global Variables ******/

    inputPanel ip1,ip2;
    drawPanel dp1;
    graphPanel gp1, gp2;

 	double g1x[], g1y[], g2x[], g2y[]; // x and y data for gp1 and gp2


    Color c;

    double rnd; // hold a random number
	int repeat; // number of times to repeat the compute loop

    int E;      // system energy
    int dE;     // energy change
    int L;      // linear lattice size
    int N; 		// number of spins LxL
    double T;      // bath temperature
    int M;      // magnetization

	int NUp;	// number of up spins in the lattice

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

	double [] SS,RR; // accumulator for S
	int i0,j0;  // center coordinates for spatial correlation
	
	
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
				} // end for..j
		    } // end for..i
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

    } // end data

    public void output () {
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
		// spin value over a ball of radius R surrounding a point i,j
		int s = 0;
		R = Math.min(R,L/3);
		R = Math.max(R,1);
		
		int min = L/3;
 		int max = 2*L/3;

		// pick a random centroid in the middle third of the system
		do {
			i0 = min + (int) (Math.random() * (max-min));		
			j0 = min + (int) (Math.random() * (max-min));		
		} while (system[i0][j0] == -1);
		// System.out.println("i0: " + i0 + "\tj0: " + j0); // debugging
		
		for (int i = i0-R; i<i0+R; i++) {
			for (int j = j0-R; j<j0+R; j++) {
				if (system[i][j] == 1) 	s += 1;
			}
		}
		
		return 1.0*s / (R*R);  // normalize to get average density
						
	}
	
	public int getNUp() {
		// calculates the number of lattice sites with spin of +1
		return (N + M) / 2;
	}
	
    /****** P251Applet Methods ******/
    public void fillPanels() {
		
		// set variables
		nequil = defneq;
		repeat = defrep;
		mcs = defmcs;
		L = defL;
		T = defT;
		LatticeFill = defF;
	
		system = new int[L][L];
		N = L * L;
		
		g1x = new double [5];
		g1y = new double [5];
		g2x = new double [5];
		g2y = new double [5];
		

		for (int i=0;i<5;i++) {
			g1x[i] = i;
			g2x[i] = i;
			g1y[i] = 0;
			g2y[i] = 0;
		}
	
		gp1 = new graphPanel(400,250, true);
		gp1.setDotSize(3);
		gp1.setXLabel("N_up");
		gp1.setYLabel("P(N_up)");
		gp1.setTitle("Sampling Probabilities for Initial Lattice");
		
		gp2 = new graphPanel(400,250,true);
		gp2.setDotSize(3);
		gp2.setXLabel("N_up");
		gp2.setYLabel("P(N_up)");
		gp2.setTitle("Sampling Probabilities after 500 Metropolis Steps");

		addPanel(gp1);
		addPanel(gp2);


    } // end fillPanels

    public void initValues() {
	} // end initValues

    public void readValues() {
	} // end readValues

    public void compute() {
		for (int i=0;i<5;i++) {
			g1x[i] = i;
			g2x[i] = i;
			g1y[i] = 0;
			g2y[i] = 0;
		}
		
		for (int t=0; t<repeat; t++) {
			System.out.println("System " + (t+1) + "/" + repeat + " @ T = " + T);
			initial();
			
			// calculate initial configuration
			NUp = getNUp();
			g1y[NUp] += 1;
			
			for (int i=0; i<nequil; i++) Metropolis();
			initialize();
			
			for (int i=0; i<mcs; i++) {
				Metropolis();
		    	data();
				if (Thread.interrupted()) return;
			}
			
			NUp = getNUp();
			g2y[NUp] += 1;
		}
		
		for (int i=0; i<5; i++) {
			g2y[i] = 1.0 * g2y[i] / repeat;
			g1y[i] = 1.0 * g1y[i] / repeat;		
		}
		
		System.out.println(String.format("\n\nInitial:\t%4.3f\t%4.3f\t%4.3f\t%4.3f\t%4.3f", g1y[0],g1y[1],g1y[2],g1y[3],g1y[4]));
		System.out.println(String.format("\n\nEquilib:\t%4.3f\t%4.3f\t%4.3f\t%4.3f\t%4.3f", g2y[0],g2y[1],g2y[2],g2y[3],g2y[4]));

		gp1.addData(g1x,g1y,"T" + T);
		gp2.addData(g2x,g2y,"T" + T);
		
		
		
		
		
    } // end compute

} // end Ising
