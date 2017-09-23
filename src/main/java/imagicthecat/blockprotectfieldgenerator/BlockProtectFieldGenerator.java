package imagicthecat.blockprotectfieldgenerator;

import imagicthecat.blockprotectfieldgenerator.shared.Command;
import imagicthecat.blockprotectfieldgenerator.shared.ForgeEventHandler;
import imagicthecat.blockprotectfieldgenerator.shared.block.BlockFieldGenerator;
import imagicthecat.blockprotectfieldgenerator.shared.capability.IStrings;
import imagicthecat.blockprotectfieldgenerator.shared.capability.Strings;
import imagicthecat.blockprotectfieldgenerator.shared.capability.StringsStorage;
import imagicthecat.blockprotectfieldgenerator.shared.tileentity.TileFieldGenerator;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = BlockProtectFieldGenerator.MODID, version = BlockProtectFieldGenerator.VERSION)
public class BlockProtectFieldGenerator
{
  public static final String MODID = "blockprotectfieldgenerator";
  public static final String VERSION = "1.1";
  
  public static Block block_generator;
  public static ResourceLocation tex_indicator;
  
  @SidedProxy(clientSide="imagicthecat.blockprotectfieldgenerator.client.ClientEventHandler", serverSide="imagicthecat.blockprotectfieldgenerator.server.ServerEventHandler")
  public static ForgeEventHandler event_handler;
  
  @Instance(BlockProtectFieldGenerator.MODID)
  public static BlockProtectFieldGenerator instance;
  
  @EventHandler
  public void preInit(FMLPreInitializationEvent event)
  {
   	block_generator = new BlockFieldGenerator();
   	GameRegistry.registerBlock(block_generator, "blockprotectfieldgenerator:generator");
   	GameRegistry.registerTileEntity(TileFieldGenerator.class, "blockprotectfieldgenerator:generator");
  }

  @EventHandler
  public void init(FMLInitializationEvent event)
  {
    MinecraftForge.EVENT_BUS.register(event_handler);
    //CapabilityManager.INSTANCE.register(IStrings.class, new StringsStorage(), Strings.class);
  	
		GameRegistry.addRecipe(new ItemStack(block_generator),
		  "A A",
		  " B ",
		  "A A",
		  'A', Blocks.OBSIDIAN, 'B', Items.REDSTONE
		);
		
    if(FMLCommonHandler.instance().getSide() == Side.CLIENT){
	  	Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
	  	.register(Item.getItemFromBlock(block_generator), 0, new ModelResourceLocation("blockprotectfieldgenerator:generator", "inventory"));
	  	tex_indicator = new ResourceLocation("blockprotectfieldgenerator", "textures/gui/indicator.png");
    }
  }
  
  @EventHandler
  public void serverLoad(FMLServerStartingEvent event){
  	event.registerServerCommand(new Command());
  }
}
