// Bounce.java
// multiple bouncing balls
// does not handle high-velocity pass-throughs or subtle grazes
// requires slow motion
// to-do: interpolate between time steps to determine intermediate collisions (right now if dx/dy are on the order of R, then two particles can pass through each other)
//        improve collision detection to better handle multi-particle collisions
//        make ball-ball bounces conform to conservation of p/E -> right now the system cools down as momentum and energy aren't properly conserved.

import javax.swing.*;
import java.awt.Color;
import P251.*;
public class Bounce extends P251Applet
{
    private drawPanel dp;
    private inputPanel ip1, ip2, ip3;

    // system params
    int nIter, S;
    int nIter0 = 1000;
    int S0 = 10;

    // drawPanel size
    int W = 500;
    int H = 500;
    double xmin, xmax, ymin, ymax;

    // box size
    int w, h;
    int w0 = 250;
    int h0 = 250;

    // ball properties
    // N - Number of balls
    // T - Temperature (sets initial velocity)
    // R - radius
    // x,y - position arrays
    // dx,dy - velocity arrays

    int N;
    double R,T;
    int N0 = 1;
    double R0 = 10;
    double T0 = 1;

    double[][] collisions;
    double[] counts;

    double[] x, y;
    double[] dx,dy,speed;
    double[][] dx2, dy2;
    double[] dxs, dys;


    public void initValues()
    {
	w = w0;
	h = h0;
	R = R0;
	N = N0;
	T = T0;

	nIter = nIter0;
	S = S0;

	calculateDependents();
    } // end of initValues

    public void calculateDependents()
    {
	x = new double[N];
	y = new double[N];
	dx = new double[N];
	dy = new double[N];
	speed = new double[N];
	dx2 = new double[N][N+1];
	dy2 = new double[N][N+1];
	dxs = new double[N];
	dys = new double[N];
	counts = new double[N];
	collisions = new double[N][N+1];

	fillArray(x,N,-2*R);
	fillArray(y,N,-2*R);
	fillArray(dx,N,0);
	fillArray(dy,N,0);
	fillArray(speed,N,0);
	fillArray2(collisions, N, N+1, 0);
	fillArray2(dx2, N, N+1, 0);
	fillArray2(dy2, N, N+1, 0);
	fillArray(dxs, N, 0);
	fillArray(dys, N, 0);
	fillArray(counts, N, 0);

	updateBounds();
    } // end of calculateDependents

    public void initializeBalls()
    {
	int tries = 0;

	for (int n=0; n<N; n++)
	{
	    dx[n] = (Math.random() * 2*T) - T;
	    dy[n] = (Math.random() * 2*T) - T;

	    speed[n] = Math.sqrt(Math.pow(dx[n],2) + Math.pow(dy[n],2));

	    x[n] = xmin + Math.random() * (w-(2*R));
	    y[n] = ymin + Math.random() * (h-(2*R));

	    // overlap loop
	    int m = 0;
	    while (m < n)
	    {
		// if the newly placed ball doesn't overlap any existing balls,
		// then continue
		if (d(x[n],y[n], x[m],y[m]) > 2*R + 2)
		{
		    m++;
		}
		
		// otherwise, replace it and restart the check
		else
		{
		    x[n] = xmin + Math.random() * (w-(2*R));
		    y[n] = ymin + Math.random() * (h-(2*R));
		    m = 0;
		    tries++;
		}

		// if we've tried too many times, quit and assume unsolvable
		if (tries>Math.pow(N,3))
		{
		    System.out.println("Could not solve placement issue in appropriate time.");
		    return;
		}
		
	    } // end overlap check
	    
	}
    } // end of initializeBalls

    // update the bounds for the system
    public void updateBounds()
    {
	xmin = W/2 - w/2 + R;
	ymin = H/2 - h/2 + R;
	xmax = W/2 + w/2 - R;
	ymax = H/2 + h/2 - R;

    }

