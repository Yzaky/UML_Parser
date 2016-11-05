package UML;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Scanner;

public class Parser {

	public class java {

	}

	// le scanner qui fait la lecture du fichier
	private Scanner scanner;
	private File file;
	public Model model;

	public Parser(File file) {

		this.file = file;

		try {
			scanner = new Scanner(this.file, "US-ASCII");
			parseFile();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * fonction qui met toutes les declarations dans une liste parcours la liste
	 * et cree les instances de la classe CLASSE
	 */
	private void parseFile() {
		// construir une liste chainee a partir du fichier
		int count = 0;
		ArrayList<String> liste_dec = new ArrayList<String>();

		while (scanner.hasNext()) {
			liste_dec.add(count, scanner.next());
			count++;

		}
		scanner.close();
		// parcurir et parser la liste
		ListIterator<String> i = liste_dec.listIterator();

		while (i.hasNext()) {
			// <model>
			String a = i.next();
			if (a.equals("MODEL"))
				model = new Model(i.next());

			// else erreur - pas de <model>

			// <class_dec>
			if (a.equals("CLASS")) {
				Classe e = new Classe(i.next());
				model.addClass(e);
				// current = c;
				// <attribute_list>
				a = i.next();
				if (a.equals("ATTRIBUTES")) {
					String att = "";
					do {
						i.previous();
						att = att.concat(i.next());
					} while (!i.next().equals("OPERATIONS"));
					i.previous();
					a = i.next();

					if (!att.isEmpty()) // s'il y a des attributs on les retire
						parseAttributs(e, att);

				}
				// <operation_list>
				if (a.equals("OPERATIONS")) {
					// construir la string a parser
					String opp = "";
					do {
						i.previous();
						opp = opp.concat(i.next());

					} while (!i.next().equals(";"));
					i.previous();
					a = i.next();
					// s'il y a des operation on les retire
					if (!opp.isEmpty())
						parseOperations(e, opp);
				}

			}
			// <generalization>
			if (a.equals("GENERALIZATION")) {

				String classe_parent = i.next();
				Classe cm = null;
				// on cherche si la classe existe deja dans la liste du parser
				Classe temp = model.findClasse(classe_parent);
				if (temp != null) {
					cm = temp;

					// on continue avec ses sous-classes
					if (i.next().equals("SUBCLASSES")) {
						String sous_cls = i.next();
						while (!(sous_cls.contains(";"))) {
							String sous_cls_trim = "";
							if (sous_cls.contains(","))
								sous_cls_trim = sous_cls.substring(0, sous_cls.indexOf(","));
							else
								sous_cls_trim = sous_cls;
							Classe sous_classe = model.findClasse(sous_cls_trim);
							if (sous_classe != null) {
								cm.addSubclass(sous_classe);
							}
							sous_cls = i.next();
						}
					}

				}
			}
			// <association>
			if (a.equals("RELATION")) {
				Association relation = new Association(i.next());
				if (i.next().equals("ROLES"))
					;
				// else erreur grammaire ("ROLES token")
				if (i.next().equals("CLASS"))
					;
				// else erreur grammaire ("CLASS token")

				/*
				 * on cherche les classes dans la liste et on etablit la
				 * nouvelle relation entre eux
				 */
				Classe first_rel = null;
				Classe second_rel = null;
				Role premier;
				Role deuxieme;

				String classe_rel = i.next();
				Classe temp = model.findClasse(classe_rel);
				if (temp != null) {
					first_rel = temp;
					premier = new Role(first_rel);
					relation.setFirstRole(premier);
					// On ajoute la relation a la classe pour les metrics
					premier.getClasse().getAssociations().add(relation);
				}
				String cardin_trim = i.next();
				if (cardin_trim.contains(","))
					cardin_trim = cardin_trim.substring(0, cardin_trim.indexOf(","));
				// else erreur grammaire <two_roles>::=<role>,<role>
				relation.setFirstRoleCard(cardin_trim);
				if (i.next().equals("CLASS"))
					;
				// else erreur grammaire ("CLASS token")

				classe_rel = i.next();
				Classe temp2 = model.findClasse(classe_rel);
				if (temp2 != null) {
					second_rel = temp2;
					deuxieme = new Role(second_rel);
					relation.setSecondRole(deuxieme);
					// On ajoute la relation a la classe pour les metrics
					deuxieme.getClasse().getAssociations().add(relation);
				}
				relation.setSecondRoleCard(i.next());
				this.model.addAssoc(relation);

			}
			// <agregation>
			if (a.equals("AGGREGATION")) {
				Role role;
				Role role2;
				Aggregation agg = new Aggregation();
				if (i.next().equals("CONTAINER"))
					;
				// else erreur grammaire ("CONTAINER token")
				if (i.next().equals("CLASS"))
					;
				// else erreur grammaire ("CLASS token")

				String classeRole = i.next();
				Classe tmp = model.findClasse(classeRole);
				if (tmp != null) {
					role = new Role(tmp);
					agg.addRole(role);

				}
				agg.setContainerM(i.next());
				if (i.next().equals("PARTS"))
					;
				// else erreur grammaire ("PARTS token")
				if (i.next().equals("CLASS"))
					;
				// else erreur grammaire ("CLASS token")
				String classeRole2 = i.next();
				Classe tmp2 = model.findClasse(classeRole2);
				if (tmp2 != null) {
					role2 = new Role(tmp2);
					role2.setMult(i.next());
					agg.addPart(role2);

				}
				model.addAggregation(agg);
				// On ajoute l'aggregation aux classes pour calculer les metrics
				tmp.getAggregations().add(agg);
				tmp2.getAggregations().add(agg);
			}

		}
		// apres totues les infos on calcules les metriques
		// current.calculateMetrics();
	}

	public void parseAttributs(Classe c, String att) {
		att = att.replaceAll("ATTRIBUTES", "");
		while (!att.isEmpty()) {
			// plusieurs attributs
			if (att.contains(",")) {
				String at_nom = att.substring(0, att.indexOf(":"));
				String at_type = att.substring(att.indexOf(":") + 1, att.indexOf(","));
				att = att.substring(att.indexOf(",") + 1);
				Attribut attribut = new Attribut(at_nom, at_type);
				c.addAttribut(attribut);
				// un seul attribut
			} else {
				String at_nom = att.substring(0, att.indexOf(":"));
				String at_type = att.substring(att.indexOf(":") + 1);
				Attribut attribut = new Attribut(at_nom, at_type);
				c.addAttribut(attribut);
				att = att.replaceAll(at_nom, "");
				att = att.replaceAll(at_type, "");
				att = att.replaceAll(":", "");
			}

		}

	}

	public void parseOperations(Classe c, String opp) {
		opp = opp.replaceAll("OPERATIONS", "");

		while (!opp.isEmpty()) { // Pour separer les parametres a fin de les
									// gerer seuls
			String control = "";
			control = control.concat(opp.substring(0, opp.indexOf("(") + 1));
			control = control.concat(opp.substring(opp.indexOf(")")));
			// si on a plusiers operations
			if (control.contains(",")) {
				String opp_nom = opp.substring(0, opp.indexOf("("));
				opp = opp.replaceAll(opp_nom, "");
				String opp_params = opp.substring(opp.indexOf("(") + 1, opp.indexOf(")"));
				opp = opp.replaceAll(opp_params, "");
				opp = opp.replaceFirst("\\(", "");
				opp = opp.replaceFirst("\\)", "");
				String opp_vr = opp.substring(opp.indexOf(":") + 1, opp.indexOf(","));
				opp = opp.replaceFirst(":", "");
				opp = opp.replaceAll(opp_vr, "");
				opp = opp.replaceFirst(",", "");
				Operation op = new Operation(opp_nom, opp_vr);
				while (!opp_params.isEmpty()) {
					if (opp_params.contains(",")) {
						opp_params = opp_params.substring(0, opp_params.indexOf(":") + 1);
						String param_type = opp_params.substring(0, opp_params.indexOf(","));
						op.addParametre(opp_params, param_type);
						opp_params.replaceFirst(",", "");

					} else {
						opp_params = opp_params.substring(0, opp_params.indexOf(":") + 1);
						String param_type = opp_params;
						op.addParametre(opp_params, param_type);
						opp_params = opp_params.replaceAll(param_type, "");
					}
				}
				opp = opp.replaceAll(opp_params, "");
				c.addOperation(op);
			}
			// une seule operation
			else {
				String opp_nom = opp.substring(0, opp.indexOf("("));
				opp = opp.replaceAll(opp_nom, "");

				String opp_params = opp.substring(opp.indexOf("(") + 1, opp.indexOf(")"));
				opp = opp.replaceAll(opp_params, "");
				opp = opp.replaceFirst("\\(", "");
				opp = opp.replaceFirst("\\)", "");

				String opp_vr = opp.substring(opp.lastIndexOf(":") + 1);
				opp = opp.replaceFirst(":", "");
				opp = opp.replaceAll(opp_vr, "");
				Operation op = new Operation(opp_nom, opp_vr);
				// retirer les parametres
				while (!opp_params.isEmpty()) {
					if (opp_params.contains(",")) {
						opp_params = opp_params.substring(opp_params.indexOf(":") + 1);
						String param_type = opp_params.substring(0, opp_params.indexOf(","));
						op.addParametre(opp_params, param_type);
						opp_params = opp_params.replaceAll(param_type, "");
						opp_params = opp_params.replaceFirst(",", "");
					} else {
						opp_params = opp_params.substring(opp_params.indexOf(":") + 1);
						String param_type = opp_params;
						op.addParametre(opp_params, param_type);
						opp_params = opp_params.replaceAll(param_type, "");
					}
				}
				opp = opp.replaceAll(opp_params, "");
				c.addOperation(op);
			}
		}
	}

	/* setters and getters pour les attributs prives de la classe */
	public String getTitre() {
		return model.getName();
	}

	public ArrayList<Classe> getListeClasses() {
		return model.getlistClasses();
	}
}
