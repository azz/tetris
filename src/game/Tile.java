package game;

import java.awt.Color;

public class Tile {

	private Color color;
	private boolean 
			active,
			fixed;
	
	
	public Tile() {
		color = Color.BLACK;
		active = false;
		fixed = false;
	}
	
	public Tile(Color color, boolean active) {
		this.color = color;
		this.active = active;
		this.fixed = false;
	}
	
	public static Tile newInactive() {
		return new Tile(Color.GRAY, false);
	}
	
	public void randomColor() {
		color = Color.getHSBColor((float)Math.random(), (float)Math.random(), (float)Math.random());
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void deactivate() {
		active = false;
		fixed = true;
	}
	
	public Color getColor() {
		return color;
	}

	public boolean isFixed() {
		return fixed;
	}
	
}
