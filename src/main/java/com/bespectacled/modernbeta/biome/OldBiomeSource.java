package com.bespectacled.modernbeta.biome;

import java.util.ArrayList;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.biome.provider.AbstractBiomeProvider;
import com.bespectacled.modernbeta.biome.provider.VanillaBiomeProvider;
import com.bespectacled.modernbeta.biome.settings.*;
import com.bespectacled.modernbeta.gen.WorldType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;

public class OldBiomeSource extends BiomeSource {
    
    public static final Codec<OldBiomeSource> CODEC = RecordCodecBuilder.create(instance -> instance
        .group(
            Codec.LONG.fieldOf("seed").stable().forGetter(biomeSource -> biomeSource.seed),
            RegistryLookupCodec.of(Registry.BIOME_KEY).forGetter(biomeSource -> biomeSource.biomeRegistry),
            Codec.STRING.fieldOf("biome_type").stable().forGetter(biomeSource -> biomeSource.biomeType),
            BiomeSettings.CODEC.fieldOf("biome_settings").forGetter(biomeSource -> biomeSource.biomeSettings)
        ).apply(instance, (instance).stable(OldBiomeSource::new)));
    
    private final long seed;
    private final String biomeType;
    private final Registry<Biome> biomeRegistry;
    private final BiomeSettings biomeSettings;
    
    private final WorldType worldType;
    //private final BiomeType biomeType;
    
    private final AbstractBiomeProvider biomeProvider;
    
    public OldBiomeSource(long seed, Registry<Biome> biomeRegistry, String biomeType, BiomeSettings biomeSettings) {
        super(BiomeType.fromName(biomeType).createBiomeProvider(seed, biomeSettings).getBiomesForRegistry().stream().map((registryKey) -> () -> (Biome) biomeRegistry.get(registryKey)));
        
        this.seed = seed;
        this.biomeType = biomeType;
        this.biomeRegistry = biomeRegistry;
        this.biomeSettings = biomeSettings;
        
        this.worldType = WorldType.BETA;
        //this.biomeType = BiomeType.BETA;
        
        //this.biomeProvider = new BetaBiomeProvider(seed, (BetaBiomeSettings)biomeSettings);
        //this.biomeProvider = new VanillaBiomeProvider(seed, (VanillaBiomeSettings)biomeSettings);
        this.biomeProvider = BiomeType.fromName(this.biomeType).createBiomeProvider(seed, biomeSettings);
        
        /*
         *
        this.worldType = WorldType.getWorldType(settings);
        this.biomeType = BiomeType.getBiomeType(settings);
        
        this.biomeProvider = getBiomeProvider(seed, settings);
        */
    }

    @Override
    public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        return this.biomeProvider.getBiomeForNoiseGen(this.biomeRegistry, biomeX, biomeY, biomeZ);
    }

    public Biome getOceanBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        return this.biomeProvider.getOceanBiomeForNoiseGen(this.biomeRegistry, biomeX, biomeY, biomeZ);
    }
    
    public Biome getBiomeForSurfaceGen(int x, int y, int z) {
        return this.biomeProvider.getBiomeForSurfaceGen(this.biomeRegistry, x, y, z);
    }

    public Registry<Biome> getBiomeRegistry() {
        return this.biomeRegistry;
    }

    public boolean isVanilla() {
        return BiomeType.fromName(this.biomeType) == BiomeType.VANILLA;
    }
    
    public boolean isBeta() {
        return BiomeType.fromName(this.biomeType) == BiomeType.BETA;
    }
    
    public boolean isSky() {
        return BiomeType.fromName(this.biomeType) == BiomeType.SKY;
    }
    
    public boolean isIndev() {
        return this.worldType == WorldType.INDEV;
    }
    
    @Override
    protected Codec<? extends BiomeSource> getCodec() {
        return OldBiomeSource.CODEC;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public BiomeSource withSeed(long seed) {
        return new OldBiomeSource(seed, this.biomeRegistry, this.biomeType, this.biomeSettings);
    }

    public static void register() {
        Registry.register(Registry.BIOME_SOURCE, ModernBeta.createId("old"), CODEC);
    }
    
    /*
    private static AbstractBiomeProvider getBiomeProvider(long seed, CompoundTag settings) {
        WorldType worldType = WorldType.getWorldType(settings);
        BiomeType biomeType = BiomeType.getBiomeType(settings);
        
        if (worldType == WorldType.INDEV)
            return new IndevBiomeProvider(seed, settings); 
        
        switch(biomeType) {
            case BETA: return new BetaBiomeProvider(seed, BetaBiomeType.LAND);
            case BETA_ICE_DESERT: return new BetaBiomeProvider(seed, BetaBiomeType.ICE_DESERT);
            case SKY: return new SingleBiomeProvider(seed, BetaBiomes.SKY_ID);
            case PLUS: return new PlusBiomeProvider(seed, ClassicBiomes.getBiomeMap(worldType));
            case CLASSIC: return new SingleBiomeProvider(seed, ClassicBiomes.getBiomeMap(worldType).get(BiomeType.CLASSIC));
            case WINTER: return new SingleBiomeProvider(seed, ClassicBiomes.getBiomeMap(worldType).get(BiomeType.WINTER));
            case VANILLA: return new VanillaBiomeProvider(seed);
            //case NETHER: return new NetherBiomeProvider(seed);
            default: throw new IllegalArgumentException("[Modern Beta] No biome provider matching biome type.  This shouldn't happen!");
        }
    }*/
}
