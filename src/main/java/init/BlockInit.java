package init;

import java.util.function.Supplier;

import com.github.klairm.projectm.ProjectM;

import blocks.DrugPlant;
import blocks.DryingRack;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockInit {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
			ProjectM.MOD_ID);

	public static final RegistryObject<DrugPlant> WEED_PLANT = BLOCKS.register("weed_plant",
			() -> new DrugPlant(BlockBehaviour.Properties.of(Material.PLANT).noCollission().randomTicks().instabreak()
					.sound(SoundType.GRASS), 5));
	public static final RegistryObject<Block> DRYING_RACK = BLOCKS.register("drying_rack",
			() -> new DryingRack(Block.Properties.copy(Blocks.STONE)));

	@SubscribeEvent
	public static void onRegisterItems(final RegisterEvent event) {
		if (event.getRegistryKey().equals(ForgeRegistries.Keys.ITEMS)) {
			BLOCKS.getEntries().forEach((blockRegistryObject) -> {
				Block block = blockRegistryObject.get();
				Item.Properties properties = new Item.Properties().tab(ItemInit.instance);
				Supplier<Item> blockItemFactory = () -> new BlockItem(block, properties);
				event.register(ForgeRegistries.Keys.ITEMS, blockRegistryObject.getId(), blockItemFactory);
			});
		}

	}

}
