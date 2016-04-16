import java.util.ArrayList;

/**
 * Method that contains methods to check whether certain actions (such as movement) are within the game rules and allowed
 * 
 * @author Aleksejs Loginovs
 */
public class Rules 
{
	/**
	 * Default constructor
	 */
	public Rules()
	{
		
	}
	
	/**
	 * Method used to check if the move entity is trying to make is within game rules (doesn't cross walls)
	 * @param entity entity that trying to move
	 * @param field field object
	 * @param direction direction in which entity is trying to move
	 * @return true if the move is available, false if the move is unavailable
	 */
	public static boolean moveCheck(Entity entity, Field field, int direction)
	{
		//gets current entity position
		int gridx = entity.getGridx();
		int gridy = entity.getGridy();
		boolean moveAllowed = false;
		
		//changes the position variables appopriately
		switch(direction)
		{		
		case 1:	gridy--;
				break;
		case 2: gridx++;
				break;
		case 3:	gridy++;
				break;
		case 4:	gridx--;
				break;
		}
		
		//checks if the tile that entity is trying to move to is an empty symbol or a player symbol, or if the entity is a player && is attacking
		//if one is true, then move is allowed
		if(field.getTile(gridx, gridy).equals(Field.EMPTY_SYBMOL) || field.getTile(gridx, gridy).equals(Field.PLAYER_SYMBOL)
				|| (entity.isPlayerType() && field.getTile(gridx, gridy).equals(Field.ENEMY_SYMBOL)))
		{
			moveAllowed = true;
		}
		
		return moveAllowed;
	}
	
	/**
	 * Move is used to check if the enemy is attacking the player
	 * @param enemy enemy that is being checked
	 * @param field field object
	 * @return true if attack is being made, false if not
	 */
	public static boolean checkEnemyAttacking(Enemy enemy, Field field)
	{
		boolean attacking = false;
		//gets enemy position
		int gridx = enemy.getGridx();
		int gridy = enemy.getGridy();
		int direction = enemy.getDirection();
		
		//changes the position variable according to the direction
		switch(direction)
		{		
		case 1:	gridy--;
				break;
		case 2: gridx++;
				break;
		case 3:	gridy++;
				break;
		case 4:	gridx--;
				break;
		}
		
		//check if the tile enemy is trying to move to is a player tile
		if(field.getTile(gridx, gridy).equals(Field.PLAYER_SYMBOL))
		{
			attacking = true;
		}
		
		return attacking;
	}
	
	/**
	 * Method used to check if player is attacking the enemy
	 * @param enemies arraylist of enemies that might be attacked
	 * @param player player object
	 * @param field field obect
	 * @param direction direction in which player is moving
	 * @return an enemy object of the enemy that is being attacked, or null if player is not attacking
	 */
	public static Enemy checkPlayerAttacking(ArrayList<Enemy> enemies, Player player, Field field, int direction)
	{
		Enemy attacked = null;
		//gets player position
		int gridx = player.getGridx();
		int gridy = player.getGridy();
		
		//changes the position variable according to the direction
		switch(direction)
		{		
		case 1:	gridy--;
				break;
		case 2: gridx++;
				break;
		case 3:	gridy++;
				break;
		case 4:	gridx--;
				break;
		}
		
		//loops through the arraylist of enemies and if it finds the enemy that has the same coordinates as
		//the ones player is trying to move to, stores that enemy, stops looping and returns it
		for(Enemy temp: enemies)
		{
			if(gridx == temp.getGridx() && gridy == temp.getGridy())
			{
				attacked = temp;
				break;
			}
		}
		return attacked;
	}
	
	
	/**
	 * Method used to check if the game was lost
	 * @param player player object
	 * @return true if the game was lost, false if the game wasn't lost
	 */
	public static boolean checkGameLost(Player player)
	{
		//if player has no health, the game was lost
		if(player.getHp() <= 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Method used to check if the game was won
	 * @param player player object
	 * @return true if the game was won, false if the game wasn't won
	 */
	public static boolean checkGameWon(ArrayList<Enemy> enemies)
	{
		//if there are no enemies left, game was won
		if(enemies.size() == 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}

