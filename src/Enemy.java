import java.util.Random;

/**
 * Class represents the enemy object inheriting all the methods from the entity class
 * 
 * @author Aleksejs Loginovs
 *
 */

public class Enemy extends Entity
{
	int direction; //direction in that the enemy is currently moving
	int stepsLeft; //the number of steps until the change of the direction
	final int speed;//speed of the enemy
	
	/**
	 * Enemy constructor
	 * 
	 * @param x horizontal coordinate of the enemy
	 * @param y vertical coordinate of the enemy
	 * @param hp enemy health
	 * @param ap enemy attack power
	 * @param speed enemy speed
	 */
	public Enemy(int x, int y, int hp, int ap, int speed)
	{
		super(x,y,hp,ap, Entity.ENEMYTYPE);
		if(speed < 2001) //if speed is bigger than the limit, set it to max
		{
			this.speed = speed;
		}
		else
		{
			this.speed = 2000;
		}
		direction = 10;
		stepsLeft = 10;
	}
	
	/**
	 * Method is used to resolve the combat initiated by the enemy
	 * (enemy deals damage and takes small amount of damage)
	 * 
	 * @param player object of a player that takes the damage
	 */
	public void attackPlayer(Player player)
	{
		int damage;
		Random rnd = new Random();
		damage = rnd.nextInt(9) + 1 + attackPower;//the amount of damage that an enemy deals
		
		prevX = gridx;
		prevY = gridy;
		
		this.takeDamage(player.getBaseAttack()); //take small amount of damage
		player.takeDamage(damage);//deal damage
	}
	
	/**
	 * Method is used to change enemy health according to the damage it has taken
	 * 
	 * @param damage the damage received from the player
	 */
	public void takeDamage(int damage)
	{
		hp -= damage;
	}
	
	/**
	 * Method returns the direction in which the enemy is currently moving
	 * @return the direction in which the enemy is moving
	 */
	public int getDirection()
	{
		return direction;
	}
	
	/**
	 * Sets the new movement direction for the enemy
	 * 
	 * @param newDirection the new movement direction
	 */
	public void setDirection(int newDirection)
	{
		direction = newDirection;
	}
	
	/**
	 * Sets the number of steps that enemy is going to take until generating the new direction
	 * @param stepsLeft number of steps
	 */
	public void setStepsLeft(int stepsLeft)
	{
		this.stepsLeft = stepsLeft;
	}
	
	/**
	 * Returns the number of steps that enemy is going to take until generating the new direction
	 * @return number of steps
	 */
	public int getStepsLeft()
	{
		return stepsLeft;
	}
	
	/**
	 * Returns the speed of this enemy
	 * 
	 * @return the speed
	 */
	public int getSpeed()
	{
		return speed;
	}
	
}
