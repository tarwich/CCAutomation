package com.samueldillow.ccautomation;

import java.io.File;

import com.samueldillow.ccautomation.block.BlockCCAutomation;
import com.samueldillow.ccautomation.computercraft.TurtleAutomation;

import cpw.mods.fml.common.FMLCommonHandler;

public class CommonProxy {
	public File getBase() {
		return FMLCommonHandler.instance().getMinecraftServerInstance().getFile(".");
	}
	
	public void preInit() {
		// Load our block and register with everyone that needs it
		CCAutomation.Blocks.automationBlock = BlockCCAutomation.register(); 
		// Register our turtle block with everyone that needs it
		TurtleAutomation.register();
	}
}
