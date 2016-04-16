import java.util.TimerTask;
import java.util.Random;

public class MoveEnemy extends TimerTask
{
	Enemy enemy;
	Field field;
	Game game;
	
	public MoveEnemy(Enemy enemy, Field field, Game game)
	{
		this.enemy = enemy;
		this.field = field;
		this.game = game;
	}
	
	@Override
	public void run()
	{
		Random dir = new Random();
		Random rsteps = new Random();
		
		if((enemy.getStepsLeft() <= 0 && enemy.getStepsLeft()!=10) || enemy.getDirection()==10)
		{
			enemy.setDirection(dir.nextInt(4) + 1);
			enemy.setStepsLeft(rsteps.nextInt(5) + 1);
		}
		
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
	
	public void moveEnemy(Enemy enemy, Field field)
	{
		Random dir = new Random();
		Random rsteps = new Random();
		enemy.setStepsLeft(enemy.getStepsLeft()-1);
		if(Rules.moveCheck(enemy, field, enemy.getDirection()))
		{
			if(!Rules.checkEnemyAttacking(enemy, field))
			{
				enemy.move(enemy.getDirection());
				field.updateEnemyPosition(enemy.getGridx(), enemy.getGridy(), enemy.getPrevXPosition(), enemy.getPrevYPosition());
			}
			else
			{
				//System.out.println("Attacked the player");
				enemy.attackPlayer(game.getPlayer());
			}
			
			game.updateFieldLabelTexts();
		}
		else
		{
			boolean allowed = Rules.moveCheck(enemy, field, enemy.getDirection());
			while(!allowed)
			{
				enemy.setDirection(dir.nextInt(4) + 1);
				enemy.setStepsLeft(rsteps.nextInt(5) + 1);
				allowed = Rules.moveCheck(enemy, field, enemy.getDirection());
			}
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
