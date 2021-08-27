package dev.zihasz.client.feature.module.misc;

import dev.zihasz.client.Client;
import dev.zihasz.client.event.events.PacketEvent;
import dev.zihasz.client.feature.module.Category;
import dev.zihasz.client.feature.module.Module;
import dev.zihasz.client.utils.PacketUtils;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FakeLunar extends Module {

	public FakeLunar() {
		super("FakeLunar", "Fakes lunar client.", Category.MISC);
	}

	@Override
	public void onEnable() {
		Client.moduleManager.getModule(FakeVanilla.class).enable();
	}

	@SubscribeEvent
	public void onPacketSend(PacketEvent.Send event) {
		Packet<?> raw = event.getPacket();
		if (raw instanceof CPacketCustomPayload) {
			CPacketCustomPayload packet = (CPacketCustomPayload) raw;
			if (packet.getChannelName().equalsIgnoreCase("MC|Brand")) {
				mc.player.connection.sendPacket(PacketUtils.generatePayload("REGISTER", "Lunar-Client"));
			}
		}
	}

}
