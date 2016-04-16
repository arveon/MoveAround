import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JPanel;

import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.GridBagConstraints;

import java.awt.Dimension;
import java.awt.Font;


public class MainMenu implements ActionListener, WindowListener
{
	protected JFrame mainMenuFrame;
	private JPanel mainMenuContent;
	protected JButton newGame;
	protected JButton loadGame;
	protected JButton exit;
	
	private Game game;
	
	public MainMenu()
	{
		mainMenuFrame = new JFrame(); //a window that holds everything
		mainMenuContent = new JPanel(); //panel that contains all the stuff
		mainMenuContent.setLayout(new GridBagLayout()); //set the layout of that panel
		
		setupPanel();
		
		mainMenuFrame.add(mainMenuContent);
		
		mainMenuFrame.pack(); //packs the frame setting the components to their preferred sizes
		mainMenuFrame.setSize(300,200); //sets the size of the window
		mainMenuFrame.setTitle("MoveAround! - Menu"); //title of the window
		mainMenuFrame.addWindowListener(this); // not relevant for you
		
		mainMenuFrame.setLocationRelativeTo(null); //makes window appear in the center of the screen
		mainMenuFrame.setResizable(false); //window is not resizable (true - to make it resizable)
		
		//tells what to do when user is trying to close the window
		//JFrame.EXIT_ON_CLOSE terminates the program just like System.exit(-1)
		//JFrame.DISPOSE_ON_CLOSE just closes the window and keeps the program running in processes
		mainMenuFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
	}
	
	public void setupPanel()
	{
		Font buttonFont = new Font("Arial", Font.ITALIC, 20);
		//create a button object, add an actionlistener to it, set it's size and set it's font
		newGame = new JButton("Start new game");
		newGame.addActionListener(this);
		newGame.setPreferredSize(new Dimension(200,40));
		newGame.setFont(buttonFont);
		//create a button object, add an actionlistener to it, set it's size and set it's font
		loadGame = new JButton("Load game");
		loadGame.addActionListener(this);
		loadGame.setPreferredSize(new Dimension(200,40));
		loadGame.setFont(buttonFont);
		//create a button object, add an actionlistener to it, set it's size and set it's font
		exit = new JButton("Exit");
		exit.addActionListener(this);
		exit.setPreferredSize(new Dimension(200,40));
		exit.setFont(buttonFont);
		
		//variable that is used to determine where on the grid will component be placed
		GridBagConstraints constr = new GridBagConstraints(); 
		
		//add a new button to the panel at the (0;0) cell
		constr.gridx = 0;
		constr.gridy = 0;
		mainMenuContent.add(newGame, constr);
		//add another button at the (0;1) cell
		constr.gridy = 1;
		mainMenuContent.add(loadGame, constr);
		//(0;2) cell
		constr.gridy = 2;
		mainMenuContent.add(exit, constr);
	}
	
	public void show()
	{
		mainMenuFrame.setVisible(true);
	}
	
	
	
	
	public static void exit()
	{
		int sureExit = 1;
		int windowOption = JOptionPane.YES_NO_OPTION;
		String text = "Are you sure you want to exit?";
		sureExit = JOptionPane.showConfirmDialog(null,text, "Exit the game", windowOption);
		if(sureExit == 0)
		{
			System.exit(-1);
		}
		else
		{
			return;
		}
	}
	
	public void actionPerformed(ActionEvent event)
	{
		JButton button = (JButton)event.getSource(); //the button that caused the event
		if(button == newGame)
		{
			game = new Game(mainMenuFrame, "savedGames/defaultMap1.map");
			
		}
		else if(button == loadGame)
		{
			LoadField loadField = new LoadField(this);
			loadField.loadField();
		}
		else if(button == exit)
		{
			MainMenu.exit();
		}
	}
	
	public void windowClosing(WindowEvent event)
	{
		MainMenu.exit();
	}
	
	public void windowClosed(WindowEvent event)
	{
		//notUsed
	}
	
	public void windowIconified(WindowEvent event)
	{
		//notUsed
	}
	
	public void windowDeiconified(WindowEvent event)
	{
		//notUsed
	}
	
	public void windowOpened(WindowEvent event)
	{
		//notUsed
	}
	
	public void windowActivated(WindowEvent event)
	{
		//notUsed
	}
	
	public void windowDeactivated(WindowEvent event)
	{
		//notUsed
	}
	
	public void setGame(Game game)
	{
		this.game = game;
	}
	
	public JFrame getFrame()
	{
		return mainMenuFrame;
	}
	
	public static void main(String args[])
	{
		MainMenu menu = new MainMenu();
		menu.show();
	}
}
