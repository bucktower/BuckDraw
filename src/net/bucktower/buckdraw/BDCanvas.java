package net.bucktower.buckdraw;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
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

public class BDCanvas extends JPanel implements MouseListener, MouseMotionListener{

	private int currentTool;
	private Color fillColor, strokeColor;
	private BufferedImage buffer;
	private boolean shapeListHasChangedSinceLastDraw;
	private Shape tempShape;
	private int mouseStartX, mouseStartY;
	
	private ArrayList<Shape> shapes;
	private ArrayList<Shape> shapesClipboard;
	
	public boolean isDirty;
	
	public BDCanvas()
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
			if(shapes.size() > 0) {
				isDirty = true;
			}
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
	
	public void setClean() {
		isDirty = false;
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
		
		shapes.clear();
		shapeListHasChangedSinceLastDraw = true;
		repaint();
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
					BDLine tempLine = new BDLine(parts);
					shapes.add(tempLine);
					break;
				case 2: // rectangle
					System.out.println("Creating a rectangle from file.");
					BDRect tempRect = new BDRect(parts);
					shapes.add(tempRect);
					break;
				case 3: // ellipse
					System.out.println("Creating an ellipse from file.");
					BDEllipse tempEllipse = new BDEllipse(parts);
					shapes.add(tempEllipse);
					break;
//				case 4: // polygon
//					System.out.println("Creating a polygon from file.");
//					FDEllipse tempPolygon = new FDPolygon(parts);
//					shapes.add(tempPolygon);
//					break;
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
		/*for(Shape s: shapes) {
			if(s.isSelected == true) {
				shapesClipboard.add(s);
				shapes.remove(s);
				s.isSelected = false;
			}
		}
		shapeListHasChangedSinceLastDraw = true;
		repaint();*/
	}
	public void handleCopyMenuItem()
	{
		System.out.println("User selected copy.");
		/*for(Shape s: shapes) {
			if(s.isSelected == true) {
				shapesClipboard.add(s);
				s.isSelected = false;
			}
		}
		shapeListHasChangedSinceLastDraw = true;
		repaint();*/
	}
	public void handlePasteMenuItem()
	{
		System.out.println("User selected paste.");
		/*for(Shape s: shapesClipboard) {
			shapes.add(s);
			s.isSelected = false;
		}
		shapeListHasChangedSinceLastDraw = true;
		repaint();*/
	}
	public void handleDuplicateMenuItem()
	{
		System.out.println("User selected duplicate.");
	}
	public void handleSelectAllMenuItem()
	{
		System.out.println("User selected select all.");
		for(Shape s: shapes) {
			s.isSelected = true;
		}
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
		
		// selection = tool 0
		if(currentTool == 0) {
			BDRect tempRect = new BDRect();
			tempRect.setFill(null);
			tempRect.setStroke(Color.blue);
			tempRect.setPoints(mouseStartX, mouseStartY, mouseStartX, mouseStartY);
			tempShape = tempRect;
		}
		else if (currentTool == Shape.LINE_TYPE)
		{
			BDLine tempLine = new BDLine();
			tempLine.setFill(fillColor);
			tempLine.setStroke(strokeColor);
			tempLine.setPoints(mouseStartX, mouseStartY, mouseStartX, mouseStartY);
			tempShape = tempLine;
		}
		else if (currentTool == Shape.RECT_TYPE)
		{
			BDRect tempRect = new BDRect();
			tempRect.setFill(fillColor);
			tempRect.setStroke(strokeColor);
			tempRect.setPoints(mouseStartX, mouseStartY, mouseStartX, mouseStartY);
			tempShape = tempRect;
		}
		else if (currentTool == Shape.OVAL_TYPE)
		{
			BDEllipse tempEllipse = new BDEllipse();
			tempEllipse.setFill(fillColor);
			tempEllipse.setStroke(strokeColor);
			tempEllipse.setPoints(mouseStartX, mouseStartY, mouseStartX, mouseStartY);
			tempShape = tempEllipse;
		}
	}
	public void mouseReleased(MouseEvent me)
	{
		if(tempShape != null) {
			if (currentTool == 0)
			{
				Rectangle selectionRect = new Rectangle();
				selectionRect.setBounds(mouseStartX, mouseStartY, me.getX()-mouseStartX, me.getY()-mouseStartY);
				if(selectionRect.getHeight() == 0 && selectionRect.getWidth() == 0) {
					for(Shape s: shapes) {
						s.isSelected = false;
					}
					System.out.println("All shapes have been deselected");
				} else {
					for(Shape s: shapes) {
						if(s.containedInRect(selectionRect)) {
							s.isSelected = true;
							System.out.println(s + " is selected.");
						}
					}
				}
				shapeListHasChangedSinceLastDraw = true;
				tempShape = null;
				repaint();
			}
			if (currentTool == Shape.LINE_TYPE || currentTool == Shape.RECT_TYPE || currentTool == Shape.OVAL_TYPE)
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
		if (currentTool == 0 || currentTool == Shape.LINE_TYPE || currentTool == Shape.RECT_TYPE || currentTool == Shape.OVAL_TYPE)
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
		if (currentTool == 0)
		{
			if (tempShape != null)
			((BDRect)tempShape).setPoints(mouseStartX,mouseStartY,me.getX(),me.getY());
			repaint();
		}
		else if (currentTool == Shape.LINE_TYPE)
		{
			if (tempShape != null)
			((BDLine)tempShape).setPoints(mouseStartX,mouseStartY,me.getX(),me.getY());
			repaint();
		}
		else if (currentTool == Shape.RECT_TYPE)
		{
			if (tempShape != null)
			((BDRect)tempShape).setPoints(mouseStartX,mouseStartY,me.getX(),me.getY());
			repaint();
		}
		else if (currentTool == Shape.OVAL_TYPE)
		{
			if (tempShape != null)
			((BDEllipse)tempShape).setPoints(mouseStartX,mouseStartY,me.getX(),me.getY());
			repaint();
		}
	}
	
}
