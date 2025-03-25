package top.srcres258.renewal.lootbags.network.custom

import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import top.srcres258.renewal.lootbags.LootBags

data class ServerboundSelectLootBagTypePayload(val lootBagTypeOrdinal: Int) : CustomPacketPayload {
    companion object {
        val TYPE: CustomPacketPayload.Type<ServerboundSelectLootBagTypePayload> = CustomPacketPayload.Type(
            ResourceLocation.fromNamespaceAndPath(LootBags.MOD_ID, "select_loot_bag_type")
        )

        val STREAM_CODEC: StreamCodec<ByteBuf, ServerboundSelectLootBagTypePayload> = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            { it.lootBagTypeOrdinal },
            ::ServerboundSelectLootBagTypePayload
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE
}
