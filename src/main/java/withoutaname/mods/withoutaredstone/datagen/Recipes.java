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
	protected void registerRecipes(@Nonnull Consumer<IFinishedRecipe> consumer) {
		ShapedRecipeBuilder.shapedRecipe(Registration.WIRELESS_LINK_ITEM.get())
				.patternLine("RTR")
				.patternLine("SIS")
				.key('R', Items.REDSTONE)
				.key('T', Items.REDSTONE_TORCH)
				.key('S', Items.STONE)
				.key('I', Items.IRON_INGOT)
				.addCriterion("redstone", InventoryChangeTrigger.Instance.forItems(Items.REDSTONE))
				.build(consumer);
	}

}
