package net.ynov.createnuclear.multiblock.input;

import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.ynov.createnuclear.gui.CNGuiTextures;

public class ReactorInputScreen extends AbstractSimiContainerScreen<ReactorInputMenu> {

    protected static final CNGuiTextures background = CNGuiTextures.REACTOR_SLOT_INVENTOR;

    public ReactorInputScreen(ReactorInputMenu container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    @Override
    protected void init() {
        setWindowSize(background.width, background.height+ 4 + AllGuiTextures.PLAYER_INVENTORY.height);
        setWindowOffset(0,0);
        super.init();
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = leftPos;
        int y = topPos;

        background.render(guiGraphics, x, y);

    }
}
