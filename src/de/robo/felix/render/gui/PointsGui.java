package de.robo.felix.render.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import de.robo.felix.Window;
import de.robo.felix.render.game.SpaceShuttle;

public class PointsGui extends GuiObject{

	private SpaceShuttle shuttle;
	
	public PointsGui(SpaceShuttle shuttle) {
		this.shuttle = shuttle;
	}
	
	public int getPoints() {
		return shuttle.getPoints();
	}
	
	public void addPoints(int points) {
		shuttle.addPoints(points);
	}
	
	@Override
	public void update() {
		
	}

	@Override
	public void render(Graphics2D g) {
		String s = "Points: "+getPoints();
		g.setColor(Color.WHITE);
		g.drawString(s, Window.WIDTH-100, 30);
	}
	
}
