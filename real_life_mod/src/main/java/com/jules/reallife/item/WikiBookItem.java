package com.jules.reallife.item;

import com.jules.reallife.client.WikiScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;

public class WikiBookItem extends Item {
    public WikiBookItem() {
        super(new Item.Properties().tab(ItemGroup.TAB_MISC).stacksTo(1));
        setRegistryName("wiki_book");
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (worldIn.isClientSide) {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> openGui());
        }
        return ActionResult.success(playerIn.getItemInHand(handIn));
    }

    @OnlyIn(Dist.CLIENT)
    private void openGui() {
        Minecraft.getInstance().setScreen(new WikiScreen());
    }
}
