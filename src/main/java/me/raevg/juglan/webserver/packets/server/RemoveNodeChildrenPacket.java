package me.raevg.juglan.webserver.packets.server;

import me.raevg.juglan.webserver.WebNode;

public class RemoveNodeChildrenPacket extends ServerPacket {
	private String nodeId;
	
	public RemoveNodeChildrenPacket(WebNode node) {
		if(node == null) throw new IllegalArgumentException("WebNode cannot be null!");
		
		this.nodeId = node.getId();
	}
	
	public String getNodeId() { return nodeId; }
}
