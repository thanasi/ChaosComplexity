package P251;




import javax.swing.*;
import javax.swing.text.*;
import java.util.Vector;



/**
   class to deal with text input
 */
public class inputPanel extends JPanel 
{

    private Vector<JTextComponent> txtFields;
    private Vector<Double> init_vals;
    private Vector<String> names;
    
    
    /**
       Constructor.  Takes no arguments
     */
    public inputPanel()
    {
	txtFields = new Vector<JTextComponent>();
	init_vals = new Vector<Double>();
	
    }
    
    /**
       Adds an input field.  The order the fields are added is
       important for the order they will appear and for referencing
       to get data from them

       @param name The name of the field
       @param initialValue The initial value of the field
     */
    public void addField(String name,double initialValue)
    {
	JPanel tmp = new JPanel();
	
	tmp.add(new JLabel(name));
	init_vals.add(new Double(initialValue));
	txtFields.add(new JTextField(init_vals.lastElement().toString(),10));
	tmp.add(txtFields.lastElement());

	add(tmp);
	

	
    }
    

    /**
       Returns a vector containing the parsed values of all the
       fields.  This does not do any error checking and will throw
       an error of the text entered in the box is not a well formed
       double.

       
     */
    public Vector<Double> getValues()
    {
	Vector<Double> tmp = new Vector<Double>();
	
	for(JTextComponent f : txtFields){
	    tmp.add(new Double(f.getText()));
	}
	
	
	return tmp;
	
	
    }
    

    /**
       Returns the value of a single field.  The index is set by the order
       of addition to the inputPanel.  The counting starts from 0.

       @param index The index of the field to read
     */
    public double getValue(int index)
    {
	
	return new Double(txtFields.elementAt(index).getText()).doubleValue();
    }
    

    
    /**
       Resets all fields to initial values
     */
    public void resetValues()
    {
	
	for(int j =0;j<txtFields.size();++j){
	    setValue(j,init_vals.elementAt(j));
	}
    }


    /**
       Sets a field to a value.   The index is set by the order
       of addition to the inputPanel.  The counting starts from 0.

       @param index The index of the field to set
       @param val The value to set the field to
    */
    public void setValue(int index, double val)
    {
	txtFields.elementAt(index).setText(new Double(val).toString());
    }

    /**
       Sets a field to a value The index is set by the order
       of addition to the inputPanel.  The counting starts from 0.

       
       @param index The index of the field to set
       @param val The value to set the field to

     */
    public void setValue(int index, Double val)
    {
	txtFields.elementAt(index).setText(val.toString());
    }
    
    

}
