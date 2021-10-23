package me.raevg.juglan.webserver.packets.server;

public class TitleChangePacket extends ServerPacket {
	private String title;
	
	public TitleChangePacket(String title) {
		if(title == null) throw new IllegalArgumentException("Title cannot be null!");
		this.title = title;
	}
	
	public String getTitle() { return title; }
}
