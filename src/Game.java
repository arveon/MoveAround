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

import java.awt.KeyboardFocusManager;
import java.util.ArrayList;
import java.util.Timer;


public class Game implements KeyListener, WindowListener
{
	public static final int DEFAULT_TIMER = 180;
	
	private JFrame menuFrame;
	private JFrame gameFrame;
	private JPanel gameContent;
	private JLabel health;
	private JPanel gameFieldPanel;
	
	private ArrayList<ArrayList<JLabel>> gameField;
	private ArrayList<ArrayList<String>> field;
	
	private Field curField;
	private Player player;
	private ArrayList<Enemy> enemies;
	private ArrayList<JLabel> enemyLabels;
	private ArrayList<Timer> moveEnemy;
	private Timer gameTimer;
	private int time;
	private int timer;
	private JLabel timerLabel;
	
	boolean keyPressed; //not sure if want to use it
	
	public Game(JFrame menuFrame, String filepath)
	{
		timer = 0;
		this.menuFrame = menuFrame;
		menuFrame.setVisible(false);
		keyPressed = false;
		curField = new Field(filepath, this);
		
		if(curField.getField().size() > 0 && enemies != null)
		{
			curField.updatePlayerPosition(player.getGridx(), player.getGridy());
		
			for(Enemy enemy: enemies)
			{
				curField.updateEnemyPosition(enemy.getGridx(), enemy.getGridy(), enemy.getPrevXPosition(), enemy.getPrevYPosition());
			}
			field = curField.getField();
			initialiseFrame();
			updateFieldLabelTexts();
			
			enemyMovement();
		}
		else
		{
			menuFrame.setVisible(true);
		}
	}
	
	public void enemyMovement()
	{
		moveEnemy = new ArrayList<Timer>();
		for(int i = 0; i < enemies.size(); i++)
		{
			Timer temp = new Timer();
			temp.schedule(new MoveEnemy(enemies.get(i), curField, this), 0, 2001-enemies.get(i).getSpeed());
			moveEnemy.add(temp);
		}
		gameTimer = new Timer();
		gameTimer.schedule(new GameTimer(this), 0, 1000);
	}
	
	public void initialiseFrame()
	{
		
		gameFrame = new JFrame();
		gameFrame.setFocusTraversalKeysEnabled(false);
		gameFrame.addWindowListener(this);
		
		gameContent = new JPanel();
		gameContent.setLayout(new GridBagLayout());
		gameContent.addKeyListener(this);
		gameContent.setFocusable(true);
		gameContent.requestFocusInWindow();
		
		gameFieldPanel = new JPanel();
		gameFieldPanel.setLayout(new GridBagLayout());
		
		timerLabel = new JLabel();
		timerLabel.setText("Time: " + Integer.toString(timer));
		health = new JLabel();
		enemyLabels = new ArrayList<JLabel>();
		gameField = new ArrayList<ArrayList<JLabel>>();
		
		placeField();
		
		for(int i = 0; i < enemies.size(); i++)
		{
			enemyLabels.add(new JLabel());
		}
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 2;
		gameContent.add(gameFieldPanel, constraints);
		
		constraints.gridy = 1;
		gameContent.add(health, constraints);
		
		constraints.gridy = 0;
		gameContent.add(timerLabel, constraints);
		
		for(int i = 0; i < enemyLabels.size(); i++)
		{
			constraints.gridy = i+3;
			gameContent.add(enemyLabels.get(i), constraints);
		}
		
		gameFrame.add(gameContent);
		
		gameFrame.setLocationRelativeTo(null);
		gameFrame.pack();
		gameFrame.setSize(new Dimension(400,400));
		gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		gameFrame.setVisible(true);
		
		System.out.println("asdasdasd");
	}
	
	public void placeField()
	{
		for(int i = 0; i < field.size(); i++)
		{
			ArrayList<JLabel> row = new ArrayList<JLabel>();
			for(int j = 0; j < field.get(i).size(); j++)
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

	public void updateFieldLabelTexts()
	{
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
		field = curField.getField();
		if(Rules.checkGameLost(player))
		{
			health.setText("Health: 0");
			killTimerTasks();
			JOptionPane.showMessageDialog(null, "You have been defeated!");
			closeGameField();
		}
		else if(Rules.checkGameWon(enemies))
		{
			killTimerTasks();
			JOptionPane.showMessageDialog(null, "You have defeated all enemies, congratulations!");
			closeGameField();
		}
		else
		{	
			health.setText("Health: " + player.getHp());
			for(int i = 0; i < enemies.size(); i++)
			{
				enemyLabels.get(i).setText("Enemy " + (i+1) + " hp : " + enemies.get(i).getHp());
			}
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
	
	public void setTimer(int timeRemaining)
	{
		time = timeRemaining;
		timer = timeRemaining;
	}
	
	public void updateTimer()
	{
		timer--;
		if(timer < 1)
		{
			System.out.println("Timer: " + timer);
			timerLabel.setText("Time: " + timer);
			killTimerTasks();
			JOptionPane.showMessageDialog(null, "You ran out of time. Game lost.");			
			closeGameField();
		}
		else
		{
			System.out.println("Timer: " + timer);
			timerLabel.setText("Time: " + timer);
		}
	}
	
	public void keyPressed(KeyEvent event)
	{
		boolean movementKey = false;
		int direction = 0;
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
			case KeyEvent.VK_ESCAPE:	//new SecondaryMenu();
										JOptionPane.showMessageDialog(null, "Secondary menu placeholder");
										break;
			}
			
			if(movementKey)
			{
				if(Rules.moveCheck(player, curField, direction))
				{
					Enemy temp = Rules.checkPlayerAttacking(enemies, player, curField, direction);
					if(temp == null)
					{
						player.move(direction);
					}
					else 
					{
						player.attackEnemy(this, temp, direction, curField);
					}
					curField.updatePlayerPosition(player.getGridx(), player.getGridy());
					updateFieldLabelTexts();
					keyPressed = true;
				}
				movementKey = false;
			}
		
		}
		System.out.println("Finishedmove");
	}
	
	public void keyReleased(KeyEvent event)
	{
		keyPressed = false;
	}
	
	public void keyTyped(KeyEvent event)
	{
		//not using
	}	

	public void windowIconified(WindowEvent event)
	{}
	
	public void windowDeiconified(WindowEvent event){}
	public void windowOpened(WindowEvent event){}
	public void windowActivated(WindowEvent event){}
	public void windowDeactivated(WindowEvent event){}
	public void windowClosed(WindowEvent event){}
	
	public void windowClosing(WindowEvent event)
	{
		System.out.println("WINDOWCLOSING");
		killTimerTasks();
		menuFrame.setVisible(true);
	}
	
	
	public void setPlayer(Player player)
	{
		this.player = player;
	}
	
	public void setEnemies(ArrayList<Enemy> enemyList)
	{
		this.enemies = enemyList;
	}
	
	public void setField(Field field)
	{
		this.curField = field;
	}
	
	public JFrame getFrame()
	{
		return gameFrame;
	}
	
	public Player getPlayer()
	{
		return player;
	}
	
	public void closeGameField()
	{
		gameFrame.dispose();
		menuFrame.setVisible(true);;
	}
	
	public void killTimerTasks()
	{
		for(Timer temp: moveEnemy)
		{
			temp.cancel();
		}
		gameTimer.cancel();
	}
}
