package UML;

public class Attribut {

	private String name, type;

	public Attribut(String name, String Type) {

		this.name = name;
		this.type = Type;

	}

	/* getters and setters */
	public String getName() {
		return this.name;
	}

	public void setName(String n) {
		this.name = n;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String t) {
		this.type = t;

	}

	public String toString() {
		return String.format("%s : %s", name, type);
	}
}
