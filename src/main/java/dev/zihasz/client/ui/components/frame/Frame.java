package dev.zihasz.client.ui.components.frame;

import dev.zihasz.client.Client;
import dev.zihasz.client.feature.module.Category;
import dev.zihasz.client.feature.module.Module;
import dev.zihasz.client.ui.components.Component;
import dev.zihasz.client.ui.components.button.ModuleButton;
import dev.zihasz.client.utils.Renderer2D;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Frame implements Component {

	private final Category category;
	private final String title;
	private final List<ModuleButton> modules = new ArrayList<>();
	private final int x;
	private final int y;
	private final int width;
	private final int height;

	private int offset = 0;
	private boolean open = true;
	private boolean drag = false;

	public Frame(Category category, int x, int y, int width, int height) {
		this.category = category;
		this.title = this.category.display();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		int myo = height;
		for (Module module : Client.moduleManager.getModules(category)) {

			modules.add(new ModuleButton(module, x, y + myo, width, height));
			myo += height;

		}
	}

	@Override
	public void render(int mx, int my) {
		Renderer2D.drawGradientRectangle(x, y + offset, width, height, new Color(0xff3f3f3f), new Color(0xff4f4f4f));
		fr.drawString(title, x + (width / 2f) - (fr.getStringWidth(title) / 2f), y + offset + (height / 2f) - (HALF_FONT), 0xf3f3f3, true);

		int off = height;
		for (Component component : modules) {
			component.setOffset(off);
			off += component.height();

			if (open)
				component.render(mx, my);
		}

		Renderer2D.drawRectangleOutline(x, y + offset, width, height(), 2f, new Color(0xff98ff98));
		Renderer2D.drawRectangleOutline(x, y + offset, width, height, 2f, new Color(0xff98ff98));
	}

	@Override
	public void onMouseClicked(int mx, int my, int button) {
		if (hovered(mx, my)) {
			if (button == 0) {
				// TODO: Handle dragging
				drag = true;
			}
			if (button == 1) {
				open = !open;
			}
		}

		if (open) {
			for (ModuleButton moduleButton : modules) {
				moduleButton.onMouseClicked(mx, my, button);
			}
		}
	}

	@Override
	public void onMouseReleased(int mx, int my, int button) {
		drag = false;

		if (open) {
			for (ModuleButton moduleButton : modules) {
				moduleButton.onMouseReleased(mx, my, button);
			}
		}
	}

	@Override
	public void onKeyTyped(int key, char character) {
		if (open) {
			for (ModuleButton button : modules) {
				button.onKeyTyped(key, character);
			}
		}
	}

	@Override
	public int height() {
		int out = height;

		if (open)
			for (Component component : modules)
				out += component.height();

		return out;
	}

	@Override
	public void setOffset(int offset) {
		this.offset = offset;
	}

	public boolean hovered(int mx, int my) {
		return contains(mx, my, x, y + offset, width, height);
	}

}
