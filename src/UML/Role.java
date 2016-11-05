package UML;


public class Role {

	private Classe c;
	private String m;

	public Role(Classe c) {

		this.c = c;
	}

	public void setMult(String m) {
		this.m = m;
	}

	public Classe getClasse() {

		return this.c;
	}

	public boolean findClasse(Classe cl) {
		return this.c.getName().equals(cl.getName());
	}

	public String getMult() {
		return this.m;
	}

	public String toString() {
		return String.format("CLASS %s %s", this.c.getName(), this.m.toString());
	}
}
