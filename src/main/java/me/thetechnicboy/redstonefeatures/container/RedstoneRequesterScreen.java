package me.thetechnicboy.redstonefeatures.container;

import me.thetechnicboy.redstonefeatures.Redstonefeatures;
import me.thetechnicboy.redstonefeatures.packets.ModPackets;
import me.thetechnicboy.redstonefeatures.packets.TextUpdatePacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import org.lwjgl.glfw.GLFW;

import java.net.MalformedURLException;
import java.net.URL;

public class RedstoneRequesterScreen extends AbstractContainerScreen<RedstoneRequesterMenu> {
    private static final ResourceLocation GUI = Redstonefeatures.genRL("textures/gui/redstone_requester.png");
    private EditBox textField;

    public RedstoneRequesterScreen(RedstoneRequesterMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
    }

    @Override
    protected void init() {
        super.init();
        textField = new EditBox(this.font, this.leftPos + 10, this.topPos + 35, this.imageWidth - 20, 20, Component.literal("Enter Text"));
        this.addRenderableWidget(textField);
        textField.setMaxLength(100);
        textField.setValue(menu.mURL);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTicks);
        super.renderTooltip(graphics, mouseX, mouseY);

        graphics.drawString(this.font, Component.translatable("gui.redstonefeatures.redstone_requester.enterURL"), this.leftPos + 10, this.topPos + 25, 0xFFFFFF, false );

        textField.render(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float p_97788_, int p_97789_, int p_97790_) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        graphics.blit(GUI, x, y, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (textField.isFocused() && keyCode == GLFW.GLFW_KEY_ENTER) {
            String enteredText = textField.getValue();
            boolean success = false;
            try {
                new URL(enteredText);
                success = true;
            } catch (MalformedURLException e) {}

            if(success) ModPackets.sendToServer(new TextUpdatePacket(menu.mPos, enteredText));

            if(success) this.minecraft.player.playSound(SoundEvents.NOTE_BLOCK_BELL.get(), 1.0F, 1.0F);
            if(!success) this.minecraft.player.playSound(SoundEvents.NOTE_BLOCK_BASS.get(), 1.0F, 0.5F);

            if(success) this.minecraft.setScreen(null);

            return true;
        }

        if (textField.isFocused() && keyCode == GLFW.GLFW_KEY_E) return true;

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

}
