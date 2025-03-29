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

private val BAG_STORAGE_GUI_TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(LootBags.MOD_ID,
    "textures/gui/bag_storage_gui.png")

abstract class LootBagContainerScreen<T : LootBagContainerMenu>(
    menu: T,
    playerInventory: Inventory,
    title: Component
) : AbstractContainerScreen<T>(menu, playerInventory, title) {
    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        super.render(guiGraphics, mouseX, mouseY, partialTick)

        renderTooltip(guiGraphics, mouseX, mouseY)
    }

    override fun renderSlot(guiGraphics: GuiGraphics, slot: Slot) {
        // Override this method to render fake the loot bag item if no loot bag from the output slot is available.

        if (slot is LootBagSlotItemHandler && slot.isOutputSlot &&
            menu is LootBagContainerMenu && menu.targetBagAmount == 0) {
            // Render a fake loot bag item.
            val stack = ItemStack(menu.targetBagType.asItem())
            renderSlotContents(guiGraphics, stack, slot, null)
        } else {
            super.renderSlot(guiGraphics, slot)
        }
    }
    override fun renderSlotContents(guiGraphics: GuiGraphics, itemstack: ItemStack, slot: Slot, countString: String?) {
        super.renderSlotContents(guiGraphics, itemstack, slot, countString)

        // Render the unavailable icon if no target bag can be obtained.
        if (slot is LootBagSlotItemHandler && slot.isOutputSlot && menu.targetBagAmount == 0) {
            // The unavailable icon texture is inside the bag storage GUI texture, so switch to that texture at first.
            setShaderTexture(0, BAG_STORAGE_GUI_TEXTURE)
            renderUnavailableIcon(guiGraphics, slot.x, slot.y)
        }
    }
}

private fun renderUnavailableIcon(guiGraphics: GuiGraphics, originX: Int, originY: Int) {
    guiGraphics.blit(BAG_STORAGE_GUI_TEXTURE, originX, originY, 176, 0, 16, 16)
}