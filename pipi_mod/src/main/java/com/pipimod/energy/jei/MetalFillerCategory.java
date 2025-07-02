package com.pipimod.energy.jei;

import com.pipimod.energy.EnergyMod;
import com.pipimod.energy.ModBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class MetalFillerCategory implements IRecipeCategory<MetalFillerRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(EnergyMod.MODID, "metal_filler");
    private final IDrawable background;
    private final IDrawable icon;

    public MetalFillerCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(150, 40);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM, new ItemStack(ModBlocks.METAL_FILLER.get()));
    }

    @Override
    public RecipeType<MetalFillerRecipe> getRecipeType() {
        return EnergyJeiPlugin.METAL_FILLER_TYPE;
    }

    @Override
    public ITextComponent getTitle() {
        return new StringTextComponent("Metal Filler");
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
    public void setRecipe(IRecipeLayoutBuilder builder, MetalFillerRecipe recipe, IFocusGroup focuses) {
        IRecipeSlotBuilder fuel = builder.addSlot(RecipeIngredientRole.INPUT, 19, 12);
        fuel.addItemStack(recipe.getFuel());

        IRecipeSlotBuilder input = builder.addSlot(RecipeIngredientRole.INPUT, 55, 12);
        input.addItemStack(recipe.getInput());

        IRecipeSlotBuilder output = builder.addSlot(RecipeIngredientRole.OUTPUT, 111, 12);
        output.addItemStack(recipe.getOutput());
    }
}
