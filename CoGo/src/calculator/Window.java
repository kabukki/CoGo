package calculator;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class Window extends Gui {
	private static final long serialVersionUID = 0;

	public Window(String title) {
		super(title);
		this.setTitle(title);
		this.setSize(500, 400);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setResizable(false);
		this.setUndecorated(false); // bordures

		this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
            	closeApp();
            }
        });
		
		this.setLAF();
		this.setVisible(true);
	}
	
	private void closeApp() {
		System.exit(0);
	}
}
