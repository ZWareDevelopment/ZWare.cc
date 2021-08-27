package dev.zihasz.client.feature.module.player;

import dev.zihasz.client.feature.module.Category;
import dev.zihasz.client.feature.module.Module;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.util.text.TextComponentString;

public class FakeKick extends Module {

	public FakeKick() {
		super("FakeKick", "Kicks you but not a real kick", Category.PLAYER);
	}

	@Override
	public void onEnable() {
		mc.player.connection.handleDisconnect(new SPacketDisconnect(new TextComponentString("Illegal exception: java.lang.NullPointerException")));
		disable();
	}
}
