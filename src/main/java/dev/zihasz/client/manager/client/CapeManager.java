package dev.zihasz.client.manager.client;

import dev.zihasz.client.manager.Manager;
import dev.zihasz.client.utils.CapeUtil;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CapeManager extends Manager {

	public final static String WHITE = "assets/zware/capes/white_cape.png";
	public final static String BLACK = "assets/zware/capes/black_cape.png";
	public final static String GREEN = "assets/zware/capes/green_cape.png";
	private final String capeURL = "https://pastebin.com/raw/W1Ef0Zpw";
	private Map<UUID, ResourceLocation> capes = new HashMap<>();

	public CapeManager() {
		try {
			capes = CapeUtil.getCapes(capeURL);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean hasCape(UUID uuid) {
		return capes.containsKey(uuid);
	}

	public ResourceLocation getCapeForUUID(UUID uuid) {
		if (!hasCape(uuid)) return null;

		return capes.get(uuid);
	}


}
