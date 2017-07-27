import java.awt.Color;
import java.awt.Dialog;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import net.proteanit.sql.DbUtils;


public class PracownicyInfo extends JFrame {

	private JPanel contentPane;
	
	Connection polaczenie = null;
	private JTable table;
	private JLabel lblId;
	private JLabel lblImi;
	private JLabel lblNazwisko;
	private JLabel lblNetid;
	private JLabel lblStanowisko;
	private JLabel lblUmowa;
	private JTextField idText;
	private JTextField imieText;
	private JTextField nazwiskoText;
	private JTextField netidText;
	private JTextField stanowiskoText;
	private JTextField umowaText;
	private JButton btnZapisz;
	private JLabel lblDodajPracownika;
	private JButton btnAktualizuj;
	private JButton btnUsu;
	private JComboBox<String> selektor;
	private JTextField szukajText;
	private JLabel lblCzas;
	private JLabel lblData;
	String wybranyTekst;
	private JTextArea notatnik;
	
	public void odswiezTabele()
	{
			try {
				String query = "select id, imie, nazwisko, netid, stanowisko, umowa  from InfoPracownicy";
				PreparedStatement prepstate = polaczenie.prepareStatement(query);
				ResultSet wynik = prepstate.executeQuery();
				table.setModel(DbUtils.resultSetToTableModel(wynik));
				
				prepstate.close();
				wynik.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	public void wypelnijSelektor()
	{
		try
		{
			String query = "select * from InfoPracownicy";
			PreparedStatement prepstate = polaczenie.prepareStatement(query);
			ResultSet wynik = prepstate.executeQuery();
			
			selektor.removeAllItems();
			
			selektor.addItem("Wybierz...");
			while(wynik.next())			{				
				selektor.addItem(wynik.getString("Nazwisko"));				
			}
			
		    /*for(String s:klienci){
		        selektor.addItem(wynik.getString("Nazwisko"));
			*/
			prepstate.close();
			wynik.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void zegar()
	{
		
		Thread zegar = new Thread()
		{
			public void run()
			{
				try
				{
						
						while(true){
						Calendar kalendarz = new GregorianCalendar();
						kalendarz.add(Calendar.MONTH, 1);
						
						int dzien = kalendarz.get(Calendar.DAY_OF_MONTH);
						int miesiac = kalendarz.get(Calendar.MONTH);
						int rok = kalendarz.get(Calendar.YEAR);
						
						int sekundy = kalendarz.get(Calendar.SECOND);
						int minuty = kalendarz.get(Calendar.MINUTE);
						int godz = kalendarz.get(Calendar.HOUR_OF_DAY);
						
						lblCzas.setText("Czas: " +  godz + ":" + minuty + ":"  + sekundy);
						lblData.setText("Data: " + rok + "-" + miesiac + "-" + dzien);
						
						sleep(1000);
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			
		};

		zegar.start();
	}

	/**
	 * Create the frame.
	 */
	public PracownicyInfo() {
		polaczenie = Polaczenie.dbconnect();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Baza pracowników");
		setBounds(100, 100, 1000, 600);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnPlik = new JMenu("Plik");
		menuBar.add(mnPlik);
		
		JMenuItem mntmOtwrz = new JMenuItem("Otw\u00F3rz");
		mntmOtwrz.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
		mntmOtwrz.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				{
					JFileChooser fc = new JFileChooser();
					if(fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
						File plik = fc.getSelectedFile();
						//JOptionPane.showMessageDialog(null, "Wybrany plik to " + plik.getAbsolutePath());
						try {
							Scanner sc = new Scanner(plik);
							while(sc.hasNext()){
								notatnik.append(sc.nextLine() + "\n");
							}
						} catch (FileNotFoundException e1) {
							System.out.println("Nie mo¿na otworzyæ pliku!");
							e1.printStackTrace();
						}
					}
				}
			}
		});
		mnPlik.add(mntmOtwrz);

		
		JMenuItem mntmZapisz = new JMenuItem("Zapisz");
		mntmZapisz.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
		mntmZapisz.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				if(fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION){
					File plik = fc.getSelectedFile();
					//JOptionPane.showMessageDialog(null, "Wybrany plik to " + plik);
					try {
						PrintWriter sw = new PrintWriter(plik);
						Scanner sc = new Scanner(notatnik.getText());
						while(sc.hasNext()){
							sw.println(sc.nextLine() + "\n");						
						}
						sw.close();
					} catch (FileNotFoundException e1) {
						System.out.println("Nie mo¿na zapisaæ pliku!");
						e1.printStackTrace();
					}
				}
			}
		});
		mnPlik.add(mntmZapisz);

		
		JMenuItem mntmDrukuj = new JMenuItem("Drukuj");
		mntmDrukuj.setAccelerator(KeyStroke.getKeyStroke("ctrl P"));
		mntmDrukuj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null, "Jeszcze nieobs³ugiwane... :)", "Drukuj", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		mnPlik.add(mntmDrukuj);
		
		JSeparator separator = new JSeparator();
		mnPlik.add(separator);
		
		JMenuItem mntmZamknij = new JMenuItem("Zamknij");
		mntmZamknij.setAccelerator(KeyStroke.getKeyStroke("ctrl X"));
		mntmZamknij.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int odpowiedz = JOptionPane.showConfirmDialog(null, "Czy napewno chcesz wyjœæ?", "Wyjœcie", JOptionPane.YES_NO_OPTION);
				if(odpowiedz==JOptionPane.YES_OPTION)
				{
					System.exit(JFrame.EXIT_ON_CLOSE);
				}

			}
		});
		mnPlik.add(mntmZamknij);
		
		JMenu mnEdycja = new JMenu("Edycja");
		menuBar.add(mnEdycja);
		
		JMenuItem mntmKopiuj = new JMenuItem("Kopiuj");
		mntmKopiuj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				wybranyTekst = notatnik.getSelectedText();
			}
		});
		mntmKopiuj.setAccelerator(KeyStroke.getKeyStroke("ctrl C"));
		mnEdycja.add(mntmKopiuj);
		
		JMenuItem mntmWklej = new JMenuItem("Wklej");
		mntmWklej.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				notatnik.insert(wybranyTekst, notatnik.getCaretPosition());
			}
		});
		mntmWklej.setAccelerator(KeyStroke.getKeyStroke("ctrl V"));
		mnEdycja.add(mntmWklej);
		
		JSeparator separator_3 = new JSeparator();
		mnEdycja.add(separator_3);
		
		JMenuItem mntmZmianaKoloruTa = new JMenuItem("Zmiana koloru t\u0142a");
		mntmZmianaKoloruTa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Color wybranyKolor = JColorChooser.showDialog(null, "Wybor koloru", Color.GRAY);
				contentPane.setBackground(wybranyKolor);
			}
		});
		mnEdycja.add(mntmZmianaKoloruTa);
		
		JMenu mnNarzdzia = new JMenu("Narz\u0119dzia");
		menuBar.add(mnNarzdzia);
		
		JMenuItem mntmEksportDoCsv = new JMenuItem("Eksport do CSV");
		mntmEksportDoCsv.setEnabled(false);
		mnNarzdzia.add(mntmEksportDoCsv);
		
		
		JMenu mnSkrki = new JMenu("Sk\u00F3rki");
		menuBar.add(mnSkrki);
		
		JMenu mnWybierzSkrke = new JMenu("Wybierz sk\u00F3rke");
		mnSkrki.add(mnWybierzSkrke);
		
		JMenuItem mntmWindows = new JMenuItem("Windows");
		mntmWindows.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
						| UnsupportedLookAndFeelException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				for (Window w : Window.getWindows()) {
				    SwingUtilities.updateComponentTreeUI(w);
				    if (w.isDisplayable() &&
				        (w instanceof Frame ? !((Frame)w).isResizable() :
				        w instanceof Dialog ? !((Dialog)w).isResizable() :
				        true)) w.pack();
				}
			}
		});
		mnWybierzSkrke.add(mntmWindows);
		
		JSeparator separator_1 = new JSeparator();
		mnWybierzSkrke.add(separator_1);
		
		JMenuItem mntmNib = new JMenuItem("Nimbus");
		mntmNib.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
						| UnsupportedLookAndFeelException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				for (Window w : Window.getWindows()) {
				    SwingUtilities.updateComponentTreeUI(w);
				    if (w.isDisplayable() &&
				        (w instanceof Frame ? !((Frame)w).isResizable() :
				        w instanceof Dialog ? !((Dialog)w).isResizable() :
				        true)) w.pack();
				}
			}
		});
		mnWybierzSkrke.add(mntmNib);
		
		JSeparator separator_2 = new JSeparator();
		mnWybierzSkrke.add(separator_2);
		
		JMenuItem mntmJava = new JMenuItem("Metal");
		mntmJava.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
						| UnsupportedLookAndFeelException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				for (Window w : Window.getWindows()) {
				    SwingUtilities.updateComponentTreeUI(w);
				    if (w.isDisplayable() &&
				        (w instanceof Frame ? !((Frame)w).isResizable() :
				        w instanceof Dialog ? !((Dialog)w).isResizable() :
				        true)) w.pack();
				}
			}
		});
		mnWybierzSkrke.add(mntmJava);
		
		JMenu mnInformacje = new JMenu("Informacje");
		menuBar.add(mnInformacje);
		
		JMenuItem mntmInfo = new JMenuItem("Info");
		mntmInfo.setAccelerator(KeyStroke.getKeyStroke("ctrl I"));
		mntmInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null, "Prosty program, którego zadaniem jest pobieranie wartoœci z bazy danych (sqlite) i wstawienie ich do tabeli."
						+ "\nZa pomoc¹ interfejsu mo¿emy dodawaæ u¿ytkownika, usuwaæ oraz uaktalniaæ dane.\nProgram wykorzystuje biblioteki swing, awt, jdbc oraz wielu innych ciekawych narzêdzi.\n"
						+ "Interfejs zosta³ wykonany za pomoc¹ wbudowanego w program Eclipse -> Windows Builder\nWersja programu: 1.0", "Informacje", JOptionPane.INFORMATION_MESSAGE); 
								
			}
		});
		mnInformacje.add(mntmInfo);
		
		JMenu mnPomoc = new JMenu("Pomoc");
		menuBar.add(mnPomoc);
		
		JMenuItem mntmKontakt = new JMenuItem("Kontakt");
		mntmKontakt.setAccelerator(KeyStroke.getKeyStroke("ctrl H"));
		mntmKontakt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null, "O autorze: MD"
						+ "\nKontakt: test@email.com", "Kontakt", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		mnPomoc.add(mntmKontakt);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		
		
		JButton btnWczytaj = new JButton("Wczytaj pracownik\u00F3w");
		btnWczytaj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					String query = "select id, imie, nazwisko, netid, stanowisko, umowa  from InfoPracownicy";
					PreparedStatement prepstate = polaczenie.prepareStatement(query);
					ResultSet wynik = prepstate.executeQuery();
					table.setModel(DbUtils.resultSetToTableModel(wynik));
					
					prepstate.close();
					wynik.close();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		btnWczytaj.setBounds(21, 33, 165, 20);
		contentPane.add(btnWczytaj);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(21, 76, 560, 431);
		contentPane.add(scrollPane);
		
		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				try
				{
					int wiersz = table.getSelectedRow();
					String ID_ = (table.getModel().getValueAt(wiersz, 0)).toString();
					
					String query = "select * from InfoPracownicy where id = '"+ID_+"' ";
					PreparedStatement prepstate = polaczenie.prepareStatement(query);
					
					ResultSet wynik = prepstate.executeQuery();
					
					
					while(wynik.next())
					{
						idText.setText(wynik.getString("ID"));
						imieText.setText(wynik.getString("Imie"));
						nazwiskoText.setText(wynik.getString("Nazwisko"));
						netidText.setText(wynik.getString("NetID"));
						stanowiskoText.setText(wynik.getString("Stanowisko"));
						umowaText.setText(wynik.getString("Umowa"));
					}
					prepstate.close();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		});
		scrollPane.setViewportView(table);
		
		lblId = new JLabel("ID:");
		lblId.setBounds(591, 107, 32, 14);
		contentPane.add(lblId);
		
		lblImi = new JLabel("Imi\u0119:");
		lblImi.setBounds(591, 135, 46, 14);
		contentPane.add(lblImi);
		
		lblNazwisko = new JLabel("Nazwisko:");
		lblNazwisko.setBounds(591, 160, 60, 14);
		contentPane.add(lblNazwisko);
		
		lblNetid = new JLabel("NetID:");
		lblNetid.setBounds(591, 185, 46, 14);
		contentPane.add(lblNetid);
		
		lblStanowisko = new JLabel("Stanowisko:");
		lblStanowisko.setBounds(591, 210, 73, 14);
		contentPane.add(lblStanowisko);
		
		lblUmowa = new JLabel("Umowa:");
		lblUmowa.setBounds(591, 235, 46, 14);
		contentPane.add(lblUmowa);
		
		idText = new JTextField();
		idText.setBounds(692, 104, 136, 20);
		contentPane.add(idText);
		idText.setColumns(10);
		
		imieText = new JTextField();
		imieText.setColumns(10);
		imieText.setBounds(692, 132, 136, 20);
		contentPane.add(imieText);
		
		nazwiskoText = new JTextField();
		nazwiskoText.setColumns(10);
		nazwiskoText.setBounds(692, 157, 136, 20);
		contentPane.add(nazwiskoText);
		
		netidText = new JTextField();
		netidText.setColumns(10);
		netidText.setBounds(692, 182, 136, 20);
		contentPane.add(netidText);
		
		stanowiskoText = new JTextField();
		stanowiskoText.setColumns(10);
		stanowiskoText.setBounds(692, 207, 136, 20);
		contentPane.add(stanowiskoText);
		
		umowaText = new JTextField();
		umowaText.setColumns(10);
		umowaText.setBounds(692, 232, 136, 20);
		contentPane.add(umowaText);
		
		btnZapisz = new JButton("Dodaj");
		btnZapisz.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try
				{
					String query = "replace into InfoPracownicy (id, imie, nazwisko, netid, stanowisko, umowa) values (?, ?, ?, ?, ?, ?)";
					PreparedStatement preparestate = polaczenie.prepareStatement(query);
					preparestate.setString(1, idText.getText());
					preparestate.setString(2, imieText.getText());
					preparestate.setString(3, nazwiskoText.getText());
					preparestate.setString(4, netidText.getText());
					preparestate.setString(5, stanowiskoText.getText());
					preparestate.setString(6, umowaText.getText());
					
					
					
					if(Integer.parseInt(idText.getText()) == 1)
					{						
						JOptionPane.showMessageDialog(null, "B³¹d! Zduplikowane ID, spróbuj ponownie!");						
					}	
					else
					{
						preparestate.execute();
						JOptionPane.showMessageDialog(null, "Zapisano!");						
					}
					preparestate.close();			
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				
				wypelnijSelektor();
				odswiezTabele();
				

			}
		});
		btnZapisz.setBounds(591, 297, 103, 23);
		contentPane.add(btnZapisz);
		
		lblDodajPracownika = new JLabel("Dodaj pracownika:");
		lblDodajPracownika.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblDodajPracownika.setBounds(591, 73, 136, 20);
		contentPane.add(lblDodajPracownika);
		
		btnAktualizuj = new JButton("Aktualizuj");
		btnAktualizuj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try
				{
					String query = "update InfoPracownicy set id='"+idText.getText()+"', imie='"+imieText.getText()+"', nazwisko='"+nazwiskoText.getText()+"', netid='"+netidText.getText()+"', stanowisko ='"+stanowiskoText.getText()+"', umowa='"+umowaText.getText()+"' where id = '"+idText.getText()+"' ";
					PreparedStatement preparestate = polaczenie.prepareStatement(query);
					
					if(Integer.parseInt(idText.getText()) == 1)
					{						
						JOptionPane.showMessageDialog(null, "Nie mo¿na zmieniæ konta administratora!");						
					}
					else{
						preparestate.execute();
						JOptionPane.showMessageDialog(null, "Zaktualizowano!");						
					}
					
					preparestate.close();		
				
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				
				odswiezTabele();
				wypelnijSelektor();
			}
			
		});
		btnAktualizuj.setBounds(713, 297, 103, 23);
		contentPane.add(btnAktualizuj);
		
		btnUsu = new JButton("Usu\u0144");
		btnUsu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int czyZamknac = JOptionPane.showConfirmDialog(null, "Czy napewno chcesz usun¹æ u¿ytkownika?", "Usuwanie", JOptionPane.YES_NO_OPTION);
				if(czyZamknac == 0)
				{
					try
					{
						String query = "delete from InfoPracownicy where id = '"+idText.getText()+"' ";
						PreparedStatement preparestate = polaczenie.prepareStatement(query);						
						
						if(Integer.parseInt(idText.getText()) == 1)
						{							
							JOptionPane.showMessageDialog(null, "Nie mo¿na usun¹æ konta Administratora");							
						}
						else
						{
							preparestate.execute();
							JOptionPane.showMessageDialog(null, "Usuniêto!");						
						}
						preparestate.close();					
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}	
				}
				//wypelnijSelektor();
				odswiezTabele();
				wypelnijSelektor();
				
			}
		});
		btnUsu.setBounds(859, 103, 103, 23);
		contentPane.add(btnUsu);
		
		selektor = new JComboBox();
		selektor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try
				{
					String query = "select * from InfoPracownicy where nazwisko = ? ";
					PreparedStatement prepstate = polaczenie.prepareStatement(query);
					prepstate.setString(1, (String)selektor.getSelectedItem());
					ResultSet wynik = prepstate.executeQuery();
					
					
					while(wynik.next())
					{
						idText.setText(wynik.getString("ID"));
						imieText.setText(wynik.getString("Imie"));
						nazwiskoText.setText(wynik.getString("Nazwisko"));
						netidText.setText(wynik.getString("NetID"));
						stanowiskoText.setText(wynik.getString("Stanowisko"));
						umowaText.setText(wynik.getString("Umowa"));
					}
					prepstate.close();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		});
		selektor.setBounds(838, 298, 124, 20);
		contentPane.add(selektor);
		
		JLabel lblWybierz = new JLabel("Wybierz:");
		lblWybierz.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblWybierz.setBounds(871, 260, 65, 14);
		contentPane.add(lblWybierz);
		
		szukajText = new JTextField();
		szukajText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				try
				{
					String query = "select * from InfoPracownicy where Nazwisko = ? ";
					PreparedStatement prepstate = polaczenie.prepareStatement(query);
					prepstate.setString(1, szukajText.getText() );
					ResultSet wynik = prepstate.executeQuery();
					
					table.setModel(DbUtils.resultSetToTableModel(wynik));
					
					prepstate.close();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		});
		szukajText.setBounds(314, 33, 200, 20);
		contentPane.add(szukajText);
		szukajText.setColumns(10);
		
		JLabel lblSzukaj = new JLabel("Nazwisko:");
		lblSzukaj.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblSzukaj.setBounds(228, 35, 73, 14);
		contentPane.add(lblSzukaj);
		
		lblCzas = new JLabel("Czas");
		lblCzas.setBounds(871, 11, 113, 23);
		contentPane.add(lblCzas);
		
		lblData = new JLabel("Data");
		lblData.setBounds(871, 32, 103, 23);
		contentPane.add(lblData);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(591, 376, 372, 132);
		contentPane.add(scrollPane_1);
		
		notatnik = new JTextArea();
		scrollPane_1.setViewportView(notatnik);
		
		JLabel lblNotatnik = new JLabel("Notatnik");
		lblNotatnik.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNotatnik.setBounds(591, 351, 60, 14);
		contentPane.add(lblNotatnik);
		
		JButton btnWyczy = new JButton("Wyczy\u015B\u0107");
		btnWyczy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				idText.setText("");
				imieText.setText("");
				nazwiskoText.setText("");
				netidText.setText("");
				stanowiskoText.setText("");
				umowaText.setText("");
				
			}
		});
		btnWyczy.setBounds(859, 156, 103, 23);
		contentPane.add(btnWyczy);
		
		zegar();
		
		wypelnijSelektor();
		
	}
}
