package com.bespectacled.modernbeta.gen.provider.settings;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class InfProviderSettings extends ChunkProviderSettings {
    public static final Codec<InfProviderSettings> CODEC;
    
    public boolean generateOceans;
    
    public InfProviderSettings(boolean generateOceans) {
        this.generateOceans = generateOceans;
    }
    
    @Override
    public boolean generateOceans() {
        return generateOceans;
    }

    @Override
    protected Codec<? extends ChunkProviderSettings> getCodec() {
        return CODEC;
    }
    
    static {
        CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.BOOL.fieldOf("generate_oceans").forGetter(settings -> settings.generateOceans)
        ).apply(instance, InfProviderSettings::new));
    }
}
