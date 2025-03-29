package top.srcres258.renewal.lootbags.screen.custom

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import top.srcres258.renewal.lootbags.LootBags
import top.srcres258.renewal.lootbags.util.LootBagType
import top.srcres258.renewal.lootbags.util.setShaderTexture

private val GUI_TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(LootBags.MOD_ID,
    "textures/gui/bag_storage_gui.png")

class BagStorageScreen(
    menu: BagStorageMenu,
    playerInventory: Inventory,
    title: Component
) : LootBagContainerScreen<BagStorageMenu>(menu, playerInventory, title) {
    private lateinit var switchBagTypeButton: Button

    init {
        imageWidth = 176
        imageHeight = 147
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
        setShaderTexture(0, GUI_TEXTURE)
        guiGraphics.blit(GUI_TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight)
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