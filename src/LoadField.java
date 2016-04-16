import java.io.File;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;


import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LoadField implements ActionListener
{
	JFrame loadFrame;
	JButton load;
	JButton cancel;
	JList<String> listOfLoads;
	MainMenu mainMenu;
	
	public LoadField(MainMenu mainMenu)
	{
		this.mainMenu = mainMenu;
	}
	
	public void loadField()
	{
		loadFrame = new JFrame();
		JPanel contentPanel = new JPanel();
		load = new JButton("Load");
		load.addActionListener(this);
		cancel = new JButton("Cancel");
		
		listOfLoads = new JList<String>(getListOfLoads());
		listOfLoads.setSelectedIndex(0);
		listOfLoads.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane list = new JScrollPane(listOfLoads);
		
		contentPanel.setLayout(new GridBagLayout());
		
		GridBagConstraints constr = new GridBagConstraints();
		constr.gridwidth = 2;
		contentPanel.add(list, constr);
		
		
		String[] options = {"Cancel", "Load"};
		int ifLoad = JOptionPane.showOptionDialog(null, contentPanel, "Load game", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[1]);
		
		if(ifLoad == 1)
		{
			String item = listOfLoads.getSelectedValue();
			String filePath = "savedGames/" + item + ".map";
			loadFrame.dispose();
			Game game = new Game(mainMenu.getFrame(), filePath);
			mainMenu.setGame(game);
		}
		else 
		{
			loadFrame.dispose();
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
	
	public void actionPerformed(ActionEvent event)
	{
		if(event.getSource() == load)
		{
			String item = listOfLoads.getSelectedValue();
			String filePath = "savedGames/" + item + ".map";
			loadFrame.dispose();
			Game game = new Game(mainMenu.getFrame(), filePath);
			game.enemyMovement();
			mainMenu.setGame(game);
		}
	}
	
}
