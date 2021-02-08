package com.bespectacled.modernbeta.biome.provider.settings;

import java.util.function.Supplier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.biome.Biome;

public class IndevBiomeProviderSettings extends BiomeProviderSettings {
    public static final Codec<IndevBiomeProviderSettings> CODEC;
    
    public final Supplier<Biome> mainBiome;
    public final Supplier<Biome> edgeBiome;
    
    public IndevBiomeProviderSettings(Supplier<Biome> mainBiome, Supplier<Biome> edgeBiome) {
        this.mainBiome = mainBiome;
        this.edgeBiome = edgeBiome;
    }
    
    public Supplier<Biome> getMainBiome() {
        return this.mainBiome;
    }
    
    public Supplier<Biome> getEdgeBiome() {
        return this.edgeBiome;
    }
    
    @Override
    public Codec<? extends BiomeProviderSettings> getCodec() {
        return CODEC;
    }
    
    static {
        CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Biome.REGISTRY_CODEC.fieldOf("main_biome").forGetter(settings -> settings.mainBiome),
            Biome.REGISTRY_CODEC.fieldOf("edge_biome").forGetter(settings -> settings.edgeBiome)
        ).apply(instance, IndevBiomeProviderSettings::new));
    }
}
