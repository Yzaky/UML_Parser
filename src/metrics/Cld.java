package metrics;
import java.util.ArrayList;

import UML.Classe;

public class Cld extends Metric {
	private final String name = "CLD";
	private final String description = "CLD : Taille du chemin le plus long reliant la classe courante à une classe feuille dans le graphe d'héritage.";
	private Classe ci;
	private ArrayList<Classe> listeClasse = new ArrayList<Classe>();
	private float result = 0;

	public Cld(Classe ci, ArrayList<Classe> listeClasse) {
		this.ci = ci;
		this.listeClasse = listeClasse;
		calculate();
	}

	@Override
	public void calculate() {
		// TODO Auto-generated method stub
		ArrayList<Classe> enfants = ci.getSubclass();

		if (enfants.size() == 0) {
			// si on est une feuille
			return;
		} else {
			// sinon, on augmente le niveau
			result++;

			// variable pour la profondeur maximale sur le niveau d'arbre
			float max_profondeur = 0;

			for (int i = 0; i < enfants.size(); i++) {
				Classe enfant = enfants.get(i);
				if (enfant.getMetrics().size() == 0) {
					enfant.calculateMetrics(listeClasse);
				}

				// retrouver la metrique dans un for loop
				for (int j = 0; j < enfant.getMetrics().size(); j++) {
					Metric enfant_cld = enfant.getMetrics().get(j);
					String mtr_name = enfant_cld.getName();

					if (mtr_name.equals("CLD")) {
						float profondeur_enfant = enfant_cld.getResult();
						// System.out.println("\t profondeur enfant : " +
						// enfant.getName() + " " + profondeur_enfant);
						if (profondeur_enfant > max_profondeur)
							max_profondeur = profondeur_enfant;
					}
				}
			}
			// on ajoute la profondeur maxiale sur le niveau au resultat
			result = result + max_profondeur;
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
