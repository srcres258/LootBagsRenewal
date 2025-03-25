package top.srcres258.renewal.lootbags.util

import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import top.srcres258.renewal.lootbags.item.ModItems
import top.srcres258.renewal.lootbags.item.custom.LootBagItem
import kotlin.math.pow

enum class LootBagType(
    /**
     * The identifier of the loot bag type, used as the registry name of the item.
     */
    val itemId: String,
    /**
     * Rarity of the loot bag type, the higher the rarity is, the rarer the loot bag is.
     */
    val rarity: Int,
    /**
     * Whether this type of loot bag can be dropped by vanilla through loot tables of entities.
     *
     * If set to `false`, it can only be obtained through loot bag storage blocks or by crafting.
     */
    val droppable: Boolean,
    /**
     * Whether this type of loot bag is only available in creative mode (neither available in survival nor by crafting).
     */
    val creativeOnly: Boolean
) : ItemLike {
    COMMON("common_loot_bag", 0, true, false),
    UNCOMMON("uncommon_loot_bag", 1, true, false),
    RARE("rare_loot_bag", 2, true, false),
    EPIC("epic_loot_bag", 3, true, false),
    LEGENDARY("legendary_loot_bag", 4, true, false),
    PATIENT("patient_loot_bag", 5, false, false),
    ARTIFICIAL("artificial_loot_bag", 6, false, true),
    BACON("bacon_loot_bag", 0, true, false);

    companion object {
        /**
         * The amount of loot bags required to reach the next rarity level.
         * (e.g. craft the loot bag of the next rarity level in crafting tables,
         * get one within the bag storage block, etc.)
         */
        const val AMOUNT_TO_NEXT_RARITY = 4
    }

    val lootGenerator = LootGenerator(this)

    override fun asItem(): Item = when (this) {
        COMMON -> ModItems.COMMON_LOOT_BAG.get()
        UNCOMMON -> ModItems.UNCOMMON_LOOT_BAG.get()
        RARE -> ModItems.RARE_LOOT_BAG.get()
        EPIC -> ModItems.EPIC_LOOT_BAG.get()
        LEGENDARY -> ModItems.LEGENDARY_LOOT_BAG.get()
        PATIENT -> ModItems.PATIENT_LOOT_BAG.get()
        ARTIFICIAL -> ModItems.ARTIFICIAL_LOOT_BAG.get()
        BACON -> ModItems.BACON_LOOT_BAG.get()
    }

    /**
     * Calculate the amount factor number which is equivalent to the other loot bag type.
     *
     * For example, 1 uncommon loot bag is equivalent to 4 common loot bags, so calling
     * this method on UNCOMMON with COMMON as the parameter will return `4.0F`, and `0.25F`
     * vice versa.
     */
    fun amountFactorEquivalentTo(other: LootBagType): Float =
        AMOUNT_TO_NEXT_RARITY.toFloat().pow(this.rarity - other.rarity)
}

fun LootBagItem.asLootBagType(): LootBagType = when (this) {
    ModItems.COMMON_LOOT_BAG.get() -> LootBagType.COMMON
    ModItems.UNCOMMON_LOOT_BAG.get() -> LootBagType.UNCOMMON
    ModItems.RARE_LOOT_BAG.get() -> LootBagType.RARE
    ModItems.EPIC_LOOT_BAG.get() -> LootBagType.EPIC
    ModItems.LEGENDARY_LOOT_BAG.get() -> LootBagType.LEGENDARY
    ModItems.PATIENT_LOOT_BAG.get() -> LootBagType.PATIENT
    ModItems.ARTIFICIAL_LOOT_BAG.get() -> LootBagType.ARTIFICIAL
    ModItems.BACON_LOOT_BAG.get() -> LootBagType.BACON
    else -> throw IllegalArgumentException("Invalid loot bag item: $this")
}