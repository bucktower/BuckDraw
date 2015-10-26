import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JPanel;

public class FDCanvas extends JPanel implements MouseListener, MouseMotionListener{

	private int currentTool;
	private Color fillColor, strokeColor;
	private BufferedImage buffer;
	private boolean shapeListHasChangedSinceLastDraw;
	private Shape tempShape;
	private int mouseStartX, mouseStartY;
	
	private ArrayList<Shape> shapes;
	
	public FDCanvas()
	{
		super();
		currentTool = 0;
		addMouseListener(this);
		addMouseMotionListener(this);
		shapeListHasChangedSinceLastDraw = true;
		
		shapes = new ArrayList<Shape>();
	}
	
	/*
	 * This is a little bit non-traditional, for the sake of performance. We are using a "buffered" image to
	 * draw all the shapes in your list - but only when you've changed the contents of the list.
	 * This way, you don't have to wait to draw all the shapes every time something on screen changes,
	 * like a selection rectangle or a dragged rectangle in progress, which you can draw above the rest.
	 */
	public void paintComponent(Graphics g)
	{	
		super.paintComponent(g);
		if (shapeListHasChangedSinceLastDraw)
		{
			buffer = new BufferedImage(getBounds().width, getBounds().height,BufferedImage.TYPE_INT_RGB);
			Graphics2D g2 = buffer.createGraphics();
			super.paintComponent(g2);
			// now draw all the shapes in our list into g2, instead of g.
			//---------------------------------------------
			// Temporary code to demonstrate that the toolbox buttons work....
			//  ......  you'll want something different here.
			for(Shape s: shapes) {
				s.drawSelf(g2);
			}
			//-----------------------------------------------
			
			// update the shapeListHasChangedSinceLastDraw state:
			shapeListHasChangedSinceLastDraw = false;
		}
		g.drawImage(buffer, 0, 0, null);
		
		// now if you want to draw temporary shapes, like selection rectangles or new
		// objects still being dragged, you can draw them into "g" here.
		if (tempShape!=null)
			tempShape.drawSelf(g);
		
		
	}
	// --------------------------------------------------
	// Respond to toolbar changes - these methods are called by the FDToolbar.
	public void setTool(int t)
	{
		currentTool = t;
		shapeListHasChangedSinceLastDraw = true;
		repaint();
	}
	
	public void setFillColor(Color c)
	{
		fillColor = c;
		shapeListHasChangedSinceLastDraw = true;
		repaint();
	}
	
	public void setStrokeColor(Color c)
	{ 
		strokeColor = c;
		shapeListHasChangedSinceLastDraw = true;
		repaint();
	}
	//---------------------------------------------------
	
	//----------------------------------------------
	// Respond to File menu items.... these methods are called by FalconDrawFrame.
	
	public void handleNewMenuItem()
	{
		System.out.println("The user just selected 'New.'");
	}
	
