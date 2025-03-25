package top.srcres258.renewal.lootbags.util

import net.minecraft.util.RandomSource
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

class LootGenerator(val bagType: LootBagType) {
    fun generateLoot(
        random: RandomSource = RandomSource.create(),
        maxLootGroupCount: Int = 3
    ): List<ItemStack> = when (bagType) {
        LootBagType.COMMON -> listOf(ItemStack(Items.STICK, random.nextInt(1, 10)))
        LootBagType.UNCOMMON -> listOf(ItemStack(Items.COAL, random.nextInt(1, 10)))
        LootBagType.RARE -> listOf(ItemStack(Items.IRON_INGOT, random.nextInt(1, 10)))
        LootBagType.EPIC -> listOf(ItemStack(Items.GOLD_INGOT, random.nextInt(1, 10)))
        LootBagType.LEGENDARY -> listOf(ItemStack(Items.DIAMOND, random.nextInt(1, 10)))
        LootBagType.PATIENT -> listOf(ItemStack(Items.EMERALD, random.nextInt(1, 10)))
        LootBagType.ARTIFICIAL -> listOf(ItemStack(Items.NETHERITE_INGOT, random.nextInt(1, 10)))
        LootBagType.BACON -> listOf(ItemStack(Items.PORKCHOP, random.nextInt(1, 10)))
    }
}