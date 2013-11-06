package com.samueldillow.ccautomation.computercraft;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import dan200.turtle.api.ITurtleAccess;
import dan200.turtle.api.TurtleSide;

public class TurtleContext implements IPeripheralContext {
	private ITurtleAccess turtle = null;
	private TurtleSide side = null;

	public TurtleContext(ITurtleAccess turtle, TurtleSide side) {
		this.turtle = turtle;
		this.side = side;
	}
	
	@Override
	public int getFacingDir() {
		return turtle.getFacingDir();
	}
	
	@Override
	public Vec3 getPosition() {
		return turtle.getPosition();
	}
	
	@Override
	public ItemStack getSelectedItem() {
		return turtle.getSlotContents(turtle.getSelectedSlot());
	}
	
	@Override
	public World getWorld() {
		return turtle.getWorld();
	}
}
