package calculator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class CalcPane2 extends JPanel {
	private ButtonGroup unitBG = new ButtonGroup();
	private JRadioButton unitByte = new JRadioButton("byte");
	private JRadioButton unitBit = new JRadioButton("bit");
	
	private JTextField speedTF = new JTextField();
	private JLabel speedU = new JLabel();

	private JTextField targetTF = new JFormattedTextField();
	private JLabel targetU = new JLabel();
	
	private JSlider progressS = new JSlider();
	private Font progressFont = new Font("Calibri", Font.PLAIN, 10);
	private JLabel progressV = new JLabel();
	
	private Font resultFont = new Font("Calibri", Font.PLAIN, 20);
	private JLabel estimatedTime = new JLabel("?");
	
	public int speed = -1;
	public int target = -1;
	public int elapsed = 0;
	public Unit unit = Unit.e_byte;

	public CalcPane2() {
		this.setLayout(new BorderLayout());
		
		JLabel warningL = new JLabel("This feature is unavailable at the moment.");
		warningL.setHorizontalAlignment(JLabel.CENTER);
		
		this.add(warningL, BorderLayout.CENTER);
		//this.add(resultP, BorderLayout.SOUTH);
	}
	
	private String dlTime(int time) {
		
		if (time == -1) return "?"; 
		if (time == -2) return "forever";
		
		int sec = time / 1, secRem = time % 60;
		int min = sec / 60, minRem = min % 60;
		int hrs = min / 60, hrsRem = hrs % 24;
		int dys = hrs / 24, dysRem = dys % 365;
		int yrs = dys / 365, yrsRem = yrs;
		
		return  (yrsRem > 0 ? yrsRem + " y " : "") +
				(dysRem > 0 || yrsRem > 0 ? dysRem + " j " : "") +
				(hrsRem > 0 || dysRem > 0 || yrsRem > 0 ? hrsRem + " h " : "") +
				(minRem > 0 || hrsRem > 0 || dysRem > 0 || yrsRem > 0 ? minRem + " m " : "") +
				secRem + " sec.";
	}
	
	
	
	private void updateLabels() {
		speedU.setText(unit.letter + "/s");
		targetU.setText(unit.letter);
	}
	
	
	
	private void updateResult() {
		int time = 0;
		
		if (target == -1 || speed == -1) time = -1;
		else if (speed == 0) time = -2;
		else {
			System.out.println("target w/o prog:" + target);
			target -= elapsed;
			System.out.println("target w/ prog:" + target);
			if (unit == Unit.e_bit) {
				speed /= 8;
				target /= 8;
			}
			time = (int)((double)target / (double)speed);
		}
		
		estimatedTime.setText(dlTime(time));
	}	
	private void updateSpeed() {
		try {
			speed = (speedTF.getText().length() > 0 ? Integer.parseInt(speedTF.getText()) : -1);
			speedTF.setBackground(Color.white);
		} catch (Exception e) {
			speed = -1;
			speedTF.setBackground(Color.decode("#d60a30"));
		}
	}
	private void updateTarget() {
		try {
			target = (targetTF.getText().length() > 0 ? Integer.parseInt(targetTF.getText()) : -1);
			targetTF.setBackground(Color.white);
		} catch (Exception e) {
			target = -1;
			targetTF.setBackground(Color.red);
		}
	}
	private void updateProgress() {
		progressS.setMinimum(0);
		progressS.setMaximum(target >= 0 ? target : 0);
		if (progressS.getMaximum() > 0) {
			int minorS = progressS.getMaximum() / 4;
			int majorS = minorS * 2;
			
		    progressS.setMinorTickSpacing(minorS > 0 ? minorS : 1);
		    progressS.setMajorTickSpacing(majorS > 0 ? majorS : 1);
		    progressS.setLabelTable(progressS.createStandardLabels(majorS > 0 ? majorS : 1));
		}
		elapsed = progressS.getValue();
		
		String[] prefixes = {"", "K", "M", "G"};
		String valRaw = String.valueOf(elapsed); 
		int len = valRaw.length(), maxLen = len <= 3 ? len : 3;
		int range = (len - 1) / 3;

		String val = valRaw.substring(0, len % 3);
		val += (val.length() < 3 && val.length() > 0 && len > 3 ? "." : "");
		val += valRaw.substring(len % 3, maxLen);
		
		progressV.setText(val + " " + prefixes[range] + unit.letter);
	}
	private void updateAll() {
		updateSpeed();
		updateTarget();
		updateProgress();
		updateResult();
	}

	
	
	class SpeedListener implements DocumentListener {
		public void changedUpdate(DocumentEvent e) { 
			updateAll();
		}
		public void removeUpdate(DocumentEvent e) {
			updateAll();
		}
		public void insertUpdate(DocumentEvent e) {
			updateAll();
		}
	}
	class TargetListener implements DocumentListener {
		public void changedUpdate(DocumentEvent e) { 
			updateAll();
		}
		public void removeUpdate(DocumentEvent e) {
			updateAll();
		}
		public void insertUpdate(DocumentEvent e) {
			updateAll();
		}
	}
	class ProgressListener implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			updateAll();
		}
	}
	class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			unit = (e.getSource() == unitByte ? Unit.e_byte : Unit.e_bit);
			updateAll();
			updateLabels();
		}
	}
}
