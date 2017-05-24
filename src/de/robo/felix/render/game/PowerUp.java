package de.robo.felix.render.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.robo.felix.Vector;
import de.robo.felix.render.RenderManager;

public abstract class PowerUp extends CollidableObject {

	public PowerUp() {
		radius = 10;
	}
	
	private static List<Class<? extends PowerUp>> ups = new ArrayList<>();
	static {
		ups.add(ExplosionPowerUp.class);
		//ups.add(HealthPowerUp.class);
	}
	
	public abstract void onCollect(SpaceShuttle shuttle);
	public void collectAnimation() {
		delete();
	}
	
	public static PowerUp random() {
		Random random = new Random(System.nanoTime());
		Class<? extends PowerUp> c = ups.get(random.nextInt(ups.size()));
		PowerUp ca;
		try {
			ca = c.newInstance();
			return ca;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static void spawn(double possibility, Vector position) {
		Random random = new Random(System.nanoTime());
		if(random.nextDouble() < possibility) {
			PowerUp p = PowerUp.random();
			p.setPosition(position.x, position.y);
			RenderManager.add(p);
		}
	}
	
}
