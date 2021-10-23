package me.raevg.juglan.webserver.packets.server;

import me.raevg.juglan.webserver.AbstractWebNode;
import me.raevg.juglan.webserver.WebNode;

public class AddChildPacket extends ServerPacket {
	private String	nodeId;
	private int		index;
	private String	html;
	
	public AddChildPacket(WebNode node, int index, AbstractWebNode nodeToAdd) {
		if(node == null) throw new IllegalArgumentException("WebNode cannot be null!");
		if(nodeToAdd == null) throw new IllegalArgumentException("Node to be added cannot be null!");
		
		this.nodeId = node.getId();
		this.index = index;
		this.html = nodeToAdd.getHTML();
	}
	
	public String getNodeId() { return nodeId; }
	
	public int getIndex() { return index; }
	
	public String getHtml() { return html; }
}
