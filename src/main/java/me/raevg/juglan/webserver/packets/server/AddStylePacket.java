package me.raevg.juglan.webserver.packets.server;

public class AddStylePacket extends ServerPacket {
	private String path;
	
	public AddStylePacket(String path) {
		if(path == null) throw new IllegalArgumentException("Path cannot be null!");
		
		this.path = path;
	}
	
	public String getPath() { return path; }
}
