package com.bespectacled.modernbeta.mixin;

import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import com.mojang.serialization.Lifecycle;

import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

@Mixin(Registry.class)
public interface MixinRegistryInvoker {
    @Invoker("create")
    public static <T> Registry<T> invokeCreate(RegistryKey<? extends Registry<T>> registryKey, Lifecycle lifecycle, Supplier<T> defaultEntry) {
        throw new AssertionError();
    }
}
