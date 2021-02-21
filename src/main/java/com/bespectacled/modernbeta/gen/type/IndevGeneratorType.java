package com.bespectacled.modernbeta.gen.type;

import java.util.Map;
import java.util.Optional;

import com.bespectacled.modernbeta.biome.OldBiomeSource;
import com.bespectacled.modernbeta.gen.OldChunkGenerator;
import com.bespectacled.modernbeta.gen.OldGeneratorSettings;
import com.bespectacled.modernbeta.gui.IndevCustomizeLevelScreen;
import com.bespectacled.modernbeta.mixin.MixinGeneratorTypeAccessor;
import com.google.common.collect.ImmutableMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.client.world.GeneratorType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

@Environment(EnvType.CLIENT)
public final class IndevGeneratorType extends GeneratorType {
    public static final GeneratorType INSTANCE = new IndevGeneratorType();
    
    public static final OldGeneratorSettings INDEV_SETTINGS = new OldGeneratorSettings(new CompoundTag(), true);
    
    // Add to Screen Providers
    private static Map<Optional<GeneratorType>, ScreenProvider> NEW_SCREEN_PROVIDERS = 
        new ImmutableMap.Builder<Optional<GeneratorType>, ScreenProvider>()
            .putAll(MixinGeneratorTypeAccessor.getScreenProviders())
            .put(
                Optional.<GeneratorType>of(INSTANCE), (createWorldScreen, generatorSettings) -> {
                    return new IndevCustomizeLevelScreen(createWorldScreen, INDEV_SETTINGS, "createWorld.customize.indev.title");
                }
            )
            .build();

    private IndevGeneratorType() {
        super("indev");
    }

    public static void register() {
        GeneratorType.VALUES.add(INSTANCE);
        MixinGeneratorTypeAccessor.setScreenProviders(NEW_SCREEN_PROVIDERS);
        
        //ModernBeta.LOGGER.log(Level.INFO, "Registered Indev world type.");
    }

    @Override
    protected ChunkGenerator getChunkGenerator(Registry<Biome> biomes, Registry<ChunkGeneratorSettings> genSettings, long seed) {
        INDEV_SETTINGS.providerSettings = OldGeneratorSettings.createIndevSettings();
        return new OldChunkGenerator(new OldBiomeSource(seed, biomes, INDEV_SETTINGS.providerSettings), seed, INDEV_SETTINGS);
    }
    
    
}
