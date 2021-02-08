package com.bespectacled.modernbeta.gen;

import java.util.BitSet;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Supplier;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.biome.OldBiomeSource;
import com.bespectacled.modernbeta.feature.OldFeatures;
import com.bespectacled.modernbeta.gen.provider.AbstractChunkProvider;
import com.bespectacled.modernbeta.gen.provider.settings.ChunkProviderSettings;
import com.bespectacled.modernbeta.mixin.MixinChunkGeneratorInvoker;
import com.bespectacled.modernbeta.structure.OldStructures;
import com.bespectacled.modernbeta.util.MutableBiomeArray;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.entity.SpawnGroup;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureManager;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;
import net.minecraft.world.gen.feature.StructureFeature;

public class OldChunkGenerator extends NoiseChunkGenerator {
    /*
    public static final Codec<OldChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(BiomeSource.CODEC.fieldOf("biome_source").forGetter(generator -> generator.biomeSource),
                    Codec.LONG.fieldOf("seed").stable().forGetter(generator -> generator.worldSeed),
                    OldGeneratorSettings.CODEC.fieldOf("settings").forGetter(generator -> generator.settings))
            .apply(instance, instance.stable(OldChunkGenerator::new)));
    */
    public static final Codec<OldChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        BiomeSource.CODEC.fieldOf("biome_source").forGetter(generator -> generator.biomeSource),
        Codec.LONG.fieldOf("seed").stable().forGetter(generator -> generator.worldSeed),
        Codec.STRING.fieldOf("world_type").stable().forGetter(generator -> generator.worldType),
        ChunkGeneratorSettings.CODEC.fieldOf("settings").forGetter(generator -> generator.generatorSettings),
        ChunkProviderSettings.CODEC.fieldOf("chunk_provider_settings").forGetter(generator -> generator.providerSettings)
    ).apply(instance, instance.stable(OldChunkGenerator::new)));
    
    private final String worldType;
    private final boolean generateOceans;
    
    private final ChunkGeneratorSettings generatorSettings;
    private final ChunkProviderSettings providerSettings;
    
    private final OldBiomeSource biomeSource;
    private final AbstractChunkProvider chunkProvider;
    
    public OldChunkGenerator(BiomeSource biomeSource, long seed, String worldType, ChunkGeneratorSettings chunkGeneratorSettings, ChunkProviderSettings chunkProviderSettings) {
        super(biomeSource, seed, () -> chunkGeneratorSettings);
        
        this.biomeSource = (OldBiomeSource)biomeSource;
        
        this.worldType = worldType;
        this.generatorSettings = chunkGeneratorSettings;
        this.providerSettings = chunkProviderSettings;
        
        this.chunkProvider = WorldType.fromName(this.worldType).createChunkProvider(seed, chunkGeneratorSettings, chunkProviderSettings);
        this.generateOceans = this.providerSettings.generateOceans();
    }
    

    public static void register() {
        Registry.register(Registry.CHUNK_GENERATOR, ModernBeta.createId("old"), CODEC);
    }
    
    @Override
    protected Codec<? extends ChunkGenerator> getCodec() {
        return OldChunkGenerator.CODEC;
    }
    
    @Override
    public void populateNoise(WorldAccess worldAccess, StructureAccessor structureAccessor, Chunk chunk) {
        this.chunkProvider.provideChunk(worldAccess, structureAccessor, chunk, this.biomeSource);
    }
    
    @Override
    public void buildSurface(ChunkRegion region, Chunk chunk) {
        this.chunkProvider.provideSurface(region, chunk, this.biomeSource);
        
        if (this.generateOceans) {
            MutableBiomeArray mutableBiomes = MutableBiomeArray.inject(chunk.getBiomeArray());
            ChunkPos pos = chunk.getPos();
            Biome biome;
            
            // Replace biomes in bodies of water at least four deep with ocean biomes
            for (int x = pos.getStartX(); x < pos.getEndX(); x += 4) {
                for (int z = pos.getStartZ(); z < pos.getEndZ(); z += 4) {    
                    
                    int y = GenUtil.getSolidHeight(chunk, this.getWorldHeight(), x, z);

                    if (y < this.getSeaLevel() - 4) {
                        biome = biomeSource.getOceanBiomeForNoiseGen(x >> 2, 0, z >> 2);
                        
                        mutableBiomes.setBiome(x, 0, z, biome);
                    }
                }   
            }
        }
    }

    @Override
    public void generateFeatures(ChunkRegion chunkRegion, StructureAccessor structureAccessor) {
        OldFeatures.OLD_FANCY_OAK.chunkReset();
        
        int ctrX = chunkRegion.getCenterChunkX();
        int ctrZ = chunkRegion.getCenterChunkZ();
        int ctrAbsX = ctrX * 16;
        int ctrAbsZ = ctrZ * 16;

        Chunk ctrChunk = chunkRegion.getChunk(ctrX, ctrZ);
        
        Biome biome = GenUtil.getOceanBiome(ctrChunk, this, this.getBiomeSource(), generateOceans, this.getSeaLevel());

        long popSeed = this.random.setPopulationSeed(chunkRegion.getSeed(), ctrAbsX, ctrAbsZ);

        try {
            biome.generateFeatureStep(structureAccessor, this, chunkRegion, popSeed, this.random, new BlockPos(ctrAbsX, 0, ctrAbsZ));
        } catch (Exception exception) {
            CrashReport report = CrashReport.create(exception, "Biome decoration");
            report.addElement("Generation").add("CenterX", ctrX).add("CenterZ", ctrZ).add("Seed", popSeed).add("Biome", biome);
            throw new CrashException(report);
        }
    }
    
    @Override
    public void carve(long seed, BiomeAccess biomeAccess, Chunk chunk, GenerationStep.Carver carver) {
        BiomeAccess biomeAcc = biomeAccess.withSource(biomeSource);
        ChunkPos chunkPos = chunk.getPos();

        int mainChunkX = chunkPos.x;
        int mainChunkZ = chunkPos.z;

        Biome biome = GenUtil.getOceanBiome(chunk, this, biomeSource, generateOceans, this.getSeaLevel());
        GenerationSettings genSettings = biome.getGenerationSettings();
        
        BitSet bitSet = ((ProtoChunk)chunk).getOrCreateCarvingMask(carver);

        this.random.setSeed(seed);
        long l = (this.random.nextLong() / 2L) * 2L + 1L;
        long l1 = (this.random.nextLong() / 2L) * 2L + 1L;

        for (int chunkX = mainChunkX - 8; chunkX <= mainChunkX + 8; ++chunkX) {
            for (int chunkZ = mainChunkZ - 8; chunkZ <= mainChunkZ + 8; ++chunkZ) {
                List<Supplier<ConfiguredCarver<?>>> carverList = genSettings.getCarversForStep(carver);
                ListIterator<Supplier<ConfiguredCarver<?>>> carverIterator = carverList.listIterator();

                while (carverIterator.hasNext()) {
                    ConfiguredCarver<?> configuredCarver = carverIterator.next().get();
                    
                    this.random.setSeed((long) chunkX * l + (long) chunkZ * l1 ^ seed);
                    
                    if (configuredCarver.shouldCarve(random, chunkX, chunkZ)) {
                        configuredCarver.carve(chunk, biomeAcc::getBiome, this.random, this.getSeaLevel(), chunkX, chunkZ,
                                mainChunkX, mainChunkZ, bitSet);

                    }
                }
            }
        }
    }
    
    @Override
    public void setStructureStarts(
        DynamicRegistryManager dynamicRegistryManager, 
        StructureAccessor structureAccessor,   
        Chunk chunk, 
        StructureManager structureManager, 
        long seed
    ) {
        ChunkPos chunkPos = chunk.getPos();
        
        Biome biome = GenUtil.getOceanBiome(chunk, this, biomeSource, generateOceans, this.getSeaLevel());

        ((MixinChunkGeneratorInvoker)this).invokeSetStructureStart(
            ConfiguredStructureFeatures.STRONGHOLD, 
            dynamicRegistryManager, 
            structureAccessor, 
            chunk,
            structureManager, 
            seed, 
            chunkPos, 
            biome
        );
        
        for (final Supplier<ConfiguredStructureFeature<?, ?>> supplier : biome.getGenerationSettings()
                .getStructureFeatures()) {
            ((MixinChunkGeneratorInvoker)this).invokeSetStructureStart(
                supplier.get(),
                dynamicRegistryManager, 
                structureAccessor,
                chunk, 
                structureManager,
                seed, 
                chunkPos,
                biome
            );
        }
    }
    
    @Override
    public int getHeight(int x, int z, Heightmap.Type type) {
        return this.chunkProvider.getHeight(x, z, type);
    }
    
    @Override
    public BlockPos locateStructure(ServerWorld world, StructureFeature<?> feature, BlockPos center, int radius, boolean skipExistingChunks) {
        if (!this.generateOceans)
            if (feature.equals(StructureFeature.OCEAN_RUIN) || 
                feature.equals(StructureFeature.SHIPWRECK) || 
                feature.equals(StructureFeature.BURIED_TREASURE) ||
                feature.equals(OldStructures.OCEAN_SHRINE_STRUCTURE)) {
                return null;
            }

        System.out.println("Attempting to locate structures... " + this.getStructuresConfig().getForType(feature));
        return super.locateStructure(world, feature, center, radius, skipExistingChunks);
    }
    
    @Override
    public List<SpawnSettings.SpawnEntry> getEntitySpawnList(Biome biome, StructureAccessor structureAccessor, SpawnGroup spawnGroup, BlockPos blockPos) {
        if (spawnGroup == SpawnGroup.MONSTER) {
            if (structureAccessor.getStructureAt(blockPos, false, OldStructures.OCEAN_SHRINE_STRUCTURE).hasChildren()) {
                return OldStructures.OCEAN_SHRINE_STRUCTURE.getMonsterSpawns();
            }
        }

        return super.getEntitySpawnList(biome, structureAccessor, spawnGroup, blockPos);
    }

    @Override
    public int getWorldHeight() {
        return chunkProvider.getWorldHeight();
    }

    @Override
    public int getSeaLevel() {
        return chunkProvider.getSeaLevel();
    }
    
    @Override
    public ChunkGenerator withSeed(long seed) {
        return new OldChunkGenerator(this.biomeSource.withSeed(seed), seed, this.worldType, this.generatorSettings, this.providerSettings);
    }
    
    public WorldType getWorldType() {
        return WorldType.fromName(this.worldType);
    }
    
    public AbstractChunkProvider getChunkProvider() {
        return this.chunkProvider;
    }
    
}
