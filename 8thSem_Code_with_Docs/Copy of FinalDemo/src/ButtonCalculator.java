import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

@SuppressWarnings("serial")
public class ButtonCalculator extends JFrame implements ActionListener
{
    private JButton[] buttons;
    private JTextField display;

    public ButtonCalculator()
    {
        display = new JTextField();
        display.setEditable( false );
        display.setHorizontalAlignment(JTextField.RIGHT);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout( new GridLayout(0, 5) );
        buttons = new JButton[10];

        for (int i = 0; i < buttons.length; i++)
        {
            String text = String.valueOf(i);
            JButton button = new JButton( text );
            button.addActionListener( this );
            button.setMnemonic( text.charAt(0) );
            button.setBorder( new LineBorder(Color.BLACK) );
            buttons[i] = button;
            buttonPanel.add( button );
        }

        getContentPane().add(display, BorderLayout.NORTH);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        setResizable( false );
    }

    public void actionPerformed(ActionEvent e)
    {
        JButton source = (JButton)e.getSource();
        display.replaceSelection( source.getActionCommand() );
    }

    public static void main(String[] args)
    {
        ButtonCalculator frame = new ButtonCalculator();
        frame.setDefaultCloseOperation( EXIT_ON_CLOSE );
        frame.pack();
        frame.setLocationRelativeTo( null );
        frame.setVisible(true);
    }
}
/*
shareimprove this answer
	
answered Apr 30 '13 at 16:27
camickr
163k874136
	
add a comment
up vote
1
down vote
	

You need to retrieve the JButton on which ActionEvent is fired and then append the text retrieved from the JButton to the JTextField. Here is the short Demo:
enter image description here

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
class EClock extends JFrame
{
    JTextField tf;
    public void createAndShowGUI()
    {
        setTitle("Eclock");
        Container c = getContentPane();
        tf = new JTextField(10);
        JPanel cPanel = new JPanel();
        JPanel nPanel = new JPanel();
        nPanel.setLayout(new BorderLayout());
        nPanel.add(tf);
        cPanel.setLayout(new GridLayout(4,4));
        for (int i =0 ; i < 10 ; i++)
        {
            JButton button = new JButton(String.valueOf(i));
            cPanel.add(button);
            button.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent evt)
                {
                    String val = ((JButton)evt.getSource()).getText();
                    tf.setText(tf.getText()+val);
                }
            });
        }
        c.add(cPanel);
        c.add(nPanel,BorderLayout.NORTH);
        setSize(200,250);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                EClock ec = new EClock();
                ec.createAndShowGUI();
            }
        });
    }
}

*/