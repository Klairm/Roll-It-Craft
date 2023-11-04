package init;

import com.github.klairm.projectm.ProjectM;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import recipe.DrugMachineRecipe;
import recipe.DryingRackRecipe;

public class RecipeTypeInit {
	public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister
			.create(ForgeRegistries.RECIPE_SERIALIZERS, ProjectM.MOD_ID);

	public static final RegistryObject<RecipeSerializer<DryingRackRecipe>> DRYING_RACK_SERIALIZER = SERIALIZERS
			.register("drying_rack_process", () -> DryingRackRecipe.Serializer.INSTANCE);

	public static final RegistryObject<RecipeSerializer<DrugMachineRecipe>> DRUG_MACHINE_SERIALIZER= SERIALIZERS
			.register("drug_machine", () -> DrugMachineRecipe.Serializer.INSTANCE);
	
	public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister
			.create(ForgeRegistries.RECIPE_TYPES, ProjectM.MOD_ID);

	
	public static final RegistryObject<RecipeType<DrugMachineRecipe>> DRUG_MACHINE = RECIPE_TYPES
			.register("drug_machine", () -> DrugMachineRecipe.Type.INSTANCE);
	
	public static final RegistryObject<RecipeType<DryingRackRecipe>> DRYING_RACK_PROCESS = RECIPE_TYPES
			.register("drying_rack_process", () -> DryingRackRecipe.Type.INSTANCE);
	

	public static void register(IEventBus eventBus) {
		SERIALIZERS.register(eventBus);
		RECIPE_TYPES.register(eventBus);
	}
}
