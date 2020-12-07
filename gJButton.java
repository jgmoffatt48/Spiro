import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.JButton;


public class gJButton extends JButton {

	Color myColor ;
	Color buttonDefaultColor ;
	public gJButton(String text) {
		super(text);
		setOpaque(true);
		setBorder(BorderFactory.createLineBorder(Color.lightGray,5));
		//buttonDefaultColor = getBackground();
	}
	public void setBackground(Color clr) {
		myColor = clr;
		setBorder(BorderFactory.createLineBorder(myColor, 4));
	}
	
	public Color getSelectedColor(){
		return myColor;
	}
/*	public Color  getBackground(){
		return myColor;
	}*/

	
}
