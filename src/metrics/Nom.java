package metrics;

import java.util.ArrayList;

import UML.Classe;
import UML.Operation;

public class Nom extends Metric {
	private final String name = "NOM";
	private final String description = "NOM : Nombre de méthodes locales/héritées de la classe courante. Dans le cas où une méthode est héritée et redéfinie localement, elle ne compte qu'une fois.";

	private Classe ci;
	private int nb_methodes = 0;
	private float result;

	public Nom(Classe ci) {
		this.ci = ci;
		calculate();
	}

	@Override
	void calculate() {
		// TODO Auto-generated method stub
		ArrayList<Operation> op = ci.getOperations();
		for (int i = 0; i < op.size(); i++) {
			nb_methodes++;
		}
		if (ci.getParent() != null) {
			Classe parent = ci.getParent();
			Classe sub = ci;

			do {
				ArrayList<Operation> op_parent = parent.getOperations();
				ArrayList<Operation> op_sub = sub.getOperations();
				for (int i = 0; i < op_parent.size(); i++) {
					String opp_p = op_parent.get(i).toString();
					Boolean equ = false;
					for (int j = 0; j < op_sub.size(); j++) {
						String opp_e = op_sub.get(j).toString();
						if (opp_p.equals(opp_e))
							equ = true;
					}
					if (!equ)
						nb_methodes++;
				}
				sub = parent;
				parent = parent.getParent();
			} while (parent != null);
		}
		result = nb_methodes;
	}

	// getters
	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public int getNbMeth() {
		return nb_methodes;
	}

	public float getResult() {
		return result;
	}

	public String toString() {
		String str = " " + this.getName() + " = " + (int) this.getResult();
		return str;
	}

}
