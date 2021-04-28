package withoutaname.mods.withoutaredstone.setup;

import javax.annotation.Nonnull;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientSetup {
	
	public static void init(@Nonnull final FMLClientSetupEvent event) {
		event.enqueueWork(() -> RenderTypeLookup.setRenderLayer(Registration.WIRELESS_LINK_BLOCK.get(), RenderType.cutout()));
	}
	
}
