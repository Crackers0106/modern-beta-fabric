package com.bespectacled.modernbeta.world.gen.provider;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.world.biome.BetaClimateResolver;
import com.bespectacled.modernbeta.api.world.gen.NoiseChunkProvider;
import com.bespectacled.modernbeta.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.noise.SimplexNoise;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.util.NBTUtil;
import com.bespectacled.modernbeta.util.GenUtil;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;
import com.bespectacled.modernbeta.world.gen.OldChunkGenerator;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkRandom;

public class BetaIslandsChunkProvider extends NoiseChunkProvider implements BetaClimateResolver {
    private final PerlinOctaveNoise minLimitNoiseOctaves;
    private final PerlinOctaveNoise maxLimitNoiseOctaves;
    private final PerlinOctaveNoise mainNoiseOctaves;
    private final PerlinOctaveNoise beachNoiseOctaves;
    private final PerlinOctaveNoise surfaceNoiseOctaves;
    private final PerlinOctaveNoise scaleNoiseOctaves;
    private final PerlinOctaveNoise depthNoiseOctaves;
    private final PerlinOctaveNoise forestNoiseOctaves;
    
    private final SimplexNoise islandNoise;
    
    private final boolean generateOuterIslands;
    private final int centerIslandRadius;
    private final float centerIslandFalloff;
    private final int centerOceanLerpDistance;
    private final int centerOceanRadius;
    private final float outerIslandNoiseScale;
    private final float outerIslandNoiseOffset;
    
    public BetaIslandsChunkProvider(OldChunkGenerator chunkGenerator) {
        super(chunkGenerator);
        //super(chunkGenerator, 0, 128, 64, 50, 0, -10, BlockStates.STONE, BlockStates.WATER, 2, 1, 1.0, 1.0, 80, 160, -10, 3, 0, 15, 3, 0, false, false, false, false, false);
        
        // Noise Generators
        this.minLimitNoiseOctaves = new PerlinOctaveNoise(rand, 16, true);
        this.maxLimitNoiseOctaves = new PerlinOctaveNoise(rand, 16, true);
        this.mainNoiseOctaves = new PerlinOctaveNoise(rand, 8, true);
        this.beachNoiseOctaves = new PerlinOctaveNoise(rand, 4, true);
        this.surfaceNoiseOctaves = new PerlinOctaveNoise(rand, 4, true);
        this.scaleNoiseOctaves = new PerlinOctaveNoise(rand, 10, true);
        this.depthNoiseOctaves = new PerlinOctaveNoise(rand, 16, true);
        this.forestNoiseOctaves = new PerlinOctaveNoise(rand, 8, true);
        
        this.islandNoise = new SimplexNoise(rand);
        
        // Beta Islands settings
        this.generateOuterIslands = NBTUtil.readBoolean("generateOuterIslands", providerSettings, ModernBeta.GEN_CONFIG.generateOuterIslands);
        this.centerIslandRadius = NBTUtil.readInt("centerIslandRadius", providerSettings, ModernBeta.GEN_CONFIG.centerIslandRadius);
        this.centerIslandFalloff = NBTUtil.readFloat("centerIslandFalloff", providerSettings, ModernBeta.GEN_CONFIG.centerIslandFalloff);
        this.centerOceanLerpDistance = NBTUtil.readInt("centerOceanLerpDistance", providerSettings, ModernBeta.GEN_CONFIG.centerOceanLerpDistance);
        this.centerOceanRadius = NBTUtil.readInt("centerOceanRadius", providerSettings, ModernBeta.GEN_CONFIG.centerOceanRadius);
        this.outerIslandNoiseScale = NBTUtil.readFloat("outerIslandNoiseScale", providerSettings, ModernBeta.GEN_CONFIG.outerIslandNoiseScale);
        this.outerIslandNoiseOffset = NBTUtil.readFloat("outerIslandNoiseOffset", providerSettings, ModernBeta.GEN_CONFIG.outerIslandNoiseOffset);
        
        this.setSeed(seed);
        setForestOctaves(forestNoiseOctaves);
    }
    
