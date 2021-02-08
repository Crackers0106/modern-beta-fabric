package com.bespectacled.modernbeta.biome.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.bespectacled.modernbeta.biome.beta.BetaClimateSampler;
import com.bespectacled.modernbeta.biome.provider.settings.*;

import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class PlusBiomeProvider extends AbstractBiomeProvider {
    private final Supplier<Biome> temperateBiome;
    private final Supplier<Biome> coldBiome;
    
    public PlusBiomeProvider(long seed, BiomeProviderSettings biomeSettings) {
        this.temperateBiome = ((PlusBiomeProviderSettings)biomeSettings).temperateBiome;
        this.coldBiome = ((PlusBiomeProviderSettings)biomeSettings).coldBiome;
        
        BetaClimateSampler.INSTANCE.setSeed(seed);
    }

    @Override
    public Biome getBiomeForNoiseGen(Registry<Biome> registry, int biomeX, int biomeY, int biomeZ) {
        int absX = biomeX << 2;
        int absZ = biomeZ << 2;
        
        return BetaClimateSampler.INSTANCE.sampleTemp(absX, absZ) < 0.5f ? this.coldBiome.get() : this.temperateBiome.get();
    } 

    @Override
    public List<RegistryKey<Biome>> getBiomesForRegistry() {
        List<RegistryKey<Biome>> biomeList = new ArrayList<RegistryKey<Biome>>();

        
        return biomeList;
    }

}
