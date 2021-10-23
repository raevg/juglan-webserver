package me.raevg.juglan.webserver;

import me.raevg.juglan.webserver.packets.server.CallFunctionPacket;
import me.raevg.juglan.webserver.packets.server.OpenLinkPacket;
import me.raevg.juglan.webserver.packets.server.ServerPacket;
import me.raevg.juglan.webserver.packets.server.ShowAlertPacket;

public abstract class WebSession {
	abstract void sendPacket(ServerPacket packet);
	
	abstract WebPage getPage();
	
	public void executeFunction(String function, Object... args) { sendPacket(new CallFunctionPacket(function, args)); }
	
	public void alert(String message) { sendPacket(new ShowAlertPacket(message)); }
	
	/**
	 * Open a link at the specified target and optionally focus the opened window/tab. This is not
	 * generally recommended, because browsers will prevent opening of windows/pop ups if not initiated
	 * by user action. Better alternative is an <a> link or adding onclick attribute to a button.
	 * 
	 * @param link
	 * @param target
	 * @param focus
	 */
	public void open(String link, String target, boolean focus) { sendPacket(new OpenLinkPacket(link, target, focus)); }
}
