package net.bucktower.buckdraw;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class BuckDraw {

	public static void main(String[] args) {
		BuckDrawFrame app = new BuckDrawFrame();
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String lcOSName = System.getProperty("os.name").toLowerCase();
		if(lcOSName.startsWith("mac os x")) {
			
		}
	}

}
