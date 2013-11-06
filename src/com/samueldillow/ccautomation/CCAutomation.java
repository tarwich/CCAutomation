package com.samueldillow.ccautomation;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraftforge.common.Configuration;

import com.samueldillow.ccautomation.block.BlockCCAutomation;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(
	modid        = "CCAutomation",
	dependencies = "required-after:ComputerCraft;after:CCTurtle",
	name         = "ComputerCraft Automation Peripheral",
	version      = "0.2"
	)
public class CCAutomation {
	public static class Config {
		/**
		 * Block ID for this peripheral
		 */
		public static Integer blockId = 3600;
		/**
		 * The turtle upgrade id for this peripheral 
		 */
		public static Integer upgradeId = 254;
		/**
		 * An instance of the peripheral block for configuration purposes
		 */
		public static BlockCCAutomation automationBlock;
		/**
		 * The adjective used for the automation types
		 */
		public static String automationAdjective = "automation";
		public static int cameraBlockRenderID;
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
	
	/** Proxy for server/client configuration */
	@SidedProxy(
		clientSide="com.samueldillow.ccautomation.ClientProxy",
		serverSide="com.samueldillow.ccautomation.CommonProxy")
	public static CommonProxy proxy;
	
	@EventHandler
	public void PreInit(FMLPreInitializationEvent e) {
		// Load the configuration for this mod
		Configuration configFile = new Configuration(e.getSuggestedConfigurationFile());
		// Load the block id from the configuration
		// I'm using the same block id as CCCamera, because I feel this is a competing mod
		Config.blockId = configFile.getBlock("blockId", Config.blockId, "The ID for the peripheral block").getInt();
		// I'm using the same upgrade id as CCCamera, because I feel this is a competing mod
		Config.upgradeId = Utility.cast(configFile.get("upgradeId", Config.upgradeId.toString(), "The tutle upgrade id to use"), Config.upgradeId);
		// Make the proxy do the initialization
		proxy.preInit();
	}
}
