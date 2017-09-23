package imagicthecat.blockprotectfieldgenerator.shared.tileentity;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
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
		
		this.worldObj.notifyBlockUpdate(this.pos, this.worldObj.getBlockState(this.pos), this.worldObj.getBlockState(this.pos), 2);
		this.markDirty();
	}
	
	public Set<String> getAllowedUsers()
	{
		return allowed_users;
	}
	
	public void clear()
	{
		allowed_users.clear();
		
		this.worldObj.notifyBlockUpdate(this.pos, this.worldObj.getBlockState(this.pos), this.worldObj.getBlockState(this.pos), 2);
		this.markDirty();
	}
	
	public boolean isAllowed(String user)
	{
		return allowed_users.contains(user);
	}
	
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tag) 
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
		
		return tag;
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
  public NBTTagCompound getUpdateTag()
  {
  	return writeToNBT(new NBTTagCompound());
  }
  
  @Override
  public void handleUpdateTag(NBTTagCompound tag)
  {
  	readFromNBT(tag);
  }
  
  @Override
  public SPacketUpdateTileEntity getUpdatePacket() 
  {
    return new SPacketUpdateTileEntity(this.pos, 1, writeToNBT(new NBTTagCompound()));
  }
  
  @Override
  public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) 
  {
    readFromNBT(pkt.getNbtCompound());
  }
}
