package dev.zihasz.client.feature.module.player;

import dev.zihasz.client.event.events.PacketEvent;
import dev.zihasz.client.feature.module.Category;
import dev.zihasz.client.feature.module.Module;
import dev.zihasz.client.feature.settings.Setting;
import dev.zihasz.client.feature.settings.SettingBuilder;
import dev.zihasz.client.utils.Timer;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FakeLag extends Module {

	private final Setting<Boolean> outgoing = new SettingBuilder<>(true).name("").description("").build(this);
	private final Setting<Boolean> incoming = new SettingBuilder<>(true).name("").description("").build(this);
	private final Setting<Integer> outgoingDelay = new SettingBuilder<>(20).name("").description("").range(0, 30000).visibility(v -> outgoing.value).build(this);
	private final Setting<Integer> incomingDelay = new SettingBuilder<>(20).name("").description("").range(0, 30000).visibility(v -> incoming.value).build(this);
	private final Timer incomingTimer = new Timer();
	private final Timer outgoingTimer = new Timer();
	private final Queue<Packet<?>> incomingQueue = new ConcurrentLinkedQueue<>();
	private final Queue<Packet<?>> outgoingQueue = new ConcurrentLinkedQueue<>();

	public FakeLag() {
		super("FakeLag", "Makes you look like your lagging", Category.PLAYER);
	}

	@Override
	public void onEnable() {
		incomingTimer.reset();
		outgoingTimer.reset();
	}

	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event) {
		if (incoming.getValue() && incomingTimer.passedMS(incomingDelay.getValue())) {
			mc.player.connection.sendPacket(Objects.requireNonNull(incomingQueue.poll()));
			incomingTimer.reset();
		}
		if (outgoing.getValue() && outgoingTimer.passedMS(outgoingDelay.getValue())) {
			mc.player.connection.sendPacket(Objects.requireNonNull(outgoingQueue.poll()));
			outgoingTimer.reset();
		}
	}

	@SubscribeEvent
	public void onPacketEvent(PacketEvent.Send event) {
		if (outgoing.getValue()) {
			event.cancel();
			outgoingQueue.add(event.getPacket());
		}
	}

	@SubscribeEvent
	public void onPacketEvent(PacketEvent.Read event) {
		if (incoming.getValue()) {
			event.cancel();
			incomingQueue.add(event.getPacket());
		}
	}

}
