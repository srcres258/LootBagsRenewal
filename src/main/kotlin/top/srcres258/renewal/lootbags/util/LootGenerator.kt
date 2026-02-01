package top.srcres258.renewal.lootbags.util

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.Mth
import net.minecraft.util.RandomSource
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.storage.loot.*
import net.minecraft.world.level.storage.loot.BuiltInLootTables.ABANDONED_MINESHAFT
import net.minecraft.world.level.storage.loot.BuiltInLootTables.IGLOO_CHEST
import net.minecraft.world.level.storage.loot.BuiltInLootTables.RUINED_PORTAL
import net.minecraft.world.level.storage.loot.BuiltInLootTables.SIMPLE_DUNGEON
import net.minecraft.world.level.storage.loot.BuiltInLootTables.SPAWN_BONUS_CHEST
import net.minecraft.world.level.storage.loot.BuiltInLootTables.STRONGHOLD_CORRIDOR
import net.minecraft.world.level.storage.loot.BuiltInLootTables.STRONGHOLD_CROSSING
import net.minecraft.world.level.storage.loot.BuiltInLootTables.STRONGHOLD_LIBRARY
import net.minecraft.world.level.storage.loot.BuiltInLootTables.VILLAGE_ARMORER
import net.minecraft.world.level.storage.loot.BuiltInLootTables.VILLAGE_BUTCHER
import net.minecraft.world.level.storage.loot.BuiltInLootTables.VILLAGE_CARTOGRAPHER
import net.minecraft.world.level.storage.loot.BuiltInLootTables.VILLAGE_DESERT_HOUSE
import net.minecraft.world.level.storage.loot.BuiltInLootTables.VILLAGE_FISHER
import net.minecraft.world.level.storage.loot.BuiltInLootTables.VILLAGE_FLETCHER
import net.minecraft.world.level.storage.loot.BuiltInLootTables.VILLAGE_MASON
import net.minecraft.world.level.storage.loot.BuiltInLootTables.VILLAGE_PLAINS_HOUSE
import net.minecraft.world.level.storage.loot.BuiltInLootTables.VILLAGE_SAVANNA_HOUSE
import net.minecraft.world.level.storage.loot.BuiltInLootTables.VILLAGE_SHEPHERD
import net.minecraft.world.level.storage.loot.BuiltInLootTables.VILLAGE_SNOWY_HOUSE
import net.minecraft.world.level.storage.loot.BuiltInLootTables.VILLAGE_TAIGA_HOUSE
import net.minecraft.world.level.storage.loot.BuiltInLootTables.VILLAGE_TANNERY
import net.minecraft.world.level.storage.loot.BuiltInLootTables.VILLAGE_TEMPLE
import net.minecraft.world.level.storage.loot.BuiltInLootTables.VILLAGE_TOOLSMITH
import net.minecraft.world.level.storage.loot.BuiltInLootTables.VILLAGE_WEAPONSMITH
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

private val LOOT_TABLE_ENTRIES_COMMON: Set<ResourceKey<LootTable>> by lazy {
    val result = mutableSetOf<ResourceKey<LootTable>>()

    // 从原版战利品表中筛选村庄相关的战利品表
    val villageLootTables = setOf(
        VILLAGE_WEAPONSMITH,
        VILLAGE_TOOLSMITH,
        VILLAGE_ARMORER,
        VILLAGE_MASON,
        VILLAGE_SHEPHERD,
        VILLAGE_BUTCHER,
        VILLAGE_FLETCHER,
        VILLAGE_FISHER,
        VILLAGE_TANNERY,
        VILLAGE_TEMPLE,
        VILLAGE_DESERT_HOUSE,
        VILLAGE_PLAINS_HOUSE,
        VILLAGE_TAIGA_HOUSE,
        VILLAGE_SNOWY_HOUSE,
        VILLAGE_SAVANNA_HOUSE,
        VILLAGE_CARTOGRAPHER,
    )

    result.addAll(villageLootTables)
    result
}