    // iterate through balls and move them
    // checking for collisions
    public void stepBalls()
    {
	fillArray2(collisions, N, N+1, 0);
	fillArray2(dx2, N, N+1, 0);
	fillArray2(dy2, N, N+1, 0);

	for (int n=0; n<N; n++)
	{
	    for (int m=0; m<n; m++)
	    {
		if (d(x[n]+dx[n],y[n]+dy[n],x[m]+dx[m],y[m]+dy[m]) < 2*R+1)
	       {
		   if (collisions[n][m] == 0)
		   {
		       collisions[n][m] = 1;
		       collisions[m][n] = 1;
		       handleCollision(n,m);
		   }
	       }
	    }

	    handleWall(n);


	    // update velocities based on all the interactions
	    dxs = sumArray0(dx2, N, N+1);
	    dys = sumArray0(dy2, N, N+1);
	    counts = sumArray0(collisions, N, N+1);

	    // iterate through the balls
	    // if it experienced a collision, update it's velocity
	    // based on the mean of the updated velocities
	    for (int nn=0; nn<N; nn++) 
	    {
		// System.out.println("Sx: " + dxs[nn] + "\tSy: " + dys[nn] + "\tC: " + counts[nn]);
		if (counts[nn]>0)
	        {
		    dx[nn] = (double) dxs[nn] / counts[nn];
		    dy[nn] = (double) dys[nn] / counts[nn];
	      	}
		
	    }

	    x[n] += dx[n];
	    y[n] += dy[n];

	}
	
    }

    // check for collision with the walls
    public void handleWall(int n)
    {
	if (x[n] < xmin+1)
	{
	    x[n] = 2*xmin - x[n];
	    dx2[n][N] = -dx[n];
	    if (collisions[n][N] != 1) dy2[n][N] = dy[n];
	    collisions[n][N] = 1;
	}

	if (x[n] > xmax-1)
	{
	    x[n] = 2*xmax - x[n];
	    dx2[n][N] = -dx[n];
	    if (collisions[n][N] != 1) dy2[n][N] = dy[n];
	    collisions[n][N] = 1;
	}

	if (y[n] < ymin+1)
	{
	    y[n] = 2*ymin - y[n];
	    dy2[n][N] = -dy[n];
	    if (collisions[n][N] != 1) dx2[n][N] = dx[n];
	    collisions[n][N] = 1;
	}

	if (y[n] > ymax-1)
        {
	    y[n] = 2*ymax - y[n];
	    dy2[n][N] = -dy[n];
	    if (collisions[n][N] != 1) dx2[n][N] = dx[n];
	    collisions[n][N] = 1;
	}

    } // end of handleWall


    // check for collision with other balls
    public void handleCollision(int n,int m)
    {
	// displacement vector
	double dVecx = dx[m] - dx[n];
	double dVecy = dy[m] - dy[n];
	

	// calculations for particle n
	double cAngle = angle( dVecx,dVecy, 0,1);
	double [] localFrame = rotate(dx[n], dy[n], cAngle);
	double dxl = localFrame[0];
	double dyl = localFrame[1];

	// by orthogonality, only the x component will be affected
	dxl *= -1;
	double [] globalFrame = rotate(dxl, dyl, -cAngle);

	dx2[n][m] = globalFrame[0];
	dy2[n][m] = globalFrame[1];

	// calculations for particle m (dVec -> -dVec)
	cAngle = angle( -dVecx,-dVecy, 0,1);
	localFrame = rotate(dx[m], dy[m], cAngle);
	dxl = localFrame[0];
	dyl = localFrame[1];	

	// by orthogonality, only the x component will be affected
	dxl *= -1;
	globalFrame = rotate(dxl, dyl, -cAngle);

	dx2[m][n] = globalFrame[0];
	dy2[m][n] = globalFrame[1];


    } // end of handleCollision

    public void compute() // compute is executed when the corresponding applet button is pressed
    {

	for (int i=0; i<nIter; i++)
	{
	    System.out.println("\nIteration: " + (i+1) + "/" + nIter);
	    stepBalls();
	     
	    updateDrawing();
	    if (Thread.interrupted()) return;

    	    // hang tight for sleep ms
	    try
	    {
	    	Thread.sleep( S );
	    }
	    catch(InterruptedException e)
	    {
		return;
	    }


	}


    } // end of compute

