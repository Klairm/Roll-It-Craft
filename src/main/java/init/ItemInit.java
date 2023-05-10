package init;

import com.github.klairm.projectm.ProjectM;

import items.Joint;
import items.DrugSeeds;
import items.Grinder;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
	public static ModCreativeModeTab instance = new ModCreativeModeTab(CreativeModeTab.TABS.length, "ProjectM");
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ProjectM.MOD_ID);
	public static final RegistryObject<Joint> JOINT = ITEMS.register("joint", () -> new Joint());
	public static final RegistryObject<Grinder> GRINDER = ITEMS.register("grinder", () -> new Grinder());
	public static final RegistryObject<Item> BUD = ITEMS.register("bud",
			() -> new Item(new Item.Properties().tab(instance)));
	public static final RegistryObject<Item> DRY_BUD = ITEMS.register("dry_bud",
			() -> new Item(new Item.Properties().tab(instance)));

	public static final RegistryObject<DrugSeeds> WEED_SEEDS = ITEMS.register("weed_seeds",
			() -> new DrugSeeds(new Item.Properties().tab(instance)));

	public static final RegistryObject<Item> GRINDED = ITEMS.register("grinded_weed",
			() -> new Item(new Item.Properties().tab(instance)));

}
