package me.raevg.juglan.webserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.raevg.juglan.webserver.event.KeyPressEvent;
import me.raevg.juglan.webserver.event.KeyReleaseEvent;
import me.raevg.juglan.webserver.event.MouseClickEvent;
import me.raevg.juglan.webserver.event.MousePressEvent;
import me.raevg.juglan.webserver.event.MouseReleaseEvent;
import me.raevg.juglan.webserver.event.ScrollEvent;
import me.raevg.juglan.webserver.event.ValueChangeEvent;
import me.raevg.juglan.webserver.packets.client.ClientPacket;
import me.raevg.juglan.webserver.packets.client.KeyPressEventPacket;
import me.raevg.juglan.webserver.packets.client.KeyUpEventPacket;
import me.raevg.juglan.webserver.packets.client.MouseClickEventPacket;
import me.raevg.juglan.webserver.packets.client.MouseDownEventPacket;
import me.raevg.juglan.webserver.packets.client.MouseUpEventPacket;
import me.raevg.juglan.webserver.packets.client.ScrollEventPacket;
import me.raevg.juglan.webserver.packets.client.ValueChangeEventPacket;
import me.raevg.juglan.webserver.packets.server.AddScriptPacket;
import me.raevg.juglan.webserver.packets.server.AddStylePacket;
import me.raevg.juglan.webserver.packets.server.FaviconChangePacket;
import me.raevg.juglan.webserver.packets.server.TitleChangePacket;

public abstract class WebPage {
	private WebSession	session;
	private WebNode		body	= new WebNode("body", "body");
	
	private String				title	= "---";
	private ResourceEntry		favicon	= new ResourceEntry(WebPage.class.getClassLoader(), "juglan-webserver/favicon-16x16.png");
	private List<ResourceEntry>	scripts	= new ArrayList<>();
	private List<ResourceEntry>	styles	= new ArrayList<>();
	
	protected Map<String, WebNode>	nodesCache	= new HashMap<>();
	protected Map<String, Object>	meta		= new HashMap<>();
	
	final void init0(WebSession session) {
		this.session = session;
		
		if(session != null) {
			setTitle(title);
			
			setFavicon(favicon.resLoader, favicon.res);
			
			for(ResourceEntry e : scripts)
				loadScript(e.resLoader, e.res);
			
			for(ResourceEntry e : styles)
				loadStyle(e.resLoader, e.res);
		}
		
		body.setSession(session);
	}
	
	protected abstract void init();
	
	public String getTitle() { return title; }
	
	public void setTitle(String title) {
		if(title == null) throw new NullPointerException("WebPage title cannot be null!");
		this.title = title;
		if(session != null) session.sendPacket(new TitleChangePacket(title));
	}
	
	public void setFavicon(ClassLoader resLoader, String res) {
		if(resLoader == null) throw new IllegalArgumentException("ResourceLoader cannot be null!");
		if(res == null) throw new IllegalArgumentException("Resource cannot be null!");
		
		favicon = new ResourceEntry(resLoader, res);
		JuglanWebServer.getConfig().addResourceLoader(resLoader);
		String path = "/internalres/" + res;
		if(session != null) session.sendPacket(new FaviconChangePacket(path));
	}
	
	public void loadScript(ClassLoader resLoader, String res) {
		if(resLoader == null) throw new IllegalArgumentException("ResourceLoader cannot be null!");
		if(res == null) throw new IllegalArgumentException("Resource cannot be null!");
		
		scripts.add(new ResourceEntry(resLoader, res));
		JuglanWebServer.getConfig().addResourceLoader(resLoader);
		String path = "/internalres/" + res;
		if(session != null) session.sendPacket(new AddScriptPacket(path));
	}
	
	public void loadStyle(ClassLoader resLoader, String res) {
		if(resLoader == null) throw new IllegalArgumentException("ResourceLoader cannot be null!");
		if(res == null) throw new IllegalArgumentException("Resource cannot be null!");
		
		styles.add(new ResourceEntry(resLoader, res));
		JuglanWebServer.getConfig().addResourceLoader(resLoader);
		String path = "/internalres/" + res;
		if(session != null) session.sendPacket(new AddStylePacket(path));
	}
	
