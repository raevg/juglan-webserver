package me.raevg.juglan.webserver.event;

public class MouseReleaseEvent extends MouseEvent {
	public MouseReleaseEvent(int x, int y, boolean altDown, boolean shiftDown, boolean controlDown) { super(x, y, altDown, shiftDown, controlDown); }
}
