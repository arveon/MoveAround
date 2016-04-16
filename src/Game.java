import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Dimension;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;

import java.util.ArrayList;
import java.util.Timer;

/**
 * This class contains all methods to handle the game flow and the game frame
 * 
 * @author Aleksejs Loginovs
 *
 */
public class Game implements KeyListener, WindowListener
{
	public static final int DEFAULT_TIMER = 180;//default value of the timer if it's not specified
	
	private JFrame menuFrame;
	private JFrame gameFrame;
	private JPanel gameContent;
	private JLabel health;
	private JPanel gameFieldPanel;
	
	//label field representation and a field object
	private ArrayList<ArrayList<JLabel>> gameField;
	private Field curField;
	
	//variables required to handle entities
	private Player player;
	private ArrayList<Enemy> enemies;
	private ArrayList<JLabel> enemyLabels;
	private ArrayList<Timer> moveEnemy;
	
	//variables required to handle timer
	private Timer gameTimer;
	private int timer;
	private JLabel timerLabel;
	
	//variable that doesn't allow continuous movement while holding the direction button
	boolean keyPressed;
	
	/**
	 * Constructor
	 * 
	 * @param menuFrame main menu frame
	 * @param filepath path to the file of the game that is being loaded
	 */
	public Game(JFrame menuFrame, String filepath)
	{
		//initialises the variables
		timer = 0;
		this.menuFrame = menuFrame;
		menuFrame.setVisible(false);//hides the main menu window
		keyPressed = false;
		curField = new Field(filepath, this);
		
		//if the field loaded successfully and there are enemies on the field, initialise it
		//and load it into game class fields
		//otherwise, go back to the main menu
		if(curField.getField().size() > 0 && enemies != null)
		{
			//place the player on the field
			curField.updatePlayerPosition(player.getGridx(), player.getGridy());
			
			//place enemies on the field
			for(Enemy enemy: enemies)
			{
				curField.updateEnemyPosition(enemy.getGridx(), enemy.getGridy(), enemy.getPrevXPosition(), enemy.getPrevYPosition());
			}
			initialiseFrame();
			updateFieldLabelTexts();
			
			//launch method that will start enemy movement
			enemyMovement();
		}
		else
		{
			menuFrame.setVisible(true);
		}
	}
	
	/**
	 * Method that starts enemy movements and the game timer
	 * 
	 */
	public void enemyMovement()
	{
		//initialise array list of timers for enemy movements
		moveEnemy = new ArrayList<Timer>();
		//fill in the array setting the enemy movement speeds
		for(int i = 0; i < enemies.size(); i++)
		{
			Timer temp = new Timer();
			temp.schedule(new MoveEnemy(enemies.get(i), curField, this), 0, 2001-enemies.get(i).getSpeed());
			moveEnemy.add(temp);
		}
		//initialise game timer and launch it
		gameTimer = new Timer();
		gameTimer.schedule(new GameTimer(this), 0, 1000);
	}
	
	/**
	 * Initialise game frame, putting all the necessary elements on it on their places
	 */
	public void initialiseFrame()
	{
		//initialise the frame
		gameFrame = new JFrame();
		gameFrame.setFocusTraversalKeysEnabled(false);
		gameFrame.addWindowListener(this);
		
		//initialise the panel and set it's properties
		gameContent = new JPanel();
		gameContent.setLayout(new GridBagLayout());
		gameContent.addKeyListener(this);
		gameContent.setFocusable(true);
		gameContent.requestFocusInWindow();
		
		//initialise timer, player health and enemy health labels
		timerLabel = new JLabel();
		timerLabel.setText("Time: " + Integer.toString(timer));
		health = new JLabel();
		enemyLabels = new ArrayList<JLabel>();
		
		placeField();
		
		//fill in enemy arraylist of labels
		for(int i = 0; i < enemies.size(); i++)
		{
			enemyLabels.add(new JLabel());
		}
		
		//add elements to the gameContent panel
		//add gameField to the gameContent panel
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 2;
		gameContent.add(gameFieldPanel, constraints);
		
		//add player health indicator to the gameContent panel
		constraints.gridy = 1;
		gameContent.add(health, constraints);
		
		//add timer indicator to the gameContent panel
		constraints.gridy = 0;
		gameContent.add(timerLabel, constraints);
		
		//add enemy labels to the gameContent panel
		for(int i = 0; i < enemyLabels.size(); i++)
		{
			constraints.gridy = i+3;
			gameContent.add(enemyLabels.get(i), constraints);
		}
		
		//add gameContent panel to the frame
		gameFrame.add(gameContent);
		
		//set frame properties
		gameFrame.setLocationRelativeTo(null);
		gameFrame.pack();
		gameFrame.setSize(new Dimension(400,400));
		gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		gameFrame.setVisible(true);
	}
	
