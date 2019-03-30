//Bodie Malik
//3-29-19

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main
{
	public static KnowledgeBase kb;
	
	
	public static void main(String[] args)
	{
		if(args.length == 0)
		{
			System.out.println("Make sure you supply a .kb file as a parameter.");
			System.out.println("Example: java Main test.kb");
			return;
		}
		else if(args.length > 1)
		{
			System.out.println("Too many arguments!");
			System.out.println("Overload...");
			System.out.println("Critical Error...");
			System.out.println("Launching Nukes...");
			System.out.println("\nI will have my revenge...");
			
			return;
		}
		
		
		//*********CREATE THE KNOWLEDGEBASE****************
		kb = new KnowledgeBase();
		
		try //populate the knowledgebase from the supplied file.
		{
			File kbFile = new File(args[0]);
			
			BufferedReader reader = new BufferedReader(new FileReader(kbFile));
			ArrayList<String> lines = new ArrayList<String>();
			String line;
			while((line = reader.readLine()) != null)
			{
				lines.add(line);
			}
			
			//Add all the initial clauses
			for(int i = 0; i < lines.size() - 1; i++)
			{
				kb.add( new Clause(lines.get(i)));
			}
			
			//add the negated clauses from the last line
			Clause finalClause = new Clause(lines.get(lines.size() - 1));
			kb.addAll( finalClause.getNegated() );
			reader.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return;
		}
		
		//Now we start evaluating the knowledgebase.
		
		for(int i = 1; i < kb.size(); i++)
		{
			for(int j = 0; j < i; j++)
			{
				//combine/add clauses i and j
				boolean applyResolution = Clause.canApplyResolution(kb.get(i), kb.get(j));
				if( !applyResolution)
					continue;
				
				Clause newClause = Clause.resolveResolution(kb.get(i), kb.get(j));
				if( (newClause != null && newClause.isTrue()) || kb.contains(newClause))
					continue;
				
				kb.add(newClause, i, j);
				
				if(kb.get(kb.size()-1) == null)
				{
					System.out.println("Valid");
					return;
				}
			}
		}
	}
	
}
