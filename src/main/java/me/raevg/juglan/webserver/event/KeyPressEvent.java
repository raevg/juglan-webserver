package me.raevg.juglan.webserver.event;

public class KeyPressEvent extends KeyEvent {
	public KeyPressEvent(String key, boolean altDown, boolean shiftDown, boolean controlDown) { super(key, altDown, shiftDown, controlDown); }
}
