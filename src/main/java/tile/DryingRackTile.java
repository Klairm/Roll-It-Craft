package tile;

import init.BlockEntityInit;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;

import net.minecraft.world.level.block.state.BlockState;

public class DryingRackTile extends BlockEntity implements EntityBlock {

	public DryingRackTile(BlockPos pos, BlockState state) {
		super(BlockEntityInit.DRYING_RACK.get(), pos, state);

	}

	public static void tick(Level level, BlockPos pos, BlockState state, BlockEntity be) {

		if (!level.isClientSide) {
			level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BUCKET_FILL, SoundSource.PLAYERS, 10,
					1.0F, true);
		}

	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		// TODO Auto-generated method stub
		return BlockEntityInit.DRYING_RACK.get().create(pos, state);
	}

}
