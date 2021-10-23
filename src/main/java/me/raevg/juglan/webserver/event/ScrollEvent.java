package me.raevg.juglan.webserver.event;

import java.math.BigDecimal;

public class ScrollEvent extends Event {
	private BigDecimal	scrollX;
	private BigDecimal	scrollY;
	
	public ScrollEvent(BigDecimal scrollX, BigDecimal scrollY) {
		this.scrollX = scrollX;
		this.scrollY = scrollY;
	}
	
	public BigDecimal getScrollX() { return scrollX; }
	
	public BigDecimal getScrollY() { return scrollY; }
	
	@Override
	public void consume() { throw new UnsupportedOperationException("Event is not consumable!"); }
}
