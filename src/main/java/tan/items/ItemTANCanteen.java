package tan.items;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.ItemFluidContainer;
import tan.ToughAsNails;
import tan.api.thirst.IDrinkable;
import tan.api.utils.TANPlayerStatUtils;
import tan.stats.ThirstStat;

public class ItemTANCanteen extends ItemFluidContainer
{
    public ItemTANCanteen(int id)
    {
        super(id);
        this.maxStackSize = 1;
        this.capacity = 200;  /*FluidContainerRegistry.BUCKET_VOLUME / 5*/
        this.setMaxDamage(4);
        this.setCreativeTab(ToughAsNails.tabToughAsNails);
    }
    
    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitVecX, float hitVecY, float hitVecZ)
    {
        return false;
    }
    
    @Override
    public ItemStack onEaten(ItemStack itemStack, World world, EntityPlayer player)
    {
        if (!player.capabilities.isCreativeMode)
        {
            drain(itemStack, 50, true);
            itemStack.damageItem(1, player);
        }

        if (itemStack.getItemDamage() >= itemStack.getMaxDamage())
        {
            itemStack.setItemDamage(0);
        }

        if (!world.isRemote)
        {
            ThirstStat thirstStat = TANPlayerStatUtils.getPlayerStat(player, ThirstStat.class);
            
            thirstStat.addThirst(5);
            
            TANPlayerStatUtils.setPlayerStat(player, thirstStat);
        }

        return itemStack;
    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
    {
        MovingObjectPosition pos = this.getMovingObjectPositionFromPlayer(world, player, true);

        FluidStack fluid = getFluid(itemStack);
        
        if (fluid == null || fluid.amount != capacity)
        {
            if (pos != null)
            {
                int x = pos.blockX;
                int y = pos.blockY;
                int z = pos.blockZ;

                Fluid blockFluid = FluidRegistry.lookupFluidForBlock(Block.blocksList[world.getBlockId(x, y, z)]);
                
                if (blockFluid != null)
                {
                    if (blockFluid == FluidRegistry.WATER || blockFluid instanceof IDrinkable)
                    {
                        this.fill(itemStack, new FluidStack(blockFluid, capacity), true);
                        itemStack.setItemDamage(0);
                        world.setBlockToAir(x, y, z);
                        
                        return itemStack;
                    }
                }
            }
        }
        
        if (fluid.amount != 0)
        {
            player.setItemInUse(itemStack, this.getMaxItemUseDuration(itemStack));
        }

        return itemStack;
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List stringList, boolean showAdvancedInfo) 
    {
        FluidStack fluid = getFluid(itemStack);
        
        if (fluid != null && fluid.amount > 0)
        {   
            stringList.add(fluid.getFluid().getLocalizedName());
        }
    }
    
    @Override
    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
    	return EnumAction.drink;
    }
    
    @Override
    public int getMaxItemUseDuration(ItemStack par1ItemStack)
    {	
        return 48;
    }

    @Override
    public void registerIcons(IconRegister iconRegister)
    {
        itemIcon = iconRegister.registerIcon("toughasnails:canteenfull");
    }
}