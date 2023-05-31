package items;

import init.ItemInit;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;

import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class WeedBong extends Item {

	public WeedBong() {
		super(new Item.Properties().stacksTo(0).defaultDurability(500).tab(ItemInit.instance));

	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player entity, InteractionHand interactionHand) {
		ItemStack lighter = new ItemStack(Items.FLINT_AND_STEEL);
		ItemStack bong = entity.getItemInHand(interactionHand);
		Inventory inventory = entity.getInventory();

		if (inventory.contains(lighter)) {

			lighter.hurtAndBreak(5, entity, (Player) -> {
				Player.broadcastBreakEvent(interactionHand);

			});

			entity.startUsingItem(interactionHand);

			level.playLocalSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.BUBBLE_COLUMN_UPWARDS_AMBIENT,
					SoundSource.PLAYERS, 20, 1.0F, true);

			return InteractionResultHolder.consume(bong);
		} else

		{

			return InteractionResultHolder.fail(bong);
		}

	}

	@Override
	public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {

		MobEffectInstance[] effects = { new MobEffectInstance(MobEffects.BLINDNESS, 1000),
				new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 3000), new MobEffectInstance(MobEffects.WEAKNESS, 4000),
				new MobEffectInstance(MobEffects.HEALTH_BOOST, 7000), new MobEffectInstance(MobEffects.HUNGER, 5000) };

		for (MobEffectInstance effect : effects) { // why not
			pLivingEntity.addEffect(effect, pLivingEntity);
		}

		pLevel.playLocalSound(pLivingEntity.getX(), pLivingEntity.getY(), pLivingEntity.getZ(),
				SoundEvents.BUBBLE_COLUMN_BUBBLE_POP, SoundSource.PLAYERS, 20, 1.0F, true);
		pStack.hurtAndBreak(50, pLivingEntity, (Player) -> {
			Player.broadcastBreakEvent(pLivingEntity.getUsedItemHand());
		});

		pStack.finishUsingItem(pLevel, pLivingEntity);

	}

	@Override
	public UseAnim getUseAnimation(ItemStack p_41452_) {

		return UseAnim.BOW;
	}

	@Override
	public int getUseDuration(ItemStack pStack) {
		return 1500;

	}

}
