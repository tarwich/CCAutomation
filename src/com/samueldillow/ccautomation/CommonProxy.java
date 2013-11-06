package com.samueldillow.ccautomation;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.samueldillow.ccautomation.block.BlockCCAutomation;
import com.samueldillow.ccautomation.computercraft.TurtleAutomation;

import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy {
	public void preInit() {
		// Load our block and register with everyone that needs it
		CCAutomation.Blocks.automationBlock = BlockCCAutomation.register(); 
		// Register our turtle block with everyone that needs it
		TurtleAutomation.register();
	}
}
