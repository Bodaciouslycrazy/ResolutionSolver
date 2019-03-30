
public class Literal
{

	private boolean negated = false;
	private String atom;
	private String atomWithNegation;
	
	public Literal(String rawText) {
		
		String trimmed = rawText.trim();
		if(trimmed.charAt(0) == '~')
		{
			negated = true;
			trimmed = trimmed.substring(1);
		}
		
		atom = trimmed;
		
		if(negated)
			atomWithNegation = "~" + atom;
		else
			atomWithNegation = atom;
	}
	
	public Literal(String atm, boolean neg)
	{
		atom = atm;
		negated = neg;
	}
	
	public String getAtom()
	{
		return atom;
	}
	
	public boolean isNegated()
	{
		return negated;
	}
	
	public boolean sameAtom(Literal other)
	{
		return atom.equals(other.getAtom());
	}
	
	public Literal negate()
	{
		negated = !negated;
		return this;
	}
	
	public Literal clone()
	{
		return new Literal(atom, negated);
	}
	
	public String toString()
	{
		return atomWithNegation;
	}
	//STATIC FUNCTIONS
	
	//Allows you to combine two literals into one, logically.
	//If one is negated and the other is not, then it resolves to true, meaning no new Literal needs to be returned.
	//If they are both the same, then simply return one of them to get rid of duplicates.
	public static Literal combineLikeTerms(Literal a, Literal b)
	{
		if( !a.sameAtom(b) )
			System.out.println("ERROR: combining Literals that have different atoms " + a.getAtom() + " and " + b.getAtom() + ".");
		
		if( a.isNegated() != b.isNegated() )
			return null;
		else
			return a;
	}

}
