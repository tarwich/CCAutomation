package com.samueldillow.ccautomation;

import java.util.logging.Logger;

import net.minecraft.util.Icon;
import net.minecraftforge.common.Configuration;

import com.samueldillow.ccautomation.block.BlockCCAutomation;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(
	modid        = "CCAutomation",
	dependencies = "required-after:ComputerCraft;after:CCTurtle",
	name         = "ComputerCraft Automation Peripheral",
	version      = "0.2"
	)
public class CCAutomation {
	public static final String LUA_SOURCE = "/assets/ccautomation/lua";
	
	public static class Config {
		/** Block ID for this peripheral */
		public static Integer blockId = 3600;
		/** The turtle upgrade id for this peripheral */
		public static Integer upgradeId = 254;
		/** An instance of the peripheral block for configuration purposes */
		public static BlockCCAutomation automationBlock;
		/** The adjective used for the automation types */
		public static String automationAdjective = "automation";
		public static int cameraBlockRenderID;
		/** Path to where lua files originate */
		public static String LuaPath = "mods/ccautomation/lua";
		/** How far we can see */
		public static Integer sightDistance = 64;
	}
	
	public static class Blocks {
		/** The main block for this mod */
		public static BlockCCAutomation automationBlock = null;
	}
	
	public static class Icons {
		public static Icon turtle = null;
		public static Icon top    = null;
		public static Icon front  = null;
		public static Icon side   = null;
	};
	
	/** The instance of this mod as instantiated by Forge */
	@Instance
	public static CCAutomation instance;
	
	/** Proxy for server/client configuration */
	@SidedProxy(
		clientSide="com.samueldillow.ccautomation.ClientProxy",
		serverSide="com.samueldillow.ccautomation.CommonProxy")
	public static CommonProxy proxy;
	
	/** Logger for writing to the forge log */
	public static Logger log = Logger.getAnonymousLogger();
	
	@EventHandler
	public void PreInit(FMLPreInitializationEvent event) {
		// Setup the CCAutomation logger
		CCAutomation.log = event.getModLog();
		// Don't know why, but we have to set the log parent
		CCAutomation.log.setParent(FMLLog.getLogger());
		// Load the configuration for this mod
		Configuration configFile = new Configuration(event.getSuggestedConfigurationFile());
		// Load the block id from the configuration
		// I'm using the same block id as CCCamera, because I feel this is a competing mod
		Config.blockId = configFile.getBlock("blockId", Config.blockId, 
				"The ID for the peripheral block").getInt();
		// I'm using the same upgrade id as CCCamera, because I feel this is a competing mod
		Config.upgradeId = Utility.cast(configFile.get("upgradeId", "", 
				"The tutle upgrade id to use"
				), Config.upgradeId);
		// Load the sight distance
		Config.sightDistance = Utility.cast(configFile.get("sightDistance", "", 
				"The limit to how far we can see. This can cause problems if too large"
				), Config.sightDistance);
		// Make the proxy do the initialization
		proxy.preInit();
	}
}
