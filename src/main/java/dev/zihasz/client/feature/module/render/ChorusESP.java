package dev.zihasz.client.feature.module.render;

import dev.zihasz.client.event.events.PacketEvent;
import dev.zihasz.client.feature.module.Category;
import dev.zihasz.client.feature.module.Module;
import dev.zihasz.client.feature.settings.Setting;
import dev.zihasz.client.feature.settings.SettingBuilder;
import dev.zihasz.client.utils.Renderer3D;
import dev.zihasz.client.utils.Timer;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class ChorusESP extends Module {

	private final Setting<Integer> range = new SettingBuilder<>(32).name("Range").description("Range").build(this);
	private final Setting<Boolean> name = new SettingBuilder<>(true).name("Name").description("Name").build(this);
	private final Setting<Boolean> fill = new SettingBuilder<>(true).name("Fill").description("Fill").build(this);
	private final Setting<Color> fillColor = new SettingBuilder<>(new Color(0x98ffffff)).name("FillColor").description("FillColor").build(this);
	private final Setting<Boolean> outline = new SettingBuilder<>(true).name("Outline").description("Outline").build(this);
	private final Setting<Color> outlineColor = new SettingBuilder<>(new Color(0xffffffff)).name("OutlineColor").description("OutlineColor").build(this);
	private final Setting<Float> outlineWidth = new SettingBuilder<>(1.5f).name("OutlineWidth").description("OutlineWidth").build(this);
	private final Timer timer;
	private BlockPos position;

	public ChorusESP() {
		super("ChorusESP", "Shows where a chorus will TP you", Category.RENDER);
		timer = new Timer();
	}

	@SubscribeEvent
	public void onPacketRead(PacketEvent.Read event) {
		Packet<?> raw = event.getPacket();
		if (raw instanceof SPacketSoundEffect) {
			SPacketSoundEffect packet = (SPacketSoundEffect) raw;
			if (packet.sound == SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT) {
				position = new BlockPos(packet.getX(), packet.getY(), packet.getZ());
				timer.reset();
			}
		}
	}

	@SubscribeEvent
	public void onRender3D(RenderWorldLastEvent event) {
		if (position != null) {
			if (timer.passedMS(2000)) {
				position = null;
				return;
			}
			drawBlock(position);
		}
	}

	private void drawBlock(BlockPos pos) {
		AxisAlignedBB bb = new AxisAlignedBB(pos);

		if (fill.getValue())
			Renderer3D.drawBBFill(bb, fillColor.getValue());

		if (outline.getValue())
			Renderer3D.drawBBOutline(bb, outlineWidth.getValue(), outlineColor.getValue());
	}

}
