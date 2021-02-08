package com.bespectacled.modernbeta.gui;

import java.util.function.BiConsumer;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.biome.BiomeType;
import com.bespectacled.modernbeta.gen.provider.settings.ChunkProviderSettings;
import com.bespectacled.modernbeta.gen.provider.settings.InfOldProviderSettings;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.option.CyclingOption;

public class InfdevOldCustomizeLevelScreen extends InfCustomizeLevelScreen {
    private boolean generateInfdevPyramid;
    private boolean generateInfdevWall;
    
    public InfdevOldCustomizeLevelScreen(
        CreateWorldScreen parent, 
        String title, 
        InfOldProviderSettings providerSettings,
        BiomeType biomeType, 
        boolean showOceansOption,
        BiConsumer<BiomeType, ChunkProviderSettings> consumer
    ) {
        super(parent, title, providerSettings, biomeType, showOceansOption, consumer);
        
        this.generateInfdevPyramid = ModernBeta.BETA_CONFIG.generateInfdevPyramid;
        this.generateInfdevWall = ModernBeta.BETA_CONFIG.generateInfdevWall;
    }
    
    @Override
    protected void init() {
        super.init();
        
        this.buttonList.addSingleOptionEntry(
           CyclingOption.create("createWorld.customize.infdev.generateInfdevPyramid", 
               (gameOptions) -> { return generateInfdevPyramid; }, 
               (gameOptions, option, value) -> { // Setter
                   this.generateInfdevPyramid = value;
                   ((InfOldProviderSettings)this.providerSettings).generateInfdevPyramid = this.generateInfdevPyramid;
                   
                   this.consumer.accept(this.biomeType, providerSettings);
       }));
       
       this.buttonList.addSingleOptionEntry(
           CyclingOption.create("createWorld.customize.infdev.generateInfdevWall", 
               (gameOptions) -> { return generateInfdevWall; }, 
               (gameOptions, option, value) -> { // Setter
                   this.generateInfdevWall = value;
                   ((InfOldProviderSettings)this.providerSettings).generateInfdevWall = this.generateInfdevWall;
                   
                   this.consumer.accept(this.biomeType, providerSettings);
       }));
    }
}
