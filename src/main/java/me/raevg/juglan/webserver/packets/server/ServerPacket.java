package me.raevg.juglan.webserver.packets.server;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.github.cliftonlabs.json_simple.JsonObject;

public abstract class ServerPacket {
	public String toJSON() {
		try {
			JsonObject json = new JsonObject();
			json.put("packet", getClass().getSimpleName());
			Class<?> clazz = this.getClass();
			while(ServerPacket.class.isAssignableFrom(clazz)) {
				for(Field f : clazz.getDeclaredFields()) {
					f.trySetAccessible();
					if(!Modifier.isTransient(f.getModifiers()) && !Modifier.isStatic(f.getModifiers())) {
						if(json.containsKey(f.getName())) throw new RuntimeException("Class " + getClass().getName() + " or superclasses contain fields with duplicate names!");
						json.put(f.getName(), f.get(this));
					}
				}
				clazz = clazz.getSuperclass();
			}
			return json.toJson();
		} catch(Exception e) {
			throw new RuntimeException("Error trying to serialize packet!", e);
		}
	}
}
