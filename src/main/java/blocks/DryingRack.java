package blocks;

import init.BlockEntityInit;
import net.minecraft.core.BlockPos;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import tile.DryingRackTile;

public class DryingRack extends Block implements EntityBlock {

	public DryingRack(Properties props) {
		super(props);

	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {

		return new DryingRackTile(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
			BlockEntityType<T> type) {
		// TODO Auto-generated method stub
		return type == BlockEntityInit.DRYING_RACK.get() ? DryingRackTile::tick : null;
	}

}
