package com.bespectacled.modernbeta.biome.settings;

import java.util.function.Supplier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class PlusBiomeSettings extends BiomeSettings {
    public static final Codec<PlusBiomeSettings> CODEC;
    
    public final Supplier<Biome> temperateBiome;
    public final Supplier<Biome> coldBiome;
    
    public PlusBiomeSettings(Supplier<Biome> temperateBiome, Supplier<Biome> coldBiome) {
        this.temperateBiome = temperateBiome;
        this.coldBiome = coldBiome;
    }
    
    public Supplier<Biome> getTemperateBiome() {
        return this.temperateBiome;
    }
    
    public Supplier<Biome> getColdBiome() {
        return this.coldBiome;
    }
    
    @Override
    public Codec<? extends BiomeSettings> getCodec() {
        return CODEC;
    }
    
    public static PlusBiomeSettings createSettings(Registry<Biome> biomeRegistry, Identifier temperateBiomeId, Identifier coldBiomeId) {
        return new PlusBiomeSettings(
            () -> biomeRegistry.getOrThrow(RegistryKey.of(Registry.BIOME_KEY, temperateBiomeId)),
            () -> biomeRegistry.getOrThrow(RegistryKey.of(Registry.BIOME_KEY, coldBiomeId))
        );
    }
    
    static {
        CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Biome.REGISTRY_CODEC.fieldOf("temperate_biome").forGetter(settings -> settings.temperateBiome),
            Biome.REGISTRY_CODEC.fieldOf("cold_biome").forGetter(settings -> settings.coldBiome)
        ).apply(instance, PlusBiomeSettings::new));
    }
}
