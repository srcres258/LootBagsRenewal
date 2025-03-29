package top.srcres258.renewal.lootbags.screen.custom

import net.minecraft.core.NonNullList
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.*
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.neoforged.neoforge.network.PacketDistributor
import top.srcres258.renewal.lootbags.block.ModBlocks
import top.srcres258.renewal.lootbags.block.entity.custom.BagStorageBlockEntity
import top.srcres258.renewal.lootbags.network.custom.ServerboundSelectLootBagTypePayload
import top.srcres258.renewal.lootbags.screen.ModMenuTypes
import top.srcres258.renewal.lootbags.util.LootBagType

class BagStorageMenu(
    containerId: Int,
    inv: Inventory,
    level: Level,
    val blockEntity: BagStorageBlockEntity,
    val data: ContainerData
) : LootBagContainerMenu(
    ModMenuTypes.BAG_STORAGE.get(),
    containerId,
    inv,
    level,
    data,
    blockEntity.itemHandler,
    blockEntity.itemHandler,
    26, 16,
    135, 16,
    8, 66,
    8, 123
) {
    constructor(
        containerId: Int,
        inv: Inventory,
        level: Level,
        extraData: FriendlyByteBuf
    ) : this(containerId, inv, level, level.getBlockEntity(extraData.readBlockPos()) as BagStorageBlockEntity,
        SimpleContainerData(BagStorageBlockEntity.ContainerDataType.entries.size).also { data ->
            /*
             * Bug fix: calling `broadcastChanges` whenever a value within DataSlot gets changed may not actually
             *  send changes to the server due to inner value-change-detecting mechanics of vanilla's DataSlot
             *  (detects whether values have been changed by checking their previous values). Hence, we set the
             *  initial value of some slots to be a fancy number (here we use 114514 as one of the choices)
             *  to ensure DataSlot's value-change detection is actually going to work.
             */
            data.set(BagStorageBlockEntity.ContainerDataType.TARGET_BAG_TYPE.ordinal, 114514)
        })

    init {
        setSynchronizer(object : ContainerSynchronizer {
            override fun sendInitialData(
                container: AbstractContainerMenu,
                items: NonNullList<ItemStack>,
                carriedItem: ItemStack,
                initialData: IntArray
            ) {}

            override fun sendSlotChange(container: AbstractContainerMenu, slot: Int, itemStack: ItemStack) {}

            override fun sendCarriedChange(containerMenu: AbstractContainerMenu, stack: ItemStack) {}

            override fun sendDataChange(container: AbstractContainerMenu, id: Int, value: Int) {
                if (id == BagStorageBlockEntity.ContainerDataType.TARGET_BAG_TYPE.ordinal &&
                    level.isClientSide) {
                    // When the target loot bag type changes, and it's on the client,
                    // send it to the server to update the target bag type.
                    sendSelectLootBagTypePayloadToServer(value)
                }
            }
        })
    }

    val storedBagAmount: Int
        get() = data.get(BagStorageBlockEntity.ContainerDataType.STORED_BAG_AMOUNT.ordinal)

    override var targetBagType: LootBagType
        get() = LootBagType.entries[data.get(BagStorageBlockEntity.ContainerDataType.TARGET_BAG_TYPE.ordinal) %
                LootBagType.entries.size]
        set(value) {
            data.set(BagStorageBlockEntity.ContainerDataType.TARGET_BAG_TYPE.ordinal, value.ordinal)
            broadcastChanges()
        }

    override val targetBagAmount: Int
        get() = (storedBagAmount.toFloat() * LootBagType.COMMON.amountFactorEquivalentTo(targetBagType)).toInt()

    override fun stillValid(player: Player): Boolean =
        stillValid(ContainerLevelAccess.create(level, blockEntity.blockPos), player, ModBlocks.BAG_STORAGE.get())
}

private fun sendSelectLootBagTypePayloadToServer(value: Int) {
    PacketDistributor.sendToServer(ServerboundSelectLootBagTypePayload(value))
}