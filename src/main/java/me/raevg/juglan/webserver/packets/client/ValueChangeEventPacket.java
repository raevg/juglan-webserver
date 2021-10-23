package me.raevg.juglan.webserver.packets.client;

public class ValueChangeEventPacket extends ClientPacket {
	private String	nodeId;
	private String	value;
	
	public String getNodeId() { return nodeId; }
	
	public String getValue() { return value; }
}
