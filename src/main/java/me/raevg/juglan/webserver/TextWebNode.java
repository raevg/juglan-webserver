package me.raevg.juglan.webserver;

public class TextWebNode extends AbstractWebNode {
	private String content;
	
	public TextWebNode(String content) {
		if(content == null) throw new IllegalArgumentException("Text node text cannot be null!");
		this.content = content;
	}
	
	@Override
	public String getHTML() { return content; }
}
