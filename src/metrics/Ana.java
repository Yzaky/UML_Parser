package metrics;
import UML.Classe;
import UML.Operation;

public class Ana extends Metric {

	private final String name = "ANA";
	private final String description = "ANA : Nombre moyen d'arguments des methodes locales pour la classe courrante.";
	private Classe ci;
	private int nb_args = 0;
	private int nb_methodes = 0;
	private float result;

	public Ana(Classe ci) {
		this.ci = ci;
		calculate();
	}

	@Override
	public void calculate() {

		for (int i = 0; i < ci.getOperations().size(); i++) {
			nb_methodes++;
			Operation op = ci.getOperations().get(i);
			for (int j = 0; j < op.getParams().size(); j++) {
				nb_args++;
			}

		}
		if (nb_methodes != 0) {

			result = (float) nb_args / nb_methodes;
		} else {
			result = 0;
		}
	}

	// getters
	public String getName() {
		return name;

	}

	public String getDescription() {
		return description;

	}

	public int getNumArgs() {
		return nb_args;

	}

	public int getNumMeth() {
		return nb_methodes;

	}

	public float getResult() {
		return result;
	}

	public String toString() {
		String str = " " + this.getName() + " = " + String.format("%.2f", this.getResult());
		return str;
	}
}
