import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

import javax.swing.JOptionPane;

import java.util.ArrayList;

public class Field
{
	public final static String PLAYER_SYMBOL = "1";
	public final static String ENEMY_SYMBOL = "£";
	public final static String WALL_SYMBOL = "/";
	public final static String EMPTY_SYBMOL = "0";
	ArrayList<ArrayList<String>> field;
	private Game game;
	
	private boolean isLoaded;
	
	public Field()
	{
		isLoaded = false;
	}
	
	public Field(String filepath, Game game)
	{
		isLoaded = false;
		this.game = game;
		field = new ArrayList<ArrayList<String>>();
		
		initialiseField(filepath);
	}
	
	public ArrayList<ArrayList<String>> getField()
	{
		return field;
	}
	
	public String getTile(int x, int y)
	{
		return field.get(y).get(x);
	}
	
	public void setTile(int x, int y, String replacement)
	{
		field.get(y).set(x, replacement);
	}
	
	public void initialiseField(String filepath)
	{
		FileReader stream;
		BufferedReader read;
		
		if(filepath.equals(null))
		{
			JOptionPane.showMessageDialog(null, "The file is corrupted, can't load the game.", "Corrupted file", JOptionPane.ERROR_MESSAGE);
			game.closeGameField();
		}
		
		try
		{
			String line;
			stream = new FileReader(filepath);
			read = new BufferedReader(stream);
			
			//skip blank lines
			line = read.readLine();
			line = line.trim();
			int counter = 0;
			
			//if the file is empty, don't load anything
			if(line == null)
			{
				JOptionPane.showMessageDialog(null, "The file is empty, can't load the game.", "Error - corrupted file", JOptionPane.ERROR_MESSAGE);
				read.close();
				return;
			}
			
			while(line.length() < 1 && counter < 20)
			{
				line = read.readLine();
				counter++;
			}
			
			//if timeGiven is provided, store it
			if(line.substring(0,4).equals("Time"))
			{
				String temp[] = line.split(": ");
				game.setTimer(Integer.parseInt(temp[1].trim()));			
				line = read.readLine();
				line = line.trim();
			}
			else
			{
				game.setTimer(Game.DEFAULT_TIMER);
			}
			
			//skip blank lines
			counter = 0;
			while(line.length() < 1 && counter < 20)
			{
				line = read.readLine();
				counter++;
			}
			
			//read player stuff if there is such
			if(line.substring(0,6).equals("Player"))
			{
				String temp[] = line.split(": ");
				if(temp[0].equals("Player"))
				{
					String[] playerDetails = temp[1].split(",");
					game.setPlayer(new Player(Integer.parseInt(playerDetails[0].trim()), Integer.parseInt(playerDetails[1].trim()), Integer.parseInt(playerDetails[2].trim()), playerDetails[3].trim()));
					line = read.readLine();
					line = line.trim();
				}
			}
			else
			{
				game.setPlayer(new Player(1,1,25,"DEFAULT"));
			}
			
			//skip blank lines
			counter = 0;
			while(line.length() < 1 && counter < 20)
			{
				line = read.readLine();
				counter++;
			}
			
			//read enemy settings and create enemy objects
			if(line.substring(0,5).equals("Enemy"))
			{
				String temp[] = line.split(": ");
				ArrayList<Enemy> enemies = new ArrayList<Enemy>();
				//store next line as enemy object if it has information regarding the enemy
				while(line.substring(0,5).equals("Enemy"))
				{
					temp = line.split(": ");
					String[] enemyDetails = temp[1].split(",");
					enemies.add(new Enemy(Integer.parseInt(enemyDetails[0].trim()),Integer.parseInt(enemyDetails[1].trim()),Integer.parseInt(enemyDetails[2].trim()),10, Integer.parseInt(enemyDetails[3].trim())));
					line = read.readLine();
					line = line.trim();
					//for testing
					System.out.println("EnemyCreated " + enemyDetails[0] + enemyDetails[1]);
					//just to assure there's no exception (throws StringOutOfBoundsException if its a blank line while attempting to substring it)
					if(line.length()<1)
					{
						break;
					}
				}
				game.setEnemies(enemies);
			}
			
			//skip blank lines
			counter = 0;
			while(line.length() < 1 && counter < 20)
			{
				line = read.readLine();
				counter++;
			}
			
			//read the field if there is such
			if(line.substring(0,1).equals("/"))
			{
				while(line != null)
				{
					ArrayList<String> row = new ArrayList<String>();
					for(int i = 0; i < line.length(); i++)
					{
						row.add(line.substring(i, i+1));
					}
					field.add(row);
					line = read.readLine();
				}
				isLoaded = true;
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Save file corrupt and doesn't contain a field", "Error - corrupted file", JOptionPane.ERROR_MESSAGE);
			}
			read.close();
		}
		catch(IOException e)
		{
			System.out.println("Error occured while attempting to read the map file.\n" + e.getStackTrace());
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			JOptionPane.showMessageDialog(null, "The file is corrupted, can't load the game.\n" + e.getStackTrace(), "Error - corrupted file", JOptionPane.ERROR_MESSAGE);
			System.out.println(e.getMessage());
		}
		catch(NumberFormatException e)
		{
			JOptionPane.showMessageDialog(null, "The file is corrupted, can't load the game.\n" + e.getStackTrace(), "Error - corrupted file", JOptionPane.ERROR_MESSAGE);
			System.out.println(e.getMessage());
		}
		catch(StringIndexOutOfBoundsException e)
		{
			JOptionPane.showMessageDialog(null, "The file is corrupted, can't load the game.\n" + e.getStackTrace(), "Error - corrupted file", JOptionPane.ERROR_MESSAGE);
			System.out.println(e.getMessage());
		}
	}
	
