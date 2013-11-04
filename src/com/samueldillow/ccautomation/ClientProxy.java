package com.samueldillow.ccautomation;

import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
	@Override
	public void preInit() {
	    CCAutomation.Config.cameraBlockRenderID = RenderingRegistry.getNextAvailableRenderId();
		// Let the common proxy do its thing
		super.preInit();
	}
}
