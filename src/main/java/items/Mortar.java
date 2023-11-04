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
import net.minecraft.world.level.Explosion.BlockInteraction;
import net.minecraft.world.level.Level;

public class Mortar extends Item {

	public Mortar() {
		super(new Item.Properties().stacksTo(0).defaultDurability(150).tab(ItemInit.instance));

	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player entity, InteractionHand interactionHand) {
		ItemStack cocaLeafItemStack= new ItemStack(ItemInit.COCA_LEAF.get());
		ItemStack crushedCocaine = new ItemStack(ItemInit.CRUSHED_COCA.get());
		ItemStack mortar = entity.getItemInHand(interactionHand);
		Inventory inventory = entity.getInventory();
		
		

		if (inventory.contains(cocaLeafItemStack)) {
			
			inventory.removeItem(inventory.findSlotMatchingItem(cocaLeafItemStack), 1);
			if (inventory.getFreeSlot() < 0) {
				
				entity.drop(crushedCocaine, false);
			}
			mortar.hurtAndBreak(2, entity, (Player) -> {
				Player.broadcastBreakEvent(interactionHand);
			
			});
			inventory.add(crushedCocaine);
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