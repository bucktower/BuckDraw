package net.bucktower.buckdraw;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class BDToolbar extends JPanel implements ActionListener
{

	private BDCanvas theCanvas;
	private JButton[] buttonList;
	private JButton   fillColorButton, strokeColorButton;
	private Color     fillColor,       strokeColor;
	private JCheckBox fillCheckbox,    strokeCheckbox;
	private final int NUM_BUTTONS = 5;
	private String[] iconFiles = {"Arrow.png","Line.png","Rect.png","Circle.png","Triangle.png"};
	private JColorChooser myColorChooser;
	
	public BDToolbar(BDCanvas cv)
	{
		super();
		theCanvas = cv;
		
		setupToolButtons();
		setupColorButtons();
	}
	/**
	 * sets up the layout and links the actionListeners for the tool buttons
	 */
	public void setupToolButtons()
	{
		buttonList = new JButton[NUM_BUTTONS];
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		setMaximumSize(new Dimension(50,10000));
		for (int i=0; i<NUM_BUTTONS; i++)
		{
			buttonList[i] = new JButton(new ImageIcon(iconFiles[i]));
			add(buttonList[i]);
			buttonList[i].addActionListener(this);
		}
		
		
	}
	
	/**
	 * sets up the layout and links he actionListeners for the two color buttons.
	 */
	public void setupColorButtons()
	{
		fillCheckbox = new JCheckBox("Fill");
		fillCheckbox.setSelected(true);
		fillCheckbox.setHorizontalTextPosition(SwingConstants.LEFT);
		fillCheckbox.addActionListener(this);
		add(fillCheckbox);
		
		fillColorButton = new JButton();
		fillColorButton.setDisabledIcon(new ImageIcon("nullIcon.png"));
		fillColorButton.addActionListener(this);
		fillColor = Color.LIGHT_GRAY;
		setButtonColor(fillColorButton,fillColor);
		add(fillColorButton);
		
		// Repeat, for stroke!
		strokeCheckbox = new JCheckBox("Stroke");
		strokeCheckbox.setSelected(true);
		strokeCheckbox.setHorizontalTextPosition(SwingConstants.LEFT);
		strokeCheckbox.addActionListener(this);
		add(strokeCheckbox);
		
		strokeColorButton = new JButton();
		strokeColorButton.setDisabledIcon(new ImageIcon("nullIcon.png"));
		strokeColorButton.addActionListener(this);
		strokeColor = Color.DARK_GRAY;
		setButtonColor(strokeColorButton,strokeColor);
		add(strokeColorButton);
		
		// let the canvas know about these colors
		theCanvas.setFillColor(Color.LIGHT_GRAY);
		theCanvas.setStrokeColor(Color.DARK_GRAY);
		
		// initialize the color chooser that will appear when one of these buttons is pressed.
		myColorChooser = new JColorChooser();
	}
	/**
	 * one of the buttons has been pressed - we need to deal with what to do about this.
	 */
	public void actionPerformed(ActionEvent ae)
	{
		for (int i = 0; i<NUM_BUTTONS; i++)
		{
			if (ae.getSource()==buttonList[i])
				theCanvas.setTool(i);
		}
		if (ae.getSource() == fillColorButton)
			handleFillColorButtonPress();
		
		if (ae.getSource() == strokeColorButton)
			handleStrokeColorButtonPress();
		
		if (ae.getSource() == fillCheckbox)
			handleFillCheckboxChange();
		
		if (ae.getSource() == strokeCheckbox)
			handleStrokeCheckboxChange();
	}
	
	/**
	 * The user has just pressed the fill color button - responds by asking for a color,
	 * updating the color button, and passing along the color to the canvas.
	 */
	public void handleFillColorButtonPress()
	{
		myColorChooser.setColor(fillColor);
		int result = JOptionPane.showConfirmDialog(this,myColorChooser);
		if (JOptionPane.YES_OPTION==result) // if the user clicked the "ok" button (not cancel)
		{
			fillColor= myColorChooser.getColor();
			setButtonColor(fillColorButton, fillColor);
			if (fillCheckbox.isSelected())
				theCanvas.setFillColor(fillColor);
			repaint();
		}
	}
	/**
	 * The user has just pressed the stroke color button - responds by asking for a color,
	 * updating the color button, and passing along the color to the canvas.
	 */
	public void handleStrokeColorButtonPress()
	{
		myColorChooser.setColor(strokeColor);
		int result = JOptionPane.showConfirmDialog(this,myColorChooser);
		if (JOptionPane.YES_OPTION==result) // if the user clicked the "ok" button (not cancel)
		{
			strokeColor= myColorChooser.getColor();
			setButtonColor(strokeColorButton, strokeColor);
			if (strokeCheckbox.isSelected())
				theCanvas.setStrokeColor(strokeColor);
			repaint();
		}
	}
	/**
	 * The user has just changed the "fill" checkbox - responds by notifying the canvas of the
	 * color (or null, if checkbox is unselected) and enables/disables the fill color button.
	 */
	public void handleFillCheckboxChange()
	{
		if (fillCheckbox.isSelected())
		{
			theCanvas.setFillColor(fillColor);
			fillColorButton.setEnabled(true);
		}
		else
		{
			theCanvas.setFillColor(null);
			fillColorButton.setEnabled(false);
		}
	}
	/**
	 * The user has just changed the "stroke" checkbox - responds by notifying the canvas of the
	 * color (or null, if checkbox is unselected) and enables/disables the stroke color button.
	 */
	public void handleStrokeCheckboxChange()
	{
		if (strokeCheckbox.isSelected())
		{
			theCanvas.setStrokeColor(strokeColor);
			strokeColorButton.setEnabled(true);
		}
		else
		{	
			theCanvas.setStrokeColor(null);
			strokeColorButton.setEnabled(false);
		}
	}
	
	/**
	 * changes the apparent color of the button to the given color
	 * @param button - which button to change
	 * @param c - what color to change to
	 */
	public void setButtonColor(JButton button, Color c)
	{
		int iconWidth = 50;
		int iconHeight= 50;
		
		BufferedImage bi = new BufferedImage(iconWidth,
                iconHeight,
                BufferedImage.TYPE_INT_ARGB);
		Graphics g = bi.getGraphics();
		g.setColor(c);
		g.fillRect(3, 3, iconWidth-6,iconHeight-6);
		button.setIcon(new ImageIcon(bi));

	}
}
