import java.io.File;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class test {
	
	public static void main(String[] args)
	{
		test t = new test();
		String[] list = t.getListOfLoads();
		
		for(String temp:list)
		{
			System.out.println(temp);
		}
	}
	
	public String[] getListOfLoads()
	{
		ArrayList<String> arrayListOfFiles = new ArrayList<String>();
		String[] listOfFiles = new String[0];
		File[] arrayOfFiles = null;
		File folder = new File("savedGames/");
		
		if(folder.exists())
		{
			try
			{
				arrayOfFiles = folder.listFiles();
				
				for(int i = 0; i < arrayOfFiles.length; i++)
				{
					String temp = arrayOfFiles[i].getPath();
					if(temp.substring(temp.length()-4, temp.length()).equals(".map"))
					{
						temp = temp.replace("savedGames\\", "");
						temp = temp.replace(".map", "");
						arrayListOfFiles.add(temp);
					}
				}
				
				listOfFiles = new String[arrayListOfFiles.size()];
				for(int i = 0; i < arrayListOfFiles.size(); i++)
				{
					listOfFiles[i] = arrayListOfFiles.get(i);
				}
				
				return listOfFiles;
			}
			catch(SecurityException e)
			{
				JOptionPane.showMessageDialog(null, "Sorry, couldn't read the file...", "FileReadError", JOptionPane.ERROR_MESSAGE);
			}
			
		}
		
		return listOfFiles;
	}
	
	
}