    // initial Panel drawing
    public void fillPanels() 
    {
	initValues();

	// box properties
        ip1 = new inputPanel();
	ip1.addField("box width (<" + W + ")", w0);
	ip1.addField("box height (<" + H + ")", h0);

	// ball properties
	ip2 = new inputPanel();
	ip2.addField("N", N0);
	ip2.addField("R", R0);
	ip2.addField("T", T0);

	// system properties
	ip3 = new inputPanel();
	ip3.addField("sleep", S0);
	ip3.addField("niter", nIter0);

	addPanel(ip1);
	addPanel(ip2);
	addPanel(ip3);
        dp = new drawPanel(W, H); 
	dp.setDrawBounds(0,0, W, H);

	
	readValues();
    } // end of fillPanels


    public void readValues()
    {
	w = (int) ip1.getValue(0);
	h = (int) ip1.getValue(1);

	N = Math.min((int) ip2.getValue(0), (int)(h*w/(6*R*R)));
	R = ip2.getValue(1);
	T = ip2.getValue(2);

	S = (int) ip3.getValue(0);
	nIter = (int) ip3.getValue(1);

	calculateDependents();
	initializeBalls();	    

	updateDrawing();
    } // end of readValues



    // update frame, ball, path
    public void updateDrawing()
    {
	dp.clear();
	dp.addLine(xmin-R, ymin-R, xmax+R, ymin-R);
	dp.addLine(xmax+R, ymin-R, xmax+R, ymax+R);
	dp.addLine(xmax+R, ymax+R, xmin-R, ymax+R);
	dp.addLine(xmin-R, ymax+R, xmin-R, ymin-R);	

	for (int i=0; i<N; i++)
	{
	    // String div = "-";
	    // for (int l=0; l<100; l++) {div+="-";}
	    // System.out.println(div);
	    // System.out.println("\nn\tx\ty\tdx\tdy\tspeed\n\n");
	    // String t = "\t";
	    // for (int n=0; n<N; n++)
	    // {
	    // 	System.out.println(
	    //        String.format("%d\t%5.2f\t%5.2f\t%5.2f\t%5.2f\t%5.2f", 
	    // 			 n+1, x[n], y[n], dx[n], dy[n], speed[n]));
	    // }



	    // draw ball
	    dp.addArc(x[i]-R,y[i]+R, 
		      2*R+1,2*R+1, 
		      0,360, 
		      Color.blue);

	    // draw velocity
	    dp.addLine(x[i],y[i],
		       x[i] + R * (double)(dx[i]/speed[i]), y[i] + R * (double)(dy[i]/speed[i]),
		       Color.blue);
	}
    
        addPanel(dp);
    } // end of drawPanel

    ///////////////////////////////
    // general purpose functions
    ///////////////////////////////
    public void fillArray(double[] Arr, int N, double V)
    {
	for (int i=0; i<N; i++)
	{
	    Arr[i] = V;
	}
    }

    public void fillArray2(double[][] Arr, int N, int M, double V)
    {
	for (int i=0; i<N; i++)
	{
	    for (int j=0; j<M; j++)
	    {
		Arr[i][j] = V;
	    }

	}
    }

    public double[] sumArray0(double[][] Arr, int N, int M)
    {
	double[] sum = new double[N];
	fillArray(sum, N, 0);

	for (int i=0; i<N; i++)
	{
	    for (int j=0; j<M; j++)
	    {
		sum[i] += Arr[i][j];
	    }
	}

	return sum;
    }
 
    public double[] rotate(double v1, double v2, double theta)
    {
	double w1 = v1 * Math.cos(theta) - v2 * Math.sin(theta);
	double w2 = v1 * Math.sin(theta) + v2 * Math.cos(theta);

	double [] ret = {w1,w2};
	return ret;
    }


    public double dot(double v1, double v2, double w1, double w2)
    {
	return (v1*w1 + v2*w2);
    }

    public double angle(double v1, double v2, double w1, double w2)
    {
	return dot(v1,v2,w1,w2) / (Math.pow(dot(v1,v2,v1,v2) * dot(w1,w2,w1,w2),2));
    }

    public double d(double v1, double v2, double w1, double w2)
    {
	return Math.sqrt(Math.pow((v1-w1),2) + Math.pow((v2-w2),2));
    }



} // end of applet