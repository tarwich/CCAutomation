package com.samueldillow.ccautomation.computercraft;

import java.util.Hashtable;
import java.util.Vector;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import com.samueldillow.ccautomation.CCAutomation;
import com.samueldillow.ccautomation.Utility;

import dan200.computer.api.ComputerCraftAPI;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IHostedPeripheral;
import dan200.computer.api.ILuaContext;
import dan200.computer.api.IMount;

public class PeripheralAutomation implements IHostedPeripheral {
	enum Methods {
		LOOK, 
		USE,
	}
	
	private IPeripheralContext context;
	
	public PeripheralAutomation(IPeripheralContext context) {
		this.context = context;
	}

	private Vec3 addDirection(ComputerCraftDirection direction, Vec3 vector) {
		switch(direction) {
		case BACK   : vector = Vec3.createVectorHelper(-vector.xCoord, 0d, -vector.zCoord); break;
		case DOWN   : vector = Vec3.createVectorHelper(0, -1, 0)                          ; break;
		case FORWARD: vector = Vec3.createVectorHelper(vector.xCoord, 0d, vector.zCoord)  ; break;
		case UP     : vector = Vec3.createVectorHelper(0, 1, 0)                           ; break;
		}
		return vector;
	}

	@Override
	public void attach(IComputerAccess computer) {
		// Get a mount for the lua files in this jar
		IMount mount = AssetMount.getMount();
		// Mount the files in the computer 
		if(mount != null) computer.mount("ccautomation", mount);
		// Error
		else CCAutomation.log.severe("Cannot access my own lua files");
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext luaContext, int method, Object[] arguments) throws Exception {
		switch(Methods.values()[method]) {
		case LOOK: return doLook(computer, luaContext, arguments);
		case USE : return doUse(computer, luaContext, arguments);
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
		// The result we're going to return (-1 shouldn't match any known blocks)
		int blockID = -1;
		// Get the world from the context (for block id lookup)
		World world = context.getWorld();
		// The first argument should be the direction name
		String directionName = (String) Utility.at(0, arguments);
		// Change the direction name into a direction
		ComputerCraftDirection direction = getDirection(directionName);
		// This is for seeking, and is declared up here so that it can be returned 
		int i = 0;
		
		// The direction was a standard direction
		if(direction != null) {
			// Get the orientation of the peripheral and adjust by direction
			Vec3 vector = addDirection(direction, vector(context.getFacingDir()));
			// Change the vector from relative to absolute
			vector = context.getPosition().addVector(vector.xCoord, vector.yCoord, vector.zCoord);
			// Lookup the block in the world
			blockID = world.getBlockId((int)vector.xCoord, (int)vector.yCoord, (int)vector.zCoord);
		}
		
		// The direction was not a standard direction
		else {
			// This is an orientation for seeking. Null means don't seek
			Vec3 orientation = null;
			
			// Seek forwards until we find a non-air block
			if("ahead".startsWith(directionName)) 
				// Get the direction we're facing in world coordinates
				orientation = vector(context.getFacingDir());
			// Seek upwards until we find a non-air block
			else if("above".startsWith(directionName)) orientation = vector(0, 1, 0);
			// Seek downwards until we find a non-air block
			else if("below".startsWith(directionName)) orientation = vector(0, -1, 0);
			
			// Seek in the direction of orientation (if non-null)
			if(orientation != null) {
				// Start the caret at our context position
				Vec3 vector = context.getPosition();
				
				// We're going to walk in this 
				for(i=0; i<CCAutomation.Config.sightDistance; ++i) {
					// Move forward (or whatever direction orientation is)
					vector = vector.addVector(orientation.xCoord, orientation.yCoord, orientation.zCoord);
					// Look at the block here
					blockID = world.getBlockId((int)vector.xCoord, (int)vector.yCoord, (int)vector.zCoord);
					// If this isn't air, then vision stops
					if(blockID > 0) break; 
				}
			}
		}
		
		return new Object[] { blockID, i+1 };
	}

	private Object[] doUse(IComputerAccess computer, ILuaContext luaContext, Object[] arguments) {
		// Get the currently selected item
		ItemStack item = context.getSelectedItem();
		// Actually, we want a tool
		ItemTool tool = (ItemTool) item.getItem();
		
		// Only continue if the item is non-null
		if(tool != null) {
			// Get the name of the direction that we're supposed to be using
			String directionName = Utility.cast(Utility.at(0, arguments), "forward");
			// Get the direction that we're supposed to be using
			ComputerCraftDirection direction = getDirection(directionName);
			// Get my location as a starting point
			Vec3 myPosition = context.getPosition();
			// Get the world that I'm installed in
			World world = context.getWorld();
			// A cursor in the world to use for affecting blocks
			Vec3 vector = vector(context.getFacingDir());
			// Move the cursor by the desired direction
			vector = addDirection(direction, vector);
			// Change the cursor from relative to absolute
			vector = vector.addVector(myPosition.xCoord, myPosition.yCoord, myPosition.zCoord);
			// Get the block id we're supposed to be targeting
			int blockID = world.getBlockId((int)vector.xCoord, (int)vector.yCoord, (int)vector.zCoord);
			// Load the block for this id
			Block block = (Block) Utility.at(blockID, Block.blocksList);
			
			// Only continue if the block is found
			if(block != null) {
				// If this tool can harvest the block, then harvest away
				if(item.canHarvestBlock(block)) {
					// Break the block
					world.destroyBlock((int) vector.xCoord, (int) vector.yCoord, (int) vector.zCoord, true);
					// Damage the tool
					item.setItemDamage(item.getItemDamage() + 1);
					return new Object[] { true };
				}
			}
			
		}
		
		return new Object[] { Boolean.FALSE };
	}

	/**
	 * Resolve a direction from lua to the list of known directions
	 * 
	 * This method will return null on failure
	 * 
	 * @param direction The direction to resolve
	 * 
	 * @return The {@link ComputerCraftDirection} for this direction, or null on failure
	 */
	@SuppressWarnings("unused")
	public ComputerCraftDirection getDirection(String direction) {
		if(false) ;
		else if("back"   .startsWith(direction)) return ComputerCraftDirection.BACK;
		else if("down"   .startsWith(direction)) return ComputerCraftDirection.DOWN;
		else if("forward".startsWith(direction)) return ComputerCraftDirection.FORWARD;
		else if("up"     .startsWith(direction)) return ComputerCraftDirection.UP;
		
		return null;
	}

	@Override
	public String[] getMethodNames() {
		return new String[] { 
			"look",
			"use"
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
	
	/**
	 * Create a Vec3 from a series of coordinates
	 * 
	 * This method will simply call the Vec3 builder, but since it's such a strange builder, we're making a wrapper so 
	 * that in the the event that it changes, calls can be fixed in one central location.
	 * 
	 * @param x The x coordinate
	 * @param y The y coordinate 
	 * @param z The z coordinate
	 * 
	 * @return The newly created vector
	 */
	private Vec3 vector(int x, int y, int z) {
		return Vec3.createVectorHelper(x, y, z);
	}

	/**
	 * Resolve a ComputerCraft direction to a vector
	 * 
	 * @param facingDir The facing direction from ComputerCraft
	 * 
	 * @return A vector indicating motion along an axis
	 */
	private Vec3 vector(int facingDir) {
		switch(facingDir) {
		case  2: return vector( 0, 0,-1);
		case  3: return vector( 0, 0, 1);
		case  4: return vector(-1, 0, 0);
		case  5: return vector( 1, 0, 0);
		default: return vector( 1, 0, 0);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		// Store metadata in block
	}
}