	public boolean isLoaded()
	{
		return isLoaded;
	}
	
	public void updatePlayerPosition(int x, int y)
	{
		for(int i = 0; i < field.size(); i++)
		{
			ArrayList<String> temp = field.get(i);
			for(int j = 0; j < temp.size(); j++)
			{
				if(temp.get(j).equals(Field.PLAYER_SYMBOL))
				{
					temp.set(j, Field.EMPTY_SYBMOL);
				}
			}
		}
		
		field.get(y).set(x, Field.PLAYER_SYMBOL);
	}
	
	//TODO fix so that it resets only previous position to 0
	public void updateEnemyPosition(int x, int y, int prevX, int prevY)
	{
		ArrayList<String> temp = field.get(prevY);
		if(!temp.get(prevX).equals(Field.PLAYER_SYMBOL) || !temp.get(prevX).equals(Field.WALL_SYMBOL))
		{
			temp.set(prevX, Field.EMPTY_SYBMOL);
		}
		field.get(y).set(x, Field.ENEMY_SYMBOL);
	}
	
	public void displayField()
	{
		for(int i = 0; i < field.size(); i++)
		{
			ArrayList<String> row = field.get(i);
			for(int j = 0; j < field.get(i).size(); j++)
			{
				System.out.print(row.get(j));
			}
			System.out.println();
		}
	}
	
	
//	public static ArrayList<ArrayList<String>> getDefaultField()
//	{
//		ArrayList<ArrayList<String>> field = new ArrayList<ArrayList<String>>();
//		FileReader stream = null;
//		BufferedReader read = null;
//		String filepath = "savedGames/default/defaultMap.txt";
//		
//		if(filepath.equals(null))
//		{
//			JOptionPane.showMessageDialog(null, "Can't find the default map file. Game is not loaded!", "Corrupted file", JOptionPane.ERROR_MESSAGE);
//			return null;
//		}
//		
//		try
//		{
//			String line;
//			stream = new FileReader(filepath);
//			read = new BufferedReader(stream);
//			
//			line = read.readLine();
//			
//			//skip blank lines if there are such
//			line = read.readLine();
//			int counter = 0;
//			while(line.length() < 1 && counter < 20)
//			{
//				line = read.readLine();
//				counter++;
//			}
//			
//			//read the field if there is such
//			while(line != null)
//			{
//				ArrayList<String> row = new ArrayList<String>();
//				for(int i = 0; i < line.length(); i++)
//				{
//					row.add(line.substring(i, i+1));
//				}
//				field.add(row);
//				line = read.readLine();
//			}
//			read.close();
//			
//		}
//		catch(IOException e)
//		{
//			JOptionPane.showMessageDialog(null, "Can't read the default map file. Game is not loaded!", "Corrupted file", JOptionPane.ERROR_MESSAGE);
//			return null;
//		}
//	}
}
