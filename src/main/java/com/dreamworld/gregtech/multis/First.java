package com.dreamworld.gregtech.multis;

import com.dreamworld.gregtech.recipe.RecipeMaps;
import com.dreamworld.util.Vector3i;
import com.dreamworld.util.Vector3ic;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.input.Keyboard;

import static com.dreamworld.util.MultisUtil.*;
import static gregtech.api.enums.Textures.BlockIcons.*;

public class First extends GT_MetaTileEntity_MultiBlockBase {
	
	public First(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
	}
	
	public First(String aName) {
		super(aName);
	}
	
	@Override
	public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new First(this.mName);
	}
	
	@Override
	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
		ITexture[] rTexture;
		if (aSide == aFacing) {
			if (aActive) {
				rTexture = new ITexture[]{
						casingTexturePages[1][48],
						TextureFactory.builder().addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE).extFacing().build()
				};
			} else {
				rTexture = new ITexture[]{
						casingTexturePages[1][48],
						TextureFactory.builder().addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR).extFacing().build()
				};
			}
		} else {
			rTexture = new ITexture[]{ casingTexturePages[1][48]};
		}
		return rTexture;
	}
	
	@Override
	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "VacuumFreezer.png");
	}
	
	@Override
	public String[] getDescription() {
		final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
		tt
				.addMachineType("???")
				.addInfo("Controller Block for the ???")
				.addInfo("???")
				.addSeparator()
				.beginStructureBlock(3, 3, 3, true)
				.addController("??")
				.addCasingInfo("???", 16)
				.addEnergyHatch("Any casing", 1)
				.addMaintenanceHatch("Any casing", 1)
				.addInputHatch("Any casing", 1)
				.addOutputHatch("Any casing", 1)
				.addInputBus("Any casing", 1)
				.addOutputBus("Any casing", 1)
				.toolTipFinisher("Dream World");
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
			return tt.getStructureInformation();
		return tt.getInformation();
	}
	
	@Override
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return RecipeMaps.FirstRecipeMap;
	}
	
	@Override
	public boolean isCorrectMachinePart(ItemStack aStack) {
		return true;
	}
	
	@Override
	public boolean checkRecipe(ItemStack aStack) {
		ItemStack[] tInputList = getCompactedInputs();
		FluidStack[] tFluidList = getCompactedFluids();
		
		long tVoltage = getMaxInputVoltage();
		byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));
		GT_Recipe tRecipe = getRecipeMap().findRecipe(getBaseMetaTileEntity(), false, GT_Values.V[tTier], tFluidList, tInputList);
		if (tRecipe != null) {
			if (tRecipe.isRecipeInputEqual(true, tFluidList, tInputList)) {
				this.mEfficiency         = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
				this.mEfficiencyIncrease = 10000;
				
				calculateOverclockedNessMulti(tRecipe.mEUt, tRecipe.mDuration, 1, tVoltage);
				//In case recipe is too OP for that machine
				if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1)
					return false;
				if (this.mEUt > 0) {
					this.mEUt = (-this.mEUt);
				}
				this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
				this.mOutputItems     = new ItemStack[]{tRecipe.getOutput(0)};
				this.mOutputFluids    = new FluidStack[]{tRecipe.getFluidOutput(0)};
				updateSlots();
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean checkMachine(IGregTechTileEntity c, ItemStack aStack) {
		
		int CASING_TEXTURE_ID = 176;
		
		Block GENERAL_CASING = GregTech_API.sBlockCasings8;
		int GENERAL_CASING_META = 0;
		
		Block PIPES = GregTech_API.sBlockCasings2;
		int PIPES_META = 13;
		
		Block SOLID_STEEL_CASING = GregTech_API.sBlockCasings2;
		int SOLID_STEEL_CASING_META = 0;
		
		boolean checkStructure = true;
		
		final Vector3ic forgeDirection = new Vector3i(
				ForgeDirection.getOrientation(c.getBackFacing()).offsetX,
				ForgeDirection.getOrientation(c.getBackFacing()).offsetY,
				ForgeDirection.getOrientation(c.getBackFacing()).offsetZ
		);
		
		for (int X = -2; X <= 2; X++) {
			for (int Z = -4; Z <= 0; Z++) {
				for (int Y = 0; Y <= 1; Y++) {
					
					if (Z == 0 && X == 0 && Y == 0) continue;
					
					if (X == -2 && (Z == 0 || Z == -4)) continue;
					if (X == 2 && (Z == 0 || Z == -4)) continue;
					
					final Vector3ic offset = rotateOffsetVector(forgeDirection, X, Y, Z);
					
					IGregTechTileEntity currentTE = c.getIGregTechTileEntityOffset(offset.x(), offset.y(), offset.z());
					
					if (!super.addMaintenanceToMachineList(currentTE, CASING_TEXTURE_ID)
							&& !super.addInputToMachineList(currentTE, CASING_TEXTURE_ID)
							&& !super.addMufflerToMachineList(currentTE, CASING_TEXTURE_ID)
							&& !super.addEnergyInputToMachineList(currentTE, CASING_TEXTURE_ID)
							&& !super.addOutputToMachineList(currentTE, CASING_TEXTURE_ID)) {
						
						if (!getBlock(c, offset, GENERAL_CASING, GENERAL_CASING_META)) {
//							setBlock(c, offset, GENERAL_CASING, GENERAL_CASING_META);
							checkStructure = false;
						}
					}
				}
			}
		}
		
		Vector3ic offsetPipes;
		offsetPipes = rotateOffsetVector(forgeDirection, 0, 2, -1);
		if (!getBlock(c, offsetPipes, PIPES, PIPES_META)) {
//			setBlock(c, offsetPipes, PIPES, PIPES_META);
			checkStructure = false;
		}
		offsetPipes = rotateOffsetVector(forgeDirection, 0, 2, -3);
		if (!getBlock(c, offsetPipes, PIPES, PIPES_META)) {
//			setBlock(c, offsetPipes, PIPES, PIPES_META);
			checkStructure = false;
		}
		offsetPipes = rotateOffsetVector(forgeDirection, -1, 2, -2);
		if (!getBlock(c, offsetPipes, PIPES, PIPES_META)) {
//			setBlock(c, offsetPipes, PIPES, PIPES_META);
			checkStructure = false;
		}
		offsetPipes = rotateOffsetVector(forgeDirection, 1, 2, -2);
		if (!getBlock(c, offsetPipes, PIPES, PIPES_META)) {
//			setBlock(c, offsetPipes, PIPES, PIPES_META);
			checkStructure = false;
		}
		
		for (int X = -1; X <= 1; X++) {
			for (int Z = -3; Z <= -1; Z++) {
				for (int Y = 2; Y <= 6; Y++) {
					 if (X == -1 && (Z == -1 || Z == -3)) {
						 final Vector3ic offset = rotateOffsetVector(forgeDirection, X, Y, Z);
						 if (!getBlock(c, offset, SOLID_STEEL_CASING, SOLID_STEEL_CASING_META)) {
//							 setBlock(c, offset, SOLID_STEEL_CASING, SOLID_STEEL_CASING_META);
							 checkStructure = false;
						 }
					 }
					 if (X == 1 && (Z == -1 || Z == -3)) {
						 final Vector3ic offset = rotateOffsetVector(forgeDirection, X, Y, Z);
						 if (!getBlock(c, offset, SOLID_STEEL_CASING, SOLID_STEEL_CASING_META)) {
//							 setBlock(c, offset, SOLID_STEEL_CASING, SOLID_STEEL_CASING_META);
							 checkStructure = false;
					 
						 }
					 }
				}
			}
		}
		
//		if (mInputBusses.size() > 5) {
//			checkStructure = false;
//		}
		
		return checkStructure;
	}
	
	@Override
	public int getMaxEfficiency(ItemStack aStack) {
		return 10000;
	}
	
	@Override
	public int getPollutionPerTick(ItemStack aStack) {
		return 0;
	}
	
	@Override
	public int getDamageToComponent(ItemStack aStack) {
		return 0;
	}
	
	@Override
	public boolean explodesOnComponentBreak(ItemStack aStack) {
		return false;
	}
}