package me.raevg.juglan.webserver.event;

public class KeyEvent extends Event {
	public static final int	KEY_UNIDENTIFIED	= 1;
	public static final int	KEY_ALT				= 2;
	public static final int	KEY_ALTGR			= 3;
	public static final int	KEY_CAPS_LOCK		= 4;
	public static final int	KEY_CONTROL			= 5;
	public static final int	KEY_META			= 6;
	public static final int	KEY_SHIFT			= 7;
	public static final int	KEY_ENTER			= 8;
	public static final int	KEY_TAB				= 9;
	public static final int	KEY_SPACE_BAR		= 10;
	public static final int	KEY_ARROW_DOWN		= 11;
	public static final int	KEY_ARROW_LEFT		= 12;
	public static final int	KEY_ARROW_RIGHT		= 13;
	public static final int	KEY_ARROW_UP		= 14;
	public static final int	KEY_END				= 15;
	public static final int	KEY_HOME			= 16;
	public static final int	KEY_PAGE_DOWN		= 17;
	public static final int	KEY_PAGE_UP			= 18;
	public static final int	KEY_BACKSPACE		= 19;
	public static final int	KEY_DELETE			= 20;
	public static final int	KEY_INSERT			= 21;
	public static final int	KEY_CONTEXT_MENU	= 22;
	public static final int	KEY_ESCAPE			= 23;
	
	public static final int	KEY_F1	= 24;
	public static final int	KEY_F2	= 25;
	public static final int	KEY_F3	= 26;
	public static final int	KEY_F4	= 27;
	public static final int	KEY_F5	= 28;
	public static final int	KEY_F6	= 29;
	public static final int	KEY_F7	= 30;
	public static final int	KEY_F8	= 31;
	public static final int	KEY_F9	= 32;
	public static final int	KEY_F10	= 33;
	public static final int	KEY_F11	= 34;
	public static final int	KEY_F12	= 35;
	public static final int	KEY_F13	= 36;
	public static final int	KEY_F14	= 37;
	public static final int	KEY_F15	= 38;
	public static final int	KEY_F16	= 39;
	public static final int	KEY_F17	= 40;
	public static final int	KEY_F18	= 41;
	public static final int	KEY_F19	= 42;
	public static final int	KEY_F20	= 43;
	
	private String	key;
	private boolean	altDown;
	private boolean	shiftDown;
	private boolean	controlDown;
	
	public KeyEvent(String key, boolean altDown, boolean shiftDown, boolean controlDown) {
		this.key = key;
		this.altDown = altDown;
		this.shiftDown = shiftDown;
		this.controlDown = controlDown;
	}
	
	public String getKey() { return key; }
	
	public int decodeSpecialKey() {
		switch(key) {
			case "Alt":
				return KEY_ALT;
			
			case "AltGraph":
				return KEY_ALTGR;
			
			case "CapsLock":
				return KEY_CAPS_LOCK;
			
			case "Control":
				return KEY_CONTROL;
			
			case "Meta":
			case "OS":
				return KEY_META;
			
			case "Shift":
				return KEY_SHIFT;
			
			case "Enter":
				return KEY_ENTER;
			
			case "Tab":
				return KEY_TAB;
			
			case " ":
			case "Spacebar":
				return KEY_SPACE_BAR;
			
			case "ArrowDown":
			case "Down":
				return KEY_ARROW_DOWN;
			
			case "ArrowLeft":
			case "Left":
				return KEY_ARROW_LEFT;
			
			case "ArrowRight":
			case "Right":
				return KEY_ARROW_RIGHT;
			
			case "ArrowUp":
			case "Up":
				return KEY_ARROW_UP;
			
			case "End":
				return KEY_END;
			
			case "Home":
				return KEY_HOME;
			
			case "PageDown":
				return KEY_PAGE_DOWN;
			
			case "PageUp":
				return KEY_PAGE_UP;
			
			case "Backspace":
				return KEY_BACKSPACE;
			
			case "Delete":
			case "Del":
				return KEY_DELETE;
			
			case "Insert":
				return KEY_INSERT;
			
			case "ContextMenu":
			case "Apps":
				return KEY_CONTEXT_MENU;
			
			case "Escape":
			case "Esc":
				return KEY_ESCAPE;
			
			case "F1":
				return KEY_F1;
			case "F2":
				return KEY_F2;
			case "F3":
				return KEY_F3;
			case "F4":
				return KEY_F4;
			case "F5":
				return KEY_F5;
			case "F6":
				return KEY_F6;
			case "F7":
				return KEY_F7;
			case "F8":
				return KEY_F8;
			case "F9":
				return KEY_F9;
			case "F10":
				return KEY_F10;
			case "F11":
				return KEY_F11;
			case "F12":
				return KEY_F12;
			case "F13":
				return KEY_F13;
			case "F14":
				return KEY_F14;
			case "F15":
				return KEY_F15;
			case "F16":
				return KEY_F16;
			case "F17":
				return KEY_F17;
			case "F18":
				return KEY_F18;
			case "F19":
				return KEY_F19;
			case "F20":
				return KEY_F20;
			
			case "Unidentified":
			default:
				return KEY_UNIDENTIFIED;
		}
	}
	
	public boolean isAltDown() { return altDown; }
	
	public boolean isShiftDown() { return shiftDown; }
	
	public boolean isControlDown() { return controlDown; }
}
