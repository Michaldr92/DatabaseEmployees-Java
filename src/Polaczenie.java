import java.sql.Connection;
import java.sql.DriverManager;

import javax.swing.JOptionPane;



public class Polaczenie {
	Connection polaczenie = null;
	
	public static Connection dbconnect(){
		try
		{
			Class.forName("org.sqlite.JDBC");
			//Connection polaczenie = DriverManager.getConnection("jdbc:sqlite:C:\\Desktop\\BazaPracownikow.sqlite");
			Connection polaczenie = DriverManager.getConnection("jdbc:sqlite:BazaPracownikow.sqlite");
			JOptionPane.showMessageDialog(null, "Sukces: Po³¹czono");
			return polaczenie;
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "Nie mo¿na po³¹czyæ siê z baz¹");
			return null;
		}
	}
	
}
