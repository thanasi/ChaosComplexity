// Copyright 2009 Thomas A Caswell 
//
// This program is free software: you can redistribute it and/or
// modify it under the terms of the GNU General Public License as
// published by the Free Software Foundation, either version 3 of the
// License, or (at your option) any later version.
//
// This program is distributed in the hope that it will be useful, but
// WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see
// <http://www.gnu.org/licenses/>.
package P251;



import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;
import java.util.Vector;

/**
   Wrapper class for applets for use in P251
 */
public abstract class P251Applet extends JApplet {
    
    private JPanel button_panel;
    private     JPanel disp_panel;
	
    private Vector<P251Panel> panels;
    
    

    private SwingWorker5 worker;
    
    private ActionListener stopListener = new ActionListener(){
	    public void actionPerformed(ActionEvent event)
	    {
		worker.interrupt();

	    }
	};
    
    private ActionListener startListener = new ActionListener(){
	    public void actionPerformed(ActionEvent event) 
	    {

		
	
		worker = new SwingWorker5(){
			public Object construct()
			{
			    return internal_compute();
			}
		    };
		worker.start();
	    }
    

 	};
    
    private ActionListener clearListener = new ActionListener(){
	    public void actionPerformed(ActionEvent event)
	    {
		for(P251Panel p : panels)
		    p.clear();
	    }
	};
    private ActionListener resetListener = new ActionListener(){
	    public void actionPerformed(ActionEvent event)
	    {
		initValues();
	    }
	};
    private ActionListener readListener = new ActionListener(){
	    public void actionPerformed(ActionEvent event)
	    {
		readValues();
	    }
	};

    
    /**
       Method that initializes the applet.  You should not need to
       override or explicitly call this function, however it needs to
       be public for the applet to work.
    */
    public void init() 
    {
	try{
	    SwingUtilities.invokeAndWait(new Runnable() {
		    public void run() {
			create_gui();
		    }
		});
	}
	catch(Exception e){
	    System.err.println("Something has gone horribly wrong, please try again or contact a TA");
	    System.err.println(e.getCause());
	    System.err.println(e.getMessage());
	    System.err.println(e.toString());

	}
    
    }
    private void create_gui()
    {
	panels = new Vector<P251Panel>();
	

	
	
	JPanel content_pane = new JPanel(new BorderLayout());
	setContentPane(content_pane);
	button_panel = new JPanel(new FlowLayout());
	disp_panel = new JPanel(new FlowLayout());
	
		
		
	JButton start_button = new JButton("Start Computation");
	start_button.addActionListener(startListener);
	JButton stop_button = new JButton("Interrupt Computation");
	stop_button.addActionListener(stopListener);
	JButton clear_button = new JButton("Clear display");
	clear_button.addActionListener(clearListener);
	JButton reset_button = new JButton("Re-Initialize");
	reset_button.addActionListener(resetListener);
	JButton read_button = new JButton("Read Input");
	read_button.addActionListener(readListener);
	
	button_panel.add(read_button);
	button_panel.add(start_button);
	button_panel.add(stop_button);
	button_panel.add(clear_button);
	button_panel.add(reset_button);
	
	fillPanels();
	initValues();
	
	add(disp_panel, BorderLayout.CENTER);
	add(button_panel, BorderLayout.SOUTH);
	
	

    }

    /**
       Adds a panel to the main display region of your applet. 
       Any JComponent can be added, however the objects should 
       implement P251Panel to be registered so that they are cleared
       by the 'Clear display' button.
    */
    public void addPanel(JComponent component)
    {
	disp_panel.add(component);
	if(component instanceof P251Panel){
	    panels.add((P251Panel)component);
	}
	
	
    }
    /**
       Adds a button to the button region of your applet.  This
       is only needed to add more buttons than default.
    */
    public void addButton(JComponent button)
    {
	button_panel.add(button);
    }
    
    /**
        Method to build your applet.  You must override this method to
	initialize and add any panels or additional buttons to your
	applet.  This is the first user defined function to run, no
	local variables will be initialized.

    */
    public abstract void fillPanels();
    
    /**
       This is an optional initialization function. This is called
       after fillPanels() during applet setup, thus it is always safe
       to assume that any variables defined in .  This function is also called by the default
       'Re-initialize' button.  If this function is empty, then this
       button will do nothing.
    */
    public void initValues(){};

    
    /**
       Method for your applet to do computation.  This must be override
       and is run when the start button is hit.  In order for the 'Interrupt Computation'
       button to work the method must contain the following code:
       {@code 
       if (Thread.interrupted()) 
       {
       return; 
       }
       }
       at the point where you want it to be able to break.
    */
    public abstract void compute();
    
    /**
       This is an optional function for reading in data.  This is the function
       called when the 'Read Input' button is pushed.  If this function is not
       overridden then the button will do nothing.       
     */
    public void readValues()
    {
    }
 



    /**
       Internal wrapper for compute() to make it work with a void
       function and the swing worker classes
     */
    private Object internal_compute(){
	compute();
	return "no return value";
    };

    
}


