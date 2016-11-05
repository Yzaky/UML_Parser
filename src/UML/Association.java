package UML;

public class Association {

	private String name;
	private Role a, b;

	public Association(String name) {

		this.name = name;
	}

	public boolean findClasseRA(Classe c) {
		return this.a.findClasse(c);
	}

	public boolean findClasseRB(Classe c) {
		return this.b.findClasse(c);
	}
	/* getters and setters */
	public String getFirstRelationName() {
		return this.a.getClasse().getName();
	}

	public String getSecondRelationName() {
		return this.b.getClasse().getName();
	}

	public String getFirstRelationCard() {
		return this.a.getMult();
	}

	public String getSecondRelationCard() {
		return this.b.getMult();
	}

	public String getName() {
		return this.name;
	}

	public void setFirstRoleCard(String M) {
		this.a.setMult(M);
	}

	public void setSecondRoleCard(String M) {
		this.b.setMult(M);
	}

	public Role getFirstRole() {
		return this.a;
	}

	public Role getSecondRole() {
		return this.b;
	}

	public void setFirstRole(Role a) {
		this.a = a;
	}

	public void setSecondRole(Role b) {
		this.b = b;
	}
	/*
	 * public String toString() { StringBuilder sb = new StringBuilder();
	 * sb.append("RELATIONss ").append(name).append("\n\tROLES\n\t\t")
	 * .append(getFirstRole()).append("\n\t\t").append(getSecondRole())
	 * .append("\n"); return sb.toString(); }
	 */
}
