package de.robo.felix.render;

import java.util.Random;

import de.robo.felix.Vector;

public class Camera {

	private static Vector offset = new Vector(0,0);
	
	public static void setOffset(Vector offset) {
		Camera.offset = offset;
	}
	
	public static int getXOffset() {
		return (int) Math.round(offset.x);
	}
	
	public static int getYOffset() {
		return (int) Math.round(offset.y);
	}

	public static void wiggleWiggleWiggle(int intensity, double delay) {
		new Thread(() -> {
			
			int a = intensity;
			Random random = new Random(System.nanoTime());
			while(Math.abs(a) > 0) {
				int xwiggle = random.nextInt(2*intensity)-intensity;
				int ywiggle = random.nextInt(2*intensity)-intensity;
				
				offset = offset.add(new Vector(xwiggle, ywiggle));
				try {
					Thread.sleep((long)(1000*delay));
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					a -= 1;
				}
			}
			offset = Vector.ZERO;
			
		}).start();
	}
	
	public static void update() {
		
	}
}
