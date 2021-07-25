package withoutaname.mods.withoutaredstone.datagen;

import java.util.function.Consumer;
import javax.annotation.Nonnull;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;

import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import withoutaname.mods.withoutaredstone.setup.Registration;

public class Recipes extends RecipeProvider {
	
	public Recipes(DataGenerator generatorIn) {
		super(generatorIn);
	}
	
	@Override
	protected void buildCraftingRecipes(@Nonnull Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(Registration.WIRELESS_LINK_ITEM.get())
				.pattern("RTR")
				.pattern("SIS")
				.define('R', Items.REDSTONE)
				.define('T', Items.REDSTONE_TORCH)
				.define('S', Items.STONE)
				.define('I', Items.IRON_INGOT)
				.unlockedBy("redstone", InventoryChangeTrigger.TriggerInstance.hasItems(Items.REDSTONE))
				.save(consumer);
	}
	
}
