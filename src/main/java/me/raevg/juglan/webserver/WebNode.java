package me.raevg.juglan.webserver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import me.raevg.juglan.webserver.event.Event;
import me.raevg.juglan.webserver.packets.server.AddChildPacket;
import me.raevg.juglan.webserver.packets.server.RemoveNodeAttributePacket;
import me.raevg.juglan.webserver.packets.server.RemoveNodeChildrenPacket;
import me.raevg.juglan.webserver.packets.server.RemoveNodePacket;
import me.raevg.juglan.webserver.packets.server.RemoveNodeStyleAttributePacket;
import me.raevg.juglan.webserver.packets.server.SetNodeAttributePacket;
import me.raevg.juglan.webserver.packets.server.SetNodeStyleAttributePacket;

import java.util.Set;
import java.util.UUID;

public class WebNode extends AbstractWebNode {
	protected boolean				tagOnly			= false;
	protected String				tag;
	protected String				id;
	protected Set<String>			classes			= new HashSet<>();
	protected Map<String, String>	attributes		= new HashMap<>();
	protected Map<String, String>	styleAttributes	= new HashMap<>();
	
	protected List<AbstractWebNode>	children	= new ArrayList<>();
	protected WebSession			session;
	protected WebNode				parent;
	
	public WebNode(String tag) {
		if(tag == null) throw new IllegalArgumentException("WebNode tag cannot be null!");
		this.tag = tag;
		id = UUID.randomUUID().toString();
	}
	
	public WebNode(String tag, boolean tagOnly) {
		this(tag);
		this.tagOnly = tagOnly;
	}
	
	public WebNode(String tag, String id) {
		this(tag);
		if(id == null) throw new IllegalArgumentException("WebNode id cannot be null!");
		this.id = id;
	}
	
	public WebNode(String tag, String id, boolean tagOnly) {
		this(tag, id);
		this.tagOnly = tagOnly;
	}
	
	/**
	 * Adds the node to the given class. More than one class may be specified, but should be delimited
	 * by at least one space.
	 * 
	 * @param classes one or more classes to be added to this node, delimited by at least one space
	 * @throws IllegalArgumentException if at least one of the specified classes contains invalid
	 *                                  characters
	 */
	public void addClass(String clazz) {
		String[] clazzez = clazz.split(" ");
		for(String s : clazzez) {
			if(s.isBlank()) continue;
			s = s.strip();
			if(!isValidAttributeIdentifier(s)) throw new IllegalArgumentException("String " + s + " is not a valid attribute/class identifier!");
		}
		for(String s : clazzez) {
			if(s.isBlank()) continue;
			s = s.strip();
			classes.add(s);
			if(session != null)
				session.sendPacket(new SetNodeAttributePacket(this, "class", String.join(" ", classes)));
		}
	}
	
	/**
	 * Removes the node from the given class. More than one class may be specified, but should be
	 * delimited by at least one space.
	 * 
	 * @param classes one or more classes to be removed from this node, delimited by at least one space
	 * @throws IllegalArgumentException if at least one of the specified classes contains invalid
	 *                                  characters
	 */
	public void removeClass(String clazz) {
		String[] clazzez = clazz.split(" ");
		for(String s : clazzez) {
			if(s.isBlank()) continue;
			s = s.strip();
			if(!isValidAttributeIdentifier(s)) throw new IllegalArgumentException("String " + s + " is not a valid attribute/class identifier!");
		}
		for(String s : clazzez) {
			if(s.isBlank()) continue;
			s = s.strip();
			classes.remove(s);
			if(session != null)
				session.sendPacket(new SetNodeAttributePacket(this, "class", String.join(" ", classes)));
		}
	}
	
