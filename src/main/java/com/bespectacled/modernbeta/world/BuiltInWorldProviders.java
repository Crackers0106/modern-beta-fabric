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
    public static final WorldProvider INFDEV_611;
    public static final WorldProvider INFDEV_415;
    public static final WorldProvider INFDEV_227;
    public static final WorldProvider INDEV;
    public static final WorldProvider BETA_ISLANDS;
    
    static {
        BETA = new WorldProvider(
            BuiltInTypes.Chunk.BETA.name, 
            OldChunkGeneratorSettings.BETA.toString(), 
            BuiltInTypes.Biome.BETA.name,
            BuiltInTypes.CaveBiome.VANILLA.name, 
            BetaBiomes.FOREST_ID.toString(), 
            BuiltInTypes.WorldScreen.INF.name
        );
        
        
        SKYLANDS = new WorldProvider(
            BuiltInTypes.Chunk.SKYLANDS.name, 
            OldChunkGeneratorSettings.SKYLANDS.toString(), 
            BuiltInTypes.Biome.SINGLE.name, 
            BuiltInTypes.CaveBiome.NONE.name, 
            BetaBiomes.SKY_ID.toString(), 
            BuiltInTypes.WorldScreen.BASE.name
        );
        
        ALPHA = new WorldProvider(
            BuiltInTypes.Chunk.ALPHA.name,
            OldChunkGeneratorSettings.ALPHA.toString(), 
            BuiltInTypes.Biome.SINGLE.name, 
            BuiltInTypes.CaveBiome.NONE.name, 
            ClassicBiomes.ALPHA_ID.toString(), 
            BuiltInTypes.WorldScreen.INF.name
        );
        
        INFDEV_611 = new WorldProvider(
            BuiltInTypes.Chunk.INFDEV_611.name,
            OldChunkGeneratorSettings.INFDEV_611.toString(),
            BuiltInTypes.Biome.SINGLE.name,
            BuiltInTypes.CaveBiome.NONE.name,
            ClassicBiomes.ALPHA_ID.toString(),
            BuiltInTypes.WorldScreen.INF.name
        );
        
        INFDEV_415 = new WorldProvider(
            BuiltInTypes.Chunk.INFDEV_415.name,
            OldChunkGeneratorSettings.INFDEV_415.toString(), 
            BuiltInTypes.Biome.SINGLE.name, 
            BuiltInTypes.CaveBiome.NONE.name, 
            ClassicBiomes.INFDEV_415_ID.toString(), 
            BuiltInTypes.WorldScreen.INF.name
        );
        
        INFDEV_227 = new WorldProvider(
            BuiltInTypes.Chunk.INFDEV_227.name,
            OldChunkGeneratorSettings.INFDEV_227.toString(), 
            BuiltInTypes.Biome.SINGLE.name, 
            BuiltInTypes.CaveBiome.NONE.name, 
            ClassicBiomes.INFDEV_227_ID.toString(), 
            BuiltInTypes.WorldScreen.INFDEV_227.name
        );
        
        INDEV = new WorldProvider(
            BuiltInTypes.Chunk.INDEV.name,
            OldChunkGeneratorSettings.INDEV.toString(), 
            BuiltInTypes.Biome.SINGLE.name, 
            BuiltInTypes.CaveBiome.NONE.name, 
            IndevBiomes.INDEV_NORMAL_ID.toString(), 
            BuiltInTypes.WorldScreen.INDEV.name
        );
        
        BETA_ISLANDS = new WorldProvider(
            BuiltInTypes.Chunk.BETA_ISLANDS.name,
            OldChunkGeneratorSettings.BETA_ISLANDS.toString(),
            BuiltInTypes.Biome.BETA.name,
            BuiltInTypes.CaveBiome.VANILLA.name,
            BetaBiomes.FOREST_ID.toString(),
            BuiltInTypes.WorldScreen.ISLAND.name
        );
    }
}
