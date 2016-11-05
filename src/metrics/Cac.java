package metrics;
import java.util.ArrayList;

import UML.Aggregation;
import UML.Association;
import UML.Classe;

public class Cac extends Metric {

	private final String name = "CAC";
	private final String description = "CAC : Nombre d'associations(incluant les agregations) locales/herites auxquelles participe la classe courante";
	private Classe ci;
	private float result = 0;

	public Cac(Classe ci, ArrayList<Classe> listeClasse) {
		this.ci = ci;
		calculate();
	}

	@Override
	public void calculate() {
		// TODO Auto-generated method stub

		// traiter les relations
		Classe classe_rel_courrante = ci;
		do {
			ArrayList<Association> rel = classe_rel_courrante.getAssociations();
			int nb_rel = rel.size();
			for (int i = 0; i < nb_rel; i++) {
				result++;
				// les sous-classes de la classe en relation
				if (rel.get(i).getFirstRole().getClasse().getSubclass().size() > 0) {
					ArrayList<Association> rel_s = rel.get(i).getSecondRole().getClasse().getAssociations();
					int nb_rels = rel_s.size();
					for (int j = 0; j < nb_rels; j++) {
						result++;
					}
				}
			}
			// monter dans l'hierarchie si le parent existe
			classe_rel_courrante = classe_rel_courrante.getParent();
		} while (classe_rel_courrante != null);

		// traiter les aggregations
		Classe classe_agg_courrante = ci;
		do {
			ArrayList<Aggregation> agg = classe_agg_courrante.getAggregations();
			int nb_agg = agg.size();
			for (int k = 0; k < nb_agg; k++) {
				result++;
				// les sous-classes de la classe aggregee
				if (agg.get(k).getContainer().getClasse().getSubclass().size() > 0) {
					ArrayList<Association> rel_g = agg.get(k).getContainer().getClasse().getAssociations();
					int nb_relg = rel_g.size();
					for (int m = 0; m < nb_relg; m++) {
						result++;
					}
				}
			}
			// monter dans l'hierarchie si le parent existe
			classe_agg_courrante = classe_agg_courrante.getParent();
		} while (classe_agg_courrante != null);
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
