package nova.core.recipes.crafting;

import nova.core.entity.component.Player;
import nova.core.item.Item;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.Optional;

/**
 * Represents a crafting grid. Crafting grids contain an item stack in each slot, each of which can be read and possibly
 * modified.
 * @author Stan Hebben
 */
public interface CraftingGrid {
	String topologySquare = "square";
	String typeCrafting = "crafting";

	/**
	 * Represents the player that is currently using this crafting grid.
	 * @return crafting grid player
	 */
	Optional<Player> getPlayer();

	/**
	 * Returns the total size of this crafting grid. For a square crafting grid, this is width x height. Note that the
	 * size can be less than width x height in the case of non-square crafting grid (but never more).
	 * @return The size
	 */
	int size();

	/**
	 * Gets the stack in a specified slot.
	 * @param slot slot index
	 * @return item stack in the given slot
	 */
	Optional<Item> getStack(int slot);

	/**
	 * Modifies the stack in the given slot. If the modification is not possible, this method returns false. If modification
	 * was successful, returns true.
	 *
	 * Slots in a crafting grid should also be ordered with the slots at the y=0 first, starting from smallest x to
	 * largest x and then from smallest y to largest y (natural order). However, not all (x, y) positions need to have
	 * a corresponding slot.
	 * @param slot slot to be modified
	 * @param Item item stack to be set
	 * @return true if modification was successful, false otherwise
	 */
	boolean setStack(int slot, Optional<Item> Item);

	/**
	 * Gets the width of the crafting grid. For a non-square grid,, this should return the highest acceptable x-value + 1.
	 * @return crafting grid width
	 */
	int getWidth();

	/**
	 * Gets the height of the crafting grid. For a non-square grid, this should return the highest acceptable y-value + 1.
	 * @return crafting grid height
	 */
	int getHeight();

	/**
	 * Gets the stack at the given (x, y) position. Returns null if there is no stack at that position.
	 * @param x x position
	 * @param y y position
	 * @return stack at the given position
	 */
	Optional<Item> getStack(int x, int y);

	/**
	 * Sets the stack at the given (x, y) position.
	 * @param x x position
	 * @param y y posittion
	 * @param Item stack to be set
	 * @return true if the modification is successful, false otherwise
	 */
	boolean setStack(int x, int y, Optional<Item> Item);

	/**
	 * Gives back a certain item. In the case of a player's crafting grid, this would typically go back to the player's
	 * inventory. Machines may implement this method differently.
	 * @param Item The {@link Item}
	 */
	void giveBack(Item Item);

	/**
	 * Gets the topology of the crafting grid. For a square grid, this should be CraftingGrid.TOPOLOGY_SQUARE. Other
	 * kinds of grids may return a different value.
	 * @return crafting grid topology
	 */
	String getTopology();

	/**
	 * Gets the type of crafting grid. For a crafting recipe, this should return CraftingGrid.TYPE_CRAFTING. Other
	 * machines or crafting tables (with a separate set of recipes) may return a different value.
	 * @return crafting grid type0
	 */
	String getType();

	/**
	 * Counts the number of filled stacks in this crafting grid.
	 * @return number of non-empty stacks in this crafting grid
	 */
	default int countFilledStacks() {
		int filledStacks = 0;
		for (int i = 0; i < size(); i++) {
			if (getStack(i).isPresent()) {
				filledStacks++;
			}
		}
		return filledStacks;
	}

	/**
	 * Gets the first non-empty item in this crafting grid. Returns empty if and only if the crafting grid is completely
	 * empty.
	 * @return first non-empty item
	 */
	default Optional<Item> getFirstNonEmptyItem() {
		for (int i = 0; i < size(); i++) {
			Optional<Item> stackInSlot = getStack(i);
			if (stackInSlot.isPresent()) {
				return stackInSlot;
			}
		}

		return Optional.empty();
	}

	/**
	 * Finds the position of the first non-empty stack in this crafting grid. Returns empty if and only if the crafting
	 * grid is completely empty.
	 * @return first non-empty item position
	 */
	default Optional<Vector2D> getFirstNonEmptyPosition() {
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				if (getStack(x, y).isPresent()) {
					return Optional.of(new Vector2D(x, y));
				}
			}
		}

		return Optional.empty();
	}
}
