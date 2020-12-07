


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.beans.Transient;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class SpiroDrawPnl extends JPanel implements MouseListener,MouseMotionListener,MouseWheelListener{


	Graphics2D g2;
	private SpiroFun mowner;
	
	double mscale = 1;
	
	double mscaleMax = 2.00;
	double mscaleMin = 0.1;
	
/*	private BufferedImage pnlBuf;
	private BufferedImage mainCircleBuf;
	private BufferedImage rollerCircleBuf;
	private BufferedImage dotCircleBuf;*/
	
	
	BufferedImage mainBuff = null;
	Graphics2D g2Buff = null;
	Graphics2D gLocal = null;
	
	
	gCircle4 mainCir;
	gCircle4 rollerCir;
	gCircle4 mdot;
	
	// Some place to start
	double degrees = 1.00;
	private int paintRepeatCnt = 360;
	
	int curRepaintCnt = 1;
	Vector<Point> mdotCentersVec = new Vector<Point>();
	private double zoomlevel = 1;
	
	private double mHshift = 0.00;
	private double mVshift = 0.00;
	private int mySize;
	private int cnt;
	
	
	public SpiroDrawPnl(SpiroFun owner) {
		mowner = owner;
		jbinit();
		doSetup();
	}
	
	private void jbinit() {
		addMouseWheelListener(this);
		addMouseMotionListener(this);
		addMouseListener(this);
	}


	public Dimension getPreferredSize() {
/*		Dimension dim = new Dimension((int)(mscale*(mainCir.mcDiameter+mainCir.mcstrokeWidth)),(int)(mscale *(mainCir.mcDiameter+mainCir.mcstrokeWidth)));
		
		if(getWidth() > mySize){
			System.err.println(" getPreferredSize  "  + " getWidth() > mySize");
			
		}*/
		
		int perfWidth = (int) ((mySize*mscale) > getWidth() ? (mySize*mscale) : getWidth());
		int perfHeight = (int) ((mySize*mscale) > getHeight() ? (mySize*mscale) : getHeight());
		return new Dimension(perfWidth,perfHeight);

	}


	public void doSetup() {
		
		
		mowner.pageColor = mowner.mpageClrBtn.getSelectedColor();
		this.setBackground(mowner.pageColor);
		mainBuff = null;
/*		mainCircleBuf =  null;
		rollerCircleBuf = null;
		dotCircleBuf = null;*/
		
		
		mscale = getAsDouble(mowner.mZoomLevelTxt.getText(),1.0);
		if(mscale < mscaleMin){mscale = mscaleMin;}
		mowner.mZoomLevelTxt.setText("" + mscale);
		
		// number of repaints
		setRepeatCnt(getAsInt(mowner.repeatRateTxt.getText(), 5));
		setRollerRateDegrees(getAsInt(mowner.rollerRateTxt.getText(), 1));
		
		mHshift = getAsInt(mowner.mhShiftTxt.getText(), 0);
		mVshift = getAsInt(mowner.mvShiftTxt.getText(), 0);
		
		// CIRCLE 1 #########################################################
		mainCir = new gCircle4();
		mainCir.mcstrokeClr = mowner.mmainBorderClrBtn.getSelectedColor();
		mainCir.mcfillClr = mowner.mmainFillClrBtn.getSelectedColor();
		mainCir.setDiameter(getAsDouble(mowner.mainSizeTxt.getText(),100.00));
		mainCir.setStrokeWidth(getAsInt(mowner.mmainstrokeWidthTxt.getText(),20));
		mainCir.bpaintBorder = mowner.bPaintMainBorderCheckbox.isSelected();
		mainCir.bpaintFill = mowner.bPaintMainFillCheckbox.isSelected();

		
		// CIRCLE 2 #########################################################
		rollerCir = new gCircle4();
		
		rollerCir.setDiameter(getAsInt(mowner.rollerSizeTxt.getText(), 10));
		rollerCir.setStrokeWidth(getAsInt(mowner.mrollerStrokeWidthTxt.getText(), 2));
		rollerCir.mcstrokeClr = mowner.mrollerBorderClrBtn.getSelectedColor();
		rollerCir.mcfillClr = mowner.mrollerFillClrBtn.getSelectedColor();
		rollerCir.bpaintBorder = mowner.paintRollerBorderCheckbox.isSelected();
		rollerCir.bpaintFill = mowner.paintRollerFillCheckbox.isSelected();


		// CIRCLE 3 #########################################################
		mdot = new gCircle4();
		mdot.setDiameter( getAsInt(mowner.mdotSizeTxt.getText(), 2) );
		mdot.setStrokeWidth(getAsInt(mowner.mdotStrokeWidthTxt.getText(),2));
		mdot.mcstrokeClr = mowner.mdotBorderClrBtn.getSelectedColor();
		mdot.mcfillClr = mowner.mdotFillClrBtn.getSelectedColor();
		mdot.bpaintBorder = mowner.bPaintDotBorderCheckbox.isSelected();
		mdot.bpaintFill = mowner.bPaintDotFillCheckbox.isSelected();
		
		// How big am I ????
		// The bigger of mainCir or roller circle + dotwidit
		double main = mainCir.mcDiameter + ((double)mainCir.mcstrokeWidth/2.00);
		double roller = rollerCir.mcDiameter + ((double)rollerCir.mcstrokeWidth/2.00);
		double dot =  mdot.mcDiameter + ((double)mdot.mcstrokeWidth/2.00);
	
		doLayoutCircles();
		
		// the Dot is constrained by the bigger of these 2 circles

		
		// 

		// ???? Wrk in Progress;
		mySize = (int) ((main > roller ? main + dot + 0.5 : roller + main/2) + dot + 0.5);
		
		mySize = (int) ((main > roller ? main : roller) + 2*dot + 0.5);
		
		
	//	setSize(new Dimension(mySize,mySize));

	}




	private void doLayoutCircles(){
		//Top of Page
		mainCir.setCenterX((mainCir.mcDiameter/2.00 + mainCir.mcstrokeWidth/2.00));
		mainCir.setCenterY(mainCir.mcDiameter/2.00 + mainCir.mcstrokeWidth/2.00);
		// Top of main circle
		rollerCir.setCenterX(mainCir.mcDiameter/2.00  + mainCir.mcstrokeWidth/2.00 );
		rollerCir.setCenterY(mainCir.mcstrokeWidth+rollerCir.mcstrokeWidth/2.00 + rollerCir.mcDiameter/2.00);
		//mCirclesVec.add(rollerCir);
		
		int atX = rollerCir.getCenterPoint().x - mdot.mcstrokeWidth/2;
		int atY = (int) rollerCir.getLocationY();
		
		mdot.setCenterX((atX+mdot.mcDiameter/2));
		mdot.setCenterY((atY+mdot.mcDiameter/2));
	}

	


	
	public void paint(Graphics g){
		
		super.paint(g);
		gLocal = (Graphics2D) g;
		
		doLayoutCircles();

		if(mainBuff == null){
			mdotCentersVec.clear();
			mainBuff = new BufferedImage(getPreferredSize().width, getPreferredSize().height, BufferedImage.TYPE_4BYTE_ABGR);
			g2 = (Graphics2D) mainBuff.getGraphics();
			
			
			// Now go paint the Buffer
		}
		else{
	
			if(mscale != 1){
				AffineTransform at = new AffineTransform();
		        at.scale(mscale,mscale);
		        gLocal.transform(at);
		    }
			
			gLocal.drawImage(mainBuff,(int)mHshift,(int)mVshift, this);
			
			return;
		}
		
																																																																																																																																																																																																																																																																											
		g2.translate(mHshift, mVshift);

		double mainCirCircumference = (double)mainCir.mcDiameter*Math.PI;
		double rcircumferance = Math.PI * (double)rollerCir.mcDiameter;
		double degrees2 = mainCirCircumference/rcircumferance * degrees;	
		double radians1 = degrees*Math.PI/180.00;
		double radians2 = degrees2*Math.PI/180.00;
		
		Point circleCenterPt = mainCir.getCenterPoint();
		Point pageCenterPt = new Point(this.getWidth()/2,this.getHeight()/2);
		pageCenterPt = new Point(mySize/2,mySize/2);
		

		g2Buff = (Graphics2D) mainBuff.getGraphics();

		
		// Paint the Spiro at the center of the a to start
		g2.translate(pageCenterPt.getX() - circleCenterPt.getX(), pageCenterPt.getY() - circleCenterPt.getY());
		
		
		if(mowner.bPaintMainBorderCheckbox.isSelected() || mowner.bPaintMainFillCheckbox.isSelected()){
			mainCir.draw(g2);
		}

		if(mowner.paintRollerBorderCheckbox.isSelected()  || mowner.paintRollerFillCheckbox.isSelected()){
			rollerCir.draw(g2);
			
		}
		if(mowner.bPaintDotBorderCheckbox.isSelected() || mowner.bPaintDotFillCheckbox.isSelected()){
			mdot.draw(g2);
		}
		

	for (curRepaintCnt = 0; curRepaintCnt < paintRepeatCnt; curRepaintCnt++) {

				double cptX = rollerCir.getCenterX();
				double cptY = rollerCir.getCenterY();
				
				rotatePoint2(mainCir,rollerCir,radians1);
				
				double dx = cptX - rollerCir.getCenterX();
				double dy = cptY - rollerCir.getCenterY();
				

				if(mowner.paintRollerBorderCheckbox.isSelected() || mowner.paintRollerFillCheckbox.isSelected() ){
					rollerCir.draw(g2);
				}
					

				// Now put the dot back on the roller circle
					mdot.setCenterX(mdot.getCenterX() - dx);
					mdot.setCenterY(mdot.getCenterY() - dy);
					
				// Now rotate the dot
				// it's already been rotated by radians1
				rotatePoint2(rollerCir,mdot,-(radians2-radians1));
				
				
				if(mowner.bPaintDotBorderCheckbox.isSelected()  || mowner.bPaintDotFillCheckbox.isSelected()){
					mdot.draw(g2);
				}
				
				
				if(mowner.bPaintDotsOnTop){
					mdotCentersVec.add(mdot.getCenterPoint());	
				}
				
				// TODO -- What center to show
				//	doDrawCenterCross(g2);				
		}
		g2.translate(-(pageCenterPt.getX() - circleCenterPt.getX()), -(pageCenterPt.getY() - circleCenterPt.getY()));
		gLocal.drawImage(mainBuff,(int)mHshift,(int)mVshift, this);
		
		if(mowner.bPaintDotsOnTop){
			paintDotsOnTop(mdotCentersVec,g2);
		}
	}
	

	
	private void paintDotsOnTop(Vector<Point> mdotCentersVec, Graphics2D g2) {
		Vector<String> pointsVec = new Vector<String>();
		String ptsStr = "";
		Point atPt;
		for (int i = 0; i < mdotCentersVec.size(); i++) {
			atPt = mdotCentersVec.elementAt(i);
			ptsStr = ptsStr + atPt.toString();
			mdot.setCenterX(atPt.getX());
			mdot.setCenterY(atPt.getY());
			mdot.draw(g2);
		}
		//System.err.println(ptsStr);
	}




	private void doDrawCenterCross(Graphics2D g2) {
		g2.setColor(Color.black);
		g2.setStroke(new BasicStroke(2));
		g2.drawLine(getWidth()/2, 0, getWidth()/2, getHeight());
		g2.drawLine(0, getHeight()/2 , getWidth(), getHeight()/2 );
		
	}

	private void rotatePoint2(gCircle4 circleMe, gCircle4 dot, double angle) {
		
		double rX = Math.cos(angle) * (dot.getCenterX() - circleMe.getCenterX()) - Math.sin(angle) * (dot.getCenterY() - circleMe.getCenterY()) + circleMe.getCenterX();
        double rY = Math.sin(angle) * (dot.getCenterX() - circleMe.getCenterX()) + Math.cos(angle) * (dot.getCenterY() - circleMe.getCenterY()) + circleMe.getCenterY();
       dot.setCenterX(rX);
       dot.setCenterY(rY);
	}

	public void setRollerRateDegrees(double numDegrees) { 
		degrees=numDegrees;
		repaint();
	}
	
	public void setRepeatCnt(int cnt) { paintRepeatCnt = cnt; repaint();}
	public int getRepeatCnt() { return paintRepeatCnt; }

	public double getAsDouble(String val, double retval) {
		try {
			retval = Double.parseDouble(val);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		
		return retval;
	}

	public int getAsInt(String val,int retval){
		
		try {
			retval = Integer.parseInt(val);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
		}
		
		return retval;
	}




//MouseListener
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	public void mousePressed(MouseEvent arg0) {
		System.err.println();
		
	}
	public void mouseReleased(MouseEvent arg0) {
		
		if(mdragPt != null){
			mHshift = mHshift + ( arg0.getPoint().getX() - mdragPt.getX());
			mVshift = mVshift + (arg0.getPoint().getY() - mdragPt.getY() );
			mdragPt = null;
			
			
			mowner.mhShiftTxt.setText(mHshift + "");
			mowner.mvShiftTxt.setText(mVshift + "");
		}
	}




	//MouseMotionListenet
	Point mdragPt ;
	public void mouseDragged(MouseEvent arg0) {
		//System.err.println("mouseDragged  " + arg0.getPoint());
		if(mdragPt == null){
			mdragPt = arg0.getPoint();
		}
		else{
			mHshift = mHshift + ( arg0.getPoint().getX() - mdragPt.getX());
			mVshift = mVshift + (arg0.getPoint().getY() - mdragPt.getY() );
			
			
		//	System.err.println("mHshift = " + mHshift  +" and  " + "mVshift = " + mVshift);
			mowner.mhShiftTxt.setText(mHshift + "");
			mowner.mvShiftTxt.setText(mVshift + "");
			mdragPt = arg0.getPoint();
			repaint();
		}
		 
		
	}
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		
	//	System.err.println("mouseWheelMoved " + arg0.getWheelRotation());
		
		if(arg0.isControlDown()){

			mscale = mscale + arg0.getWheelRotation()/10.00;
			
			if(mscale > 2)
				mscale = 2.00;
			
			if(mscale < 0.2)
				mscale = 0.2;
				
			System.err.println(mscale);
			mowner.mZoomLevelTxt.setText(mscale + "");
			mowner.doLayout();
			mowner.mScroller.doLayout();
			repaint();
			
		}
	}

	public boolean setHShift(double val) {
		
		if(mHshift == val){
			return false;
		}
		mHshift = val;
		mainBuff = null;
		return true;
	}
	public boolean setVShift(double val) {

		if(mVshift == val){
			return false;
		}
		mVshift = val;
		mainBuff = null;
		return true;
	}
	
}

	
	
	