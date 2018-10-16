package de.ellpeck.naturesaura.events;

import de.ellpeck.naturesaura.Helper;
import de.ellpeck.naturesaura.blocks.ModBlocks;
import de.ellpeck.naturesaura.blocks.tiles.TileEntityWoodStand;
import de.ellpeck.naturesaura.recipes.TreeRitualRecipe;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.terraingen.SaplingGrowTreeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;

public class TerrainGenEvents {

    @SubscribeEvent
    public void onTreeGrow(SaplingGrowTreeEvent event) {
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        if (!world.isRemote) {
            if (Helper.checkMultiblock(world, pos, TileEntityWoodStand.GOLD_POWDER_POSITIONS, ModBlocks.GOLD_POWDER.getDefaultState(), true)) {
                List<TileEntity> tileEntities = Helper.getTileEntitiesInArea(world, pos, 5);
                List<TileEntityWoodStand> stands = new ArrayList<>();
                List<ItemStack> usableItems = new ArrayList<>();

                for (TileEntity tile : tileEntities) {
                    if (tile instanceof TileEntityWoodStand) {
                        TileEntityWoodStand stand = (TileEntityWoodStand) tile;
                        if (!stand.stack.isEmpty()) {
                            usableItems.add(stand.stack);
                            stands.add(stand);
                        }
                    }
                }

                IBlockState sapling = world.getBlockState(pos);
                ItemStack saplingStack = sapling.getBlock().getItem(world, pos, sapling);
                if (!saplingStack.isEmpty()) {
                    for (TreeRitualRecipe recipe : TreeRitualRecipe.RECIPES) {
                        if (recipe.matchesItems(saplingStack, usableItems)) {
                            Map<TileEntityWoodStand, ItemStack> actuallyInvolved = new HashMap<>();
                            List<ItemStack> stillRequired = new ArrayList<>(Arrays.asList(recipe.items));
                            TileEntityWoodStand toPick = null;

                            for (TileEntityWoodStand stand : stands) {
                                int index = Helper.getItemIndex(stillRequired, stand.stack);
                                if (index >= 0) {
                                    actuallyInvolved.put(stand, stand.stack);
                                    stillRequired.remove(index);

                                    if (toPick == null) {
                                        toPick = stand;
                                    }
                                }
                            }

                            toPick.setRitual(pos, recipe, actuallyInvolved);
                            break;
                        }
                    }
                }
            }
        }
    }
}