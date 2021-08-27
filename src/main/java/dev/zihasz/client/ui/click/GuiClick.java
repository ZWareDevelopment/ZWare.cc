package dev.zihasz.client.ui.click;

import dev.zihasz.client.feature.module.Category;
import dev.zihasz.client.ui.components.frame.Frame;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiClick extends GuiScreen {

	protected final List<Frame> frames = new ArrayList<>();

	public GuiClick() {
		int i = 20;
		for (Category category : Category.values()) {
			frames.add(new Frame(category, i, 20, 100, 20));
			i += 120;
		}
	}

	@Override
	public void initGui() {
		super.initGui();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		for (Frame frame : frames)
			frame.render(mouseX, mouseY);

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		for (Frame frame : frames)
			frame.onMouseClicked(mouseX, mouseY, mouseButton);

		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		for (Frame frame : frames)
			frame.onMouseReleased(mouseX, mouseY, state);

		super.mouseReleased(mouseX, mouseY, state);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		for (Frame frame : frames)
			frame.onKeyTyped(keyCode, typedChar);

		super.keyTyped(typedChar, keyCode);
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
	}

}
