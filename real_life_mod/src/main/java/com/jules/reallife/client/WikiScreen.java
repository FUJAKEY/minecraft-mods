package com.jules.reallife.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class WikiScreen extends Screen {
    private int page = 0;
    private static final int MAX_PAGES = 4; // 0 to 4 = 5 pages

    public WikiScreen() {
        super(new StringTextComponent("Real Life Wiki"));
    }

    @Override
    protected void init() {
        int x = this.width / 2;
        int y = this.height / 2;

        this.addButton(new Button(x - 100, y + 80, 80, 20, new StringTextComponent("Prev"), (btn) -> {
            if (page > 0) page--;
        }));

        this.addButton(new Button(x + 20, y + 80, 80, 20, new StringTextComponent("Next"), (btn) -> {
            if (page < MAX_PAGES) page++;
        }));
    }

    @Override
    public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(ms);
        super.render(ms, mouseX, mouseY, partialTicks);

        this.font.drawShadow(ms, new TranslationTextComponent("reallife.wiki.title").getString(), this.width / 2 - 40, 20, 0xFFFFFF);

        String key = "reallife.wiki.page" + page;
        this.font.drawWordWrap(new TranslationTextComponent(key), this.width / 2 - 100, 50, 200, 0xFFFFFF);

        this.font.drawShadow(ms, "Page " + (page + 1) + "/" + (MAX_PAGES + 1), this.width / 2 - 20, this.height / 2 + 110, 0xAAAAAA);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
