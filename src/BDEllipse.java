import java.awt.Graphics;
import java.awt.Rectangle;

public class BDEllipse extends Shape {

	private int x1,x2,y1,y2;
	
	private int borderPadding = 1;
	
	public BDEllipse(String[] data, int startIndex) 
	{
		super(data,startIndex); // this grabs the colors, which takes values 0-5.
		try
		{
			x1 = Integer.parseInt(data[startIndex+6]);
			y1 = Integer.parseInt(data[startIndex+7]);
			x2 = Integer.parseInt(data[startIndex+8]);
			y2 = Integer.parseInt(data[startIndex+9]);
		}
		catch (NumberFormatException nfe)
		{
			System.out.println("Error parsing an integer in data.");
			nfe.printStackTrace();
		}
	}

	public BDEllipse(String[] data) 
	{
		this(data,1);
	}

	public BDEllipse() 
	{
		super();
	}
	
	@Override
	public int numParamsNeeded() {
		return 13; // 3 for fill, 3 for stroke, 3 for fill, 4 for x1, y1, x2, y2.
	}

	@Override
	public void drawSelf(Graphics g) {
		int minX = Math.min(x1, x2);
		int minY = Math.min(y1, y2);
		int maxX = Math.max(x1, x2);
		int maxY = Math.max(y1, y2);
		if(getStroke() != null) {
			g.setColor(getStroke());
			g.drawOval(minX, minY, maxX-minX, maxY-minY);
		}
		if(getFill() != null) {
			g.setColor(getFill());
			g.fillOval(minX+borderPadding, minY+borderPadding, (maxX-minX)-borderPadding*2, (maxY-minY)-borderPadding*2);
		}
		setPoints(minX,minY,maxX,maxY);
	}
	
	@Override
	public String toString() 
	{
		String result = super.toString();// start with the type & colors.
		result+=x1+"\t";
		result+=y1+"\t";
		result+=x2+"\t";
		result+=y2;
		
		return result;
	}

	@Override
	public boolean containsPoint(double x, double y) {
		int margin = 5; // lets see whether it is within 5 pixels - odds are bad of hitting the line exactly.
		
		// first, let's check if we're in the neighborhood....
		if (x<Math.min(x1, x2)-5 || x>Math.max(x1,x2)+5 || y< Math.min(y1, y2)-5 || y>Math.max(y1, y2)+5 ) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean containedInRect(Rectangle r) {
		int leftEdge = (int)Math.min(r.getX(), r.getX()+r.getWidth());
		int rightEdge = (int)Math.max(r.getX(), r.getX()+r.getWidth());
		int topEdge = (int)Math.min(r.getY(), r.getY()+r.getHeight());
		int bottomEdge = (int)Math.max(r.getY(), r.getY()+r.getHeight());
		
		return (Math.min(x1, x2)>leftEdge && Math.max(x1, x2)<rightEdge 
			 && Math.min(y1, y2)>topEdge && Math.max(y1, y2)<bottomEdge);
	}

	@Override
	public int getType() {
		return Shape.OVAL_TYPE;
	}

	/**
	 * @return the x1
	 */
	public int getX1() 
	{
		return x1;
	}

	/**
	 * @return the x2
	 */
	public int getX2() 
	{
		return x2;
	}

	/**
	 * @return the y1
	 */
	public int getY1() 
	{
		return y1;
	}
	
	/**
	 * @return the y2
	 */
	public int getY2() 
	{
		return y2;
	}

	public void setPoints(int xa, int ya, int xb, int yb)
	{
		x1 = xa;
		y1 = ya;
		x2 = xb;
		y2 = yb;
	}
	
	
}
