package top.srcres258.renewal.lootbags.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

data class BagStorageRecord(val storedBagAmount: Int, val targetBagTypeOrdinal: Int) {
    companion object {
        val CODEC: Codec<BagStorageRecord> = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.INT.fieldOf("storedBagAmount").forGetter { it.storedBagAmount },
                Codec.INT.fieldOf("targetBagTypeOrdinal").forGetter { it.targetBagTypeOrdinal }
            ).apply(instance, ::BagStorageRecord)
        }
        val STREAM_CODEC: StreamCodec<ByteBuf, BagStorageRecord> = StreamCodec.composite(
            ByteBufCodecs.INT, { it.storedBagAmount },
            ByteBufCodecs.INT, { it.targetBagTypeOrdinal },
            ::BagStorageRecord
        )
    }
}
