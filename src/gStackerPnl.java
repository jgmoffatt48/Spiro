

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.LayoutManager;

import javax.swing.JPanel;

public class gStackerPnl extends JPanel {

	
	private JPanel lastPnl = this;

	public gStackerPnl(BorderLayout borderLayout) {
			super(borderLayout);

		
	}

	public void addItem(Component item) {
		JPanel stacker = new JPanel(new BorderLayout(10,10));
		stacker.add(item,BorderLayout.NORTH);
		lastPnl.add(stacker, BorderLayout.CENTER);
		lastPnl  = stacker;
	}
	
	
	
}
