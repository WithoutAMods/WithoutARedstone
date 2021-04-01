package withoutaname.mods.withoutaredstone;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import withoutaname.mods.withoutaredstone.setup.ClientSetup;
import withoutaname.mods.withoutaredstone.setup.ModSetup;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(WithoutARedstone.MODID)
public class WithoutARedstone {

	public static final String MODID = "withoutaredstone";

	public static final Logger LOGGER = LogManager.getLogger();

	public WithoutARedstone() {
		// Register the setup method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(ModSetup::init);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init);
	}

}
