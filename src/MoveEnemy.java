import java.util.TimerTask;
import java.util.Random;

/**
 * Class is used to handle the enemy movement the enemy
 * @author Aleksejs Loginovs
 *
 */
public class MoveEnemy extends TimerTask
{
	Enemy enemy;//enemy that is being moved
	Field field;//field on which enemy is being moved
	Game game;
	
	/**
	 * Constructor that stores the enemy that is being moved, game field an game object
	 * @param enemy enemy that is being moved
	 * @param field game field
	 * @param game game object
	 */
	public MoveEnemy(Enemy enemy, Field field, Game game)
	{
		this.enemy = enemy;
		this.field = field;
		this.game = game;
	}
	
	/**
	 * Method that checks if player has remaining steps in current direction and assigns a new direction randomly if not
	 * 
	 */
	@Override
	public void run()
	{
		Random dir = new Random();
		Random rsteps = new Random();
		
		//set the direction if it wasn't set previously
		if((enemy.getStepsLeft() <= 0 && enemy.getStepsLeft()!=10) || enemy.getDirection()==10)
		{
			enemy.setDirection(dir.nextInt(4) + 1);
			enemy.setStepsLeft(rsteps.nextInt(5) + 1);
		}
		
		//if an enemy didn't run out of steps in the current direction, move him
		//otherwise, enemy already made all the steps in the previous direction so assign a new random direction
		if(enemy.getStepsLeft() > 0)
		{
			moveEnemy(enemy, field);
		}
		else
		{
			enemy.setDirection(dir.nextInt(4) + 1);
			enemy.setStepsLeft(rsteps.nextInt(5) + 1);
			moveEnemy(enemy, field);
		}
	}
	
	/**
	 * This method moves the enemy in the direction it's moving in
	 * @param enemy
	 * @param field
	 */
	public void moveEnemy(Enemy enemy, Field field)
	{
		Random dir = new Random();
		Random rsteps = new Random();
		
		enemy.setStepsLeft(enemy.getStepsLeft()-1);
		//if the move is allowed, move the enemy
		if(Rules.moveCheck(enemy, field, enemy.getDirection()))
		{
			//if the enemy is attacking a player, do the attack, otherwise move him
			if(!Rules.checkEnemyAttacking(enemy, field))
			{
				enemy.move(enemy.getDirection());
				field.updateEnemyPosition(enemy.getGridx(), enemy.getGridy(), enemy.getPrevXPosition(), enemy.getPrevYPosition());
			}
			else
			{
				enemy.attackPlayer(game.getPlayer());
			}
			//update display
			game.updateFieldLabelTexts();
		}
		else
		{
			//set new directions for the enemy until the move is allowed
			boolean allowed = Rules.moveCheck(enemy, field, enemy.getDirection());
			while(!allowed)
			{
				enemy.setDirection(dir.nextInt(4) + 1);
				enemy.setStepsLeft(rsteps.nextInt(5) + 1);
				allowed = Rules.moveCheck(enemy, field, enemy.getDirection());
			}
			//move the enemy in the direction set for him or attack the player if it is attacking
			enemy.setStepsLeft(enemy.getStepsLeft()-1);
			if(!Rules.checkEnemyAttacking(enemy, field))
			{
				enemy.move(enemy.getDirection());
				field.updateEnemyPosition(enemy.getGridx(), enemy.getGridy(), enemy.getPrevXPosition(), enemy.getPrevYPosition());
			}
			else
			{
				enemy.attackPlayer(game.getPlayer());
			}
			
			game.updateFieldLabelTexts();
		}
	}
}
