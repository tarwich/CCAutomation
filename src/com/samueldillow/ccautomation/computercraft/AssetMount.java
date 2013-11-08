package com.samueldillow.ccautomation.computercraft;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.samueldillow.ccautomation.CCAutomation;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ModContainer;
import dan200.computer.api.IMount;

public class AssetMount implements IMount {
	private File targetFile;

	private AssetMount(String path) throws Exception {
		ModContainer container = FMLCommonHandler.instance().findContainerFor(CCAutomation.instance);
		File modFile = container.getSource();
		
		targetFile = new File(modFile, path);
		
		if(!targetFile.exists()) 
			throw new Exception(String.format("Cannot find file: %s near %s", path, modFile));
	}

	@Override
	public boolean exists(String path) throws IOException {
		return new File(targetFile, path).exists();
	}

	@Override
	public boolean isDirectory(String path) throws IOException {
		return new File(targetFile, path).isDirectory();
	}

	@Override
	public void list(String path, List<String> contents) throws IOException {
		// Look up the file at path
		File file = new File(targetFile, path);
		// Add all the children to the list (required by IMount)
		for(String child : file.list()) contents.add(child);
	}

	@Override
	public long getSize(String path) throws IOException {
		return new File(targetFile, path).getTotalSpace();
	}

	@Override
	public InputStream openForRead(String path) throws IOException {
		return new FileInputStream(new File(targetFile, path));
	}

	public static AssetMount getMount() {
		try {
			return new AssetMount(CCAutomation.LUA_SOURCE);
		} catch(Exception ignore) {
			return null;
		}
	}
}
