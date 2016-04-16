import java.util.Random;

/**
 * Class represents all living entities that are on the field
 * 
 * @author Aleksejs Loginovs	
 *
 */
public class Entity 
{
	public final static int PLAYERTYPE = 1;//constant that stores the default player type value
	public final static int ENEMYTYPE = 2;//constant that stores the default enemy type value
	
	//entity coordinates
	protected int gridx;
	protected int gridy;
	//entity health, attackPower and type (enemy/player)
	protected int hp;
	protected int attackPower;
	protected int type;
	//previous entity position
	protected int prevX;
	protected int prevY;

	/**
	 * Entity constructor sets the loaded values
	 * 
	 * @param x horizontal coordinate of the entity
	 * @param y vertical coordinate of the entity
	 * @param hp entity health
	 * @param ap entity attack power
	 * @param type entity type
	 */
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
	
	/**
	 * Method returns x coordinate of the entity
	 * @return x coordinate
	 */
	public int getGridx()
	{
		return gridx;
	}
	
	/**
	 * Method returns y coordinate of the entity
	 * @return y coordinate
	 */
	public int getGridy()
	{
		return gridy;
	}
	
	/**
	 * Returns entity remaining health
	 * 
	 * @return health points
	 */
	public int getHp()
	{
		return hp;
	}
	
	/**
	 * Returns entity attack power
	 * 
	 * @return attack power
	 */
	public int getAttackPower()
	{
		return attackPower;
	}

	/**
	 * Method sets x coordinate of the entity
	 * @param gridx x coordinate
	 */
	public void setGridx(int gridx)
	{
		this.gridx = gridx;
	}
	
	/**
	 * Method sets y coordinate of the entity
	 * @param gridx y coordinate
	 */
	public void setGridy(int gridy)
	{
		this.gridy = gridy;
	}
	
	/**
	 * Method sets entity health to the one received
	 * 
	 * @param hp new health value
	 */
	public void setHp(int hp)
	{
		this.hp = hp;
	}
	
	/**
	 * Method sets entity attack power to the one received
	 *  
	 * @param attackPower new attack power value
	 */
	public void setAttackPower(int attackPower)
	{
		this.attackPower = attackPower;
	}
	
	/**
	 * Method used to store px and y coordinates received as previous entity position coordinates
	 * 
	 * @param x previous position x coordinate
	 * @param y previous position y coordinate
	 */
	public void storePrevPosition(int x, int y)
	{
		prevX = x;
		prevY = y;
	}
	
	/**
	 * Method used to get previous position x coordinate
	 * @return previous position x coordinate
	 */
	public int getPrevXPosition()
	{
		return prevX;
	}

	/**
	 * Method used to get previous position y coordinate
	 * @return previous position y coordinate
	 */
	public int getPrevYPosition()
	{
		return prevY;
	}
	
	/**
	 * Method is used to move the entity in the appropriate direction
	 * 
	 * @param direction direction in that entity is being moved
	 */
	public void move(int direction)
	{
		System.out.println(gridx + " " + gridy);//prints the current position coordinates in the console (testing)
		switch(direction)//updates the position values depending on the move saving previous position
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
		System.out.println(gridx + " " + gridy);//prints the new position coordinates in the console
	}
	
	/**
	 * Method is used to get base attack of the entity usually, to get the damage received while attacking
	 * @return damage 
	 */
	public int getBaseAttack()
	{
		Random rnd = new Random();
		
		return rnd.nextInt(10);
	}
	
	/**
	 * Method is used to check if the entity is a player or not
	 * @return false if the entity is not a player, true if it is a player
	 */
	public boolean isPlayerType()
	{
		boolean result = false;
		if(type == Entity.PLAYERTYPE)
		{
			result = true;
		}
		else if(type == Entity.ENEMYTYPE)
		{
			result = false;
		}
		return result;
	}
}
