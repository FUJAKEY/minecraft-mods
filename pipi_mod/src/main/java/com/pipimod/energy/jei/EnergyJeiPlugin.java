package com.pipimod.energy.jei;

import com.pipimod.energy.EnergyMod;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.util.ResourceLocation;
import java.util.Collections;

public class EnergyJeiPlugin implements IModPlugin {
    public static final ResourceLocation ID = new ResourceLocation(EnergyMod.MODID, "jei_plugin");
    public static final RecipeType<MetalFillerRecipe> METAL_FILLER_TYPE = RecipeType.create(EnergyMod.MODID, "metal_filler", MetalFillerRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper helper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new MetalFillerCategory(helper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(METAL_FILLER_TYPE, Collections.singletonList(new MetalFillerRecipe()));
    }
}
