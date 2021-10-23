package me.raevg.juglan.webserver.packets.client;

public abstract class KeyEventPacket extends ClientPacket {
	private String	nodeId;
	private String	key;
	private boolean	altDown, shiftDown, controlDown;
	
	public String getNodeId() { return nodeId; }
	
	public String getKey() { return key; }
	
	public boolean isAltDown() { return altDown; }
	
	public boolean isShiftDown() { return shiftDown; }
	
	public boolean isControlDown() { return controlDown; }
}
