package com.bespectacled.modernbeta.world.gen.provider.settings;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.registry.ChunkProviderRegistry.BuiltInChunkType;
import com.bespectacled.modernbeta.config.ModernBetaGenerationConfig;

import net.minecraft.nbt.NbtCompound;

public class ChunkProviderSettings {
    protected static final ModernBetaGenerationConfig CONFIG = ModernBeta.BETA_CONFIG.generation_config;
    
    public static NbtCompound createSettingsBeta() {
        NbtCompound settings = new NbtCompound();
        
        settings.putString("worldType", BuiltInChunkType.BETA.name);
        settings.putBoolean("generateOceans", CONFIG.generateOceans);
        settings.putBoolean("generateNoiseCaves", CONFIG.generateNoiseCaves);
        settings.putBoolean("generateAquifers", CONFIG.generateAquifers);
        settings.putBoolean("generateDeepslate", CONFIG.generateDeepslate);
        
        return settings;
    }
    
    public static NbtCompound createSettingsAlpha() {
        NbtCompound settings = new NbtCompound();
        
        settings.putString("worldType", BuiltInChunkType.ALPHA.name);
        settings.putBoolean("generateOceans", CONFIG.generateOceans);
        settings.putBoolean("generateNoiseCaves", CONFIG.generateNoiseCaves);
        settings.putBoolean("generateAquifers", CONFIG.generateAquifers);
        settings.putBoolean("generateDeepslate", CONFIG.generateDeepslate);
        
        return settings;
    }
    
    public static NbtCompound createSettingsSkylands() {
        NbtCompound settings = new NbtCompound();
        
        settings.putString("worldType", BuiltInChunkType.SKYLANDS.name);
        settings.putBoolean("generateNoiseCaves", CONFIG.generateNoiseCaves);
        settings.putBoolean("generateDeepslate", CONFIG.generateDeepslate);
        
        return settings;
    }
    
    public static NbtCompound createSettingsInfdev415() {
        NbtCompound settings = new NbtCompound();
        
        settings.putString("worldType", BuiltInChunkType.INFDEV_415.name);
        settings.putBoolean("generateOceans", CONFIG.generateOceans);
        settings.putBoolean("generateNoiseCaves", CONFIG.generateNoiseCaves);
        settings.putBoolean("generateAquifers", CONFIG.generateAquifers);
        settings.putBoolean("generateDeepslate", CONFIG.generateDeepslate);
        
        return settings;
    }
    
    public static NbtCompound createSettingsInfdev227() {
        NbtCompound settings = new NbtCompound();
        
        settings.putString("worldType", BuiltInChunkType.INFDEV_227.name);
        settings.putBoolean("generateOceans", CONFIG.generateOceans);
        settings.putBoolean("generateDeepslate", CONFIG.generateDeepslate);
        settings.putBoolean("generateInfdevPyramid", CONFIG.generateInfdevPyramid);
        settings.putBoolean("generateInfdevWall", CONFIG.generateInfdevWall);
        
        return settings;
    }
    
    public static NbtCompound createSettingsIndev() {
        NbtCompound settings = new NbtCompound();
        
        settings.putString("worldType", BuiltInChunkType.INDEV.name);
        settings.putString("levelType", CONFIG.indevLevelType);
        settings.putString("levelTheme", CONFIG.indevLevelTheme);
        settings.putInt("levelWidth", CONFIG.indevLevelWidth);
        settings.putInt("levelLength", CONFIG.indevLevelLength);
        settings.putInt("levelHeight", CONFIG.indevLevelHeight);
        settings.putFloat("caveRadius", CONFIG.indevCaveRadius);
        
        return settings;
    }
    
    public static NbtCompound createSettingsBetaIslands() {
        NbtCompound settings = new NbtCompound();
        
        settings.putString("worldType", BuiltInChunkType.BETA_ISLANDS.name);
        settings.putBoolean("generateOceans", CONFIG.generateOceans);
        settings.putBoolean("generateNoiseCaves", CONFIG.generateNoiseCaves);
        settings.putBoolean("generateAquifers", CONFIG.generateAquifers);
        settings.putBoolean("generateDeepslate", CONFIG.generateDeepslate);
        
        settings.putInt("centerOceanLerpDistance", CONFIG.centerOceanLerpDistance);
        settings.putInt("centerOceanRadius", CONFIG.centerOceanRadius);
        settings.putFloat("centerIslandFalloff", CONFIG.centerIslandFalloff);
        settings.putFloat("outerIslandNoiseScale", CONFIG.outerIslandNoiseScale);
        settings.putFloat("outerIslandNoiseOffset", CONFIG.outerIslandNoiseOffset);
        
        return settings;
    }
}