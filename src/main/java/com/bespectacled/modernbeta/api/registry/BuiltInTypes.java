package com.bespectacled.modernbeta.api.registry;

public class BuiltInTypes {
    public enum Chunk {
        BETA("beta"),
        SKYLANDS("skylands"),
        ALPHA("alpha"),
        INFDEV_415("infdev"),
        INFDEV_227("infdev_old"),
        INDEV("indev"),
        BETA_ISLANDS("beta_islands")
        ;
        
        public final String name;
        
        private Chunk(String name) { this.name = name; }
    }
    
    public enum ChunkSettings {
        BETA("beta"),
        SKYLANDS("skylands"),
        ALPHA("alpha"),
        INFDEV_415("infdev"),
        INFDEV_227("infdev_old"),
        INDEV("indev"),
        BETA_ISLANDS("beta_islands")
        ;
        
        public final String name;
        
        private ChunkSettings(String name) { this.name = name; }
    }
    
    public enum Biome {
        BETA("beta"),
        SINGLE("single"),
        VANILLA("vanilla");
        
        public final String name;
        
        private Biome(String name) { this.name = name; }
    }
    
    public enum WorldScreen {
        INF("inf"),
        SKYLANDS("skylands"),
        INFDEV_OLD("infdev_old"),
        INDEV("indev"),
        ISLAND("island")
        ;
        
        public final String name;
        
        private WorldScreen(String id) { this.name = id; }
    }
}
