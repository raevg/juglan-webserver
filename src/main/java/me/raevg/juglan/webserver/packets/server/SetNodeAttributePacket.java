package me.raevg.juglan.webserver.packets.server;

import me.raevg.juglan.webserver.WebNode;

public class SetNodeAttributePacket extends ServerPacket {
	private String	nodeId;
	private String	key;
	private String	value;
	
	public SetNodeAttributePacket(WebNode node, String key, String value) {
		if(node == null) throw new IllegalArgumentException("WebNode cannot be null!");
		if(key == null) throw new IllegalArgumentException("Key cannot be null!");
		if(value == null) throw new IllegalArgumentException("Value cannot be null!");
		
		this.nodeId = node.getId();
		this.key = key;
		this.value = value;
	}
	
	public String getNodeId() { return nodeId; }
	
	public String getKey() { return key; }
	
	public String getValue() { return value; }
}
