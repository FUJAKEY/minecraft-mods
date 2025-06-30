package com.pipimod.energy;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;

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
        super.render(stack, mouseX, mouseY, partialTicks);
        String facing = Minecraft.getInstance().player.getDirection().getSerializedName();
        String text = new net.minecraft.util.text.TranslationTextComponent("screen.energymod.facing", facing).getString();
        this.font.draw(stack, text, this.width/2f - this.font.width(text)/2f, 20, 0xFFFFFF);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
