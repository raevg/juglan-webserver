package me.raevg.juglan.webserver.packets.client;

import java.math.BigDecimal;

public class ScrollEventPacket extends ClientPacket {
	private String		nodeId;
	private BigDecimal	scrollX;
	private BigDecimal	scrollY;
	
	public String getNodeId() { return nodeId; }
	
	public BigDecimal getScrollX() { return scrollX; }
	
	public BigDecimal getScrollY() { return scrollY; }
}
