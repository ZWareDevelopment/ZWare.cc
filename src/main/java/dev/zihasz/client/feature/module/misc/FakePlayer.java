package dev.zihasz.client.feature.module.misc;

import com.mojang.authlib.GameProfile;
import dev.zihasz.client.feature.module.Category;
import dev.zihasz.client.feature.module.Module;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.UUID;

public class FakePlayer extends Module {

	private final int entityId = -99;
	EntityOtherPlayerMP player = null;

	public FakePlayer() {
		super("FakePlayer", "Creates a player for testing", Category.MISC);
	}

	@Override
	public void onEnable() {
		if (nullCheck()) return;
		GameProfile profile = new GameProfile(UUID.fromString("7c42a18c-659f-4f49-876e-5c065e50b86d"), "06d");
		player = new EntityOtherPlayerMP(mc.world, profile);
		player.copyLocationAndAnglesFrom(mc.player);
		player.rotationYawHead = mc.player.rotationYawHead;
		mc.world.addEntityToWorld(entityId, player);
	}

	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event) {
		if (nullCheck()) return;
		player.inventory = mc.player.inventory;
	}

	@Override
	public void onDisable() {
		if (nullCheck()) return;
		mc.world.removeEntityFromWorld(entityId);
	}

}
