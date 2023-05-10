package blocks;

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
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import tile.DryingRackTile;

public class DryingRack extends HorizontalDirectionalBlock implements EntityBlock {
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	private static final VoxelShape SHAPE = Block.box(0, 8, 14, 16, 12, 16);

	public DryingRack(Properties props) {
		super(props);

	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
			InteractionHand interactionHand, BlockHitResult hitResult) {

		if (!level.isClientSide() && interactionHand == InteractionHand.MAIN_HAND) {
			BlockEntity dryingRack = level.getBlockEntity(pos);

			if (dryingRack instanceof DryingRackTile) {

				if (((DryingRackTile) dryingRack).inventory.getStackInSlot(0).isEmpty()) {
					if (player.getItemInHand(interactionHand).getItem().asItem() == ItemInit.BUD.get()) {
						((DryingRackTile) dryingRack).inventory.insertItem(0, new ItemStack(ItemInit.BUD.get()), false);
						((DryingRackTile) dryingRack).setActive();

					}

				} else {
					player.drop(((DryingRackTile) dryingRack).inventory.getStackInSlot(0), false);
					((DryingRackTile) dryingRack).inventory.extractItem(0, 1, false);
					if (((DryingRackTile) dryingRack).getActive()) {
						((DryingRackTile) dryingRack).setActive();
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
		return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection());
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

		return new DryingRackTile(pos, state);
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(FACING);

	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext collisionContext) {

		return SHAPE;
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
			BlockEntityType<T> type) {

		return type == BlockEntityInit.DRYING_RACK.get() ? DryingRackTile::tick : null;
	}

}
