package imagicthecat.blockprotectfieldgenerator.shared;

import java.util.List;

import imagicthecat.blockprotectfieldgenerator.BlockProtectFieldGenerator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;

public class ForgeEventHandler {
	long last_time;
	Pair<Boolean, Boolean> info;
	
	public ForgeEventHandler()
	{
		last_time = System.nanoTime();
		info = new Pair<Boolean, Boolean>(false, true);
	}
	
	@SubscribeEvent
	public void playerInteract(PlayerInteractEvent evt)
	{
			BlockPos pos = evt.getPos();
			if(pos == null && evt.getEntity() != null)
				pos = evt.getEntity().getPosition();
			
			//if position valid, target not a generator and not in an allowed area -> prevent interactions
			if(pos != null && evt.getWorld().getBlockState(pos).getBlock() != BlockProtectFieldGenerator.block_generator && 
					!Tools.checkArea(evt.getEntityPlayer(), pos).second){
				evt.setCanceled(true);
				
				if(evt.getWorld().isRemote)
					evt.getEntityPlayer().addChatMessage(new TextComponentString("A field generator prevents you from interacting here."));
			}
	}
	
	@SubscribeEvent
	public void playerPlace(PlaceEvent evt)
	{
		BlockPos pos = evt.getPos();
		
		//if position valid, target not a generator and not in an allowed area -> prevent interactions
		if(pos != null && 
				!Tools.checkArea(evt.getPlayer(), pos, (evt.getPlacedBlock().getBlock() == BlockProtectFieldGenerator.block_generator ? 16 : 8), pos).second){
			evt.setCanceled(true);
			
			if(evt.getPlacedBlock().getBlock() == BlockProtectFieldGenerator.block_generator)
				evt.getPlayer().addChatMessage(new TextComponentString("A field generator prevents you from placing another generator here."));
		}
	}
	
	@SubscribeEvent
	public void explosion(ExplosionEvent.Start evt)
	{
		//protected area prevents explosions
		System.out.println(evt.getExplosion().getPosition());
		if(!Tools.findBlocks(evt.getWorld(), BlockProtectFieldGenerator.block_generator, new BlockPos(evt.getExplosion().getPosition()), 14, true, null).isEmpty())
			evt.setCanceled(true);
	}
}
