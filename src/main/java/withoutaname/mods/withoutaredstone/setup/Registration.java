package withoutaname.mods.withoutaredstone.setup;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import withoutaname.mods.withoutaredstone.blocks.WirelessLinkBlock;
import withoutaname.mods.withoutaredstone.blocks.WirelessLinkEntity;

import static withoutaname.mods.withoutaredstone.WithoutARedstone.MODID;

public class Registration {
	
	private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
	private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
	private static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, MODID);
	
	public static void init() {
		BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
		ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
		TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
	
	public static final RegistryObject<WirelessLinkBlock> WIRELESS_LINK_BLOCK = BLOCKS.register("wireless_link", WirelessLinkBlock::new);
	public static final RegistryObject<BlockItem> WIRELESS_LINK_ITEM = ITEMS.register("wireless_link", () -> new BlockItem(WIRELESS_LINK_BLOCK.get(), ModSetup.DEFAULT_ITEM_PROPERTIES));
	public static final RegistryObject<BlockEntityType<WirelessLinkEntity>> WIRELESS_LINK_TILE = TILES.register("wireless_link", () -> BlockEntityType.Builder.of(WirelessLinkEntity::new, WIRELESS_LINK_BLOCK.get()).build(null));
}
