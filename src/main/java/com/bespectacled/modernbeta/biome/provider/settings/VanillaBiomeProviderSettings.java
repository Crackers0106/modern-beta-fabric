package com.bespectacled.modernbeta.biome.provider.settings;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Supplier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;

public class VanillaBiomeProviderSettings extends BiomeProviderSettings {
    public static final Codec<VanillaBiomeProviderSettings> CODEC;
    
    private final List<Supplier<Biome>> biomes;
    
    public VanillaBiomeProviderSettings(List<Supplier<Biome>> biomes) {
        this.biomes = biomes;
    }
    
    @Override
    public Codec<? extends BiomeProviderSettings> getCodec() {
        return CODEC;
    }
    
    public static VanillaBiomeProviderSettings createDefaultSettings(Registry<Biome> biomeRegistry) {
        List<Supplier<Biome>> biomes = new ArrayList<Supplier<Biome>>();
        Iterator<Entry<RegistryKey<Biome>, Biome>> biomeIter = BuiltinRegistries.BIOME.getEntries().iterator();
        
        while (biomeIter.hasNext()) {
            Entry<RegistryKey<Biome>, Biome> entry = (Entry<RegistryKey<Biome>, Biome>)biomeIter.next();
            
            if (isValidCategory(entry.getValue().getCategory())) {
                biomes.add(() -> biomeRegistry.getOrThrow(entry.getKey()));
            }
        }
        
        return new VanillaBiomeProviderSettings(biomes);
    }
    
    private static boolean isValidCategory(Category category) {
        boolean isValid = 
            category != Category.NONE &&
            //category != Category.BEACH &&
            //category != Category.OCEAN &&
            category != Category.NETHER &&
            category != Category.THEEND;
        
        return isValid;
    }
    
    static {
        CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Biome.REGISTRY_CODEC.listOf().fieldOf("biomes").forGetter(settings -> settings.biomes)
        ).apply(instance, VanillaBiomeProviderSettings::new));
    }
}
