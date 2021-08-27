package dev.zihasz.client.ui.components;

import dev.zihasz.client.utils.Util;
import net.minecraft.client.gui.FontRenderer;

public interface Component extends Util {

	float HALF_FONT = 3.5f;
	FontRenderer fr = mc.fontRenderer;

	void render(int mx, int my);

	void onMouseClicked(int mx, int my, int button);

	void onMouseReleased(int mx, int my, int button);

	void onKeyTyped(int key, char character);

	int height();

	void setOffset(int offset);

	default boolean contains(int mx, int my, int x, int y, int width, int height) {
		return x < mx && mx < x + width && y < my && my < y + height;
	}

}
