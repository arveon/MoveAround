import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

import javax.swing.JOptionPane;

import java.util.ArrayList;

/**
 * Class is used to handle the game field (load, set up, update)
 * @author Aleksejs Loginovs
 *
 */
public class Field
{
	//symbols that are used to represent player, enemies, walls and empty tiles
	public final static String PLAYER_SYMBOL = "1";
	public final static String ENEMY_SYMBOL = "£";
	public final static String WALL_SYMBOL = "/";
	public final static String EMPTY_SYBMOL = "0";
	ArrayList<ArrayList<String>> field;//the field
	private Game game;//current game class
	
//	private boolean isLoaded;
//	
//	/**
//	 * The default field constructor
//	 * 
//	 */
//	public Field()
//	{
//		isLoaded = false;
//	}
	
	/**
	 * The field cunstructor that is used to initialise values
	 * 
	 * @param filepath path to the text file that is being loaded
	 * @param game object of a game that is currently running
	 */
	public Field(String filepath, Game game)
	{

		this.game = game;
		field = new ArrayList<ArrayList<String>>();
		
		initialiseField(filepath);
	}
	
	/**
	 * Returns the game field stored in this object
	 * @return a game field
	 */
	public ArrayList<ArrayList<String>> getField()
	{
		return field;
	}
	
	/**
	 * Returns the tile stored in the received coordinates
	 * @param x tile x coordinate
	 * @param y tile y coordinate
	 * @return tile that is stored under received coordinates
	 */
	public String getTile(int x, int y)
	{
		return field.get(y).get(x);
	}
	
	/**
	 * Set tile under received coordinates to the received value
	 * 
	 * @param x tile x coordinate
	 * @param y tile y coordinate
	 * @param replacement new string representation of the tile that will be replaced
	 */
	public void setTile(int x, int y, String replacement)
	{
		field.get(y).set(x, replacement);
	}
	
	/**
	 * The method loads the player, enemy and timer data from the file,
	 * creates enemy and player objects and reads and stores the field
	 * 
	 * @param filepath path to the field file
	 */
	public void initialiseField(String filepath)
	{
		FileReader stream;
		BufferedReader read;
		
		if(filepath.equals(null))
		{
			JOptionPane.showMessageDialog(null, "You are trying to access a non-existing file.", "Non-existing file", JOptionPane.ERROR_MESSAGE);
			game.closeGameField();
			return;
		}
		
		try
		{
			//prepare to read the file
			String line;
			stream = new FileReader(filepath);
			read = new BufferedReader(stream);
			
			//start reading
			line = read.readLine();
			
			//if the file is empty, throw an error and stop loading
			if(line == null)
			{
				JOptionPane.showMessageDialog(null, "The file is empty, can't load the game.", "Error - corrupted file", JOptionPane.ERROR_MESSAGE);
				read.close();
				return;
			}
			
			line = line.trim();
			
			//skip blank lines
			int counter = 0;
			while(line.length() < 1 && counter < 20)
			{
				line = read.readLine();
				counter++;
			}
			
			//if timeGiven is provided, store it
			if(line.substring(0,4).equals("Time"))
			{
				//get the second half of the line and store the value
				String temp[] = line.split(": ");
				game.setTimer(Integer.parseInt(temp[1].trim()));			
				line = read.readLine();
				line = line.trim();
			}
			else
			{
				game.setTimer(Game.DEFAULT_TIMER);//set default timer if the time is not specified
			}
			
			//skip blank lines
			counter = 0;
			while(line.length() < 1 && counter < 20)
			{
				line = read.readLine();
				counter++;
			}
			
			//read player details if there are such, else sets player coordinates to their default values
			if(line.substring(0,6).equals("Player"))
			{
				String temp[] = line.split(": ");
				String[] playerDetails = temp[1].split(",");
				game.setPlayer(new Player(Integer.parseInt(playerDetails[0].trim()), Integer.parseInt(playerDetails[1].trim()), Integer.parseInt(playerDetails[2].trim()), playerDetails[3].trim()));
				line = read.readLine();
				line = line.trim();
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
				//read the field and store it in an arraylist of arraylists of strings
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
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Save file corrupt and doesn't contain a field", "Error - corrupted file", JOptionPane.ERROR_MESSAGE);
				read.close();
				return;
			}
			read.close();
		}
		//ERROR HANDLING
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
	
	/**
	 * Method finds the player symbol on the field, sets it to the empty symbol
	 * and sets the player symbol on the given coordinates
	 * 
	 * @param x coordinate where player will be placed
	 * @param y coordinate where player will be placed
	 */
	public void updatePlayerPosition(int x, int y)
	{
		//loops through the field to find the current player position
		//and remove the player symbol from this tile
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
		//puts player on the new position
		setTile(x, y, Field.PLAYER_SYMBOL);
	}
	
	/**
	 * Method updates the enemy position removing the enemy symbol from it's previous position
	 * and adding enemy symbol to the new one
	 * 
	 * @param x where to place enemy on x axis
	 * @param y where to place enemy on y axis
	 * @param prevX where to remove enemy from x axis
	 * @param prevY where to remove enemy from y axis
	 */
	public void updateEnemyPosition(int x, int y, int prevX, int prevY)
	{
		ArrayList<String> temp = field.get(prevY);
		if(!temp.get(prevX).equals(Field.PLAYER_SYMBOL) || !temp.get(prevX).equals(Field.WALL_SYMBOL))
		{
			temp.set(prevX, Field.EMPTY_SYBMOL);
		}
		field.get(y).set(x, Field.ENEMY_SYMBOL);
	}
	
	/**
	 * Method is used to display the field in console 	
	 * NOT USED IN THE FINAL VERSION! ONLY FOR TESTING
	 */
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
}