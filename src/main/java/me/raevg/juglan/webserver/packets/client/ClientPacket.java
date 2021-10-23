package me.raevg.juglan.webserver.packets.client;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

public abstract class ClientPacket {
	private static final Set<Class<? extends ClientPacket>> registredPackets = new HashSet<>();
	
	static {
		registredPackets.add(MouseClickEventPacket.class);
		registredPackets.add(MouseUpEventPacket.class);
		registredPackets.add(MouseDownEventPacket.class);
		registredPackets.add(KeyPressEventPacket.class);
		registredPackets.add(KeyUpEventPacket.class);
		registredPackets.add(ValueChangeEventPacket.class);
		registredPackets.add(ScrollEventPacket.class);
	}
	
	public static ClientPacket fromJSON(String string) {
		try {
			JsonObject json = (JsonObject) Jsoner.deserialize(string);
			String packetType = (String) json.get("packet");
			
			Class<?> clazz = null;
			for(Class<? extends ClientPacket> c : registredPackets) {
				if(c.getSimpleName().equals(packetType)) {
					clazz = c;
					break;
				}
			}
			if(clazz == null) throw new RuntimeException("Packet type " + packetType + " is not recognized!");
			
			Object obj = clazz.getDeclaredConstructor().newInstance();
			while(ClientPacket.class.isAssignableFrom(clazz)) {
				for(Field f : clazz.getDeclaredFields()) {
					f.trySetAccessible();
					if(!Modifier.isTransient(f.getModifiers()) && !Modifier.isStatic(f.getModifiers())) {
						f.set(obj, json.get(f.getName()));
					}
				}
				clazz = clazz.getSuperclass();
			}
			
			return (ClientPacket) obj;
		} catch(Exception e) {
			throw new RuntimeException("Error trying to deserialize packet!", e);
		}
	}
}
