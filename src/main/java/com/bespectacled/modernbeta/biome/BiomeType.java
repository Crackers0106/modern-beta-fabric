package com.bespectacled.modernbeta.biome;

import java.util.function.BiFunction;

import com.bespectacled.modernbeta.biome.provider.*;
import com.bespectacled.modernbeta.biome.provider.settings.BiomeProviderSettings;

import net.minecraft.nbt.CompoundTag;

public enum BiomeType {
    BETA("beta", BetaBiomeProvider::new),
    SKY("sky", SingleBiomeProvider::new),
    CLASSIC("classic", SingleBiomeProvider::new),
    WINTER("winter", SingleBiomeProvider::new),
    PLUS("plus", PlusBiomeProvider::new),
    VANILLA("vanilla", VanillaBiomeProvider::new);
    //NETHER("nether");
    
    private final String name;
    private final BiFunction<Long, BiomeProviderSettings, AbstractBiomeProvider> biomeProvider;
    
    private BiomeType(String name, BiFunction<Long, BiomeProviderSettings, AbstractBiomeProvider> biomeProvider) {
        this.name = name;
        this.biomeProvider = biomeProvider;
    }
    
    public String getName() {
        return this.name;
    }
    
    public AbstractBiomeProvider createBiomeProvider(long seed, BiomeProviderSettings settings) {
        return this.biomeProvider.apply(seed, settings);
    }
    
    public static BiomeType fromName(String name) {
        for (BiomeType t : BiomeType.values()) {
            if (t.name.equalsIgnoreCase(name)) {
                return t;
            }
        }
        
        throw new IllegalArgumentException("[Modern Beta] No biome type matching name: " + name);
    }
    
    public static BiomeType getBiomeType(CompoundTag settings) {
        BiomeType type = BiomeType.BETA;
        
        if (settings.contains("biomeType")) 
            type = BiomeType.fromName(settings.getString("biomeType"));
        
        return type;
    }
}