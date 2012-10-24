package game;

import java.awt.Graphics;

/**
 * Abstract level game engine
 */
public class GameInstance {

	private Game base;
	
	// Game Objects
	private TetrisGrid self;
	private TetrisGrid opponent;
	
	// Timers
	private float timeSinceLastStep;
	private float stepTime;

	public GameInstance(Game base) {
		this.base = base;
		
		stepTime = 10f; // second, I think
		timeSinceLastStep = 0;
	}

	public void init() {

		self = new TetrisGrid(50, 50);
		opponent = new TetrisGrid(350, 50);
	}

	public void update() {
		timeSinceLastStep += base.delta();
		
		self.update(base);
		
		if (timeSinceLastStep > stepTime) {
			timeSinceLastStep = 0;
			self.step(base);
			opponent.step(base);
		}
	}

	public void draw(Graphics g) {
		
		self.draw(g, base);
		opponent.draw(g, base);
	}
}
