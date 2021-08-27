package dev.zihasz.client.utils;

import dev.zihasz.client.manager.client.CapeManager;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;
import java.util.UUID;

public class CapeUtil implements Util {

	public static HashMap<UUID, ResourceLocation> getCapes(String pastebinURL) throws IOException {

		HashMap<UUID, ResourceLocation> outMap = new HashMap<>();

		URL url = new URL(pastebinURL);
		Scanner scanner = new Scanner(url.openConnection().getInputStream());

		while (scanner.hasNextLine()) {
			String[] splitLine = scanner.nextLine().split(" ");
			if (splitLine.length < 2)
				throw new IllegalStateException("Incorrect cape line found");

			switch (splitLine[1]) {
				case "WHITE":
					outMap.put(UUID.fromString(splitLine[0]), new ResourceLocation(CapeManager.WHITE));
					break;
				case "BLACK":
					outMap.put(UUID.fromString(splitLine[0]), new ResourceLocation(CapeManager.BLACK));
					break;
				case "GREEN":
					outMap.put(UUID.fromString(splitLine[0]), new ResourceLocation(CapeManager.GREEN));
					break;
			}
		}

		return outMap;
	}
}