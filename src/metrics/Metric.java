package metrics;

public abstract class Metric {
	private String name;
	private String description;
	private float result;
	abstract void calculate();
	
	public String getName()
	{
		return name;
		
	}
	
	public float getResult()
	{
		return result;
		
	}
	
	public String getDescription()
	{
		return description;
	}
}
