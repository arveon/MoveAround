import java.util.TimerTask;

public class GameTimer extends TimerTask
{
	private Game game;
	public GameTimer(Game game)
	{
		this.game = game;
	}
	
	public void run()
	{
		game.updateTimer();
	}
}
