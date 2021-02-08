package com.bespectacled.modernbeta.biome.provider.settings;

import java.util.function.Supplier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class SingleBiomeProviderSettings extends BiomeProviderSettings {
    public static final Codec<SingleBiomeProviderSettings> CODEC;
    
    public final Supplier<Biome> biome;
    
    public SingleBiomeProviderSettings(Supplier<Biome> biome) {
        this.biome = biome;
    }
    
    public Supplier<Biome> getBiome() {
        return this.biome;
    }
    
    @Override
    public Codec<? extends BiomeProviderSettings> getCodec() {
        return CODEC;
    }
    
    public static SingleBiomeProviderSettings createSettings(Registry<Biome> biomeRegistry, Identifier biomeId) {
        return new SingleBiomeProviderSettings(() -> biomeRegistry.getOrThrow(RegistryKey.of(Registry.BIOME_KEY, biomeId)));
    }
    
    static {
        CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Biome.REGISTRY_CODEC.fieldOf("biome").forGetter(settings -> settings.biome)
        ).apply(instance, SingleBiomeProviderSettings::new));
    }
}
