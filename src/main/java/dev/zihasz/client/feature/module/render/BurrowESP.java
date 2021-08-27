package dev.zihasz.client.feature.module.render;

import dev.zihasz.client.feature.module.Category;
import dev.zihasz.client.feature.module.Module;
import dev.zihasz.client.feature.settings.Setting;
import dev.zihasz.client.feature.settings.SettingBuilder;
import dev.zihasz.client.utils.EntityUtil;
import dev.zihasz.client.utils.Renderer3D;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.Comparator;
import java.util.HashMap;

public class BurrowESP extends Module {

	private final Setting<Integer> range = new SettingBuilder<>(32).name("Range").description("Range").build(this);
	private final Setting<Boolean> name = new SettingBuilder<>(true).name("Name").description("Name").build(this);
	private final Setting<Boolean> fill = new SettingBuilder<>(true).name("Fill").description("Fill").build(this);
	private final Setting<Color> fillColor = new SettingBuilder<>(new Color(0x98ffffff)).name("FillColor").description("FillColor").build(this);
	private final Setting<Boolean> outline = new SettingBuilder<>(true).name("Outline").description("Outline").build(this);
	private final Setting<Color> outlineColor = new SettingBuilder<>(new Color(0xffffffff)).name("OutlineColor").description("OutlineColor").build(this);
	private final Setting<Float> outlineWidth = new SettingBuilder<>(1.5f).name("OutlineWidth").description("OutlineWidth").build(this);

	private final HashMap<EntityPlayer, BlockPos> burrowMap = new HashMap<>();

	public BurrowESP() {
		super("BurrowESP", "Shows if someone is burrowed or not", Category.RENDER);
	}

	@SubscribeEvent
	public void onUpdate(TickEvent.ClientTickEvent event) {
		burrowMap.clear();
		mc.world.loadedEntityList.stream()
				.filter(entity -> entity instanceof EntityPlayer)
				.map(entity -> (EntityPlayer) entity)
				.filter(entity -> !entity.isDead)
				.filter(entity -> entity != mc.player)
				.filter(entity -> mc.player.getDistance(entity) <= range.getValue())
				.filter(EntityUtil::isBurrowed)
				.sorted(Comparator.comparingDouble(entity -> mc.player.getDistance(entity)))
				.forEach(player -> burrowMap.put(player, player.getPosition()));
	}

	@SubscribeEvent
	public void onRender3D(RenderWorldLastEvent event) {
		this.burrowMap.entrySet().forEach(entry -> {
			EntityPlayer player = entry.getKey();
			BlockPos blockPos = entry.getValue();
			drawBlock(blockPos);
			drawText(blockPos, player.getGameProfile().getName());
		});
	}

	private void drawBlock(BlockPos pos) {
		AxisAlignedBB bb = new AxisAlignedBB(pos);

		if (fill.getValue())
			Renderer3D.drawBBFill(bb, fillColor.getValue());

		if (outline.getValue())
			Renderer3D.drawBBOutline(bb, outlineWidth.getValue(), outlineColor.getValue());
	}

	private void drawText(BlockPos pos, String text) {
		if (name.getValue())
			Renderer3D.drawTextFromBlock(pos, text, 0xffffffff, 1.5f);
	}

}
