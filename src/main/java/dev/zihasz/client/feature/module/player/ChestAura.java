package dev.zihasz.client.feature.module.player;

import dev.zihasz.client.feature.module.Category;
import dev.zihasz.client.feature.module.Module;
import dev.zihasz.client.feature.settings.Setting;
import dev.zihasz.client.feature.settings.SettingBuilder;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Comparator;

public class ChestAura extends Module {

	private final Setting<Boolean> packet = new SettingBuilder<>(true).name("Packet").description("duh").build(this);
	private final Setting<Boolean> shulkerOnly = new SettingBuilder<>(true).name("Shulker Only").description("only shulkers").build(this);
	private final Setting<Double> range = new SettingBuilder<>(4.5).name("Range").description("only shulkers").range(0.0, 7.0).build(this);

	public ChestAura() {
		super("ChestAura", "Opens chests around you. duh.", Category.PLAYER);
	}

	@SubscribeEvent
	public void onUpdate(TickEvent.ClientTickEvent event) {
		TileEntityShulkerBox box = (TileEntityShulkerBox) mc.world.loadedTileEntityList.stream()
				.filter(tileEntity -> !shulkerOnly.getValue() || tileEntity instanceof TileEntityShulkerBox)
				.filter(tileEntity -> mc.player.getDistance(tileEntity.getPos().x, tileEntity.getPos().y, tileEntity.getPos().z) < range.getValue())
				.sorted(Comparator.comparing(tileEntity ->
						mc.player.getDistance(
								tileEntity.getPos().x,
								tileEntity.getPos().y,
								tileEntity.getPos().z)))
				.findFirst()
				.orElse(null);
		if (box != null) {
			RayTraceResult rt = mc.world.rayTraceBlocks(
					new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ),
					new Vec3d(box.getPos().getX() + 0.5, box.getPos().getY() + 0.5, box.getPos().getZ() + 0.5)
			);
			if (!packet.getValue())
				mc.playerController.processRightClickBlock(
						mc.player,
						mc.world,
						box.getPos(),
						getEnumFacing(true, box.getPos()),
						rt.hitVec,
						EnumHand.MAIN_HAND);
			else
				mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(
						box.getPos(),
						getEnumFacing(true, box.getPos()),
						EnumHand.MAIN_HAND,
						(float) rt.hitVec.x,
						(float) rt.hitVec.y,
						(float) rt.hitVec.z));
		}
	}

	private EnumFacing getEnumFacing(boolean rayTrace, BlockPos placePosition) {
		RayTraceResult result = mc.world.rayTraceBlocks(
				new Vec3d(
						mc.player.posX,
						mc.player.posY + mc.player.getEyeHeight(),
						mc.player.posZ),
				new Vec3d(
						placePosition.getX() + 0.5,
						placePosition.getY() + 0.5,
						placePosition.getZ() + 0.5)
		);

		if (placePosition.getY() == 255) return EnumFacing.DOWN;
		if (rayTrace) return (result == null || result.sideHit == null) ? EnumFacing.UP : result.sideHit;

		return EnumFacing.UP;
	}

}