	/**
	 * Set an attribute of this node. The value may contain single quotes(') as well as double
	 * quotes("), but double quotes must be properly escaped with a backslash(\).
	 * 
	 * @param key   the key of the attribute to be set
	 * @param value the value of the attribute to be set
	 * @throws IllegalArgumentException if key is {@code null} or empty or blank or contains invalid
	 *                                  characters per {@link #isValidAttributeIdentifier(String)}
	 * @throws IllegalArgumentException if value is {@code null}
	 */
	public void setAttribute(String key, String value) {
		if(key == null) throw new IllegalArgumentException("Key cannot be null!");
		if(!isValidAttributeIdentifier(key)) throw new IllegalArgumentException("Key contains invalid characters!");
		if(value == null) throw new IllegalArgumentException("Value cannot be null!");
		attributes.put(key, value);
		if(session != null) session.sendPacket(new SetNodeAttributePacket(this, key, value));
	}
	
	/**
	 * Removes the specified attribute from this node. If the key does not exist, nothing is changed.
	 * 
	 * @param key the key of the attribute to be removed
	 */
	public void removeAttribute(String key) {
		attributes.remove(key);
		if(session != null) session.sendPacket(new RemoveNodeAttributePacket(this, key));
	}
	
	/**
	 * @param key the attribute key to search for
	 * @return whether or not the specified {@code key} is present in the attributes list
	 */
	public boolean hasAttribute(String key) { return attributes.containsKey(key); }
	
	/**
	 * @param key
	 * @return the value of the attribute specified by {@code key} or {@code null} if the attribute does
	 *         not exist
	 */
	public String getAttribute(String key) { return attributes.get(key); }
	
	/**
	 * Set a specific style attribute for this node.
	 * 
	 * @param key
	 * @param value
	 * @throws IllegalArgumentException if {@code key} is {@code null} or contains invalid characters
	 * @throws IllegalArgumentException if {@code value} is {@code null}
	 */
	public void setStyle(String key, String value) {
		if(key == null) throw new IllegalArgumentException("Key cannot be null!");
		if(!isValidAttributeIdentifier(key)) throw new IllegalArgumentException("Key contains invalid characters!");
		if(value == null) throw new IllegalArgumentException("Value cannot be null!");
		styleAttributes.put(key, value);
		if(session != null) session.sendPacket(new SetNodeStyleAttributePacket(this, key, value));
	}
	
	/**
	 * Remove a specific style attribute from this node.
	 * 
	 * @param key
	 */
	public void removeStyle(String key) {
		styleAttributes.remove(key);
		if(session != null) session.sendPacket(new RemoveNodeStyleAttributePacket(this, key));
	}
	
	/**
	 * Attach a child to this node at the specified index. This method may throw a user exception
	 * because of the call to {@link #setSession(WebSession)}.
	 * 
	 * @param at   the index at which this child is to be added
	 * @param node the child node to be attached
	 * @throws IllegalArgumentException if {@code node} is {@code null}
	 * @throws IllegalArgumentException if {@code node} is this node
	 * @throws IllegalArgumentException if {@code at} is less than zero or greater than the children
	 *                                  list size
	 */
	public void addChild(int at, AbstractWebNode node) {
		if(node == null) throw new IllegalArgumentException("Cannot add null node!");
		if(node == this) throw new IllegalArgumentException("A node cannot be added to itself!");
		if(at < 0 || at > children.size()) throw new IllegalArgumentException("Specified index " + at + " is invalid!");
		
		if(node instanceof WebNode) {
			WebNode wnode = (WebNode) node;
			if(wnode.parent != null) throw new IllegalStateException("Node has another parent. Please remove the node from its previous parent!");
			wnode.parent = this;
		}
		
		children.add(at, node);
		if(session != null) session.sendPacket(new AddChildPacket(this, at, node));
		
		if(node instanceof WebNode) {
			WebNode wnode = (WebNode) node;
			wnode.setSession(session);
		}
	}
	
	/**
	 * Attach a child to the end of this node's children. Internally a call to
	 * {@link #addChild(int, AbstractWebNode)} is made.
	 * 
	 * @param node the child node to be attached
	 */
	public void addChild(AbstractWebNode node) { addChild(children.size(), node); }
	
