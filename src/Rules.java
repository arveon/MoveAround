import java.util.ArrayList;

public class Rules 
{
	
	
	public Rules()
	{
		
	}
	
	public static boolean moveCheck(Entity entity, Field field, int direction)
	{
		int gridx = entity.getGridx();
		int gridy = entity.getGridy();
		boolean moveAllowed = false;
		
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
		
		if(field.getTile(gridx, gridy).equals(Field.EMPTY_SYBMOL) || field.getTile(gridx, gridy).equals(Field.PLAYER_SYMBOL)
				|| (entity.isPlayerType() && field.getTile(gridx, gridy).equals(Field.ENEMY_SYMBOL)))
		{
			System.out.println("Move allowed!");
			moveAllowed = true;
		}
		
		return moveAllowed;
	}
	
	public static boolean checkEnemyAttacking(Enemy enemy, Field field)
	{
		boolean attacking = false;
		int gridx = enemy.getGridx();
		int gridy = enemy.getGridy();
		int direction = enemy.getDirection();
		
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
		
		if(field.getTile(gridx, gridy).equals(Field.PLAYER_SYMBOL))
		{
			attacking = true;
		}
		
		return attacking;
	}
	
	public static Enemy checkPlayerAttacking(ArrayList<Enemy> enemies, Player player, Field field, int direction)
	{
		Enemy attacked = null;
		
		int gridx = player.getGridx();
		int gridy = player.getGridy();
		
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
	
	public static boolean checkGameLost(Player player)
	{
		if(player.getHp() <= 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public static boolean checkGameWon(ArrayList<Enemy> enemies)
	{
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

