import java.util.ArrayList;
import java.util.Collections;

//A group of literals
public class Clause {

	private ArrayList<Literal> literals;

	public Clause() {
		literals = new ArrayList<Literal>();
	}

	public Clause(String line) {
		literals = new ArrayList<Literal>();
		String[] parts = line.split(" ");

		for (int i = 0; i < parts.length; i++) {
			literals.add(new Literal(parts[i]));
		}
	}

	public Literal get(int index)
	{
		return literals.get(index);
	}
	
	public ArrayList<Literal> getLiterals() {
		return literals;
	}

	public void add(Literal lit) {
		literals.add(lit);
	}

	public void addAll(ArrayList<Literal> lit) {
		literals.addAll(lit);
	}

	public int size() {
		return literals.size();
	}

	public boolean isTrue()
	{
		return literals.isEmpty();
	}
	
	public boolean isEmpty() {
		return literals.isEmpty();
	}
	
	public String getSortedString()
	{
		ArrayList<String> arr = new ArrayList<String>();
		for( Literal l : literals)
		{
			arr.add(l.toString());
		}
		
		Collections.sort(arr);
		
		StringBuilder sb = new StringBuilder();
		for( String s : arr)
		{
			sb.append(s);
		}
		
		return sb.toString();
	}

	// Returns a negated version of the entire clause.
	// Useful for negating the cause we want to test.
	public ArrayList<Clause> getNegated() {
		ArrayList<Clause> newClauses = new ArrayList<Clause>();

		for (int i = 0; i < literals.size(); i++) {
			Clause cur = new Clause();
			cur.add(literals.get(i).clone().negate());
			newClauses.add(cur);
		}

		return newClauses;
	}

	// Removes duplicate literals.
	// Used after combining two clauses.
	public void simplify() {
		ArrayList<Literal> newList = new ArrayList<Literal>();

		while (literals.size() > 0) {
			Literal cur = literals.get(0);
			literals.remove(0);

			
			for (int i = 0; i < literals.size(); i++) {
				// if atoms are different, ignore. Can't be combined/simplified
				if (!literals.get(i).sameAtom(cur))
					continue;

				Literal removed = literals.get(i);
				literals.remove(i);
				i--;

				//If there is a negation, the clause resolves to true.
				if(cur.isNegated() != removed.isNegated())
				{
					newList.clear();
					literals = newList;
					return;
				}
					
			}

			newList.add(cur);
		}

		literals = newList;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < literals.size(); i++) {
			sb.append(literals.get(i).toString()).append(" ");
		}

		return sb.toString();
	}
	
	public boolean equals(Clause other)
	{
		//if(other == null || other.size() != literals.size()) return false;
		
		ArrayList<Literal> thisCopy = (ArrayList<Literal>) literals.clone();
		ArrayList<Literal> otherCopy = (ArrayList<Literal>) other.getLiterals().clone();
		
		while(thisCopy.size() > 0)
		{
			Literal cur = thisCopy.get(0);
			thisCopy.remove(0);
			boolean foundAtom = false;
			
			for(int i = 0; i < otherCopy.size(); i++)
			{
				if( !cur.sameAtom(otherCopy.get(i)))
					continue;
				
				foundAtom = true;
				
				//If they have different negations, then they are not the same
				if(cur.isNegated() != otherCopy.get(i).isNegated())
					return false;
				
				//Copy has been found successfully. Remove from list.
				otherCopy.remove(i);
				break;
			}
			
			//if not found atom, then the clauses are not equal.
			if(!foundAtom)
				return false;
		}
		
		//If there are still 
		if(otherCopy.size() > 0)
			return false;
		
		return true;
	}

	// STATIC FUNCTIONS

	public static boolean hasLikeAtom(Clause a, Clause b)
	{
		ArrayList<String> atoms = new ArrayList<String>();
		
		for(int i = 0; i < a.size(); i++)
		{
			atoms.add(a.get(i).getAtom());
		}
		
		for(int i = 0; i < b.size(); i++)
		{
			if(atoms.contains(b.get(i).getAtom()))
				return true;
		}
		
		return false;
	}
	
	public static boolean canApplyResolution(Clause a, Clause b)
	{
		for(int i = 0; i < a.size(); i++)
		{
			for(int j = 0; j < b.size(); j++)
			{
				if(a.get(i).sameAtom(b.get(j)) && a.get(i).isNegated() != b.get(j).isNegated())
					return true;
			}
		}
		
		return false;
	}

	/*
	public static Clause combine(Clause a, Clause b) {
		Clause combined = new Clause();
		combined.addAll(a.getLiterals());
		combined.addAll(b.getLiterals());
		
		if(combined.size() == 2)
		{
			if(combined.get(0).sameAtom(combined.get(1)) && combined.get(0).isNegated() != combined.get(1).isNegated())
				return null;
		}
		
		combined.simplify();
		return combined;
	}
	*/
	
	public static Clause resolveResolution(Clause a, Clause b)
	{
		int aLit = -1;
		int bLit = -1;
		for(int i = 0; i < a.size(); i++)
		{
			for(int j = 0; j < b.size(); j++)
			{
				if(a.get(i).sameAtom(b.get(j)) && a.get(i).isNegated() != b.get(j).isNegated())
				{
					aLit = i;
					bLit = j;
					break;
				}
			}
		}
		
		if(aLit < 0 || bLit < 0)
			return new Clause();
		
		//Found the two we are resolving.
		//Add all but those two literals into one clause, then simplify.
		Clause combined = new Clause();
		ArrayList<Literal> cloneA = (ArrayList<Literal>) a.getLiterals().clone();
		ArrayList<Literal> cloneB = (ArrayList<Literal>) b.getLiterals().clone();
		cloneA.remove(aLit);
		cloneB.remove(bLit);
		combined.addAll(cloneA);
		combined.addAll(cloneB);
		
		//Basically, return a false clause if there is a contradiction
		if(combined.size() == 0)
			return null;
		
		combined.simplify();
		return combined;
	}
}
