package com.bespectacled.modernbeta.api.world.gen;

import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;

public interface HeightmapSampler {
    int getSeaLevel();
    int getHeight(int x, int z, Heightmap.Type heightmap, HeightLimitView world);
}
