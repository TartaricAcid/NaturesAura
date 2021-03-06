package de.ellpeck.naturesaura.blocks.tiles.render;

import de.ellpeck.naturesaura.Helper;
import de.ellpeck.naturesaura.blocks.tiles.TileEntityWoodStand;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;

public class RenderWoodStand extends TileEntitySpecialRenderer<TileEntityWoodStand> {
    @Override
    public void render(TileEntityWoodStand tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        ItemStack stack = tile.items.getStackInSlot(0);
        if (!stack.isEmpty()) {
            GlStateManager.pushMatrix();
            Item item = stack.getItem();
            if (item instanceof ItemBlock && ((ItemBlock) item).getBlock().getRenderLayer() == BlockRenderLayer.SOLID) {
                GlStateManager.translate(x + 0.5F, y + 0.9735F, z + 0.5F);
                float scale = 0.65F;
                GlStateManager.scale(scale, scale, scale);
            } else {
                GlStateManager.translate(x + 0.5F, y + 0.825F, z + 0.5F);
                float scale = 0.4F;
                GlStateManager.scale(scale, scale, scale);
                GlStateManager.rotate(90F, 1F, 0F, 0F);
            }
            Helper.renderItemInWorld(stack);
            GlStateManager.popMatrix();
        }
    }
}
