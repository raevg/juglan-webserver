package me.raevg.juglan.webserver.event;

public class MouseEvent extends Event {
	private int		x;
	private int		y;
	private boolean	altDown;
	private boolean	shiftDown;
	private boolean	controlDown;
	
	public MouseEvent(int x, int y, boolean altDown, boolean shiftDown, boolean controlDown) {
		this.x = x;
		this.y = y;
		this.altDown = altDown;
		this.shiftDown = shiftDown;
		this.controlDown = controlDown;
	}
	
	public int getX() { return x; }
	
	public int getY() { return y; }
	
	public boolean isAltDown() { return altDown; }
	
	public boolean isShiftDown() { return shiftDown; }
	
	public boolean isControlDown() { return controlDown; }
}
