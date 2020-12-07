

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileSystemView;

public class SpiroFun extends JFrame {
	
	
	FileUtils fu = new FileUtils();
	
	private SpiroDrawPnl spRight;
	
    Color pageColor = Color.white;
    gJButton mpageClrBtn = new gJButton("Page Color");
	JTextField repeatRateTxt = new JTextField("360");	
	JTextField mhShiftTxt;
	JTextField mvShiftTxt;
    
	
	
	JTextField mainSizeTxt = new JTextField("800");
	private JSlider mainSizeSlider = new JSlider(10, 1000);
	JCheckBox bPaintMainBorderCheckbox;
	JCheckBox bPaintMainFillCheckbox;
	public JTextField mmainstrokeWidthTxt;
	private boolean bPaintMain;
	gJButton mmainBorderClrBtn = new gJButton("Border Color");
	gJButton mmainFillClrBtn = new gJButton("Fill Color");
	
	
	
	
	JTextField rollerSizeTxt = new JTextField(100); 
	private JSlider rollerSizeSlider = new JSlider(10, 1000);
	private JSlider rollerRateSlider = new JSlider(1, 720);	
	JTextField rollerRateTxt = new JTextField("5");	
	JCheckBox paintRollerBorderCheckbox;
	JCheckBox paintRollerFillCheckbox;
	gJButton mrollerBorderClrBtn = new gJButton("Border Color");
	gJButton mrollerFillClrBtn = new gJButton("Fill Color");
	JTextField mrollerStrokeWidthTxt;
	private boolean bPaintRoller;
		
		
		
	JTextField mdotSizeTxt =  new JTextField("2");
	private JSlider dotSizeSlider = new JSlider(1, 1000);
	JCheckBox bPaintDotBorderCheckbox;
	JCheckBox bPaintDotFillCheckbox;
	gJButton mdotBorderClrBtn = new gJButton("Border Color");
	gJButton mdotFillClrBtn = new gJButton("Fill Color");
	JTextField mdotStrokeWidthTxt;	
	private boolean bPaintDot;

	gJButton mOpenBtn = new gJButton("Open");
	gJButton mSaveBtn = new gJButton("Save");
	protected Vector<String> openSpiros;
	int viewerOpenCount = 0;
	JScrollPane mScroller;
	private Font mboldFont;

	
	//  TODO  -- Not perfect yet
	public boolean bPaintDotsOnTop = true;

	JTextField mZoomLevelTxt;

	
	
	public SpiroFun(String title) {
		super(title);
		binit();
	}

	private void binit() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		 //setLocationRelativeTo(null);
		
		Font curFont = rollerRateTxt.getFont();
		
		mboldFont = new Font(curFont.getFontName(),Font.BOLD,curFont.getSize() + 2);
		JSplitPane mainSp = new JSplitPane();
		this.getContentPane().add(mainSp);

		gStackerPnl sidePnl = new gStackerPnl(new BorderLayout());
	//	sidePnl.setBackground(Color.red);
		
		mainSp.add(sidePnl,JSplitPane.LEFT);
		
		sidePnl.addItem(doZoomPanel());
		sidePnl.addItem(doPagePropertiesPanel());
		sidePnl.addItem(doMainCirclePropertiesPanel());
		sidePnl.addItem(doRollerCirclePropertiesPanel());
		sidePnl.addItem(doDotCirclePropertiesPanel());
		sidePnl.addItem(doOpenSaveBtnPanel());
		
		
		spRight = new SpiroDrawPnl(this);	
		 mScroller = new JScrollPane(spRight);
		 
