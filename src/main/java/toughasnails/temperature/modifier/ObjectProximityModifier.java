package toughasnails.temperature.modifier;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import toughasnails.api.TANBlocks;
import toughasnails.api.temperature.Temperature;
import toughasnails.block.BlockTANCampfire;
import toughasnails.season.SeasonTime;
import toughasnails.temperature.TemperatureDebugger;
import toughasnails.temperature.TemperatureDebugger.Modifier;

public class ObjectProximityModifier extends TemperatureModifier
{
    public ObjectProximityModifier(TemperatureDebugger debugger)
    {
        super(debugger);
    }

    @Override
    public int modifyChangeRate(World world, EntityPlayer player, int changeRate)
    {
        int newChangeRate = changeRate;
        BlockPos playerPos = player.getPosition();
        
        int tempSourceBlocks = 0;
        
        //System.out.println(new Calendar(world).getSubSeason());
        
        for (int x = -2; x <= 2; x++)
        {
            for (int y = -2; y <= 2; y++)
            {
                for (int z = -2; z <= 2; z++)
                {
                    BlockPos pos = playerPos.add(x, y - 1, z);
                    IBlockState state = world.getBlockState(pos);

                    if (getBlockTemperature(player, state) != 0.0F) tempSourceBlocks++;
                }
            }
        }
        
        debugger.start(Modifier.NEARBY_BLOCKS_RATE, newChangeRate);
        newChangeRate -= tempSourceBlocks * 20;
        debugger.end(newChangeRate);
        
        return newChangeRate;
    }

    @Override
    public Temperature modifyTarget(World world, EntityPlayer player, Temperature temperature)
    {
        int temperatureLevel = temperature.getRawValue();
        int newTemperatureLevel = temperatureLevel;
        BlockPos playerPos = player.getPosition();
        
        float blockTemperatureModifier = 0.0F;
        
        for (int x = -1; x <= 1; x++)
        {
            for (int y = -1; y <= 1; y++)
            {
                for (int z = -1; z <= 1; z++)
                {
                    BlockPos pos = playerPos.add(x, y - 1, z);
                    IBlockState state = world.getBlockState(pos);

                    blockTemperatureModifier += getBlockTemperature(player, state);
                }
            }
        }
        
        debugger.start(Modifier.NEARBY_BLOCKS_TARGET, newTemperatureLevel);
        newTemperatureLevel += blockTemperatureModifier;
        debugger.end(newTemperatureLevel);
        
        return new Temperature(newTemperatureLevel);
    }

    public static float getBlockTemperature(EntityPlayer player, IBlockState state)
    {
        World world = player.worldObj;
        Material material = state.getMaterial();
        BiomeGenBase biome = world.getBiomeGenForCoords(player.getPosition());
        
        if (state.getBlock() == TANBlocks.campfire)
        {
        	if (state.getValue(BlockTANCampfire.BURNING) == true)
        	{
        		return 12.0F;
        	}
        	else
        	{
        		return 0.0F;
        	}
        }
        
        if (material == Material.fire)
        {
            return 1.0F;
        }
        else if (material == Material.lava)
        {
            return 1.5F;
        }
        
        return 0.0F;
    }
}
