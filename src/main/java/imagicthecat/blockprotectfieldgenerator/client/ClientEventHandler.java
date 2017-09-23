package imagicthecat.blockprotectfieldgenerator.client;

import imagicthecat.blockprotectfieldgenerator.BlockProtectFieldGenerator;
import imagicthecat.blockprotectfieldgenerator.shared.ForgeEventHandler;
import imagicthecat.blockprotectfieldgenerator.shared.Pair;
import imagicthecat.blockprotectfieldgenerator.shared.Tools;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientEventHandler extends ForgeEventHandler{
	long last_time;
	Pair<Boolean, Boolean> info;
	
	public ClientEventHandler()
	{
		last_time = System.nanoTime();
		info = new Pair<Boolean, Boolean>(false, true);
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
		EntityPlayer player = mc.thePlayer;
		if(evt.phase == TickEvent.Phase.END && player!= null){
			
			long new_time = System.nanoTime();
			if(new_time-last_time >= 750000000){ //check area every 0.75 seconds for optimization
				info = Tools.checkArea(player, mc.thePlayer.getPosition());
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
			  Gui.drawModalRectWithCustomSizedTexture(res.getScaledWidth()*2-40, res.getScaledHeight()*2-40, 0, 0, 32, 32, 32, 32);
			  GlStateManager.popAttrib();
			  GlStateManager.popMatrix();
			}
		}
	}
}
