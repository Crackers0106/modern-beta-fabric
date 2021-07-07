package com.bespectacled.modernbeta.client.color;

import java.util.function.Supplier;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.world.biome.BetaClimateResolver;
import net.minecraft.block.BlockState;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.client.color.world.GrassColors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BlockRenderView;

public final class BetaBlockColors implements BetaClimateResolver {
    private static BetaBlockColors INSTANCE;
    
    private final Supplier<Boolean> useBetaColorsConfigSupplier;
    private boolean useBetaColors;
    
    private BetaBlockColors() {
        this.useBetaColorsConfigSupplier = () -> ModernBeta.RENDER_CONFIG.renderBetaBiomeColor;
        this.useBetaColors = false;
    }
    
    public static BetaBlockColors getInstance() {
        if (INSTANCE == null)
            INSTANCE = new BetaBlockColors();
        
        return INSTANCE;
    }
    
    public void setSeed(long seed, boolean useBetaColors) {
        this.setSeed(seed);
        this.useBetaColors = useBetaColors;
    }
    
    public int getGrassColor(BlockState state, BlockRenderView view, BlockPos pos, int tintNdx) {
        if (view == null || pos == null) { // Appears to enter here when loading color for inventory block
            return 8174955; // Default tint, from wiki
        }
        
        if (this.useBetaColors && this.useBetaColorsConfigSupplier.get()) {
            int x = pos.getX();
            int z = pos.getZ();

            double temp = this.sampleTemp(x, z);
            double rain = this.sampleRain(x, z);

            return GrassColors.getColor(temp, rain);
        }
        
        return BiomeColors.getGrassColor(view, pos);
    }
    
    public int getTallGrassColor(BlockState state, BlockRenderView view, BlockPos pos, int tintNdx) {
        if (view == null || pos == null) { // Appears to enter here when loading color for inventory block
            return 8174955; // Default tint, from wiki
        }
        
        if (this.useBetaColors && this.useBetaColorsConfigSupplier.get()) {
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();

            long shift = x * 0x2fc20f + z * 0x5d8875 + y;
            shift = shift * shift * 0x285b825L + shift * 11L;
            x = (int) ((long) x + (shift >> 14 & 31L));
            y = (int) ((long) y + (shift >> 19 & 31L));
            z = (int) ((long) z + (shift >> 24 & 31L));
            
            double temp = this.sampleTemp(x, z);
            double rain = this.sampleRain(x, z);

            return GrassColors.getColor(temp, rain);
        }
        
        return BiomeColors.getGrassColor(view, pos);
    }
    
    public int getFoliageColor(BlockState state, BlockRenderView view, BlockPos pos, int tintNdx) {
        if (view == null || pos == null) { // Appears to enter here when loading color for inventory block
            return 4764952; // Default tint, from wiki
        }
        
        if (this.useBetaColors && this.useBetaColorsConfigSupplier.get()) {
            int x = pos.getX();
            int z = pos.getZ();
            
            double temp = this.sampleTemp(x, z);
            double rain = this.sampleRain(x, z);
            
            return FoliageColors.getColor(temp, rain);
        }
        
        return BiomeColors.getFoliageColor(view, pos);
    }
    
    public int getReedColor(BlockState state, BlockRenderView view, BlockPos pos, int tintNdx) {
        if (this.useBetaColors && this.useBetaColorsConfigSupplier.get()) {
            return 0xFFFFFF;
        }
        
        return BiomeColors.getGrassColor(view, pos);
    }
    
    @SuppressWarnings("unused")
    private static double getTempOffset(int y) {
        return MathHelper.clamp(1.0 - (256D - y) / 128D, 0.0, 0.5);
    }
}
