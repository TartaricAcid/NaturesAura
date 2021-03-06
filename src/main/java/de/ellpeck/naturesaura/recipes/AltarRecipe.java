package de.ellpeck.naturesaura.recipes;

import de.ellpeck.naturesaura.Helper;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class AltarRecipe {

    public static final Map<ResourceLocation, AltarRecipe> RECIPES = new HashMap<>();

    public final ResourceLocation name;
    public final ItemStack input;
    public final ItemStack output;
    public final Block catalyst;
    public final int aura;
    public final int time;

    public AltarRecipe(ResourceLocation name, ItemStack input, ItemStack output, Block catalyst, int aura, int time) {
        this.name = name;
        this.input = input;
        this.output = output;
        this.catalyst = catalyst;
        this.aura = aura;
        this.time = time;

        RECIPES.put(this.name, this);
    }

    public static AltarRecipe forInput(ItemStack input) {
        for (AltarRecipe recipe : RECIPES.values()) {
            if (Helper.areItemsEqual(recipe.input, input, true)) {
                return recipe;
            }
        }
        return null;
    }
}
