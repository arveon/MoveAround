import java.util.Random;

public class Player extends Entity 
{
	public final static int ATTACKPOWER = 10;

	//private Item[] items;
	private String name;
	private int defence;
	
	public Player(int gridx, int gridy, int hp, String name)//, Item[] items)
	{
		super(gridx, gridy, hp, Player.ATTACKPOWER, Entity.PLAYERTYPE);
		this.name = name;
		defence = 0;
		System.out.println("PLAYERCOORDINATES " + gridx + gridy);
	}
	
	@Override
	public boolean isPlayerType()
	{
		return true;
	}
	
	public void attackEnemy(Game game, Enemy enemy, int direction, Field field)
	{		
		if(Rules.moveCheck(enemy, field, direction))
		{
			enemy.move(direction);
			field.updateEnemyPosition(enemy.getGridx(), enemy.getGridy(), enemy.getPrevXPosition(), enemy.getPrevYPosition());
			
			if(Rules.moveCheck(enemy, field, direction))
			{
				enemy.move(direction);
				field.updateEnemyPosition(enemy.getGridx(), enemy.getGridy(), enemy.getPrevXPosition(), enemy.getPrevYPosition());
			}
		}
		
		game.setField(field);
		
		Random rnd = new Random();
		int damage = rnd.nextInt(10) + attackPower;
		
		enemy.takeDamage(damage);
		takeDamage(enemy.getBaseAttack()-defence);
	}
	
	public void takeDamage(int damage)
	{
		System.out.println("Taken damage: " + (damage-defence));
		hp = hp - (damage-defence);
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
}
