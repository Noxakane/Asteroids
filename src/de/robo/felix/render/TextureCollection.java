package de.robo.felix.render;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

import de.robo.felix.render.game.SpaceShuttle;

public class TextureCollection {

	public static Image asteroid;
	public static Image shuttle1;
	public static Image shuttle2;
	public static Image lazorrrrr;
	public static Image shuttle;
	
	public static void load() {
		try {
			ClassLoader loader = TextureCollection.class.getClassLoader();
			asteroid = ImageIO.read(loader.getResourceAsStream("textures/asteroid_1.png"));
			shuttle1 = ImageIO.read(loader.getResourceAsStream("textures/rip.png"));
			shuttle2 = ImageIO.read(loader.getResourceAsStream("textures/NabooFake.png"));
			shuttle = shuttle1;
			lazorrrrr = ImageIO.read(loader.getResourceAsStream("textures/LAZOR.png"));
			
			
		} catch(IOException e) {
			
		}

	}
	/**
	 * TODO: Wenn in SpaceShuttle.java textures auf shuttle statt shuttle1 gesetzt wird, dann müsste
	 * durch den Aufruf in MainMenu beim Spielstart shuttle = shuttle1 gesetzt sein. Spiel stürzt aber ab.
	 * Erst nach Schiffwechsel (egal welches) ist das Spiel dann spielbar.
	 *  
	 * @param sID
	 */
	public static void useShuttle(int sID){
		switch(sID){
		case 0:
			//Felix' Spaceshuttle
			shuttle = shuttle1;
		case 1:
			//Robo's Spaceshuttle;
			shuttle = shuttle2;
		}
		SpaceShuttle s = RenderManager.shuttle;
		if(s != null)
			s.setTexture(shuttle);
	}
}
