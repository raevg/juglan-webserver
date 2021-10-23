package me.raevg.juglan.webserver.packets.server;

public class AddScriptPacket extends ServerPacket {
	private String path;
	
	public AddScriptPacket(String path) {
		if(path == null) throw new IllegalArgumentException("Path cannot be null!");
		
		this.path = path;
	}
	
	public String getPath() { return path; }
}
