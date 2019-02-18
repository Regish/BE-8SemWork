import java.awt.event.*;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.io.File;
import java.io.IOException;


public class GRMDemo {

	JFrame jtfMainFrame;
	JButton jbnButton1, jbnButton2, jbnButton3, jbnButton4;
	JTextField jtfInput;
	JTextArea jtAreaOutput;
	JPanel jplPanel;
	Container contentPane;
	JTextArea jhlp= new JTextArea();
	public GRMDemo() {
		jtfMainFrame = new JFrame("Which Button Demo");
		jtfMainFrame.setSize(25,25);
		jbnButton1 = new JButton("About");
		jbnButton2 = new JButton("Run");
		jbnButton3 = new JButton("Help");
		jbnButton4 = new JButton("Exit");
		
		//jtfInput = new JTextField(20);
		
		jplPanel = new JPanel();
		jbnButton1.setMnemonic(KeyEvent.VK_I); //Set ShortCut Keys
		jbnButton1.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if ("About".equals(e.getActionCommand())) {
					
					 File myFile = new File( "About.pdf");
		                try {
							Desktop.getDesktop().open(myFile);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					//C:/Users/Gourav/OpenCVworkspace
					jtfInput = new JTextField(0);
					jtfInput.addActionListener(this);
					jtAreaOutput = new JTextArea(50,50);
					jtAreaOutput.setCaretPosition(jtAreaOutput.getDocument()
							.getLength());
					jtAreaOutput.setEditable(false);
					jtfInput.setText("Button 1!");	
					
					jbnButton1.setBackground(Color.cyan);
					jbnButton2.setBackground(Color.GREEN);
					jbnButton3.setBackground(Color.GREEN);
					jbnButton4.setBackground(Color.GREEN);
					jplPanel.add(jtfInput);
					}
				
				
			}

		});
		jbnButton2.setMnemonic(KeyEvent.VK_I);
		jbnButton2.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if ("Run".equals(e.getActionCommand())) {
								
					MainAppFrame.main(null);
					jbnButton2.setBackground(Color.CYAN);
					jbnButton1.setBackground(Color.GREEN);
					jbnButton3.setBackground(Color.GREEN);
					jbnButton4.setBackground(Color.GREEN);
				}
				
				//jtfInput.setText("Button 2!");
			}

		
		});
		jbnButton3.setMnemonic(KeyEvent.VK_I);
		jbnButton3.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if ("Help".equals(e.getActionCommand())) {
					
					File myFile = new File( "Help.pdf");
	                try {
						Desktop.getDesktop().open(myFile);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					jbnButton3.setBackground(Color.CYAN);
					jbnButton2.setBackground(Color.GREEN);
					jbnButton1.setBackground(Color.GREEN);
					jbnButton4.setBackground(Color.GREEN);
					
					//C:/Users/Gourav/OpenCVworkspace/FinalDemo/
					//String arg0="This is an Help";
				//	jhlp.setText(arg0);
				
				}
				
				//jtfInput.setText("Button 3!");
			}
		});
		jbnButton4.setMnemonic(KeyEvent.VK_I);
		jbnButton4.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if ("Exit".equals(e.getActionCommand())) {
					jbnButton4.setBackground(Color.CYAN);
					System.exit(0);
					
					jbnButton4.setBackground(Color.CYAN);
					jbnButton2.setBackground(Color.GREEN);
					jbnButton3.setBackground(Color.GREEN);
					jbnButton1.setBackground(Color.GREEN);
					}
				
				
				//jtfInput.setText("Button 4!");
			}
		});
		jplPanel.setLayout(new FlowLayout());
		
		jbnButton1.setBackground(Color.GREEN);
		jbnButton2.setBackground(Color.GREEN);
		jbnButton3.setBackground(Color.GREEN);
		jbnButton4.setBackground(Color.GREEN);
		jplPanel.add(jbnButton1);
		jplPanel.add(jbnButton2);
		jplPanel.add(jbnButton3);
		jplPanel.add(jbnButton4);
		jplPanel.add(jhlp);
		jtfMainFrame.getContentPane().add(jplPanel, BorderLayout.CENTER);
		jtfMainFrame.setBackground(Color.black);
		jtfMainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jtfMainFrame.setUndecorated(true);
		jplPanel.setBackground(Color.ORANGE);
		jtfMainFrame.pack();
		jtfMainFrame.setSize(400, 200);
		jtfMainFrame.setVisible(true);
		

		
	}
	public static void main(String[] args) {
		// Set the look and feel to Java Swing Look
		try {
			UIManager.setLookAndFeel(UIManager
					.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
		}
	@SuppressWarnings("unused")
	GRMDemo application = new GRMDemo();
	}
}