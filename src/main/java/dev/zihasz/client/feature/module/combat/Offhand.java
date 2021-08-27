package dev.zihasz.client.feature.module.combat;

import dev.zihasz.client.feature.module.Category;
import dev.zihasz.client.feature.module.Module;
import dev.zihasz.client.feature.settings.Setting;
import dev.zihasz.client.feature.settings.SettingBuilder;
import dev.zihasz.client.utils.Timer;
import dev.zihasz.client.utils.InventoryUtil;
import dev.zihasz.client.utils.PlayerUtil;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.Map;

public class Offhand extends Module {

	private final Map<Item, ItemStack> itemLookup = new HashMap<>();
	private final Timer switchTimer = new Timer();

	private final Setting<Integer> totemHealth = new SettingBuilder<>(10).name("Totem Health").description("The health to put totems in your hand.").range(0, 36).build(this);
	private final Setting<Integer> switchDelay = new SettingBuilder<>(100).name("Switch Delay").description("Delay in ms between switches.").range(0, 1000).build(this);

	private final Setting<Item> item = new SettingBuilder<>(Item.Crystal).name("Item").description("The item to put in your offhand normally.").build(this);
	private final Setting<Item> rightClickItem = new SettingBuilder<>(Item.Gap).name("Right Click Item").description("Item to use when wright click is down.").visibility(v -> item.getValue() != Item.Gap).build(this);
	private final Setting<Boolean> swordOnly = new SettingBuilder<>(true).name("Sword Only").description("Only gap when swording.").parent(rightClickItem).build(this);

	public Offhand() {
		super("Offhand", "Puts items in your offhand, useful in the ends against the opps.", Category.COMBAT);
		itemLookup.put(Item.Totem, new ItemStack(Items.TOTEM_OF_UNDYING));
		itemLookup.put(Item.Crystal, new ItemStack(Items.END_CRYSTAL));
		itemLookup.put(Item.Gap, new ItemStack(Items.GOLDEN_APPLE));
		itemLookup.put(Item.Bow, new ItemStack(Items.BOW));
		itemLookup.put(Item.Potion, new ItemStack(Items.POTIONITEM));
		itemLookup.put(Item.Exp, new ItemStack(Items.EXPERIENCE_BOTTLE));
		itemLookup.put(Item.Torch, new ItemStack(Blocks.TORCH));
		itemLookup.put(Item.Obsidian, new ItemStack(Blocks.OBSIDIAN));
	}

	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event) {
		if (nullCheck()) return;

		if (!switchTimer.passedMS(switchDelay.value)) return;

		boolean switched;
		if (PlayerUtil.getFullHealth() > totemHealth.getValue()) {
			if (mc.gameSettings.keyBindUseItem.isKeyDown() && (!swordOnly.getValue() || mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() == Items.DIAMOND_SWORD))
				switched = InventoryUtil.switchToOffhand(InventoryUtil.findInventory(itemLookup.get(rightClickItem.value).getItem()));
			else
				switched = InventoryUtil.switchToOffhand(InventoryUtil.findInventory(itemLookup.get(item.value).getItem()));
		} else {
			switched = InventoryUtil.switchToOffhand(InventoryUtil.findInventory(Items.TOTEM_OF_UNDYING));
		}

		if (switched) switchTimer.reset();
	}

	private enum Item {
		Totem,
		Crystal,
		Gap,
		Bow,
		Potion,
		Exp,
		Torch,
		Obsidian
	}

}