		 mScroller.setViewportView(spRight);
		mainSp.add(mScroller,JSplitPane.RIGHT);
		
		
		pack();
		setVisible(true);
	}

	
	private JPanel doZoomPanel() {
		gStackerPnl vp = new gStackerPnl(new BorderLayout());
		vp.setBorder(BorderFactory.createTitledBorder("Zoom Level"));
		
		mZoomLevelTxt = new JTextField("1.0");
		
		String fname = mZoomLevelTxt.getFont().getFontName();
		mZoomLevelTxt.setFont(new Font(fname,Font.BOLD,14));
		vp.addItem(mZoomLevelTxt);
		
		mZoomLevelTxt.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent arg0) {}
			public void keyReleased(KeyEvent arg0) {}
			public void keyPressed(KeyEvent arg0) {
				 
				if(arg0.getKeyCode() == 10){// Enter
					int val = Integer.parseInt(repeatRateTxt.getText());
						spRight.mscale = spRight.getAsDouble(mZoomLevelTxt.getText(),1.0);
						spRight.doSetup();
						SpiroFun.this.repaint();
				}
				
			}
		});
		
		
		return vp;
	}

	private JPanel doPagePropertiesPanel() {
		gStackerPnl vp = new gStackerPnl(new BorderLayout());
		//vp.setBackground(Color.red);
		vp.setBorder(BorderFactory.createTitledBorder("Page Properties"));
		
		mpageClrBtn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
			spRight.setBackground(pageColor = SpiroFun.this.doColorChooser(pageColor));
			mpageClrBtn.setBackground(pageColor);
			
				SpiroFun.this.repaint();
			}
			
		});
		
		JPanel pan = new JPanel(new BorderLayout(40,10));
		pan.setLayout(new GridLayout(2, 2,10,20));
		pan.add(mpageClrBtn);
		
		repeatRateTxt.setBorder(BorderFactory.createTitledBorder("RePaints No"));
		repeatRateTxt.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent arg0) {}
			public void keyReleased(KeyEvent arg0) {}
			public void keyPressed(KeyEvent arg0) {
				 
				if(arg0.getKeyCode() == 10){// Enter
					int val = Integer.parseInt(repeatRateTxt.getText());
					
					if( spRight.getRepeatCnt() != val){
					
						spRight.setRepeatCnt((int)val);
						spRight.doSetup();
						spRight.repaint();
					}
				}
				
			}
		});
		
		pan.add(repeatRateTxt);
		
		mhShiftTxt = new JTextField("0");
		mhShiftTxt.setBorder(BorderFactory.createTitledBorder("H offset"));
		mhShiftTxt.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent arg0) {

			}
			
			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode() == 10){// Enter
					int val = Integer.parseInt(mhShiftTxt.getText());
					
					if(spRight.setHShift((double)val) ){
						spRight.doSetup();
						spRight.repaint();
					}
				}
				
			}
		});
		
		pan.add(mhShiftTxt);
		
		mvShiftTxt = new JTextField("0");
		mvShiftTxt.setBorder(BorderFactory.createTitledBorder("V offset"));
		
		mvShiftTxt.addKeyListener(new KeyListener() {
			
			
			@Override
			public void keyTyped(KeyEvent arg0) {

			}
			
			
			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode() == 10){// Enter
					int val = Integer.parseInt(mvShiftTxt.getText());
					if(spRight.setVShift((double)val) ){
						spRight.setVShift((double)val);
						spRight.doSetup();
						spRight.repaint();
					}
				}
				
			}
		});
		
		pan.add(mvShiftTxt);
		
		
		vp.addItem(pan);
		vp.addItem(doRollRatePanel());
		return vp;
	}

	private JPanel doOpenSaveBtnPanel() {
		gStackerPnl vp = new gStackerPnl(new BorderLayout());
		vp.setBackground(Color.red);
		
		
		vp.setBorder(BorderFactory.createEtchedBorder());
		JPanel pan = new JPanel(new BorderLayout(40,10));
		pan.setLayout(new GridLayout(1, 2,10,20));
		
		
		pan.add(mOpenBtn);
		
		mOpenBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Open the file
			 if(openSpiros == null){
						//File inFile = new File (FileSystemView.getFileSystemView().getDefaultDirectory().getPath() +  "\\BigSpiro.txt");
				 File inFile = new File (FileSystemView.getFileSystemView().getDefaultDirectory().getPath() +  File.separator  +  "testFile.txt");
				 //File inFile = new File (FileSystemView.getFileSystemView().getDefaultDirectory().getPath() +  "\\BBEst2.txt");
				 //File inFile = new File (FileSystemView.getFileSystemView().getDefaultDirectory().getPath() +  File.separator  + "TryMe.txt");
				// File inFile = new File (FileSystemView.getFileSystemView().getDefaultDirectory().getPath() +  File.separator  +  "shortList.txt");
				 if(!inFile.exists()){
					 try {
						inFile.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					 openSpiros = new Vector<String>();
				 }
				 else{
					 openSpiros = fu.getLineByLineVector(inFile);
				 }
				 
				 
			}
			 else{
				 if(viewerOpenCount < openSpiros.size()){
					 setSpiroLine(openSpiros.elementAt(viewerOpenCount++));
				 }
				 
			 }

			}
		});
		pan.add(mSaveBtn);
		
		vp.addItem(pan);
		
		mSaveBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {

				
				String saveStr = "pageColor="+ doColorToString(mpageClrBtn.getSelectedColor()) + "::";
				saveStr = saveStr + "repaintCnt="+spRight.getRepeatCnt() + "::";
				saveStr = saveStr + "rollRate="+rollerRateTxt.getText() + "::";
				saveStr = saveStr + "hShift="+mhShiftTxt.getText() + "::";
				saveStr = saveStr + "vShift="+mvShiftTxt.getText() + "::";
				
				
				
				String c1Dia = "c1Dia=" + mainSizeTxt.getText()+ "::";
				String c1PaintBorder = "c1PaintBorder=" +  bPaintMainBorderCheckbox.isSelected()+ "::";
				String c1BorderColor = "c1BorderColor=" +  doColorToString(mmainBorderClrBtn.getSelectedColor()) + "::";
				String c1BorderWidth = "c1BorderWidth=" +  mmainstrokeWidthTxt.getText()+ "::";
				String c1PaintFill = "c1PaintFill= + "   + bPaintMainFillCheckbox.isSelected()+ "::";
				String c1FillColor = "c1FillColor=" +  doColorToString(mmainFillClrBtn.getSelectedColor()) + "::";
				
				String c2Dia = "c2Dia=" +  rollerSizeTxt.getText()+ "::";
				String c2PaintBorder = "c2PaintBorder=" +  paintRollerBorderCheckbox.isSelected() + ""+ "::";
				String c2BorderColor = "c2BorderColor=" +  doColorToString(mrollerBorderClrBtn.getSelectedColor())+ "::";
				String c2BorderWidth = "c2BorderWidth=" +  mrollerStrokeWidthTxt.getText()+ "::";
				String c2PaintFill = "c2PaintFill= + "   + bPaintMainFillCheckbox.isSelected()+ "::";
				String c2FillColor = "c2FillColor=" +  doColorToString(mmainFillClrBtn.getSelectedColor()) + "::";
				
				
				String c3Dia = "c3Dia=" +  mdotSizeTxt.getText()+ "::";
				String c3PaintBorder = "c3PaintBorder=" +  bPaintDotBorderCheckbox.isSelected() + ""+ "::";
				String c3BorderColor = "c3BorderColor=" +  doColorToString(mdotBorderClrBtn.getSelectedColor())+ "::";
				String c3BorderWidth = "c3BorderWidth=" +  mdotStrokeWidthTxt.getText() + "::";
				String c3PaintFill = "c3PaintFill= + "   + bPaintMainFillCheckbox.isSelected()+ "::";
				String c3FillColor = "c3FillColor=" +  doColorToString(mmainFillClrBtn.getSelectedColor()) + "::";
				
				
				
				saveStr = saveStr +c1Dia+c1PaintBorder+c1BorderColor+c1BorderWidth+c1PaintFill+c1FillColor;
				saveStr = saveStr +c2Dia+c2PaintBorder+c2BorderColor+c2BorderWidth+c2PaintFill+c2FillColor;
				saveStr = saveStr +c3Dia+c3PaintBorder+c3BorderColor+c3BorderWidth+c3PaintFill+c3FillColor;
				
				System.err.println(saveStr);
				
				
				FileUtils fu = new FileUtils();
				File in = new File (FileSystemView.getFileSystemView().getDefaultDirectory().getPath() +  File.separator  +  "shortList.txt");
				try {
					in.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				
				Vector<String> toWrite = fu.getLineByLineVector(in);
				toWrite.add(saveStr);
				fu.writeToFile(toWrite, FileSystemView.getFileSystemView().getDefaultDirectory().getPath() +  File.separator  + "shortList.txt");
				
				System.err.println("Saved to "  +FileSystemView.getFileSystemView().getDefaultDirectory().getPath() +  File.separator  + "shortList.txt");
			}
		});
		
		
		
		
		return vp;
	}

	protected void setSpiroLine(String string) {
		System.err.println("Read in + " + string);
		
		String end = "::";

		mpageClrBtn.setBackground(getAsColor(getValueFor("pageColor", string, end), Color.blue));
		pageColor = mpageClrBtn.getBackground();
		repeatRateTxt.setText(getValueFor("repaintCnt=",string,end));
		rollerRateTxt.setText(getValueFor("rollRate=",string,end));
		mhShiftTxt.setText(getValueFor("hShift=",string,end));
		mvShiftTxt.setText(getValueFor("vShift=",string,end));
		
		
		mainSizeTxt.setText(getValueFor("c1Dia=",string,end));
		bPaintMainBorderCheckbox.setSelected(getValueFor("c1PaintBorder=", string, end).equals("true") ? true : false);
		mmainBorderClrBtn.setBackground(getAsColor(getValueFor("c1BorderColor=",string, end), Color.lightGray));
		mmainstrokeWidthTxt.setText(getValueFor("c1BorderWidth=",string,end));
		bPaintMainFillCheckbox.setSelected(getValueFor("c1PaintFill=", string, end).equals("true") ? true : false);
		mmainFillClrBtn.setBackground(getAsColor(getValueFor("c1FillColor=",string, end), Color.lightGray));
		
		rollerSizeTxt.setText(getValueFor("c2Dia=",string,end));
		paintRollerBorderCheckbox.setSelected(getValueFor("c2PaintBorder=", string, end).equals("true") ? true : false);
		mrollerBorderClrBtn.setBackground(getAsColor(getValueFor("c2BorderColor=",string, end), Color.lightGray));
		mrollerStrokeWidthTxt.setText(getValueFor("c2BorderWidth=",string,end));
		paintRollerFillCheckbox.setSelected(getValueFor("c2PaintFill=", string, end).equals("true") ? true : false);
		mrollerFillClrBtn.setBackground(getAsColor(getValueFor("c2FillColor=",string, end), Color.lightGray));
		

		mdotSizeTxt.setText(getValueFor("c3Dia=",string,end));
		bPaintDotBorderCheckbox.setSelected(getValueFor("c3PaintBorder=", string, end).equals("true") ? true : false);
		mdotBorderClrBtn.setBackground(getAsColor(getValueFor("c3BorderColor=",string, end), Color.lightGray));
		mdotStrokeWidthTxt.setText(getValueFor("c3BorderWidth=",string,end));
		bPaintDotFillCheckbox.setSelected(getValueFor("c3PaintFill=", string, end).equals("true") ? true : false);
		mdotFillClrBtn.setBackground(getAsColor(getValueFor("c3FillColor=",string, end), Color.lightGray));
		
		
		System.err.println("Read in + DONE DONE ");
		
		spRight.doSetup();
		repaint();
	}

	private Color getAsColor(String val,Color def){
		
		Color  test = null;

		String r = getValueFor("[r=", val, ",");
		String g = getValueFor(",g=", val, ",");
		String b = getValueFor(",b=", val, ",");
		String a = getValueFor(",a=", val, "]");

		try {
			test = new Color(spRight.getAsInt(r, 155),spRight.getAsInt(g, 10),spRight.getAsInt(b, 255),spRight.getAsInt(a, 0));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(test == null)
			test = def;
		
		return test;
	}	
	
	
	private String doColorToString(Color clr){
		String savestrWithAlpha = "java.awt.Color[" +
		"r=" + clr.getRed() + ",g=" + clr.getGreen() + ",b=" + clr.getBlue() + ",a="  +  clr.getAlpha() + "]";
		
		return savestrWithAlpha;
	}
	
	
	
	private String getValueFor(String name, String str, String end) {
		
		int atName = str.indexOf(name) + name.length();
		int nameEnd = str.indexOf(end,atName);
		String val =  str.substring(atName, nameEnd);
		return val;
	}

	private JPanel doPageColorPanel() {
		gStackerPnl vp = new gStackerPnl(new BorderLayout());
		vp.setBackground(Color.red);
		vp.setBorder(BorderFactory.createEtchedBorder());
		vp.addItem(mpageClrBtn);
		mpageClrBtn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
			spRight.setBackground(pageColor = SpiroFun.this.doColorChooser(pageColor));
			mpageClrBtn.setBackground(pageColor);
			
				SpiroFun.this.repaint();
			}
			
		});
		
		
		return vp;
	}

	private Component doRepeatRatePanel() {
		gStackerPnl vp = new gStackerPnl(new BorderLayout());
		vp.setBackground(Color.red);
		vp.setBorder(BorderFactory.createEtchedBorder());		
		
		repeatRateTxt.setBorder(BorderFactory.createTitledBorder("RePaints No"));
		repeatRateTxt.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent arg0) {}
			public void keyReleased(KeyEvent arg0) {}
			public void keyPressed(KeyEvent arg0) {
				 
				if(arg0.getKeyCode() == 10){// Enter
					int val = Integer.parseInt(repeatRateTxt.getText());
					
					if( spRight.getRepeatCnt() != val){
					
						spRight.setRepeatCnt((int)val);
						spRight.doSetup();
						spRight.repaint();
					}
				}
				
			}
		});
		
