package withoutaname.mods.withoutaredstone.datagen;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraftforge.common.data.LanguageProvider;

import withoutaname.mods.withoutaredstone.WithoutARedstone;
import withoutaname.mods.withoutaredstone.setup.Registration;

public class Language extends LanguageProvider {
	
	private final String locale;
	
	public Language(DataGenerator gen, String locale) {
		super(gen, WithoutARedstone.MODID, locale);
		this.locale = locale;
	}
	
	@Override
	protected void addTranslations() {
		add(Registration.WIRELESS_LINK_BLOCK.get(), "Wireless Link", "Wireless Link");
		
		add("screen.withoutaredstone.wireless_link_modify", "Wireless Link", "Wireless Link");
		
		add("itemGroup.withoutaredstone", "WithoutARedstone", "WithoutARedstone");
	}
	
	private void add(@Nonnull String key, @Nonnull String de_de, @Nonnull String en_us) {
		switch (locale) {
			case "de_de":
				add(key, de_de);
				break;
			case "en_us":
				add(key, en_us);
				break;
		}
	}
	
	private void add(@Nonnull Item key, @Nonnull String de_de, @Nonnull String en_us) {
		add(key.getDescriptionId(), de_de, en_us);
	}
	
	private void add(@Nonnull Block key, @Nonnull String de_de, @Nonnull String en_us) {
		add(key.getDescriptionId(), de_de, en_us);
	}
	
}
