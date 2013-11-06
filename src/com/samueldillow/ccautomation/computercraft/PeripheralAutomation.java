package com.samueldillow.ccautomation.computercraft;

import java.util.Hashtable;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemInWorldManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import com.samueldillow.ccautomation.CCAutomation;
import com.samueldillow.ccautomation.Utility;

import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IHostedPeripheral;
import dan200.computer.api.ILuaContext;

public class PeripheralAutomation implements IHostedPeripheral {
	enum Methods {
		LOOK, 
		USE,
	}
	
	/** Hastable for quickly finding the direction requested from lua */
	private Hashtable<String, ComputerCraftDirection> directions = new Hashtable<String, ComputerCraftDirection>() {{
		put("b"       , ComputerCraftDirection.BACK);
		put("back"    , ComputerCraftDirection.BACK);
		put("backward", ComputerCraftDirection.BACK);
		put("d"       , ComputerCraftDirection.DOWN);
		put("down"    , ComputerCraftDirection.DOWN);
		put("f"       , ComputerCraftDirection.FORWARD);
		put("forward" , ComputerCraftDirection.FORWARD);
		put("u"       , ComputerCraftDirection.UP);
		put("up"      , ComputerCraftDirection.UP);
	}};
	
	private IPeripheralContext context;
	
	public PeripheralAutomation(IPeripheralContext context) {
		this.context = context;
	}

	@Override
	public void attach(IComputerAccess computer) {
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
		return new Object[] { "yay" };
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
			
			int x = (int) myPosition.xCoord;
			int y = (int) myPosition.yCoord;
			int z = (int) myPosition.zCoord;
			
			int blockID = 0;
			
			switch(direction) {
			case BACK:
				// Move the cursor to the coordinates that would be behind us 
				switch(context.getFacingDir()) {
				case 2: ++z; break; 
				case 3: --z; break; 
				case 4: ++x; break; 
				case 5: --x; break; 
				}
				break;
			case DOWN:
				// Move the cursor to the coordinates below us 
				--y;
				break;
			case FORWARD:
				// Move the cursor to the coordinates that would be in front of us 
				switch(context.getFacingDir()) {
				case 2: --z; break; 
				case 3: ++z; break; 
				case 4: --x; break; 
				case 5: ++x; break; 
				}
				break;
			case UP:
				// Move the cursor to the coordinates above us
				++y;
				break;
			default:
				System.err.printf("Direction % not supported", directionName);
				break;
			}
			
			// Get the block id we're supposed to be targeting
			blockID = world.getBlockId(x, y, z);
			// Load the block for this id
			Block block = (Block) Utility.at(blockID, Block.blocksList);
			
			// Only continue if the block is found
			if(block != null) {
				// If this tool can harvest the block, then harvest away
				if(item.canHarvestBlock(block)) {
					// Break the block
					world.destroyBlock(x, y, z, true);
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
	public ComputerCraftDirection getDirection(String direction) {
		// Look up the direction in our hashtable
		ComputerCraftDirection result = directions.get(direction);
		// Couldn't find the direction, so lowercase and search again
		if(result == null) result = directions.get(direction.toLowerCase());
		
		return result;
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

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		// Store metadata in block
	}
}
