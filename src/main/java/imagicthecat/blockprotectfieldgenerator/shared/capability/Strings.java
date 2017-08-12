package imagicthecat.blockprotectfieldgenerator.shared.capability;

import java.util.ArrayList;
import java.util.List;

public class Strings implements IStrings {
	
  private List<String> strings;
  
  public Strings()
  {
  	strings = new ArrayList<String>();
  }
  
	@Override
	public void add(String str) 
	{
		strings.add(str);
	}

	@Override
	public void remove(int index) 
	{
		strings.remove(index);
	}

	@Override
	public String get(int index) 
	{
		return strings.get(index);
	}

	@Override
	public int size() 
	{
		return strings.size();
	}

	@Override
	public void clear() 
	{
		strings.clear();
	}
}
