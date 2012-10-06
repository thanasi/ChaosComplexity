// Bounce.java
// bouncing ball
// with input panel

import javax.swing.*;
import java.awt.Color;
import P251.*;
public class Bounce extends P251Applet
{
    private drawPanel dp;
    private inputPanel ip1, ip2, ip3;


    // drawpanel size
    int W = 500;
    int H = 500;

    // number of iterations to run
    int N0 = 100;
    int niter = 100;
    int n;

    // size of box to bounce around in
    int w0 = 220;
    int h0 = 220;

    int width = w0;
    int height = h0;

    // i,j bounds
    int imin, imax, jmin, jmax;
    
    // sleep time
    int S0 = 100;
    int S;

    // ball radius
    int R0 = 15;
    int R;

    // path array initialization
    int[] iArr, jArr;
    
    // starting position
    int i,j;
    // starting velocity
    int di,dj;


    // initial values
    int i0 = W/2;
    int j0 = H/2;
    int di0 = 1;
    int dj0 = 2;

    public void initValues()
    {
	i = i0;
	j = j0;
	di = di0;
	dj = dj0;
	n = 0;
	niter = N0;
	width = w0;
	height = h0;
	S = S0; // sleep time
	
	imin = (W/2) - width/2 + R;
	imax = (W/2) + width/2 - R;
	jmin = (H/2) - height/2 + R;
	jmax = (H/2) + height/2 - R;


	iArr = new int[niter];
	jArr = new int[niter];

	for (int k=0; k<niter; k++)
	{
	    iArr[k] = -1;
	    jArr[k] = -1;
	}

	
    } // end of initValues


    public void compute() // compute is executed when the corresponding applet button is pressed
    {
	for (n=0; n<niter; n++)
	{
	    System.out.println(" " + (n+1) + " / " + niter);
	    // increment position based on velocities
	    i += di;
	    j += dj;

	    // check that position is valid
	    // if not, reverse relevant velocity and place ball on within the bounds
	    // treating the collision as elastic
	    if (i < imin)
	    {
		System.out.println("bounce left");
		i = 2*imin - i;
		di *= -1;
	    }

	    if (i>imax)
	    {
		System.out.println("bounce right");
		i = 2*(imax) - i;
		di *= -1;
	    }

	    if (j<jmin)
	    {
		System.out.println("bounce bottom");
		j = 2*jmin-j;
		dj *= -1;
	    }

	    if (j>jmax)
	    {
		System.out.println("bounce top");
		j = 2*jmax - j;
		dj *= -1;
	    }

	    if (Thread.interrupted()) return;

	    // update path info
	    iArr[n] = i;
	    jArr[n] = j;

	    // redraw the panel
	    updatePanel();

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
    public void fillPanels() // specifies and outputs the drawing panel object to the applet
    {

	initValues();

        ip1 = new inputPanel();
	ip1.addField("width", w0);
	ip1.addField("height", h0);

	ip2 = new inputPanel();
	ip2.addField("R", R0);
	ip2.addField("niter", N0);
	ip2.addField("sleep", S0);

	ip3 = new inputPanel();
	// ip3.addField("i", i0);
	// ip3.addField("j", j0);
	ip3.addField("di", di0);
	ip3.addField("dj", dj0);
	
	addPanel(ip1);
	addPanel(ip2);
	addPanel(ip3);
        dp = new drawPanel(W, H); 
	updatePanel();

    } // end of fillPanels


    public void readValues()
    {
	width = (int) ip1.getValue(0);
	height = (int) ip1.getValue(1);

	R = (int) ip2.getValue(0);
	niter = (int) ip2.getValue(1);
	S = (int) ip2.getValue(2);

	di = (int) ip3.getValue(0);
	dj = (int) ip3.getValue(1);
	
	iArr = new int[niter];
	jArr = new int[niter];

	// reset bounds
	imin = (W/2) - width/2 + R;
	imax = (W/2) + width/2 - R;
	jmin = (H/2) - height/2 + R;
	jmax = (H/2) + height/2 - R;

	// start the ball in the center
	i = (imin + imax) / 2;
	j = (jmin + jmax) / 2;

	System.out.println("imin -> " + imin);
	System.out.println("imax -> " + imax);
	System.out.println("jmin -> " + jmin);
	System.out.println("jmax -> " + jmax);

	// reset tracking
	for (int k=0; k<niter; k++)
	{
	    iArr[k] = -1;
	    jArr[k] = -1;
	}

	updatePanel();
    } // end of readValues



    // update frame, ball, path
    public void updatePanel()
    {
	dp.clear();
	dp.setDrawBounds(0,0, W, H);
	dp.addLine(imin-R, jmin-R, imax+R, jmin-R);
	dp.addLine(imax+R, jmin-R, imax+R, jmax+R);
	dp.addLine(imax+R, jmax+R, imin-R, jmax+R);
	dp.addLine(imin-R, jmax+R, imin-R, jmin-R);	

	dp.addArc(i-R,j+R, 2*R+1,2*R+1, 0,360, Color.blue);
	for (int k = 0; k<niter; k++)
	{
	    dp.addLine(iArr[k],jArr[k],
		       iArr[k],jArr[k],
		       Color.red);
	}

        addPanel(dp);
    } // end of drawPanel


} // end of applet