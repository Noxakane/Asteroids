package de.robo.felix;

import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferStrategy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.Set;

import javax.swing.JFrame;

import de.robo.felix.command.CommandExecutor;
import de.robo.felix.render.RenderManager;
import de.robo.felix.render.TextureCollection;
import de.robo.felix.render.game.Asteroid;
import de.robo.felix.render.game.Background;
import de.robo.felix.render.game.CollisionManager;
import de.robo.felix.render.game.GameObject;
import de.robo.felix.render.game.Input;
import de.robo.felix.render.game.SpaceShuttle;
import de.robo.felix.render.gui.HealthBar;
import de.robo.felix.render.gui.PointsGui;

public class Window extends JFrame{

	private JFrame frame;
	private Thread gameLoop;
	private boolean running = false;
	private Canvas canvas;
	
	public static final int FPS_CAP = 200;
	public static final int UPS_CAP = 60;
	public static boolean INFINITE_FPS = false;
	public static final int WIDTH = 800, HEIGHT = 600;
	
	public Window() {
		frame = new JFrame();
		canvas = new Canvas();
	}

	
	public void init() {
		
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		WindowListener exitListener = new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent e) {
		    	frame.dispose();
		        MainMenu.startMenu();
		        }
		     
		};
		
		
		
		frame.setSize(WIDTH, HEIGHT);
		frame.addWindowListener(exitListener);
		frame.setLocation(200, 200);
		frame.add(canvas);
		frame.setResizable(false);
		canvas.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				Input.keyReleased(e.getKeyCode());
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				Input.keyPressed(e.getKeyCode());
			}
		});
		
		frame.setVisible(true);

		RenderManager.add(new Background());
		SpaceShuttle shuttle = new SpaceShuttle();
		shuttle.setConstraints(100, 100, 20);
		
		Asteroid.spawn(10);
		RenderManager.add(shuttle);

		RenderManager.add(new HealthBar());
		RenderManager.add(new PointsGui(shuttle));
	}
	
	public synchronized void start() {
		running = true;
		if(gameLoop == null) {
			
			gameLoop = new Thread(() -> {
				
				long lastTime = System.nanoTime();
				long lastTimeFps = System.nanoTime();
				long lastTimeUps = System.nanoTime();
				int fps = 0;
				while(running) {
					BufferStrategy bs = canvas.getBufferStrategy();
					if(bs == null) {
						canvas.createBufferStrategy(3);
						continue;
					}
					
					Graphics2D g = (Graphics2D) bs.getDrawGraphics();
					g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				             RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
					//g.clearRect(0, 0, WIDTH, HEIGHT);
					
					long thisTime = System.nanoTime();
					if(thisTime -lastTimeFps >= 1000000000) {
						frame.setTitle("FPS: "+fps);
						fps=0;
						lastTimeFps = thisTime;
					}
					
					if(thisTime -lastTimeUps >= 1000000000 / UPS_CAP) {
						RenderManager.updateAll();
						lastTimeUps = thisTime;
					}
					
					if(thisTime - lastTime >= Math.pow(10, 9) / FPS_CAP || INFINITE_FPS) {
						//60 mal pro sekunde rendern
						RenderManager.clear();
						CollisionManager.handleCollisions();
						RenderManager.renderAll(g);
						lastTime = thisTime;
						fps++;
					}
					
					bs.show();
				}
				
			});
			gameLoop.start();
			
			Thread spawnLoop = new Thread(() -> {
				
				double asteroidSpawnDelay = 3; //in sekunden
				double deltaSpawnDelay = 0.1d;
				double minSpawnDelay = 0.5;

				while(running) {

					Set<Asteroid> asts = Asteroid.spawn(1);
					Random random = new Random(System.nanoTime());
					for(Asteroid ast : asts) {
						ast.setSpeed(random.nextDouble()+0.2);
					}
					 
					try {
						Thread.sleep((int)(asteroidSpawnDelay*1000));
						asteroidSpawnDelay -= deltaSpawnDelay;
						asteroidSpawnDelay = Math.max(minSpawnDelay, asteroidSpawnDelay);
						
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
			spawnLoop.start();
			
			Thread cmdLoop = new Thread(() -> {
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
				
				while(running) {
					try {
						String input = reader.readLine();
						CommandExecutor.execute(input);
					} catch (IOException e) {
						e.printStackTrace();
						break;
					}
				}
			});
			cmdLoop.start();
		} else {
			gameLoop.start();
		}
	}
	
	public static Vector randomPointAroundMid(double offset) {
		Vector v = new Vector(WIDTH/2d, HEIGHT/2d);
		Random random = new Random(System.nanoTime());
		v = v.add(new Vector(random.nextDouble()*2*offset - offset, 
				random.nextDouble() * 2 * offset - offset));
		return v;
	}
	
	
	public static void activateFPSLimit(){
		INFINITE_FPS = false;
		
	}
	public static void deactivateFPSLimit(){
		INFINITE_FPS = true;
	}
	
	public static void activateHCMode(){
		//TODO: HP auf 1 Leben setzen, keinen neuen HP Spawn erlauben.
	}
	
	public static void deactivateHCMode(){
		//TODO: activateHCMode rückgängig machen
	}
	
	
	
	public void closeGameWindow(){
		
	}
}
