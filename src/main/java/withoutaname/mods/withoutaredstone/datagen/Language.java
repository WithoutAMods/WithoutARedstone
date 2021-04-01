package withoutaname.mods.withoutaredstone.datagen;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraftforge.common.data.LanguageProvider;
import withoutaname.mods.withoutaredstone.WithoutARedstone;

public class Language extends LanguageProvider {

	private final String locale;

	public Language(DataGenerator gen, String locale) {
		super(gen, WithoutARedstone.MODID, locale);
		this.locale = locale;
	}

	@Override
	protected void addTranslations() {
		add("itemGroup.emptymod", "EmptyMod", "EmptyMod");
	}

	private void add(String key, String de_de, String en_us) {
		switch(locale) {
			case "de_de":
				add(key, de_de);
				break;
			case "en_us":
				add(key, en_us);
				break;
		}
	}

	private void add(Item key, String de_de, String en_us) {
		add(key.getTranslationKey(), de_de, en_us);
	}

	private void add(Block key, String de_de, String en_us) {
		add(key.getTranslationKey(), de_de, en_us);
	}

}
