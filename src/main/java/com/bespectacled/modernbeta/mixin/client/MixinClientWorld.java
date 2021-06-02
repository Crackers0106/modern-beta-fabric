package com.bespectacled.modernbeta.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.dimension.DimensionType;
import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.world.biome.BetaClimateResolver;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;
import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(value = ClientWorld.class, priority = 1)
public abstract class MixinClientWorld extends World implements BetaClimateResolver {
    @Shadow private MinecraftClient client;
    
    @Unique private BlockPos curPos = new BlockPos(0, 0, 0);
    @Unique private boolean useBetaBiomeColors;
    @Unique private boolean isOverworld;

    private MixinClientWorld() {
        super(null, null, null, null, false, false, 0L);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(
        ClientPlayNetworkHandler netHandler, 
        ClientWorld.Properties properties,
        RegistryKey<World> worldKey, 
        DimensionType dimensionType, 
        int loadDistance, 
        Supplier<Profiler> profiler,
        WorldRenderer renderer, 
        boolean debugWorld, 
        long seed, 
        CallbackInfo ci
    ) {
        
        boolean useBetaBiomeColors = ModernBeta.RENDER_CONFIG.useFixedSeed;
        long worldSeed = ModernBeta.RENDER_CONFIG.fixedSeed;
        
        if (this.isClient && this.client.getServer() != null) { // Server check
           BiomeSource biomeSource = this.client.getServer().getOverworld().getChunkManager().getChunkGenerator().getBiomeSource();
           boolean inBetaWorld = biomeSource instanceof OldBiomeSource && ((OldBiomeSource)biomeSource).getBiomeProvider() instanceof BetaClimateResolver;
           
           if (!ModernBeta.RENDER_CONFIG.useFixedSeed && inBetaWorld) {
               useBetaBiomeColors = true;
               worldSeed = this.client.getServer().getOverworld().getSeed();
           }
        }
        
        this.isOverworld = worldKey != null ?
            worldKey.getValue().equals(DimensionType.OVERWORLD_REGISTRY_KEY.getValue()) :
            false;
        
        this.useBetaBiomeColors = useBetaBiomeColors;
        
        ModernBeta.setBlockColorsSeed(worldSeed, useBetaBiomeColors && this.isOverworld);
    }
    
    @ModifyVariable(
        method = "method_23777",
        at = @At("HEAD"),
        index = 1
    )
    private BlockPos captureBlockPos(BlockPos pos) {
        return this.curPos = pos;
    }
    
    @ModifyVariable(
        method = "method_23777",
        at = @At(value = "INVOKE_ASSIGN",  target = "Lnet/minecraft/world/biome/Biome;getSkyColor()I"),
        index = 6  
    )
    private int injectBetaSkyColor(int skyColor) {
        if (ModernBeta.RENDER_CONFIG.renderBetaSkyColor && this.useBetaBiomeColors && this.isOverworld) {
            int x = (int)this.curPos.getX();
            int z = (int)this.curPos.getZ();
            
            skyColor = this.sampleSkyColor(x, z);
        }
        
        return skyColor;
    }
}