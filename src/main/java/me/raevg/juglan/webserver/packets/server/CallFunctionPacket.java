package me.raevg.juglan.webserver.packets.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CallFunctionPacket extends ServerPacket {
	private String			function;
	private List<Object>	args	= new ArrayList<>();
	
	public CallFunctionPacket(String function, Object... args) {
		if(function == null) throw new IllegalArgumentException("Function name cannot be null!");
		
		this.function = function;
		for(Object o : args)
			this.args.add(o);
	}
	
	public String getFunction() { return function; }
	
	public List<Object> getArgs() { return Collections.unmodifiableList(args); }
}
