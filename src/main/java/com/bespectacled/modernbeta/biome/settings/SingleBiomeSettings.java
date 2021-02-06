package com.bespectacled.modernbeta.biome.settings;

import java.util.function.Supplier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class SingleBiomeSettings extends BiomeSettings {
    public static final Codec<SingleBiomeSettings> CODEC;
    
    public final Supplier<Biome> biome;
    
    public SingleBiomeSettings(Supplier<Biome> biome) {
        this.biome = biome;
    }
    
    public Supplier<Biome> getBiome() {
        return this.biome;
    }
    
    @Override
    public Codec<? extends BiomeSettings> getCodec() {
        return CODEC;
    }
    
    public static SingleBiomeSettings createSettings(Registry<Biome> biomeRegistry, Identifier biomeId) {
        return new SingleBiomeSettings(() -> biomeRegistry.getOrThrow(RegistryKey.of(Registry.BIOME_KEY, biomeId)));
    }
    
    static {
        CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Biome.REGISTRY_CODEC.fieldOf("biome").forGetter(settings -> settings.biome)
        ).apply(instance, SingleBiomeSettings::new));
    }
}
