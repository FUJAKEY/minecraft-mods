package com.pipimod.energy.jei;

import com.pipimod.energy.EnergyMod;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.util.ResourceLocation;
import java.util.Collections;

public class EnergyJeiPlugin implements IModPlugin {
    public static final ResourceLocation ID = new ResourceLocation(EnergyMod.MODID, "jei_plugin");

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
        registration.addRecipes(Collections.singletonList(new MetalFillerRecipe()), MetalFillerCategory.UID);
    }
}
