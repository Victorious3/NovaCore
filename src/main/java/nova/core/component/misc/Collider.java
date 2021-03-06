package nova.core.component.misc;

/**
 * @author Calclavia
 */

import nova.core.component.Component;
import nova.core.component.ComponentProvider;
import nova.core.entity.Entity;
import nova.core.event.Event;
import nova.core.event.EventListener;
import nova.core.util.shape.Cuboid;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public class Collider extends Component {
	public final ComponentProvider provider;

	/**
	 * A general cuboid that represents the bounds of this object.
	 */
	public Supplier<Cuboid> boundingBox = () -> new Cuboid(new Vector3D(0, 0, 0), new Vector3D(1, 1, 1));

	/**
	 * The boxes that provide occlusion to the specific block.
	 */
	public Function<Optional<Entity>, Set<Cuboid>> occlusionBoxes = opEnt -> Collections.singleton(boundingBox.get());

	public Function<Optional<Entity>, Set<Cuboid>> selectionBoxes = opEnt -> Collections.singleton(boundingBox.get());

	/**
	 * Called to check if the block is a cube.
	 * Returns true if this block is a cube.
	 */
	public Supplier<Boolean> isCube = () -> true;

	/**
	 * Called to check if the block is an opaque cube.
	 * Returns true if this block is a cube that is opaque.
	 */
	public Supplier<Boolean> isOpaqueCube = isCube;

	public Collider(ComponentProvider provider) {
		this.provider = provider;
	}

	public Collider setBoundingBox(Cuboid boundingBox) {
		return setBoundingBox(() -> boundingBox);
	}

	public Collider setBoundingBox(Supplier<Cuboid> boundingBox) {
		this.boundingBox = boundingBox;
		return this;
	}

	public Collider setOcclusionBoxes(Function<Optional<Entity>, Set<Cuboid>> occlusionBoxes) {
		this.occlusionBoxes = occlusionBoxes;
		return this;
	}

	public Collider setSelectionBoxes(Function<Optional<Entity>, Set<Cuboid>> selectionBoxes) {
		this.selectionBoxes = selectionBoxes;
		return this;
	}

	public Collider isCube(boolean is) {
		isCube = () -> is;
		return this;
	}

	public Collider isOpaqueCube(boolean is) {
		isOpaqueCube = () -> is;
		return this;
	}

	/**
	 * Called when an entity collides with this object. More specifically, when
	 * the entity's block bounds coincides with the bounds of this object.
	 *
	 * Note that a full block can never collide with another entity as the entity
	 * does not go within the block's boundaries.
	 *
	 * Entity - the colliding entity
	 */
	public static class CollideEvent extends Event {
		public final Entity entity;

		public CollideEvent(Entity entity) {
			this.entity = entity;
		}
	}
}
