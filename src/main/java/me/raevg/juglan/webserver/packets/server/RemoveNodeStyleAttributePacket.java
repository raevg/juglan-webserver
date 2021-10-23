package me.raevg.juglan.webserver.packets.server;

import me.raevg.juglan.webserver.WebNode;

public class RemoveNodeStyleAttributePacket extends ServerPacket {
	private String	nodeId;
	private String	key;
	
	public RemoveNodeStyleAttributePacket(WebNode node, String key) {
		if(node == null) throw new IllegalArgumentException("WebNode cannot be null!");
		if(key == null) throw new IllegalArgumentException("Key cannot be null!");
		
		this.nodeId = node.getId();
		this.key = key;
	}
	
	public String getNodeId() { return nodeId; }
	
	public String getKey() { return key; }
}
