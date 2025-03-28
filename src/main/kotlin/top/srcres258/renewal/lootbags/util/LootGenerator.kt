package top.srcres258.renewal.lootbags.util

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.Mth
import net.minecraft.util.RandomSource
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.storage.loot.*
import net.minecraft.world.level.storage.loot.entries.LootPoolEntry
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet
import org.apache.commons.lang3.mutable.MutableInt
import java.util.*
import kotlin.math.min

private val LOOT_TABLE_ENTRIES: Set<ResourceKey<LootTable>> by lazy {
    val result = mutableSetOf<ResourceKey<LootTable>>()

    // Include vanilla loot tables.
    result.addAll(BuiltInLootTables.all())

    // Include loot tables from all registered entities.
    for ((_, entity) in BuiltInRegistries.ENTITY_TYPE.entrySet()) {
        val lootTable = entity.defaultLootTable
        if (lootTable !in result) {
            result.add(lootTable)
        }
    }

    result
}

class LootGenerator(private val bagType: LootBagType) {
    fun generateLoot(
        level: ServerLevel,
        lootParamsBuilder: LootParams.Builder = LootParams.Builder(level)
    ): List<ItemStack> {
        val random = level.random

        return when (bagType) {
            // Equal to 1 common bag by its rarity.
            LootBagType.COMMON -> generateLootsFromLootTables(
                level, 0.0F, 50.0F, 1, 1,
                allowSameTable = true, allowSamePool = true, lootParamsBuilder = lootParamsBuilder
            )
            // Equal to 4 common bags by its rarity.
            LootBagType.UNCOMMON -> generateLootsFromLootTables(
                level, 0.0F, 50.0F, 1, 4,
                allowSameTable = true, allowSamePool = true, lootParamsBuilder = lootParamsBuilder
            )
            // Equal to 16 common bags by its rarity.
            LootBagType.RARE -> generateLootsFromLootTables(
                level, 0.0F, 100.0F, 2, 4,
                allowSameTable = false, allowSamePool = false, lootParamsBuilder = lootParamsBuilder
            )
            // Equal to 64 common bags by its rarity.
            LootBagType.EPIC -> generateLootsFromLootTables(
                level, 100.0F, 200.0F, 4, 8,
                allowSameTable = false, allowSamePool = false, lootParamsBuilder = lootParamsBuilder
            )
            // Equal to 256 common bags by its rarity.
            LootBagType.LEGENDARY -> generateLootsFromLootTables(
                level, 200.0F, 400.0F, 8, 16,
                allowSameTable = false, allowSamePool = false, lootParamsBuilder = lootParamsBuilder
            )
            // Equal to 1024 common bags by its rarity.
            LootBagType.PATIENT -> generateLootsFromLootTables(
                level, 400.0F, 800.0F, 16, 32,
                allowSameTable = false, allowSamePool = false, lootParamsBuilder = lootParamsBuilder
            )
            // Equal to 4096 common bags by its rarity.
            LootBagType.ARTIFICIAL -> generateLootsFromLootTables(
                level, 800.0F, 1024.0F, 32, 64,
                allowSameTable = true, allowSamePool = true, lootParamsBuilder = lootParamsBuilder
            )
            // Equal to 1 common bag by its rarity.
            LootBagType.BACON -> listOf(ItemStack(Items.PORKCHOP, random.nextIntBetweenInclusive(1, 8)))
        }
    }
}

private fun generateLootsFromLootTables(
    level: ServerLevel,
    luckMinimum: Float,
    luckMaximum: Float,
    tableCount: Int,
    poolCountPerTable: Int = 1,
    allowSameTable: Boolean = true,
    allowSamePool: Boolean = true,
    lootParamsBuilder: LootParams.Builder = LootParams.Builder(level)
): List<ItemStack> {
    val random = level.random
    val tried = mutableSetOf<Int>()
    val result = mutableListOf<ItemStack>()

    do {
        tried.clear()
        for (i in 0 ..< min(tableCount, LOOT_TABLE_ENTRIES.size)) {
            var tableIndex: Int
            if (allowSameTable) {
                tableIndex = random.nextInt(LOOT_TABLE_ENTRIES.size)
            } else {
                do {
                    tableIndex = random.nextInt(LOOT_TABLE_ENTRIES.size)
                } while (tableIndex in tried)
            }
            if (!allowSameTable) {
                tried.add(tableIndex)
            }

            val lootTableKey = LOOT_TABLE_ENTRIES.elementAt(tableIndex)
            val lootTable = level.server.reloadableRegistries().getLootTable(lootTableKey)
            if (lootTable.pools.isEmpty()) {
                continue
            }
            result.addAll(generateLootsFromLootPools(luckMinimum, luckMaximum, lootTable.pools,
                poolCountPerTable, allowSamePool, random, lootParamsBuilder))
        }
    } while (areItemStacksEmpty(result))

    return result.excludeEmptyElements()
}

private fun generateLootsFromLootPools(
    luckMinimum: Float,
    luckMaximum: Float,
    lootPools: List<LootPool>,
    poolCount: Int,
    allowSamePool: Boolean,
    random: RandomSource,
    lootParamsBuilder: LootParams.Builder,
    maxAttempts: Int = 100
): List<ItemStack> {
    val tried = mutableSetOf<Int>()
    val result = mutableListOf<ItemStack>()
    var attempts = 0

    do {
        tried.clear()
        for (i in 0 ..< min(poolCount, lootPools.size)) {
            var poolIndex: Int
            if (allowSamePool) {
                poolIndex = random.nextInt(lootPools.size)
            } else {
                do {
                    poolIndex = random.nextInt(lootPools.size)
                } while (poolIndex in tried)
            }

            val luck = if (luckMinimum >= luckMaximum) {
                luckMaximum
            } else {
                Mth.randomBetween(random, luckMinimum, luckMaximum)
            }

            val pool = lootPools[poolIndex]
            val lootParams = lootParamsBuilder.withLuck(luck)
                .create(LootContextParamSet.builder().build())
            addRandomItemFromLootPoolEntries(
                pool.entries,
                result::add,
                LootContext.Builder(lootParams).create(Optional.empty())
            )
        }
        attempts++
    } while (areItemStacksEmpty(result) && attempts <= maxAttempts)

    return result.excludeEmptyElements()
}

private fun addRandomItemFromLootPoolEntries(
    entries: List<LootPoolEntryContainer>,
    stackConsumer: (ItemStack) -> Unit,
    context: LootContext
) {
    val random = context.random
    val list = mutableListOf<LootPoolEntry>()
    val mutableInt = MutableInt()

    for (container in entries) {
        container.expand(context) { entry ->
            val k = entry.getWeight(context.luck)
            if (k > 0) {
                list.add(entry)
                mutableInt.add(k)
            }
        }
    }

    val i = list.size
    if (mutableInt.value != 0 && i != 0) {
        if (i == 1) {
            list[0].createItemStack(stackConsumer, context)
        } else {
            var j = random.nextInt(mutableInt.value)

            for (entry in list) {
                j -= entry.getWeight(context.luck)
                if (j < 0) {
                    entry.createItemStack(stackConsumer, context)
                    break
                }
            }
        }
    }
}

private fun areItemStacksEmpty(stacks: List<ItemStack>): Boolean =
    stacks.isEmpty() || stacks.all { it.isEmpty }

private fun List<ItemStack>.excludeEmptyElements(): List<ItemStack> =
    mutableListOf<ItemStack>().also { result ->
        for (stack in this) {
            if (!stack.isEmpty) {
                result.add(stack)
            }
        }
    }