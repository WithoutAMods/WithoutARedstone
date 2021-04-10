package withoutaname.mods.withoutaredstone.datagen;

import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Items;
import withoutaname.mods.withoutaredstone.setup.Registration;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class Recipes extends RecipeProvider{

	public Recipes(DataGenerator generatorIn) {
		super(generatorIn);
	}

	@Override
	protected void buildShapelessRecipes(@Nonnull Consumer<IFinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(Registration.WIRELESS_LINK_ITEM.get())
				.pattern("RTR")
				.pattern("SIS")
				.define('R', Items.REDSTONE)
				.define('T', Items.REDSTONE_TORCH)
				.define('S', Items.STONE)
				.define('I', Items.IRON_INGOT)
				.unlockedBy("redstone", InventoryChangeTrigger.Instance.hasItems(Items.REDSTONE))
				.save(consumer);
	}

}
