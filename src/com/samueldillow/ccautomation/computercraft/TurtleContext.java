package com.samueldillow.ccautomation.computercraft;

import dan200.turtle.api.ITurtleAccess;
import dan200.turtle.api.TurtleSide;

public class TurtleContext implements IPeripheralContext {
	private ITurtleAccess turtle = null;
	private TurtleSide side = null;

	public TurtleContext(ITurtleAccess turtle, TurtleSide side) {
		this.turtle = turtle;
		this.side = side;
	}
}
