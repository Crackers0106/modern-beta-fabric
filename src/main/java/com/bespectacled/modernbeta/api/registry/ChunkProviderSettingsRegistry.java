package com.bespectacled.modernbeta.api.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

import net.minecraft.nbt.CompoundTag;

public class ChunkProviderSettingsRegistry {
    public enum BuiltInChunkSettingsType {
        BETA("beta"),
        SKYLANDS("skylands"),
        ALPHA("alpha"),
        INFDEV_415("infdev_415"),
        INFDEV_227("infdev_227"),
        INDEV("indev"),
        BETA_ISLANDS("beta_islands")
        ;
        
        public final String name;
        
        private BuiltInChunkSettingsType(String name) { this.name = name; }
    }
    
    private static final Map<String, Supplier<CompoundTag>> REGISTRY = new HashMap<>(); 
    
    public static void register(String name, Supplier<CompoundTag> settings) {
        if (REGISTRY.containsKey(name)) 
            throw new IllegalArgumentException("[Modern Beta] Registry already contains chunk settings named " + name);
        
        REGISTRY.put(name, settings);
    }
    
    public static Supplier<CompoundTag> get(String name) {
        if (!REGISTRY.containsKey(name))
            throw new NoSuchElementException("[Modern Beta] Registry does not contain chunk settings named " + name);
        
        return REGISTRY.get(name);
    }
}
