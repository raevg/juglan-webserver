package me.raevg.juglan.webserver.event;

public abstract class Event {
	private boolean consumed = false;
	
	public Event() {}
	
	public boolean isConsumed() { return consumed; }
	
	public void consume() { consumed = true; }
}
