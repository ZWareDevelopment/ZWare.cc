package dev.zihasz.client.feature.module.player;

import dev.zihasz.client.event.events.PacketEvent;
import dev.zihasz.client.feature.module.Category;
import dev.zihasz.client.feature.module.Module;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoFall extends Module {

	public NoFall() {
		super("NoFall", "Take no fall damage like a god.", Category.PLAYER);
	}

	@SubscribeEvent
	public void onPackerSend(PacketEvent.Read event) {
		if (event.getPacket() instanceof CPacketPlayer && mc.player.fallDistance >= 6) {
			CPacketPlayer packet = (CPacketPlayer) event.getPacket();
			packet.onGround = true;
		}
	}

}

