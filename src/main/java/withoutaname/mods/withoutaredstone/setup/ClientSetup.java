package withoutaname.mods.withoutaredstone.setup;

import javax.annotation.Nonnull;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientSetup {
	
	public static void init(@Nonnull final FMLClientSetupEvent event) {
		event.enqueueWork(() -> ItemBlockRenderTypes.setRenderLayer(Registration.WIRELESS_LINK_BLOCK.get(), RenderType.cutout()));
	}
	
}
