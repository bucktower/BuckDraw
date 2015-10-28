package net.bucktower.buckdraw;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class Shape {

	private Color fill;  // not every shape will make use of these, but they will all have them.
	private Color stroke;
	
	public static final int LINE_TYPE = 1; // "static" means this single variable belongs to this
	public static final int RECT_TYPE = 2; // class and is shared by all the members of the class.
	public static final int OVAL_TYPE = 3; // "final" means it's a constant. So these TYPE variables
	public static final int POLYGON_TYPE = 4; // are shared constants that are publicly accessible.

	
	public Shape(String[] data, int startIndex)
	{
		try
		{
			if (!data[startIndex].equals(""))
				setFill(new Color(Integer.parseInt(data[startIndex]),
							   Integer.parseInt(data[startIndex+1]),
							   Integer.parseInt(data[startIndex+2])));
			if (!data[startIndex+3].equals(""))
				setStroke(new Color(Integer.parseInt(data[startIndex+3]),
							   Integer.parseInt(data[startIndex+4]),
							   Integer.parseInt(data[startIndex+5])));
			
		}catch (NumberFormatException nfe)
		{
			System.out.println("Trouble reading color data.");
			nfe.printStackTrace();
		}
	}
	/**
	 * a convenience overload of the constructor - assumes that we have an array of strings and
	 * that the first string in the array was the kind, which we have used already, so the load
	 * will start with the second item in the array, at index 1.
	 * @param data
	 */
	public Shape(String[] data)
	{
		this(data,1);
	}
	
	public Shape()
	{

	}
	
	/**
	 * returns the number of parameters (spreadsheet columns) needed to initialize this object,
	 * not including the type.
	 * @return how many
	 */
	public abstract int numParamsNeeded();
	
	/**
	 * draws this shape (and just this shape) into the Graphics context, g.
	 * @param g
	 */
	public abstract void drawSelf(Graphics g);
	
	/**
	 * creates a string representation of this Shape that could be used by the constructor to make another one.
	 */
	public String toString()
	{
		// this portion will return out the type, the fill and the stroke, with a trailing tab.
		String result = getType()+"\t";
		if (getFill() == null)
			result+="\t\t\t";
		else
			result+=getFill().getRed()+"\t"+getFill().getGreen()+"\t"+getFill().getBlue()+"\t";
		if (getStroke() == null)
			result+="\t\t\t";
		else
			result+=getStroke().getRed()+"\t"+getStroke().getGreen()+"\t"+getStroke().getBlue()+"\t";
		return result;
	}
	
	/**
	 * used by the canvas to determine whether a user's click would be able to select this item.
	 * @param x
	 * @param y
	 * @return whether this Shape can be selected by a click at (x,y).
	 */
	public abstract boolean containsPoint(double x, double y);

	/**
	 * used by the canvas to determine whether a user's drag-select would be able to select this item.
	 * @param r - the selection rectangle
	 * @return whether this shape is contained inside this rectangle.
	 */
	public abstract boolean containedInRect(Rectangle r);
	
	/**
	 * gives back a code number (one of the constants in the Shape class) indicating what type 
	 * of shape this is.
	 * @return Shape type.
	 */
	public abstract int getType();

	/**
	 * @return the fill
	 */
	public Color getFill() {
		return fill;
	}

	/**
	 * @param inFill - the fill to set
	 */
	public void setFill(Color inFill) {
		fill = inFill;
	}

	/**
	 * @return the stroke
	 */
	public Color getStroke() {
		return stroke;
	}

	/**
	 * @param inStroke - the stroke to set
	 */
	public void setStroke(Color inStroke) {
		stroke = inStroke;
	}
	
	
}