    @Override
    public void provideSurface(ChunkRegion region, Chunk chunk, OldBiomeSource biomeSource) {
        double eighth = 0.03125D;

        int chunkX = chunk.getPos().x;
        int chunkZ = chunk.getPos().z;
        
        int bedrockFloor = this.minY + this.bedrockFloor;
        
        ChunkRandom rand = this.createChunkRand(chunkX, chunkZ);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        
        double[] sandNoise = this.surfaceNoisePool.borrowObj();
        double[] gravelNoise = this.surfaceNoisePool.borrowObj();
        double[] surfaceNoise = this.surfaceNoisePool.borrowObj();

        sandNoise = beachNoiseOctaves.sampleArrBeta(
            sandNoise, 
            chunkX * 16, chunkZ * 16, 0.0D, 
            16, 16, 1,
            eighth, eighth, 1.0D);
        
        gravelNoise = beachNoiseOctaves.sampleArrBeta(
            gravelNoise, 
            chunkX * 16, 109.0134D, chunkZ * 16, 
            16, 1, 16, 
            eighth, 1.0D, eighth);
        
        surfaceNoise = surfaceNoiseOctaves.sampleArrBeta(
            surfaceNoise, 
            chunkX * 16, chunkZ * 16, 0.0D, 
            16, 16, 1,
            eighth * 2D, eighth * 2D, eighth * 2D
        );

        for (int z = 0; z < 16; z++) {
            for (int x = 0; x < 16; x++) {
                int absX = (chunkX << 4) + x;
                int absZ = (chunkZ << 4) + z;
                int topY = GenUtil.getSolidHeight(chunk, this.worldHeight, this.minY, x, z, this.defaultFluid) + 1;
                
                boolean genSandBeach = sandNoise[z + x * 16] + rand.nextDouble() * 0.20000000000000001D > 0.0D;
                boolean genGravelBeach = gravelNoise[z + x * 16] + rand.nextDouble() * 0.20000000000000001D > 3D;
                int surfaceDepth = (int) (surfaceNoise[z + x * 16] / 3D + 3D + rand.nextDouble() * 0.25D);
                
                int flag = -1;
                
                Biome biome = biomeSource.getBiomeForSurfaceGen(region, mutable.set(absX, topY, absZ));

                BlockState biomeTopBlock = biome.getGenerationSettings().getSurfaceConfig().getTopMaterial();
                BlockState biomeFillerBlock = biome.getGenerationSettings().getSurfaceConfig().getUnderMaterial();

                BlockState topBlock = biomeTopBlock;
                BlockState fillerBlock = biomeFillerBlock;
                
                boolean usedCustomSurface = this.useCustomSurfaceBuilder(biome, biomeSource.getBiomeRegistry().getId(biome), region, chunk, rand, mutable);

                // Generate from top to bottom of world
                for (int y = this.worldTopY - 1; y >= this.minY; y--) {

                    // Randomly place bedrock from y=0 (or minHeight) to y=5
                    if (y <= bedrockFloor + rand.nextInt(5)) {
                        chunk.setBlockState(mutable.set(x, y, z), BlockStates.BEDROCK, false);
                        continue;
                    }
                    
                    // Skip if used custom surface generation or if below minimum surface level.
                    if (usedCustomSurface || y < this.minSurfaceY) {
                        continue;
                    }

                    BlockState someBlock = chunk.getBlockState(mutable.set(x, y, z));

                    if (someBlock.equals(BlockStates.AIR)) { // Skip if air block
                        flag = -1;
                        continue;
                    }

                    if (!someBlock.equals(this.defaultBlock)) { // Skip if not stone
                        continue;
                    }

                    if (flag == -1) {
                        if (surfaceDepth <= 0) { // Generate stone basin if noise permits
                            topBlock = BlockStates.AIR;
                            fillerBlock = this.defaultBlock;
                            
                        } else if (y >= this.seaLevel - 4 && y <= this.seaLevel + 1) { // Generate beaches at this y range
                            topBlock = biomeTopBlock;
                            fillerBlock = biomeFillerBlock;

                            if (genGravelBeach) {
                                topBlock = BlockStates.AIR; // This reduces gravel beach height by 1
                                fillerBlock = BlockStates.GRAVEL;
                            }

                            if (genSandBeach) {
                                topBlock = BlockStates.SAND;
                                fillerBlock = BlockStates.SAND;
                            }
                        }

                        if (y < this.seaLevel && topBlock.equals(BlockStates.AIR)) { // Generate water bodies
                            topBlock = this.defaultFluid;
                        }

                        flag = surfaceDepth;
                        if (y >= this.seaLevel - 1) {
                            chunk.setBlockState(mutable.set(x, y, z), topBlock, false);
                        } else {
                            chunk.setBlockState(mutable.set(x, y, z), fillerBlock, false);
                        }

                        continue;
                    }

                    if (flag <= 0) {
                        continue;
                    }

                    flag--;
                    chunk.setBlockState(mutable.set(x, y, z), fillerBlock, false);

                    // Generates layer of sandstone starting at lowest block of sand, of height 1 to 4.
                    if (flag == 0 && fillerBlock.equals(BlockStates.SAND)) {
                        flag = rand.nextInt(4);
                        fillerBlock = BlockStates.SANDSTONE;
                    }
                }
            }
        }
        
        this.surfaceNoisePool.returnObj(sandNoise);
        this.surfaceNoisePool.returnObj(gravelNoise);
        this.surfaceNoisePool.returnObj(surfaceNoise);
    }

