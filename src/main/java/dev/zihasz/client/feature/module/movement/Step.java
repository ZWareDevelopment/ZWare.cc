package dev.zihasz.client.feature.module.movement;

import dev.zihasz.client.feature.module.Category;
import dev.zihasz.client.feature.module.Module;
import dev.zihasz.client.feature.settings.Setting;
import dev.zihasz.client.feature.settings.SettingBuilder;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Step extends Module {

	private final Setting<Mode> modeSetting = new SettingBuilder<>(Mode.Vanilla)
			.name("Mode")
			.description("The mode to use. \n - Vanilla: Good for singleplayer or servers with bad anti cheat. \n - Normal: Good for most servers \n - Bypass: Good for strict servers \n")
			.build(this);
	private final Setting<Integer> heightSetting = new SettingBuilder<>(2).name("Height").description("The step height").build(this);

	public Step() {
		super("Step", "Steps up blocks bruh", Category.MOVEMENT);
	}

	@Override
	public void onEnable() {
		if (modeSetting.value.equals(Mode.Vanilla))
			mc.player.stepHeight = heightSetting.value;
	}

	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event) {

	}

	@Override
	public void onDisable() {
		if (modeSetting.value.equals(Mode.Vanilla))
			mc.player.stepHeight = .6f;
	}

	private enum Mode {
		Vanilla,
		Normal,
		Bypass
	}

}
