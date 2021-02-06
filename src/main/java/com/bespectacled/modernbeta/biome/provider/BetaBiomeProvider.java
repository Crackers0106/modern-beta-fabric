package com.bespectacled.modernbeta.biome.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.bespectacled.modernbeta.biome.beta.BetaBiomes;
import com.bespectacled.modernbeta.biome.beta.BetaClimateSampler;
import com.bespectacled.modernbeta.biome.settings.BetaBiomeSettings;
import com.bespectacled.modernbeta.biome.settings.BiomeSettings;
import com.bespectacled.modernbeta.biome.beta.BetaBiomes.BetaBiomeType;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class BetaBiomeProvider extends AbstractBiomeProvider {
    private static final double[] TEMP_HUMID_POINT = new double[2];
    private static final Biome BIOME_TABLE[] = new Biome[4096];
    
    public BetaBiomeProvider(long seed, BiomeSettings biomeSettings) {
        BetaClimateSampler.INSTANCE.setSeed(seed);
        
        this.generateBiomeLookup(((BetaBiomeSettings)biomeSettings).getBiomeMap());
    }

    @Override
    public Biome getBiomeForNoiseGen(Registry<Biome> registry, int biomeX, int biomeY, int biomeZ) {
        int absX = biomeX << 2;
        int absZ = biomeZ << 2;
        
        BetaClimateSampler.INSTANCE.sampleTempHumid(TEMP_HUMID_POINT, absX, absZ);
        //return registry.get(BetaBiomes.getBiomeFromLookup(TEMP_HUMID_POINT[0], TEMP_HUMID_POINT[1], this.betaBiomeType));
        return this.getBiomeFromLookup(TEMP_HUMID_POINT[0], TEMP_HUMID_POINT[1], BetaBiomeType.LAND);
    }
 
    @Override
    public Biome getOceanBiomeForNoiseGen(Registry<Biome> registry, int biomeX, int biomeY, int biomeZ) {
        int absX = biomeX << 2;
        int absZ = biomeZ << 2;
        
        BetaClimateSampler.INSTANCE.sampleTempHumid(TEMP_HUMID_POINT, absX, absZ);
        return registry.get(BetaBiomes.getBiomeFromLookup(TEMP_HUMID_POINT[0], TEMP_HUMID_POINT[1], BetaBiomeType.OCEAN));
    }
    
    @Override
    public Biome getBiomeForSurfaceGen(Registry<Biome> registry, int x, int y, int z) {
        BetaClimateSampler.INSTANCE.sampleTempHumid(TEMP_HUMID_POINT, x, z);
        return this.getBiomeFromLookup(TEMP_HUMID_POINT[0], TEMP_HUMID_POINT[1], BetaBiomeType.LAND);
    }

    @Override
    public List<RegistryKey<Biome>> getBiomesForRegistry() {
        List<RegistryKey<Biome>> biomeList = new ArrayList<RegistryKey<Biome>>();

        for (Identifier i : BetaBiomes.BIOMES) {
            biomeList.add(RegistryKey.of(Registry.BIOME_KEY, i));
        }
        
        return biomeList;
    }
    
    private void generateBiomeLookup(Map<String, Supplier<Biome>> biomeMap) {
        for (int i = 0; i < 64; i++) {
            for (int j = 0; j < 64; j++) {
                BIOME_TABLE[i + j * 64] = getBiome((float) i / 63F, (float) j / 63F, biomeMap);
            }
        }
    }
    
    private Biome getBiome(float temp, float humid, Map<String, Supplier<Biome>> biomeMap) {
        humid *= temp;

        if (temp < 0.1F) {
            return biomeMap.get("tundra").get();
        }

        if (humid < 0.2F) {
            if (temp < 0.5F) {
                return biomeMap.get("tundra").get();
            }
            if (temp < 0.95F) {
                return biomeMap.get("savanna").get();
            } else {
                return biomeMap.get("desert").get();
            }
        }

        if (humid > 0.5F && temp < 0.7F) {
            return biomeMap.get("swampland").get();
        }

        if (temp < 0.5F) {
            return biomeMap.get("taiga").get();
        }

        if (temp < 0.97F) {
            if (humid < 0.35F) {
                return biomeMap.get("shrubland").get();
            } else {
                return biomeMap.get("forest").get();
            }
        }

        if (humid < 0.45F) {
            return biomeMap.get("plains").get();
        }

        if (humid < 0.9F) {
            return biomeMap.get("seasonal_forest").get();
        } else {
            return biomeMap.get("rainforest").get();
        }
    }
    
    public Biome getBiomeFromLookup(double temp, double humid, BetaBiomeType type) {
        int i = (int) (temp * 63D);
        int j = (int) (humid * 63D);
        
        return BIOME_TABLE[i + j * 64];
    }

}
