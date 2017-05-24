package de.robo.felix.render.game;

import java.awt.Graphics2D;

public abstract class CollidableObject extends GameObject implements Collidable {

	public abstract void render(Graphics2D g);

	@Override
	public void onCollideWith(Collidable other) {
		
	}

	@Override
	public boolean collidesWith(Collidable other) {
		return super.collides(other);
	}

	
}
