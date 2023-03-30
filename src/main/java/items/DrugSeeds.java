package items;

import java.lang.reflect.Field;

import blocks.DrugPlant;
import init.BlockInit;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.RegistryObject;

public class DrugSeeds extends Item {

	public DrugSeeds(Item.Properties properties) {
		super(properties);

	}

	@Override
	public InteractionResult useOn(UseOnContext context) {

		try {
			// Using reflexion to give DrugPlant and DrugSeeds classes more polymorphism

			Field blockField;
			BlockPos pos = context.getClickedPos();
			// Format string to get the name of the seed and the block that will be placed
			String drugName = this.getName(getDefaultInstance()).getString(5).trim().toUpperCase() + "_PLANT";

			/*
			 * Get the field property of BlockInit using the drugName string, example of
			 * drugName; WEED_PLANT then we get the object of it which should be a
			 * RegistryObject, we check that in the if condition, then we save that property
			 * ( which is a RegistryObject (and should be of type DrugPlant) into the
			 * registryObj object In the example of WEED_PLANT is the equivalent of doing:
			 * BlockInit.WEED_PLANT.get().defaultBlockState()
			 */
			blockField = BlockInit.class.getDeclaredField(drugName);
			Object blockProperty = blockField.get(null);
			if (blockProperty instanceof RegistryObject<?>) {
				RegistryObject<DrugPlant> registryObj = (RegistryObject<DrugPlant>) blockProperty;

				if (context.getLevel().getBlockState(pos).getBlock() == Blocks.FARMLAND
						&& (context.getLevel().getBlockState(pos.above()).getBlock() == Blocks.AIR)) {

					context.getLevel().setBlock(pos.above(), registryObj.get().defaultBlockState(), 3);
					context.getPlayer().getInventory().removeItem(
							context.getPlayer().getInventory().findSlotMatchingItem(new ItemStack(this)), 1);

				}

			}

			return InteractionResult.PASS;
		} catch (Exception e) {
			// assume hell if it fails
			e.printStackTrace();
			return InteractionResult.FAIL;
		}

	}

	@Override
	public UseAnim getUseAnimation(ItemStack p_41452_) {

		return UseAnim.SPEAR;
	}

}