/*		repeatRateTxt.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent arg0) {}
			public void focusGained(FocusEvent arg0) {}
		});*/
		
		
		
		vp.addItem(repeatRateTxt);

		return vp;
	}
	
	
	
	private JPanel doRollRatePanel() {
		
		gStackerPnl vp = new gStackerPnl(new BorderLayout());
		//vp.setBackground(Color.red);
		//vp.setBorder(BorderFactory.createEtchedBorder());		
		
		
		rollerRateTxt.addKeyListener(new KeyListener() {
			
			public void keyTyped(KeyEvent arg0) {}
			public void keyReleased(KeyEvent arg0) {}
			public void keyPressed(KeyEvent arg0) {
				// Enter 
				if(arg0.getKeyCode() == 10){
					
			
					try {
						double val = Double.parseDouble(rollerRateTxt.getText());
						if( val >  rollerRateSlider.getMaximum())
							val = rollerRateSlider.getMaximum();
						
						spRight.setRollerRateDegrees(val);
						rollerRateSlider.setValue((int)val);

					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
					
				}
				
			}
		});
		
		rollerRateTxt.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent arg0) {
				double val = Double.parseDouble(rollerRateTxt.getText());
				if( val >  rollerRateSlider.getMaximum())
					val = rollerRateSlider.getMaximum();
				
				rollerRateSlider.setValue((int)val);
			}
			
			public void focusGained(FocusEvent arg0) {}
		});
		
		
		rollerRateTxt.setBorder(BorderFactory.createTitledBorder("Role Rate (Degrees)"));
		
		rollerRateSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				rollerRateTxt.setText(rollerRateSlider.getValue() + "");
				spRight.doSetup();
				spRight.repaint();
				
			}
		});		
		
		vp.addItem(rollerRateTxt);
		vp.addItem(rollerRateSlider);

		return vp;
	}

	private JPanel doRollerCirclePropertiesPanel() {
		gStackerPnl vp = new gStackerPnl(new BorderLayout());
		vp.setBackground(Color.red);
		vp.setBorder(BorderFactory.createEtchedBorder());
		
		rollerSizeTxt = new JTextField("50");
		rollerSizeTxt.setBorder(BorderFactory.createTitledBorder("Diameter"));
		rollerSizeTxt.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent arg0) {}
			public void focusGained(FocusEvent arg0) {}
		});
		
		rollerSizeTxt.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent arg0) {return;}
			
			@Override
			public void keyReleased(KeyEvent arg0) {return;}
			
			@Override
			public void keyPressed(KeyEvent arg0) {

				// Enter 
				if(arg0.getKeyCode() == 10){
					System.err.println("keyPressed " + arg0.getKeyCode());
					System.err.println("currentSize " + rollerSizeTxt.getText());
					
					int val = Integer.parseInt(rollerSizeTxt.getText());
					if( val >  rollerSizeSlider.getMaximum())
						val = rollerSizeSlider.getMaximum();
					
					rollerSizeSlider.setValue(val);
				}	
			}
		});
		
		
		
		rollerSizeTxt.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent arg0) {
			//	System.err.println("JTextField currentSize -- " + rollerSizeTxt.getText());
				spRight.doSetup();
				spRight.repaint();
				
			}
		});
		
		
		rollerSizeSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				//System.err.println("Value -- " + rollerSizeSlider.getValue());
				rollerSizeTxt.setText(rollerSizeSlider.getValue() + "");
				spRight.doSetup();
				spRight.repaint();
			}
		});
		
		 JTextField title = new JTextField("Roller Circle Properties");
		 title.setFont(mboldFont);
		 title.setEditable(false);
		vp.addItem(title);
		vp.addItem(rollerSizeTxt);
		vp.addItem(rollerSizeSlider);
		
		paintRollerBorderCheckbox = new JCheckBox("Paint Border ??");
		
		paintRollerBorderCheckbox.setSelected(true);
		bPaintRoller = true;
		
		paintRollerBorderCheckbox.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if(paintRollerBorderCheckbox.isSelected() != bPaintRoller){
					bPaintRoller = paintRollerBorderCheckbox.isSelected();
					spRight.doSetup();
					spRight.repaint();
				}
			}
		});
		
		JPanel pan = new JPanel(new BorderLayout(40,10));
		pan.setLayout(new GridLayout(1, 3,10,20));
		pan.add(paintRollerBorderCheckbox);
		mrollerBorderClrBtn = new gJButton("Border COLOR");
		//mrollerBorderClrBtn.setForeground(Color.LIGHT_GRAY);
		pan.add(mrollerBorderClrBtn);
		mrollerBorderClrBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mrollerBorderClrBtn.setBackground(spRight.rollerCir.mcstrokeClr = SpiroFun.this.doColorChooser(spRight.rollerCir.mcstrokeClr));
				SpiroFun.this.repaint();
			}
		});
		mrollerStrokeWidthTxt = new JTextField("2");
		mrollerStrokeWidthTxt.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent arg0) {}
			public void keyReleased(KeyEvent arg0) {}
			public void keyPressed(KeyEvent arg0) {
				spRight.doSetup();
				spRight.repaint();
			}
		});
		
		
		pan.add(mrollerStrokeWidthTxt);
		vp.addItem(pan);
		
		
		
		
		JPanel pan2 = new JPanel(new BorderLayout(40,10));
		pan2.setLayout(new GridLayout(1, 3,10,20));
		
		paintRollerFillCheckbox = new JCheckBox("Fill ??");
		paintRollerFillCheckbox.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent arg0) {
				spRight.doSetup();
				spRight.repaint();
			}
		});
		
		
		
		pan2.add(paintRollerFillCheckbox);
		
		mrollerFillClrBtn = new gJButton("Fill COLOR");
	//	mrollerFillClrBtn.setForeground(null);
		mrollerFillClrBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mrollerFillClrBtn.setBackground(spRight.rollerCir.mcfillClr = SpiroFun.this.doColorChooser(spRight.rollerCir.mcfillClr));
				spRight.repaint();
			}
		});		
		
		pan2.add(mrollerFillClrBtn);
		

		JTextField rollerstrokeWidthTxt =  new JTextField("20");
		rollerstrokeWidthTxt.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent arg0) {}
			public void keyReleased(KeyEvent arg0) {}
			public void keyPressed(KeyEvent arg0) {
				spRight.doSetup();
				spRight.repaint();
			}
		});
		
		// Acting as a spacer only
		rollerstrokeWidthTxt.setVisible(false);
		
		
		pan2.add(rollerstrokeWidthTxt);
		vp.addItem(pan2);
		
		
		
		return vp;
				
	}

	private Component doDotCirclePropertiesPanel() {
		
		gStackerPnl dotCirclePnl = new gStackerPnl(new BorderLayout());
		dotCirclePnl.setBackground(Color.red);
		dotCirclePnl.setBorder(BorderFactory.createEtchedBorder());
		
		
		bPaintDotBorderCheckbox = new JCheckBox("Paint Border ??");
		

		mdotSizeTxt.setBorder(BorderFactory.createTitledBorder("Diameter"));
		mdotSizeTxt.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent arg0) {}
			public void focusGained(FocusEvent arg0) {}
		});
		
		mdotSizeTxt.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent arg0) {return;}
			
			@Override
			public void keyReleased(KeyEvent arg0) {return;}
			
			@Override
			public void keyPressed(KeyEvent arg0) {

				// Enter 
				if(arg0.getKeyCode() == 10){
					
					int val = Integer.parseInt(mdotSizeTxt.getText());
					if( val >  dotSizeSlider.getMaximum())
						val = dotSizeSlider.getMaximum();
					
					dotSizeSlider.setValue(val);
					spRight.doSetup();
					spRight.repaint();
				}	
			}
		});
		
		
		
		mdotSizeTxt.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent arg0) {
				spRight.doSetup();
				spRight.repaint();
				
			}
		});
		
		
		dotSizeSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				mdotSizeTxt.setText(dotSizeSlider.getValue() + "");
				spRight.doSetup();
				spRight.repaint();
			}
		});
		
		JTextField title = new JTextField("Dot Circle Properties");
		title.setFont(mboldFont);
		title.setEditable(false);
		dotCirclePnl.addItem(title);
		dotCirclePnl.addItem(mdotSizeTxt);
		dotCirclePnl.addItem(dotSizeSlider);
		
		
		
		bPaintDotBorderCheckbox.setSelected(true);
		bPaintDot = true;
		bPaintDotBorderCheckbox.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if(bPaintDotBorderCheckbox.isSelected() != bPaintDot){
					bPaintDot = bPaintDotBorderCheckbox.isSelected();
					spRight.doSetup();
					spRight.repaint();
				}
			}
		});
		
		JPanel pan = new JPanel(new BorderLayout(40,10));
		pan.setLayout(new GridLayout(1, 3,10,20));
		pan.add(bPaintDotBorderCheckbox);
		mdotBorderClrBtn = new gJButton("border COLOR");
	//	mdotBorderClrBtn.setForeground(Color.LIGHT_GRAY);
		mdotBorderClrBtn.setBackground(Color.black);
		pan.add(mdotBorderClrBtn);
		mdotBorderClrBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mdotBorderClrBtn.setBackground(spRight.mdot.mcstrokeClr  = SpiroFun.this.doColorChooser(spRight.mdot.mcstrokeClr));
				//SpiroFun.this.repaint();
				spRight.doSetup();
				spRight.repaint();
			}
		});
		
		
		mdotStrokeWidthTxt = new JTextField("2");
		mdotStrokeWidthTxt.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent arg0) {}
			public void keyReleased(KeyEvent arg0) {}
			public void keyPressed(KeyEvent arg0) {
				spRight.doSetup();
				spRight.repaint();
			}
		});
		pan.add(mdotStrokeWidthTxt);
		dotCirclePnl.addItem(pan);
		
		
		JPanel pan2 = new JPanel(new BorderLayout(40,10));
		pan2.setLayout(new GridLayout(1, 3,10,20));
		
		bPaintDotFillCheckbox = new JCheckBox("Fill ??");
		pan2.add(bPaintDotFillCheckbox);
		
		mdotFillClrBtn = new gJButton("Fill COLOR");
		//mdotFillClrBtn.setForeground(null);
		mdotFillClrBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mdotFillClrBtn.setBackground(spRight.mdot.mcfillClr = SpiroFun.this.doColorChooser(spRight.mdot.mcfillClr));
				spRight.repaint();
			}
		});		
		
		pan2.add(mdotFillClrBtn);
		

		JTextField dotstrokeWidthTxt =  new JTextField("20");
		dotstrokeWidthTxt.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent arg0) {}
			public void keyReleased(KeyEvent arg0) {}
			public void keyPressed(KeyEvent arg0) {
				spRight.doSetup();
				spRight.repaint();
			}
		});
		
		// Acting as a spacer only
		dotstrokeWidthTxt.setVisible(false);
		
		
		pan2.add(dotstrokeWidthTxt);
		dotCirclePnl.addItem(pan2);
		
		
		return dotCirclePnl;
	}

	private JPanel doMainCirclePropertiesPanel() {
		
		
		gStackerPnl vp = new gStackerPnl(new BorderLayout(100,100));
		vp.setBackground(Color.red);
		vp.setBorder(BorderFactory.createEtchedBorder());
		
		mainSizeSlider = new JSlider(100, 2000);	
		mainSizeSlider.setValue(800);

		
		mainSizeTxt.setText(mainSizeSlider.getValue() + "");
		mainSizeTxt.setBorder(BorderFactory.createTitledBorder("Diameter"));
		mainSizeTxt.addFocusListener(new FocusListener() {
		
			public void focusLost(FocusEvent arg0) {
			}
			public void focusGained(FocusEvent arg0) {
				
			}
		});
		
		mainSizeTxt.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent arg0) {
				return;
			}
			
			@Override
			public void keyReleased(KeyEvent arg0) {
				return;
			}
			
			@Override
			public void keyPressed(KeyEvent arg0) {

				// Enter 
				if(arg0.getKeyCode() == 10){
					try {
						int val = Integer.parseInt(mainSizeTxt.getText());
						if( val >  mainSizeSlider.getMaximum())
							val = mainSizeSlider.getMaximum();
						
						mainSizeSlider.setValue(val);
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}	
			}
		});
		
		
		
		mainSizeTxt.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				spRight.doSetup();
				repaint();
			}
		});
		
		
		mainSizeSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				mainSizeTxt.setText(mainSizeSlider.getValue() + "");
				spRight.doSetup();
				spRight.repaint();
			}
		});
		
		 JTextField title = new JTextField("Main Circle Properties");
		 title.setFont(mboldFont);
		 title.setEditable(false);
		vp.addItem(title);
		vp.addItem(mainSizeTxt);
		vp.addItem(mainSizeSlider);
		
		
		
		JPanel pan = new JPanel(new BorderLayout(40,10));
		pan.setLayout(new GridLayout(1, 3,10,20));
		
		bPaintMainBorderCheckbox = new JCheckBox("Paint Border ??");
		bPaintMainBorderCheckbox.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				spRight.doSetup();
				spRight.repaint();
			}
		});
		pan.add(bPaintMainBorderCheckbox);
		
		mmainBorderClrBtn = new gJButton("Border COLOR");
		//mmainBorderClrBtn.setForeground(Color.LIGHT_GRAY);
		mmainBorderClrBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mmainBorderClrBtn.setBackground(spRight.mainCir.mcstrokeClr = SpiroFun.this.doColorChooser(spRight.mainCir.mcstrokeClr));
				spRight.repaint();
			}
		});		
		
		pan.add(mmainBorderClrBtn);
		

		mmainstrokeWidthTxt = new JTextField("20");
		
		mmainstrokeWidthTxt.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent arg0) {}
			public void keyReleased(KeyEvent arg0) {
				// Hit enter to Apply
				if(arg0.getKeyCode() == 10){
					spRight.doSetup();
					spRight.repaint();
				}
			}
			public void keyPressed(KeyEvent arg0) {

			}
		});
		
		pan.add(mmainstrokeWidthTxt);
		
		vp.addItem(pan);
		
		JPanel pan2 = new JPanel(new BorderLayout(40,10));
		pan2.setLayout(new GridLayout(1, 3,10,20));
		
		bPaintMainFillCheckbox = new JCheckBox("Fill ??");
		bPaintMainFillCheckbox.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent arg0) {
				spRight.doSetup();
				spRight.repaint();
			}
		});
		
		
		
		pan2.add(bPaintMainFillCheckbox);
		
		mmainFillClrBtn = new gJButton("Fill COLOR");
		//mmainFillClrBtn.setForeground(Color.LIGHT_GRAY);
		mmainFillClrBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mmainFillClrBtn.setBackground(spRight.mainCir.mcfillClr = SpiroFun.this.doColorChooser(spRight.mainCir.mcfillClr));
				spRight.doSetup();
				spRight.repaint();
			}
		});		
		
		pan2.add(mmainFillClrBtn);
		

		JTextField dummy = new JTextField("20");
		// Acting as a spacer only
		dummy.setVisible(false);
		pan2.add(dummy);
		
		vp.addItem(pan2);
		
		
		
		return vp;
				
	}
	
	
	
	
	private Color doColorChooser(Color startClr) {
		Color selectedclr = JColorChooser.showDialog(this,"Choose Color",startClr);
		
/*		if(selectedclr != null)
			System.err.println("selectedclr.getAlpha() " + selectedclr.getAlpha() );
		*/
		if(selectedclr == null)
			selectedclr = startClr;
		
		return selectedclr;
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SpiroFun sf = new SpiroFun("Spiro Fun");

	}

}
