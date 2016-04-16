import java.util.Random;
public class Entity 
{
	public final static int PLAYERTYPE = 1;
	public final static int ENEMYTYPE = 2;
	
	protected int gridx;
	protected int gridy;
	protected int hp;
	protected int attackPower;
	protected int type;
	
	protected int prevX;
	protected int prevY;

	
	public Entity(int gridx, int gridy, int hp, int attackPower, int type)
	{
		this.gridx = gridx;
		this.gridy = gridy+1;
		this.hp = hp;
		this.attackPower = attackPower;
		this.type = type;
		prevX = gridx;
		prevY = gridy;
	}
	
	public int getGridx()
	{
		return gridx;
	}
	
	public int getGridy()
	{
		return gridy;
	}
	
	public int getHp()
	{
		return hp;
	}
	
	public int getAttackPower()
	{
		return attackPower;
	}

	public void setGridx(int gridx)
	{
		this.gridx = gridx;
	}
	
	public void setGridy(int gridy)
	{
		this.gridy = gridy;
	}
	
	public void setHp(int hp)
	{
		this.hp = hp;
	}
	
	public void setAttackPower(int attackPower)
	{
		this.attackPower = attackPower;
	}
	
	public void storePrevPosition(int x, int y)
	{
		prevX = x;
		prevY = y;
	}
	
	public int getPrevXPosition()
	{
		return prevX;
	}
	
	public int getPrevYPosition()
	{
		return prevY;
	}
	
	public void move(int direction)
	{
		System.out.println(gridx + " " + gridy);
		switch(direction)
		{
		case 1:	prevX = gridx;
				prevY = gridy;
				gridy--;
				break;
		case 2: prevX = gridx;
				prevY = gridy;
				gridx++;	
				break;
		case 3:	prevX = gridx;
				prevY = gridy;
				gridy++;
				break;
		case 4:	prevX = gridx;
				prevY = gridy;
				gridx--;
				break;
		}
		System.out.println(gridx + " " + gridy);
	}
	
	public int getBaseAttack()
	{
		Random rnd = new Random();
		
		return rnd.nextInt(10);
	}
	
	public boolean isPlayerType()
	{
		return false;
	}
	
	
}