	/**
	 * Remove all children from this node. This is the only way text nodes can be removed from the node.
	 * This method may throw a user exception because of the calls to {@link #setSession(WebSession)}.
	 */
	public void removeChildren() {
		Set<WebNode> set = new HashSet<>();
		for(AbstractWebNode awn : children) {
			if(awn instanceof WebNode) {
				WebNode nd = (WebNode) awn;
				nd.parent = null;
				set.add(nd);
			}
		}
		if(session != null) session.sendPacket(new RemoveNodeChildrenPacket(this));
		children.clear();
		
		for(WebNode nd : set)
			nd.setSession(null);
	}
	
	/**
	 * Removes this node from its parent, and invalidates this node. If the node is not attached to a
	 * parent, nothing is changed. This method may throw a user exception because of the call to
	 * {@link #setSession(WebSession)}.
	 */
	public void remove() {
		if(parent != null) {
			parent.children.remove(this);
			parent = null;
			if(session != null) session.sendPacket(new RemoveNodePacket(this));
			setSession(null);
		}
	}
	
	/**
	 * @return an unmodifiable list of this node's children.
	 */
	public List<AbstractWebNode> getChildren() { return Collections.unmodifiableList(children); }
	
	/**
	 * @return the parent of this node or {@code null} if this node is not a child of a node.
	 */
	public WebNode getParent() { return parent; }
	
	public String getId() { return id; }
	
	/**
	 * @return the session this node is attached to. May be {@code null} if the node is not attached to
	 *         a parent or the page is invalid.
	 */
	public WebSession getSession() { return session; }
	
	/**
	 * Sets the session of this node and its child nodes. This method may throw a user exception because
	 * of the calls to {@link #validate()} or {@link #invalidate()}, resulting in an invalid state.
	 * 
	 * @param session
	 */
	public void setSession(WebSession session) {
		if(this.session == session) return;
		
		if(this.session != null && this.session.getPage() != null) this.session.getPage().uncacheNode(this);
		this.session = session;
		if(this.session != null && this.session.getPage() != null) this.session.getPage().cacheNode(this);
		for(AbstractWebNode awn : children)
			if(awn instanceof WebNode)
				((WebNode) awn).setSession(session);
		
		if(session == null)
			invalidate();
		else
			validate();
	}
	
	/**
	 * Called when this node is attached to a parent(i.e added to the DOM). This is called after the
	 * valid session is set. This method should not throw any exceptions, otherwise it may result in an
	 * invalid internal state.
	 */
	protected void validate() {}
	
	/**
	 * Called when the node is removed from the DOM(i.e detached from parent and session set to
	 * {@code null}). This is called after session is set to {@code null}. Used for removing any
	 * listeners, etc. This method should not throw any exceptions, otherwise it may result in an
	 * invalid internal state.
	 */
	protected void invalidate() {}
	
	/**
	 * Called when an event was fired on this node.
	 * 
	 * @param event
	 */
	protected void event(Event event) {}
	
	@Override
	public String getHTML() {
		String str = "<" + tag + " id='" + id + "'";
		
		if(!classes.isEmpty()) {
			Iterator<String> iter = classes.iterator();
			String s = iter.next();
			while(iter.hasNext())
				s += " " + iter.next();
			str += " class='" + s + "'";
		}
		
		for(Entry<String, String> e : attributes.entrySet())
			str += " " + e.getKey() + " = \"" + e.getValue() + "\"";
		
		if(!styleAttributes.isEmpty()) {
			String s = "";
			for(Entry<String, String> e : styleAttributes.entrySet())
				s += e.getKey() + ": " + e.getValue() + ";";
			str += "style = '" + s + "'";
		}
		
		if(tagOnly) {
			str += " />";
		} else {
			str += ">";
			for(AbstractWebNode awn : children)
				str += awn.getHTML();
			str += "</" + tag + ">";
		}
		return str;
	}
	
	public static boolean isValidAttributeIdentifier(String str) {
		if(str == null || str.isEmpty() || str.isBlank()) return false;
		for(char c : str.toCharArray()) {
			if(Character.isLetter(c) || Character.isDigit(c) || c == '-' || c == '_') continue;
			return false;
		}
		return true;
	}
}
