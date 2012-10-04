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



import java.awt.*;
import javax.swing.*;


import java.util.Vector;




import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartPanel;
import org.jfree.data.xy.XYSeries;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.Range;
import org.jfree.chart.renderer.xy.XYDotRenderer;
import org.jfree.data.xy.XYSeriesCollection;

/**
   Wrapper class for JFreeChart that produces a scatter plot.
 */
public class graphPanel extends JPanel implements P251Panel
{
    
    private XYSeriesCollection dataset;
    private ChartPanel chart;



    /**
       Irrecoverably removes all data from the graph.  
     */
    public void clear()
    {
	dataset.removeAllSeries();
	
	
    }
    
    /**
       Sets the background color of the graph.
     */
    public void setBackgroudColor(Color color)
    {
	chart.getChart().getXYPlot().setBackgroundPaint(color);
	
    }

    /**
       Sets the color of the grid lines on the graph.  To get rid of the
       grid, set to be the same color as the background.
     */
    public void setGridColor(Color color)
    {
	chart.getChart().getXYPlot().setRangeGridlinePaint(color);
	chart.getChart().getXYPlot().setDomainGridlinePaint(color);
    }
    
    /**
       Sets the y-axis label
     */
    public void setYLabel(String label)
    {
	chart.getChart().getXYPlot().getRangeAxis().setLabel(label);
	
    }
    /**
       Sets the x-axis label
     */
    public void setXLabel(String label)
    {
	chart.getChart().getXYPlot().getDomainAxis().setLabel(label);
	
    }

    /**
       Sets the title
     */
    public void setTitle(String title)
    {
	chart.getChart().setTitle(title);
    }
    
    
    /**
       Constructor

       @param width width of the graph in the applet
       @param height height of the graph in the applet
       @param legend to include a legend
     */
    public graphPanel (int width ,int height, Boolean legend)
    {

	
	
	//	setLayout( new FlowLayout());
	dataset = new XYSeriesCollection();
	chart = new ChartPanel(ChartFactory.createScatterPlot("XY Chart", // Title
							      "x-axis", // x-axis Label
							      "y-axis", // y-axis Label
							      dataset,legend));
	

	chart.getChart().getXYPlot().setRenderer(new XYDotRenderer());;
	setBackgroudColor(Color.white);
	setGridColor(Color.black);
	
	chart.setPreferredSize( new Dimension(width-10,height-10));
	add(chart);
	setPreferredSize( new Dimension(width,height));
    }

    /**
       sets the size of the points on the scatter plot.   The default size is 1.
       The minimum size is 1.

       @param size size of the point on the graph, if size < 1 it is set to 1
     */
    public void setDotSize(int size)
    {
	if(size <1)
	    size = 1;
	
	((XYDotRenderer)chart.getChart().getXYPlot().getRenderer()).setDotWidth(size);
	((XYDotRenderer)chart.getChart().getXYPlot().getRenderer()).setDotHeight(size);
    }
    
    /**
       Adds data to the plot.  Checks that x,y are the same length
       and then adds them to the named series pairwise.
     */
    public void addData(double[] x,double[] y,String name)     {
	
	XYSeries tmp_series;
	
	try{
	    tmp_series = dataset.getSeries(name);
	}
	catch(org.jfree.data.UnknownKeyException e){
	    tmp_series = new XYSeries(name);
	    dataset.addSeries(tmp_series);
	}
	
	System.out.println("spot 1");
	

	if(x.length != y.length){
	    System.out.println("sizes don't match");
	    return;
	    
	}
	for(int j = 0; j<x.length;++j){
	    tmp_series.add(x[j],y[j],false);
	}
	tmp_series.fireSeriesChanged();
	
	
    }
    
    
    /**
       resizes the axis to the given limits
     */
    public void resizeAxis(double d0,double d1,double r0,double r1)
    {
	XYPlot plot = (XYPlot) chart.getChart().getPlot();
	plot.getDomainAxis().setAutoRange(false);
	plot.getDomainAxis().setRange(d0,d1);
	plot.getRangeAxis().setAutoRange(false);
	plot.getRangeAxis().setRange(r0,r1);
	System.out.println(plot.getDomainAxisCount());
	System.out.println(plot.getRangeAxisCount());

	// plot.getRangeAxis().setDefaultAutoRange(new Range(0,1));

    }


