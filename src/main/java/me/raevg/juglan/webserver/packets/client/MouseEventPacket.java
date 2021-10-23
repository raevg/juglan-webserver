package me.raevg.juglan.webserver.packets.client;

import java.math.BigDecimal;

public abstract class MouseEventPacket extends ClientPacket {
	private String		nodeId;
	private BigDecimal	x, y;
	private boolean		altDown, shiftDown, controlDown;
	
	public String getNodeId() { return nodeId; }
	
	public BigDecimal getX() { return x; }
	
	public BigDecimal getY() { return y; }
	
	public boolean isAltDown() { return altDown; }
	
	public boolean isShiftDown() { return shiftDown; }
	
	public boolean isControlDown() { return controlDown; }
}
