package nova.core.wrapper.mc17.wrapper.item;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import nova.core.component.renderer.ItemRenderer;
import nova.core.item.Item;
import nova.core.render.model.CustomModel;
import nova.core.retention.Storable;
import nova.core.wrapper.mc17.wrapper.render.BWClientRenderManager;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.DoubleBuffer;
import java.util.Arrays;

/**
 * @author Stan
 * @since 3/02/2015.
 */
public class BWItem extends Item implements Storable {
	private final net.minecraft.item.Item item;
	private final int meta;
	private final NBTTagCompound tag;

	private final String id;

	EntityItem fakeEntity = new EntityItem(null, 0, 0, 0, makeItemStack(count()));

	public BWItem(ItemStack itemStack) {
		this(itemStack.getItem(), itemStack.getHasSubtypes() ? itemStack.getItemDamage() : 0, itemStack.getTagCompound());
	}

	public BWItem(net.minecraft.item.Item item, int meta, NBTTagCompound tag) {
		this.item = item;
		this.meta = meta;
		this.tag = tag;

		id = net.minecraft.item.Item.itemRegistry.getNameForObject(item) + (item.getHasSubtypes() ? ":" + meta : "");

		add(new ItemRenderer())
			.onRender(model -> {
					model.addChild(new CustomModel(self -> {
						Tessellator.instance.draw();
						GL11.glPushMatrix();
						DoubleBuffer buffer = BufferUtils.createDoubleBuffer(4 * 4);
						double[] flatArray = Arrays.stream(self.matrix.getMatrix().getData())
							.flatMapToDouble(Arrays::stream)
							.toArray();
						buffer.put(flatArray);
						buffer.position(0);
						GL11.glMultMatrix(buffer);
						BWClientRenderManager.renderItem.doRender(fakeEntity, 0, 0, 0, 0, 0);
						GL11.glPopMatrix();
						Tessellator.instance.startDrawingQuads();
					}));
				}
			);
	}

	public net.minecraft.item.Item getItem() {
		return item;
	}

	public int getMeta() {
		return meta;
	}

	public NBTTagCompound getTag() {
		return tag;
	}

	public net.minecraft.item.ItemStack makeItemStack(int stackSize) {
		ItemStack result = new ItemStack(item, stackSize, meta);
		if (tag != null) {
			result.setTagCompound(tag);
		}
		return result;
	}

	@Override
	public String getID() {
		return id;
	}

	@Override
	public String toString() {
		return getID();
	}
}
