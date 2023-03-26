package blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;

public class DrugPlant extends SugarCaneBlock {
	private int maxHeight;
	

	public DrugPlant(Properties settings, int maxHeight) {
		super(settings);
		this.maxHeight = maxHeight;

	}

	@Override
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource randomSource) {
		if (world.isEmptyBlock(pos.above())) {
			int i;
			for (i = 1; world.getBlockState(pos.below(i)).is(this); ++i) {
			}

			if (i < maxHeight) {
				int j = state.getValue(AGE);
				if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(world, pos, state, true)) {
					if (j == 15) {
						world.setBlockAndUpdate(pos.above(), this.defaultBlockState());
						net.minecraftforge.common.ForgeHooks.onCropsGrowPost(world, pos.above(),
								this.defaultBlockState());
						world.setBlock(pos, state.setValue(AGE, Integer.valueOf(0)), 4);
					} else {
						world.setBlock(pos, state.setValue(AGE, Integer.valueOf(j + 1)), 4);
					}
				}
			}
		}

	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		BlockState soil = world.getBlockState(pos.below());
		if (soil.canSustainPlant(world, pos.below(), Direction.UP, this))
			return true;
		BlockState blockstate = world.getBlockState(pos.below());
		if (blockstate.is(this)) {
			return true;
		} else {

			if (blockstate.is(BlockTags.CROPS)) {
				BlockPos blockpos = pos.below();

				for (Direction direction : Direction.Plane.HORIZONTAL) {
					BlockState blockstate1 = world.getBlockState(blockpos.relative(direction));
					FluidState fluidstate = world.getFluidState(blockpos.relative(direction));
					if (state.canBeHydrated(world, pos, fluidstate, blockpos.relative(direction))
							|| blockstate1.is(Blocks.FROSTED_ICE)) {
						return true;
					}
				}
			}

			return false;
		}
	}

	@Override
	public net.minecraftforge.common.PlantType getPlantType(BlockGetter world, BlockPos pos) {
		return net.minecraftforge.common.PlantType.CROP;
	}
}
