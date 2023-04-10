package items;

import init.ItemInit;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;

import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class Grinder extends Item {

	public Grinder() {
		super(new Item.Properties().stacksTo(0).defaultDurability(150).tab(ItemInit.instance));

	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player entity, InteractionHand interactionHand) {
		ItemStack budItemStack = new ItemStack(ItemInit.BUD.get());
		ItemStack grindedWeedStack = new ItemStack(ItemInit.GRINDED.get());
		ItemStack grinder = entity.getItemInHand(interactionHand);
		Inventory inventory = entity.getInventory();

		if (inventory.contains(budItemStack)) {
			
			inventory.removeItem(inventory.findSlotMatchingItem(budItemStack), 1);
			if (inventory.getFreeSlot() < 0) {
				
				entity.drop(grindedWeedStack, false);
			}
			grinder.hurtAndBreak(2, entity, (Player) -> {
				Player.broadcastBreakEvent(interactionHand);
			
			});
			inventory.add(grindedWeedStack);
			level.playLocalSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.SPYGLASS_USE,
					SoundSource.PLAYERS, 10, 1.0F, true);
			
			return InteractionResultHolder.success(entity.getItemInHand(interactionHand));
		} else {

			return InteractionResultHolder.fail(entity.getItemInHand(interactionHand));
		}

	}

	@Override
	public UseAnim getUseAnimation(ItemStack p_41452_) {

		return UseAnim.SPEAR;
	}

}
