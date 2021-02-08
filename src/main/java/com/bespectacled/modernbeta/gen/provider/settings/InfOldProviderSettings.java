package com.bespectacled.modernbeta.gen.provider.settings;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class InfOldProviderSettings extends InfProviderSettings {
    public static final Codec<InfOldProviderSettings> CODEC;
    
    public boolean generateInfdevWall;
    public boolean generateInfdevPyramid;
    
    public InfOldProviderSettings(boolean generateOceans, boolean generateInfdevWall, boolean generateInfdevPyramid) {
        super(generateOceans);
        
        this.generateInfdevWall = generateInfdevWall;
        this.generateInfdevPyramid = generateInfdevPyramid;
    }
    
    public boolean generateInfdevWall() {
        return this.generateInfdevWall;
    }
    
    public boolean generateInfdevPyramid() {
        return this.generateInfdevPyramid;
    }

    @Override
    protected Codec<? extends ChunkProviderSettings> getCodec() {
        return CODEC;
    }
    
    static {
        CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.BOOL.fieldOf("generate_oceans").forGetter(settings -> settings.generateOceans),
            Codec.BOOL.fieldOf("generate_infdev_wall").forGetter(settings -> settings.generateInfdevWall),
            Codec.BOOL.fieldOf("generate_infdev_pyramid").forGetter(settings -> settings.generateInfdevPyramid)
        ).apply(instance, InfOldProviderSettings::new));
    }
}
