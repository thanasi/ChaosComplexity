// FirstOrbit.java
// an orbit calculation for particles bouncing off the top and bottom of a rectangle.
import javax.swing.*;
import P251.*;
public class FirstOrbit extends P251Applet
{
    private drawPanel dp; // declares the drawing panel object
    // declare variables
    int iold, jold, inew, jnew; // x and y coordinates of particle at beginning
    // and end of a flight between bounces
    int di; // advance of i in each bounce
    
    public void compute() // compute is executed when the corresponding applet button is pressed
    {
        // set initial values
        iold = 0;
        jold = 0;
        di = 22;
        // set up a loop to run through the same steps many times
        while (iold <= 200) 
        { // terminate loop when x coordinate exceeds 200
            inew = iold + di; // advance i
            jnew = 200 - jold; // go from top to bottom
            dp.addLine(iold,jold,inew,jnew); // draw line between points
            // start next line at endpoint of the line just drawn
            iold = inew;
            jold = jnew;
        } // end of while
        dp.repaint();
    }
    
    public void fillPanels() // specifies and outputs the drawing panel object to the applet
    {
        dp = new drawPanel(220,200); 
        dp.setDrawBounds(0,0,220,200);
        addPanel(dp);
    } // end of fillPanels
} // end of applet