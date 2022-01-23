package com.dreamworld.gregtech.recipe;

import gregtech.api.util.GT_Recipe;

import java.util.HashSet;

import static gregtech.api.enums.GT_Values.E;
import static gregtech.api.enums.GT_Values.RES_PATH_GUI;

public class RecipeMaps {
	
	public static final GT_Recipe.GT_Recipe_Map FirstRecipeMap;
	public static final GT_Recipe.GT_Recipe_Map GeneratorRecipeMap;
	public static final GT_Recipe.GT_Recipe_Map ThirdRecipeMap;
	
	static {
		FirstRecipeMap = new GT_Recipe.GT_Recipe_Map(new HashSet<>(1000),
				"gt.recipe.first_map", "First Map", null, RES_PATH_GUI + "nei/Default",
				6, 6, 0, 0, 1, E, 1, E,
				true, true);
		GeneratorRecipeMap = new GT_Recipe.GT_Recipe_Map(new HashSet<>(1000),
				"gt.recipe.generator_map", "Generator Map", null, RES_PATH_GUI + "nei/Default",
				2, 2, 1, 0, 1, "Voltage: ", 1, " EU/t",
				false, true);
		ThirdRecipeMap = new GT_Recipe.GT_Recipe_Map(new HashSet<>(1000),
				"gt.recipe.third_map", "Third Map", null, RES_PATH_GUI + "nei/Default",
				6, 6, 0, 0, 1, E, 1, E,
				true, true);
	}
}