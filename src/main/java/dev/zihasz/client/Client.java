package dev.zihasz.client;

import dev.zihasz.client.manager.config.ConfigManager;
import dev.zihasz.client.manager.feature.CommandManager;
import dev.zihasz.client.manager.feature.ModuleManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Client.ID, name = Client.NAME, version = Client.VERSION, certificateFingerprint = "", updateJSON = "", clientSideOnly = true)
public class Client {

	public static final String ID = "zware";
	public static final String NAME = "ZWare.cc";
	public static final String CNAME = String.format("§d%s§r%s", NAME.charAt(0), NAME.substring(1));
	public static final String VERSION = "1.0";

	public static final Logger LOGGER = LogManager.getLogger(Client.NAME + " " + Client.VERSION);

	@Instance
	public static Client INSTANCE;

	public static CommandManager commandManager;
	public static ConfigManager configManager;
	public static ModuleManager moduleManager;

	public Client() {
		INSTANCE = this;
	}

	@EventHandler
	public void onPreInit(FMLPreInitializationEvent event) {

	}

	@EventHandler
	public void onInit(FMLInitializationEvent event) {
		commandManager = new CommandManager();
		configManager = new ConfigManager();
		moduleManager = new ModuleManager();

		configManager.load();

		Runtime.getRuntime().addShutdownHook(new Thread(() -> configManager.save()));
	}

	@EventHandler
	public void onPostInit(FMLPostInitializationEvent event) {

	}

	@EventHandler
	public void onLoadComplete(FMLLoadCompleteEvent event) {

	}


}
