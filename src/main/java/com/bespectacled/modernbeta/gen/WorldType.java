package com.bespectacled.modernbeta.gen;

import com.bespectacled.modernbeta.gen.provider.*;
import com.bespectacled.modernbeta.gen.provider.settings.ChunkProviderSettings;
import com.bespectacled.modernbeta.util.TriFunction;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public enum WorldType {
    BETA("beta", BetaChunkProvider::new),
    SKYLANDS("skylands", SkylandsChunkProvider::new),
    ALPHA("alpha", AlphaChunkProvider::new),
    INFDEV("infdev", InfdevChunkProvider::new),
    INFDEV_OLD("infdev_old", InfdevOldChunkProvider::new),
    INDEV("indev", IndevChunkProvider::new),
    FLAT("flat", FlatChunkProvider::new),
    NETHER("nether", NetherChunkProvider::new);
    
    private final String name;
    private final TriFunction<Long, ChunkGeneratorSettings, ChunkProviderSettings, AbstractChunkProvider> chunkProvider;
    
    private WorldType(String name, TriFunction<Long, ChunkGeneratorSettings, ChunkProviderSettings, AbstractChunkProvider> chunkProvider) {
        this.name = name;
        this.chunkProvider = chunkProvider;
    }
    
    public String getName() {
        return this.name;
    }
    
    public AbstractChunkProvider createChunkProvider(long seed, ChunkGeneratorSettings generatorSettings, ChunkProviderSettings providerSettings) {
        return this.chunkProvider.apply(seed, generatorSettings, providerSettings);
    }
    
    public static WorldType fromName(String name) {
        for (WorldType w : WorldType.values()) {
            if (w.name.equalsIgnoreCase(name)) {
                return w;
            }
        }
        
        throw new IllegalArgumentException("[Modern Beta] No world type matching name: " + name);
    }
    
    public static WorldType getWorldType(CompoundTag settings) {
        WorldType type = WorldType.BETA;
        
        if (settings.contains("worldType"))
            type = WorldType.fromName(settings.getString("worldType"));
        
        return type;
    }
}