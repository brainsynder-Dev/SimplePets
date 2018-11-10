package simplepets.brainsynder.nms.v1_13_R1.utils;

import net.minecraft.server.v1_13_R1.BlockPosition;
import net.minecraft.server.v1_13_R1.DataWatcherRegistry;
import net.minecraft.server.v1_13_R1.DataWatcherSerializer;
import net.minecraft.server.v1_13_R1.IBlockData;

import java.util.Optional;
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
        BOOLEAN = DataWatcherRegistry.i;
        BLOCK_POS = DataWatcherRegistry.l;
        UUID = DataWatcherRegistry.o;
        BLOCK = DataWatcherRegistry.h;
    }
}