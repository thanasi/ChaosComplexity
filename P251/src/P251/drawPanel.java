package P251;

import java.awt.*;
import javax.swing.*;
import java.util.Vector;


/**
   Class to wrap around a panel to make drawing easier
*/
public class drawPanel extends JPanel implements P251Panel
{   

    private Color defaultColor;
    private Color backgroundColor;
    
    private Vector<p251_shape> shapes;
    /**
       bounds of the image
    */
    private float bounds [];
    /**
       Constructor.
       @param width width of the panel in the applet
       @param height height of the panel in the applet
       @param xmin the minimum x that the panel will display in it's units
       @param ymin the minimum y that the panel will display in it's units
       @param xmax the maximum x that the panel will display in it's units
       @param ymax the maximum y that the panel will display in it's units
    */
    public drawPanel(int width,int height,float xmin,float ymin,float xmax,float ymax)
    {
	shapes=  new Vector<p251_shape>();
	bounds = new float[4];
	
	setDrawBounds(0,0,100,100);
	
	setBackground(Color.red);
	setPreferredSize( new Dimension(width,height));

	defaultColor = Color.black;
	backgroundColor = Color.white;
	
    }
    /**
       Constructor. assumes (0,0,100,100) for bounds
       @param width width of the panel in the applet
       @param height height of the panel in the applet
    */
    public drawPanel(int width,int height)
    {
	this(width,height,0,0,100,100);
    }
    
    
    /**
       Adds a line do be drawn.  The positions are given in the panel's units.

       @param xStart x position of start point
       @param yStart y position of start point
       @param xEnd x position of end point
       @param yEnd y position of end point
       @param color color of the line
    */
    public void addLine(double xStart,double yStart,double xEnd,double yEnd ,Color color)
    {
	shapes.add(new line(xStart,yStart,xEnd,yEnd,color));
	
    }
    
    /**
       Adds a line do be drawn.  The positions are given in the panel's units.

       @param xStart x position of start point
       @param yStart y position of start point
       @param xEnd x position of end point
       @param yEnd y position of end point
    */
    public void addLine(double xStart,double yStart,double xEnd,double yEnd )
    {
	shapes.add(new line(xStart,yStart,xEnd,yEnd,defaultColor));
	
    }
    
    /**
       Adds an arc to be drawn, follows the same meanings as Graphics.drawArc
    */
    public void addArc(double x,double y,double width,double height,int startAngle,int arcAngle,Color color)
    {
	shapes.add(new arc( x,y,width,height,startAngle,arcAngle,color));
    }

    /**
       Adds an arc to be drawn, follows the same meanings as Graphics.drawArc
    */
    public void addArc(double x,double y,double width,double height,int startAngle,int arcAngle)
    {
	shapes.add(new arc( x,y,width,height,startAngle,arcAngle,defaultColor));
    }

    /**
       Draws a string on to the panel

       @param txt string to be drawn
       @param x the x coordinate of where to put the string
       @param y the y coordinate of where to put the string
       @param color the color to make the text
     */
    public void addString(String txt, double x, double y, Color color)
    {
	shapes.add(new text(x,y,txt,color));
    }

    /**
       Draws a string on to the panel

       @param txt string to be drawn
       @param x the x coordinate of where to put the string
       @param y the y coordinate of where to put the string

     */
    public void addString(String txt, double x, double y)
    {
	shapes.add(new text(x,y,txt,defaultColor));
    }
    
    
    /**
       sets the bounds of the window in it's own terms
       @param xmin the minimum x that the panel will display in it's units
       @param ymin the minimum y that the panel will display in it's units
       @param xmax the maximum x that the panel will display in it's units
       @param ymax the maximum y that the panel will display in it's units

    */
    public void setDrawBounds(float xmin,float ymin,float xmax,float ymax)
    {
	bounds[0] = xmin;
	bounds[1] = ymin;
	bounds[2] = xmax;
	bounds[3] = ymax;

	
    }
    

