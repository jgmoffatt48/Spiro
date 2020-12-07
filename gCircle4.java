
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

	
	public class gCircle4{

		int mcstrokeWidth = 1;
		BasicStroke mcstroke = new BasicStroke(mcstrokeWidth);
		
		Color mcstrokeClr = Color.red;
		Color mcfillClr = Color.blue;
		
		double mcDiameter = 50.00;
		double mcenterX = 25.00;
		double mcenterY = 25.00;
		public boolean bpaintBorder = true;
		public boolean bpaintFill = false;
		
		public  gCircle4(){
			
		}
		public Point getCenterPoint() {
			return new Point((int)(getCenterX()+0.5),(int)(getCenterY()+0.5));
		}

		public void setStrokeWidth(int sw) {
			mcstrokeWidth = sw;
			mcstroke = new BasicStroke((int)mcstrokeWidth);
		}
		
		public void setDiameter(double dia) { mcDiameter = dia; }

		public void setCenterX(double cX){ mcenterX = cX; }
		public void setCenterY(double cY){mcenterY = cY; }
		
		public double getCenterX(){ return mcenterX; }
		public double getCenterY(){ return mcenterY; }
		
		public double getLocationX(){
			return getCenterPoint().getX() - mcDiameter/2.00;
		}
		public double getLocationY(){
			return getCenterPoint().getY() - mcDiameter/2.00;
		}
		
		public void draw(Graphics2D g2){
	
			g2.setStroke(mcstroke);
			
					
			if(bpaintFill && mcfillClr != null){
				g2.setColor(mcfillClr);
				g2.fillOval((int)(getLocationX()+0.5) ,(int)(getLocationY()+0.5),(int)mcDiameter,(int)mcDiameter);
			}	
			
			if(bpaintBorder){
				g2.setColor(mcstrokeClr);
				g2.drawOval((int)(getLocationX()+0.5),(int)(getLocationY()+0.5),(int)mcDiameter,(int)mcDiameter);
			}

		}
		public void draw(Graphics2D g2BufCir, boolean isBuffer) {
			if(isBuffer){
				g2BufCir.setStroke(mcstroke);
				
				if(bpaintFill && mcfillClr != null){
					g2BufCir.setColor(mcfillClr);
					g2BufCir.fillOval((int)(0) ,(int)(0),(int)mcDiameter,(int)mcDiameter);
				}	
				
				if(bpaintBorder){
					g2BufCir.setColor(mcstrokeClr);
					g2BufCir.drawOval(0,0,(int)mcDiameter,(int)mcDiameter);
				}
				
			}
		}
		
	}
		
	