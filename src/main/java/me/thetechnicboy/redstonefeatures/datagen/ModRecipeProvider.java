package me.thetechnicboy.redstonefeatures.datagen;

import me.thetechnicboy.redstonefeatures.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {

    public ModRecipeProvider(PackOutput output){
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.REDSTONE_REQUESTER.get())
                .pattern("SES")
                .pattern("ERE")
                .pattern("SES")
                .define('E', Items.ECHO_SHARD)
                .define('R', Items.REDSTONE_BLOCK)
                .define('S', Items.SMOOTH_STONE)
                .unlockedBy("has_well", has(Items.ECHO_SHARD))
                .showNotification(false)
                .save(consumer);
    }
}
