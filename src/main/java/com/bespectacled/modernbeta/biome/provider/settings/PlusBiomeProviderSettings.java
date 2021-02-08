package com.bespectacled.modernbeta.biome.provider.settings;

import java.util.function.Supplier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class PlusBiomeProviderSettings extends BiomeProviderSettings {
    public static final Codec<PlusBiomeProviderSettings> CODEC;
    
    public final Supplier<Biome> temperateBiome;
    public final Supplier<Biome> coldBiome;
    
    public PlusBiomeProviderSettings(Supplier<Biome> temperateBiome, Supplier<Biome> coldBiome) {
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
    public Codec<? extends BiomeProviderSettings> getCodec() {
        return CODEC;
    }
    
    public static PlusBiomeProviderSettings createSettings(Registry<Biome> biomeRegistry, Identifier temperateBiomeId, Identifier coldBiomeId) {
        return new PlusBiomeProviderSettings(
            () -> biomeRegistry.getOrThrow(RegistryKey.of(Registry.BIOME_KEY, temperateBiomeId)),
            () -> biomeRegistry.getOrThrow(RegistryKey.of(Registry.BIOME_KEY, coldBiomeId))
        );
    }
    
    static {
        CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Biome.REGISTRY_CODEC.fieldOf("temperate_biome").forGetter(settings -> settings.temperateBiome),
            Biome.REGISTRY_CODEC.fieldOf("cold_biome").forGetter(settings -> settings.coldBiome)
        ).apply(instance, PlusBiomeProviderSettings::new));
    }
}
