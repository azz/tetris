package game;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.Random;

public class TetrisGrid {

	Tile[][] 
			tiles,
			previousTiles;
	
	Tetrimino currentTetrimino;
	
	public static enum Collision { WALL, TILE, NONE };
	
	private static final byte 
			WIDTH = 14,
			HEIGHT = 20,
			TILE_SIZE = 15;

	private int 
			xOffset,
			yOffset;
	
	private float 
			rotateTimer,
			moveTimer;
		
	private float
			rotateStepTime,
			moveStepTime;
	
	private boolean rotateKeyDown;
	
	public TetrisGrid(int xOffset, int yOffset) {
		tiles = new Tile[WIDTH][HEIGHT];

		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				tiles[i][j] = Tile.newInactive();
				//test
				// tiles[i][j].randomColor();
			}
		}
		
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		
		this.rotateTimer = 0f;
		this.moveTimer = 0f;
		
		this.rotateStepTime = 2f;
		this.moveStepTime = 2f;
		
		this.rotateKeyDown = false;
		
		currentTetrimino = null;
	}
	
	/**
	 * @return the Width
	 */
	public static byte width() {
		return WIDTH;
	}

	/**
	 * @return the Width
	 */
	public static byte height() {
		return HEIGHT;
	}

	public void update(Game base) {

		if (currentTetrimino == null)
			return;

		// rotate 
		if (rotateTimer > 0f)
			rotateTimer -= base.delta();

		if (rotateTimer == 0f && base.kb().wasKeyReleased(KeyEvent.VK_UP))
			rotateKeyDown = false;
		
		if (!rotateKeyDown && rotateTimer == 0f && base.kb().wasKeyPressed(KeyEvent.VK_UP)) {
			currentTetrimino.rotate(true);
			rotateTimer = rotateStepTime;
			rotateKeyDown = true;
		}

		if (rotateTimer < 0f)
			rotateTimer = 0f;
		
		// translate
		if (moveTimer > 0f)
			moveTimer -= base.delta();
		
		if (moveTimer == 0f && base.kb().isKeyDown(KeyEvent.VK_LEFT)) {
			if (canMove(-1, 0)) {
				currentTetrimino.move(-1);
				moveTimer = moveStepTime;
			}
		}
		if (moveTimer == 0f && base.kb().isKeyDown(KeyEvent.VK_RIGHT)) {
			if (canMove(1, 0)) {
				currentTetrimino.move(1);
				moveTimer = moveStepTime;
			}
		}
		
		if (moveTimer < 0f) 
			moveTimer = 0f;
		
		placeCurrent();
		
	}
	
	public void step(Game base) {
		
		previousTiles = tiles;
		
		Random r = new Random();
		
		// test
		if (Math.random() < 0.1) {
			if (currentTetrimino == null || !currentTetrimino.isAlive()) {
				Tetrimino.Type[] types = Tetrimino.Type.values();
				currentTetrimino = new Tetrimino(types[r.nextInt(types.length)]);
			}				
		}

		// randomizeTiles();
		
		if (currentTetrimino == null)
			return;
		
		// place
		placeCurrent();
			
		// may be set to null if ends path
		if (currentTetrimino != null)
			currentTetrimino.step();
	}

	public void draw(Graphics g, Game base) {
		
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				
				g.setColor(tiles[i][j].getColor());
				g.fill3DRect(i * TILE_SIZE + xOffset, j * TILE_SIZE + yOffset, TILE_SIZE, TILE_SIZE, true);
			}
		}
	}
	
	/**
	 * Get a specific tile
	 * 
	 * @param column Starts at 0
	 * @param row Starts at 0
	 * @return the tile
	 */
	public Tile getTile(int column, int row) {
		return tiles[column][row];
	}
	
	private void placeCurrent() {

		flushPrevious();
		
		// check collisions here
		
		// place
		boolean[][] piece = currentTetrimino.getCurrentArray();
		boolean running = true;
		
//		Collision collisionType = detectCollisions();
//		
//		if (collisionType == Collision.WALL) {
//			return;
//		}	
//		if (collisionType == Collision.TILE) {
//			fixCurrent();
//			return;
//		}
		
		for (int i = 0; running && i < piece.length; i++) {
			for (int j = 0; running && j < piece[0].length; j++) {
				if (piece[i][j]) {
					if (j + currentTetrimino.getAge() != tiles[0].length) {
						// move down
						tiles[WIDTH/2 - 1 + i + currentTetrimino.getTranslation()][j + 
						        currentTetrimino.getAge()] = new Tile(currentTetrimino.color(), true);
					} else {
						// has reached bottom
						revert();
						fixCurrent();
						running = false;
					}
				}
			}
		}
	}

	private boolean canMove(int deltaX, int deltaY) {
		
		// check X
		int x = (WIDTH/2 - 1) + currentTetrimino.getTranslation();
		if (deltaX != 0) {
			x += deltaX;
			int tileWidth = currentTetrimino.getCurrentArray().length;
			int xLeft = x; // - tileWidth + 1;
			int xRight = x + tileWidth - 1;
			if (xLeft < 0 || xRight >= tiles.length) {
				return false;
			}
		}
		
		// check Y
		int y = currentTetrimino.getAge();
		if (deltaY != 0) {
			y += deltaY;
			if (y < 0 || y >= tiles[0].length) {
				return false;
			}
		}
		return true;
	}
	
	private Collision detectCollisions() {
		// get the current tile set
		boolean[][] a = currentTetrimino.getCurrentArray();
		Tile tile = null;
		for (int i = 0; i < a.length; i++) {
			if (a[i][a[0].length - 1]) {
				// x value, i.e. the the x-coordinate 0<x<tiles.length.
				int x = WIDTH/2 - 1 + i + currentTetrimino.getTranslation();
				System.out.println(x);
				if (x >= tiles.length || x < 0) {
					return Collision.WALL;
				}
				tile = tiles[x][a[0].length + currentTetrimino.getAge()];
				if (tile.isFixed()) {
					return Collision.TILE;
				} else if (!tile.isActive()) {
					return Collision.NONE;
				}
			}
		}
		
		return Collision.NONE;
	}

	private void fixCurrent() {

		currentTetrimino.kill();
		currentTetrimino = null;
		
	}

	public void revert() {
		
		flushPrevious();
		
		boolean[][] piece = currentTetrimino.getPreviousArray();
		
		for (int i = 0; i < piece.length; i++) {
			for (int j = 0; j < piece[0].length; j++) {
				if (piece[i][j]) {
					tiles[WIDTH/2 - 1 + i + currentTetrimino.getTranslation()]
							[j + currentTetrimino.getAge() - 1] =
							new Tile(currentTetrimino.color(), false);
				}
			}
		}
		
	}
	
	private void flushPrevious() {
		
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				if (tiles[i][j].isActive())
					tiles[i][j] = Tile.newInactive();
			}
		}
	}

	private void randomizeTiles() {

		for (Tile[] tl : tiles) {
			for (Tile t : tl) {
				t.randomColor();
			}
		}
		
	}

	
}
