package com.pipimod.energy.jei;

import com.pipimod.energy.EnergyMod;
import com.pipimod.energy.ModBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.gui.IRecipeLayout;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;

/**
 * Recipe category for the Metal Filler shown in JEI.
 */
public class MetalFillerCategory implements IRecipeCategory<MetalFillerRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(EnergyMod.MODID, "metal_filler");

    private final IDrawable background;
    private final IDrawable icon;

    public MetalFillerCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(150, 40);
        this.icon = helper.createDrawableIngredient(new ItemStack(ModBlocks.METAL_FILLER.get()));
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends MetalFillerRecipe> getRecipeClass() {
        return MetalFillerRecipe.class;
    }

    @Override
    public String getTitle() {
        return "Metal Filler";
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setIngredients(MetalFillerRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(Arrays.asList(
                Ingredient.of(recipe.getFuel()),
                Ingredient.of(recipe.getInput())
        ));
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getOutput());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, MetalFillerRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
        stacks.init(0, true, 19, 12);
        stacks.set(0, recipe.getFuel());

        stacks.init(1, true, 55, 12);
        stacks.set(1, recipe.getInput());

        stacks.init(2, false, 111, 12);
        stacks.set(2, recipe.getOutput());
    }

    @Override
    public boolean isHandled(MetalFillerRecipe recipe) {
        return true;
    }
}
