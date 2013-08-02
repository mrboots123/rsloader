package org.kenneh.loader.net;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.kenneh.loader.utils.Logger;


public class Parameters {

	private Map<String, String> params = new HashMap<>();
	private boolean oldschool;
	private Random random;

	private Logger log;

	public Parameters(boolean oldschool, Logger log) {
		this.oldschool = oldschool;
		this.random = new Random();
		this.log = log;
	}

	public String get(String key) {
		return params.containsKey(key) ? params.get(key) : "";
	}

	private void addParam(String key) {
		addParam(key, "");
	}

	private void addParam(String key, String value) {
		params.put(key, value);
	}

	private URLConnection getURLConnection(final boolean oldschoolscape) throws MalformedURLException, IOException {
		final StringBuilder builder = new StringBuilder("http://");
		builder.append(oldschool ? "oldschool" : "world");
		builder.append(random.nextInt(100));
		builder.append(".runescape.com/l=0/jav_config.ws");
		final String link = builder.toString();

		log.debug("Connecting to " + link);
		final URLConnection connection = new URL(link).openConnection();
		connection.addRequestProperty("User-Agent", "Opera/9.80 (Windows NT 6.1; U; en-GB) Presto/2.10.229 Version/11.61");
		return connection;
	}

	public void download() {
		try {
			URL website = new URL(params.get("codebase") + params.get("initial_jar"));
			ReadableByteChannel rbc = Channels.newChannel(website.openStream());
			FileOutputStream fos = new FileOutputStream(System.getProperty("java.io.tmpdir") + File.separator + "Runescape.jar");
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			fos.close();
		} catch(Exception a) {
			a.printStackTrace();
		}
	}

	public boolean parse() {
		try {
			log.debug("Attempting to parse game parameters...");
			final URLConnection connection = getURLConnection(oldschool);
			final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			String inputLine;
			while ((inputLine = reader.readLine()) != null) {
				inputLine = inputLine.replaceAll("\">'", "\"").replaceAll("'", "")
						.replaceAll("\\(", "").replaceAll("\\)", "")
						.replaceAll("\"", "").replaceAll(" ", "")
						.replaceAll("param=", "")
						.replaceAll(";", "").replaceAll("value", "");
				final String[] splitted = inputLine.split("=");
				if (splitted.length == 1) {
					addParam(splitted[0]);
				} else if (splitted.length == 2) {
					addParam(splitted[0], splitted[1]);
				} else if (splitted.length == 3) {
					addParam(splitted[0], splitted[1] + "=" + splitted[2]);
				} else if (splitted.length == 4) {
					addParam(splitted[0], splitted[1] + "=" + splitted[2] + "=" + splitted[3]);
				}
			}

			reader.close();
		} catch (final UnknownHostException e) {
			log.debug("The world we are trying to load does not exist... trying a new world!");
			download();
		} catch (final IOException e) {
			e.printStackTrace();
			return false;
		}
		log.debug("Parsed a total of " + params.size() + " parameters");
		return true;
	}
}
