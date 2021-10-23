package me.raevg.juglan.webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.glassfish.tyrus.server.Server;

public class JuglanWebServer {
	private static boolean			started	= false;
	private static Configuration	config;
	
	public static void start(Configuration config) {
		if(started) throw new IllegalStateException("Servers already started!");
		started = true;
		JuglanWebServer.config = config;
		
		Thread t = new Thread(() -> {
			String page2 = "";
			try(BufferedReader br = new BufferedReader(new InputStreamReader(JuglanWebServer.class.getClassLoader().getResourceAsStream("juglan-webserver/webpage.html")))) {
				String line;
				while((line = br.readLine()) != null)
					page2 += line + "\n";
			} catch(IOException e1) {
				throw new RuntimeException(e1);
			}
			
			page2 = page2.replace("%WSPORT%", String.valueOf(config.websocketPort));
			
			final String page = page2;
			
			HttpServer server = HttpServer.createSimpleServer(null, config.httpPort);
			server.getServerConfiguration().addHttpHandler(new HttpHandler() {
				public void service(Request request, Response response) throws Exception {
					final SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
					final String date = format.format(new Date(System.currentTimeMillis()));
					response.setContentType("text/plain");
					response.setContentLength(date.length());
					response.getWriter().write(date);
				}
			}, "/time");
			server.getServerConfiguration().addHttpHandler(new HttpHandler() {
				@Override
				public void service(Request request, Response response) throws Exception {
					response.setContentType("text/html");
					response.setContentLength(page.length());
					response.getWriter().write(page);
				}
			}, "/");
			server.getServerConfiguration().addHttpHandler(new HttpHandler() {
				@Override
				public void service(Request request, Response response) throws Exception {
					if(request.getRequestURI().length() >= 13) {
						String path = request.getRequestURI().substring(13);
						InputStream stream = null;
						for(ClassLoader clazz : config.resourceLoaders) {
							stream = clazz.getResourceAsStream(path);
							if(stream != null) break;
						}
						if(stream != null) {
							String mimeType = URLConnection.guessContentTypeFromName(path);
							byte[] content = stream.readAllBytes();
							response.setContentType(mimeType);
							response.setContentLength(content.length);
							response.setStatus(200);
							response.getOutputStream().write(content);
						} else {
							response.setStatus(404);
							response.setContentType("text/html");
							String resp = "<h2>Not Found</h2>";
							response.setContentLength(resp.length());
							response.getWriter().write(resp);
						}
					} else {
						response.setStatus(404);
						response.setContentType("text/html");
						String resp = "<h2>Not Found</h2>";
						response.setContentLength(resp.length());
						response.getWriter().write(resp);
					}
				}
			}, "/internalres");
			
			Server wsserver = new Server("localhost", config.websocketPort, "", null, MyWsendpoint.class);
			
			try {
				server.start();
				wsserver.start();
				new CountDownLatch(1).await();
			} catch(Exception e) {
				e.printStackTrace();
				server.shutdownNow();
				wsserver.stop();
			}
		});
		t.setDaemon(true);
		t.start();
	}
	
	public static Configuration getConfig() { return config; }
	
	public static class Configuration {
		private int										httpPort		= 8000;
		private int										websocketPort	= 8080;
		private Map<String, Class<? extends WebPage>>	requestPages	= new HashMap<>();
		private Set<ClassLoader>						resourceLoaders	= new HashSet<>();
		
		public Configuration() { resourceLoaders.add(JuglanWebServer.class.getClassLoader()); }
		
		public int getHTTPPort() { return httpPort; }
		
		public void setHTTPPort(int httpPort) { this.httpPort = httpPort; }
		
		public int getWebsocketPort() { return websocketPort; }
		
		public void setWebsocketPort(int websocketPort) { this.websocketPort = websocketPort; }
		
		public void setRequestPage(String context, Class<? extends WebPage> clazz) {
			if(context == null) throw new IllegalArgumentException("Request context cannot be null!");
			if(!context.startsWith("/")) throw new IllegalArgumentException("Request context must start with a slash(/)!");
			if(clazz == null) throw new IllegalArgumentException("Web page class cannot be null!");
			if(!clazz.getModule().isOpen(clazz.getPackageName(), getClass().getModule()))
				throw new IllegalArgumentException("Package `" + clazz.getPackageName() + "` of module `" + clazz.getModule().getName() + "` is not open to `" + getClass().getModule().getName() + "`!");
			
			requestPages.put(context, clazz);
		}
		
		public Map<String, Class<? extends WebPage>> getRequestPages() { return Collections.unmodifiableMap(requestPages); }
		
		public void addResourceLoader(ClassLoader clazz) {
			if(clazz == null) throw new IllegalArgumentException("ResourceLoader cannot be null!");
			resourceLoaders.add(clazz);
		}
	}
}
