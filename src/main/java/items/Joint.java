package items;

import init.ItemInit;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class Joint extends Item {

	public Joint() {
		super(new Item.Properties().defaultDurability(100).tab(ItemInit.instance)
				.food(new FoodProperties.Builder().nutrition(0).alwaysEat()
						.effect(() -> new MobEffectInstance(MobEffects.HUNGER, 2000), 1)
						.effect(() -> new MobEffectInstance(MobEffects.BLINDNESS, 1000), 1)
						.effect(() -> new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 1000), 1)
						.effect(() -> new MobEffectInstance(MobEffects.HEALTH_BOOST, 3000), 1)
						.effect(() -> new MobEffectInstance(MobEffects.WEAKNESS, 1000), 1)

						.build()));

	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player entity, InteractionHand interactionHand) {

		ItemStack itemstack = entity.getItemInHand(interactionHand);
		if (itemstack.isEdible()) {
			if (entity.canEat(itemstack.getFoodProperties(entity).canAlwaysEat())) {
				entity.startUsingItem(interactionHand);
				RandomSource randomsource = entity.getRandom();
				for (int i = 0; i < 2; i++) {
					level.addAlwaysVisibleParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, (double) entity.getX() + 0.8D,
							(double) entity.getY() + randomsource.nextDouble() + randomsource.nextDouble(),
							(double) entity.getZ() + 0.5D
									+ randomsource.nextDouble() / 3.0D * (double) (randomsource.nextBoolean() ? 1 : -1),
							0, 0.05D, 0.05D);
				}

				return InteractionResultHolder.consume(itemstack);
			} else {
				return InteractionResultHolder.fail(itemstack);
			}
		} else {
			return InteractionResultHolder.pass(entity.getItemInHand(interactionHand));
		}
	}

	@Override
	public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity entity) {
		RandomSource randomsource = entity.getRandom();
		level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE,
				(double) entity.getX() + 0.5D
						+ randomsource.nextDouble() / 4.0D * (double) (randomsource.nextBoolean() ? 1 : -1),
				(double) entity.getY() + 0.4D,
				(double) entity.getZ() + 0.5D
						+ randomsource.nextDouble() / 4.0D * (double) (randomsource.nextBoolean() ? 1 : -1),
				0.0D, 0.005D, 0.0D);
		return super.finishUsingItem(itemStack, level, entity);
	}

	@Override
	public UseAnim getUseAnimation(ItemStack item) {
		return UseAnim.BOW;

	}

	@Override
	public SoundEvent getEatingSound() {
		return SoundEvents.CAMPFIRE_CRACKLE;
	}

}
