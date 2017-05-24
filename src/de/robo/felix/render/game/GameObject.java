package de.robo.felix.render.game;

import de.robo.felix.Vector;
import de.robo.felix.render.RenderManager;
import de.robo.felix.render.Renderable;
import de.robo.felix.render.Updateable;

public abstract class GameObject implements Updateable, Renderable {

	public static final int DESPAWN_DISTANCE = 20;
	
	protected double x = 0, y = 0;
	protected double radius = 20;
	protected int zIndex = 0;

	public boolean collides(Collidable other) {

		double dx = other.getPosition().x - x;
		double dy = other.getPosition().y - y;
		double dist = Math.sqrt(dx*dx + dy*dy);
		return dist < radius + other.getRadius();
	}
	
	public double getRadius() {
		return radius;
	}
	
	public Vector getPosition() {
		return new Vector(x, y);
	}

	public void setX(double x) {
		this.x=x;
	}
	
	public void setY(double y) {
		this.y=y;
	}
	
	public void setPosition(double x, double y) {
		setX(x);
		setY(y);
	}
	
	public void setRadius(double radius) {
		this.radius = radius;
	}
	
	public void setConstraints(double x, double y, double radius) {
		setPosition(x, y);
		setRadius(radius);
	}
	
	public void onRegister() {
		
	}
	
	@Override
	public void update() {
		
	}
	
	@Override
	public int getZIndex() {
		return zIndex;
	}
	
	public void delete() {
		RenderManager.delete(this);
	}
	
}
