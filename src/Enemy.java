import java.util.Random;

public class Enemy extends Entity
{
	int direction;
	int stepsLeft;
	final int speed;
	
	public Enemy(int x, int y, int hp, int ap, int speed)
	{
		super(x,y,hp,ap, Entity.ENEMYTYPE);
		direction = 10;
		stepsLeft = 10;
		if(speed < 2001)
		{
			this.speed = speed;
		}
		else
		{
			this.speed = 0;
		}
		
	}
	
	public void attackPlayer(Player player)
	{
		int damage;
		Random rnd = new Random();
		damage = rnd.nextInt(9) + 1 + attackPower;
		
		prevX = gridx;
		prevY = gridy;
		
		this.takeDamage(player.getBaseAttack());
		player.takeDamage(damage);
	}
	
	public void takeDamage(int damage)
	{
		hp -= damage;
	}
	
	public int getDirection()
	{
		return direction;
	}
	
	public void setDirection(int newDirection)
	{
		direction = newDirection;
	}
	
	public void setStepsLeft(int stepsLeft)
	{
		this.stepsLeft = stepsLeft;
	}
	
	public int getStepsLeft()
	{
		return stepsLeft;
	}
	
	public int getSpeed()
	{
		return speed;
	}
	
}
