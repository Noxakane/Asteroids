package de.robo.felix;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import de.robo.felix.render.TextureCollection;




public class MainMenu extends JFrame{
	static JFrame menu;
	static boolean menuCreated = false;
	
	public MainMenu(){
		menu = new JFrame("Hauptmenü - Asteroids");
		TextureCollection.load();
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		menu.setLocation(200, 200);
		menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		menu.setSize(600, 300);
		menu.setVisible(true);
		
		JButton startButton = new JButton("Spiel starten");
		startButton.setVerticalTextPosition(AbstractButton.CENTER);
		startButton.setHorizontalTextPosition(AbstractButton.LEADING);
		startButton.addActionListener((ActionEvent) -> {
		        MainMenu.startGame();
		        menu.setVisible(false);
		});
		startButton.setToolTipText("Startet das Spiel");
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(startButton, gbc);
		
		
		JButton configButton = new JButton("Optionen");
		configButton.setVerticalTextPosition(AbstractButton.CENTER);
		configButton.setHorizontalTextPosition(AbstractButton.LEADING);
		configButton.addActionListener((ActionEvent e) -> {

		    	ConfigurationMenu.openOptions();
		        menu.setVisible(false);
		    });
		
		configButton.setToolTipText("Öffnet die Spieloptionen");
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 1;
		panel.add(configButton, gbc);
		
		JButton closeButton = new JButton("Spiel verlassen");
		closeButton.setVerticalTextPosition(AbstractButton.CENTER);
		closeButton.setHorizontalTextPosition(AbstractButton.LEADING);
		closeButton.addActionListener(new ActionListener(){
		      public void actionPerformed(ActionEvent e)
		      {
		        System.exit(0);
		      }
		    });
		
		closeButton.setToolTipText("Spiel verlassen");
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.5;
		gbc.gridx = 0;
		gbc.gridy = 2;
		panel.add(closeButton, gbc);
		menu.add(panel);		
		menu.validate();
		menu.repaint();
	}
	
	public static void startMenu(){
		if(menuCreated){
			menu.setVisible(true);
		}
		else{
			new MainMenu();
		}
	}
		
	
	public static void startGame(){
		//catch 'no spaceship'-exception
		Window game = new Window();
		game.init();
		game.start();
	}
	
}
