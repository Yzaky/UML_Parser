package UML;
import java.util.ArrayList;
public class Operation {

	private String name, returnType;
	private ArrayList<String> params;

	public Operation(String name, String Type) {
		this.name = name;
		this.returnType = Type;
		this.params = new ArrayList<String>();

	}

	public void addParametre(String p, String t) {
		String s = t ;
		this.params.add(s);

	}
	/* getters and setters*/
	public String getMethodeName() {

		return this.name;
	}

	public ArrayList<String> getParams() {

		return params;
	}

	public String getType() {

		return this.returnType;
	}

	
	  public String toString(){ 
		  
		  return String.format("%s  (%s): %s", name,params,returnType);
		  
	  }
	 

}
