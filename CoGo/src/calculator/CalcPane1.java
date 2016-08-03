package calculator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
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

public class CalcPane1 extends JPanel {
	private ButtonGroup unitBG = new ButtonGroup();
	private JRadioButton unitByte = new JRadioButton("byte");
	private JRadioButton unitOctet = new JRadioButton("octet");
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
	private JLabel endTime = new JLabel("?");
	
	public int speed = -1;
	public int target = -1;
	public int elapsed = 0;
	public Unit unit = Unit.e_byte;

	public CalcPane1() {
		this.setLayout(new BorderLayout());
		
		JPanel topP = new JPanel(new GridLayout(1, 2));
		JPanel settingsP = new JPanel(new GridLayout(3, 1));
		JPanel optionsP = new JPanel(new GridLayout(3, 1));
		
		// Speed panel
		JPanel speedP = new JPanel();
		JLabel speedL = new JLabel("Speed");

		speedTF.setPreferredSize(new Dimension(100, 20));
		speedP.add(speedL);
		speedP.add(speedTF);
		speedP.add(speedU);
		
		// Target panel
		JPanel targetP = new JPanel();
		JLabel targetL = new JLabel("Target");

		targetTF.setPreferredSize(new Dimension(100, 20));
		targetP.add(targetL);
		targetP.add(targetTF);
		targetP.add(targetU);

		// Progress panel
		JPanel progressP = new JPanel();
		JLabel progressL = new JLabel("Download progress:");

		progressS.setMinimum(0);
		progressS.setMaximum(0);
	    progressS.setPaintTicks(true);
	    progressS.setPaintLabels(true);
	    progressS.setFont(progressFont);
	    progressV.setText(String.valueOf(elapsed));
	    progressP.setLayout(new BoxLayout(progressP, BoxLayout.Y_AXIS));
	    progressP.add(progressL);
		progressP.add(progressS);
		progressP.add(progressV);
		
		// Unit panel
		JPanel unitP = new JPanel();
		JLabel unitL = new JLabel("Unit: ");

		unitByte.setSelected(true);
		unitBit.setSelected(false);
		unitBG.add(unitByte);
		unitBG.add(unitOctet);
		unitBG.add(unitBit);
		unitP.add(unitL);
		unitP.add(unitByte);
		unitP.add(unitOctet);
		unitP.add(unitBit);
		
		// Info panel
		JPanel infoP = new JPanel(new GridLayout(5, 1));
		JLabel infoL1 = new JLabel("1 octet (o) => 1 byte (B)", JLabel.CENTER);
		JLabel infoL2 = new JLabel("1 byte (B) => 8 bits (b)", JLabel.CENTER);
		JLabel infoL3 = new JLabel("1.000 B = 1 KB", JLabel.CENTER);
		JLabel infoL4 = new JLabel("1.000 KB = 1 MB", JLabel.CENTER);
		JLabel infoL5 = new JLabel("1.000 MB = 1 GB", JLabel.CENTER);

		infoP.add(infoL1);
		infoP.add(infoL2);
		infoP.add(infoL3);
		infoP.add(infoL4);
		infoP.add(infoL5);
		
		// Refresh panel
		JPanel refreshP = new JPanel();
		JButton refreshB = new JButton("Refresh");

		refreshP.add(refreshB);
		refreshP.setAlignmentX(refreshP.getHeight() / 2);
		
		// Result panel
		JPanel resultP = new JPanel(new GridLayout(2, 2));
		JLabel resultL1 = new JLabel("Remaining time: ", JLabel.CENTER);
		JLabel resultL2 = new JLabel("End time: ", JLabel.CENTER);
		
		estimatedTime.setHorizontalAlignment(JLabel.CENTER);
		estimatedTime.setFont(resultFont);
		endTime.setHorizontalAlignment(JLabel.CENTER);
		endTime.setFont(resultFont);
		resultP.add(resultL1);
		resultP.add(estimatedTime);
		resultP.add(resultL2);
		resultP.add(endTime);
		resultP.setBackground(Color.white);

		// Listeners
		unitByte.addActionListener(new ButtonListener());
		unitOctet.addActionListener(new ButtonListener());
		unitBit.addActionListener(new ButtonListener());
		refreshB.addActionListener(new RefreshListener());
		progressS.addChangeListener(new ProgressListener());
		speedTF.getDocument().addDocumentListener(new SpeedListener());
		targetTF.getDocument().addDocumentListener(new TargetListener());
		
		updateProgress();
		updateLabels();
		
		settingsP.setBorder(BorderFactory.createTitledBorder("Settings"));
		optionsP.setBorder(BorderFactory.createTitledBorder("Options"));
		resultP.setBorder(BorderFactory.createTitledBorder("ETA"));
		
		settingsP.add(speedP);
		settingsP.add(targetP);
		settingsP.add(progressP);
		optionsP.add(unitP);
		optionsP.add(infoP);
		optionsP.add(refreshP);
		
		topP.add(settingsP);
		topP.add(optionsP);
		
		this.add(topP, BorderLayout.CENTER);
		this.add(resultP, BorderLayout.SOUTH);
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
				(dysRem > 0 || yrsRem > 0 ? dysRem + " d " : "") +
				(hrsRem > 0 || dysRem > 0 || yrsRem > 0 ? hrsRem + " h " : "") +
				(minRem > 0 || hrsRem > 0 || dysRem > 0 || yrsRem > 0 ? minRem + " m " : "") +
				secRem + " sec.";
	}
	private String endTime(int time) {
		if (time == -1) return "?"; 
		if (time == -2) return "never";
				
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/YY HH:mm:ss");
		Date endDate = new Date(cal.getTimeInMillis() + ((long)time * 1000));
		System.out.println(endDate);

		return df.format(endDate);
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
			target -= elapsed;
			if (unit == Unit.e_bit) {
				speed /= 8;
				target /= 8;
			}
			time = (int)((double)target / (double)speed);
		}
		
		estimatedTime.setText(dlTime(time));
		endTime.setText(endTime(time));
	}	
	private void updateSpeed() {
		try {
			speed = (speedTF.getText().length() > 0 ? Integer.parseUnsignedInt(speedTF.getText()) : -1);
			speedTF.setBackground(Color.white);
		} catch (Exception e) {
			speed = -1;
			speedTF.setBackground(Color.decode("#d60a30"));
		}
	}
	private void updateTarget() {
		try {
			target = (targetTF.getText().length() > 0 ? Integer.parseUnsignedInt(targetTF.getText()) : -1);
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
			unit = (e.getSource() == unitByte ? Unit.e_byte : e.getSource() == unitOctet ? Unit.e_octet : Unit.e_bit);
			updateAll();
			updateLabels();
		}
	}
	class RefreshListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			updateAll();
		}
	}
}
