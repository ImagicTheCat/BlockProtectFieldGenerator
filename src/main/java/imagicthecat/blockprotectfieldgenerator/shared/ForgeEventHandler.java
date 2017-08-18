package imagicthecat.blockprotectfieldgenerator.shared;

import java.util.List;

import imagicthecat.blockprotectfieldgenerator.BlockProtectFieldGenerator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
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
		if(pos != null && 
				!Tools.checkArea(evt.player, pos, (evt.placedBlock.getBlock() == BlockProtectFieldGenerator.block_generator ? 16 : 8), pos).second){
			evt.setCanceled(true);
			
			if(evt.placedBlock.getBlock() == BlockProtectFieldGenerator.block_generator)
				evt.player.addChatMessage(new ChatComponentText("A field generator prevents you from placing another generator here."));
		}
	}
	
	@SubscribeEvent
	public void explosion(ExplosionEvent.Start evt)
	{
		//protected area prevents explosions
		System.out.println(evt.explosion.getPosition());
		if(!Tools.findBlocks(evt.world, BlockProtectFieldGenerator.block_generator, new BlockPos(evt.explosion.getPosition()), 14, true, null).isEmpty())
			evt.setCanceled(true);
	}
	
  public static void drawModalRectWithCustomSizedTexture(int x, int y, float u, float v, int width, int height, float textureWidth, float textureHeight, float z)
  {
      float f = 1.0F / textureWidth;
      float f1 = 1.0F / textureHeight;
      Tessellator tessellator = Tessellator.getInstance();
      WorldRenderer worldrenderer = tessellator.getWorldRenderer();
      worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
      worldrenderer.pos((double)x, (double)(y + height), z).tex((double)(u * f), (double)((v + (float)height) * f1)).endVertex();
      worldrenderer.pos((double)(x + width), (double)(y + height), z).tex((double)((u + (float)width) * f), (double)((v + (float)height) * f1)).endVertex();
      worldrenderer.pos((double)(x + width), (double)y, z).tex((double)((u + (float)width) * f), (double)(v * f1)).endVertex();
      worldrenderer.pos((double)x, (double)y, z).tex((double)(u * f), (double)(v * f1)).endVertex();
      tessellator.draw();
  }
	
  /*
	@SubscribeEvent
	public void onRender(RenderGameOverlayEvent.Post evt)
	{
		GuiScreen screen = Minecraft.getMinecraft().currentScreen;
		if(evt.type == ElementType.HOTBAR && screen != null){
			GlStateManager.color(1, 1, 1, 1);
      
			screen.mc.getTextureManager().bindTexture(BlockProtectFieldGenerator.tex_indicator);
			screen.drawTexturedModalRect(0, 0, 0, 0, 100, 100);
		}
	}
	*/
	
	@SubscribeEvent
	public void onRenderTick(RenderTickEvent evt)
	{
		Minecraft mc = Minecraft.getMinecraft();
		if(evt.phase == TickEvent.Phase.END && mc.thePlayer != null){
			
			long new_time = System.nanoTime();
			if(new_time-last_time >= 750000000){ //check area every 0.75 seconds for optimization
				info = Tools.checkArea(mc.thePlayer, mc.thePlayer.getPosition());
				last_time = new_time;
			}
			
			if(info.first){
				ScaledResolution res = new ScaledResolution(mc);
				
				if(info.second)
					GlStateManager.color(0, 1, 0, 1);
				else
					GlStateManager.color(1, 0, 0, 1);
	      
				mc.getTextureManager().bindTexture(BlockProtectFieldGenerator.tex_indicator);
				GlStateManager.pushMatrix();
				GlStateManager.pushAttrib();
				GlStateManager.enableAlpha();
				GlStateManager.enableBlend();
				GlStateManager.scale(0.5,0.5,0.5);
			  drawModalRectWithCustomSizedTexture(res.getScaledWidth()*2-40, res.getScaledHeight()*2-40, 0, 0, 32, 32, 32, 32, 0);
			  GlStateManager.popAttrib();
			  GlStateManager.popMatrix();
			}
		}
	}
}
