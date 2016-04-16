import java.util.Random;

/**
 * Class represents the player entity and handles the player actions
 * 
 * @author Aleksejs Loginovs
 */
public class Player extends Entity 
{
	public final static int ATTACKPOWER = 10; //for player it is always default
	
	private String name;
	private int defence;
	
	/**
	 * Constructor that initialises player position on the map, health and name
	 * @param gridx player position on x axis
	 * @param gridy player position on y axis
	 * @param hp player health
	 * @param name player name
	 */
	public Player(int gridx, int gridy, int hp, String name)
	{
		super(gridx, gridy, hp, Player.ATTACKPOWER, Entity.PLAYERTYPE);
		this.name = name;
		defence = 0;
	}
	
	/**
	 * Method is used to process player attacks
	 * @param game game object
	 * @param enemy enemy that is being attacked
	 * @param direction direction from which the attack came
	 * @param field field object
	 */
	public void attackEnemy(Game game, Enemy enemy, int direction, Field field)
	{		
		//if enemy can be pushed back, puch him back
		if(Rules.moveCheck(enemy, field, direction))
		{
			enemy.move(direction);
			field.updateEnemyPosition(enemy.getGridx(), enemy.getGridy(), enemy.getPrevXPosition(), enemy.getPrevYPosition());
			
			//if enemy can be pushed back again, push him back
			if(Rules.moveCheck(enemy, field, direction))
			{
				enemy.move(direction);
				field.updateEnemyPosition(enemy.getGridx(), enemy.getGridy(), enemy.getPrevXPosition(), enemy.getPrevYPosition());
			}
		}
		//update the field in the game thread
		game.setField(field);
		
		//generate damage dealt to the enemy
		Random rnd = new Random();
		int damage = rnd.nextInt(10) + attackPower;
		
		//deal damage
		enemy.takeDamage(damage);
		//take small amount of damage
		takeDamage(enemy.getBaseAttack()-defence);
	}
	
	/**
	 * Method is used to take damage from enemies
	 * @param damage damage being dealt by the enemy
	 */
	public void takeDamage(int damage)
	{
		//takes damaga subtracting player defence from the damage dealt by the enemy
		hp = hp - (damage-defence);
	}
	
	/**
	 * Method used to get player's name
	 * @return player's name
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Method used to set player's name
	 * @param name new name for the player
	 */
	public void setName(String name)
	{
		this.name = name;
	}
}
