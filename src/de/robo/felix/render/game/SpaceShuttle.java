package de.robo.felix.render.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.SynchronousQueue;

import javax.imageio.ImageIO;

import de.robo.felix.Vector;
import de.robo.felix.Window;
import de.robo.felix.render.Camera;
import de.robo.felix.render.RenderManager;
import de.robo.felix.render.TextureCollection;
import de.robo.felix.render.gui.HealthBar;

public class SpaceShuttle extends CollidableObject implements PointsCollector, ShooterSource {

	private int points;
	private double speed = 5;
	private double shootCooldown = 0.25; //In sekunden
	private boolean canShoot = true;
	private Vector dir = new Vector(0,1);
	private double angle = 0;
	private double rotationSpeed = 5; //Rotation angle per update
	private boolean invulnerable = false;
	private double invulnerableTime = 1;
	private double currSpeed = 0;
	private double speedInc = 0.25;
	private double speedDec = 0.2;
	private double deltaAngle = 0;
	private double angleInc = 0.07;
	private double angleDec = 0.05;
	private double maxAngle = 1.2;
	private List<PowerUpEffect> effects = new ArrayList<PowerUpEffect>();
	private Image texture;
	
	public SpaceShuttle() {
		zIndex = 100;
		texture = TextureCollection.shuttle;
		System.out.println("Wurst");
		radius = texture.getWidth(null);
	}
	
	public void setTexture(Image texture) {
		this.texture = texture;
	}
	
	@Override
	public void onCollideWith(Collidable other) {
		if(other instanceof Asteroid) {
			if(invulnerable)
				return;
			
			double health = HealthBar.instance.getPercentage();
			double newHealth = health - 1d/3d;
			if(newHealth < 0.1d)
				System.exit(0);
			HealthBar.instance.setPercentage(newHealth);
			setInvulnerable(invulnerableTime);
			((Asteroid)other).breakDown(false, false, this);
			Camera.wiggleWiggleWiggle(5, 0.03);
		} else if(other instanceof PowerUp) {
			PowerUp p = (PowerUp) other;
			p.onCollect(this);
			p.collectAnimation();
		}
	}
	
	public void addPoints(int p) {
		points += p;
	}
	
	public int getPoints() {
		return points;
	}
	
	public void setInvulnerable(double time) {
		invulnerable = true;
		new Thread(() -> {
			try {
				Thread.sleep((long)(1000*time));
			} catch (InterruptedException e) {
				invulnerable = false;
				e.printStackTrace();
			}
			invulnerable = false;
		}).start();
	}

	@Override
	public void render(Graphics2D g) {
		if(texture != null) {
			double offset = Math.sqrt(radius*radius*2);
			
			AffineTransform transform = new AffineTransform();
			transform.rotate(Math.toRadians(angle-90), x, y);
			transform.translate(x-radius, y-radius);
			
			//transform.scale(1.5, 1.5);
			
			g.drawImage(texture, transform, null);
			
			
			return;
		}
		
		
		g.setColor(Color.WHITE);
		int[] xs = new int[3];
		int[] ys = new int[3];
		
		double da = 360d/3d;
		double dx = 0;
		double dy = 1;
		for(int i = 0; i < 3; i++) {
			dx = x+Math.cos(Math.toRadians(da*i+angle+180))*radius;
			dy = y+Math.sin(Math.toRadians(da*i+angle+180))*radius;
			xs[i] = (int)dx;
			ys[i] = (int)dy;
		}
		
		
		g.fillPolygon(xs, ys, 3);
		//h = sin(30) * radius
	}
	
	@Override
	public void update() {	
		double dy = 0;
		if(Input.keyDown(KeyEvent.VK_UP)) {
			dy -= 1;
		}
		if(Input.keyDown(KeyEvent.VK_DOWN)) {
			dy+= 1;
		}
		if(Input.keyDown(KeyEvent.VK_RIGHT)) {
			//deltaAngle = Math.min(deltaAngle + angleInc, maxAngle);
			rotate(maxAngle);
		} else if(Input.keyDown(KeyEvent.VK_LEFT)) {
			//deltaAngle = Math.min(deltaAngle + angleInc, maxAngle);
			rotate(-maxAngle);
		} else {
			//deltaAngle = Math.min(deltaAngle - angleDec, maxAngle);
		}
		if(Input.keyDown(KeyEvent.VK_SPACE)) {
			shoot();
		}

		boolean isMoving = dy != 0;
		if(isMoving) {
			if(dy > 0)
				currSpeed += speedInc;
			else if(dy < 0)
				currSpeed -= speedInc;
		} else {
			if(currSpeed < 0)
				currSpeed += speedDec;
			else if(currSpeed > 0)
				currSpeed -= speedDec;
		}
		currSpeed = Math.min(currSpeed, speed);
		currSpeed = Math.max(currSpeed, -speed);
		
		Vector v = dir.normalized().mul(currSpeed);
		x += v.x;
		y += v.y;
		
		
		if(x < -DESPAWN_DISTANCE)
			x = Window.WIDTH;
		
		if(x > Window.WIDTH+DESPAWN_DISTANCE)
			x = 0;
		
		if(y < -DESPAWN_DISTANCE)
			y = Window.HEIGHT;
		
		if(y > Window.HEIGHT+DESPAWN_DISTANCE)
			y = 0 ;
	}
	
	public void rotate(double modifier) {
		angle += rotationSpeed * modifier;
		angle = angle > 360 ? angle - 360 : angle;
		angle = angle < 0 ? 360 - angle : angle;
		
		dir = new Vector(Math.cos(Math.toRadians(angle)), Math.sin(Math.toRadians(angle)));
		dir = dir.normalized();
	}
	
	public void shoot() {
		if(!canShoot)
			return;
		
		Vector pos = new Vector(x, y).add(dir.mul(-10));
		
		Laser laser = new Laser(this);
		laser.setConstraints(pos.x, pos.y, 2);
		laser.setDirection(dir.mul(-1)); //TODO
		laser.setLength(25);
		canShoot = false;
		startShootCooldown();
		RenderManager.add(laser);
	}
	
	private void startShootCooldown() {
		new Thread(() -> {
			try {
				Thread.sleep((long) (1000*shootCooldown));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			canShoot = true;
		}).start();
	}

}
