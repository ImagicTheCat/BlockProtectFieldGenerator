package imagicthecat.blockprotectfieldgenerator.shared.block;


import javax.annotation.Nullable;

import imagicthecat.blockprotectfieldgenerator.shared.tileentity.TileFieldGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class BlockFieldGenerator extends Block implements ITileEntityProvider{
  public BlockFieldGenerator()
  {
    super(Material.ROCK);
    this.setHardness(1.5f);
    this.setUnlocalizedName("blockfieldgenerator");
    this.setCreativeTab(CreativeTabs.REDSTONE);
  }

	@Override
	public TileEntity createNewTileEntity(World world, int meta) 
	{
		return new TileFieldGenerator();
	}
	

	@Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if(!world.isRemote){ //server
			TileFieldGenerator tile = (TileFieldGenerator)world.getTileEntity(pos);
			
			if(player.isSneaking()){
				//show who is registered on the generator
				
				player.addChatMessage(new TextComponentString("Registered users on this field generator:"));
				for(String u : tile.getAllowedUsers())
					player.addChatMessage(new TextComponentString("- "+u));
			}
			else{
				//allow player to the field generator
				tile.allow(player.getName());
				player.addChatMessage(new TextComponentString("You are now allowed on this field generator."));
			}
		}
		
		return super.onBlockActivated(world, pos, state, player, hand, heldItem, side, hitX, hitY, hitZ);
	}
}
