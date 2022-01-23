package com.dreamworld.gregtech.recipe;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class AddRecipe {
	
	public static void addToFirstMap(ItemStack[] aInputs, ItemStack[] aOutput, FluidStack[] fInputs, FluidStack[] fOutput, int aDuration, int aEUt) {
		RecipeMaps.FirstRecipeMap.addRecipe(true, aInputs, aOutput, null, null, fInputs, fOutput, aDuration, aEUt, 0);
	}
	
	public static void addGeneratorFuel(ItemStack[] aInputs, ItemStack[] aOutput, FluidStack[] fInputs, FluidStack[] fOutput, int aDuration, int aEUt) {
		RecipeMaps.GeneratorRecipeMap.addRecipe(true, aInputs, aOutput, null, null, fInputs, fOutput, aDuration, 0, aEUt);
	}
	
	public static void addThirdMap(ItemStack[] aInputs, ItemStack[] aOutput, int[] chance, int aDuration) {
		RecipeMaps.ThirdRecipeMap.addRecipe(true, aInputs, aOutput, null, chance, null, null, aDuration, 0, 0);
	}
}