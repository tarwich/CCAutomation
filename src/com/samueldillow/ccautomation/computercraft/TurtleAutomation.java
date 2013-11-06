package com.samueldillow.ccautomation.computercraft;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

import com.samueldillow.ccautomation.CCAutomation;

import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;
import dan200.turtle.api.ITurtleUpgrade;
import dan200.turtle.api.TurtleAPI;
import dan200.turtle.api.TurtleSide;
import dan200.turtle.api.TurtleUpgradeType;
import dan200.turtle.api.TurtleVerb;

public class TurtleAutomation implements ITurtleUpgrade {
	public static TurtleAutomation register() {
		// Make an instance to register
		TurtleAutomation self = new TurtleAutomation();
		// Register this automation upgrade with the turtle api
		TurtleAPI.registerUpgrade(self);
		// In case something else needs this configuration instance
		return self;
	}

	@Override
	public int getUpgradeID() { return CCAutomation.Config.upgradeId; }

	@Override
	public String getAdjective() { return CCAutomation.Config.automationAdjective; }

	@Override
	public TurtleUpgradeType getType() { return TurtleUpgradeType.Peripheral; }

	@Override
	public ItemStack getCraftingItem() {
		return new ItemStack(CCAutomation.Blocks.automationBlock, 1);
	}

	@Override
	public boolean isSecret() { return false; }

	@Override
	public IHostedPeripheral createPeripheral(ITurtleAccess turtle, final TurtleSide side) {
		return new PeripheralAutomation(new TurtleContext(turtle, side));
	}

	@Override
	public boolean useTool(ITurtleAccess turtle, TurtleSide side, TurtleVerb verb, int direction) {
		return false;
	}

	@Override
	public Icon getIcon(ITurtleAccess turtle, TurtleSide side) {
		return CCAutomation.Icons.turtle;
	}
}
