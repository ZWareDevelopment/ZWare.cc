package dev.zihasz.client.feature.module.player;

import dev.zihasz.client.feature.module.Category;
import dev.zihasz.client.feature.module.Module;
import dev.zihasz.client.utils.PacketUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.lang3.RandomStringUtils;

public class AntiModDetect extends Module {

	public AntiModDetect() {
		super("AntiModDetect", "Makes it so admins cant see what mods you are using", Category.PLAYER);
	}

	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event) {
		if (mc.player.ticksExisted % 5 == 0) {
			mc.player.connection.sendPacket(PacketUtils.generatePayload("MC|Brand", RandomStringUtils.random(1000)));
		}
	}
}
