package blocks;

import blockentity.DryingRackEntity;
import init.BlockEntityInit;

import init.ItemInit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DryingRack extends HorizontalDirectionalBlock implements EntityBlock {
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

	private static VoxelShape SHAPE = Block.box(0, 10, 14, 16, 12, 16);

	public DryingRack(Properties props) {
		super(props);

	}

	public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
		Direction direction = pState.getValue(FACING);
		BlockPos blockpos = pPos.relative(direction.getOpposite());
		BlockState blockstate = pLevel.getBlockState(blockpos);
		return blockstate.isFaceSturdy(pLevel, blockpos, direction);
	}

	public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel,
			BlockPos pCurrentPos, BlockPos pFacingPos) {
		return pFacing.getOpposite() == pState.getValue(FACING) && !pState.canSurvive(pLevel, pCurrentPos)
				? Blocks.AIR.defaultBlockState()
				: pState;
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
			InteractionHand interactionHand, BlockHitResult hitResult) {

		BlockEntity dryingRack = level.getBlockEntity(pos);
		DryingRackEntity dryingRackEntity = (DryingRackEntity) dryingRack;

		if (!level.isClientSide() && interactionHand == InteractionHand.MAIN_HAND) {

			if (dryingRack instanceof DryingRackEntity) {

				if (dryingRackEntity.inventory.getStackInSlot(0).isEmpty()) {
					
						
						dryingRackEntity.inventory.insertItem(0, new ItemStack(player.getItemInHand(interactionHand).getItem()), false);
						player.getInventory().removeFromSelected(false);
						dryingRackEntity.setActive();
						dryingRackEntity.updateEntity();

					

				} else {
					dryingRackEntity.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(),
							dryingRackEntity.inventory.getStackInSlot(0));
					dryingRackEntity.inventory.extractItem(0, 1, false);
					if (dryingRackEntity.getActive()) {
						dryingRackEntity.setActive();
					}
					dryingRackEntity.updateEntity();
				}

			}

			return InteractionResult.SUCCESS;
		}
		return super.use(state, level, pos, player, interactionHand, hitResult);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
		// TODO Auto-generated method stub
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
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {

		return new DryingRackEntity(pos, state);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);

	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext collisionContext) {

		SHAPE = switch (state.getValue(FACING)) {
		case EAST -> Block.box(0, 11, 0, 2, 15, 16);
		case NORTH -> Block.box(0, 11, 14, 16, 15, 16);
		case SOUTH -> Block.box(0, 11, 0, 16, 15, 2);
		case WEST -> Block.box(14, 11, 0, 16, 15, 16);
		default -> Block.box(0, 10, 14, 16, 12, 16);
		};

		return SHAPE;
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
			BlockEntityType<T> type) {

		return type == BlockEntityInit.DRYING_RACK.get() ? DryingRackEntity::tick : null;
	}

	@Override
	public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
		if (!pState.is(pNewState.getBlock())) {
			BlockEntity blockentity = pLevel.getBlockEntity(pPos);
			DryingRackEntity dryingRackEntity = (DryingRackEntity) blockentity;

			dryingRackEntity.dropItemStack(pLevel, pPos.getX(), pPos.getY(), pPos.getZ(),
					dryingRackEntity.inventory.getStackInSlot(0));

		}
		super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
	}

}
