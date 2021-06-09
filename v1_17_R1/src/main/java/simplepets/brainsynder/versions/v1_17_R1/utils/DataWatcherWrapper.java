package simplepets.brainsynder.versions.v1_17_R1.utils;

import net.minecraft.server.v1_16_R3.*;

import java.util.Optional;
import java.util.UUID;

public class DataWatcherWrapper {
    public static final DataWatcherSerializer<String> STRING;
    public static final DataWatcherSerializer<Byte> BYTE;
    public static final DataWatcherSerializer<Integer> INT;
    public static final DataWatcherSerializer<Float> FLOAT;
    public static final DataWatcherSerializer<Boolean> BOOLEAN;
    public static final DataWatcherSerializer<BlockPosition> BLOCK_POS;
    public static final DataWatcherSerializer<Optional<UUID>> UUID;
    public static final DataWatcherSerializer<Optional<IBlockData>> BLOCK;
    public static final DataWatcherSerializer<VillagerData> DATA;

    static {
        STRING = DataWatcherRegistry.d;
        BYTE = DataWatcherRegistry.a;
        INT = DataWatcherRegistry.b;
        FLOAT = DataWatcherRegistry.c;
        BOOLEAN = DataWatcherRegistry.i;
        BLOCK_POS = DataWatcherRegistry.l;
        UUID = DataWatcherRegistry.o;
        BLOCK = DataWatcherRegistry.h;
        DATA = DataWatcherRegistry.q;
    }
}