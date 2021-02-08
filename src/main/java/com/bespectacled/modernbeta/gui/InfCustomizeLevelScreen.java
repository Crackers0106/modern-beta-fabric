package com.bespectacled.modernbeta.gui;

import java.util.function.BiConsumer;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.biome.BiomeType;
import com.bespectacled.modernbeta.gen.provider.settings.ChunkProviderSettings;
import com.bespectacled.modernbeta.gen.provider.settings.InfProviderSettings;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.text.TranslatableText;

public class InfCustomizeLevelScreen extends AbstractCustomizeLevelScreen {
    
    protected BiomeType biomeType;
    protected boolean generateOceans;
    
    protected final boolean showOceansOption;
    
    protected final BiConsumer<BiomeType, ChunkProviderSettings> consumer;
    
    public InfCustomizeLevelScreen(
        CreateWorldScreen parent,
        String title,
        ChunkProviderSettings providerSettings,
        BiomeType biomeType, 
        boolean showOceansOption,
        BiConsumer<BiomeType, ChunkProviderSettings> consumer
    ) {
        super(parent, title, providerSettings);

        this.biomeType = biomeType;
        this.generateOceans = ModernBeta.BETA_CONFIG.generateOceans;
        
        this.showOceansOption = showOceansOption;
        
        this.consumer = consumer;
    }
    
    @Override
    protected void init() {
        super.init();
        
        buttonList.addSingleOptionEntry(
            CyclingOption.create(
                "createWorld.customize.biomeType", 
                BiomeType.values(), 
                (value) -> new TranslatableText("createWorld.customize.biomeType." + value.getName()), 
                (gameOptions) -> { return this.biomeType; }, 
                (gameOptions, option, value) -> {
                    this.biomeType = value;
                    
                    this.consumer.accept(this.biomeType, this.providerSettings);
                })
        );
            
   
        if (this.showOceansOption) {
            buttonList.addSingleOptionEntry(
                CyclingOption.create("createWorld.customize.inf.generateOceans", 
                (gameOptions) -> { return generateOceans; }, 
                (gameOptions, option, value) -> { // Setter
                    this.generateOceans = value;
                    ((InfProviderSettings)this.providerSettings).generateOceans = value;
                    
                    this.consumer.accept(this.biomeType, this.providerSettings);
            }));
        }
    }
}
