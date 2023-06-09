<<<<<<< HEAD
package blockentity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.github.klairm.projectm.ProjectM;
import com.mojang.datafixers.types.templates.Tag;

import init.BlockEntityInit;
import init.ItemInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class DryingRackBlockEntity extends BlockEntity implements EntityBlock {
	// TODO: Actually use the item capability correctly, override proper methods.

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

	public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T be) {
		DryingRackBlockEntity dryingEntity = (DryingRackBlockEntity) be;
		if (!canSurvive(level, pos)) {
			level.destroyBlock(pos, true);
		}
		if (dryingEntity.isActive && !level.isClientSide()) {

			dryingEntity.timer++;
			if (dryingEntity.timer > 20) { // 1 second is equal to 20 ticks
				dryingEntity.timer = 0;

				dryingEntity.setTime(1);

				if (dryingEntity.time >= dryingEntity.processTime) {
					dryingEntity.inventory.extractItem(0, 1, false);
					dryingEntity.inventory.insertItem(0, new ItemStack(ItemInit.DRY_BUD.get()), false);

					dryingEntity.setTime(0);
					dryingEntity.updateEntity();
					dryingEntity.setActive();

				}
			}
		}

	}

	public static boolean canSurvive(Level level, BlockPos pos) {
		Block blockNorth = level.getBlockState(pos.north()).getBlock();
		Block blockSouth = level.getBlockState(pos.south()).getBlock();
		Block blockWest = level.getBlockState(pos.west()).getBlock();
		Block blockEast = level.getBlockState(pos.east()).getBlock();

            return !(blockNorth == Blocks.AIR && blockSouth == Blocks.AIR && blockWest == Blocks.AIR
                    && blockEast == Blocks.AIR);

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
=======
package blockentity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.github.klairm.projectm.ProjectM;
import com.mojang.datafixers.types.templates.Tag;

import init.BlockEntityInit;
import init.ItemInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class DryingRackBlockEntity extends BlockEntity implements EntityBlock {
	// TODO: Actually use the item capability correctly, override proper methods.

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

	public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T be) {
		DryingRackBlockEntity dryingEntity = (DryingRackBlockEntity) be;

		if (dryingEntity.isActive && !level.isClientSide()) {

			dryingEntity.timer++;
			if (dryingEntity.timer > 20) { // 1 second is equal to 20 ticks
				dryingEntity.timer = 0;

				dryingEntity.setTime(1);

				if (dryingEntity.time >= dryingEntity.processTime) {
					dryingEntity.inventory.extractItem(0, 1, false);
					dryingEntity.inventory.insertItem(0, new ItemStack(ItemInit.DRY_BUD.get()), false);
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
>>>>>>> c0e57b9309c4cac74c9e97d86fa50b97356a08f2
