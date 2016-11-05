package metrics;

import java.util.ArrayList;

import UML.Classe;

public class Dit extends Metric {
	private final String name = "DIT";
	private final String description = "DIT : Taille du chemin le plus long reliant la classe couranteà une classe racine dans le graphe d'héritage.";

	private Classe ci;
	private ArrayList<Classe> listeClasse = new ArrayList<Classe>();
	private float result = 0;

	public Dit(Classe ci, ArrayList<Classe> listeClasse) {
		this.ci = ci;
		this.listeClasse = listeClasse;
		calculate();
	}

	@Override
	public void calculate() {
		// TODO Auto-generated method stub
		if (ci.getParent() == null) {
			// si on est a la racine
			return;
		} else {
			result++;
			Classe parent = ci.getParent();

			// si le parent n'as pas encore calcule ses metriques
			if (parent.getMetrics().size() == 0) {
				// appel recursif sur les parents
				parent.calculateMetrics(listeClasse);
			}

			// retrouver la metrique dans un for loop
			for (int i = 0; i < parent.getMetrics().size(); i++) {
				Metric parent_dit = parent.getMetrics().get(i);
				String mtr_name = parent_dit.getName();
				if (mtr_name.equals("DIT")) {
					Float parent_result = parent_dit.getResult();
					result = result + parent_result;
				}
			}
		}
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
		String str = " " + this.getName() + " = " + +(int) this.getResult();
		return str;
	}

}
