package dev.zihasz.client.feature.module.player;

import dev.zihasz.client.event.events.PacketEvent;
import dev.zihasz.client.feature.module.Category;
import dev.zihasz.client.feature.module.Module;
import dev.zihasz.client.feature.settings.Setting;
import dev.zihasz.client.feature.settings.SettingBuilder;
import dev.zihasz.client.utils.Timer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PingSpoof extends Module {

	private final Setting<Integer> delay = new SettingBuilder<>(20).name("Delay").description("Delay in millis.").range(0, 1000).build(this);
	private final Queue<Packet<?>> packets = new ConcurrentLinkedQueue<>();
	private final Timer timer = new Timer();
	private boolean receive = true;

	public PingSpoof() {
		super("PingSpoof", "Make your ping look higher. (thx phobos)", Category.PLAYER);
	}

	public static double getIncremental(double val, double inc) {
		double one = 1.0 / inc;
		return (double) Math.round(val * one) / one;
	}

	@Override
	public void onEnable() {
		this.clearQueue();
	}

	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event) {
		this.clearQueue();
	}

	@Override
	public void onDisable() {
		this.clearQueue();
	}

	@SubscribeEvent
	public void onPacketSend(PacketEvent.Send event) {
		if (this.receive && mc.player != null && !mc.isSingleplayer() && mc.player.isEntityAlive() && event.getPacket() instanceof CPacketKeepAlive) {
			this.packets.add(event.getPacket());
			event.cancel();
		}
	}

	public void clearQueue() {
		if (mc.player != null && !mc.isSingleplayer() && mc.player.isEntityAlive() && this.timer.passedMS(this.delay.getValue())) {
			int limit = (int) getIncremental(Math.random() * 10.0, 1.0);
			this.receive = false;
			for (int i = 0; i < limit; i++) {
				Packet<?> packet = this.packets.poll();
				if (packet != null) {
					mc.player.connection.sendPacket(packet);
				}
			}
			this.timer.reset();
			this.receive = true;
		}
	}

}
