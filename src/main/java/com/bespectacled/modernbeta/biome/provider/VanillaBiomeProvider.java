package com.bespectacled.modernbeta.biome.provider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.bespectacled.modernbeta.biome.vanilla.VanillaBiomeLayer;
import com.bespectacled.modernbeta.biome.vanilla.VanillaOceanLayer;

import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.biome.source.BiomeLayerSampler;

public class VanillaBiomeProvider extends AbstractBiomeProvider {
    
    private final BiomeLayerSampler biomeSampler;
    private final BiomeLayerSampler oceanSampler;
    
    public VanillaBiomeProvider(long seed) {
        this.biomeSampler = VanillaBiomeLayer.build(seed, false, 4, -1);
        this.oceanSampler = VanillaOceanLayer.build(seed, false, 6, -1);
    }

    @Override
    public Biome getBiomeForNoiseGen(Registry<Biome> registry, int biomeX, int biomeY, int biomeZ) {
        return this.biomeSampler.sample(registry, biomeX, biomeZ);
    }

    @Override
    public Biome getOceanBiomeForNoiseGen(Registry<Biome> registry, int biomeX, int biomeY, int biomeZ) {
        return this.oceanSampler.sample(registry, biomeX, biomeZ);
    }

    @Override
    public List<RegistryKey<Biome>> getBiomesForRegistry() {
        return BuiltinRegistries.BIOME.getEntries()
            .stream()
            .filter(e -> this.isValidCategory(e.getValue().getCategory()))
            .map(e -> e.getKey())
            .collect(Collectors.toList());
    }
    
    private boolean isValidCategory(Category category) {
        boolean isValid = 
            category != Category.NONE &&
            //category != Category.BEACH &&
            //category != Category.OCEAN &&
            category != Category.NETHER &&
            category != Category.THEEND;
        
        return  isValid;
    }

}
