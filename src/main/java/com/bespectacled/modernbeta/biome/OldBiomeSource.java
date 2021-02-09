package com.bespectacled.modernbeta.biome;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.biome.provider.AbstractBiomeProvider;
import com.bespectacled.modernbeta.biome.provider.settings.*;
import com.bespectacled.modernbeta.gen.WorldType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;

public class OldBiomeSource extends BiomeSource {
    
    public static final Codec<OldBiomeSource> CODEC = RecordCodecBuilder.create(instance -> instance
        .group(
            Codec.LONG.fieldOf("seed").stable().forGetter(biomeSource -> biomeSource.seed),
            RegistryLookupCodec.of(Registry.BIOME_KEY).forGetter(biomeSource -> biomeSource.biomeRegistry),
            Codec.STRING.fieldOf("biome_type").stable().forGetter(biomeSource -> biomeSource.biomeType),
            BiomeProviderSettings.CODEC.fieldOf("biome_provider_settings").forGetter(biomeSource -> biomeSource.biomeSettings)
        ).apply(instance, (instance).stable(OldBiomeSource::new)));
    
    private final long seed;
    private final String biomeType;
    private final Registry<Biome> biomeRegistry;
    private final BiomeProviderSettings biomeSettings;
    
    private final WorldType worldType;
    private final AbstractBiomeProvider biomeProvider;
    
    public OldBiomeSource(long seed, Registry<Biome> biomeRegistry, String biomeType, BiomeProviderSettings biomeSettings) {
        super(BiomeType.fromName(biomeType).createBiomeProvider(seed, biomeSettings).getBiomesForRegistry().stream().map((registryKey) -> () -> (Biome) biomeRegistry.get(registryKey)));
        
        this.seed = seed;
        this.biomeType = biomeType;
        this.biomeRegistry = biomeRegistry;
        this.biomeSettings = biomeSettings;
        
        this.worldType = WorldType.BETA;
        this.biomeProvider = BiomeType.fromName(this.biomeType).createBiomeProvider(seed, biomeSettings);
    }

    @Override
    public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        return this.biomeProvider.getBiomeForNoiseGen(this.biomeRegistry, biomeX, biomeY, biomeZ);
    }

    public Biome getOceanBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        return this.biomeProvider.getOceanBiomeForNoiseGen(this.biomeRegistry, biomeX, biomeY, biomeZ);
    }
    
    public Biome getBiomeForSurfaceGen(int x, int y, int z) {
        return this.biomeProvider.getBiomeForSurfaceGen(this.biomeRegistry, x, y, z);
    }

    public Registry<Biome> getBiomeRegistry() {
        return this.biomeRegistry;
    }

    public boolean isVanilla() {
        return BiomeType.fromName(this.biomeType) == BiomeType.VANILLA;
    }
    
    public boolean isBeta() {
        return BiomeType.fromName(this.biomeType) == BiomeType.BETA;
    }
    
    public boolean isSky() {
        return BiomeType.fromName(this.biomeType) == BiomeType.SKY;
    }
    
    public boolean isIndev() {
        return this.worldType == WorldType.INDEV;
    }
    
    @Override
    protected Codec<? extends BiomeSource> getCodec() {
        return OldBiomeSource.CODEC;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public BiomeSource withSeed(long seed) {
        return new OldBiomeSource(seed, this.biomeRegistry, this.biomeType, this.biomeSettings);
    }

    public static void register() {
        Registry.register(Registry.BIOME_SOURCE, ModernBeta.createId("old"), CODEC);
    }
}
