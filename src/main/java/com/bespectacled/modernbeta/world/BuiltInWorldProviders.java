package com.bespectacled.modernbeta.world;

import com.bespectacled.modernbeta.api.registry.BuiltInTypes;
import com.bespectacled.modernbeta.api.world.WorldProvider;
import com.bespectacled.modernbeta.world.biome.beta.BetaBiomes;
import com.bespectacled.modernbeta.world.biome.classic.ClassicBiomes;
import com.bespectacled.modernbeta.world.biome.indev.IndevBiomes;
import com.bespectacled.modernbeta.world.gen.OldChunkGeneratorSettings;

public class BuiltInWorldProviders {
    public static final WorldProvider BETA;
    public static final WorldProvider SKYLANDS;
    public static final WorldProvider ALPHA;
    public static final WorldProvider INFDEV_415;
    public static final WorldProvider INFDEV_227;
    public static final WorldProvider INDEV;
    public static final WorldProvider BETA_ISLANDS;
    
    static {
        BETA = new WorldProvider(
            BuiltInTypes.Chunk.BETA.name, 
            BuiltInTypes.ChunkSettings.BETA.name,
            OldChunkGeneratorSettings.BETA.toString(), 
            BuiltInTypes.WorldScreen.INF.name,
            BuiltInTypes.Biome.BETA.name,
            BetaBiomes.FOREST_ID.toString()
        );
        
        SKYLANDS = new WorldProvider(
            BuiltInTypes.Chunk.SKYLANDS.name, 
            BuiltInTypes.ChunkSettings.SKYLANDS.name,
            OldChunkGeneratorSettings.SKYLANDS.toString(), 
            BuiltInTypes.WorldScreen.SKYLANDS.name, 
            BuiltInTypes.Biome.SINGLE.name,
            BetaBiomes.SKY_ID.toString()
        );
        
        ALPHA = new WorldProvider(
            BuiltInTypes.Chunk.ALPHA.name,
            BuiltInTypes.ChunkSettings.ALPHA.name,
            OldChunkGeneratorSettings.ALPHA.toString(), 
            BuiltInTypes.WorldScreen.INF.name, 
            BuiltInTypes.Biome.SINGLE.name,
            ClassicBiomes.ALPHA_ID.toString()
        );
        
        INFDEV_415 = new WorldProvider(
            BuiltInTypes.Chunk.INFDEV_415.name,
            BuiltInTypes.ChunkSettings.INFDEV_415.name,
            OldChunkGeneratorSettings.INFDEV_415.toString(), 
            BuiltInTypes.WorldScreen.INF.name, 
            BuiltInTypes.Biome.SINGLE.name,
            ClassicBiomes.INFDEV_415_ID.toString()
        );
        
        INFDEV_227 = new WorldProvider(
            BuiltInTypes.Chunk.INFDEV_227.name,
            BuiltInTypes.ChunkSettings.INFDEV_227.name,
            OldChunkGeneratorSettings.INFDEV_227.toString(), 
            BuiltInTypes.WorldScreen.INFDEV_OLD.name, 
            BuiltInTypes.Biome.SINGLE.name,
            ClassicBiomes.INFDEV_227_ID.toString()
        );
        
        INDEV = new WorldProvider(
            BuiltInTypes.Chunk.INDEV.name,
            BuiltInTypes.ChunkSettings.INDEV.name,
            OldChunkGeneratorSettings.INDEV.toString(), 
            BuiltInTypes.WorldScreen.INDEV.name, 
            BuiltInTypes.Biome.SINGLE.name,
            IndevBiomes.INDEV_NORMAL_ID.toString()
        );
        
        BETA_ISLANDS = new WorldProvider(
            BuiltInTypes.Chunk.BETA_ISLANDS.name,
            BuiltInTypes.ChunkSettings.BETA_ISLANDS.name,
            OldChunkGeneratorSettings.BETA_ISLANDS.toString(),
            BuiltInTypes.WorldScreen.ISLAND.name,
            BuiltInTypes.Biome.BETA.name,
            BetaBiomes.FOREST_ID.toString()
        );
    }
}
