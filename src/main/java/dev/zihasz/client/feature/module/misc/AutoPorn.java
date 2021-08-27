package dev.zihasz.client.feature.module.misc;

import dev.zihasz.client.feature.module.Category;
import dev.zihasz.client.feature.module.Module;
import dev.zihasz.client.feature.settings.Setting;
import dev.zihasz.client.feature.settings.SettingBuilder;

import java.awt.*;
import java.net.URI;

public class AutoPorn extends Module {

	Setting<Mode> mode = new SettingBuilder<>(Mode.Lesbian).name("Mode").description("Whyt porn do you like?").build(this);

	public AutoPorn() {
		super("AutoPorn", "Opens porn in enw browser", Category.MISC);
	}

	@Override
	public void onEnable() {
		try {
			Desktop.getDesktop().browse(URI.create(String.format("https://www.pornhub.com/video/search?search=%s", mode.getValue().toString().toLowerCase())));
		} catch (Exception ignored) {
		}
		disable();
	}

	private enum Mode {
		Porn,
		Lesbian,
		Step,
		MILF,
		Hentai,
		Feet,
		BDSM,
		Teas,
		Creampie,
		Squirt,
		Gangbang,
		Teen,
		Cumshot,
	}

}
