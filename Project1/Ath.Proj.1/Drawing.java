// Drawing.java
// first class to test drawing
import javax.swing.*;
import java.awt.Color;
import P251.*;

public class Drawing extends P251Applet
{

    private drawPanel dp;
    
    public void fillPanels()
    {
	dp = new drawPanel(600,600);
	dp.setBackgroundColor(Color.yellow);
	addPanel(dp);
    }

    public void compute() 
    {
	dp.setDrawBounds(0f,0f, 100f, 100f);
	dp.addLine(5,10, 60,15, Color.red);
	dp.addArc(60-5,15+5, 10,10, 0,180, Color.magenta);
	dp.addLine(60,15, 60,15, Color.black);
	dp.repaint();
    }
    
}