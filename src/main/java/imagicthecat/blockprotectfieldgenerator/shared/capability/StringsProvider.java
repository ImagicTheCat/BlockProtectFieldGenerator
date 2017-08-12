package imagicthecat.blockprotectfieldgenerator.shared.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class StringsProvider implements ICapabilitySerializable<NBTBase>{
	@CapabilityInject(IStrings.class)
	public static final Capability<IStrings> STRINGS_CAP = null;
	private IStrings instance = STRINGS_CAP.getDefaultInstance();
	 
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		
		return capability == STRINGS_CAP;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == STRINGS_CAP ? (T) this.instance : null;
	}

	@Override
	public NBTBase serializeNBT() {
	  return STRINGS_CAP.getStorage().writeNBT(STRINGS_CAP, this.instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		 STRINGS_CAP.getStorage().readNBT(STRINGS_CAP, this.instance, null, nbt);
	}

}
