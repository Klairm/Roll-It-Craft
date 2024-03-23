package blocks;

import blockentity.DrugMachineEntity;
import blockentity.DryingRackEntity;
import blockentity.DrugMachineEntity;
import init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;

public class DrugMachine extends Block implements EntityBlock {
	// This is an temporary machine to process cocaine,
	// TODO: create a dissolver machine and a mixer one
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

	private static VoxelShape SHAPE = Block.box(0.1, 0, 0, 16.1, 14, 16);

	public DrugMachine(Properties pProperties) {

		super(pProperties);

	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand,
			BlockHitResult hit) {
		if (!level.isClientSide()) {
			BlockEntity entity = level.getBlockEntity(pos);
			if (entity instanceof DrugMachineEntity) {
				NetworkHooks.openScreen(((ServerPlayer) player), (DrugMachineEntity) entity, pos);
			} else {
				throw new IllegalStateException("Our Container provider is missing!");
			}
			
		}
		return InteractionResult.sidedSuccess(level.isClientSide());
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext collisionContext) {

		return SHAPE;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);

	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext pContext) {

		return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		//
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {

		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {

		return new DrugMachineEntity(pPos, pState);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
			BlockEntityType<T> type) {

		return type == BlockEntityInit.DRUG_MACHINE.get() ? DrugMachineEntity::tick : null;
	}

	@Override
	public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
		if (!pState.is(pNewState.getBlock())) {
			BlockEntity blockentity = pLevel.getBlockEntity(pPos);
			DrugMachineEntity drugMachineEntity = (DrugMachineEntity) blockentity;

			for(int i = 0; i < drugMachineEntity.getInventory().getSlots(); i++) {
				drugMachineEntity.dropItemStack(pLevel, pPos.getX(), pPos.getY(), pPos.getZ(),
						drugMachineEntity.getInventory().getStackInSlot(i));
			}
			

		}
		super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
	}

}
