package imagicthecat.blockprotectfieldgenerator.shared;

import imagicthecat.blockprotectfieldgenerator.BlockProtectFieldGenerator;
import imagicthecat.blockprotectfieldgenerator.shared.tileentity.TileFieldGenerator;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class Tools {
	
	//search blocks by block in a cube radius
	public static List<BlockPos> findBlocks(World world, Block block, BlockPos pos, int radius)
	{
		List<BlockPos> list = new ArrayList<BlockPos>();
		
		for(int i = -radius; i <= radius; i++){
			for(int j = -radius; j <= radius; j++){
				for(int k = -radius; k <= radius; k++){
					BlockPos bpos = new BlockPos(pos.getX()+i,pos.getY()+j,pos.getZ()+k);
					
					if(world.getBlockState(bpos).getBlock() == block)
						list.add(bpos);
				}
			}
		}
		
		return list;
	}
	
	//check if the area at this pos is protected and if the player is allowed or not
	// return Pair(protected, allowed)
	public static Pair<Boolean, Boolean> checkArea(EntityPlayer player, BlockPos pos)
	{
		boolean _protected = false;
		boolean allowed = true;
		
		List<BlockPos> blocks = findBlocks(player.worldObj, BlockProtectFieldGenerator.block_generator, pos, 8);
		if(!blocks.isEmpty()){
			_protected = true;
			
			for(BlockPos bpos : blocks){
				//check if a not allowed generator is present
				TileFieldGenerator tile = (TileFieldGenerator) player.worldObj.getTileEntity(bpos);
				if(tile != null && !tile.isAllowed(player.getName())){
					allowed = false;
					break;
				}
			}
		}
		
		return new Pair<Boolean, Boolean>(_protected, allowed);
	}
}
