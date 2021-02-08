package com.bespectacled.modernbeta.biome.provider.settings;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.bespectacled.modernbeta.biome.beta.*;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class BetaBiomeProviderSettings extends BiomeProviderSettings {
    public static final Codec<BetaBiomeProviderSettings> CODEC;
    
    private final Supplier<Biome> forestBiome;
    private final Supplier<Biome> shrublandBiome;
    private final Supplier<Biome> desertBiome;
    private final Supplier<Biome> savannaBiome;
    private final Supplier<Biome> plainsBiome;
    private final Supplier<Biome> seasonalForestBiome;
    private final Supplier<Biome> rainforestBiome;
    private final Supplier<Biome> swamplandBiome;
    private final Supplier<Biome> taigaBiome;
    private final Supplier<Biome> tundraBiome;
    private final Supplier<Biome> iceDesertBiome;
    
    private final Map<String, Supplier<Biome>> biomeMap;

    public BetaBiomeProviderSettings(
        Supplier<Biome> forestBiome,
        Supplier<Biome> shrublandBiome,
        Supplier<Biome> desertBiome,
        Supplier<Biome> savannaBiome,
        Supplier<Biome> plainsBiome,
        Supplier<Biome> seasonalForestBiome,
        Supplier<Biome> rainforestBiome,
        Supplier<Biome> swamplandBiome,
        Supplier<Biome> taigaBiome,
        Supplier<Biome> tundraBiome,
        Supplier<Biome> iceDesertBiome
    ) {
        this.forestBiome = forestBiome;
        this.shrublandBiome = shrublandBiome;
        this.desertBiome = desertBiome;
        this.savannaBiome = savannaBiome;
        this.plainsBiome = plainsBiome;
        this.seasonalForestBiome = seasonalForestBiome;
        this.rainforestBiome = rainforestBiome;
        this.swamplandBiome = swamplandBiome;
        this.taigaBiome = taigaBiome;
        this.tundraBiome = tundraBiome;
        this.iceDesertBiome = iceDesertBiome;
    
        this.biomeMap = new HashMap<String, Supplier<Biome>>();
        this.createBiomeMap();
    }
    
    public Map<String, Supplier<Biome>> getBiomeMap() {
        return this.biomeMap;
    }
    
    private void createBiomeMap() {
        this.biomeMap.put("forest", this.forestBiome);
        this.biomeMap.put("shrubland", this.shrublandBiome);
        this.biomeMap.put("desert", this.desertBiome);
        this.biomeMap.put("savanna", this.savannaBiome);
        this.biomeMap.put("plains", this.plainsBiome);
        this.biomeMap.put("seasonal_forest", this.seasonalForestBiome);
        this.biomeMap.put("rainforest", this.rainforestBiome);
        this.biomeMap.put("swampland", this.swamplandBiome);
        this.biomeMap.put("taiga", this.taigaBiome);
        this.biomeMap.put("tundra", this.tundraBiome);
        this.biomeMap.put("ice_desert", this.iceDesertBiome);
    }
    
    @Override
    public Codec<? extends BiomeProviderSettings> getCodec() {
        return CODEC;
    }
    
    public static BetaBiomeProviderSettings createDefaultSettings(Registry<Biome> biomeRegistry) {
        return new BetaBiomeProviderSettings(
            () -> biomeRegistry.getOrThrow(RegistryKey.of(Registry.BIOME_KEY, BetaBiomes.FOREST_ID)),
            () -> biomeRegistry.getOrThrow(RegistryKey.of(Registry.BIOME_KEY, BetaBiomes.SHRUBLAND_ID)),
            () -> biomeRegistry.getOrThrow(RegistryKey.of(Registry.BIOME_KEY, BetaBiomes.DESERT_ID)),
            () -> biomeRegistry.getOrThrow(RegistryKey.of(Registry.BIOME_KEY, BetaBiomes.SAVANNA_ID)),
            () -> biomeRegistry.getOrThrow(RegistryKey.of(Registry.BIOME_KEY, BetaBiomes.PLAINS_ID)),
            () -> biomeRegistry.getOrThrow(RegistryKey.of(Registry.BIOME_KEY, BetaBiomes.SEASONAL_FOREST_ID)),
            () -> biomeRegistry.getOrThrow(RegistryKey.of(Registry.BIOME_KEY, BetaBiomes.RAINFOREST_ID)),
            () -> biomeRegistry.getOrThrow(RegistryKey.of(Registry.BIOME_KEY, BetaBiomes.SWAMPLAND_ID)),
            () -> biomeRegistry.getOrThrow(RegistryKey.of(Registry.BIOME_KEY, BetaBiomes.TAIGA_ID)),
            () -> biomeRegistry.getOrThrow(RegistryKey.of(Registry.BIOME_KEY, BetaBiomes.TUNDRA_ID)),
            () -> biomeRegistry.getOrThrow(RegistryKey.of(Registry.BIOME_KEY, BetaBiomes.TUNDRA_ID))
        );
    }
    
    static {
        CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Biome.REGISTRY_CODEC.fieldOf("forest").forGetter(settings -> settings.forestBiome),
            Biome.REGISTRY_CODEC.fieldOf("shrubland").forGetter(settings -> settings.shrublandBiome),
            Biome.REGISTRY_CODEC.fieldOf("desert").forGetter(settings -> settings.desertBiome),
            Biome.REGISTRY_CODEC.fieldOf("savanna").forGetter(settings -> settings.savannaBiome),
            Biome.REGISTRY_CODEC.fieldOf("plains").forGetter(settings -> settings.plainsBiome),
            Biome.REGISTRY_CODEC.fieldOf("seasonal_forest").forGetter(settings -> settings.seasonalForestBiome),
            Biome.REGISTRY_CODEC.fieldOf("rainforest").forGetter(settings -> settings.rainforestBiome),
            Biome.REGISTRY_CODEC.fieldOf("swampland").forGetter(settings -> settings.swamplandBiome),
            Biome.REGISTRY_CODEC.fieldOf("taiga").forGetter(settings -> settings.taigaBiome),
            Biome.REGISTRY_CODEC.fieldOf("tundra").forGetter(settings -> settings.tundraBiome),
            Biome.REGISTRY_CODEC.fieldOf("ice_desert").forGetter(settings -> settings.iceDesertBiome)
        ).apply(instance, BetaBiomeProviderSettings::new));
    }
}
