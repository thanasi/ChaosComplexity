// fibo.java
// fibonacci java class
// write out fibonacci sequence and ratio
// Athanasios Athanassiadis
// Complexity, Project 1, F2013
// 10/3/2012

import java.applet.Applet;
public class Fibo extends Applet
{
    
    int i,j,k;
    int n, nsteps, ypos;
    double r;


    public void start()
    {
	nsteps = 20;

	i=1;
	j=1;
        System.out.println(" 0,1,1 ");

	for (n=1; n<=nsteps; n++)
	{
	    k = i+j;
	    r = (double) k / (double) j;
	    System.out.println(" " + n + "," + k + "," + r );
	    i = j;
	    j = k;
	}
    }

}