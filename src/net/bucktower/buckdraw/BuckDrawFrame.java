package net.bucktower.buckdraw;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class BuckDrawFrame extends JFrame implements ActionListener
{
	private BDToolbar toolPanel;
	private BDCanvas canvasPanel;
	private JMenuItem newMI, openMI, saveMI, saveAsMI;
	private JMenuItem cutMI, copyMI, pasteMI, duplicateMI, selectAllMI, groupMI;
	private File theFile;
	
	public BuckDrawFrame()
	{
		super("BuckDraw");
		setSize(1200,900);
		setupMenus();
		getContentPane().setLayout(new BorderLayout());
		canvasPanel = new BDCanvas();
		toolPanel = new BDToolbar(canvasPanel);
		getContentPane().add(toolPanel, BorderLayout.WEST);
		getContentPane().add(canvasPanel, BorderLayout.CENTER);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	/**
	 * sets up the menu at the top of the window and links the menu items (MIs) to this class's
	 * actionPerformed() method.
	 */
	public void setupMenus()
	{
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		String lcOSName = System.getProperty("os.name").toLowerCase();
		if(lcOSName.startsWith("mac os x")) {
			System.setProperty("apple.laf.useScreenMenuBar", "true");
		}
		
		JMenuBar myMenubar = new JMenuBar();
		
		myMenubar.add(createFileMenu());
		myMenubar.add(createEditMenu());
		this.setJMenuBar(myMenubar);	
	}
	/**
	 * set up the File menu that will be added to the menubar.
	 * @return the File Menu
	 */
	public JMenu createFileMenu()
	{
		JMenu fileMenu = new JMenu("File");
		
		newMI = new JMenuItem("New");
		openMI = new JMenuItem("Open...");
		saveMI = new JMenuItem("Save");
		saveAsMI = new JMenuItem("Save As...");

		newMI.addActionListener(this);
		openMI.addActionListener(this);
		saveMI.addActionListener(this);
		saveAsMI.addActionListener(this);
		
		fileMenu.add(newMI);
		fileMenu.add(openMI);
		fileMenu.addSeparator();
		fileMenu.add(saveMI);
		fileMenu.add(saveAsMI);
		return fileMenu;
	}
	/**
	 * set up the Edit menu that will be added to the menu bar.
	 * @return the Edit menu.
	 */
	public JMenu createEditMenu()
	{
		JMenu editMenu = new JMenu("Edit");
		
		cutMI = new JMenuItem("Cut");
		copyMI = new JMenuItem("Copy");
		pasteMI = new JMenuItem("Paste");
		duplicateMI = new JMenuItem("Duplicate");
		selectAllMI = new JMenuItem("Select All");
		groupMI = new JMenuItem("Group");
		
		cutMI.addActionListener(this);
		copyMI.addActionListener(this);
		pasteMI.addActionListener(this);
		duplicateMI.addActionListener(this);
		selectAllMI.addActionListener(this);
		groupMI.addActionListener(this);
		
		editMenu.add(cutMI);
		editMenu.add(copyMI);
		editMenu.add(pasteMI);
		editMenu.add(duplicateMI);
		editMenu.addSeparator();
		editMenu.add(selectAllMI);
		editMenu.add(groupMI);
		return editMenu;
	}
	
	/**
	 * in response to the user's selection of a menu item, routes to the correct method, based
	 * on which item was selected.
	 */
	public void actionPerformed(ActionEvent ae)
	{
		if (ae.getSource() == newMI) {
			if(canvasPanel.isDirty) {
				handleSaveMI();
			}
			handleNewMI();
		}
		if (ae.getSource() == openMI) {
			if(canvasPanel.isDirty) {
				handleSaveMI();
			}
			handleOpenMI();
		}
		if (ae.getSource() == saveMI)
			handleSaveMI();
		if (ae.getSource() == saveAsMI)
			handleSaveAsMI();
		if (ae.getSource() == cutMI)
			canvasPanel.handleCutMenuItem();
		if (ae.getSource() == copyMI)
			canvasPanel.handleCopyMenuItem();
		if (ae.getSource() == pasteMI)
			canvasPanel.handlePasteMenuItem();
		if (ae.getSource() == duplicateMI)
			canvasPanel.handleDuplicateMenuItem();
		if (ae.getSource() == selectAllMI)
			canvasPanel.handleSelectAllMenuItem();
		if (ae.getSource() == groupMI)
			canvasPanel.handleGroupMenuItem();
		
	}
	/**
	 * in response to the user selecting "new," tells the canvas to create a new doc.
	 */
	public void handleNewMI()
	{
		theFile = null; // clears the current file location - so saving this new one
		                // won't accidentally overwrite a previous one.
		canvasPanel.handleNewMenuItem();
	}
	
	/**
	 * in response to the user's selection of the open MI, creates an open file dialog and
	 * routes the selected file to the canvas' openFile() method.
	 */
	public void handleOpenMI()
	{
		JFileChooser chooser = new JFileChooser();
		chooser.setSelectedFile(theFile); // starts the dialog off where the last file was found.
		
		int result = chooser.showOpenDialog(this); // displays the dialog and waits until the
												   // user picks "ok" or "cancel." The result
		                                           // variable gets a code for which one was picked.
		if (JFileChooser.APPROVE_OPTION == result)
		{
			theFile = chooser.getSelectedFile(); // gets the file the user picked.
			canvasPanel.openFile(theFile);
		} // otherwise, the user clicked "cancel," so do nothing.
	}
	
	/**
	 * in response to the user's selection of the save MI, tells the canvas to saveFile() with
	 * the current file if there is one; otherwise, calls "saveAs."
	 */
	public void handleSaveMI()
	{
		if (null == theFile)
		{
			handleSaveAsMI();
		}
		else
		{
			canvasPanel.saveFile(theFile);
		}
	}
	
	/**
	 * in response to the user selecting saveAs (or save with no current file), creates
	 * a "save file" dialog and sends the file to the canvas to create/update it.
	 */
	public void handleSaveAsMI()
	{
		JFileChooser chooser = new JFileChooser();
		chooser.setSelectedFile(theFile);
		int result = chooser.showSaveDialog(this);
		if (JFileChooser.APPROVE_OPTION == result)
		{
			theFile = chooser.getSelectedFile();
			canvasPanel.saveFile(theFile);
			canvasPanel.setClean();
		}
		// otherwise, the user picked "cancel," so do nothing.
	}
}
