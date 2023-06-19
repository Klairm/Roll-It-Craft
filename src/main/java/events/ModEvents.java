package events;

import java.util.List;

import com.github.klairm.projectm.ProjectM;

import init.ItemInit;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades.ItemListing;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;

import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ProjectM.MOD_ID)
public class ModEvents {

	@SubscribeEvent
	public static void addCustomTrades(VillagerTradesEvent event) {
		if (event.getType() == VillagerProfession.FARMER) {
			Int2ObjectMap<List<ItemListing>> trades = event.getTrades();
			trades.get(1).add((trader, rand) -> new MerchantOffer(new ItemStack(Items.EMERALD, 2),
					new ItemStack(ItemInit.WEED_SEEDS.get(), 5), 5, 8, 0.5F));
			trades.get(1).add((trader, rand) -> new MerchantOffer(new ItemStack(ItemInit.BUD.get(), 10),
					new ItemStack(ItemInit.GRINDER.get(), 1), 2, 3, 0.8F));
			trades.get(1).add((trader, rand) -> new MerchantOffer(new ItemStack(Items.DIAMOND, 5),
					new ItemStack(ItemInit.COCA_BAG.get(), 1), 2, 3, 0.8F));
			trades.get(1).add((trader, rand) -> new MerchantOffer(new ItemStack(Items.DIAMOND, 2),
					new ItemStack(ItemInit.COCA_SEEDS.get(), 2), 2, 3, 0.8F));
		}
	}

}
