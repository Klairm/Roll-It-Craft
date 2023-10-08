
package blockentity;

import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import init.BlockEntityInit;
import init.ItemInit;
import net.minecraft.core.BlockPos;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;

import net.minecraft.world.level.block.state.BlockState;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import recipe.DryingRackRecipe;

public class DryingRackBlockEntity extends BlockEntity implements EntityBlock {

	boolean isActive = false;
	protected final int size = 1;
	int timer = 0;
	int time = 0;
	private final int processTime = 300;

	public final ItemStackHandler inventory;

	private LazyOptional<IItemHandler> lazyItemhandler = LazyOptional.empty();
	private ItemStackHandler itemHandler;

	public DryingRackBlockEntity(BlockPos pos, BlockState state) {
		super(BlockEntityInit.DRYING_RACK.get(), pos, state);

		this.inventory = this.createInventory();

	}

	public LazyOptional<IItemHandler> getLazyItemhandler() {
		return this.lazyItemhandler;
	}

	public ItemStack getRenderStack() {
		return this.inventory.getStackInSlot(0);
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

	private static boolean hasRecipe(DryingRackBlockEntity entity) {
		SimpleContainer tempInv = new SimpleContainer(entity.inventory.getSlots());

		tempInv.setItem(0, entity.inventory.getStackInSlot(0));

		Optional<DryingRackRecipe> recipe = entity.level.getRecipeManager().getRecipeFor(DryingRackRecipe.Type.INSTANCE,
				tempInv, entity.level);

		System.out.println(recipe.isPresent());
		return recipe.isPresent();

	}

	public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T be) {
		if (level.isClientSide()) {
			return;
		}
		DryingRackBlockEntity dryingEntity = (DryingRackBlockEntity) be;
		if (hasRecipe(dryingEntity) && dryingEntity.isActive) {

			// TODO: Make craft method to clean code.
			SimpleContainer inventory = new SimpleContainer(dryingEntity.inventory.getSlots());

			inventory.setItem(0, dryingEntity.inventory.getStackInSlot(0));

			Optional<DryingRackRecipe> recipe = dryingEntity.level.getRecipeManager()
					.getRecipeFor(DryingRackRecipe.Type.INSTANCE, inventory, dryingEntity.level);
			dryingEntity.timer++;
			if (dryingEntity.timer > 20) {
				dryingEntity.timer = 0;

				dryingEntity.setTime(1);
				if (dryingEntity.time >= dryingEntity.processTime) {
					dryingEntity.inventory.extractItem(0, 1, false);

					dryingEntity.inventory.insertItem(0, new ItemStack(recipe.get().getResultItem().getItem()), false);

					dryingEntity.setTime(0);
					dryingEntity.updateEntity();
					dryingEntity.setActive();

				}

			}
		}

	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {

		return BlockEntityInit.DRYING_RACK.get().create(pos, state);
	}

	@Override
	public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap) {

		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return lazyItemhandler.cast();
		}
		return super.getCapability(cap);
	}

	@Override
	protected void saveAdditional(CompoundTag nbt) {
		super.saveAdditional(nbt);

		nbt.putBoolean("active", this.isActive);
		nbt.put("item", this.inventory.getStackInSlot(0).serializeNBT());
		nbt.putInt("timeProcessed", this.time);

	}

	@Override
	public void load(CompoundTag nbt) {

		super.load(nbt);
		this.isActive = nbt.getBoolean("active");
		this.time = nbt.getInt("timeProcessed");
		if (nbt.contains("item")) {
			CompoundTag itemTag = nbt.getCompound("item");
			this.inventory.setStackInSlot(0, ItemStack.of(itemTag));
		}

	}

	public void setTime(int time) {
		if (time == 0) {
			this.time = time;
		} else {
			this.time += time;
		}

	}

	public void setActive() {
		this.isActive = !this.isActive;
		this.setChanged();
	}

	public boolean getActive() {
		return this.isActive;
	}

	public void updateEntity() {

		this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
		this.setChanged();

	}

	public void dropItemStack(Level pLevel, double pX, double pY, double pZ, ItemStack pStack) {
		double d0 = (double) EntityType.ITEM.getWidth();
		double d1 = 1.0D - d0;
		double d2 = d0 / 2.0D;
		double d3 = Math.floor(pX) + pLevel.random.nextDouble() * d1 + d2;
		double d4 = Math.floor(pY) + pLevel.random.nextDouble() * d1;
		double d5 = Math.floor(pZ) + pLevel.random.nextDouble() * d1 + d2;

		while (!pStack.isEmpty()) {
			ItemEntity itementity = new ItemEntity(pLevel, d3, d4, d5, pStack.split(pLevel.random.nextInt(21) + 10));
			float f = 0.05F;
			itementity.setDeltaMovement(pLevel.random.triangle(0.0D, 0.11485000171139836D),
					pLevel.random.triangle(0.2D, 0.11485000171139836D),
					pLevel.random.triangle(0.0D, 0.11485000171139836D));
			pLevel.addFreshEntity(itementity);
		}
	}

	@Override
	public CompoundTag getUpdateTag() {

		CompoundTag tag = super.getUpdateTag();

		tag.put("item", this.inventory.getStackInSlot(0).serializeNBT());

		return tag;

	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {

		super.onDataPacket(net, pkt);
		this.load(pkt.getTag());

	}

	@Override
	public void handleUpdateTag(CompoundTag tag) {

		super.handleUpdateTag(tag);

		if (tag.contains("item")) {
			CompoundTag itemTag = tag.getCompound("item");
			this.inventory.getStackInSlot(0).deserializeNBT(itemTag);
		}

	}

	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		// Will get tag from #getUpdateTag
		return ClientboundBlockEntityDataPacket.create(this);
	}

}
