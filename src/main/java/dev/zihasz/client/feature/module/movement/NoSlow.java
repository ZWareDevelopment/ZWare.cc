package dev.zihasz.client.feature.module.movement;

import dev.zihasz.client.feature.module.Category;
import dev.zihasz.client.feature.module.Module;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoSlow extends Module {

	public NoSlow() {
		super("NoSlow", "Makes you unslow.", Category.MOVEMENT);
	}

	@SubscribeEvent
	public void onInput(InputUpdateEvent event) {
		if (mc.player.isHandActive() && !mc.player.isRiding()) {
			event.getMovementInput().moveForward *= 5;
			event.getMovementInput().moveStrafe *= 5;
		}
	}

}
