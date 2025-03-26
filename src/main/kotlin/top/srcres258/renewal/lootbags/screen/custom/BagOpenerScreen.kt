package top.srcres258.renewal.lootbags.screen.custom

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import top.srcres258.renewal.lootbags.LootBags
import top.srcres258.renewal.lootbags.util.setShaderTexture

private val GUI_TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(LootBags.MOD_ID,
    "textures/gui/bag_opener_gui.png")
private val PROGRESS_BAR_TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(LootBags.MOD_ID,
    "textures/gui/progress_bar.png")

class BagOpenerScreen(
    menu: BagOpenerMenu,
    playerInventory: Inventory,
    title: Component
) : AbstractContainerScreen<BagOpenerMenu>(menu, playerInventory, title) {
    init {
        imageWidth = 176
        imageHeight = 183
    }

    override fun renderLabels(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int) {
        // Override this method in order to let the screen only render the GUI title,
        // regardless of the player inventory title.
        guiGraphics.drawString(font, title, titleLabelX, titleLabelY, 0x404040, false)
    }

    override fun renderBg(guiGraphics: GuiGraphics, partialTick: Float, mouseX: Int, mouseY: Int) {
        setShaderTexture(0, GUI_TEXTURE)
        guiGraphics.blit(GUI_TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight)
        renderProgressBar(guiGraphics)
    }

    private fun renderProgressBar(guiGraphics: GuiGraphics) {
        if (menu.isCrafting) {
            setShaderTexture(0, PROGRESS_BAR_TEXTURE)
            guiGraphics.blit(PROGRESS_BAR_TEXTURE, leftPos + 7, topPos + 36, 0, 0, menu.scaledProgress, 6)
        }
    }

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        super.render(guiGraphics, mouseX, mouseY, partialTick)
        renderTooltip(guiGraphics, mouseX, mouseY)
    }
}