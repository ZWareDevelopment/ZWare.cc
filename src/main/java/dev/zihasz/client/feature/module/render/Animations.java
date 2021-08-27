package dev.zihasz.client.feature.module.render;

import dev.zihasz.client.event.events.PacketEvent;
import dev.zihasz.client.feature.module.Category;
import dev.zihasz.client.feature.module.Module;
import dev.zihasz.client.feature.settings.Setting;
import dev.zihasz.client.feature.settings.SettingBuilder;
import net.minecraft.init.MobEffects;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Animations extends Module {

	private final Setting<Mode> mode = new SettingBuilder<>(Mode.High).name("Mode").description("Mode").build(this);
	private final Setting<Swing> swing = new SettingBuilder<>(Swing.Offhand).name("Swing").description("Swing").build(this);
	private final Setting<Boolean> slow = new SettingBuilder<>(true).name("Slow").description("Swing").build(this);

	public Animations() {
		super("Animations", "Change animations", Category.RENDER);
	}

	@SubscribeEvent
	public void onUpdate(TickEvent.ClientTickEvent event) {
		if (nullCheck()) return;

		if (swing.getValue() == Swing.Offhand) {
			mc.player.swingingHand = EnumHand.OFF_HAND;
		}
		if (mode.getValue() != Mode.High) {
			if (mc.entityRenderer.itemRenderer.prevEquippedProgressMainHand >= 0.9) {
				mc.entityRenderer.itemRenderer.equippedProgressMainHand = 1.0f;
				mc.entityRenderer.itemRenderer.itemStackMainHand = mc.player.getHeldItemMainhand();
			}
		}
	}

	@Override
	public void onEnable() {
		if (slow.getValue()) {
			mc.player.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 255000));
		}
	}

	@Override
	public void onDisable() {
		if (slow.getValue()) {
			mc.player.removePotionEffect(MobEffects.MINING_FATIGUE);
		}
	}

	@SubscribeEvent
	public void onPacketSend(PacketEvent.Send event) {
		Packet<?> raw = event.getPacket();
		if (raw instanceof CPacketAnimation) {
			CPacketAnimation packet = (CPacketAnimation) raw;
			if (swing.getValue() == Swing.Disable)
				event.cancel();
		}
	}

	private enum Swing {
		Mainhand,
		Offhand,
		Disable,
	}

	private enum Mode {
		Low,
		High,
	}

}
