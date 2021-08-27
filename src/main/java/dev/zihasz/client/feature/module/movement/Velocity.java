package dev.zihasz.client.feature.module.movement;

import dev.zihasz.client.event.events.PacketEvent;
import dev.zihasz.client.event.events.PushEvent;
import dev.zihasz.client.feature.module.Category;
import dev.zihasz.client.feature.module.Module;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Velocity extends Module {

	public Velocity() {
		super("Velocity", "Makes you not take knockback", Category.MOVEMENT);
	}

	@SubscribeEvent
	public void onPacketRead(PacketEvent.Read event) {
		Packet<?> rawPacket = event.getPacket();
		if (rawPacket instanceof SPacketEntityVelocity) {
			SPacketEntityVelocity packet = (SPacketEntityVelocity) rawPacket;
			if (packet.entityID == mc.player.entityId) event.cancel();
		}
		if (rawPacket instanceof SPacketExplosion) {
			event.cancel();
		}
	}

	@SubscribeEvent
	public void onPush(PushEvent event) {
		event.cancel();
	}

}
