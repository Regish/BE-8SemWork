import javax.swing.JOptionPane;

class LoginDemo
{
public static void main(String arg[])
{
try
{
Login frame=new Login();
frame.setSize(400,150);
frame.setVisible(true);
}
catch(Exception e)
{JOptionPane.showMessageDialog(null, e.getMessage());}
}
}