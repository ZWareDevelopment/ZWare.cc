package dev.zihasz.client.feature.module.misc;

import dev.zihasz.client.event.events.PacketEvent;
import dev.zihasz.client.feature.module.Category;
import dev.zihasz.client.feature.module.Module;
import dev.zihasz.client.utils.MessageBus;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerAbilities;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PacketLogger extends Module {

	public PacketLogger() {
		super("PacketLogger", "Logs outgoing packets", Category.MISC);
	}

	@SubscribeEvent
	public void onPacketSend(PacketEvent.Send event) {
		Packet<?> raw = event.getPacket();
		if (raw instanceof CPacketPlayer) {
			CPacketPlayer packet = (CPacketPlayer) raw;
			MessageBus.sendDebugMessage("PacketPlayer: ");
			MessageBus.sendDebugMessage(String.format("  X: %s, Y: %s, Z: %s", packet.x, packet.y, packet.z));
			MessageBus.sendDebugMessage(String.format("  Yaw: %s, Pitch: %s", packet.yaw, packet.pitch));
			MessageBus.sendDebugMessage(String.format("  OnGround: %s", packet.onGround));
		}
		if (raw instanceof CPacketPlayerAbilities) {
			CPacketPlayerAbilities packet = (CPacketPlayerAbilities) raw;
			MessageBus.sendDebugMessage("PacketPlayerAbilities: ");
			MessageBus.sendDebugMessage(String.format("  AllowFly: %s, Flying: %s", packet.isAllowFlying(), packet.isFlying()));
			MessageBus.sendDebugMessage(String.format("  Invuln: %s, Creative: %s", packet.isInvulnerable(), packet.isCreativeMode()));

		}
		if (raw instanceof CPacketConfirmTransaction) {
			CPacketConfirmTransaction packet = (CPacketConfirmTransaction) raw;
			MessageBus.sendDebugMessage("PacketConfirmTransaction: ");
			MessageBus.sendDebugMessage(String.format("  WID: %s, UID: %s", packet.getWindowId(), packet.getUid()));
		}
		if (raw instanceof CPacketConfirmTeleport) {
			CPacketConfirmTeleport packet = (CPacketConfirmTeleport) raw;
			MessageBus.sendDebugMessage("PacketConfirmTeleport: ");
			MessageBus.sendDebugMessage(String.format("  TID: %s", packet.getTeleportId()));
		}
	}

}
