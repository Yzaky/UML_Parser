package metrics;
import java.util.ArrayList;

import UML.Classe;
import UML.Operation;

public class Etc extends Metric {
	private final String name = "ETC";
	private final String description = "ETC : Nombre de fois où la classe courante apparait comme types des arguments dans les méthodes des autres classes du diagramme.";

	private Classe ci;
	private ArrayList<Classe> listeClasse = new ArrayList<Classe>();
	private float result;

	public Etc(Classe ci, ArrayList<Classe> listeClasse) {
		this.ci = ci;
		this.listeClasse = listeClasse;
		calculate();
	}

	@Override
	public void calculate() {
		// TODO Auto-generated method stub
		int counter = 0;
		ArrayList<String> params = new ArrayList<String>();

		// on construit une liste de tous les parametres(pas uniques) des
		// methodes des classes dans la liste
		for (int i = 0; i < listeClasse.size(); i++) {
			Classe cl = listeClasse.get(i);
			for (int j = 0; j < cl.getOperations().size(); j++) {
				Operation op = cl.getOperations().get(j);
				for (int k = 0; k < op.getParams().size(); k++) {
					String param = op.getParams().get(k);
					params.add(param);
				}
			}
		}

		/*
		 * on verifie dans la liste des parametres trouves si le nom de la
		 * classe existe et on incremente le compteur
		 */
		for (int i = 0; i < params.size(); i++) {
			if ((ci.getName()).equals(params.get(i))) {
				counter++;
			}
		}

		result = counter;
	}

	// getters
	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public float getResult() {
		return result;
	}

	public String toString() {
		String str = " " + this.getName() + " = " + (int) this.getResult();
		return str;
	}
}
