package com.pipimod.energy;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class WireConfigScreen extends Screen {
    private final BlockPos pos;
    private final WireBlockEntity wire;

    public WireConfigScreen(BlockPos pos, WireBlockEntity wire) {
        super(new net.minecraft.util.text.TranslationTextComponent("screen.energymod.title"));
        this.pos = pos;
        this.wire = wire;
    }

    private String label(Direction dir) {
        String side = new net.minecraft.util.text.TranslationTextComponent("direction." + dir.getSerializedName()).getString();
        String mode = new net.minecraft.util.text.TranslationTextComponent("wiremode." + wire.getMode(dir).name().toLowerCase()).getString();
        return side + ": " + mode;
    }

    @Override
    protected void init() {
        int y = this.height / 4;
        for (Direction dir : Direction.values()) {
            Button btn = new Button(this.width / 2 - 100, y, 200, 20, new StringTextComponent(label(dir)), b -> {
                WireMode m = wire.getMode(dir).next();
                EnergyMod.CHANNEL.sendToServer(new SetWireModePacket(pos, dir, m));
                wire.setMode(dir, m);
                b.setMessage(new StringTextComponent(label(dir)));
            });
            this.addButton(btn);
            y += 24;
        }
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);
        fillGradient(stack, 0, 0, this.width, this.height, 0xC0101010, 0xD0101010);
        super.render(stack, mouseX, mouseY, partialTicks);

        Direction facing = Minecraft.getInstance().player.getDirection();
        Direction left = facing.getCounterClockWise();
        Direction right = facing.getClockWise();
        Direction back = facing.getOpposite();

        String top = new TranslationTextComponent("direction." + facing.getSerializedName()).getString();
        String leftText = new TranslationTextComponent("direction." + left.getSerializedName()).getString();
        String rightText = new TranslationTextComponent("direction." + right.getSerializedName()).getString();
        String bottom = new TranslationTextComponent("direction." + back.getSerializedName()).getString();

        this.font.draw(stack, top, this.width/2f - this.font.width(top)/2f, 10, 0xFFFFFF);
        this.font.draw(stack, leftText, 5, this.height/2f - this.font.lineHeight/2f, 0xFFFFFF);
        this.font.draw(stack, rightText, this.width - 5 - this.font.width(rightText), this.height/2f - this.font.lineHeight/2f, 0xFFFFFF);
        this.font.draw(stack, bottom, this.width/2f - this.font.width(bottom)/2f, this.height - 10 - this.font.lineHeight, 0xFFFFFF);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
