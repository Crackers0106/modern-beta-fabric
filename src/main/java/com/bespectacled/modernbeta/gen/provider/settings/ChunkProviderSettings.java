package com.bespectacled.modernbeta.gen.provider.settings;

import java.util.function.Function;

import com.bespectacled.modernbeta.ModernBeta;
import com.mojang.serialization.Codec;

import net.minecraft.util.registry.Registry;

public abstract class ChunkProviderSettings {
    public static final Codec<ChunkProviderSettings> CODEC;
    
    protected abstract Codec<? extends ChunkProviderSettings> getCodec();
    
    public boolean generateOceans() {
        return false;
    }
    
    static {
        Registry.register(ChunkProviderSettingsRegistry.CHUNK_PROVIDER_SETTINGS, ModernBeta.createId("inf"), InfProviderSettings.CODEC);
        Registry.register(ChunkProviderSettingsRegistry.CHUNK_PROVIDER_SETTINGS, ModernBeta.createId("inf_old"), InfOldProviderSettings.CODEC);
        
        CODEC = ChunkProviderSettingsRegistry.CHUNK_PROVIDER_SETTINGS.dispatchStable(ChunkProviderSettings::getCodec, Function.identity());
    }
}
