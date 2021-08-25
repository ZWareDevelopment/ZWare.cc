package dev.zihasz.client.feature.module.combat;

import dev.zihasz.client.feature.module.Category;
import dev.zihasz.client.feature.module.Module;
import dev.zihasz.client.feature.module.render.HoleESP;
import dev.zihasz.client.feature.settings.Setting;
import dev.zihasz.client.feature.settings.SettingBuilder;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;

public class AutoCrystal extends Module {

	public AutoCrystal() {
		super("AutoCrystal", "Automatic crystals :brain:", Category.COMBAT);
	}

	private final Setting<Void> configParent = new SettingBuilder<>((Void) null).name("Config").description("Configuration related settings.").build(this);
	private final Setting<Preset> configPreset = new SettingBuilder<>(Preset.SemiStrict).name("Config Preset").description("Default config presets.").callback((preset, preset2) -> true).parent(configParent).build(this);
	private final Setting<Boolean> lockPBR = new SettingBuilder<>(true).name("LockPBR").description("Lock place-break range. Useful on some servers.").parent(configParent).build(this);
	private final Setting<Boolean> lockPBD = new SettingBuilder<>(true).name("LockPBD").description("Lock place-break delay. Useful on some servers.").parent(configParent).build(this);
	private final Setting<Boolean> autoConfig = new SettingBuilder<>(true).name("Auto Delay Config").description("Automatically config delays.").parent(configParent).build(this);
	private final Setting<Integer> autoConfigPlaceFactor = new SettingBuilder<>(0).name("AC Place Factor").description("The number to divide your ping with to get the place delay. (Set to 0 to don't change the place delay)").range(0, 10).parent(autoConfig).build(this);
	private final Setting<Integer> autoConfigBreakFactor = new SettingBuilder<>(2).name("AC Break Factor").description("The number to divide your ping with to get the break delay. (Set to 0 to don't change the break delay)").range(0, 10).parent(autoConfig).build(this);

	private final Setting<Boolean> placeSetting = new SettingBuilder<>(true).name("Place").description("Do place cycles.").build(this);
	private final Setting<Rotate> placeRotate = new SettingBuilder<>(Rotate.None).name("Place Rotate").description("How to rotate to the place position.").parent(placeSetting).build(this);
	private final Setting<Float> placeRange = new SettingBuilder<>(5.0f).name("Place Range").description("How far can the AC place crystals.").parent(placeSetting).build(this);
	private final Setting<Boolean> placeWalls = new SettingBuilder<>(true).name("Place Walls").description("Can the AC place through walls.").parent(placeSetting).build(this);
	private final Setting<Float> placeWallsRange = new SettingBuilder<>(3.5f).name("Place Walls Range").description("How far can the AC place crystals through walls.").visibility(v -> placeWalls.getValue()).parent(placeSetting).build(this);
	private final Setting<Integer> placeDelay = new SettingBuilder<>(0).name("Place Delay").description("Place delay in milliseconds").range(0, 5000).build(this);
	private final Setting<Integer> placeMinDamage = new SettingBuilder<>(8).name("Place Min Damage").description("The minimum damage done to the target when the crystal is placed.").parent(placeSetting).build(this);
	private final Setting<Integer> placeMaxDamage = new SettingBuilder<>(36).name("Place Max Damage").description("The maximum damage done to the target when the crystal is placed.").parent(placeSetting).build(this);
	private final Setting<Integer> placeMinSDamage = new SettingBuilder<>(0).name("Place Min Self Damage").description("The minimum damage done to yourself when the crystal is placed.").parent(placeSetting).build(this);
	private final Setting<Integer> placeMaxSDamage = new SettingBuilder<>(18).name("Place Max Self Damage").description("The maximum damage done to yourself when the crystal is placed.").parent(placeSetting).build(this);
	private final Setting<Integer> placeFacePlace = new SettingBuilder<>(12).name("Face Place").description("The health to faceplace at.").parent(placeSetting).build(this);
	private final Setting<Integer> placeArmorPercent = new SettingBuilder<>(20).name("Armor Breaker").description("The percent to start face placing to break armor.").parent(placeSetting).build(this);

