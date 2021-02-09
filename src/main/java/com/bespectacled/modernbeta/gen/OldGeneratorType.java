package com.bespectacled.modernbeta.gen;

import java.util.Optional;
import java.util.function.Supplier;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.biome.BiomeType;
import com.bespectacled.modernbeta.biome.OldBiomeSource;
import com.bespectacled.modernbeta.biome.beta.BetaBiomes;
import com.bespectacled.modernbeta.biome.classic.ClassicBiomes;
import com.bespectacled.modernbeta.biome.provider.settings.BetaBiomeProviderSettings;
import com.bespectacled.modernbeta.biome.provider.settings.BiomeProviderSettings;
import com.bespectacled.modernbeta.biome.provider.settings.PlusBiomeProviderSettings;
import com.bespectacled.modernbeta.biome.provider.settings.SingleBiomeProviderSettings;
import com.bespectacled.modernbeta.biome.provider.settings.VanillaBiomeProviderSettings;
import com.bespectacled.modernbeta.gen.provider.settings.ChunkProviderSettings;
import com.bespectacled.modernbeta.gen.provider.settings.InfOldProviderSettings;
import com.bespectacled.modernbeta.gen.provider.settings.InfProviderSettings;
import com.bespectacled.modernbeta.gui.InfCustomizeLevelScreen;
import com.bespectacled.modernbeta.gui.InfdevOldCustomizeLevelScreen;
import com.bespectacled.modernbeta.mixin.client.MixinGeneratorTypeAccessor;
import com.bespectacled.modernbeta.mixin.client.MixinMoreOptionsDialogInvoker;
import com.google.common.collect.ImmutableMap;