    @Override
    protected void generateScaleDepth(int startNoiseX, int startNoiseZ, int curNoiseX, int curNoiseZ, double[] scaleDepth) {
        int horizNoiseResolution = 16 / (this.noiseSizeX + 1);
        int x = (startNoiseX / this.noiseSizeX * 16) + curNoiseX * horizNoiseResolution + horizNoiseResolution / 2;
        int z = (startNoiseZ / this.noiseSizeZ * 16) + curNoiseZ * horizNoiseResolution + horizNoiseResolution / 2;
        
        int noiseX = startNoiseX + curNoiseX;
        int noiseZ = startNoiseZ + curNoiseZ;
        
        double depthNoiseScaleX = 200D;
        double depthNoiseScaleZ = 200D;
        
        //double baseSize = noiseResolutionY / 2D; // Or: 17 / 2D = 8.5
        double baseSize = 8.5D;
        
        double temp = this.sampleTemp(x, z);
        double rain = this.sampleRain(x, z) * temp;
        
        rain = 1.0D - rain;
        rain *= rain;
        rain *= rain;
        rain = 1.0D - rain;

        double scale = this.scaleNoiseOctaves.sample(noiseX, noiseZ, 1.121D, 1.121D);
        scale = (scale + 256D) / 512D;
        scale *= rain;
        
        if (scale > 1.0D) {
            scale = 1.0D;
        }
        
        double depth0 = this.depthNoiseOctaves.sample(noiseX, noiseZ, depthNoiseScaleX, depthNoiseScaleZ);
        depth0 /= 8000D;

        if (depth0 < 0.0D) {
            depth0 = -depth0 * 0.29999999999999999D;
        }

        depth0 = depth0 * 3D - 2D;

        if (depth0 < 0.0D) {
            depth0 /= 2D;

            if (depth0 < -1D) {
                depth0 = -1D;
            }

            depth0 /= 1.3999999999999999D;
            depth0 /= 2D;

            scale = 0.0D;

        } else {
            if (depth0 > 1.0D) {
                depth0 = 1.0D;
            }
            depth0 /= 8D;
        }

        if (scale < 0.0D) {
            scale = 0.0D;
        }

        scale += 0.5D;
        //depthVal = (depthVal * (double) noiseResolutionY) / 16D;
        //double depthVal2 = (double) noiseResolutionY / 2D + depthVal * 4D;
        depth0 = depth0 * baseSize / 8D;
        double depth1 = baseSize + depth0 * 4D;
        
        scaleDepth[0] = scale;
        scaleDepth[1] = depth1;
        scaleDepth[2] = this.generateIslandOffset(startNoiseX, startNoiseZ, curNoiseX, curNoiseZ);
    }
    
    
    @Override
    protected double generateNoise(int noiseX, int noiseY, int noiseZ, double[] scaleDepth) {
        double scale = scaleDepth[0];
        double depth = scaleDepth[1];
        double islandOffset = scaleDepth[2];
        
        // Var names taken from old customized preset names
        double coordinateScale = 684.41200000000003D * this.xzScale; 
        double heightScale = 684.41200000000003D * this.yScale;
        
        double mainNoiseScaleX = this.xzFactor; // Default: 80
        double mainNoiseScaleY = this.yFactor;  // Default: 160
        double mainNoiseScaleZ = this.xzFactor;

        double lowerLimitScale = 512D;
        double upperLimitScale = 512D;
        
        double heightStretch = 12D;
        
        double density = 0.0D;
        double densityOffset = (((double)noiseY - depth) * heightStretch) / scale;
        
        if (densityOffset < 0.0D) {
            densityOffset *= 4D;
        }
        
        // Equivalent to current MC noise.sample() function, see NoiseColumnSampler.
        double mainNoise = (this.mainNoiseOctaves.sample(
            noiseX, noiseY, noiseZ, 
            coordinateScale / mainNoiseScaleX, 
            heightScale / mainNoiseScaleY, 
            coordinateScale / mainNoiseScaleZ
        ) / 10D + 1.0D) / 2D;
        
        if (mainNoise < 0.0D) {
            density = this.minLimitNoiseOctaves.sample(noiseX, noiseY, noiseZ, coordinateScale, heightScale, coordinateScale) / lowerLimitScale;
        } else if (mainNoise > 1.0D) {
            density = this.maxLimitNoiseOctaves.sample(noiseX, noiseY, noiseZ, coordinateScale, heightScale, coordinateScale) / upperLimitScale;
        } else {
            double minLimitNoise = this.minLimitNoiseOctaves.sample(noiseX, noiseY, noiseZ, coordinateScale, heightScale, coordinateScale) / lowerLimitScale;
            double maxLimitNoise = this.maxLimitNoiseOctaves.sample(noiseX, noiseY, noiseZ, coordinateScale, heightScale, coordinateScale) / upperLimitScale;
            density = minLimitNoise + (maxLimitNoise - minLimitNoise) * mainNoise;
        }
        
        // Equivalent to current MC addition of density offset, see NoiseColumnSampler.
        double densityWithOffset = density - densityOffset;
        
        // Add island offset
        densityWithOffset += islandOffset;
        
        // Sample for noise caves
        densityWithOffset = this.sampleNoiseCave(
            densityWithOffset, 
            noiseX * this.horizontalNoiseResolution, 
            noiseY * this.verticalNoiseResolution,
            noiseZ * this.horizontalNoiseResolution
        );
        
        densityWithOffset = this.applyTopSlide(densityWithOffset, noiseY, 4);
        densityWithOffset = this.applyBottomSlide(densityWithOffset, noiseY, -3);
        
        return densityWithOffset;
    }

