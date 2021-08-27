package dev.zihasz.client.feature.module.player;

import dev.zihasz.client.event.events.PacketEvent;
import dev.zihasz.client.feature.module.Category;
import dev.zihasz.client.feature.module.Module;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PortalGodMode extends Module {

	public PortalGodMode() {
		super("PortalGodMode", "Makes you invincible while in portals, might cause de-sync/rubber-banding.", Category.PLAYER);
	}

	@SubscribeEvent
	public void onPacketSend(PacketEvent.Send event) {
		if (event.getPacket() instanceof CPacketConfirmTeleport)
			event.cancel();
	}

}