	public void openFile(File theFile)
	{
		System.out.println("The user just opted to open file: "+theFile);
		try {
			Scanner dataFile = new Scanner(theFile);
			System.out.println(dataFile.hasNext());
			
			while(dataFile.hasNext()){
				String line = dataFile.nextLine();
				String[] parts = line.split("\t");
				
				Color fill = new Color(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
				Color stroke = new Color(Integer.parseInt(parts[4]), Integer.parseInt(parts[5]), Integer.parseInt(parts[6]));
				
				switch(Integer.parseInt(parts[0])) {
				case 1: // line
					System.out.println("Creating a line from file.");
					FDLine tempLine = new FDLine(parts);
					shapes.add(tempLine);
					break;
				case 2: // rectangle
					System.out.println("Creating a rectangle from file.");
					FDRect tempRect = new FDRect(parts);
					shapes.add(tempRect);
					break;
				default:
					System.out.println("Shape type not recognized when loading shapes");
					break;
				}
			}
			dataFile.close();
			shapeListHasChangedSinceLastDraw = true;
			repaint();
		} catch (FileNotFoundException fnfe) {
			System.out.println("Could not find file.");
		}
	}
	
	public void saveFile(File theFile)
	{
		System.out.println("The user just opted to save file: "+theFile);
		
		// creating a file
        PrintWriter printWriter = null;
                
        try
        {
            printWriter = new PrintWriter(theFile);
            for(Shape s: shapes) {
            	printWriter.println(s.toString());
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if ( printWriter != null ) 
            {
                printWriter.close();
            }
        }
	}
	//-----------------------------------------------
	
	//-----------------------------------------------
	// Respond to Edit menu items... these methods are called by FalconDrawFrame.
	public void handleCutMenuItem()
	{
		System.out.println("User selected cut.");
	}
	public void handleCopyMenuItem()
	{
		System.out.println("User selected copy.");
	}
	public void handlePasteMenuItem()
	{
		System.out.println("User selected paste.");
	}
	public void handleDuplicateMenuItem()
	{
		System.out.println("User selected duplicate.");
	}
	public void handleSelectAllMenuItem()
	{
		System.out.println("User selected select all.");
	}
	public void handleGroupMenuItem()
	{
		System.out.println("User selected group.");
	}
	//-----------------------------------------------

	//-----------------------------------------------
	// Respond to the mouse  - you have to have all of these methods, but some or all can be "empty."
	public void mousePressed(MouseEvent me)
	{
		System.out.println("User pressed mouse at: "+me.getX()+", "+me.getY());
		mouseStartX = me.getX();
		mouseStartY = me.getY();
		if (currentTool == Shape.LINE_TYPE)
		{
			FDLine tempLine = new FDLine();
			tempLine.setFill(fillColor);
			tempLine.setStroke(strokeColor);
			tempLine.setPoints(mouseStartX, mouseStartY, mouseStartX, mouseStartY);
			tempShape = tempLine;
		}
		else if (currentTool == Shape.RECT_TYPE)
		{
			FDRect tempRect = new FDRect();
			tempRect.setFill(fillColor);
			tempRect.setStroke(strokeColor);
			tempRect.setPoints(mouseStartX, mouseStartY, mouseStartX, mouseStartY);
			tempShape = tempRect;
		}
	}
	public void mouseReleased(MouseEvent me)
	{
		if(tempShape != null) {
			if (currentTool == Shape.LINE_TYPE)
			{
				shapes.add(tempShape);
				shapeListHasChangedSinceLastDraw = true;
				tempShape = null;
				repaint();
			}
			else if (currentTool == Shape.RECT_TYPE)
			{
				shapes.add(tempShape);
				shapeListHasChangedSinceLastDraw = true;
				tempShape = null;
				repaint();
			}
		}
		System.out.println("User released mouse at: "+me.getX()+", "+me.getY());
	}
	public void mouseClicked(MouseEvent me) // this means "released the mouse at the same place as it was first pressed."
	{
		System.out.println("User clicked mouse at: "+me.getX()+", "+me.getY());
	}
	public void mouseEntered(MouseEvent me)
	{
		System.out.println("User entered mouse at: "+me.getX()+", "+me.getY());
	}
	public void mouseExited(MouseEvent me)
	{
		if (currentTool == Shape.LINE_TYPE)
		{
			tempShape = null;
			repaint();
		}
		if (currentTool == Shape.RECT_TYPE)
		{
			tempShape = null;
			repaint();
		}
		System.out.println("User exited mouse at: "+me.getX()+", "+me.getY());
	}
    //-----------------------------------------------
	// Respond to the mouse in motion  - you have to have all of these methods, but some or all can be "empty."
	public void mouseMoved(MouseEvent me)
	{
		// only activate this if needed - it will slow the program down.
		//System.out.println("User moved mouse at: "+me.getX()+", "+me.getY());
	}
	public void mouseDragged(MouseEvent me)
	{
		if (currentTool == Shape.LINE_TYPE)
		{
			if (tempShape != null)
			((FDLine)tempShape).setPoints(mouseStartX,mouseStartY,me.getX(),me.getY());
			repaint();
		}
		if (currentTool == Shape.RECT_TYPE)
		{
			if (tempShape != null)
			((FDRect)tempShape).setPoints(mouseStartX,mouseStartY,me.getX(),me.getY());
			repaint();
		}
	}
	
}
