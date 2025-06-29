package com.pipimod.energy;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class WireToolItem extends Item {
    public WireToolItem(Properties props) { super(props); }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        World world = context.getLevel();
        if (!world.isClientSide) {
            if (world.getBlockEntity(context.getClickedPos()) instanceof WireBlockEntity) {
                WireBlockEntity wire = (WireBlockEntity) world.getBlockEntity(context.getClickedPos());
                Direction side = context.getClickedFace();
                wire.toggleMode(side);
                PlayerEntity player = context.getPlayer();
                if (player != null) {
                    player.displayClientMessage(new StringTextComponent("Mode " + wire.getMode(side) + " on " + side), true);
                }
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }
}
