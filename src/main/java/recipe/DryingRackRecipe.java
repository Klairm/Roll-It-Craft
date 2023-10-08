package recipe;

import javax.annotation.Nullable;

import org.antlr.v4.runtime.misc.NotNull;

import com.github.klairm.projectm.ProjectM;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

public class DryingRackRecipe implements Recipe<SimpleContainer> {
	private final ResourceLocation id;
	private final ItemStack output;
	private final NonNullList<Ingredient> recipeItems;

	public DryingRackRecipe(ResourceLocation id, ItemStack output, NonNullList<Ingredient> recipeItems) {
		this.id = id;
		this.output = output;
		this.recipeItems = recipeItems;
	}

	@Override
	public boolean matches(SimpleContainer pContainer, Level pLevel) {
		
		return recipeItems.get(0).test(pContainer.getItem(0)); 
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {

		return recipeItems;
	}

	@Override
	public ItemStack assemble(SimpleContainer pContainer) {

		return output;
	}

	@Override
	public boolean canCraftInDimensions(int pWidth, int pHeight) {

		return true;
	}

	@Override
	public ItemStack getResultItem() {

		return output.copy();
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {

		return Serializer.INSTANCE;
	}

	@Override
	public RecipeType<?> getType() {
		return Type.INSTANCE;
	}

	public static class Type implements RecipeType<DryingRackRecipe> {
		private Type() {
		}

		public static final Type INSTANCE = new Type();

		public static final String ID = "drying_rack_process";

	}

	public static class Serializer implements RecipeSerializer<DryingRackRecipe> {
		public static final Serializer INSTANCE = new Serializer();
		public static final ResourceLocation ID = new ResourceLocation(ProjectM.MOD_ID, "drying_rack_process");

		@Override
		public DryingRackRecipe fromJson(ResourceLocation pRecipeId, JsonObject json) {
			ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));

			JsonArray ingredients = GsonHelper.getAsJsonArray(json, "ingredients");
			NonNullList<Ingredient> inputs = NonNullList.withSize(1, Ingredient.EMPTY);
			for (int i = 0; i < inputs.size(); i++) {
				inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
			}
			return new DryingRackRecipe(pRecipeId, output, inputs);
		}

		@Override
		public @Nullable DryingRackRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
			NonNullList<Ingredient> inputs = NonNullList.withSize(pBuffer.readInt(), Ingredient.EMPTY);

			for (int i = 0; i < inputs.size(); i++) {
				inputs.set(i, Ingredient.fromNetwork(pBuffer));
			}

			ItemStack output = pBuffer.readItem();
			return new DryingRackRecipe(pRecipeId, output, inputs);

		}

		@Override
		public void toNetwork(FriendlyByteBuf pBuffer, DryingRackRecipe pRecipe) {
			pBuffer.writeInt(pRecipe.getIngredients().size());

			for (Ingredient ing : pRecipe.getIngredients()) {
				ing.toNetwork(pBuffer);
			}

			pBuffer.writeItemStack(pRecipe.getResultItem(), false);

		}

	}

}
