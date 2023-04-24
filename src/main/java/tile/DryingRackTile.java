package tile;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import init.BlockEntityInit;
import init.ItemInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class DryingRackTile extends BlockEntity implements EntityBlock {
	// TODO: Actually use the item capability correctly, override proper methods.

	boolean isActive = false;
	protected final int size = 1;
	int timer = 0;
	int time = 0;

	public final ItemStackHandler inventory;

	private LazyOptional<IItemHandler> lazyItemhandler = LazyOptional.empty();

	public DryingRackTile(BlockPos pos, BlockState state) {
		super(BlockEntityInit.DRYING_RACK.get(), pos, state);

		this.inventory = this.createInventory();

	}

	public LazyOptional<IItemHandler> getLazyItemhandler() {
		return this.lazyItemhandler;
	}

	private ItemStackHandler createInventory() {

		return new ItemStackHandler(this.size) {

			@Override
			public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {

				return super.extractItem(slot, amount, simulate);
			}

			@Override
			public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {

				return super.insertItem(slot, stack, simulate);
			}
		};
	}

	public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T be) {
		DryingRackTile dryingEntity = (DryingRackTile) be;

		if (dryingEntity.isActive && !level.isClientSide()) {
			dryingEntity.timer++;
			if (dryingEntity.timer > 20) { // 1 second is equal to 20 ticks

				dryingEntity.time++;
				if (dryingEntity.time == 60) {
					System.out.println(dryingEntity.inventory.getStackInSlot(0));
					// TODO: Dry the weed after 60 seconds (?)

				}
			}
		}

	}

	@Override
	public void load(CompoundTag nbt) {

		super.load(nbt);
		this.isActive = nbt.getBoolean("active");

	}

	@Override
	protected void saveAdditional(CompoundTag nbt) {
		super.saveAdditional(nbt);

		nbt.putBoolean("active", this.isActive);

	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {

		return BlockEntityInit.DRYING_RACK.get().create(pos, state);
	}

	@Override
	public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
		// TODO Auto-generated method stub
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return lazyItemhandler.cast();
		}
		return super.getCapability(cap);
	}

	public void setActive() {
		this.isActive = !this.isActive;
		this.setChanged();
	}

	public boolean getActive() {
		return this.isActive;
	}

}
