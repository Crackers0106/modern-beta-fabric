package com.bespectacled.modernbeta.gen.provider.settings;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.mixin.MixinRegistryInvoker;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;

import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

public class ChunkProviderSettingsRegistry {
    public static final RegistryKey<Registry<Codec<? extends ChunkProviderSettings>>> CHUNK_PROVIDER_SETTINGS_KEY;
    public static final Registry<Codec<? extends ChunkProviderSettings>> CHUNK_PROVIDER_SETTINGS;
    
    private static <T> RegistryKey<Registry<T>> createRegistryKey(String registryId) {
        return RegistryKey.<T>ofRegistry(ModernBeta.createId(registryId));
    }
    
    static {
        CHUNK_PROVIDER_SETTINGS_KEY = createRegistryKey("worldgen/chunk_provider_settings");
        CHUNK_PROVIDER_SETTINGS = MixinRegistryInvoker.invokeCreate(CHUNK_PROVIDER_SETTINGS_KEY, Lifecycle.stable(), () -> ChunkProviderSettings.CODEC);
    }
}