    // can change the strings to comparables, but what is the point?
    

    /**
       Bins and adds to the data series named name. if a data series
       with that name already exists,  if no
       series with name exists a new data series is created.
       Binning  is useful if
       there are many redundant points in the data.   
       The data is
       binned between [bottom,top). Data out side of this range are
       discarded.   Plots points at the center of bins that non-zero
       counts.
 
       @param x the x coordinate of all of the data
       @param data the x-coordinate data to be binned
       @param name name of the data series
       @param bottom the minimum value of the data to be binned, any
       data less than bottom is discarded
       @param top the maximum value of the data to be binned, any value
       equal to or greater than this value will be discarded
       @param binCount the number of bins to use, more bins gives finer resolution

       @see #addDataBinning( double [] data, double y, String name,double bottom, double top,int binCount)
     */
    public void addDataBinning(double x, double [] data, String name,double bottom, double top,int binCount)
    {
	XYSeries tmp_series;
	
	try{
	    tmp_series = dataset.getSeries(name);
	}
	catch(org.jfree.data.UnknownKeyException e){
	    tmp_series = new XYSeries(name);
	    dataset.addSeries(tmp_series);
	}
	
		
	    
	// add a catch here to deal with if the name is wrong

	// make histogram of values

	int hist [] = new int [binCount];
	for(double val : data){
	    // watch the logic here, protecting bounds
	    if(val>=bottom&&val<top){
		++hist[(int)Math.floor(binCount * val)];
	    }
	}
	
	// pick out non-zero entries,add those entries to series
	for(int j= 0;j<binCount;++j){
	    if(hist[j]>0){
		tmp_series.add(x,((double)j)/binCount + (top-bottom)/(2*binCount),false);
	    }
	}
	tmp_series.fireSeriesChanged();
	
	
    }
    
    /**
       Bins and adds to the data series named name. if a data series
       with that name already exists,  if no
       series with name exists a new data series is created.
       Binning  is useful if
       there are many redundant points in the data.   
       The data is
       binned between [bottom,top). Data out side of this range are
       discarded.   Plots points at the center of bins that non-zero
       counts.
       
       @param data the x-coordinate data to be binned
       @param y the y coordinate of all of the data
       @param name name of the data series
       @param bottom the minimum value of the data to be binned, any
       data less than bottom is discarded
       @param top the maximum value of the data to be binned, any value
       equal to or greater than this value will be discarded
       @param binCount the number of bins to use, more bins gives finer resolution

       @see #addDataBinning(double x, double [] data, String name,double bottom, double top,int binCount)

       
     */
    public void addDataBinning( double [] data, double y, String name,double bottom, double top,int binCount)
    {
	XYSeries tmp_series;
	
	try{
	    tmp_series = dataset.getSeries(name);
	}
	catch(org.jfree.data.UnknownKeyException e){
	    tmp_series = new XYSeries(name);
	    dataset.addSeries(tmp_series);
	}
	
		
	    
	// add a catch here to deal with if the name is wrong

	// make histogram of values

	int hist [] = new int [binCount];
	for(double x : data){
	    // watch the logic here, protecting bounds
	    if(x>=bottom&&x<top){
		++hist[(int)Math.floor(binCount * x)];
	    }
	}
	
	// pick out non-zero entries,add those entries to series
	for(int j= 0;j<binCount;++j){
	    if(hist[j]>0){
		tmp_series.add(((double)j)/binCount + (top-bottom)/(2*binCount),y,false);
	    }
	}
	tmp_series.fireSeriesChanged();
	
	
    }
    
}


