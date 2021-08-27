package dev.zihasz.client.feature.module.render;

import dev.zihasz.client.feature.module.Category;
import dev.zihasz.client.feature.module.Module;
import dev.zihasz.client.utils.PlayerUtil;
import dev.zihasz.client.utils.Renderer2D;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;

public class HealthIndicator extends Module {

	private final ScaledResolution sr = new ScaledResolution(mc);

	public HealthIndicator() {
		super("Health", "Shows how much health you have", Category.RENDER);
	}

	@SubscribeEvent
	public void onRender2D(TickEvent.RenderTickEvent event) {
		Renderer2D.drawCircle(sr.getScaledWidth() / 2, sr.getScaledHeight() / 2, radius(), new Color(0xffff9898));
	}

	private float radius() {
		float r = PlayerUtil.getFullHealth() * 2;
		return r == 0 ? 1 : r;
	}

}