    private double generateIslandOffset(int startNoiseX, int startNoiseZ, int curNoiseX, int curNoiseZ) {
        float noiseX = curNoiseX + startNoiseX;
        float noiseZ = curNoiseZ + startNoiseZ;
        
        float oceanDepth = 200.0F;
        
        int centerOceanLerpDistance = this.centerOceanLerpDistance * this.noiseSizeX;
        int centerOceanRadius = this.centerOceanRadius * this.noiseSizeX;
        
        float centerIslandRadius = this.centerIslandRadius * this.noiseSizeX;
        
        float outerIslandNoiseScale = this.outerIslandNoiseScale;
        float outerIslandNoiseOffset = this.outerIslandNoiseOffset;
        
        float dist = noiseX * noiseX + noiseZ * noiseZ;
        float radius = MathHelper.sqrt(dist);
        
        float islandOffset = centerIslandRadius - radius; 
        if (islandOffset < 0.0) 
            islandOffset *= this.centerIslandFalloff; // Increase the rate of falloff past the island radius
        
        islandOffset = MathHelper.clamp(islandOffset, -oceanDepth, 0.0F);
            
        if (this.generateOuterIslands && radius > centerOceanRadius) {
            float islandAddition = (float)this.islandNoise.sample(noiseX / outerIslandNoiseScale, noiseZ / outerIslandNoiseScale, 1.0, 1.0) + outerIslandNoiseOffset;
            
            // 0.885539 = Simplex upper range, but scale a little higher to ensure island centers have untouched terrain.
            islandAddition /= 0.8F;
            islandAddition = MathHelper.clamp(islandAddition, 0.0F, 1.0F);
            
            // Interpolate noise addition so there isn't a sharp cutoff at start of ocean ring edge.
            islandAddition = (float)MathHelper.clampedLerp(0.0F, islandAddition, (radius - centerOceanRadius) / centerOceanLerpDistance);
            
            islandOffset += islandAddition * oceanDepth;
            islandOffset = MathHelper.clamp(islandOffset, -oceanDepth, 0.0F);
        }
        
        return islandOffset;
    }
}
