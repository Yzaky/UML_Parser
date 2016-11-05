package UML;

import java.util.ArrayList;
import java.util.Iterator;

import metrics.Ana;
import metrics.Cac;
import metrics.Cld;
import metrics.Dit;
import metrics.Etc;
import metrics.Itc;
import metrics.Metric;
import metrics.Noa;
import metrics.Noc;
import metrics.Nod;
import metrics.Nom;

public class Classe {

	private String name;
	private Classe Parent;
	private ArrayList<Attribut> attributes;
	private ArrayList<Operation> operations;
	private ArrayList<Classe> subclasses;
	private ArrayList<Metric> metrics = new ArrayList<Metric>();
	private ArrayList<Association> assoiations = new ArrayList<Association>();
	private ArrayList<Aggregation> aggregations = new ArrayList<Aggregation>();

	public Classe(String name) {

		this.name = name;
		this.attributes = new ArrayList<Attribut>();
		this.operations = new ArrayList<Operation>();
		this.subclasses = new ArrayList<Classe>();
		this.metrics = new ArrayList<Metric>();
		this.Parent = null;
	}

	/* getters and setters */
	public String getName() {
		return this.name;

	}

	public void setName(String n) {

		this.name = n;
	}

	public boolean hasParent() {
		return this.Parent != null;
	}

	public ArrayList<Association> getAssociations() {
		return assoiations;
	}

	public ArrayList<Aggregation> getAggregations() {
		return aggregations;
	}

	public Classe getParent() {
		return this.Parent;
	}

	public void setParent(Classe c) {
		this.Parent = c;

	}

	public ArrayList<Attribut> getAttributes() {

		return attributes;
	}

	public void addAttribut(Attribut a) {

		this.attributes.add(a);
	}

	public void addOperation(Operation o) {
		operations.add(o);
	}

	public ArrayList<Operation> getOperations() {

		return operations;
	}

	public void addSubclass(Classe c) {

		this.subclasses.add(c);
		c.setParent(this);
	}

	public ArrayList<Classe> getSubclass() {

		return subclasses;
	}

	public ArrayList<Metric> getMetrics() {
		return metrics;
	}

	/* fonctions specifiques pour le calcul de chaque metrique */
	public void calculateAna() {
		Metric ana = new Ana(this);
		metrics.add(ana);
	}

	public void calculateNom() {
		Metric nom = new Nom(this);
		metrics.add(nom);
	}

	public void calculateNoa() {
		Metric noa = new Noa(this);
		metrics.add(noa);
	}

	public void calculateItc(Classe ci, ArrayList<Classe> listeClasse) {
		Metric itc = new Itc(ci, listeClasse);
		metrics.add(itc);
	}

	public void calculateEtc(Classe ci, ArrayList<Classe> listeClasse) {
		Metric etc = new Etc(ci, listeClasse);
		metrics.add(etc);
	}

	public void calculateCac(Classe ci, ArrayList<Classe> listeClasse) {
		Metric cac = new Cac(ci, listeClasse);
		metrics.add(cac);
	}

	public void calculateDit(Classe ci, ArrayList<Classe> listeClasse) {
		Metric dit = new Dit(ci, listeClasse);
		metrics.add(dit);
	}

	public void calculateCld(Classe ci, ArrayList<Classe> listeClasse) {
		Metric cld = new Cld(ci, listeClasse);
		metrics.add(cld);
	}

	public void calculateNoc() {
		Metric noc = new Noc(this);
		metrics.add(noc);
	}

	public void calculateNod(Classe ci, ArrayList<Classe> listeClasse) {
		Metric nod = new Nod(ci, listeClasse);
		metrics.add(nod);
	}

	// fonction generale appellee par DiagramWorkFrame
	public void calculateMetrics(ArrayList<Classe> listeClasse) {
		calculateAna();
		calculateNom();
		calculateNoa();
		calculateItc(this, listeClasse);
		calculateEtc(this, listeClasse);
		calculateCac(this, listeClasse);
		calculateDit(this, listeClasse);
		calculateCld(this, listeClasse);
		calculateNoc();
		calculateNod(this, listeClasse);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("CLASS %s\n", name));
		sb.append("\tATTRIBUTES\n");
		Iterator<Attribut> iterAttr = attributes.iterator();
		while (iterAttr.hasNext()) {
			sb.append(String.format("\t\t%s\n", iterAttr.next()));
		}
		sb.append("\tOPERATIONS\n");
		Iterator<Operation> iterOp = operations.iterator();
		while (iterOp.hasNext()) {
			sb.append(String.format("\t\t%s\n", iterOp.next()));
		}

		return sb.toString();
	}

}
