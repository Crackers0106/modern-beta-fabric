package com.bespectacled.modernbeta.gen;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.gen.provider.settings.ChunkProviderSettings;
import com.bespectacled.modernbeta.util.BlockStates;

import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public class OldGeneratorSettings {
    public static final RegistryKey<ChunkGeneratorSettings> BETA;
    public static final RegistryKey<ChunkGeneratorSettings> ALPHA;
    public static final RegistryKey<ChunkGeneratorSettings> SKYLANDS;
    public static final RegistryKey<ChunkGeneratorSettings> INFDEV;
    public static final RegistryKey<ChunkGeneratorSettings> INFDEV_OLD;
    public static final RegistryKey<ChunkGeneratorSettings> INDEV;
    public static final RegistryKey<ChunkGeneratorSettings> NETHER;
    
    public static final ChunkGeneratorSettings BETA_GENERATOR_SETTINGS;
    public static final ChunkGeneratorSettings ALPHA_GENERATOR_SETTINGS;
    public static final ChunkGeneratorSettings SKYLANDS_GENERATOR_SETTINGS;
    public static final ChunkGeneratorSettings INFDEV_GENERATOR_SETTINGS;
    public static final ChunkGeneratorSettings INFDEV_OLD_GENERATOR_SETTINGS;
    public static final ChunkGeneratorSettings INDEV_GENERATOR_SETTINGS;
    public static final ChunkGeneratorSettings NETHER_GENERATOR_SETTINGS;
    
    public ChunkGeneratorSettings generatorSettings;
    public ChunkProviderSettings providerSettings;
    
    public OldGeneratorSettings(ChunkGeneratorSettings generatorSettings, ChunkProviderSettings providerSettings) {
        this.generatorSettings = generatorSettings;
        this.providerSettings = providerSettings;
    }
    
    private static ChunkGeneratorSettings register(RegistryKey<ChunkGeneratorSettings> key, ChunkGeneratorSettings settings) {
        BuiltinRegistries.<ChunkGeneratorSettings, ChunkGeneratorSettings>add(BuiltinRegistries.CHUNK_GENERATOR_SETTINGS, key.getValue(), settings);
        return settings;
    }
    
    public static void register() {
        register(BETA, BETA_GENERATOR_SETTINGS);
        register(ALPHA, ALPHA_GENERATOR_SETTINGS);
        register(SKYLANDS, SKYLANDS_GENERATOR_SETTINGS);
        register(INFDEV, INFDEV_GENERATOR_SETTINGS);
        register(INFDEV_OLD, INFDEV_OLD_GENERATOR_SETTINGS);
        register(INDEV, INDEV_GENERATOR_SETTINGS);
        register(NETHER, NETHER_GENERATOR_SETTINGS);
    }
    
    static {
        BETA = RegistryKey.<ChunkGeneratorSettings>of(Registry.NOISE_SETTINGS_WORLDGEN, ModernBeta.createId(WorldType.BETA.getName()));
        ALPHA = RegistryKey.<ChunkGeneratorSettings>of(Registry.NOISE_SETTINGS_WORLDGEN, ModernBeta.createId(WorldType.ALPHA.getName()));
        SKYLANDS = RegistryKey.<ChunkGeneratorSettings>of(Registry.NOISE_SETTINGS_WORLDGEN, ModernBeta.createId(WorldType.SKYLANDS.getName()));
        INFDEV = RegistryKey.<ChunkGeneratorSettings>of(Registry.NOISE_SETTINGS_WORLDGEN, ModernBeta.createId(WorldType.INFDEV.getName()));
        INFDEV_OLD = RegistryKey.<ChunkGeneratorSettings>of(Registry.NOISE_SETTINGS_WORLDGEN, ModernBeta.createId(WorldType.INFDEV_OLD.getName()));
        INDEV = RegistryKey.<ChunkGeneratorSettings>of(Registry.NOISE_SETTINGS_WORLDGEN, ModernBeta.createId(WorldType.INDEV.getName()));
        NETHER = RegistryKey.<ChunkGeneratorSettings>of(Registry.NOISE_SETTINGS_WORLDGEN, ModernBeta.createId(WorldType.NETHER.getName()));
        
        BETA_GENERATOR_SETTINGS = new ChunkGeneratorSettings(OldGeneratorConfig.STRUCTURES, OldGeneratorConfig.BETA_SHAPE_CONFIG, BlockStates.STONE, BlockStates.WATER, -10, 0, 64, false);
        ALPHA_GENERATOR_SETTINGS = new ChunkGeneratorSettings(OldGeneratorConfig.STRUCTURES, OldGeneratorConfig.ALPHA_SHAPE_CONFIG, BlockStates.STONE, BlockStates.WATER, -10, 0, 64, false);
        SKYLANDS_GENERATOR_SETTINGS = new ChunkGeneratorSettings(OldGeneratorConfig.STRUCTURES, OldGeneratorConfig.SKYLANDS_SHAPE_CONFIG, BlockStates.STONE, BlockStates.WATER, -10, -10, 0, false);
        INFDEV_GENERATOR_SETTINGS = new ChunkGeneratorSettings(OldGeneratorConfig.STRUCTURES, OldGeneratorConfig.INFDEV_SHAPE_CONFIG, BlockStates.STONE, BlockStates.WATER, -10, 0, 64, false);
        INFDEV_OLD_GENERATOR_SETTINGS = new ChunkGeneratorSettings(OldGeneratorConfig.STRUCTURES, OldGeneratorConfig.INFDEV_SHAPE_CONFIG, BlockStates.STONE, BlockStates.WATER, -10, 0, 64, false);
        INDEV_GENERATOR_SETTINGS = new ChunkGeneratorSettings(OldGeneratorConfig.INDEV_STRUCTURES, OldGeneratorConfig.INDEV_SHAPE_CONFIG, BlockStates.STONE, BlockStates.WATER, -10, 0, 64, false); 
        NETHER_GENERATOR_SETTINGS = new ChunkGeneratorSettings(OldGeneratorConfig.STRUCTURES, OldGeneratorConfig.NETHER_SHAPE_CONFIG, BlockStates.STONE, BlockStates.WATER, 128, 0, 32, false);
    }
}
