package com.dreamworld.gregtech.multis;

import com.dreamworld.gregtech.recipe.RecipeMaps;
import com.dreamworld.util.Vector3i;
import com.dreamworld.util.Vector3ic;
import gregtech.api.GregTech_API;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.input.Keyboard;

import static com.dreamworld.util.MultisUtil.getBlock;
import static com.dreamworld.util.MultisUtil.rotateOffsetVector;
import static gregtech.api.enums.Textures.BlockIcons.*;

public class Third extends GT_MetaTileEntity_MultiBlockBase {
	
	public Third(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
	}
	
	public Third(String aName) {
		super(aName);
	}
	
	@Override
	public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new Third(this.mName);
	}
	
	@Override
	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
		ITexture[] rTexture;
		if (aSide == aFacing) {
			if (aActive) {
				rTexture = new ITexture[]{
						casingTexturePages[0][10],
						TextureFactory.builder().addIcon(MACHINE_BRONZEBLASTFURNACE_ACTIVE).extFacing().build()
				};
			} else {
				rTexture = new ITexture[]{
						casingTexturePages[0][10],
						TextureFactory.builder().addIcon(MACHINE_BRONZEBLASTFURNACE).extFacing().build()
				};
			}
		} else {
			rTexture = new ITexture[]{casingTexturePages[0][10]};
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
		return RecipeMaps.ThirdRecipeMap;
	}
	
	@Override
	public boolean isCorrectMachinePart(ItemStack aStack) {
		return true;
	}
	
	protected void noMaintenance() {
		mWrench        = true;
		mScrewdriver   = true;
		mSoftHammer    = true;
		mHardHammer    = true;
		mSolderingTool = true;
		mCrowbar       = true;
	}
	
	@Override
	public boolean checkRecipe(ItemStack aStack) {
		ItemStack[] tInputList = getCompactedInputs();
		FluidStack[] tFluidList = getCompactedFluids();
		
		GT_Recipe tRecipe = getRecipeMap().findRecipe(getBaseMetaTileEntity(), false, 0, tFluidList, tInputList);
		if (tRecipe != null) {
			if (tRecipe.isRecipeInputEqual(true, tFluidList, tInputList)) {
				this.mEfficiency         = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
				this.mEfficiencyIncrease = 10000;
				
				ItemStack[] tOut = new ItemStack[tRecipe.mOutputs.length];
				for (int h = 0; h < tRecipe.mOutputs.length; h++) {
					if (tRecipe.getOutput(h) != null) {
						tOut[h] = tRecipe.getOutput(h).copy();
						tOut[h].stackSize = 0;
					}
				}
				
				for (int f = 0; f < tOut.length; f++) {
					if (tRecipe.mOutputs[f] != null && tOut[f] != null) {
						if (getBaseMetaTileEntity().getRandomNumber(10000) < tRecipe.getOutputChance(f)) {
							tOut[f].stackSize += tRecipe.mOutputs[f].stackSize;
						}
					}
				}
				
				this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
				this.mOutputItems     = tOut;
				this.mOutputFluids    = new FluidStack[]{tRecipe.getFluidOutput(0)};
				updateSlots();
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean checkMachine(IGregTechTileEntity c, ItemStack aStack) {
		noMaintenance();
		int CASING_TEXTURE_ID = 10;
		Block GENERAL_CASING = GregTech_API.sBlockCasings1;
		int GENERAL_CASING_META = 10;
		
		boolean checkStructure = true;
		
		final Vector3ic forgeDirection = new Vector3i(
				ForgeDirection.getOrientation(c.getBackFacing()).offsetX,
				ForgeDirection.getOrientation(c.getBackFacing()).offsetY,
				ForgeDirection.getOrientation(c.getBackFacing()).offsetZ
		);
		
		for (int X = -1; X <= 1; X++) {
			for (int Z = -2; Z <= 0; Z++) {
				for (int Y = -1; Y <= 2; Y++) {
					if (Z == 0 && X == 0 && Y == 0) continue;
					
					if (X == 0 && Y == 0 && Z == -1) continue;
					if (X == 0 && Y == 1 && Z == -1) continue;
					
					final Vector3ic offset = rotateOffsetVector(forgeDirection, X, Y, Z);
					IGregTechTileEntity currentTE = c.getIGregTechTileEntityOffset(offset.x(), offset.y(), offset.z());
					
					if (!super.addInputToMachineList(currentTE, CASING_TEXTURE_ID)
							&& !super.addMaintenanceToMachineList(currentTE, CASING_TEXTURE_ID)
							&& !super.addOutputToMachineList(currentTE, CASING_TEXTURE_ID)) {
						if (!getBlock(c, offset, GENERAL_CASING, GENERAL_CASING_META)) {
//							setBlock(c, offset, GENERAL_CASING, GENERAL_CASING_META);
							checkStructure = false;
						}
					}
				}
			}
		}
		
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