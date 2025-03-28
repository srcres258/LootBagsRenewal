package top.srcres258.renewal.lootbags.screen.custom

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import top.srcres258.renewal.lootbags.LootBags
import top.srcres258.renewal.lootbags.util.setShaderTexture

private val GUI_TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(LootBags.MOD_ID,
    "textures/gui/loot_recycler_gui.png")
private val BAG_STORAGE_GUI_TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(LootBags.MOD_ID,
    "textures/gui/bag_storage_gui.png")

class LootRecyclerScreen(
    menu: LootRecyclerMenu,
    playerInventory: Inventory,
    title: Component
) : AbstractContainerScreen<LootRecyclerMenu>(menu, playerInventory, title) {
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
        guiGraphics.blit(GUI_TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight)
    }

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        super.render(guiGraphics, mouseX, mouseY, partialTick)

        renderTooltip(guiGraphics, mouseX, mouseY)
    }

    override fun renderSlotContents(guiGraphics: GuiGraphics, itemstack: ItemStack, slot: Slot, countString: String?) {
        super.renderSlotContents(guiGraphics, itemstack, slot, countString)

        // Render the unavailable icon if no target bag can be obtained.
        if (slot is BagStorageSlotItemHandler && slot.isOutputSlot && menu.targetBagAmount == 0) {
            // The unavailable icon texture is inside the bag storage GUI texture, so switch to that texture at first.
            setShaderTexture(0, BAG_STORAGE_GUI_TEXTURE)
            renderUnavailableIcon(guiGraphics, slot.x, slot.y)
        }
    }

    override fun renderLabels(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int) {
        super.renderLabels(guiGraphics, mouseX, mouseY)

        guiGraphics.drawString(font, Component.translatable("tooltip.lootbags.bag_storage.stored"),
            62, 15, 0x404040, false)
        guiGraphics.drawString(font, menu.storedBagAmount.toString(), 62, 24, 0x404040, false)
    }
}

private fun renderUnavailableIcon(guiGraphics: GuiGraphics, originX: Int, originY: Int) {
    guiGraphics.blit(BAG_STORAGE_GUI_TEXTURE, originX, originY, 176, 0, 16, 16)
}