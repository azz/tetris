package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

public class Keyboard implements KeyListener {

	Set<Integer> keysDown;
	Set<Integer> oldKeys;
	
	public static KeyEvent KEYS;
	
	public Keyboard() {
		keysDown = new HashSet<Integer>();
		oldKeys = new HashSet<Integer>();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (keysDown.contains(e.getKeyCode())) {
			
			keysDown.remove(e.getKeyCode());
			// System.out.println("Key up:" + KeyEvent.getKeyText(e.getKeyCode()));
			
			// System.out.println(Arrays.deepToString(keysDown.toArray()));
		}
		e.consume();
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (!keysDown.contains(e.getKeyCode())) {
			
			keysDown.add(e.getKeyCode());
			// System.out.println("Key down:" + KeyEvent.getKeyText(e.getKeyCode()));
		}
		e.consume();
	}

	public boolean isKeyDown(int keyCode) {
		return keysDown.contains(keyCode);
	}

	public boolean wasKeyDown(int keyCode) {
		return oldKeys.contains(keyCode);
	}
	
	public boolean wasKeyPressed(int keyCode) {
		return keysDown.contains(keyCode) && !oldKeys.contains(keyCode);
	}

	public boolean wasKeyReleased(int keyCode) {
		return !keysDown.contains(keyCode) && oldKeys.contains(keyCode);
	}
	
	public Set<Integer> getKeysDown() {
		// return deep copy
		Set<Integer> temp = new HashSet<Integer>();
		for (int val : keysDown)
			temp.add(val);
		
		return temp;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {}

	public void update() {
		
		// oldKeys = keysDown;
		oldKeys.clear();
		// should be small, not a memory concern
		for (int key : keysDown) {
			oldKeys.add(key);
		}
		// System.out.println("OLD KEYS: " + oldKeys);
		// System.out.println("NEW KEYS: " + keysDown);
	}
	
}
