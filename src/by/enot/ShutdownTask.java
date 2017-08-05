package by.enot;

import java.io.IOException;
import java.util.TimerTask;

/**
 * Created by sintetic on 13.08.2016.
 */
public class ShutdownTask extends TimerTask {
	public void run() {
		ProcessBuilder shutdown = new ProcessBuilder(
				"shutdown", "/p");
		try {
			shutdown.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
}
