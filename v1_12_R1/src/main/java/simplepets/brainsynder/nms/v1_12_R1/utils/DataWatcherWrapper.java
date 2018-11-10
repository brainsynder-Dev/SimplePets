package simplepets.brainsynder.nms.v1_12_R1.utils;

import com.google.common.base.Optional;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.DataWatcherRegistry;
import net.minecraft.server.v1_12_R1.DataWatcherSerializer;
import net.minecraft.server.v1_12_R1.IBlockData;

import java.util.UUID;

public class DataWatcherWrapper {
    public static final DataWatcherSerializer<Byte> BYTE;
    public static final DataWatcherSerializer<Integer> INT;
    public static final DataWatcherSerializer<Float> FLOAT;
    public static final DataWatcherSerializer<Boolean> BOOLEAN;
    public static final DataWatcherSerializer<BlockPosition> BLOCK_POS;
    public static final DataWatcherSerializer<Optional<UUID>> UUID;
    public static final DataWatcherSerializer<Optional<IBlockData>> BLOCK;

    static {
        BYTE = DataWatcherRegistry.a;
        INT = DataWatcherRegistry.b;
        FLOAT = DataWatcherRegistry.c;
        BOOLEAN = DataWatcherRegistry.h;
        BLOCK_POS = DataWatcherRegistry.j;
        UUID = DataWatcherRegistry.m;
        BLOCK = DataWatcherRegistry.g;
    }
}
