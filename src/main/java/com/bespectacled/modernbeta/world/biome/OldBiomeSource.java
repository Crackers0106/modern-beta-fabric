package com.bespectacled.modernbeta.world.biome;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.registry.Registries;
import com.bespectacled.modernbeta.api.world.WorldSettings;
import com.bespectacled.modernbeta.api.world.biome.BiomeProvider;
import com.bespectacled.modernbeta.api.world.biome.BiomeResolver;
import com.bespectacled.modernbeta.api.world.gen.HeightmapSampler;
import com.bespectacled.modernbeta.mixin.MixinBiomeSourceAccessor;
import com.bespectacled.modernbeta.util.NBTUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.dynamic.RegistryLookupCodec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;

public class OldBiomeSource extends BiomeSource {
    public static final Codec<OldBiomeSource> CODEC = RecordCodecBuilder.create(instance -> instance
        .group(
            Codec.LONG.fieldOf("seed").stable().forGetter(biomeSource -> biomeSource.seed),
            RegistryLookupCodec.of(Registry.BIOME_KEY).forGetter(biomeSource -> biomeSource.biomeRegistry),
            NbtCompound.CODEC.fieldOf("provider_settings").forGetter(biomeSource -> biomeSource.biomeProviderSettings)
        ).apply(instance, (instance).stable(OldBiomeSource::new)));

    private static final int OCEAN_MIN_DEPTH = 4;
    
    private final long seed;
    private final Registry<Biome> biomeRegistry;
    private final NbtCompound biomeProviderSettings;
    //private final Optional<NbtCompound> caveBiomeProviderSettings;
    private final BiomeProvider biomeProvider;

    private Optional<HeightmapSampler> heightmapSampler;
    
    public OldBiomeSource(long seed, Registry<Biome> biomeRegistry, NbtCompound settings) {
        super(List.of());
        
        this.seed = seed;
        this.biomeRegistry = biomeRegistry;
        this.biomeProviderSettings = settings;
        this.biomeProvider = Registries.BIOME.get(NBTUtil.readStringOrThrow(WorldSettings.TAG_BIOME, settings)).apply(this);
        
        this.heightmapSampler = Optional.empty();
        
        // Set biomes list here, instead of constructor.
        ((MixinBiomeSourceAccessor)this).setBiomes(
            this.biomeProvider
                .getBiomesForRegistry()
                .stream()
                .map((registryKey) -> (Biome) biomeRegistry.get(registryKey))
                .collect(Collectors.toList())
        );
    }

    @Override
    public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        //return this.biomeProvider.getBiomeForNoiseGen(this.biomeRegistry, biomeX, biomeY, biomeZ);
        
        int x = biomeX << 2;
        int z = biomeZ << 2;
        
        if (this.heightmapSampler.isPresent()) {
            HeightmapSampler sampler = this.heightmapSampler.get();
            
            if (sampler.getHeight(x, z, Heightmap.Type.OCEAN_FLOOR_WG, null) < sampler.getSeaLevel() - OCEAN_MIN_DEPTH)
                return this.biomeProvider.getOceanBiomeForNoiseGen(this.biomeRegistry, biomeX, biomeY, biomeZ);
        } 
        
        return this.biomeProvider.getBiomeForNoiseGen(this.biomeRegistry, biomeX, biomeY, biomeZ);
    }

    public Biome getOceanBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        return this.biomeProvider.getOceanBiomeForNoiseGen(this.biomeRegistry, biomeX, biomeY, biomeZ);
    }
    
    public Biome getBiomeForSurfaceGen(ChunkRegion region, BlockPos pos) {
        if (this.biomeProvider instanceof BiomeResolver biomeResolver)
            return biomeResolver.getBiome(this.biomeRegistry, pos.getX(), pos.getY(), pos.getZ());
        
        return region.getBiome(pos);
    }
    
    public Registry<Biome> getBiomeRegistry() {
        return this.biomeRegistry;
    }
    
    public long getWorldSeed() {
        return this.seed;
    }
    
    public BiomeProvider getBiomeProvider() {
        return this.biomeProvider;
    }
    
    public NbtCompound getProviderSettings() {
        return new NbtCompound().copyFrom(this.biomeProviderSettings);
    }
    
    public void setHeightmapSampler(HeightmapSampler heightmapSampler) {
        this.heightmapSampler = Optional.of(heightmapSampler);
    }
    
    @Environment(EnvType.CLIENT)
    @Override
    public BiomeSource withSeed(long seed) {
        return new OldBiomeSource(seed, this.biomeRegistry, this.biomeProviderSettings);
    }
    
    @Override
    protected Codec<? extends BiomeSource> getCodec() {
        return OldBiomeSource.CODEC;
    }

    public static void register() {
        Registry.register(Registry.BIOME_SOURCE, ModernBeta.createId("old"), CODEC);
    }
}