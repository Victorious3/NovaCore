package nova.core.wrapper.mc17.wrapper.item;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import nova.core.item.Item;

import java.util.Iterator;

/**
 * Created by Stan on 3/02/2015.
 */
public class WrappedNBTTagCompound extends NBTTagCompound {
	private final Item item;

	public WrappedNBTTagCompound(Item item) {
		this.item = item;
	}

	public Item getItem() {
		return item;
	}

	@Override
	public NBTBase copy() {
		WrappedNBTTagCompound result = new WrappedNBTTagCompound(item);
		Iterator iterator = this.func_150296_c().iterator();

		while (iterator.hasNext()) {
			String s = (String) iterator.next();
			result.setTag(s, getTag(s).copy());
		}

		return result;
	}
}
