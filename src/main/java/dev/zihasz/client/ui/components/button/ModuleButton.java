package dev.zihasz.client.ui.components.button;

import dev.zihasz.client.feature.module.Module;
import dev.zihasz.client.feature.settings.Setting;
import dev.zihasz.client.ui.components.Component;
import dev.zihasz.client.ui.components.settings.CheckboxComponent;
import dev.zihasz.client.ui.components.settings.SettingComponent;
import dev.zihasz.client.utils.render.Renderer2D;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ModuleButton implements Component {

	private final List<SettingComponent<?>> settings = new ArrayList<>();

	private final Module module;
	private int x, y, width, height;

	private int offset = 0;
	private boolean open = false;

	public ModuleButton(Module module, int x, int y, int width, int height) {
		this.module = module;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		int so = height;
		for (Setting<?> setting : module.getSettings()) {
			if (setting.value instanceof Boolean) {
				settings.add(new CheckboxComponent((Setting<Boolean>) setting, x, y + so, width, height));
				so += height;
			}
		}
	}

	@Override
	public void render(int mx, int my) {
		// Renderer2D.drawRectangle(x, y, width, height, new Color(0xff3f3f3f));
		Renderer2D.drawGradientRectangle(x, y, width, height, new Color(0xff3f3f3f), new Color(0xff4f4f4f));
		fr.drawString(module.name, x + 3.5f, y + (height / 2f) - HALF_FONT, module.enabled ? 0xff98ff98 : 0xf3f3f3, true);
		fr.drawString(open ? "V" : ">", x + width - fr.getStringWidth(open ? "V" : ">") - 3.5f, y + (height / 2f) - HALF_FONT, 0xf3f3f3, true);

		if (open) {
			for (Component component : settings) {
				component.render(mx, my);
			}
		}
	}

	@Override
	public void onMouseClicked(int mx, int my, int button) {
		if (hovered(mx, my)) {
			if (button == 0) {
				module.setEnabled(!module.isEnabled());
			}
			if (button == 1) {
				open = !open;
			}
		}

		if (open) {
			for (Component component : settings) {
				component.onMouseClicked(mx, my, button);
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
		int out = height;

		if (open) {
			for (Component component : settings) {
				out += component.height();
			}
		}

		return out;
	}

	@Override
	public void setOffset(int offset) {
		this.offset = offset;
	}

	public boolean hovered(int mx, int my) {
		return contains(mx, my, x, y, width, height);
	}

}
