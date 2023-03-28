package blocks;

import init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import tile.DryingRackTile;

public class DryingRack extends Block implements EntityBlock {

	public DryingRack(Properties props) {
		super(props);

	}
	
	@Override
	public InteractionResult use(BlockState p_60503_, Level p_60504_, BlockPos p_60505_, Player p_60506_,
			InteractionHand p_60507_, BlockHitResult p_60508_) {
		// TODO Auto-generated method stub
		return super.use(p_60503_, p_60504_, p_60505_, p_60506_, p_60507_, p_60508_);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {

		return new DryingRackTile(pos, state);
	}

	is
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
			BlockEntityType<T> type) {
		// TODO Auto-generated method stub
		return type == BlockEntityInit.DRYING_RACK.get() ? DryingRackTile::tick : null;
	}

}
