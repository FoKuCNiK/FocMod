package com.dreamworld;

import codechicken.nei.recipe.HandlerInfo;
import com.dreamworld.block.GenericBlock;
import com.dreamworld.gregtech.multis.First;
import com.dreamworld.gregtech.item.Item;
import com.dreamworld.gregtech.multis.Second;
import com.dreamworld.gregtech.multis.Third;
import com.dreamworld.gregtech.recipe.AddRecipe;
import com.dreamworld.gregtech.recipe.RecipeMaps;
import cpw.mods.fml.common.event.*;
import gregtech.api.enums.Materials;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import static codechicken.nei.recipe.GuiRecipeTab.handlerMap;

public class CommonProxy {

    // preInit "Run before anything else. Read your config, create blocks, items,
    // etc, and register them with the GameRegistry."
    public void preInit(FMLPreInitializationEvent event) 	{
        Config.syncronizeConfiguration(event.getSuggestedConfigurationFile());

        DreamWorld.info(Config.greeting);
        DreamWorld.info("I am " + Tags.MODNAME + " at version " + Tags.VERSION + " and group name " + Tags.GROUPNAME);
    }

    // load "Do your mod setup. Build whatever data structures you care about. Register recipes."
    public void init(FMLInitializationEvent event) {
        registerBlocks();
        registerMultis();
    }
    
    private void registerMultis() {
        Item.FIRST.set(new First(24000, "dreamworld.multis.first", "First Multis").getStackForm(1));
        Item.SECOND.set(new Second(24001, "dreamworld.multis.second", "Second Multis").getStackForm(1));
        Item.THIRD.set(new Third(24002, "dreamworld.multis.third", "Third Multis").getStackForm(1));
    }
    
    private void registerBlocks() {
        Item.FIRST_CASING.set(new ItemStack(new GenericBlock("first_casing")));
    }

    // postInit "Handle interaction with other mods, complete your setup based on this."
    public void postInit(FMLPostInitializationEvent event) {
        registerRecipes();
        registerNEIPicture();
    }
    
    private void registerNEIPicture() {
        HandlerInfo info = new HandlerInfo.Builder(RecipeMaps.FirstRecipeMap.mUnlocalizedName, Tags.MODNAME, Tags.MODID)
                .setDisplayStack(Item.FIRST.get(1)).setMaxRecipesPerPage(2).setHeight(135).setWidth(166).setShiftY(6).build();
        handlerMap.put(info.getHandlerName(), info);
    
        info = new HandlerInfo.Builder(RecipeMaps.GeneratorRecipeMap.mUnlocalizedName, Tags.MODNAME, Tags.MODID)
                .setDisplayStack(Item.SECOND.get(1)).setMaxRecipesPerPage(2).setHeight(135).setWidth(166).setShiftY(6).build();
        handlerMap.put(info.getHandlerName(), info);
    
        info = new HandlerInfo.Builder(RecipeMaps.ThirdRecipeMap.mUnlocalizedName, Tags.MODNAME, Tags.MODID)
                .setDisplayStack(Item.THIRD.get(1)).setMaxRecipesPerPage(2).setHeight(135).setWidth(166).setShiftY(6).build();
        handlerMap.put(info.getHandlerName(), info);
    }
    
    private void registerRecipes() {
        AddRecipe.addToFirstMap(
                new ItemStack[]{new ItemStack(Blocks.stone, 2), new ItemStack(Blocks.sponge, 1)},
                new ItemStack[]{Item.FIRST_CASING.get(1)},
                new FluidStack[]{Materials.Oxygen.getGas(1000), Materials.Water.getFluid(33)},
                new FluidStack[]{Materials.Water.getFluid(1000), Materials.Oxygen.getGas(50)},
                20 * 20, 480
        );
        
        AddRecipe.addGeneratorFuel(
                new ItemStack[]{new ItemStack(Blocks.stone, 2), new ItemStack(Blocks.sponge, 0)},
                new ItemStack[]{Item.FIRST_CASING.get(1)},
                new FluidStack[]{Materials.Oxygen.getGas(1000), Materials.Water.getFluid(33)},
                new FluidStack[]{Materials.Water.getFluid(1000), Materials.Oxygen.getGas(50)},
                20 * 50, 512
        );
    
        AddRecipe.addThirdMap(
                new ItemStack[]{new ItemStack(Blocks.dirt, 2), new ItemStack(Blocks.sponge, 0)},
                new ItemStack[]{new ItemStack(Blocks.stone, 12)},
                new int[]{5000}, 20 * 1
        );
    }
}
