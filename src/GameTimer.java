import java.util.TimerTask;

/**
 * Class represents the game timer
 * 
 * @author Aleksejs Loginovs
 */
public class GameTimer extends TimerTask
{
	private Game game;
	/**
	 * Constructor that stores the game object within the class
	 * @param game object of a currently running game
	 */
	public GameTimer(Game game)
	{
		this.game = game;
	}
	
	/**
	 * Method updates game timer in the game field
	 */
	public void run()
	{
		game.updateTimer();
	}
}
