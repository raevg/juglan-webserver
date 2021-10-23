package me.raevg.juglan.webserver.event;

public class MouseClickEvent extends MouseEvent {
	public MouseClickEvent(int x, int y, boolean altDown, boolean shiftDown, boolean controlDown) { super(x, y, altDown, shiftDown, controlDown); }
}
