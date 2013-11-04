package com.samueldillow.ccautomation.computercraft;

import com.samueldillow.ccautomation.CCAutomation;

import net.minecraft.nbt.NBTTagCompound;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IHostedPeripheral;
import dan200.computer.api.ILuaContext;
import dan200.turtle.api.ITurtleAccess;

public class PeripheralAutomation implements IHostedPeripheral {
	enum methods {
		LOOK
	}
	
	private IPeripheralContext context;
	
	public PeripheralAutomation(IPeripheralContext context) {
		this.context = context;
	}

	@Override
	public void attach(IComputerAccess computer) {
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext luaContext, int method, Object[] arguments) throws Exception {
		switch(methods.values()[method]) {
		case LOOK: return doLook(computer, luaContext, arguments);
		}
		
		return null;
	}

	@Override
	public boolean canAttachToSide(int side) {
		return true;
	}

	@Override
	public void detach(IComputerAccess computer) {
	}

	private Object[] doLook(IComputerAccess computer, ILuaContext luaContext, Object[] arguments) {
		return new Object[] { "yay" };
	}

	@Override
	public String[] getMethodNames() {
		return new String[] { 
			"look"
		};
	}

	@Override
	public String getType() {
		return CCAutomation.Config.automationAdjective;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		// Load metadata from block
	}

	@Override
	public void update() {
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		// Store metadata in block
	}
}
