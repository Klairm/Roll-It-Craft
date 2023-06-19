package items;

import init.ItemInit;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
		super(new Item.Properties().defaultDurability(100).tab(ItemInit.instance));

	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player entity, InteractionHand interactionHand) {

		ItemStack itemstack = entity.getItemInHand(interactionHand);

		entity.startUsingItem(interactionHand);

		System.out.println(entity.getLookAngle());
		for (int i = 0; i < 2; i++) {
			level.addAlwaysVisibleParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, entity.getLookAngle().x + entity.getX(),
					entity.getY() + entity.getLookAngle().y + 1.8, entity.getLookAngle().z + entity.getZ(), 0, 0.005,
					0.005);
		}

		return InteractionResultHolder.consume(itemstack);

	}

	@Override
	public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {

		MobEffectInstance[] effects = { new MobEffectInstance(MobEffects.BLINDNESS, 1000),
				new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 1500), new MobEffectInstance(MobEffects.WEAKNESS, 1000),
				new MobEffectInstance(MobEffects.REGENERATION, 2000), new MobEffectInstance(MobEffects.HUNGER, 2000) };

		for (MobEffectInstance effect : effects) {
			pLivingEntity.addEffect(effect, pLivingEntity);
		}

		pLevel.addAlwaysVisibleParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE,
				pLivingEntity.getLookAngle().x + pLivingEntity.getX(),
				pLivingEntity.getY() + pLivingEntity.getLookAngle().y + 1.8,
				pLivingEntity.getLookAngle().z + pLivingEntity.getZ(), 0, 0.005, 0.005);
		pLivingEntity.playSound(getEatingSound(), 50, 1);

		pStack.finishUsingItem(pLevel, pLivingEntity);

	}

	@Override
	public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
		pStack.hurtAndBreak(5, pLivingEntity, (Player) -> {
			Player.broadcastBreakEvent(pLivingEntity.getUsedItemHand());
		});
		return super.finishUsingItem(pStack, pLevel, pLivingEntity);
	}

	@Override
	public UseAnim getUseAnimation(ItemStack item) {

		return UseAnim.BOW;

	}

	@Override
	public int getUseDuration(ItemStack pStack) {
		return 50;

	}

	@Override
	public SoundEvent getEatingSound() {

		return SoundEvents.FIRE_AMBIENT;
	}

}
