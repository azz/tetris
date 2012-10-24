package ui;

import game.Game;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class Tetris extends JFrame {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					new Tetris();
				} catch (Exception e) {
					System.err.println("Uncaught exception in main thread occured.");
				}
			}
		});
	}

	private Tetris() {	
		add(new Game(this));
		
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("Tetris");
        setResizable(false);
        setVisible(true);
	}

}
