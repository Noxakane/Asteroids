package de.robo.felix.command;

import java.util.HashMap;
import java.util.Map;

public class CommandExecutor {

	static {
		register("showLayers", new CommandShowLayers());
	}
	
	private static Map<String, Command> cmds = new HashMap<>();
	private static void register(String label, Command cmd) {
		cmds.put(label, cmd);
	}
	
	public static void execute(String input) {
		String[] words = input.split(" ");
		Command cmd = cmds.get(words[0]);
		if(cmd == null)
			throw new IllegalArgumentException("Unknown command: "+words[0]);
		
		String[] args = new String[words.length-1];
		for(int i = 1; i < words.length; i++)
			args[i-1] = words[i];
		
		cmd.execute(args);
	}
	
}
