package dev.zihasz.client.feature.module.render;

import dev.zihasz.client.feature.module.Category;
import dev.zihasz.client.feature.module.Module;
import dev.zihasz.client.feature.settings.Setting;
import dev.zihasz.client.feature.settings.SettingBuilder;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class FOV extends Module {

	private final Setting<Integer> fov = new SettingBuilder<>(130).name("FOV").description("The main field of view.").range(0, 360).build(this);
	private final Setting<Boolean> items = new SettingBuilder<>(true).name("Items").description("Change the item FOV").build(this);
	private final Setting<Integer> itemFov = new SettingBuilder<>(130).name("Item FOV").description("Items field of view.").range(0, 360).visibility(v -> items.getValue()).build(this);

	public FOV() {
		super("FOV", "Modify your FOV.", Category.RENDER);
	}

	@SubscribeEvent
	public void onUpdate(TickEvent.ClientTickEvent event) {
		mc.gameSettings.fovSetting = fov.getValue();
	}

	@SubscribeEvent
	public void onFOVModifier(EntityViewRenderEvent.FOVModifier event) {
		event.setFOV(itemFov.getValue());
	}

}
