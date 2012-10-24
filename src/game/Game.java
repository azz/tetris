package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Game extends JPanel implements ActionListener {

	// CONSTANTS
	private final static short WINDOW_HEIGHT = 400;
	private final static short WINDOW_WIDTH = 600;
	private final static Dimension WINDOW = new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT);
	public final static Rectangle WINDOW_RECT = new Rectangle(WINDOW);
	private JFrame frame;

	
	public static enum GameState { CONSTRUCTING, INITIALIZED, PAUSED, PLAYING, TERMINATED, LOBBY };
	public static enum ServerStatus { HOST, CLIENT, DISABLED };
	public static enum GameType { SINGLE, MULTI };
	
	public GameState state;
	public ServerStatus server;
	public GameType mode;
	
	private GameInstance gameInstance;
	
	private static Color BACKGROUND_COLOR;
	
	// TICKDATA
	protected  int fps = 60;
	protected final double GAME_HERTZ = 30.0;
	protected int frameCount = 0;
	private final double TIME_BETWEEN_UPDATES = 1E9 / GAME_HERTZ;
	private final int MAX_UPDATES_BEFORE_RENDER = 5;
	
	// Input
	private Keyboard kb;
	
	/**
	 * @return the state
	 */
	public GameState getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(GameState state) {
		this.state = state;
	}

	/**
	 * @return the kb
	 */
	public Keyboard kb() {
		return kb;
	}

	/**
	 * @return the delta
	 */
	public float delta() {
		return delta;
	}

	// DOUBLE BUFFERING
	private static class DB {
		static Image image;
		static Graphics g;
	}
	
	private float delta;
	
	public Game(JFrame frame) {

		state = GameState.CONSTRUCTING;
		mode = GameType.SINGLE;
		server = ServerStatus.DISABLED;
		
		this.frame = frame;
		frame.setSize(WINDOW);
        
		BACKGROUND_COLOR = Color.BLACK;
		
		kb = new Keyboard();
		this.addKeyListener(kb);
		setFocusable(true);
		setBackground(BACKGROUND_COLOR);
		setDoubleBuffered(true);

		startGameThread();
	}

	private void startGameThread() {
		Thread loop = new Thread()
		{
			public void run()
			{
				init();

//				state = GameState.PAUSED;
//				while (state == GameState.PAUSED)
//				{
//					try 
//					{
//						update();
//						Thread.yield();
//						Thread.sleep(10);
//					} catch (InterruptedException ex) {ex.printStackTrace();}
//				}
				System.out.println("-Game Started-");
				gameLoop(); //begin the game
			}
		};
		loop.start();
	}

	private void init() {
		gameInstance = new GameInstance(this);
		
		gameInstance.init();
		
		state = GameState.INITIALIZED;
	}
	
	private synchronized void update() {
	
		
		gameInstance.update();
		
		kb.update();
	}
	
	private void gameLoop() {

		final double TARGET_FPS = fps;
		final double TARGET_TIME_BETWEEN_RENDERS = 1E9 / TARGET_FPS;
		
		double lastUpdateTime = System.nanoTime();
		double lastRenderTime = System.nanoTime();
		int lastSecondTime = (int) (lastUpdateTime / 1E9);
		int startSecond = lastSecondTime;
		
		state = GameState.PLAYING;
		
		while (state != GameState.TERMINATED)
		{
			
			double now = System.nanoTime();	    	
			int updateCount = 0;
			
			if (state != GameState.PAUSED)
			{
				//UPDATE
				while(now - lastUpdateTime > TIME_BETWEEN_UPDATES && updateCount < MAX_UPDATES_BEFORE_RENDER)
				{
					update();
					
					lastUpdateTime += TIME_BETWEEN_UPDATES;
					updateCount++;
				}
				
				//If update takes too long, dynamically alter update time
				if (now - lastUpdateTime > TIME_BETWEEN_UPDATES)
				{
					lastUpdateTime = now - TIME_BETWEEN_UPDATES;
				}
				
				//DRAW
				delta = Math.min(1.0f, (float) ((now - lastUpdateTime) / TIME_BETWEEN_UPDATES));
				repaint();
				lastRenderTime = now;
				
				// sounds();
				
				//PRINT
				int thisSecond = (int) (lastUpdateTime / 1E9);
				if (thisSecond > lastSecondTime)
				{
					System.out.println("" + (thisSecond-startSecond) + "s : " + frameCount + "fps");
					fps = frameCount;
					frameCount = 0;
					lastSecondTime = thisSecond;
				}
				
				while (now - lastRenderTime < TARGET_TIME_BETWEEN_RENDERS && now - lastUpdateTime < TIME_BETWEEN_UPDATES)
				{
					Thread.yield();
					
					try {
						Thread.sleep(1);
					} catch(Exception e) {} 
					
					now = System.nanoTime();
				}
			}
		} //End of game loop
		
	}
	
	/**
	 * DRAWING
	 */
	public void paint(Graphics g) {
		
		DB.image = createImage(WINDOW_WIDTH, WINDOW_HEIGHT);
		DB.g = DB.image.getGraphics();
		paintComponent(DB.g);
		g.drawImage(DB.image, 0, 0, this);
//
//		super.paint(g);
//
//		Graphics2D g2d = (Graphics2D) g;	 
//		
//		// paint here...
//		
//		
//		Toolkit.getDefaultToolkit().sync();
//		g.dispose();
	}

	public void paintComponent(Graphics g) {
		g.setColor(BACKGROUND_COLOR);
		g.fillRect(WINDOW_RECT.x, WINDOW_RECT.y, WINDOW_RECT.width, WINDOW_RECT.height);
		
		gameInstance.draw(g);
		
		// debug
		g.setColor(Color.WHITE);
		g.setFont(new Font("Monospace", Font.BOLD, 8));
		g.drawString("FPS: " + fps, 20, 20);
		
		frameCount++;
	}
	
	@Override
	public void actionPerformed(ActionEvent actionEvent) {
	    repaint();
	}

}
