package tan.core;

import net.minecraft.block.Block;
import tan.api.temperature.TemperatureRegistry;
import tan.temperaturemodifiers.TemperaturePlayerStateModifier;
import tan.temperaturemodifiers.TemperatureSourceModifier;
import tan.temperaturemodifiers.TemperatureTimeModifier;

public class TANTemperature
{
    public static void init()
    {
        registerTemperatureModifiers();
        registerTemperatureSources();
    }
    
    private static void registerTemperatureModifiers()
    {
        TemperatureRegistry.registerTemperatureModifier(new TemperatureSourceModifier());
        TemperatureRegistry.registerTemperatureModifier(new TemperatureTimeModifier());
        TemperatureRegistry.registerTemperatureModifier(new TemperaturePlayerStateModifier());
    }
    
    private static void registerTemperatureSources()
    {
        TemperatureRegistry.registerTemperatureSource(Block.fire.blockID, -1, 4F, 0.08F);
        TemperatureRegistry.registerTemperatureSource(Block.torchWood.blockID, -1, 1F, 0.02F);
        TemperatureRegistry.registerTemperatureSource(Block.lavaMoving.blockID, -1, 5F, 0.14F);
        TemperatureRegistry.registerTemperatureSource(Block.lavaStill.blockID, -1, 5F, 0.14F);
        TemperatureRegistry.registerTemperatureSource(Block.waterMoving.blockID, -1, -0.5F, 0.03F);
        TemperatureRegistry.registerTemperatureSource(Block.waterStill.blockID, -1, -0.5F, 0.03F);
        TemperatureRegistry.registerTemperatureSource(Block.ice.blockID, 0, -1F, 0.05F);
    }
}
