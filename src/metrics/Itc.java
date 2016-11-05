package metrics;
import java.util.ArrayList;

import UML.Classe;
import UML.Operation;

public class Itc extends Metric {
	private final String name = "ITC";
	private final String description = "ITC : Nombre de fois où d'autres classes du diagramme apparaissent comme types des arguments des méthodesde la classe courante.";
	private Classe ci;
	private ArrayList<Classe> listeClasse = new ArrayList<Classe>();
	private float result;

	public Itc(Classe ci, ArrayList<Classe> listeClasse) {
		this.ci = ci;
		this.listeClasse = listeClasse;
		calculate();
	}

	@Override
	public void calculate() {
		// TODO Auto-generated method stub
		int counter = 0;
		ArrayList<String> params = new ArrayList<String>();

		// on construit une liste de tous les parametres(uniques) des methodes
		// de la classe
		for (int i = 0; i < ci.getOperations().size(); i++) {
			Operation op = ci.getOperations().get(i);
			for (int j = 0; j < op.getParams().size(); j++) {
				String param = op.getParams().get(j);
				if (!params.contains(param)) {
					params.add(param);
				}
			}
		}

		/*
		 * on verifie dans la liste des classes les parametres trouves et on
		 * incremente le compteur
		 */
		for (int i = 0; i < params.size(); i++) {
			for (int j = 0; j < listeClasse.size(); j++) {
				if ((listeClasse.get(j).getName()).equals(params.get(i))) {
					counter++;
				}
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
