package imagicthecat.blockprotectfieldgenerator.shared;

import imagicthecat.blockprotectfieldgenerator.BlockProtectFieldGenerator;
import imagicthecat.blockprotectfieldgenerator.shared.tileentity.TileFieldGenerator;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class Command implements ICommand {

	@Override
	public int compareTo(ICommand arg0) {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "bpfg";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "bpfg allowme";
	}

	@Override
	public List<String> getCommandAliases() {
		return new ArrayList<String>();
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		World world = sender.getEntityWorld();
		if(!world.isRemote){
			EntityPlayerMP player = (EntityPlayerMP)sender.getCommandSenderEntity();
			if(player != null){
				if(args.length >= 1 && args[0].equals("allowme")){
					String name = player.getName();
					
					int count = 0;
					for(BlockPos bp : Tools.findBlocks(world, BlockProtectFieldGenerator.block_generator, sender.getPosition(), 8)){
						TileFieldGenerator tile = (TileFieldGenerator)world.getTileEntity(bp);
						tile.allow(name);
						count++;
					}
					
					player.addChatMessage(new ChatComponentText("Allowed on "+count+" generators."));
				}
				else
					player.addChatMessage(new ChatComponentText("Unknown subcommand."));
			}
		}
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		return (sender.getCommandSenderEntity() != null 
				&& FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().canSendCommands(((EntityPlayerMP)sender.getCommandSenderEntity()).getGameProfile()));
	}

	@Override
	public List<String> addTabCompletionOptions(ICommandSender sender,
			String[] args, BlockPos pos) {
		// TODO Auto-generated method stub
		return new ArrayList<String>();
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		// TODO Auto-generated method stub
		return false;
	}

}
