package blocks;

import blockentity.DryingRackBlockEntity;
import init.BlockEntityInit;
import init.ItemInit;
import it.unimi.dsi.fastutil.bytes.Byte2BooleanSortedMaps.SynchronizedSortedMap;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

public class DryingRack extends HorizontalDirectionalBlock implements EntityBlock {
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	private static VoxelShape SHAPE = Block.box(0, 10, 14, 16, 12, 16);

	public DryingRack(Properties props) {
		super(props);

	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
			InteractionHand interactionHand, BlockHitResult hitResult) {

		if (!level.isClientSide() && interactionHand == InteractionHand.MAIN_HAND) {
			BlockEntity dryingRack = level.getBlockEntity(pos);

			if (dryingRack instanceof DryingRackBlockEntity) {

				if (((DryingRackBlockEntity) dryingRack).inventory.getStackInSlot(0).isEmpty()) {
					if (player.getItemInHand(interactionHand).getItem().asItem() == ItemInit.BUD.get()) {
						((DryingRackBlockEntity) dryingRack).inventory.insertItem(0, new ItemStack(ItemInit.BUD.get()),
								false);
						((DryingRackBlockEntity) dryingRack).setActive();

					}

				} else {
					player.drop(((DryingRackBlockEntity) dryingRack).inventory.getStackInSlot(0), false);
					((DryingRackBlockEntity) dryingRack).inventory.extractItem(0, 1, false);
					if (((DryingRackBlockEntity) dryingRack).getActive()) {
						((DryingRackBlockEntity) dryingRack).setActive();
					}
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

		return new DryingRackBlockEntity(pos, state);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);

	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext collisionContext) {

		switch (state.getValue(FACING)) {

		case EAST:
			SHAPE = Block.box(0, 11, 0, 2, 15, 16);

			break;
		case NORTH:
			SHAPE = Block.box(0, 11, 14, 16, 15, 16);

			break;
		case SOUTH:
			SHAPE = Block.box(0, 11, 0, 16, 15, 2);
			break;

		case WEST:
			SHAPE = Block.box(14, 11, 0, 16, 15, 16);
			break;
		default:
			SHAPE = Block.box(0, 10, 14, 16, 12, 16);
			break;

		}

		return SHAPE;
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
			BlockEntityType<T> type) {

		return type == BlockEntityInit.DRYING_RACK.get() ? DryingRackBlockEntity::tick : null;
	}

}
