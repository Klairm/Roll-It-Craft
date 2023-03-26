package items;

import init.BlockInit;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;

public class DrugSeeds extends Item {
	public DrugSeeds(Item.Properties properties) {
		super(properties);

	}

	@Override
	public InteractionResult useOn(UseOnContext context) {

		BlockPos pos = context.getClickedPos();
		if (context.getLevel().getBlockState(pos).getBlock() == Blocks.FARMLAND
				&& (context.getLevel().getBlockState(pos.above()).getBlock() == Blocks.AIR)) {

			context.getLevel().setBlock(pos.above(), BlockInit.WEED_PLANT.get().defaultBlockState(), 3);

		}
		System.out.println();
		return InteractionResult.PASS;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack p_41452_) {

		return UseAnim.BLOCK;
	}

}
