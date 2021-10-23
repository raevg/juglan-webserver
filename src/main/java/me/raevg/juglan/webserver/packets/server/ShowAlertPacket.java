package me.raevg.juglan.webserver.packets.server;

public class ShowAlertPacket extends ServerPacket {
	private String message;
	
	public ShowAlertPacket(String message) {
		if(message == null) throw new IllegalArgumentException("Message cannot be null!");
		this.message = message;
	}
	
	public String getMessage() { return message; }
}
