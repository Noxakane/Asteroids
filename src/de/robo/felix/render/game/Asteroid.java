package de.robo.felix.render.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.imageio.ImageIO;

import de.robo.felix.Vector;
import de.robo.felix.Window;
import de.robo.felix.render.Particle;
import de.robo.felix.render.RenderManager;
import de.robo.felix.render.TextureCollection;

public class Asteroid extends CollidableObject implements PointsAward {

	public static final int STANDARD_RADIUS = 40;
	
	private int size = 2;
	
	private Vector dir = new Vector(1,-1);
	private Vector velocity = new Vector(0,0);
	private double speed = 0.4;
	private double deltaVel = 0.9;
	
	private Image texture;
	
	public Asteroid() {
		texture = TextureCollection.asteroid;
	}
	
	@Override
	public void onCollideWith(Collidable other) {
		if(other instanceof Laser) {
			breakDown(((Laser)other).getShooter());
		}
	}
	
	@Override
	public void render(Graphics2D g) {
		int size = (int) Math.round(radius*2);
		g.drawImage(texture, (int) (x-radius), (int) (y-radius), size, size, null);
		
		/*g.setColor(Color.BLUE);
		g.fillOval((int) Math.round(x-radius), (int) Math.round(y-radius), 
				(int) Math.round(2*radius), (int) Math.round(2*radius));*/
	}
	
	public void breakDown(ShooterSource shooter) {
		breakDown(false, true, shooter);
	}
	
	public void breakDown(boolean complete, boolean spawnPowerUp, ShooterSource shooter) {
		if(size > 0 && !complete) {
			double nRadius = radius/1.5;
			Asteroid a1 = new Asteroid();
			a1.size = size - 1;
			a1.setConstraints(x+10, y+10, nRadius);
			a1.velocity = new Vector(-dir.y, dir.x).mul(2);
			a1.dir = dir.add(a1.velocity.mul(0.2));
			a1.speed = speed * 1.25;
			
			Asteroid a2 = new Asteroid();
			a2.setConstraints(x+10, y+10, nRadius);
			a2.size = size - 1;
			a2.velocity = new Vector(dir.y, -dir.x).mul(2);
			a2.dir = dir;
			a2.dir = dir.add(a2.velocity.mul(0.2));

			a2.speed = speed * 1.25;
			
			RenderManager.add(a1);
			RenderManager.add(a2);
		} else {
			if(spawnPowerUp) PowerUp.spawn(1, new Vector(x, y));
		}
		Set<Particle> s = Particle.spawn((size+2)*10, new Vector(x, y), Vector.ZERO, 8, 1);
		Random random = new Random(System.nanoTime());
		for(Particle p : s) {
			p.setSpeed(random.nextDouble()*8+5);
			p.setColor(new Color(135, 114, 101));
		}
		
		if(shooter instanceof PointsCollector)
			((PointsCollector)shooter).addPoints(getAward());
		
		delete();
	}
	
	public void setDirection(Vector v) {
		dir = v.normalized();
	}
	
	public Vector getDirection() {
		return dir;
	}
	
	public static Set<Asteroid> spawn(int amount) {
		Set<Asteroid> set = new HashSet<Asteroid>();
		Random random = new Random(System.nanoTime());
		for(int i = 0; i < amount; i++) {
			Asteroid ast = new Asteroid();
			int xr = random.nextInt(Window.WIDTH);
			int yr = random.nextInt(Window.HEIGHT);
			int where = random.nextInt(4); //0 = links, 1 = rechts, 2 = oben, 3 = unten
			if(where == 0) {
				xr = -Asteroid.STANDARD_RADIUS;
			} else if(where == 1) {
				xr = Window.WIDTH + Asteroid.STANDARD_RADIUS;
			} else if(where == 2) {
				yr = -Asteroid.STANDARD_RADIUS;
			} else if(where == 3) {
				yr = Window.HEIGHT + Asteroid.STANDARD_RADIUS;
			}
			
			ast.setPosition(xr, yr);
			Vector dir = Window.randomPointAroundMid(250).add(ast.getPosition().mul(-1));
			ast.setDirection(dir);
			set.add(ast);
			RenderManager.add(ast);
		}
		return set;
	}
	
	public void setSpeed(double d) {
		this.speed = d;
	}
	
	@Override
	public void update() {
		x += dir.x * speed + velocity.x;
		y += dir.y * speed + velocity.y;

		velocity = velocity.mul(deltaVel);

		if(x < -DESPAWN_DISTANCE)
			x = Window.WIDTH + DESPAWN_DISTANCE;
		
		if(x > Window.WIDTH+DESPAWN_DISTANCE)
			x = -DESPAWN_DISTANCE;
		
		if(y < -DESPAWN_DISTANCE)
			y = Window.HEIGHT + DESPAWN_DISTANCE;
		
		if(y > Window.HEIGHT+DESPAWN_DISTANCE)
			y = -DESPAWN_DISTANCE;
	}

	@Override
	public int getAward() {
		return (3 - size);
	}

}
