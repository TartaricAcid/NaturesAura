package de.ellpeck.naturesaura.reg;

import de.ellpeck.naturesaura.NaturesAura;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class ModRegistry {

    private static final List<IModItem> ALL_ITEMS = new ArrayList<>();

    public static void addItemOrBlock(IModItem item) {
        ALL_ITEMS.add(item);
    }

    private static void registerItem(Item item, String name, boolean addCreative) {
        item.setTranslationKey(NaturesAura.MOD_ID + "." + name);

        item.setRegistryName(NaturesAura.MOD_ID, name);
        ForgeRegistries.ITEMS.register(item);

        if (addCreative) {
            item.setCreativeTab(NaturesAura.CREATIVE_TAB);
        } else {
            item.setCreativeTab(null);
        }
    }

    private static void registerBlock(Block block, String name, ItemBlock item, boolean addCreative) {
        block.setTranslationKey(NaturesAura.MOD_ID + "." + name);

        block.setRegistryName(NaturesAura.MOD_ID, name);
        ForgeRegistries.BLOCKS.register(block);

        item.setRegistryName(block.getRegistryName());
        ForgeRegistries.ITEMS.register(item);

        if (addCreative) {
            block.setCreativeTab(NaturesAura.CREATIVE_TAB);
        } else {
            block.setCreativeTab(null);
        }
    }

    public static void preInit(FMLPreInitializationEvent event) {
        for (IModItem item : ALL_ITEMS) {
            if (item instanceof Item) {
                registerItem((Item) item, item.getBaseName(), item.shouldAddCreative());
            } else if (item instanceof Block) {
                Block block = (Block) item;

                ItemBlock itemBlock;
                if (item instanceof ICustomItemBlockProvider) {
                    itemBlock = ((ICustomItemBlockProvider) item).getItemBlock();
                } else {
                    itemBlock = new ItemBlock(block);
                }

                registerBlock(block, item.getBaseName(), itemBlock, item.shouldAddCreative());
            }

            if (item instanceof IModelProvider) {
                Map<ItemStack, ModelResourceLocation> models = ((IModelProvider) item).getModelLocations();

                for (ItemStack stack : models.keySet()) {
                    NaturesAura.proxy.registerRenderer(stack, models.get(stack));
                }
            }

            item.onPreInit(event);
        }
    }

    public static void init(FMLInitializationEvent event) {
        for (IModItem item : ALL_ITEMS) {
            if (item instanceof IColorProvidingBlock) {
                NaturesAura.proxy.addColorProvidingBlock((IColorProvidingBlock) item);
            }

            if (item instanceof IColorProvidingItem) {
                NaturesAura.proxy.addColorProvidingItem((IColorProvidingItem) item);
            }

            item.onInit(event);
        }
    }

    public static void postInit(FMLPostInitializationEvent event) {
        for (IModItem item : ALL_ITEMS) {
            item.onPostInit(event);
        }
    }
}
