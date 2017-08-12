package imagicthecat.blockprotectfieldgenerator;

import imagicthecat.blockprotectfieldgenerator.shared.ForgeEventHandler;
import imagicthecat.blockprotectfieldgenerator.shared.block.BlockFieldGenerator;
import imagicthecat.blockprotectfieldgenerator.shared.capability.IStrings;
import imagicthecat.blockprotectfieldgenerator.shared.capability.Strings;
import imagicthecat.blockprotectfieldgenerator.shared.capability.StringsStorage;
import imagicthecat.blockprotectfieldgenerator.shared.tileentity.TileFieldGenerator;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = BlockProtectFieldGenerator.MODID, version = BlockProtectFieldGenerator.VERSION)
public class BlockProtectFieldGenerator
{
  public static final String MODID = "blockprotectfieldgenerator";
  public static final String VERSION = "1.0";
  
  public static Block block_generator;
  
  public void regBlock(String idname, Block block)
  {
  	GameRegistry.registerBlock(block_generator, "blockprotectfieldgenerator:generator");
  	
    if(FMLCommonHandler.instance().getSide() == Side.CLIENT){
	  	Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
	  	.register(Item.getItemFromBlock(block), 0, new ModelResourceLocation(idname, "inventory"));
    }
  }

  @EventHandler
  public void init(FMLInitializationEvent event)
  {
    MinecraftForge.EVENT_BUS.register(new ForgeEventHandler());
    //CapabilityManager.INSTANCE.register(IStrings.class, new StringsStorage(), Strings.class);
    
   
  	block_generator = new BlockFieldGenerator();
  	regBlock("blockprotectfieldgenerator:generator", block_generator);
  	GameRegistry.registerTileEntity(TileFieldGenerator.class, "blockprotectfieldgenerator:generator");
  	
		GameRegistry.addRecipe(new ItemStack(block_generator),
		  "A A",
		  " B ",
		  "A A",
		  'A', Blocks.obsidian, 'B', Items.redstone);
  }
  
  @EventHandler
  public void preInit(FMLPreInitializationEvent event)
  {
  }
}