    /**
       Set the background color
       
       @param color the color to set as the new background
     */
    public void setBackgroundColor(Color color)
    {
	backgroundColor = color;
    }
    
    
    /**
       Set the default draw color
       
       @param color the new default draw color
     */
    public void setDrawColor(Color color)
    {
	defaultColor = color;
    }
    

    
    /**
       Over ridden paint function.  You should not need to worry about
       the details of this function.
     */
    public void paint(Graphics g_in)
    {
	Dimension rv = null;
	
	rv = getSize(rv);
	
	Graphics2D g = (Graphics2D) g_in;
	g.setBackground(backgroundColor);
	//	g.setColor(backgroundColor);
	
	g.clearRect(0,0,rv.width,rv.height);
	
	g.setColor(defaultColor);
	// loop over lines
	for(p251_shape s :shapes){
	    s.draw_to_cavnas(g);
	}
	
    }
    
    /**
       scales between the panel units and screen units for X
    */
    private int scalex(double in)
    {
	return (int) ((in-bounds[0])/(bounds[2]-bounds[0])*getSize().width);
	
    }
    /**
       scales between the panel units and screen units for Y
    */
    private int scaley(double in)
    {
	return (int) ((in-bounds[1])/(bounds[3]-bounds[1])*getSize().height);

    }
    
    /**
       Removes all shapes from display.
     */
    public void clear()
    {
	shapes.clear();
	repaint();
	
    }
    
    /**
       internal interface for shape objects
     */
    interface p251_shape
    {
	void draw_to_cavnas(Graphics g);
    }


    /**
       internal class to represent a line, the rest of these classes
       are basically the same, this one is documented, the rest are not.
     */
    class line implements p251_shape
    {
	// location data
	double x1_,y1_,x2_,y2_;
	// color data
	Color c_;
	
	/**
	   Constructor. all it really does is set the variables 
	 */
	line(double x1,double y1,double x2,double y2,Color color)
	{
	    x1_ = x1;
	    y1_ = y1;
	    x2_ = x2;
	    y2_ = y2;
	    c_ = color;
	    
	}

	/**
	   Draws image to the graphics object.  This is really just
	   a wrapper for the appropriate graphics function.
	 */
	public void draw_to_cavnas(Graphics g)
	{
	    // get current color
	    Color tmp = g.getColor();
	    // set color to be the color we want
	    g.setColor(c_);
	    // draw the objcet
	    g.drawLine(scalex(x1_),scaley(y1_),scalex(x2_),scaley(y2_));
	    // return the color to what it was to avoid side effects
	    g.setColor(tmp);
	    
	}
    }


    class arc implements p251_shape
    {
	double x_;double y_;double width_;double height_;
	int startAngle_;int arcAngle_;
	Color c_;
	
	arc(double x,double y,double width,double height,int startAngle,int arcAngle,Color color)
	{
	    x_          = x;	 
	    y_          =y;	 
	    width_      =width;	 
	    height_     =height;	 
	    startAngle_ =startAngle;
	    arcAngle_  =arcAngle;  
	    c_ = color;
	    
	}
	

	public void draw_to_cavnas(Graphics g)
	{
	    Color tmp = g.getColor();
	    g.setColor(c_);
	 
	    g.drawArc(scalex(x_),scaley(y_),scalex(width_),scaley(height_),startAngle_,arcAngle_ );

	    g.setColor(tmp);
	    
	}
    }
    
    class text implements p251_shape
    {
	double x_,y_;
	String txt_;
	Color c_;
	
	text(double x,double y, String txt,Color color)
	{
	    x_ = x;
	    y_ = y;
	    txt_ = txt;
	    c_ = color;
	    
	}
	public void draw_to_cavnas(Graphics g)
	{
	    Color tmp = g.getColor();
	    g.setColor(c_);
	    g.drawString(txt_,scalex(x_),scaley(y_));
	    g.setColor(tmp);
	    
	}
	
    }
    
}
