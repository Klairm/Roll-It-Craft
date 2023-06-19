package items;

import init.ItemInit;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;

import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class CocaineBag extends Item {

	public CocaineBag(Item.Properties properties) {
		super(properties);

	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player entity, InteractionHand interactionHand) {

		MobEffectInstance[] effects = { new MobEffectInstance(MobEffects.HARM, 1),
				new MobEffectInstance(MobEffects.JUMP, 1500), new MobEffectInstance(MobEffects.WEAKNESS, 5000),
				new MobEffectInstance(MobEffects.DIG_SPEED.addAttributeModifier(Attributes.ATTACK_SPEED,
						"AF8B6E3F-3328-4C0A-AA36-5BA2BB9DBEF3", 5,
						AttributeModifier.Operation.MULTIPLY_TOTAL), 2000),
				new MobEffectInstance(
						MobEffects.MOVEMENT_SPEED.addAttributeModifier(Attributes.MOVEMENT_SPEED,
								"91AEAA56-376B-4498-935B-2F7F68070635", 8, AttributeModifier.Operation.MULTIPLY_TOTAL),
						2000) };

		for (MobEffectInstance effect : effects) {
			entity.addEffect(effect, entity);
		}

		entity.getInventory().removeItem(entity.getInventory().findSlotMatchingItem(new ItemStack(this)), 1);

		return InteractionResultHolder.consume(entity.getItemInHand(interactionHand));

	}

	@Override
	public UseAnim getUseAnimation(ItemStack p_41452_) {

		return UseAnim.SPEAR;
	}

}
