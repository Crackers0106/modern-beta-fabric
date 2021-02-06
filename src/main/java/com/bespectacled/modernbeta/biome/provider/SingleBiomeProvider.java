package com.bespectacled.modernbeta.biome.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.bespectacled.modernbeta.biome.settings.BiomeSettings;
import com.bespectacled.modernbeta.biome.settings.SingleBiomeSettings;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class SingleBiomeProvider extends AbstractBiomeProvider {
    private final Supplier<Biome> biome;
    
    public SingleBiomeProvider(long seed, BiomeSettings settings) {
        this.biome = ((SingleBiomeSettings)settings).biome;
    }

    @Override
    public Biome getBiomeForNoiseGen(Registry<Biome> registry, int biomeX, int biomeY, int biomeZ) {
        return biome.get();
    }

    @Override
    public List<RegistryKey<Biome>> getBiomesForRegistry() {

        List<RegistryKey<Biome>> biomeList = new ArrayList<RegistryKey<Biome>>();
        
        //biomeList.add(RegistryKey.of(Registry.BIOME_KEY, this.biomeId));
        
        return biomeList;
    }
}
