package dev.zihasz.client.feature.module.player;

import dev.zihasz.client.event.events.PacketEvent;
import dev.zihasz.client.feature.module.Category;
import dev.zihasz.client.feature.module.Module;
import dev.zihasz.client.utils.PlayerUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class Disabler extends Module {

	private final List<CPacketConfirmTransaction> transactionList = new ArrayList<>();
	private int transactions = 0;

	public Disabler() {
		super("Disabler", "anticheat :cold:", Category.PLAYER);
	}

	@Override
	public void onEnable() {
		mc.player.ticksExisted = 0;
	}

	@SubscribeEvent
	public void onTick() {
		if (nullCheck()) return;

		mc.player.connection.sendPacket(new CPacketKeepAlive(0));

		if (mc.player.ticksExisted % 5 == 0) {
		}
		if (mc.player.ticksExisted % 20 == 0) {
		}
		if (mc.player.ticksExisted % 60 == 0) {
			double x = mc.player.posX;
			double y = mc.player.posY;
			double z = mc.player.posZ;
			mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.lastTickPosX, mc.player.lastTickPosY, mc.player.lastTickPosZ, false));
			mc.player.connection.sendPacket(new CPacketPlayer.Position(x - PlayerUtil.getDiffX() / 2D, y - PlayerUtil.getDiffY() / 2D, z - PlayerUtil.getDiffZ() / 2D, true));
			mc.player.connection.sendPacket(new CPacketPlayer.Position(x, y, z, true));
		}
		if (mc.player.ticksExisted % 120 == 0 && transactionList.size() > transactions) {
			mc.player.connection.sendPacket(transactionList.get(transactions++));
		}
		if (mc.player.ticksExisted % 600 == 0) {
			transactionList.clear();
			transactions = 0;
		}
	}

	@SubscribeEvent
	public void onPacketSend(PacketEvent.Send event) {
		Packet<?> raw = event.getPacket();
		if (raw instanceof CPacketConfirmTransaction) {
			transactionList.add((CPacketConfirmTransaction) raw);
			event.setCanceled(true);
		}
	}

	@Override
	public void onDisable() {
		transactionList.clear();
		transactions = 0;
	}

}
