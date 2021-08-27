package dev.zihasz.client.ui.components.settings;

import dev.zihasz.client.feature.settings.Setting;
import dev.zihasz.client.utils.Renderer2D;

import java.awt.*;

public class CheckboxComponent extends SettingComponent<Boolean> {

	private int offset = 0;
	private boolean open = true;

	public CheckboxComponent(Setting<Boolean> setting, int x, int y, int width, int height) {
		super(setting, x, y, width, height);
	}

	@Override
	public void render(int mx, int my) {
		Renderer2D.drawGradientRectangle(x, y, width, height, new Color(0xff3f3f3f), new Color(0xff4f4f4f));
		Renderer2D.drawRectangle(x + width - 15, y + 5, 10, 10, setting.value ? new Color(0xff98ff98) : new Color(0xff5f5f5f));
		fr.drawString(setting.getName(), x + 3.5f, y + (height / 2f) - HALF_FONT, setting.value ? 0xff98ff98 : 0xf3f3f3, true);
	}

	@Override
	public void onMouseClicked(int mx, int my, int button) {
		if (hovered(mx, mx)) {
			if (button == 0) {
				setting.setValue(!setting.getValue());
			}
			if (button == 1) {
				open = !open;
			}
		}
	}

	@Override
	public void onMouseReleased(int mx, int my, int button) {

	}

	@Override
	public void onKeyTyped(int key, char character) {

	}

	@Override
	public int height() {
		return height;
	}

	@Override
	public void setOffset(int offset) {
		this.offset = offset;
	}

	public boolean hovered(int mx, int my) {
		return contains(mx, my, x, y, width, height);
	}

}
