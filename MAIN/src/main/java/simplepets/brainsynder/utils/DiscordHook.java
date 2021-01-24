package simplepets.brainsynder.utils;

import lib.brainsynder.json.JsonObject;
import simplepets.brainsynder.PetCore;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class DiscordHook {

	private final String url;
	private String content;
	private String username;
	private String avatarUrl;
	private final PetCore plugin;

	public DiscordHook(PetCore pl, String url) {
		this.plugin = pl;
		this.url = url;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public void send() {
		JsonObject json = new JsonObject();
		json.add("content", this.content);
		json.add("username", this.username);
		json.add("avatar_url", this.avatarUrl);
		CompletableFuture.runAsync(() -> {

			try {
				URL u = new URL(url);
				HttpsURLConnection connection = (HttpsURLConnection) u.openConnection();
				connection.addRequestProperty("Content-Type", "application/json");
				connection.addRequestProperty("User-Agent", "StaffUtils");
				connection.setDoOutput(true);
				connection.setRequestMethod("POST");

				OutputStream stream = connection.getOutputStream();
				stream.write(json.toString().getBytes());
				stream.flush();
				stream.close();

				connection.getInputStream().close();
				connection.disconnect();
			}catch(IOException e) {}
		});
	}
}

