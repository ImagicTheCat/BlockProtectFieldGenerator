package imagicthecat.blockprotectfieldgenerator.shared.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class StringsStorage implements IStorage<IStrings> {

	@Override
	public NBTBase writeNBT(Capability<IStrings> capability, IStrings instance,
			EnumFacing side) 
	{
		NBTTagCompound tag = new NBTTagCompound();
		
		//write strings
		int size = instance.size();
		tag.setInteger("s", size);
		for(int i = 0; i < size; i++)
			tag.setString(i+"", instance.get(i));
		
		return tag;
	}

	@Override
	public void readNBT(Capability<IStrings> capability, IStrings instance,
			EnumFacing side, NBTBase nbt) 
	{
		NBTTagCompound tag = (NBTTagCompound)nbt;
		
		//load strings
		instance.clear();
		int size = tag.getInteger("s");
		for(int i = 0; i < size; i++)
			instance.add(tag.getString(i+""));
	}

}
