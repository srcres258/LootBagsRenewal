package top.srcres258.renewal.lootbags.screen.custom

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import top.srcres258.renewal.lootbags.LootBags
import top.srcres258.renewal.lootbags.util.LootBagType

private val GUI_TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(LootBags.MOD_ID, "textures/gui/bag_storage_gui.png")

class BagStorageScreen(
    menu: BagStorageMenu,
    playerInventory: Inventory,
    title: Component
) : AbstractContainerScreen<BagStorageMenu>(menu, playerInventory, title) {
    private lateinit var switchBagTypeButton: Button

    init {
        imageWidth = 175
        imageHeight = 146
        inventoryLabelX = 8
        inventoryLabelY = 54
    }

    override fun init() {
        super.init()

        switchBagTypeButton = addRenderableWidget(
            object : Button(builder(Component.translatable("tooltip.lootbags.bag_storage.switch_bag_type")) {
                var bagTypeIndex = (menu.targetBagType.ordinal + 1) % LootBagType.entries.size
                while (LootBagType.entries[bagTypeIndex].creativeOnly) {
                    bagTypeIndex++
                    if (bagTypeIndex >= LootBagType.entries.size) {
                        bagTypeIndex %= LootBagType.entries.size
                    }
                }
                menu.targetBagType = LootBagType.entries[bagTypeIndex]
            }.bounds(leftPos + 44, topPos + 40, 90, 16)) {
                override fun isHoveredOrFocused(): Boolean = isHovered
            }
        )
    }

    override fun renderBg(guiGraphics: GuiGraphics, partialTick: Float, mouseX: Int, mouseY: Int) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader)
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F)
        RenderSystem.setShaderTexture(0, GUI_TEXTURE)

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
            RenderSystem.setShader(GameRenderer::getPositionTexShader)
            RenderSystem.setShaderColor(1F, 1F, 1F, 1F)
            RenderSystem.setShaderTexture(0, GUI_TEXTURE)
            renderUnavailableIcon(guiGraphics, slot.x, slot.y)
        }
    }

    override fun resize(minecraft: Minecraft, width: Int, height: Int) {
        super.resize(minecraft, width, height)
    }

    override fun renderLabels(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int) {
        super.renderLabels(guiGraphics, mouseX, mouseY)

        // Stored
        guiGraphics.drawString(font, Component.translatable("tooltip.lootbags.bag_storage.stored"),
            44, 16, 0x404040, false)
        guiGraphics.drawString(font, menu.storedBagAmount.toString(),
            44, 25, 0x404040, false)
        // Needed
        val neededAmount = menu.targetBagType.amountFactorEquivalentTo(LootBagType.COMMON).toInt()
        guiGraphics.drawString(font, Component.translatable("tooltip.lootbags.bag_storage.needed"),
            88, 16, 0x404040, false)
        guiGraphics.drawString(font, neededAmount.toString(),
            88, 25, 0x404040, false)
    }
}

private fun renderUnavailableIcon(guiGraphics: GuiGraphics, originX: Int, originY: Int) {
    guiGraphics.blit(GUI_TEXTURE, originX, originY, 176, 0, 16, 16)
}