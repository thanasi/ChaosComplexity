package P251;


import javax.swing.*;
import javax.swing.event.*;


/**
   class to deal with text output
 */
public class outputPanel extends JPanel implements P251Panel
{

    private JTextArea txt;
    
    public void clear()
    {
	txt.setText("");
    }
    
    
    /**
       appends data to text area
     */
    public void addData(Double[] x,Double[] y,String name)  
    {
	if(x.length != y.length){
	    System.out.println("sizes don't match");
	    return;
	}
	txt.append("--- " + name + " ---\n");
	System.out.println("--- " + name + " ---");
	
	for(int j = 0; j<x.length;++j){
	    txt.append(x[j].toString() + "\t" +y[j].toString()+"\n");
	    System.out.println(x[j].toString() + "\t" +y[j].toString());
	}
	
	
    }
    	
    
    /**
       appends data to text area
     */
    public void addData(double[] x,double[] y,String name)  
    {
	if(x.length != y.length){
	    System.out.println("sizes don't match");
	    return;
	}
	txt.append("--- " + name + " ---\n");
	System.out.println("--- " + name + " ---");
	for(int j = 0; j<x.length;++j){
	    txt.append((new Double(x[j])).toString() + "\t" +(new Double(y[j])).toString()+"\n");
	    System.out.println((new Double(x[j])).toString() + "\t" +(new Double(y[j])).toString());
	}
	
	
    }
    	
    
    /**
       Constructor
     */
    public outputPanel(int a,int b)
    {
	txt = new  JTextArea(a,b);
	txt.setEditable(true);
	add(new JScrollPane(txt));
	
	
	
    }
    
    
}
