package dev.zihasz.client.feature.module.misc;

import dev.zihasz.client.event.events.PacketEvent;
import dev.zihasz.client.feature.module.Category;
import dev.zihasz.client.feature.module.Module;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

public class FakeVanilla extends Module {

	public FakeVanilla() {
		super("FakeVanilla", "Fakes vanilla to servers.", Category.MISC);
	}

	@SubscribeEvent
	public void onPacketSend(PacketEvent.Send event) {
		if (event.getPacket() instanceof FMLProxyPacket && !mc.isSingleplayer())
			event.cancel();
	}

}
