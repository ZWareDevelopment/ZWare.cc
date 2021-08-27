package dev.zihasz.client.feature.module.player;

import dev.zihasz.client.event.events.BlockEvent;
import dev.zihasz.client.event.events.PacketEvent;
import dev.zihasz.client.feature.module.Category;
import dev.zihasz.client.feature.module.Module;
import dev.zihasz.client.feature.settings.Setting;
import dev.zihasz.client.feature.settings.SettingBuilder;
import dev.zihasz.client.utils.Renderer3D;
import dev.zihasz.client.utils.Timer;
import dev.zihasz.client.utils.WorldUtil;
import net.minecraft.init.Items;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;

public class InstantMine extends Module {

	private final Timer timer = new Timer();
	private final Setting<Boolean> autoBreak = new SettingBuilder<>(true).name("AutoBreak").description("idfk ig").build(this);
	private final Setting<Integer> delay = new SettingBuilder<>(20).name("Delay").description("Delay between breaks ig.").range(0, 500).build(this);
	private final Setting<Boolean> picOnly = new SettingBuilder<>(true).name("PickaxeOnly").description("Only when using a pickaxe.").build(this);
	private boolean packetCancel = false;
	private BlockPos renderBlock;
	private BlockPos lastBlock;
	private EnumFacing direction;

	public InstantMine() {
		super("InstantMine", "Also known as CivBreaker", Category.PLAYER);
	}

	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event) {
		if (renderBlock != null) {
			if (autoBreak.getValue() && timer.passedMS(delay.getValue())) {
				if (picOnly.getValue() && !(mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() == Items.DIAMOND_PICKAXE))
					return;
				mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK,
						renderBlock, direction));
				timer.reset();
			}
		}
		mc.playerController.blockHitDelay = 0;
	}

	@SubscribeEvent
	public void onRender3D(RenderWorldLastEvent event) {
		if (renderBlock != null) {
			Renderer3D.drawBBFill(new AxisAlignedBB(renderBlock), new Color(255, 255, 255, 40));
		}
	}

	public BlockPos getTarget() {
		return renderBlock;
	}

	public void setTarget(BlockPos pos) {
		renderBlock = pos;
		packetCancel = false;
		mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK,
				pos, EnumFacing.DOWN));
		packetCancel = true;
		mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK,
				pos, EnumFacing.DOWN));
		direction = EnumFacing.DOWN;
		lastBlock = pos;
	}

	@SubscribeEvent
	public void onPacketSend(PacketEvent.Send event) {
		Packet<?> packet = event.getPacket();
		if (packet instanceof CPacketPlayerDigging) {
			CPacketPlayerDigging digPacket = (CPacketPlayerDigging) packet;
			if (((CPacketPlayerDigging) packet).getAction() == CPacketPlayerDigging.Action.START_DESTROY_BLOCK && packetCancel)
				event.cancel();
		}
	}

	@SubscribeEvent
	public void onPacketRead(BlockEvent.Damage event) {
		if (WorldUtil.canBreak(event.getPos())) {

			if (lastBlock == null || event.getPos().x != lastBlock.x || event.getPos().y != lastBlock.y || event.getPos().z != lastBlock.z) {
				packetCancel = false;
				mc.player.swingArm(EnumHand.MAIN_HAND);
				mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK,
						event.getPos(), event.getFacing()));
				packetCancel = true;
			} else {
				packetCancel = true;
			}
			//Command.sendChatMessage("Breaking");
			mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK,
					event.getPos(), event.getFacing()));

			renderBlock = event.getPos();
			lastBlock = event.getPos();
			direction = event.getFacing();

			event.cancel();

		}
	}
}
