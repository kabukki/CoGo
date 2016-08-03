package calculator;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Gui extends JFrame {
	private static final long serialVersionUID = 0;
	
	protected JTabbedPane tabs = new JTabbedPane();
	protected CalcPane1 tab1 = new CalcPane1();
	protected CalcPane2 tab2 = new CalcPane2();
	protected JLabel label = new JLabel();
	
	public static String font = "Calibri";
	
	
	public Gui(String title) {
		super(title);
		// Setting tabs
		this.tabs.add("Time estimation", this.tab1);
		this.tabs.add("Connection requirement", this.tab2);
		
		// Layout
		this.getContentPane().add(this.tabs);

		//this.container.add(this.ctl);

		//this.setContentPane(container);
	}
	
	protected void setLAF() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception e) { e.printStackTrace(); }
	}

}
