package withoutaname.mods.withoutaredstone.setup;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import withoutaname.mods.withoutaredstone.blocks.WirelessLinkBlock;
import withoutaname.mods.withoutaredstone.blocks.WirelessLinkTile;

import static withoutaname.mods.withoutaredstone.WithoutARedstone.MODID;

public class Registration {

	private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
	private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
	private static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MODID);

	public static void init() {
		BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
		ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
		TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

	public static final RegistryObject<WirelessLinkBlock> WIRELESS_LINK_BLOCK = BLOCKS.register("wireless_link", WirelessLinkBlock::new);
	public static final RegistryObject<BlockItem> WIRELESS_LINK_ITEM = ITEMS.register("wireless_link", () -> new BlockItem(WIRELESS_LINK_BLOCK.get(), ModSetup.defaultItemProperties));
	public static final RegistryObject<TileEntityType<WirelessLinkTile>> WIRELESS_LINK_TILE = TILES.register("wireless_link", () -> TileEntityType.Builder.create(WirelessLinkTile::new, WIRELESS_LINK_BLOCK.get()).build(null));
}
