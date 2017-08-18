package imagicthecat.blockprotectfieldgenerator.shared;

import java.util.List;

import imagicthecat.blockprotectfieldgenerator.BlockProtectFieldGenerator;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ForgeEventHandler {
	@SubscribeEvent
	public void playerInteract(PlayerInteractEvent evt)
	{
			BlockPos pos = evt.pos;
			if(pos == null && evt.entity != null)
				pos = evt.entity.getPosition();
			
			//if position valid, target not a generator and not in an allowed area -> prevent interactions
			if(pos != null && evt.world.getBlockState(pos).getBlock() != BlockProtectFieldGenerator.block_generator && 
					!Tools.checkArea(evt.entityPlayer, pos).second){
				evt.setCanceled(true);
				
				if(evt.world.isRemote)
					evt.entityPlayer.addChatMessage(new ChatComponentText("A field generator prevents you from interacting here."));
			}
	}
	
	@SubscribeEvent
	public void playerPlace(PlaceEvent evt)
	{
		BlockPos pos = evt.pos;
		
		//if position valid, target not a generator and not in an allowed area -> prevent interactions
		if(pos != null && evt.world.getBlockState(pos).getBlock() != BlockProtectFieldGenerator.block_generator && 
				!Tools.checkArea(evt.player, pos).second){
			evt.setCanceled(true);
			
			if(evt.world.isRemote)
				evt.player.addChatMessage(new ChatComponentText("A field generator prevents you from interacting here."));
		}
	}
	
	@SubscribeEvent
	public void explosion(ExplosionEvent.Start evt)
	{
		//protected area prevents explosions
		System.out.println(evt.explosion.getPosition());
		if(!Tools.findBlocks(evt.world, BlockProtectFieldGenerator.block_generator, new BlockPos(evt.explosion.getPosition()), 14, true).isEmpty())
			evt.setCanceled(true);
	}
}
