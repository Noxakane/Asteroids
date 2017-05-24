package de.robo.felix;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.print.DocFlavor.URL;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;

import de.robo.felix.render.TextureCollection;


public class ConfigurationMenu {
	static JFrame config;
	private boolean fpsLimit = true;
	private String fpsLimitStatus = "aktiviert";
	private boolean hcMode = false;
	private String hcModeStatus = "deaktiviert";
	
	private java.net.URL resource1 = ConfigurationMenu.class.getResource( "/textures/rip.png" );
	private java.net.URL resource2 = ConfigurationMenu.class.getResource( "/textures/NabooFake.png" );

	private final  ImageIcon spcShip1 = new ImageIcon(resource1);
	private final  ImageIcon spcShip2 = new ImageIcon(resource2);
	
	ConfigurationMenu(){
		config = new JFrame("Spieloptionen");
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		panel.setLayout(new GridBagLayout());
		
		config.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		config.setLocation(200, 200);
		config.setSize(400, 400);
		config.setVisible(true);
		
		JLabel s1 = new JLabel(spcShip1);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.5;
		gbc.ipady = 25;
		s1.setVerticalTextPosition(AbstractButton.CENTER);
		panel.add(s1, gbc);
		JLabel s2 = new JLabel(spcShip2);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 0.5;
		gbc.ipady = 25;
		s2.setVerticalTextPosition(AbstractButton.CENTER);
		panel.add(s2, gbc);
		
		
		//Spaceship selection
		JRadioButton spaceShip1 = new JRadioButton("Raumschiff 1", true);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.5;
		spaceShip1.setVerticalTextPosition(AbstractButton.CENTER);
		spaceShip1.addActionListener(new ActionListener(){
			 public void actionPerformed(ActionEvent e){
				 TextureCollection.useShuttle(0);
			 }
		    });
		panel.add(spaceShip1, gbc);
		JRadioButton spaceShip2 = new JRadioButton("Raumschiff 2", false);
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.5;
		spaceShip2.setVerticalTextPosition(AbstractButton.CENTER);
		spaceShip2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				 TextureCollection.useShuttle(1);
			 }
	    });
		panel.add(spaceShip2, gbc);
		ButtonGroup spShipButtonGrp = new ButtonGroup();
		spShipButtonGrp.add(spaceShip1);
		spShipButtonGrp.add(spaceShip2);
		
		
		
		//FPS limitation
		JButton fpsLimitButton = new JButton("FPS Limit " + fpsLimitStatus);
		fpsLimitButton.setVerticalTextPosition(AbstractButton.CENTER);
		fpsLimitButton.setHorizontalTextPosition(AbstractButton.LEADING);
		fpsLimitButton.addActionListener(new ActionListener(){
		      public void actionPerformed(ActionEvent e)
		      {
		    	if(!fpsLimit){
		    		Window.activateFPSLimit();
		    		fpsLimitStatus = "aktiviert";
		    		fpsLimitButton.setText("FPS Limit " + fpsLimitStatus);
		    		fpsLimit = true;
		    	}
		    	else{
		    		Window.deactivateFPSLimit();
		    		fpsLimitStatus = "deaktiviert";
		    		fpsLimitButton.setText("FPS Limit " + fpsLimitStatus);
		    		fpsLimit = false;
		    	}
		      }
		    });
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 2;
		gbc.weightx = 0.5;
		panel.add(fpsLimitButton, gbc);
		
		
		//Hardcore Mode
		JButton hcModeButton = new JButton("Hardcore-Modus " + hcModeStatus);
		hcModeButton.setVerticalTextPosition(AbstractButton.CENTER);
		hcModeButton.setHorizontalTextPosition(AbstractButton.LEADING);
		hcModeButton.addActionListener(new ActionListener(){
		      public void actionPerformed(ActionEvent e)
		      {
		    	if(hcMode){
		    		//TODO: Window.deactivateHCMode();
		    		hcModeStatus = "deaktiviert";
		    		hcModeButton.setText("Hardcore-Modus " + hcModeStatus);
		    		hcMode = false;
		    	}
		    	else{
		    		//TODO: Window.activateHCMode();
		    		hcModeStatus = "aktiviert";
		    		hcModeButton.setText("Hardcore-Modus " + hcModeStatus);
		    		hcMode = true;
		    	}
		      }
		    });
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 2;
		gbc.weightx = 0.5;
		panel.add(hcModeButton, gbc);
		
		
		//Close Options
		JButton closeButton = new JButton("Schlieﬂen");
		closeButton.setVerticalTextPosition(AbstractButton.CENTER);
		closeButton.setHorizontalTextPosition(AbstractButton.LEADING);
		closeButton.addActionListener(new ActionListener(){
		      public void actionPerformed(ActionEvent e)
		      {
		        config.setVisible(false);
		        MainMenu.menu.setVisible(true);
		      }
		    });
		closeButton.setToolTipText("Optionen schlieﬂen");
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.weightx = 0.5;
		gbc.gridwidth = 2;
		panel.add(closeButton, gbc);
		
		config.add(panel);
		config.validate();
		config.repaint();
	}
	
	
	public static void openOptions(){
		new ConfigurationMenu();
	}
}
