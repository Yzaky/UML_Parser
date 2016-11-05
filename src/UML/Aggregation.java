package UML;
import java.util.ArrayList;

public class Aggregation {

	private Role container;
	private ArrayList<Role> parts;

	public Aggregation() {
		this.container = null;
		this.parts = new ArrayList<Role>();
	}

	public boolean findClass(Classe c) {
		return this.container.findClasse(c);
	}

	public void addRole(Role role) {
		this.container = role;

	}

	public void addPart(Role Part) {
		parts.add(Part);
	}

	/* getters and setters */
	public void setContainerM(String M) {
		this.container.setMult(M);
	}

	public String getPartsName() {

		int i;
		String s = "";
		for (i = 0; i < this.parts.size(); i++) {
			s = s + this.parts.get(i).getClasse().getName();

		}
		return s;

	}

	public String getPartsCard() {
		int i;
		String s = "";
		for (i = 0; i < this.parts.size(); i++) {
			s = s + this.parts.get(i).getMult();

		}
		return s;
	}

	public Role getContainer() {
		return this.container;
	}

}
