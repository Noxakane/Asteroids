package de.robo.felix.render.game;

public class Input {

	private static boolean[] keys = new boolean[41]; 
	
	public static boolean keyDown(int key) {
		if(keys.length < key)
			return false;
		return keys[key];
	}
	
	public static void keyPressed(int key) {

		if(keys.length > key) {
			keys[key] = true;
		}
	}
	
	public static void keyReleased(int key) {
		if(keys.length > key) {
			keys[key] = false;
		}
	}
	
}
