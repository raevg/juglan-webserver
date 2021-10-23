package me.raevg.juglan.webserver.event;

public class ValueChangeEvent extends Event {
	private String value;
	
	public ValueChangeEvent(String value) { this.value = value; }
	
	public String getValue() { return value; }
	
	@Override
	public void consume() { throw new UnsupportedOperationException("Event is not consumable!"); }
}
