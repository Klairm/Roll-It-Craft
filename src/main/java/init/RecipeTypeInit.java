package init;

import com.github.klairm.projectm.ProjectM;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import recipe.DryingRackRecipe;

public class RecipeTypeInit {
	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister
			.create(ForgeRegistries.RECIPE_SERIALIZERS, ProjectM.MOD_ID);

	public static final RegistryObject<RecipeSerializer<DryingRackRecipe>> DRYING_RACK_RECIPE_SERIALIZER = RECIPE_SERIALIZERS
			.register("drying_rack_process", () -> DryingRackRecipe.Serializer.INSTANCE);

	public static void register(IEventBus eventBus) {
		RECIPE_SERIALIZERS.register(eventBus);

	}

}
