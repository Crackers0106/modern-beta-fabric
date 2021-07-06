package com.bespectacled.modernbeta.mixin.colormatic;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.bespectacled.modernbeta.api.world.biome.BetaClimateResolver;

import org.spongepowered.asm.mixin.injection.At;

import io.github.kvverti.colormatic.colormap.BiomeColormap;
import io.github.kvverti.colormatic.properties.ColormapProperties;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.biome.Biome;

@Mixin(BiomeColormap.class)
public class MixinBiomeColormap implements BetaClimateResolver {
    @Shadow private ColormapProperties properties;
    @Shadow private NativeImage colormap;

    @Unique private boolean isBetaWorld = false;
    
    @Inject(
        method = "getColor(Lnet/minecraft/util/registry/DynamicRegistryManager;Lnet/minecraft/world/biome/Biome;III)I", 
        at = @At("HEAD"), 
        cancellable = true
    )
    private void mixin(DynamicRegistryManager manager, Biome biome, int posX, int posY, int posZ, CallbackInfoReturnable<Integer> info) {
        if (this.isBetaWorld && this.properties.getFormat() == ColormapProperties.Format.VANILLA) {
            double temp = this.sampleTemp(posX, posZ);
            double rain = this.sampleRain(posX, posZ);
            
            info.setReturnValue(((MixinBiomeColormapInvoker)this).invokeGetColor(temp, rain));
        }
    }
}
