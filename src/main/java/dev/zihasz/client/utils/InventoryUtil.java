package dev.zihasz.client.utils;

import net.minecraft.block.Block;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class InventoryUtil implements Util {

	public static int findHotbar(Item item) {
		for (int i = 0; i < 9; i++) {
			if (mc.player.inventory.getStackInSlot(i).equals(new ItemStack(item)))
				return i;
		}
		return -1;
	}

	public static int findHotbar(Block block) {
		for (int i = 0; i < 9; i++) {
			if (mc.player.inventory.getStackInSlot(i).equals(new ItemStack(block)))
				return i;
		}
		return -1;
	}

	public static int findInventory(Item item) {
		return mc.player.inventory.getSlotFor(new ItemStack(item));
	}

	public static int findInventory(Block block) {
		return mc.player.inventory.getSlotFor(new ItemStack(block));
	}

	public static boolean switchToOffhand(int fromSlot) {
		if (fromSlot < 0) return false;

		inventoryAction(fromSlot, 0, ClickType.PICKUP);
		inventoryAction(45, 0, ClickType.PICKUP);
		inventoryAction(fromSlot, 0, ClickType.PICKUP);
		mc.playerController.updateController();

		return true;
	}

	public static boolean switchTo(int slot) {
		if (slot < 0 && mc.player.inventory.currentItem == slot) return false;

		mc.player.inventory.currentItem = slot;
		mc.playerController.updateController();
		return true;
	}

	public static boolean switchTo(Item item) {
		return switchTo(findHotbar(item));
	}

	public static boolean switchTo(Block block) {
		return switchTo(findHotbar(block));
	}

	public static void inventoryAction(int slot, int button, ClickType type) {
		mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot, button, type, mc.player);
	}

}
