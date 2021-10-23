package me.raevg.juglan.webserver;

import java.util.HashSet;
import java.util.Set;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import me.raevg.juglan.webserver.packets.client.ClientPacket;
import me.raevg.juglan.webserver.packets.server.ServerPacket;

@ServerEndpoint("/websocket")
public class MyWsendpoint extends WebSession {
	
	private static Set<MyWsendpoint> clients = new HashSet<>();
	
	private Session	session;
	private WebPage	page;
	
	public MyWsendpoint() {}
	
	@OnOpen
	public void onOpen(Session session) {
		try {
			String path = session.getRequestParameterMap().get("path").get(0);
			Class<? extends WebPage> clazz = JuglanWebServer.getConfig().getRequestPages().get(path);
			if(clazz == null) {
				session.getBasicRemote().sendText("Invalid request context!");
				return;
			}
			// System.out.println("--- Connected: " + session.getId());
			this.session = session;
			clients.add(this);
			
			// TODO check if session and context are the same as an already cached one, and load from cache,
			// instead of creating a new one, this is to prevent session destroy on a reload or internet loss
			// for a moment
			
			page = clazz.getDeclaredConstructor().newInstance();
			page.init0(this);
			page.init();
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@OnMessage
	public void onMessage(String message, Session session) {
		// System.out.println("--- Message: " + message);
		
		ClientPacket packet = ClientPacket.fromJSON(message);
		page.recievedPacket(packet);
	}
	
	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
		// System.out.println("--- Session: " + session.getId());
		// System.out.println("--- Closing because: " + closeReason);
		
		// invalidate session and page
		page.init0(null);
		clients.remove(this);
	}
	
	// TODO onError
	@OnError
	public void onError(Throwable e) {
		System.err.println("A websocket error has occured!");
		e.printStackTrace();
	}
	
	@Override
	public void sendPacket(ServerPacket packet) {
		try {
			session.getBasicRemote().sendText(packet.toJSON());
		} catch(Exception e) {
			throw new RuntimeException("Error while trying to send " + packet + "!", e);
		}
	}
	
	@Override
	WebPage getPage() { return page; }
}