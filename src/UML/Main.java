package UML;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Main extends JFrame implements ActionListener, ItemListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static JMenuBar menubar;
	private JMenu file;
	private JMenu metrics;
	private JMenuItem open;
	private JMenuItem export;
	private static Frame cls;
	private static JPanel contentPane;

	public Main() {
		// le conteneur du menu
		menubar = new JMenuBar();
		menubar.setBackground(Color.lightGray);

		// les items du menu
		file = new JMenu();
		file.setText("File");
		metrics = new JMenu();
		metrics.setText("Metrics");

		// sous-menu pour file
		open = new JMenuItem();
		open.setText("Open file");
		// actionListener pour le bouton open
		open.addActionListener(this);

		export = new JMenuItem();
		export.setText("Export");
		export.addActionListener(this);
		export.setEnabled(false);

		menubar.add(file);
		menubar.add(metrics);
		file.add(open);
		metrics.add(export);

	}

	public void enableMetricsMenu() {
		export.setEnabled(true);
	}

	public JMenuItem getExport() {
		return export;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// ouvrir un fichier
		if ((e.getActionCommand()).equals("Open file")) {
			// Chosir le fichier a lire et faire le filtre UCD
			JFileChooser openFile = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("UCD File", "ucd");
			openFile.setCurrentDirectory(new java.io.File("."));
			openFile.setFileFilter(filter);
			openFile.showOpenDialog(null);
			File file = openFile.getSelectedFile();
			Parser p = new Parser(file);
			cls = new Frame(this, contentPane, p);

		}

		// exporter les metrics
		if ((e.getActionCommand()).equals("Export")) {
			// Chosir le fichier a lire
			JFileChooser saveFile = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV File", "csv");
			saveFile.setFileFilter(filter);
			int returnVal = saveFile.showSaveDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = saveFile.getSelectedFile();
				Path path = Paths.get(file.getAbsolutePath() + ".csv");
				cls.writeCsvFile(path, file);
			}
		}
	}

	public Container createContentPane() {
		// creer le conteneur dans la fenetre
		contentPane = new JPanel(new GridBagLayout());
		contentPane.setOpaque(true);
		contentPane.setBackground(new Color(248, 248, 255));

		return contentPane;
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
	private static void createAndShowGUI() {
		// creer la fenetre
		JFrame frame = new JFrame("UML Diagram");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// initialiser l'interface graphique
		Main gui = new Main();

		frame.setContentPane(gui.createContentPane());

		frame.setJMenuBar(menubar);

		// rendre la fenetre visible
		frame.setSize(1100, 700);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	public void itemStateChanged(ItemEvent arg0) {
		// TODO Auto-generated method stub
	}

}