	/**
	 * Initialise all field elements and put them on the gameFieldPanel panel
	 */
	public void placeField()
	{
		gameField = new ArrayList<ArrayList<JLabel>>();
		gameFieldPanel = new JPanel();
		gameFieldPanel.setLayout(new GridBagLayout());
		//add tiles to the field panel
		for(int i = 0; i < curField.getField().size(); i++)
		{
			ArrayList<JLabel> row = new ArrayList<JLabel>();
			for(int j = 0; j < curField.getField().get(i).size(); j++)
			{
				JLabel temp = new JLabel();
				row.add(temp);
				GridBagConstraints tempConst = new GridBagConstraints();
				tempConst.gridx = j;
				tempConst.gridy = i;
				gameFieldPanel.add(temp, tempConst);
			}
			gameField.add(row);
		}
	}
	
	/**
	 * Method updates all texts on the frame
	 */
	public void updateFieldLabelTexts()
	{
		//removes killed enemiy symbols from the field and removes them from the array
		for(int i = 0; i < enemies.size(); i++)
		{
			if(enemies.get(i).getHp() <= 0)
			{
				curField.setTile(enemies.get(i).getGridx(), enemies.get(i).getGridy(), Field.EMPTY_SYBMOL);
				enemies.remove(i);
				moveEnemy.get(i).cancel();
				moveEnemy.remove(i);
				enemyLabels.get(i).setText("Enemy XX  hp: 0");
				enemyLabels.remove(i);
				System.out.println("Enemy has been killed");
			}
		}
		//checks if the game was won, lost or continues
		if(Rules.checkGameLost(player))
		{
			//if lost, updates player health, stops timer and enemy movements and exits to main menu
			//displaying a loser message
			health.setText(player.getName() + ": 0");
			killTimerTasks();
			JOptionPane.showMessageDialog(null, "You have been defeated!", "You have lost.", JOptionPane.PLAIN_MESSAGE);
			closeGameField();
		}
		else if(Rules.checkGameWon(enemies))
		{
			//if the game was won, stops timer and enemy movements and exits to main menu
			//displaying a winner message
			killTimerTasks();
			JOptionPane.showMessageDialog(null, player.getName() + " you have defeated all enemies, congratulations!", "You have won.", JOptionPane.PLAIN_MESSAGE);
			closeGameField();
		}
		else
		{	
			//if game keeps going
			//update player health
			health.setText(player.getName() + ": " + player.getHp());
			//update enemy health indicators
			for(int i = 0; i < enemies.size(); i++)
			{
				enemyLabels.get(i).setText("Enemy " + (i+1) + " hp : " + enemies.get(i).getHp());
			}
			//updates the game field
			for(int i = 0; i < gameField.size(); i++)
			{
				ArrayList<JLabel> temp = gameField.get(i);
				for(int j = 0; j < temp.size(); j++)
				{
					temp.get(j).setText(curField.getTile(j,i));
				}
			}
		}
	}
	
	/**
	 * Sets the timer value to the one received
	 * @param timeRemaining new time remaining until you lose the game
	 */
	public void setTimer(int timeRemaining)
	{
		timer = timeRemaining;
	}
	
