package com.bespectacled.modernbeta.biome.provider.settings;

import com.bespectacled.modernbeta.ModernBeta;

import net.minecraft.nbt.NbtCompound;

public class BiomeProviderSettings {
    public static NbtCompound createBiomeSettings(String biomeType, String caveBiomeType, String singleBiome) {
        NbtCompound settings = new NbtCompound();
        
        settings.putString("biomeType", biomeType);
        settings.putString("caveBiomeType", caveBiomeType);
        settings.putString("singleBiome", singleBiome);
        
        return settings;
    }

    public static NbtCompound addBetaBiomeSettings(NbtCompound settings) {
        settings.putString("desert", ModernBeta.BETA_CONFIG.biomeConfig.betaDesertBiome);
        settings.putString("forest", ModernBeta.BETA_CONFIG.biomeConfig.betaForestBiome);
        settings.putString("ice_desert", ModernBeta.BETA_CONFIG.biomeConfig.betaIceDesertBiome);
        settings.putString("plains", ModernBeta.BETA_CONFIG.biomeConfig.betaPlainsBiome);
        settings.putString("rainforest", ModernBeta.BETA_CONFIG.biomeConfig.betaRainforestBiome);
        settings.putString("savanna", ModernBeta.BETA_CONFIG.biomeConfig.betaSavannaBiome);
        settings.putString("shrubland", ModernBeta.BETA_CONFIG.biomeConfig.betaShrublandBiome);
        settings.putString("seasonal_forest", ModernBeta.BETA_CONFIG.biomeConfig.betaSeasonalForestBiome);
        settings.putString("swampland", ModernBeta.BETA_CONFIG.biomeConfig.betaSwamplandBiome);
        settings.putString("taiga", ModernBeta.BETA_CONFIG.biomeConfig.betaTaigaBiome);
        settings.putString("tundra", ModernBeta.BETA_CONFIG.biomeConfig.betaTundraBiome);
        
        settings.putString("ocean", ModernBeta.BETA_CONFIG.biomeConfig.betaOceanBiome);
        settings.putString("cold_ocean", ModernBeta.BETA_CONFIG.biomeConfig.betaColdOceanBiome);
        settings.putString("frozen_ocean", ModernBeta.BETA_CONFIG.biomeConfig.betaFrozenOceanBiome);
        settings.putString("lukewarm_ocean", ModernBeta.BETA_CONFIG.biomeConfig.betaLukewarmOceanBiome);
        settings.putString("warm_ocean", ModernBeta.BETA_CONFIG.biomeConfig.betaWarmOceanBiome);
        
        return settings;
    }
    
    public static NbtCompound addVanillaBiomeSettings(NbtCompound settings) {
        settings.putInt("vanillaBiomeSize", ModernBeta.BETA_CONFIG.biomeConfig.vanillaBiomeSize);
        settings.putInt("vanillaOceanBiomeSize", ModernBeta.BETA_CONFIG.biomeConfig.vanillaOceanBiomeSize);
        
        return settings;
    }

}