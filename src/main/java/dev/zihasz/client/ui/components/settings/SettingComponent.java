package dev.zihasz.client.ui.components.settings;

import dev.zihasz.client.feature.settings.Setting;
import dev.zihasz.client.ui.components.Component;

public abstract class SettingComponent<T> implements Component {

	protected final Setting<T> setting;
	protected int x, y, width, height;

	public SettingComponent(Setting<T> setting, int x, int y, int width, int height) {
		this.setting = setting;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

}
