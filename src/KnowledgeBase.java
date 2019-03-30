import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;

public class KnowledgeBase
{
	private ArrayList<Clause> clauses;
	private HashSet<String> clauseHash;
	
	public KnowledgeBase()
	{
		clauses = new ArrayList<Clause>();
		clauseHash = new HashSet<String>();
	}
	
	public void add(Clause c)
	{
		add(c, -1, -1);
	}
	
	public void add(Clause c, int parentA, int parentB)
	{
		clauses.add(c);
		
		if(c != null)
			clauseHash.add(c.getSortedString());
		
		printClause(clauses.size() - 1, parentA, parentB);
	}
	
	public void addAll(ArrayList<Clause> list)
	{
		for(int i = 0; i < list.size(); i++)
		{
			add(list.get(i));
		}
	}
	
	public Clause get(int index)
	{
		return clauses.get(index);
	}
	
	public int size()
	{
		return clauses.size();
	}
	
	//Returns true if the knowledgebase already contains an equivalent clause.
	public boolean contains(Clause c)
	{
		if(c == null) return false;
		return clauseHash.contains(c.getSortedString());
	}
	
	private ArrayList<Integer> getIndexArray()
	{
		ArrayList<Integer> arr = new ArrayList<Integer>();
		for(int i = 0; i < clauses.size(); i++)
		{
			arr.add(i);
		}
		return arr;
	}
	
	public void printClause(int index, int parentA, int parentB)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(index + 1).append(". ");
		
		if(clauses.get(index) != null)
			sb.append( clauses.get(index).toString() );
		else
			sb.append("Contradiction");
		
		sb.append("{");
		
		if(parentA >= 0 && parentB >= 0)
			sb.append((parentA + 1) + "," + (parentB + 1));
		
		sb.append("}");
		
		System.out.println(sb.toString());
	}
}