	private final Setting<Boolean> breakSetting = new SettingBuilder<>(true).name("Break").description("Do break cycles.").build(this);
	private final Setting<Rotate> breakRotate = new SettingBuilder<>(Rotate.None).name("Break Rotate").description("How to rotate to the break position.").parent(breakSetting).build(this);
	private final Setting<Float> breakRange = new SettingBuilder<>(5.0f).name("Break Range").description("How far can the AC break crystals.").parent(breakSetting).build(this);
	private final Setting<Boolean> breakWalls = new SettingBuilder<>(true).name("Break Walls").description("Can the AC break through walls.").parent(breakSetting).build(this);
	private final Setting<Float> breakWallsRange = new SettingBuilder<>(3.5f).name("Break Walls Range").description("How far can the AC break crystals through walls.").visibility(v -> breakWalls.getValue()).parent(breakSetting).build(this);
	private final Setting<Integer> breakDelay = new SettingBuilder<>(10).name("Break Delay").description("Break delay in milliseconds").range(0, 5000).build(this);
	private final Setting<Integer> breakMinDamage = new SettingBuilder<>(8).name("Break Min Damage").description("The minimum damage done to the target when the crystal is break.").parent(breakSetting).build(this);
	private final Setting<Integer> breakMaxDamage = new SettingBuilder<>(36).name("Break Max Damage").description("The maximum damage done to the target when the crystal is break.").parent(breakSetting).build(this);
	private final Setting<Integer> breakMinSDamage = new SettingBuilder<>(0).name("Break Min Self Damage").description("The minimum damage done to yourself when the crystal is break.").parent(breakSetting).build(this);
	private final Setting<Integer> breakMaxSDamage = new SettingBuilder<>(18).name("Break Max Self Damage").description("The maximum damage done to yourself when the crystal is break.").parent(breakSetting).build(this);

	private final Setting<Switch> switchSetting = new SettingBuilder<>(Switch.None).name("Switch").description("Do switch things.").build(this);
	private final Setting<Integer> switchMinHealth = new SettingBuilder<>(6).name("Switch Min Health").description("If you're below this health the AC won't switch.").parent(switchSetting).build(this);

	private final Setting<Boolean> renderSetting = new SettingBuilder<>(true).name("Render").description("Render place pos duh").build(this);
	private final Setting<Shape> shape = new SettingBuilder<>(Shape.FULL).name("Shape").description("Shape of the render.").parent(renderSetting).build(this);
	private final Setting<Float> height = new SettingBuilder<>(.25f).name("Height").description("Height of the slab").min(0f).max(1f).visibility(v -> shape.getValue().equals(Shape.SLAB)).parent(renderSetting).build(this);
	private final Setting<Mode> mode = new SettingBuilder<>(Mode.BOTH).name("Mode").description("Way to render holes.").parent(renderSetting).build(this);
	private final Setting<Float> width = new SettingBuilder<>(1f).name("Outline Width").description("Width of the outline").min(0f).max(5f).visibility(v -> mode.getValue().equals(Mode.OUTLINE) || mode.getValue().equals(Mode.BOTH)).parent(renderSetting).build(this);
	private final Setting<Outline> outline = new SettingBuilder<>(Outline.SIMPLE).name("Outline Mode").description("The way to render the outline").visibility(v -> mode.getValue().equals(Mode.OUTLINE) || mode.getValue().equals(Mode.BOTH)).parent(renderSetting).build(this);
	private final Setting<Float> length = new SettingBuilder<>(.1f).name("Claw Length").description("Length of the \"claws\".").min(0f).max(.5f).visibility(v -> (mode.getValue().equals(Mode.OUTLINE) || mode.getValue().equals(Mode.BOTH)) && outline.getValue().equals(Outline.CLAW)).parent(renderSetting).build(this);
	private final Setting<Integer> outlineAlpha = new SettingBuilder<>(0).name("Outline Alpha").description("Alpha of the outline").min(0).max(255).visibility(v -> mode.getValue().equals(Mode.OUTLINE) || mode.getValue().equals(Mode.BOTH)).parent(renderSetting).build(this);
	private final Setting<Boolean> glow = new SettingBuilder<>(true).name("Glow").description("Give the holes a glow effect.").parent(renderSetting).build(this);
	private final Setting<Boolean> invert = new SettingBuilder<>(true).name("Invert Glow").description("Invert the glow effect").parent(glow).parent(renderSetting).build(this);
	private final Setting<Integer> glowAlpha = new SettingBuilder<>(0).name("Glow Alpha").description("Changes the \"hidden\" parts alpha.").min(0).max(255).parent(glow).parent(renderSetting).build(this);


	@Override
	public void onEnable() {

	}

	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event) {

	}

	@Override
	public void onDisable() {

	}

	private enum Preset {
		NonStrict,
		SemiStrict,
		FullStrict,
	}

	private enum Rotate {
		None,
		Client,
		Packet,
		Spoof,
	}

	private enum Switch {
		None,
		Normal,
		Silent
	}

	private enum Shape {
		FULL,
		SLAB,
		FLAT,
	}

	private enum Mode {
		OUTLINE,
		FILL,
		BOTH,
	}

	private enum Outline {
		SIMPLE,
		CLAW,
	}

}
