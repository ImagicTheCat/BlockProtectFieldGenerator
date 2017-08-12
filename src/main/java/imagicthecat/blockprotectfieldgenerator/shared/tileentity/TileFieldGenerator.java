package imagicthecat.blockprotectfieldgenerator.shared.tileentity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileFieldGenerator extends TileEntity {
	private Set<String> allowed_users;
	
	public TileFieldGenerator()
	{	
		allowed_users = new HashSet<String>();
	}
	
	public void allow(String user)
	{
		if(!allowed_users.contains(user))
			allowed_users.add(user);
		
		this.worldObj.markBlockForUpdate(this.pos);
		this.markDirty();
	}
	
	public Set<String> getAllowedUsers()
	{
		return allowed_users;
	}
	
	public void clear()
	{
		allowed_users.clear();
		
		this.worldObj.markBlockForUpdate(this.pos);
		this.markDirty();
	}
	
	public boolean isAllowed(String user)
	{
		return allowed_users.contains(user);
	}
	
  @Override
  public void writeToNBT(NBTTagCompound tag) 
  {
  	super.writeToNBT(tag);
  	
		//write users
		int size = allowed_users.size();
		
		tag.setInteger("s", size);
		int i = 0;
		for(String u : allowed_users){
			tag.setString("s"+i, u);
			i++;
		}
  }
  
  @Override
  public void readFromNBT(NBTTagCompound tag)
  {
    super.readFromNBT(tag);
    
		//load users
		allowed_users.clear();
		int size = tag.getInteger("s");
		for(int i = 0; i < size; i++)
			allowed_users.add(tag.getString("s"+i));
  }
  
  @Override
  public Packet getDescriptionPacket() 
  {
    NBTTagCompound tag = new NBTTagCompound();
    writeToNBT(tag);
    return new S35PacketUpdateTileEntity(this.pos, 1, tag);
  }
  
  @Override
  public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) 
  {
    readFromNBT(pkt.getNbtCompound());
  }
}
