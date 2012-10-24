package game;

import java.awt.Color;

public class Tetrimino {

	public static enum Type { 
		I(Color.CYAN),
		J(Color.BLUE),
		L(Color.ORANGE),
		O(Color.YELLOW),
		S(Color.GREEN),
		T(Color.MAGENTA),
		Z(Color.RED);
		
		private Color color;
		
		Type(Color color) {
			this.color = color;
		}
		
		public Color color() {
			return color;
		}
	}
	
	private static enum Orientation {
		UP, RIGHT, DOWN, LEFT
	}
	
	private Orientation orientation;
	private Type type;
	private int age;
	private int translation;
	private boolean alive;
	private boolean[][] previous;
	private boolean[][] current;
	
	public Tetrimino(Type type) {
		orientation = Orientation.UP;
		this.type = type;
		age = 0;
		translation = 0;
		alive = true;
	}
	
	public void step() {
		previous = current;
		age++;
		current = _getArray();
	}
	
	public int getAge() {
		return age;
	}
	
	public int getTranslation() {
		return translation;
	}
	
	public boolean isAlive() {
		return alive;
	}
	
	public void kill() {
		alive = false;
	}
	
	public Color color() {
		return type.color();
	}
	
	public void rotate(boolean clockwise) {
		if (!clockwise) {
			if (orientation == Orientation.LEFT)
				orientation = Orientation.DOWN;
			else if (orientation == Orientation.DOWN)
				orientation = Orientation.RIGHT;
			else if (orientation == Orientation.RIGHT)
				orientation = Orientation.UP;
			else if (orientation == Orientation.UP)
				orientation = Orientation.LEFT;
		} else {
			if (orientation == Orientation.LEFT)
				orientation = Orientation.UP;
			else if (orientation == Orientation.UP)
				orientation = Orientation.RIGHT;
			else if (orientation == Orientation.RIGHT)
				orientation = Orientation.DOWN;
			else if (orientation == Orientation.DOWN)
				orientation = Orientation.LEFT;
		}
	}

	public void move(int tranlsation) {
		this.translation += tranlsation;
	}
	
	public boolean[][] getPreviousArray() {
		return previous == null ? getCurrentArray() : previous;
	}
	
	public boolean[][] getCurrentArray() {
		if (current == null) {
			current = _getArray();
		}
		return current;
	}
	
	private boolean[][] _getArray() {
		final boolean T = true;
		final boolean F = false;
		
		switch (type) {
			case I:
				switch (orientation) {
					case RIGHT:
						// XXX
					case LEFT:
						return new boolean[][] 
								{
									{ T },
									{ T },
									{ T },
									{ T }
								};
					case DOWN:
						// XXX
					case UP:
						return new boolean[][] 
								{
									{ T, T, T, T }
								};
				}
			case J:
				switch (orientation) {
					case RIGHT:
						return new boolean[][] 
								{
									{ F, T },
									{ F, T },
									{ T, T }
								};
					case LEFT:
						return new boolean[][] 
								{
									{ T, T },
									{ T, F },
									{ T, F }
								};
					case DOWN:
						return new boolean[][] 
								{
									{ T, F, F },
									{ T, T, T }
								};
					case UP:
						return new boolean[][] 
								{
									{ T, T, T },
									{ F, F, T }
								};
				}
			case L:
				switch (orientation) {
					case RIGHT:
						return new boolean[][] 
								{
									{ T, F },
									{ T, F },
									{ T, T }
								};
					case LEFT:
						return new boolean[][] 
								{
									{ T, T },
									{ F, T },
									{ F, T }
								};
					case DOWN:
						return new boolean[][] 
								{
									{ T, T, T },
									{ T, F, F }
								};
					case UP:
						return new boolean[][] 
								{
									{ F, F, T },
									{ T, T, T }
								};
				}
			case O: // why can't they all be this easy?
				return new boolean[][] 
					{
						{ T, T },
						{ T, T }
					};
			case S:
				switch (orientation) {
					case RIGHT:
						// XXX
					case LEFT:	
						return new boolean[][] 
								{
									{ T, F },
									{ T, T },
									{ F, T }
								};
					case DOWN:				
						// XXX
					case UP:
						return new boolean[][] 
								{
									{ F, T, T },
									{ T, T, F }
								};
				}			
			case T:
				switch (orientation) {
					case RIGHT:
						return new boolean[][] 
								{
									{ T, F },
									{ T, T },
									{ T, F }
								};			
					case LEFT:
						return new boolean[][] 
								{
									{ F, T },
									{ T, T },
									{ F, T }
								};			
					case DOWN:
						return new boolean[][] 
								{
									{ T, T, T },
									{ F, T, F }
								};			
					case UP:
						return new boolean[][] 
								{
									{ F, T, F },
									{ T, T, T }
								};			
				}
			case Z:
				switch (orientation) {
					case RIGHT:
						// XXX
					case LEFT:	
						return new boolean[][] 
								{
									{ F, T },
									{ T, T },
									{ T, F }
								};
					case DOWN:				
						// XXX
					case UP:
						return new boolean[][] 
								{
									{ T, T, F },
									{ F, T, T }
								};
				}			
		}
		
		return null;	
	}
}
