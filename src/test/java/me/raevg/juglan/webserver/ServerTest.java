package me.raevg.juglan.webserver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import me.raevg.juglan.webserver.JuglanWebServer;
import me.raevg.juglan.webserver.WebNode;
import me.raevg.juglan.webserver.WebPage;
import me.raevg.juglan.webserver.JuglanWebServer.Configuration;

public class ServerTest {

	public static void main(String[] args) throws InterruptedException, FileNotFoundException, IOException {
		Configuration c = new Configuration();
		c.setHTTPPort(8000);
		c.setRequestPage("/", SamplePage.class);
		JuglanWebServer.start(c);
		new CountDownLatch(1).await();
	}

	public static class SamplePage extends WebPage {
		@Override
		protected void init() {
			setTitle("Example theme page");
			WebNode but = new WebNode("textarea");
			getBody().addChild(but);
		}
	}
}
