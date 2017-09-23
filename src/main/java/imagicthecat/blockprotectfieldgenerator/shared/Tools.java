package imagicthecat.blockprotectfieldgenerator.shared;

import imagicthecat.blockprotectfieldgenerator.BlockProtectFieldGenerator;
import imagicthecat.blockprotectfieldgenerator.shared.tileentity.TileFieldGenerator;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Tools {
	
	//search blocks by block in a cube radius
	//if lazy is true, will stop the research when one block is found
	//if ignore is not null, this pos will be ignored
	public static List<BlockPos> findBlocks(World world, Block block, BlockPos pos, int radius, boolean lazy, BlockPos ignore)
	{
		List<BlockPos> list = new ArrayList<BlockPos>();
		
		boolean done = false;
		for(int i = -radius; i <= radius && !done; i++){
			for(int j = -radius; j <= radius && !done; j++){
				for(int k = -radius; k <= radius && !done; k++){
					BlockPos bpos = new BlockPos(pos.getX()+i,pos.getY()+j,pos.getZ()+k);
					
					if(world.getBlockState(bpos).getBlock() == block && (ignore == null || !bpos.equals(ignore))){
						list.add(bpos);
						if(lazy)
							done = true;
					}
				}
			}
		}
		
		return list;
	}

	public static List<BlockPos> findBlocks(World world, Block block, BlockPos pos, int radius)
	{
		return findBlocks(world, block, pos, radius, false, null);
	}
	
	//check if a blockpos is in an area
	public static boolean isInArea(BlockPos pos, BlockPos origin, int radius)
	{
		return (Math.abs(pos.getX()-origin.getX()) <= radius
				&& Math.abs(pos.getY()-origin.getY()) <= radius
				&& Math.abs(pos.getZ()-origin.getZ()) <= radius);
	}
	
	//check if the area at this pos is protected and if the player is allowed or not
	// return Pair(protected, allowed)
	public static Pair<Boolean, Boolean> checkArea(EntityPlayer player, BlockPos pos, int radius, BlockPos ignore)
	{
		boolean _protected = false;
		boolean allowed = true;
		
		List<BlockPos> blocks = findBlocks(player.worldObj, BlockProtectFieldGenerator.block_generator, pos, radius, false, ignore);
		if(!blocks.isEmpty()){
			_protected = true;
			
			for(BlockPos bpos : blocks){
				//check if a not allowed generator is present
				TileFieldGenerator tile = (TileFieldGenerator) player.worldObj.getTileEntity(bpos);
				if(!(player.worldObj.isBlockIndirectlyGettingPowered(bpos) > 0) && tile != null && !tile.isAllowed(player.getName())){
					allowed = false;
					break;
				}
			}
		}
		
		return new Pair<Boolean, Boolean>(_protected, allowed);
	}

	public static Pair<Boolean, Boolean> checkArea(EntityPlayer player, BlockPos pos)
	{
		return checkArea(player, pos, 8, null);
	}
}
