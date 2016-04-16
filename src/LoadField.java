import java.io.File;

import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

/**
 * Class is used to handle the game loading screen 
 * 
 * @author Aleksejs Loginovs
 */
public class LoadField
{
	JList<String> listOfLoads;
	MainMenu mainMenu;
	
	/**
	 * The constructor that stores the main menu frame in one of the class fields
	 * @param mainMenu main menu frame
	 */
	public LoadField(MainMenu mainMenu)
	{
		this.mainMenu = mainMenu;
	}
	
	/**
	 * Method is used to set up the load frame and then get the name of the file selected by user.
	 * Then it creates the game object passing the file path
	 */
	public void loadField()
	{
		//initialises content panel and its contents
		JPanel contentPanel = new JPanel();
		
		//initialises, sets up and fills in the list of available game files
		listOfLoads = new JList<String>(getListOfLoads());
		listOfLoads.setSelectedIndex(0);
		listOfLoads.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane list = new JScrollPane(listOfLoads);
		
		//sets up content panel and adds the list to it
		contentPanel.setLayout(new GridBagLayout());		
		GridBagConstraints constr = new GridBagConstraints();
		constr.gridwidth = 2;
		contentPanel.add(list, constr);
		
		//displays the dialog box with the list and buttons on it
		String[] options = {"Cancel", "Load"}; //button names of the option dialog buttons
		int ifLoad = JOptionPane.showOptionDialog(null, contentPanel, "Load game", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[1]);
		
		//if user chose to load the file, load it
		if(ifLoad == 1)
		{
			String item = listOfLoads.getSelectedValue();
			String filePath = "savedGames/" + item + ".map";
			
			//create new game passing the filename to it
			new Game(mainMenu.getFrame(), filePath);
		}
	}
	
	/**
	 * Method is used to get the array of filepaths to the games that player can load
	 * @return array of filepaths to the games that the player can load
	 */
	public String[] getListOfLoads()
	{
		//initialises the variables necessary to read the files from the folder
		ArrayList<String> arrayListOfFiles = new ArrayList<String>();
		String[] listOfFiles = new String[0];
		File[] arrayOfFiles = null;
		File folder = new File("savedGames/"); //the folder to read files from
		
		//if the folder exists
		if(folder.exists())
		{
			try
			{
				//get all the files in the folder
				arrayOfFiles = folder.listFiles();
				
				//loop through all the filepaths in folder and add the ones with the .map extension
				//to the array of files available for load
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
				
				//transforms the arraylist into an array
				listOfFiles = new String[arrayListOfFiles.size()];
				for(int i = 0; i < arrayListOfFiles.size(); i++)
				{
					listOfFiles[i] = arrayListOfFiles.get(i);
				}
			}
			catch(SecurityException e)
			{
				JOptionPane.showMessageDialog(null, "Sorry, couldn't read the file...", "FileReadError", JOptionPane.ERROR_MESSAGE);
			}
		}
		return listOfFiles;
	}
}
