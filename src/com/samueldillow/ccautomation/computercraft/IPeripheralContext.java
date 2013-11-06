package com.samueldillow.ccautomation.computercraft;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public interface IPeripheralContext {
	public ItemStack getSelectedItem();
	public Vec3 getPosition();
	public World getWorld();
	public int getFacingDir();
}
