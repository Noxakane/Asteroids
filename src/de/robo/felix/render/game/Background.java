package de.robo.felix.render.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Random;

import de.robo.felix.Window;
import de.robo.felix.render.Renderable;

public class Background implements Renderable {

	private int sizeMin = 1;
	private int sizeMax = 4;
	private int amount = 100;
	
	private Image texture;
	public Background() {
		generateTexture();
	}
	
	private void generateTexture() {
		Random random = new Random(System.nanoTime());
		
		texture = new BufferedImage(Window.WIDTH, Window.HEIGHT, BufferedImage.TYPE_INT_BGR);
		Graphics2D g = (Graphics2D) texture.getGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);
		g.setColor(Color.WHITE);
		for(int i = 0; i < amount; i++) {
			int size = sizeMin + random.nextInt(sizeMax-sizeMin);
			int x = random.nextInt(Window.WIDTH);
			int y = random.nextInt(Window.HEIGHT);
			
			g.fillOval(x, y, size, size);
		}
	}
	
	@Override
	public void render(Graphics2D g) {
		g.drawImage(texture, 0, 0, Window.WIDTH, Window.HEIGHT, null);
	}

	@Override
	public int getZIndex() {
		return -1;
	}

	
	
}
