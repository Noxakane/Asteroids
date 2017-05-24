package de.robo.felix.render.game;

import java.awt.Color;
import java.awt.Graphics2D;

import de.robo.felix.render.gui.HealthBar;

public class HealthPowerUp extends PowerUp {

	@Override
	public void render(Graphics2D g) {
		g.setColor(Color.RED);
		g.fillOval((int) Math.round(x-radius), (int) Math.round(y-radius), 
				(int) Math.round(2*radius), (int) Math.round(2*radius));
	}

	@Override
	public void onCollect(SpaceShuttle shuttle) {
		HealthBar.instance.changePercentage(1/3d);
	}

}
