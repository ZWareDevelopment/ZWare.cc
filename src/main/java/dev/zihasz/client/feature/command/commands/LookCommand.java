package dev.zihasz.client.feature.command.commands;

import dev.zihasz.client.feature.command.Command;
import dev.zihasz.client.utils.client.MessageBus;

public class LookCommand extends Command {
	public LookCommand() {
		super("Look", "Looks in a direction (good for precise looking)", "<yaw> <pitch>", "rotate");
	}

	@Override
	public boolean execute(String[] arguments) throws Exception {
		if (arguments.length != 2) {
			MessageBus.sendErrorMessage("You need to supply 2 arguments!");
			return false;
		}

		float yaw;
		float pitch;

		try {
			yaw = Integer.parseInt(arguments[0]);
			pitch = Integer.parseInt(arguments[1]);
		} catch (NumberFormatException exception) {
			MessageBus.sendErrorMessage("You need to supply valid numbers!");
			return false;
		}

		mc.player.rotationYaw = yaw;
		mc.player.rotationPitch = pitch;
		return true;
	}
}
