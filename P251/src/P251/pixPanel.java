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




/**
   Provides a canvas for bitmap drawing.
 */
public class pixPanel extends JPanel implements P251Panel
{

    private Color bkgColor;
    private Color fgColor;
    private Color [] data;
    
    private Dimension pix_dims;
    

    /**
       Constructor

       @param pixX x dimmension of the pixel map
       @param pixY y dimmension of the pixel map
       @param width width of the panel
       @param height of the panel
    */
    public pixPanel(int pixX, int pixY, int width, int height)
    {
	bkgColor = Color.white;
	fgColor = Color.black;
	data = new Color[pixX*pixY];
	pix_dims = new Dimension (pixX,pixY);
	setPreferredSize(new Dimension(width, height));
	
	clear();

    }
    
    /**
       Sets the background color for the pixel map.  If this color is
       changed the background, 'off' pixels, of pixel map will not be
       updated to reflect the change.  This is the color used for
       cleared pixels.

       @param color The color
     */
    public void setBackgroundColor(Color color)
    {
	bkgColor = color;
	
    }
    
    /**
       Sets the default color of 'on' pixels.  If this value is
       changed, 'on' pixels will not change color.
       
       @param color The color
     */
    public void setDefaultColor(Color color)
    {
	fgColor = color;
	
    }
    
    /**
       Sets a pixel to be 'on' with the specified color.
       The indexes start counting from 0.

       @param x The x coordinate
       @param y The y coordinate
       @param color The color of the pixel
       
     */
    public void setPixel(int x, int y,Color color)
    {
	data[cord_to_index(x,y)] = color;
	
    }
    
    /**
       sets a pixes to be 'on' with the default color

       @param x The x coordinate
       @param y The y coordinate
       
       @see #setDefaultColor(Color color)
     */
    public void setPixel(int x, int y)
    {
	setPixel(x,y,fgColor);
	
    }

    /**
       sets a pixel to be 'off' and displays it as the background color
       
       @param x The x coordinate
       @param y The y coordinate

       @see #setBackgroundColor(Color color)
     */
    public void clearPixel(int x, int y)
    {
	setPixel(x,y,bkgColor);
    }
    
    
    /**
       overrides paint function
     */
    public void paint(Graphics g)
    {
	Dimension pan_sz = getSize();
	
	int pix_sz_y = pan_sz.height/pix_dims.height;
	int pix_sz_x = pan_sz.width/pix_dims.width;

	int pix_sz = pix_sz_x<pix_sz_y ?pix_sz_x : pix_sz_y;


	
	int x_offset = 0;
	int y_offset = 0;
	

	Color tmp = g.getColor();
	for(int j = 0; j<pix_dims.width;++j){
	    for(int k = 0; k<pix_dims.height;++k){
		g.setColor(data[cord_to_index(j,k)]);
		g.fillRect(j*pix_sz + x_offset, k*pix_sz + y_offset
			 ,pix_sz,pix_sz);
		
	    }
	}
	g.setColor(tmp);
	
    }

    /**
       sets all pixels to background color
     */
    public void clear()
    {
	for(int j = 0; j<pix_dims.height * pix_dims.width; ++j){
	    data[j] = bkgColor;
	}
	
	repaint();
    }
    
    
    private int cord_to_index(int x, int y)
    {
	return pix_dims.width * y + x;
    }
    
    

    
				     
    
    

}
