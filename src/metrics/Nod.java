package metrics;
import java.util.ArrayList;

import UML.Classe;

public class Nod extends Metric {
	private final String name = "NOD";
	private final String description = "NOD : Nombre de sous-classes directes et indirectes de la classe courante.";
	private Classe ci;
	private ArrayList<Classe> listeClasse = new ArrayList<Classe>();
	private float result = 0;

	public Nod(Classe ci, ArrayList<Classe> listeClasse) {
		this.ci = ci;
		this.listeClasse = listeClasse;
		calculate();
	}

	@Override
	public void calculate() {
		// TODO Auto-generated method stub
		// les sousclasses directes
		ArrayList<Classe> enfants = ci.getSubclass();
		int nb_sc = enfants.size();

		if (nb_sc == 0) {
			// si on est une feuille
			return;
		} else {
			// sinon, on descend dans l'arbre
			for (int i = 0; i < nb_sc; i++) {
				result++;
				Classe enfant = enfants.get(i);
				if (enfant.getMetrics().size() == 0) {
					enfant.calculateMetrics(listeClasse);
				}

				// retrouver la metrique dans un for loop
				for (int j = 0; j < enfant.getMetrics().size(); j++) {
					Metric enfant_nod = enfant.getMetrics().get(j);
					String mtr_name = enfant_nod.getName();

					// ajouter le resultat de l'enfant
					if (mtr_name.equals("NOD")) {
						result = result + enfant_nod.getResult();
					}
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
		String str = " " + this.getName() + " = " + (int) this.getResult();
		return str;
	}
}