	protected void recievedPacket(ClientPacket packet) {
		if(packet instanceof MouseClickEventPacket) {
			MouseClickEventPacket p = (MouseClickEventPacket) packet;
			WebNode node = nodesCache.get(p.getNodeId());
			if(node != null) {
				// construct event and bubble up from node until consumed
				MouseClickEvent evt = new MouseClickEvent(p.getX().intValue(), p.getY().intValue(), p.isAltDown(), p.isShiftDown(), p.isControlDown());
				while(!evt.isConsumed() && node != null) {
					node.event(evt);
					node = node.getParent();
				}
			}
		} else if(packet instanceof MouseDownEventPacket) {
			MouseDownEventPacket p = (MouseDownEventPacket) packet;
			WebNode node = nodesCache.get(p.getNodeId());
			if(node != null) {
				// construct event and bubble up from node until consumed
				MousePressEvent evt = new MousePressEvent(p.getX().intValue(), p.getY().intValue(), p.isAltDown(), p.isShiftDown(), p.isControlDown());
				while(!evt.isConsumed() && node != null) {
					node.event(evt);
					node = node.getParent();
				}
			}
		} else if(packet instanceof MouseUpEventPacket) {
			MouseUpEventPacket p = (MouseUpEventPacket) packet;
			WebNode node = nodesCache.get(p.getNodeId());
			if(node != null) {
				// construct event and bubble up from node until consumed
				MouseReleaseEvent evt = new MouseReleaseEvent(p.getX().intValue(), p.getY().intValue(), p.isAltDown(), p.isShiftDown(), p.isControlDown());
				while(!evt.isConsumed() && node != null) {
					node.event(evt);
					node = node.getParent();
				}
			}
		} else if(packet instanceof KeyPressEventPacket) {
			KeyPressEventPacket p = (KeyPressEventPacket) packet;
			WebNode node = nodesCache.get(p.getNodeId());
			if(node != null) {
				// construct event and bubble up from node until consumed
				KeyPressEvent evt = new KeyPressEvent(p.getKey(), p.isAltDown(), p.isShiftDown(), p.isControlDown());
				while(!evt.isConsumed() && node != null) {
					node.event(evt);
					node = node.getParent();
				}
			}
		} else if(packet instanceof KeyUpEventPacket) {
			KeyUpEventPacket p = (KeyUpEventPacket) packet;
			WebNode node = nodesCache.get(p.getNodeId());
			if(node != null) {
				// construct event and bubble up from node until consumed
				KeyReleaseEvent evt = new KeyReleaseEvent(p.getKey(), p.isAltDown(), p.isShiftDown(), p.isControlDown());
				while(!evt.isConsumed() && node != null) {
					node.event(evt);
					node = node.getParent();
				}
			}
		} else if(packet instanceof ScrollEventPacket) {
			ScrollEventPacket p = (ScrollEventPacket) packet;
			WebNode node = nodesCache.get(p.getNodeId());
			if(node != null) {
				ScrollEvent evt = new ScrollEvent(p.getScrollX(), p.getScrollY());
				node.event(evt);
			}
		} else if(packet instanceof ValueChangeEventPacket) {
			ValueChangeEventPacket p = (ValueChangeEventPacket) packet;
			WebNode node = nodesCache.get(p.getNodeId());
			if(node != null) {
				ValueChangeEvent evt = new ValueChangeEvent(p.getValue());
				node.event(evt);
			}
		} // else if packet is not recognized, silently ignore
	}
	
	public void cacheNode(WebNode node) { nodesCache.put(node.id, node); }
	
	public void uncacheNode(WebNode node) { nodesCache.remove(node.id); }
	
	public Object getMeta(String key) { return meta.get(key); }
	
	public void setMeta(String key, Object value) {
		if(key == null) throw new IllegalArgumentException("Key cannot be null!");
		meta.put(key, value);
	}
	
	public void removeMeta(String key) { meta.remove(key); }
	
	public WebNode getBody() { return body; }
	
	public WebSession getSession() { return session; }
	
	private class ResourceEntry {
		private ClassLoader	resLoader;
		private String		res;
		
		private ResourceEntry(ClassLoader resLoader, String res) {
			this.resLoader = resLoader;
			this.res = res;
		}
	}
}
