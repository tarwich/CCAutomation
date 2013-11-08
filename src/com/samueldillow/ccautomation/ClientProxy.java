package com.samueldillow.ccautomation;

import java.io.File;
import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ModContainer;

public class ClientProxy extends CommonProxy {
	@Override
	public File getBase() {
		return FMLClientHandler.instance().getClient().mcDataDir;
	}
	
	@Override
	public void preInit() {
		// Don't know why I need this
	    CCAutomation.Config.cameraBlockRenderID = RenderingRegistry.getNextAvailableRenderId();
		// Let the common proxy do its thing
		super.preInit();
	}
}
