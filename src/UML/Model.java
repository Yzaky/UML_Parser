package UML;
import java.util.*;
public class Model {

	private String name;
	private ArrayList<Classe> classes;
	private ArrayList<Association> assoc;
	private ArrayList<Aggregation> aggregations;

	public Model(String name) {

		this.name = name;
		this.classes = new ArrayList<Classe>();
		this.assoc = new ArrayList<Association>();
		this.aggregations = new ArrayList<Aggregation>();
	}

	public ArrayList<Aggregation> getAgg() {
		return this.aggregations;
	}

	public ArrayList<Association> getAss() {
		return this.assoc;
	}

	public String getName() {

		return this.name;

	}

	public ArrayList<Classe> getlistClasses() {

		return classes;
	}
	

	public void setName(String newN) {
		this.name = newN;

	}

	public void addClass(Classe c) {
		this.classes.add(c);
	}

	public void addAssoc(Association a) {
		this.assoc.add(a);

	}

	public void addAggregation(Aggregation aa) {
		this.aggregations.add(aa);
	}

	public Classe findClasse(String name) {
		Classe c = null;
		Iterator<Classe> it = this.classes.iterator();
		while (it.hasNext()) {
			c = it.next();
			if (c.getName().equals(name))
				return c;

		}

		return null;
	}
}
