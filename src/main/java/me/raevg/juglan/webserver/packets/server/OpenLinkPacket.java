package me.raevg.juglan.webserver.packets.server;

public class OpenLinkPacket extends ServerPacket {
	private String	link;
	private String	target;
	private boolean	focus;
	
	public OpenLinkPacket(String link, String target, boolean focus) {
		if(link == null) throw new IllegalArgumentException("Link cannot be null!");
		if(target == null) throw new IllegalArgumentException("Target cannot be null!");
		this.link = link;
		this.target = target;
		this.focus = focus;
	}
	
	public String getMessage() { return link; }
	
	public String getTarget() { return target; }
	
	public boolean isFocus() { return focus; }
}
