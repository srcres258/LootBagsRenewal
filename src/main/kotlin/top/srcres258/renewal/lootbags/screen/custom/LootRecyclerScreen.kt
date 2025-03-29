package top.srcres258.renewal.lootbags.screen.custom

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.RenderType
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import top.srcres258.renewal.lootbags.LootBags
import top.srcres258.renewal.lootbags.util.setShaderTexture

private val GUI_TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(LootBags.MOD_ID,
    "textures/gui/loot_recycler_gui.png")

class LootRecyclerScreen(
    menu: LootRecyclerMenu,
    playerInventory: Inventory,
    title: Component
) : LootBagContainerScreen<LootRecyclerMenu>(menu, playerInventory, title) {
    init {
        titleLabelX = 8
        titleLabelY = 4
        imageWidth = 176
        imageHeight = 131
        inventoryLabelX = 8
        inventoryLabelY = 38
    }

    override fun renderBg(guiGraphics: GuiGraphics, partialTick: Float, mouseX: Int, mouseY: Int) {
        setShaderTexture(0, GUI_TEXTURE)
        guiGraphics.blit(RenderType::guiTextured, GUI_TEXTURE, leftPos, topPos, 0F, 0F,
            imageWidth, imageHeight, 256, 256)
    }

    override fun renderLabels(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int) {
        super.renderLabels(guiGraphics, mouseX, mouseY)

        guiGraphics.drawString(font, Component.translatable("tooltip.lootbags.bag_storage.stored"),
            62, 15, 0x404040, false)
        guiGraphics.drawString(font, menu.storedBagAmount.toString(), 62, 24, 0x404040, false)
    }
}