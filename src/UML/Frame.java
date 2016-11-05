package UML;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.ToolTipManager;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import metrics.Metric;

public class Frame extends JPanel {

	private static final long serialVersionUID = 1L;
	private Parser parser;
	private JPanel pane;
	private Border border = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);

	private JPanel diagram_name;
	GridBagConstraints name_const = new GridBagConstraints();

	private JPanel classes;
	GridBagConstraints classes_const = new GridBagConstraints();
	// necessaire pour la communication entre association et details
	private String cls_selected;
	String[] liste_classes = new String[] {};

	private JPanel attributs;
	GridBagConstraints attributs_const = new GridBagConstraints();
	@SuppressWarnings("rawtypes")
	private JList attr_list;
	String[] atr_liste = new String[] {};

	private JPanel methodes;
	GridBagConstraints methodes_const = new GridBagConstraints();
	@SuppressWarnings("rawtypes")
	private JList meth_list;
	String[] methode_liste = new String[] {};

	private JPanel sous_classes;
	GridBagConstraints scls_const = new GridBagConstraints();
	@SuppressWarnings("rawtypes")
	private JList scls_list;
	String[] sc = new String[] {};

	private JPanel assoc;
	GridBagConstraints assoc_const = new GridBagConstraints();
	@SuppressWarnings("rawtypes")
	private JList assoc_list;
	String[] asc = new String[] {};

	private JPanel details;
	GridBagConstraints det_const = new GridBagConstraints();
	@SuppressWarnings("rawtypes")
	private JList det_list;
	String[] det = new String[] {};

	private JPanel metriques;
	GridBagConstraints met_const = new GridBagConstraints();
	@SuppressWarnings("rawtypes")
	private JList met_list;

	@SuppressWarnings({ "unchecked", "rawtypes" })

	public Frame(final Main parent, JPanel panneau, Parser p) {
		parser = p;
		pane = panneau;
		// settings pour la ligne avec le nom de la diagramme
		diagram_name = new JPanel();
		diagram_name.setLayout(new BoxLayout(diagram_name, BoxLayout.PAGE_AXIS));
		diagram_name.setBorder(border);
		JLabel titre = new JLabel(parser.getTitre());
		diagram_name.setBackground(new Color(176, 224, 230));
		diagram_name.add(titre);
		name_const.fill = GridBagConstraints.HORIZONTAL;
		name_const.gridx = 0;
		name_const.gridy = 0;
		name_const.gridwidth = 3;
		name_const.gridheight = 1;
		name_const.weightx = 1.0;
		name_const.insets = new Insets(3, 3, 40, 3);
		name_const.anchor = GridBagConstraints.FIRST_LINE_START;
		pane.add(diagram_name, name_const);

		// settings pour la liste des classes
		classes = new JPanel();
		classes.setBackground(new Color(245, 245, 245));
		classes.setLayout(new BoxLayout(classes, BoxLayout.PAGE_AXIS));
		classes.setBorder(border);
		classes.setSize(300, 620);
		/* label */
		JLabel cls_name = new JLabel("CLASSES");
		cls_name.setAlignmentX(Component.CENTER_ALIGNMENT);
		cls_name.setBorder(border);
		cls_name.setOpaque(true);
		cls_name.setBackground(new Color(245, 222, 179));

		// remplir le panneau des classes
		liste_classes = remplirListeClasse();
		@SuppressWarnings({})
		final JList cls_list = new JList(liste_classes);
		cls_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		cls_list.setFixedCellWidth(200);
		cls_list.setFixedCellHeight(20);

		/* ajouter le listner sur la liste des classes */
		cls_list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent evt) {
				if (!evt.getValueIsAdjusting()) {
					JList list = (JList) evt.getSource();
					int idx = list.getSelectedIndex();
					cls_selected = (String) list.getModel().getElementAt(idx);

					// trouver la classe selectee dans la liste
					ArrayList<Classe> cls = parser.model.getlistClasses();
					int nb_cls = cls.size();
					Classe selected = null;
					for (int i = 0; i < nb_cls; i++) {
						Classe temp = cls.get(i);
						if (temp.getName().equals(cls_selected))
							selected = cls.get(i);
					}

					atr_liste = remplirAttributeListe(selected);
					methode_liste = remplirMethodeListe(selected);
					sc = remplirSousClasseListe(selected);
					asc = remplirListeAssociations(selected);
					attr_list.setListData(atr_liste);
					meth_list.setListData(methode_liste);
					scls_list.setListData(sc);
					assoc_list.setListData(asc);

					/*
					 * activer le menu pour les metriques chaque fois qu'on
					 * selectione une class un ActionListener est associe au
					 * menu donc, remove s'impose avant le binding du nouveu
					 * listener
					 * 
					 * 
					 * //parent.getCalculate().removeActionListener(parent);
					 * //parent.getCalculate().addActionListener(parent);
					 * //met_list.setListData(new String[]{});
					 * 
					 * 
					 */
					parent.getExport().removeActionListener(parent);
					parent.getExport().addActionListener(parent);
					parent.enableMetricsMenu();
					showMetrics();
				}
			}
		});

		cls_list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		cls_list.setLayoutOrientation(JList.VERTICAL);
		cls_list.setBackground(new Color(245, 245, 245));

		classes.add(cls_name);
		classes.add(Box.createRigidArea(new Dimension(0, 10)));
		classes.add(cls_list);
		JScrollPane classe_scroller = new JScrollPane();
		/* scroller */
		classe_scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		classe_scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		classe_scroller.getViewport().add(cls_list);
		classe_scroller.getViewport().setPreferredSize(new Dimension(200, 400));
		classes.add(classe_scroller);
		classes_const.fill = GridBagConstraints.BOTH;
		classes_const.weighty = 1.0;
		classes_const.weightx = 1.0;
		classes_const.gridx = 0;
		classes_const.gridy = 1;
		classes_const.gridheight = 3;
		classes_const.insets = new Insets(0, 10, 10, 10);
		classes_const.ipadx = 10;
		classes_const.anchor = GridBagConstraints.FIRST_LINE_START;
		pane.add(classes, classes_const);

		// settings pour la liste des attributs
		attributs = new JPanel();
		attributs.setBackground(new Color(245, 245, 245));
		attributs.setLayout(new BoxLayout(attributs, BoxLayout.PAGE_AXIS));
		attributs.setBorder(border);

		/* label */
		JLabel attr_name = new JLabel("ATTRIBUTS");
		attr_name.setAlignmentX(Component.CENTER_ALIGNMENT);
		attr_name.setBorder(border);
		attr_name.setOpaque(true);
		attr_name.setBackground(new Color(245, 222, 179));

		/* list */
		attr_list = new JList(atr_liste);
		attr_list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		attr_list.setFixedCellWidth(200);
		attr_list.setFixedCellHeight(20);
		cls_list.setLayoutOrientation(JList.VERTICAL);
		attr_list.setBackground(new Color(245, 245, 245));
		attr_list.setPreferredSize(new Dimension(300, 100));
		attr_list.setMinimumSize(new Dimension(300, 100));
		attr_list.setMaximumSize(new Dimension(300, 100));
		@SuppressWarnings("unused")
		JScrollPane listScroller = new JScrollPane();
		attributs.add(attr_name);
		attributs.add(attr_list);
		attributs.add(Box.createRigidArea(new Dimension(0, 10)));
		attributs.add(attr_list);

		/* scroller */
		JScrollPane attr_scroller = new JScrollPane();
		attr_scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		attr_scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		attr_scroller.getViewport().add(attr_list);
		attr_scroller.getViewport().setPreferredSize(new Dimension(300, 100));
		attributs.add(attr_scroller);

		attributs_const.fill = GridBagConstraints.BOTH;
		attributs_const.weightx = 0.7;
		attributs_const.gridx = 1;
		attributs_const.gridy = 1;
		attributs_const.insets = new Insets(0, 10, 10, 10);
		attributs_const.ipady = 10;
		pane.add(attributs, attributs_const);

		// settings pour la liste des methodes
		methodes = new JPanel();
		methodes.setBackground(new Color(245, 245, 245));
		methodes.setLayout(new BoxLayout(methodes, BoxLayout.PAGE_AXIS));
		methodes.setSize(300, 400);
		methodes.setBorder(border);
		/* label */
		JLabel meth_name = new JLabel("METHODES");
		meth_name.setAlignmentX(Component.CENTER_ALIGNMENT);
		meth_name.setBorder(border);
		meth_name.setOpaque(true);
		meth_name.setBackground(new Color(245, 222, 179));
		/* list */
		meth_list = new JList(methode_liste);
		meth_list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		meth_list.setLayoutOrientation(JList.VERTICAL);
		meth_list.setPreferredSize(new Dimension(300, 100));
		meth_list.setMaximumSize(new Dimension(300, 100));
		meth_list.setFixedCellWidth(200);
		meth_list.setFixedCellHeight(20);
		meth_list.setBackground(new Color(245, 245, 245));
		methodes.add(meth_name);
		methodes.add(Box.createRigidArea(new Dimension(0, 10)));
		methodes.add(meth_list);
		/* scroller */
		JScrollPane meth_scroller = new JScrollPane();
		meth_scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		meth_scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		meth_scroller.getViewport().add(meth_list);
		meth_scroller.getViewport().setPreferredSize(new Dimension(300, 100));

		methodes.add(meth_scroller);
		methodes_const.fill = GridBagConstraints.BOTH;
		// methodes_const.weightx = 0.7;
		methodes_const.gridx = 2;
		methodes_const.gridy = 1;
		methodes_const.insets = new Insets(0, 10, 10, 10);
		methodes_const.ipady = 10;
		pane.add(methodes, methodes_const);

		// settings pour la liste des sous classes

		sous_classes = new JPanel();
		sous_classes.setBackground(new Color(245, 245, 245));
		sous_classes.setLayout(new BoxLayout(sous_classes, BoxLayout.PAGE_AXIS));
		sous_classes.setSize(300, 400);
		sous_classes.setBorder(border);

		/* label */

		JLabel scls_name = new JLabel("SOUS CLASSES");
		scls_name.setAlignmentX(Component.CENTER_ALIGNMENT);
		scls_name.setBorder(border);
		scls_name.setOpaque(true);
		scls_name.setBackground(new Color(245, 222, 179));

		/* list */

		scls_list = new JList(sc);
		scls_list.setPreferredSize(new Dimension(300, 100));
		scls_list.setMaximumSize(new Dimension(300, 100));
		scls_list.setFixedCellWidth(200);
		scls_list.setFixedCellHeight(20);
		scls_list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		scls_list.setLayoutOrientation(JList.VERTICAL);
		scls_list.setBackground(new Color(245, 245, 245));
		sous_classes.add(scls_name);
		sous_classes.add(Box.createRigidArea(new Dimension(0, 10)));
		sous_classes.add(scls_list);

		/* scroller */
		JScrollPane scls_scroller = new JScrollPane();
		scls_scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scls_scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scls_scroller.getViewport().add(scls_list);
		scls_scroller.getViewport().setPreferredSize(new Dimension(300, 100));

		sous_classes.add(scls_scroller);
		scls_const.fill = GridBagConstraints.BOTH;
		scls_const.weightx = 0.7;
		scls_const.gridx = 1;
		scls_const.gridy = 2;
		scls_const.insets = new Insets(0, 10, 10, 10);
		scls_const.ipady = 10;
		pane.add(sous_classes, scls_const);

		// settings pour la liste des agregation - associations
		assoc = new JPanel();
		assoc.setBackground(new Color(245, 245, 245));
		assoc.setLayout(new BoxLayout(assoc, BoxLayout.PAGE_AXIS));
		assoc.setSize(300, 400);
		assoc.setBorder(border);

		/* label */

		JLabel assoc_name = new JLabel("AGGREGATION / ASSOCIATION");
		assoc_name.setAlignmentX(Component.CENTER_ALIGNMENT);
		assoc_name.setBorder(border);
		assoc_name.setOpaque(true);
		assoc_name.setBackground(new Color(245, 222, 179));

		/* list */

		assoc_list = new JList(asc);
		assoc_list.setBackground(new Color(245, 245, 245));
		assoc_list.setPreferredSize(new Dimension(300, 100));
		assoc_list.setMaximumSize(new Dimension(300, 100));
		assoc_list.setFixedCellWidth(200);
		assoc_list.setFixedCellHeight(20);

		/* ajouter le listner sur la liste des associations */
		assoc_list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent ev) {
				JList list = (JList) ev.getSource();
				if (!ev.getValueIsAdjusting() && !list.isSelectionEmpty()) {
					int idx = list.getSelectedIndex();
					String assoc_selected = (String) list.getModel().getElementAt(idx);

					String asso_type = assoc_selected.substring(0, assoc_selected.lastIndexOf(" "));
					String asso_nom = assoc_selected.substring(assoc_selected.lastIndexOf(" ") + 1);

					// trouver la classe selectee dans la liste
					ArrayList<Classe> cls = parser.model.getlistClasses();
					int nb_cls = cls.size();
					Classe selected = null;
					for (int i = 0; i < nb_cls; i++) {
						Classe temp = cls.get(i);
						if (temp.getName().equals(cls_selected))
							selected = cls.get(i);
					}

					det = remplirListeDetails(selected, asso_type, asso_nom);
					det_list.setListData(det);
				} else {
					det = new String[] {};
					det_list.setListData(det);
				}
			}
		});

		assoc_list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		assoc_list.setLayoutOrientation(JList.VERTICAL);
		assoc.add(assoc_name);
		assoc.add(Box.createRigidArea(new Dimension(0, 10)));
		assoc.add(assoc_list);

		/* scroller */

		JScrollPane assoc_scroller = new JScrollPane();
		assoc_scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		assoc_scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		assoc_scroller.getViewport().add(assoc_list);
		assoc_scroller.getViewport().setPreferredSize(new Dimension(300, 100));

		assoc.add(assoc_scroller);
		assoc_const.fill = GridBagConstraints.BOTH;
		assoc_const.weightx = 0.7;
		assoc_const.gridx = 2;
		assoc_const.gridy = 2;
		assoc_const.insets = new Insets(0, 10, 10, 10);
		assoc_const.ipady = 10;
		pane.add(assoc, assoc_const);

		// settings pour la liste des details
		details = new JPanel();
		details.setBackground(new Color(245, 245, 245));
		details.setLayout(new BoxLayout(details, BoxLayout.PAGE_AXIS));
		details.setSize(500, 400);
		details.setBorder(border);

		/* label */

		JLabel det_name = new JLabel("DETAILS", SwingConstants.CENTER);
		det_name.setAlignmentX(Component.CENTER_ALIGNMENT);
		det_name.setBorder(border);
		det_name.setOpaque(true);
		det_name.setBackground(new Color(245, 222, 179));

		/* list */

		det_list = new JList(det);
		det_list.setBackground(new Color(245, 245, 245));
		det_list.setMinimumSize(new Dimension(300, 200));
		det_list.setMaximumSize(new Dimension(300, 200));
		det_list.setPreferredSize(new Dimension(300, 200));
		det_list.setFixedCellWidth(200);
		det_list.setFixedCellHeight(20);
		det_list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		det_list.setLayoutOrientation(JList.VERTICAL);
		details.add(det_name);
		details.add(Box.createRigidArea(new Dimension(0, 10)));
		details.add(det_list);

		/* scroller */

		JScrollPane det_scroller = new JScrollPane();
		det_scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		det_scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		det_scroller.getViewport().add(det_list);
		det_scroller.getViewport().setPreferredSize(new Dimension(300, 100));

		details.add(det_scroller);

		det_const.fill = GridBagConstraints.BOTH;
		det_const.weightx = 0.7;
		det_const.gridx = 1;
		det_const.gridy = 3;
		det_const.gridwidth = 2;
		det_const.insets = new Insets(0, 10, 10, 10);
		det_const.ipady = 10;
		pane.add(details, det_const);

		// settings pour la liste des metriques
		metriques = new JPanel();
		metriques.setBackground(new Color(248, 248, 255));
		metriques.setLayout(new BoxLayout(metriques, BoxLayout.PAGE_AXIS));

		/* label */
		JLabel met_name = new JLabel("METRIQUES", SwingConstants.CENTER);
		met_name.setAlignmentX(Component.CENTER_ALIGNMENT);
		met_name.setBorder(border);
		met_name.setOpaque(true);
		met_name.setBackground(new Color(245, 222, 179));

		/* list */
		met_list = new JList(det);
		met_list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		met_list.setLayoutOrientation(JList.VERTICAL);
		met_list.setBackground(new Color(245, 245, 245));
		met_list.setFixedCellWidth(200);
		met_list.setFixedCellHeight(20);

		met_list.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

				int index = met_list.getSelectedIndex();
				ArrayList<Classe> cls = parser.model.getlistClasses();
				int nb_cls = cls.size();
				Classe selected = null;
				for (int i = 0; i < nb_cls; i++) {
					Classe temp = cls.get(i);
					if (temp.getName().equals(cls_selected))
						selected = cls.get(i);
				}
				Metric m = selected.getMetrics().get(index);
				met_list.setToolTipText("" + m.getDescription());
				ToolTipManager.sharedInstance().setInitialDelay(0);
				ToolTipManager.sharedInstance().setDismissDelay(30000);
			}

			public void mouseExited(MouseEvent e) {

				if (met_list.getModel().getSize() != 0 && met_list.getSelectedIndex() != -1) {
					met_list.clearSelection();
					ToolTipManager.sharedInstance().setDismissDelay(0);
				}
			}
		});

		metriques.add(met_name);
		metriques.add(met_list);
		metriques.add(Box.createRigidArea(new Dimension(0, 10)));

		/* scroller */
		JScrollPane met_scroller = new JScrollPane();
		met_scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		met_scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		met_scroller.getViewport().add(met_list);
		met_scroller.getViewport().setPreferredSize(new Dimension(100, 300));

		metriques.add(met_scroller);

		met_const.fill = GridBagConstraints.BOTH;
		met_const.weighty = 1.0;
		met_const.weightx = 0.5;
		met_const.gridx = 3;
		met_const.gridy = 1;
		met_const.gridheight = 3;
		met_const.insets = new Insets(0, 10, 10, 10);
		met_const.ipadx = 10;
		met_const.anchor = GridBagConstraints.FIRST_LINE_START;
		pane.add(metriques, met_const);

		/*
		 * necessaires pour reafficher les informations provenues du parser
		 */
		pane.revalidate();
		pane.repaint();

	}

	// la fonction qui affiche la liste des classes
	public String[] remplirListeClasse() {
		ArrayList<Classe> cls = parser.model.getlistClasses();
		int nb_cls = cls.size();
		String[] c = new String[nb_cls];
		for (int i = 0; i < nb_cls; i++) {
			String nom_cls = cls.get(i).getName();
			c[i] = nom_cls;
		}

		return c;
	}

	// la fonction qui affiche la liste des attributs
	public String[] remplirAttributeListe(Classe c) {
		ArrayList<Attribut> att = c.getAttributes();
		int nb_att = att.size();
		String[] a = new String[nb_att];
		for (int i = 0; i < nb_att; i++) {
			String atr_nom = att.get(i).getName();
			String atr_type = att.get(i).getType();
			// concatenation pour l'affichage
			String atr_affiche = "";
			atr_affiche = atr_affiche.concat(atr_type);
			atr_affiche = atr_affiche.concat(" ");
			atr_affiche = atr_affiche.concat(atr_nom);
			a[i] = atr_affiche;
		}

		return a;
	}

	// la fonction qui affiche la liste des methodes
	public String[] remplirMethodeListe(Classe c) {
		ArrayList<Operation> oppr = c.getOperations();
		int nb_opp = oppr.size();
		String[] result = new String[nb_opp];
		// System.out.print(c.getName() + // nb_opp);
		for (int i = 0; i < oppr.size(); i++) {
			String each_opp = "";
			String val_retur = oppr.get(i).getType();
			each_opp = each_opp.concat(val_retur + " ");
			String nom = oppr.get(i).getMethodeName();
			each_opp = each_opp.concat(nom);
			each_opp = each_opp.concat("(");

			// la liste des parametres

			ArrayList<String> param = oppr.get(i).getParams();
			for (int j = 0; j < param.size(); j++) {
				String each_param = "";
				if (j == 0) {
					each_param = param.get(j);
					each_opp = each_opp.concat(each_param);
				} else {
					each_param = param.get(j);
					each_opp = each_opp.concat(", " + each_param);

				}

			}
			each_opp = each_opp.concat(")");
			result[i] = each_opp;

		}
		return result;
	}

	// la fonction qui affiche la liste des sous-classes
	public String[] remplirSousClasseListe(Classe c) {
		ArrayList<Classe> sc = c.getSubclass();
		int nb_sc = sc.size();
		String[] result = new String[nb_sc];

		for (int i = 0; i < nb_sc; i++) {
			result[i] = sc.get(i).getName();
		}

		return result;
	}

	// la fonction qui affiche la liste des associations / aggregations
	public String[] remplirListeAssociations(Classe c) {
		// liste de classe aggregee
		ArrayList<Aggregation> agg = this.parser.model.getAgg();
		int nb_agg = agg.size();
		// liste de classe en relation
		ArrayList<Association> rel = this.parser.model.getAss();
		ArrayList<String> restemp = new ArrayList<String>();
		int nb_rel = rel.size();
		// String[] result = new String[nb_rel + nb_agg];
		for (int i = 0; i < nb_agg; i++) {
			// traitement pour les classes agregees
			if (agg.get(i).findClass(c)) {
				{
					restemp.add("(A) " + agg.get(i).getPartsName());
				}
			}
		}

		for (int i = 0; i < nb_rel; i++) {
			if (rel.get(i).findClasseRA(c)) {
				restemp.add("(R) " + rel.get(i).getName());
			}
			if (rel.get(i).findClasseRB(c)) {
				restemp.add("(R) " + rel.get(i).getName());
			}
		}
		String[] resultat = new String[restemp.size()];
		for (int i = 0; i < restemp.size(); i++) {
			resultat[i] = restemp.get(i);
		}
		return resultat;
	}

	// la fonction qui affiche les details
	public String[] remplirListeDetails(Classe c, String asso_type, String asso_name) {
		String[] result = new String[5];
		// si aggregation
		if (asso_type.equals("(A)")) {

			ArrayList<Aggregation> agg = this.parser.model.getAgg();

			int nb_agg = agg.size();
			for (int i = 0; i < nb_agg; i++) {
				if (agg.get(i).getPartsName().equals(asso_name)) {
					result[0] = "AGGREGATION \n";
					result[1] = "\t\t CONTAINER \n";
					result[2] = "\t\t\t\t CLASS " + c.getName() + " " + agg.get(i).getContainer().getMult() + "\n";
					result[3] = "\t\t PARTS \n";
					result[4] = "\t\t\t\t CLASS " + agg.get(i).getPartsName() + " " + agg.get(i).getPartsCard();
				}
			}
		}

		// si relation
		if (asso_type.equals("(R)")) {
			ArrayList<Association> rel = this.parser.model.getAss();
			int nb_rel = rel.size();

			for (int i = 0; i < nb_rel; i++) {
				if (rel.get(i).getName().equals(asso_name)) {
					result[0] = "RELATION \n " + rel.get(i).getName();
					result[1] = "\t\t ROLES \n";
					result[2] = "\t\t\t\t CLASS " + rel.get(i).getFirstRelationName() + " "
							+ rel.get(i).getFirstRelationCard() + "\n";
					result[3] = "\t\t\t\t CLASS " + rel.get(i).getSecondRelationName() + " "
							+ rel.get(i).getSecondRelationCard() + "\n";
				}
			}
		}

		return result;
	}

	// la fonction qui calcule les metriques pour toutes les classe
	public void showMetrics() {
		ArrayList<Classe> liste = parser.getListeClasses();
		for (int i = 0; i < liste.size(); i++) {
			Classe courente = liste.get(i);

			ArrayList<Metric> metr = courente.getMetrics();
			// si les metriques ne sont pas encore calculees
			if (metr.size() == 0)
				// pour certaines metriques on a besoin de la liste de classes
				// du parser
				courente.calculateMetrics(liste);
		}

		remplirListeMetriques();
	}

	// la fonction qui affiche les metriques pour la classe selectione
	@SuppressWarnings("unchecked")
	public void remplirListeMetriques() {
		ArrayList<Classe> cls = parser.model.getlistClasses();
		int nb_cls = cls.size();
		Classe selected = null;
		for (int i = 0; i < nb_cls; i++) {
			Classe temp = cls.get(i);
			if (temp.getName().equals(cls_selected))
				selected = cls.get(i);
		}
		ArrayList<Metric> metr = selected.getMetrics();
		int nb_met = metr.size();

		String[] met = new String[nb_met];
		for (int i = 0; i < nb_met; i++) {
			Metric mt = metr.get(i);
			met[i] = mt.toString();

		}

		met_list.setListData(met);
	}

	public void writeCsvFile(Path path, File file) {
		// les constantes generales
		Charset charset = Charset.forName("US-ASCII");
		String delimiter = ",";
		String new_line = "\n";

		// creer et ecrire le fichier csv
		try (BufferedWriter writer = Files.newBufferedWriter(path, charset)) {
			// On recupere le nb de metrics a travers une classe random dans la
			// liste de classes
			int random = parser.getListeClasses().get(0).getMetrics().size();
			writer.write("Classe");
			for (int f = 0; f < random; f++) // nombre fixe de metriques,
			{
				writer.write(delimiter);
				writer.write(parser.getListeClasses().get(0).getMetrics().get(f).getName());

			}
			writer.write(new_line);
			// iteration sur la liste des classes
			int nb_classes = parser.getListeClasses().size();
			for (int i = 0; i < nb_classes; i++) {
				writer.write(parser.getListeClasses().get(i).getName());
				writer.write(delimiter);
				ArrayList<Metric> liste_m = parser.getListeClasses().get(i).getMetrics();
				int nb_metriques = liste_m.size();

				for (int j = 0; j < nb_metriques; j++) {
					Metric m = liste_m.get(j);
					String res = "" + m.getResult();

					/*
					 * traitement pour metrique ANA convertir le resultat float
					 * to string
					 */
					if (j == 0) {
						writer.write(res);
						writer.write(delimiter);
					} else if (j != nb_metriques - 1) {
						writer.write(res);
						writer.write(delimiter);
					} else
						writer.write(res);
				}
				writer.write(new_line);
			}
		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
		}
	}
}