	/**
	 * Updates the timer and if the game is lost, tells the player that he lost
	 */
	public void updateTimer()
	{
		//decreases the timer
		timer--;
		//checks if the game was lost
		if(timer < 1)
		{
			//updates timer indicator
			System.out.println("Timer: " + timer);
			timerLabel.setText("Time: " + timer);
			killTimerTasks(); //stops timed tasks (enemy movement and timer)
			JOptionPane.showMessageDialog(null, "You ran out of time. Game lost.", "You have lost.", JOptionPane.PLAIN_MESSAGE);	//displays a losing message		
			closeGameField();//exits to main menu
		}
		else
		{
			//updates the timer indicator
			System.out.println("Timer: " + timer);
			timerLabel.setText("Time: " + timer);
		}
	}
	
	/**
	 * Moves the player if the key was pressed
	 */
	@Override
	public void keyPressed(KeyEvent event)
	{
		boolean movementKey = false;
		int direction = 0;
		//if the key is being held, won't run this again until the button is releaced
		if(keyPressed != true)
		{
			switch(event.getKeyCode())
			{
			case KeyEvent.VK_W: movementKey = true;
								direction = 1;
								break;
			case KeyEvent.VK_D:	movementKey = true;
								direction = 2;
								break;
			case KeyEvent.VK_S:	movementKey = true;
								direction = 3;
								break;
			case KeyEvent.VK_A:	movementKey = true;
								direction = 4;
								break;
			case KeyEvent.VK_ESCAPE:	killTimerTasks();
										closeGameField();
										break;
			}
			
			//if the key that was pressed was a movement key, move the player
			if(movementKey)
			{
				//if move is allowed, move
				if(Rules.moveCheck(player, curField, direction))
				{
					//if player is not attacking the enemy -  move player, otherwise - attack the enemy
					Enemy temp = Rules.checkPlayerAttacking(enemies, player, curField, direction);
					if(temp == null)
					{
						player.move(direction);
					}
					else 
					{
						player.attackEnemy(this, temp, direction, curField);
					}
					//update player position and field texts and count that key was pressed
					curField.updatePlayerPosition(player.getGridx(), player.getGridy());
					updateFieldLabelTexts();
				}
				movementKey = false;
			}
			keyPressed = true;
		}
	}
	
	/**
	 * Method is used to reset the keyPressed value when the key was released
	 */
	public void keyReleased(KeyEvent event)
	{
		keyPressed = false;
	}	
	
	/**
	 * Method is used to close the window
	 */
	public void windowClosing(WindowEvent event)
	{
		System.out.println("WINDOWCLOSING");
		killTimerTasks();
		menuFrame.setVisible(true);
	}
	
	/**
	 * Method used to set player field of this class to received object
	 * @param player new player object
	 */
	public void setPlayer(Player player)
	{
		this.player = player;
	}
	
	/**
	 * Method used to set enemy arraylist field of this class to received arraylist
	 * @param enemyList new enemy arraylist
	 */
	public void setEnemies(ArrayList<Enemy> enemyList)
	{
		this.enemies = enemyList;
	}
	
	/**
	 * Method used to set field object field of this class to received object
	 * @param field new field object
	 */
	public void setField(Field field)
	{
		this.curField = field;
	}
	
	/**
	 * Method returns the game frame
	 * @return game frame
	 */
	public JFrame getFrame()
	{
		return gameFrame;
	}
	
	/**
	 * Returns the current player object
	 * @return player
	 */
	public Player getPlayer()
	{
		return player;
	}
	
	/**
	 * Method used to close the game frame and open the main menu
	 */
	public void closeGameField()
	{
		gameFrame.dispose();
		menuFrame.setVisible(true);;
	}
	
	/**
	 * Method used to stop enemy movements and the game timer
	 */
	public void killTimerTasks()
	{
		for(Timer temp: moveEnemy)
		{
			temp.cancel();
		}
		gameTimer.cancel();
	}
	
	//methods aren't used in the program
	public void keyTyped(KeyEvent event){}	
	public void windowIconified(WindowEvent event){}
	public void windowDeiconified(WindowEvent event){}
	public void windowOpened(WindowEvent event){}
	public void windowActivated(WindowEvent event){}
	public void windowDeactivated(WindowEvent event){}
	public void windowClosed(WindowEvent event){}
}
