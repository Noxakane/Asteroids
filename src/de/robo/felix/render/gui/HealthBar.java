package de.robo.felix.render.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import de.robo.felix.render.Renderable;
import de.robo.felix.render.Updateable;

public class HealthBar extends GuiObject {

	public static HealthBar instance;
	
	private int xOffset = 10, yOffset = 10;
	private int width = 200, height = 15;
	
	private double percentage = 1;
	private double lowPercentage = 1;
	private double lowDelta = 0.008;

	public HealthBar() {
		instance = HealthBar.this;
	}
	
	public double getPercentage() {
		return percentage;
	}
	
	public void setPercentage(double p) {
		percentage = p;
		percentage = Math.min(1, percentage);
	}
	
	public void changePercentage(double d) {
		percentage += d;
		percentage = Math.min(1, percentage);
	}
	
	@Override
	public void render(Graphics2D g) {
		g.setColor(new Color(1, 0, 0, 0.5f));
		g.fillRect(xOffset, yOffset, (int) Math.round(xOffset+width*lowPercentage), xOffset+height);
		g.setColor(Color.red);
		g.fillRect(xOffset, yOffset, (int) Math.round(xOffset+width*percentage), xOffset+height);
		g.setColor(Color.WHITE);
		g.drawRect(xOffset, yOffset, xOffset+width, yOffset+height);
	}

	@Override
	public void update() {
		if(percentage > lowPercentage)
			lowPercentage = percentage;
		
		if(percentage < lowPercentage) {
			lowPercentage -= lowDelta;
		}
	}

}
