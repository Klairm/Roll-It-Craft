package blockentity;

import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import gui.DrugMachineMenu;
import init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.BlastFurnaceMenu;
import net.minecraft.world.inventory.BrewingStandMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.FurnaceMenu;
import net.minecraft.world.inventory.SmokerMenu;
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
import recipe.DrugMachineRecipe;
import recipe.DryingRackRecipe;

public class DrugMachineEntity extends BlockEntity implements EntityBlock, MenuProvider {

	private boolean isActive = true;
	private final int size = 1;
	private int timer = 0;
	private int time = 0;
	private int processTime = 5;

	protected final ContainerData data;
	private LazyOptional<IItemHandler> lazyItemhandler = LazyOptional.empty();
	private final ItemStackHandler inventory = new ItemStackHandler(2) {
		@Override
		protected void onContentsChanged(int slot) {
			setChanged();
		}

		@Override
		public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {

			return super.extractItem(slot, amount, simulate);
		}

		@Override
		public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {

			return super.insertItem(slot, stack, simulate);
		}
	};

	public DrugMachineEntity(BlockPos pos, BlockState state) {
		super(BlockEntityInit.DRUG_MACHINE.get(), pos, state);
		this.data = new ContainerData() {
			@Override
			public int get(int index) {
				return switch (index) {
				case 0 -> DrugMachineEntity.this.timer;
				case 1 -> DrugMachineEntity.this.processTime;
				default -> 0;
				};
			}

			@Override
			public void set(int index, int value) {
				switch (index) {
				case 0 -> DrugMachineEntity.this.timer = value;
				case 1 -> DrugMachineEntity.this.processTime = value;
				}
			}

			@Override
			public int getCount() {
				return 2;
			}
		};

	}

	
	public ItemStackHandler getInventory() {
		return this.inventory;
	}
	protected Component getDefaultName() {
		return Component.translatable("container.furnace");
	}

	public LazyOptional<IItemHandler> getLazyItemhandler() {
		return this.lazyItemhandler;
	}

	private static boolean hasRecipe(DrugMachineEntity entity) {
		SimpleContainer tempInv = new SimpleContainer(entity.inventory.getSlots());

		tempInv.setItem(0, entity.inventory.getStackInSlot(0));

		Optional<DrugMachineRecipe> recipe = entity.level.getRecipeManager()
				.getRecipeFor(DrugMachineRecipe.Type.INSTANCE, tempInv, entity.level);

		return recipe.isPresent();

	}

	public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T be) {

		if (level.isClientSide()) {
			return;
		}
		DrugMachineEntity drugMachineEntity = (DrugMachineEntity) be;
		SimpleContainer inventory = new SimpleContainer(drugMachineEntity.inventory.getSlots());
		if (hasRecipe(drugMachineEntity)) {



			
			inventory.setItem(0, drugMachineEntity.inventory.getStackInSlot(0));

			Optional<DrugMachineRecipe> recipe = drugMachineEntity.level.getRecipeManager()
					.getRecipeFor(DrugMachineRecipe.Type.INSTANCE, inventory, drugMachineEntity.level);
			drugMachineEntity.timer++;
			if (drugMachineEntity.timer > 20) {
				drugMachineEntity.timer = 0;

				drugMachineEntity.setTime(1);
				if (drugMachineEntity.time >= drugMachineEntity.processTime) {
					drugMachineEntity.inventory.extractItem(0, 1, false);

					drugMachineEntity.inventory.insertItem(1, new ItemStack(recipe.get().getResultItem().getItem()),
							false);

					drugMachineEntity.setTime(0);
					drugMachineEntity.updateEntity();
					drugMachineEntity.setActive();

				}

			}
		}

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
	public void onLoad() {
		super.onLoad();
		lazyItemhandler = LazyOptional.of(() -> inventory);
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

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {

		return BlockEntityInit.DRYING_RACK.get().create(pos, state);
	}

	@Override
	public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {

		return new DrugMachineMenu(pContainerId, pPlayerInventory, this, this.data);

	}

	@Override
	public Component getDisplayName() {

		return Component.translatable("Drug Machine");
	}

}
