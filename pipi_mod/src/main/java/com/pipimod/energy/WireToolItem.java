package com.pipimod.energy;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.tileentity.TileEntity;

public class WireToolItem extends Item {
    public WireToolItem(Properties props) { super(props); }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        World world = context.getLevel();
        if (!world.isClientSide) {
            Direction side = context.getClickedFace();
            BlockPos pos = context.getClickedPos();
            TileEntity te = world.getBlockEntity(pos);
            if (!(te instanceof WireBlockEntity)) {
                pos = pos.relative(side);
                te = world.getBlockEntity(pos);
                side = side.getOpposite();
            }
            if (te instanceof WireBlockEntity) {
                WireBlockEntity wire = (WireBlockEntity) te;
                wire.toggleMode(side);
                WireMode after = wire.getMode(side);
                PlayerEntity player = context.getPlayer();
                if (player != null) {
                    String modeStr = new TranslationTextComponent("wiremode." + after.name().toLowerCase()).getString();
                    String sideStr = new TranslationTextComponent("direction." + side.getSerializedName()).getString();
                    player.displayClientMessage(new TranslationTextComponent("message.energymod.mode", modeStr, sideStr), true);
                }
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }
}
