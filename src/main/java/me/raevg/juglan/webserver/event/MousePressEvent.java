package me.raevg.juglan.webserver.event;

public class MousePressEvent extends MouseEvent {
	public MousePressEvent(int x, int y, boolean altDown, boolean shiftDown, boolean controlDown) { super(x, y, altDown, shiftDown, controlDown); }
}
