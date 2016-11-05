package metrics;
import java.util.ArrayList;

import UML.Classe;

public class Noc extends Metric {
	private final String name = "NOC";
	private final String description = "NOC : Nombre de sous-classes directes de la classe courante.";

	private Classe ci;
	private float result = 0;

	public Noc(Classe ci) {
		this.ci = ci;
		calculate();
	}

	@Override
	public void calculate() {
		// TODO Auto-generated method stub
		// les sousclasses directes
		ArrayList<Classe> sc = ci.getSubclass();
		int nb_sc = sc.size();
		for (int i = 0; i < nb_sc; i++)
			result++;
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