private val LOOT_TABLE_ENTRIES_UNCOMMON: Set<ResourceKey<LootTable>> by lazy {
    val result = mutableSetOf<ResourceKey<LootTable>>()

    val villageLootTables = setOf(
        //以下为普通物品
        VILLAGE_WEAPONSMITH,
        VILLAGE_TOOLSMITH,
        VILLAGE_ARMORER,
        VILLAGE_MASON,
        VILLAGE_SHEPHERD,
        VILLAGE_BUTCHER,
        VILLAGE_FLETCHER,
        VILLAGE_FISHER,
        VILLAGE_TANNERY,
        VILLAGE_TEMPLE,
        VILLAGE_DESERT_HOUSE,
        VILLAGE_PLAINS_HOUSE,
        VILLAGE_TAIGA_HOUSE,
        VILLAGE_SNOWY_HOUSE,
        VILLAGE_SAVANNA_HOUSE,
        VILLAGE_CARTOGRAPHER,
        //以下为高级物品
        SPAWN_BONUS_CHEST,
        // 简单地牢
        SIMPLE_DUNGEON,
        // 废弃矿井
        ABANDONED_MINESHAFT,
        // 要塞相关
        STRONGHOLD_LIBRARY,
        STRONGHOLD_CROSSING,
        STRONGHOLD_CORRIDOR,
        // 沙漠神殿
        BuiltInLootTables.DESERT_PYRAMID,
        // 丛林神庙
        BuiltInLootTables.JUNGLE_TEMPLE,
        // 雪屋箱子
        IGLOO_CHEST,
        // 水下废墟
        BuiltInLootTables.UNDERWATER_RUIN_SMALL,
        BuiltInLootTables.UNDERWATER_RUIN_BIG,
        // 埋藏的宝藏
        BuiltInLootTables.BURIED_TREASURE,
        // 沉船相关
        BuiltInLootTables.SHIPWRECK_MAP,
        BuiltInLootTables.SHIPWRECK_SUPPLY,
        BuiltInLootTables.SHIPWRECK_TREASURE,
        // 掠夺者前哨站
        BuiltInLootTables.PILLAGER_OUTPOST,
        // 林地府邸
        BuiltInLootTables.WOODLAND_MANSION,
        // 毁灭传送门
        RUINED_PORTAL,
        // 古城
        BuiltInLootTables.ANCIENT_CITY,
        BuiltInLootTables.ANCIENT_CITY_ICE_BOX,
        // 试炼 chambers
        BuiltInLootTables.TRIAL_CHAMBERS_REWARD,
        BuiltInLootTables.TRIAL_CHAMBERS_REWARD_COMMON,
        BuiltInLootTables.TRIAL_CHAMBERS_REWARD_RARE,
        BuiltInLootTables.TRIAL_CHAMBERS_REWARD_UNIQUE,
        BuiltInLootTables.TRIAL_CHAMBERS_SUPPLY,
        BuiltInLootTables.TRIAL_CHAMBERS_CORRIDOR,
        BuiltInLootTables.TRIAL_CHAMBERS_INTERSECTION,
        BuiltInLootTables.TRIAL_CHAMBERS_ENTRANCE,
        BuiltInLootTables.EQUIPMENT_TRIAL_CHAMBER,
        BuiltInLootTables.EQUIPMENT_TRIAL_CHAMBER_RANGED,
        BuiltInLootTables.EQUIPMENT_TRIAL_CHAMBER_MELEE,
        // 钓鱼相关
        BuiltInLootTables.FISHING,
        BuiltInLootTables.FISHING_JUNK,
        BuiltInLootTables.FISHING_FISH,
        BuiltInLootTables.FISHING_TREASURE,

        BuiltInLootTables.DESERT_WELL_ARCHAEOLOGY,
        BuiltInLootTables.DESERT_PYRAMID_ARCHAEOLOGY,
        BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_COMMON,
        BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_RARE,
        BuiltInLootTables.OCEAN_RUIN_WARM_ARCHAEOLOGY,
        BuiltInLootTables.OCEAN_RUIN_COLD_ARCHAEOLOGY

    )

    result.addAll(villageLootTables)
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
                allowSameTable = true, allowSamePool = true,
                lootTableSet = LOOT_TABLE_ENTRIES_COMMON, // 使用村庄战利品表
                lootParamsBuilder = lootParamsBuilder
            )
            // Equal to 4 common bags by its rarity.
            LootBagType.UNCOMMON -> generateLootsFromLootTables(
                level, 0.0F, 50.0F, 1, 4,
                allowSameTable = true, allowSamePool = true,
                lootTableSet = LOOT_TABLE_ENTRIES_UNCOMMON, // 使用UNCOMMON战利品表
                lootParamsBuilder = lootParamsBuilder
            )
            // Equal to 16 common bags by its rarity.
            LootBagType.RARE -> generateLootsFromLootTables(
                level, 0.0F, 100.0F, 2, 4,
                allowSameTable = false, allowSamePool = false,
                lootTableSet = LOOT_TABLE_ENTRIES, // 使用完整战利品表
                lootParamsBuilder = lootParamsBuilder
            )
            // Equal to 64 common bags by its rarity.
            LootBagType.EPIC -> generateLootsFromLootTables(
                level, 100.0F, 200.0F, 4, 8,
                allowSameTable = false, allowSamePool = false,
                lootTableSet = LOOT_TABLE_ENTRIES, // 使用完整战利品表
                lootParamsBuilder = lootParamsBuilder
            )
            // Equal to 256 common bags by its rarity.
            LootBagType.LEGENDARY -> generateLootsFromLootTables(
                level, 200.0F, 400.0F, 8, 16,
                allowSameTable = false, allowSamePool = false,
                lootTableSet = LOOT_TABLE_ENTRIES, // 使用完整战利品表
                lootParamsBuilder = lootParamsBuilder
            )
            // Equal to 1024 common bags by its rarity.
            LootBagType.PATIENT -> generateLootsFromLootTables(
                level, 400.0F, 800.0F, 16, 32,
                allowSameTable = false, allowSamePool = false,
                lootTableSet = LOOT_TABLE_ENTRIES, // 使用完整战利品表
                lootParamsBuilder = lootParamsBuilder
            )
            // Equal to 4096 common bags by its rarity.
            LootBagType.ARTIFICIAL -> generateLootsFromLootTables(
                level, 800.0F, 1024.0F, 32, 64,
                allowSameTable = true, allowSamePool = true,
                lootTableSet = LOOT_TABLE_ENTRIES, // 使用完整战利品表
                lootParamsBuilder = lootParamsBuilder
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
    lootTableSet: Set<ResourceKey<LootTable>> = LOOT_TABLE_ENTRIES, // 新增参数
    lootParamsBuilder: LootParams.Builder = LootParams.Builder(level)
): List<ItemStack> {
    val random = level.random
    val tried = mutableSetOf<Int>()
    val result = mutableListOf<ItemStack>()

    do {
        tried.clear()
        for (i in 0 ..< min(tableCount, lootTableSet.size)) { // 使用传入的集合
            var tableIndex: Int
            if (allowSameTable) {
                tableIndex = random.nextInt(lootTableSet.size)
            } else {
                do {
                    tableIndex = random.nextInt(lootTableSet.size)
                } while (tableIndex in tried)
            }
            if (!allowSameTable) {
                tried.add(tableIndex)
            }

            val lootTableKey = lootTableSet.elementAt(tableIndex) // 使用传入的集合
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
