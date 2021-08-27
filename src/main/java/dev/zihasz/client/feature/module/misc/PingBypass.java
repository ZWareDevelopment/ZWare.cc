package dev.zihasz.client.feature.module.misc;

import dev.zihasz.client.feature.module.Category;
import dev.zihasz.client.feature.module.Module;
import dev.zihasz.client.feature.settings.Setting;
import dev.zihasz.client.feature.settings.SettingBuilder;

public class PingBypass extends Module {

	private final Setting<Region> region = new SettingBuilder<>(Region.US).name("Region").description("What region is the server in that you want to play?").build(this);
	private final Setting<Boolean> detect = new SettingBuilder<>(true).name("Detect").description("Automagically detects what region is the server in.").build(this);

	public PingBypass() {
		super("PingBypass", ":flushed:", Category.CLIENT);
	}

	private enum Region {
		EU,
		UK,
		US,
		SA,
		AS,
	}

}
