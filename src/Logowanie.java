import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


public class Logowanie {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Logowanie window = new Logowanie();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	Connection polaczenie = null;
	private JTextField netTekst;
	private JPasswordField hasloTekst;
	
	/**
	 * Create the application.
	 */
	public Logowanie() {
		
		initialize();
		polaczenie = Polaczenie.dbconnect();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 600, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnZaloguj = new JButton("Zaloguj");
		btnZaloguj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try
				{
					String query = "select * from InfoPracownicy where netid = ? and haslo = ? ";
					PreparedStatement prepstate = polaczenie.prepareStatement(query);
					prepstate.setString(1, netTekst.getText());
					prepstate.setString(2, hasloTekst.getText() );
					
					ResultSet wynik = prepstate.executeQuery();
					int count = 0;
					
					while(wynik.next()){
						count++;
					}
					if(count == 1)
					{
						JOptionPane.showMessageDialog(null, "Zalogowano!");
						frame.dispose();
						PracownicyInfo pInfo = new PracownicyInfo();
						pInfo.setVisible(true);
					}
					else
					{
						JOptionPane.showMessageDialog(null, "Niepoprawne dane, spróbuj jeszcze raz!");
					}
					wynik.close();
					prepstate.close();
				}
				catch(Exception e)
				{
					JOptionPane.showMessageDialog(null, e);
				}
			}
		});
		btnZaloguj.setBounds(292, 205, 89, 23);
		frame.getContentPane().add(btnZaloguj);
		
		netTekst = new JTextField();
		netTekst.setBounds(221, 122, 160, 20);
		frame.getContentPane().add(netTekst);
		netTekst.setColumns(10);
		
		hasloTekst = new JPasswordField();
		hasloTekst.setEchoChar('*');
		hasloTekst.setBounds(221, 153, 160, 20);
		frame.getContentPane().add(hasloTekst);
		
		JLabel netId = new JLabel("NetID:");
		netId.setBounds(165, 125, 46, 14);
		frame.getContentPane().add(netId);
		
		JLabel haslo = new JLabel("Has\u0142o:");
		haslo.setBounds(165, 156, 46, 14);
		frame.getContentPane().add(haslo);
		
		JLabel lblTytulBaza = new JLabel("Przyk\u0142adowa Baza Pracownik\u00F3w");
		lblTytulBaza.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblTytulBaza.setBounds(136, 26, 316, 41);
		frame.getContentPane().add(lblTytulBaza);
		
		JLabel netidImg = new JLabel("");
		Image imgNetid = new ImageIcon(this.getClass().getResource("/netid.png")).getImage();
		netidImg.setIcon(new ImageIcon(imgNetid));
		netidImg.setBounds(385, 119, 16, 23);
		frame.getContentPane().add(netidImg);
		
		JLabel passImg = new JLabel("");
		Image imgPass = new ImageIcon(this.getClass().getResource("/pass.png")).getImage();
		passImg.setIcon(new ImageIcon(imgPass));
		passImg.setBounds(385, 153, 16, 23);
		frame.getContentPane().add(passImg);
		
		JLabel lblProjektWykonaMicha = new JLabel("Projekt wykona\u0142: MD");
		lblProjektWykonaMicha.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblProjektWykonaMicha.setBounds(385, 337, 189, 14);
		frame.getContentPane().add(lblProjektWykonaMicha);
	}
}
