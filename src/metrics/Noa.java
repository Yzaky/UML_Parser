package metrics;

import java.util.ArrayList;

import UML.Attribut;
import UML.Classe;

public class Noa extends Metric {
	private final String name = "NOA";
	private final String description = "NOA : Nombre d'attributs locaux/hérités de la classe courante.";

	private Classe ci;
	private int nb_attr = 0;
	private float result;

	public Noa(Classe ci) {
		this.ci = ci;
		calculate();
	}

	@Override
	public void calculate() {
		// TODO Auto-generated method stub
		ArrayList<Attribut> Classe_attributes = ci.getAttributes();

		for (int i = 0; i < Classe_attributes.size(); i++) {
			nb_attr++;
		}

		if (ci.getParent() != null) {
			Classe parent = ci.getParent();
			Classe sub = ci;
			do {
				ArrayList<Attribut> attr_parent = parent.getAttributes();
				ArrayList<Attribut> attr_enfant = sub.getAttributes();
				for (int i = 0; i < attr_parent.size(); i++) {
					String attr_p = attr_parent.get(i).toString();
					Boolean equ = false;

					for (int j = 0; j < attr_enfant.size(); j++) {
						String attr_e = attr_enfant.get(j).toString();
						if (attr_p.equals(attr_e)) {
							equ = true;
						}
					}
					if (!equ)
						nb_attr++;
				}
				/*
				 * on monte dans l'hierarchie des parents et dans celle des
				 * enfants a chaque niveau seuls les attributs differents vont
				 * etre comptees donc, on elimine le SHADOWING qui peut arriver
				 * par erreur
				 */
				sub = parent;
				parent = parent.getParent();
			} while (parent != null);
		}

		result = nb_attr;
	}

	// getters
	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public int getNbAttr() {
		return nb_attr;
	}

	public float getResult() {
		return result;
	}

	public String toString() {
		String str = " " + this.getName() + " = " + (int) this.getResult();
		return str;
	}

}