import net.minecraft.client.world.GeneratorType;
import net.minecraft.client.world.GeneratorType.ScreenProvider;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public class OldGeneratorType {

    private static final GeneratorType BETA;
    private static final GeneratorType SKYLANDS;
    private static final GeneratorType ALPHA;
    private static final GeneratorType INFDEV;
    private static final GeneratorType INFDEV_OLD;
    //private static final GeneratorType INDEV;
    //private static final GeneratorType NETHER;
    //private static final GeneratorType FLAT;
    
    public static void register() {
        register(BETA);
        register(SKYLANDS);
        register(ALPHA);
        register(INFDEV);
        register(INFDEV_OLD);
        //register(INDEV);
        //register(NETHER);
        //register(FLAT);
    }
    
    private static void register(GeneratorType type) {
        MixinGeneratorTypeAccessor.getValues().add(type);
    }
    
    private static BiomeProviderSettings createBiomeSettings(Registry<Biome> registryBiome, WorldType worldType, BiomeType biomeType) {        
        switch(biomeType) {
            case BETA:
                return BetaBiomeProviderSettings.createDefaultSettings(registryBiome);
            case SKY:
                return SingleBiomeProviderSettings.createSettings(registryBiome, BetaBiomes.SKY_ID);
            case CLASSIC:
                return SingleBiomeProviderSettings.createSettings(registryBiome, ClassicBiomes.getBiomeMap(worldType).get(biomeType));
            case WINTER:
                return SingleBiomeProviderSettings.createSettings(registryBiome, ClassicBiomes.getBiomeMap(worldType).get(biomeType));
            case PLUS:
                return PlusBiomeProviderSettings.createSettings(registryBiome, ClassicBiomes.getBiomeMap(worldType).get(BiomeType.CLASSIC), ClassicBiomes.getBiomeMap(worldType).get(BiomeType.WINTER));
            case VANILLA:
                return VanillaBiomeProviderSettings.createDefaultSettings(registryBiome);
            default:
                return SingleBiomeProviderSettings.createSettings(registryBiome, ClassicBiomes.ALPHA_ID);
        }
    }
    
    private static GeneratorOptions createNewGeneratorOptions(
        DynamicRegistryManager registryManager, 
        GeneratorOptions generatorOptions,
        ChunkProviderSettings chunkProviderSettings,
        WorldType worldType,
        BiomeType biomeType
    ) {
        Registry<DimensionType> registryDimensionType = registryManager.<DimensionType>get(Registry.DIMENSION_TYPE_KEY);
        Registry<ChunkGeneratorSettings> registryChunkGenSettings = registryManager.<ChunkGeneratorSettings>get(Registry.NOISE_SETTINGS_WORLDGEN);
        Registry<Biome> registryBiome = registryManager.<Biome>get(Registry.BIOME_KEY); 
        Supplier<ChunkGeneratorSettings> chunkGenSettingsSupplier = () -> registryChunkGenSettings.get(ModernBeta.createId(worldType.getName()));
        
        OldBiomeSource biomeSource = new OldBiomeSource(generatorOptions.getSeed(), registryBiome, biomeType.getName(), createBiomeSettings(registryBiome, worldType, biomeType));
        
        return new GeneratorOptions(
            generatorOptions.getSeed(),
            generatorOptions.shouldGenerateStructures(),
            generatorOptions.hasBonusChest(),
            GeneratorOptions.method_28608(
                registryDimensionType, 
                generatorOptions.getDimensions(), 
                new OldChunkGenerator(biomeSource, generatorOptions.getSeed(), worldType.getName(), chunkGenSettingsSupplier.get(), chunkProviderSettings))
        );
    }
    
    static {
        BETA = new GeneratorType("beta") {
            @Override
            protected ChunkGenerator getChunkGenerator(Registry<Biome> biomes, Registry<ChunkGeneratorSettings> generatorSettingsRegistry, long seed) {
                WorldType worldType = WorldType.BETA;
                BiomeType biomeType = BiomeType.BETA;
                
                OldBiomeSource biomeSource = new OldBiomeSource(seed, biomes, biomeType.getName(), createBiomeSettings(biomes, worldType, biomeType));
                ChunkGeneratorSettings generatorSettings = generatorSettingsRegistry.getOrThrow(OldGeneratorSettings.BETA);
                
                return new OldChunkGenerator(biomeSource, seed, worldType.getName(), generatorSettings, new InfProviderSettings(true));
            }
        };
        
        SKYLANDS = new GeneratorType("skylands") {
            @Override
            protected ChunkGenerator getChunkGenerator(Registry<Biome> biomes, Registry<ChunkGeneratorSettings> generatorSettingsRegistry, long seed) {
                WorldType worldType = WorldType.SKYLANDS;
                BiomeType biomeType = BiomeType.SKY;
                
                OldBiomeSource biomeSource = new OldBiomeSource(seed, biomes, biomeType.getName(), createBiomeSettings(biomes, worldType, biomeType));
                ChunkGeneratorSettings generatorSettings = generatorSettingsRegistry.getOrThrow(OldGeneratorSettings.SKYLANDS);
                
                return new OldChunkGenerator(biomeSource, seed, worldType.getName(), generatorSettings, new InfProviderSettings(false));
            }
        };
        
        ALPHA = new GeneratorType("alpha") {
            @Override
            protected ChunkGenerator getChunkGenerator(Registry<Biome> biomes, Registry<ChunkGeneratorSettings> generatorSettingsRegistry, long seed) {
                WorldType worldType = WorldType.ALPHA;
                BiomeType biomeType = BiomeType.CLASSIC;
                
                OldBiomeSource biomeSource = new OldBiomeSource(seed, biomes, biomeType.getName(), createBiomeSettings(biomes, worldType, biomeType));
                ChunkGeneratorSettings generatorSettings = generatorSettingsRegistry.getOrThrow(OldGeneratorSettings.ALPHA);
                
                return new OldChunkGenerator(biomeSource, seed, worldType.getName(), generatorSettings, new InfProviderSettings(true));
            }
        };
        
        INFDEV = new GeneratorType("infdev") {
            @Override
            protected ChunkGenerator getChunkGenerator(Registry<Biome> biomes, Registry<ChunkGeneratorSettings> generatorSettingsRegistry, long seed) {
                WorldType worldType = WorldType.INFDEV;
                BiomeType biomeType = BiomeType.CLASSIC;
                
                OldBiomeSource biomeSource = new OldBiomeSource(seed, biomes, biomeType.getName(), createBiomeSettings(biomes, worldType, biomeType));
                ChunkGeneratorSettings generatorSettings = generatorSettingsRegistry.getOrThrow(OldGeneratorSettings.INFDEV);
                
                return new OldChunkGenerator(biomeSource, seed, worldType.getName(), generatorSettings, new InfProviderSettings(true));
            }
        };
        
        INFDEV_OLD = new GeneratorType("infdev_old") {
            @Override
            protected ChunkGenerator getChunkGenerator(Registry<Biome> biomes, Registry<ChunkGeneratorSettings> generatorSettingsRegistry, long seed) {
                WorldType worldType = WorldType.INFDEV_OLD;
                BiomeType biomeType = BiomeType.CLASSIC;
                
                OldBiomeSource biomeSource = new OldBiomeSource(seed, biomes, biomeType.getName(), createBiomeSettings(biomes, worldType, biomeType));
                ChunkGeneratorSettings generatorSettings = generatorSettingsRegistry.getOrThrow(OldGeneratorSettings.INFDEV_OLD);
                
                return new OldChunkGenerator(biomeSource, seed, worldType.getName(), generatorSettings, new InfOldProviderSettings(true, true, true));
            }
        };
         
        /*
        INDEV = new GeneratorType("indev") {
            @Override
            protected ChunkGenerator getChunkGenerator(Registry<Biome> biomes, Registry<ChunkGeneratorSettings> genSettings, long seed) {
                return new OldChunkGenerator(new OldBiomeSource(seed, biomes, OldGeneratorSettings.INDEV_SETTINGS.providerSettings), seed, OldGeneratorSettings.INDEV_SETTINGS);
            }
        };
        
        NETHER = new GeneratorType("nether") {
            @Override
            protected ChunkGenerator getChunkGenerator(Registry<Biome> biomes, Registry<ChunkGeneratorSettings> genSettings, long seed) {
                return new OldChunkGenerator(new OldBiomeSource(seed, biomes, OldGeneratorSettings.NETHER_SETTINGS.providerSettings), seed, OldGeneratorSettings.NETHER_SETTINGS);
            }
        };
        
        
        FLAT = new GeneratorType("flat") {
            @Override
            protected ChunkGenerator getChunkGenerator(Registry<Biome> biomes, Registry<ChunkGeneratorSettings> genSettings, long seed) {
                return new OldChunkGenerator(new OldBiomeSource(seed, biomes, OldGeneratorSettings.FLAT_SETTINGS.providerSettings), seed, OldGeneratorSettings.FLAT_SETTINGS);
            }
        };
        */
        
        
        MixinGeneratorTypeAccessor.setScreenProviders(
            new ImmutableMap.Builder<Optional<GeneratorType>, ScreenProvider>()
                .putAll(MixinGeneratorTypeAccessor.getScreenProviders())
                .put(
                    Optional.<GeneratorType>of(BETA), (screen, generatorOptions) -> {
                        return new InfCustomizeLevelScreen(
                            screen, 
                            "createWorld.customize.beta.title",
                            new InfProviderSettings(true),
                            BiomeType.BETA, 
                            true, 
                            ((biomeType, chunkProviderSettings) -> ((MixinMoreOptionsDialogInvoker)screen.moreOptionsDialog).invokeSetGeneratorOptions(
                                createNewGeneratorOptions(
                                    screen.moreOptionsDialog.getRegistryManager(),
                                    generatorOptions,
                                    chunkProviderSettings,
                                    WorldType.BETA,
                                    biomeType
                            )))
                        );
                    }
                )
                .put(
                    Optional.<GeneratorType>of(SKYLANDS), (screen, generatorOptions) -> {
                        return new InfCustomizeLevelScreen(
                            screen, 
                            "createWorld.customize.skylands.title",
                            new InfProviderSettings(false),
                            BiomeType.SKY, 
                            false, 
                            ((biomeType, chunkProviderSettings) -> ((MixinMoreOptionsDialogInvoker)screen.moreOptionsDialog).invokeSetGeneratorOptions(
                                createNewGeneratorOptions(
                                    screen.moreOptionsDialog.getRegistryManager(),
                                    generatorOptions,
                                    chunkProviderSettings,
                                    WorldType.SKYLANDS,
                                    biomeType
                            )))
                        );
                    }
                )
                .put(
                    Optional.<GeneratorType>of(ALPHA), (screen, generatorOptions) -> {
                        return new InfCustomizeLevelScreen(
                            screen, 
                            "createWorld.customize.alpha.title",
                            new InfProviderSettings(true),
                            BiomeType.CLASSIC, 
                            true, 
                            ((biomeType, chunkProviderSettings) -> ((MixinMoreOptionsDialogInvoker)screen.moreOptionsDialog).invokeSetGeneratorOptions(
                                createNewGeneratorOptions(
                                    screen.moreOptionsDialog.getRegistryManager(),
                                    generatorOptions,
                                    chunkProviderSettings,
                                    WorldType.ALPHA,
                                    biomeType
                            )))
                        );
                    }
                )
                .put(
                    Optional.<GeneratorType>of(INFDEV), (screen, generatorOptions) -> {
                        return new InfCustomizeLevelScreen(
                            screen, 
                            "createWorld.customize.infdev.title",
                            new InfProviderSettings(true),
                            BiomeType.CLASSIC, 
                            true, 
                            ((biomeType, chunkProviderSettings) -> ((MixinMoreOptionsDialogInvoker)screen.moreOptionsDialog).invokeSetGeneratorOptions(
                                createNewGeneratorOptions(
                                    screen.moreOptionsDialog.getRegistryManager(),
                                    generatorOptions,
                                    chunkProviderSettings,
                                    WorldType.INFDEV,
                                    biomeType
                            )))
                        );
                    }
                )
                .put(
                    Optional.<GeneratorType>of(INFDEV_OLD), (screen, generatorOptions) -> {
                        return new InfdevOldCustomizeLevelScreen(
                            screen, 
                            "createWorld.customize.infdev.title",
                            new InfOldProviderSettings(true, true, true),
                            BiomeType.CLASSIC, 
                            true, 
                            ((biomeType, chunkProviderSettings) -> ((MixinMoreOptionsDialogInvoker)screen.moreOptionsDialog).invokeSetGeneratorOptions(
                                createNewGeneratorOptions(
                                    screen.moreOptionsDialog.getRegistryManager(),
                                    generatorOptions,
                                    chunkProviderSettings,
                                    WorldType.INFDEV_OLD,
                                    biomeType
                            )))
                        );
                    }
                )
                /*
                .put(
                    Optional.<GeneratorType>of(INDEV), (createWorldScreen, generatorSettings) -> {
                        return new IndevCustomizeLevelScreen(createWorldScreen, OldGeneratorSettings.INDEV_SETTINGS, "createWorld.customize.indev.title");
                    }
                )
                .put(
                    Optional.<GeneratorType>of(FLAT), (createWorldScreen, generatorSettings) -> {
                        return new InfCustomizeLevelScreen(createWorldScreen, OldGeneratorSettings.BETA_SETTINGS, "createWorld.customize.beta.title", BiomeType.BETA, false);
                    }
                )
                .put(
                    Optional.<GeneratorType>of(NETHER), (createWorldScreen, generatorSettings) -> {
                        return new InfCustomizeLevelScreen(createWorldScreen, OldGeneratorSettings.BETA_SETTINGS, "createWorld.customize.beta.title", BiomeType.BETA, false);
                    }
                )
                */
                .build()
        );
    }
}
