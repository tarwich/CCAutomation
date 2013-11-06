package com.samueldillow.ccautomation.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

import com.samueldillow.ccautomation.CCAutomation;
import com.samueldillow.ccautomation.tile.TileCCAutomation;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class BlockCCAutomation extends Block {
	public static BlockCCAutomation register() {
		// Instance that we're going to return
		BlockCCAutomation self = new BlockCCAutomation();
		// Name this block
		LanguageRegistry.addName(self, "Automation Block");
		// Make the recipe for our block
		GameRegistry.addRecipe(new ItemStack(self, 1), 
			"sss",
			"sis",
			"sss",
			's', Block.stone,
			'i', Item.ingotIron
		);
		
		return self;
	}
	
	public BlockCCAutomation() {
		super(CCAutomation.Config.blockId, Material.cake);
		setHardness(2f);
		GameRegistry.registerBlock(this, CCAutomation.Config.automationAdjective);
		GameRegistry.registerTileEntity(TileCCAutomation.class, CCAutomation.Config.automationAdjective);
		setCreativeTab(creativeTab());
		setLightOpacity(3);
	}
	
	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		return new TileCCAutomation(world);
	}
	
	public CreativeTabs creativeTab() {
		try {
			Class<?> cc = Class.forName("dan200.ComputerCraft");
			return (CreativeTabs) cc.getDeclaredField("creativeTab").get(cc);
		} catch(Exception ignore) {}
		
	    return CreativeTabs.tabRedstone;
	}
	
	@Override
	public Icon getIcon(int side, int metadata) {
		switch(side) {
		case 0: case 1: 
			return CCAutomation.Icons.top;
		case 4: 
			return CCAutomation.Icons.front;
		default: 
			return CCAutomation.Icons.side;
		}
	}
	
	@Override
	public int getRenderType() {
		return super.getRenderType();
	}
	
	@Override
	public void registerIcons(IconRegister iconRegister) {
		// Setup the turtle icons
		CCAutomation.Icons.turtle = iconRegister.registerIcon("ccautomation:cameraTurtle");
		CCAutomation.Icons.top    = iconRegister.registerIcon("ccautomation:cameraTop");
		CCAutomation.Icons.front  = iconRegister.registerIcon("ccautomation:camera");
		CCAutomation.Icons.side   = iconRegister.registerIcon("ccautomation:cameraSide");
		// Let the parent do any work it needs
		super.registerIcons(iconRegister);
	}
}
