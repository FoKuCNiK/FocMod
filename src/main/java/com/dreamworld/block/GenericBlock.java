package com.dreamworld.block;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class GenericBlock extends GT_API_BLOCK {
	
	private String iconName;
	private IIcon icon;
	
	public GenericBlock(String unlocalizedName) {
		setBlockName(unlocalizedName);
		setHardness(1.0f);
		setResistance(1.0f);
		GameRegistry.registerBlock(this, unlocalizedName);
		iconName = unlocalizedName;
	}
	
	@Override
	public void registerBlockIcons(IIconRegister ir) {
		icon = ir.registerIcon("dreamworld:" + iconName);
	}
	
	@Override
	@SuppressWarnings({"unchecked"})
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
		par3List.add(new ItemStack(par1, 1, 0));
	}
	
	@Override
	public IIcon getIcon(int side, int meta) {
		return icon;
	}
}