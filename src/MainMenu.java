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

/**
 * Class handles the main menu frame and user menu inputs
 * @author Aleksejs Loginovs
 *
 */
public class MainMenu implements ActionListener, WindowListener
{
	protected JFrame mainMenuFrame;
	private JPanel mainMenuContent;
	protected JButton newGame;
	protected JButton loadGame;
	protected JButton exit;
	
	/**
	 * Default menu constructor that initialises the frame and displays it
	 */
	public MainMenu()
	{
		mainMenuFrame = new JFrame(); //a window that holds everything
		mainMenuContent = new JPanel(); //panel that contains the main content of the frame
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
	
	/**
	 * Initialises all the frame contents and adds them to the panel
	 */
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
	
	/**
	 * Method is used to set the menu frame visible
	 */
	public void show()
	{
		mainMenuFrame.setVisible(true);
	}	
	
	/**
	 * Method is used to confirm that user wants to exit the program and stop it's execution
	 * or get back to the game
	 */
	public static void exit()
	{
		int sureExit = 1;
		//displays the confirmation dialog
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
	
	/**
	 * Method handles the main menu user button clicks
	 */
	public void actionPerformed(ActionEvent event)
	{
		JButton button = (JButton)event.getSource(); //the button that caused the event
		if(button == newGame)
		{
			new Game(mainMenuFrame, "savedGames/defaultMap1.map");
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
	
	/**
	 * Method is used to prevent program from closing the window after the X button was pressed
	 * and to call the exit confirmation method instead
	 */
	public void windowClosing(WindowEvent event)
	{
		MainMenu.exit();
	}
	
	/**
	 * Method is used to get the main menu frame
	 * @return main menu frame
	 */
	public JFrame getFrame()
	{
		return mainMenuFrame;
	}
	
	/**
	 * Main method that is used to launch program
	 * @param args
	 */
	public static void main(String args[])
	{
		MainMenu menu = new MainMenu();
		menu.show();
	}
	
	//methods not used in tbe program
	public void windowClosed(WindowEvent event){}
	public void windowIconified(WindowEvent event){}
	public void windowDeiconified(WindowEvent event){}
	public void windowOpened(WindowEvent event){}
	public void windowActivated(WindowEvent event){}
	public void windowDeactivated(WindowEvent event){}
}
