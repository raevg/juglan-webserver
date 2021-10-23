package me.raevg.juglan.webserver.event;

public class KeyReleaseEvent extends KeyEvent {
	public KeyReleaseEvent(String key, boolean altDown, boolean shiftDown, boolean controlDown) { super(key, altDown, shiftDown, controlDown); }
}
