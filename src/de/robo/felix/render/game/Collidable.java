package de.robo.felix.render.game;

import de.robo.felix.Vector;

public interface Collidable {

	public void onCollideWith(Collidable other);
	public boolean collidesWith(Collidable other);
	
	public double getRadius();
	public Vector getPosition();
	
}
