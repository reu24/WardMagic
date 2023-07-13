package com.reu_24.wardmagic.data;

import com.reu_24.wardmagic.init.BlockInit;
import com.reu_24.wardmagic.init.ItemInit;
import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.item.Items;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(ItemInit.MAGNIFYING_GLASS.get()).key('s', Items.STICK).key('g', Blocks.GLASS).patternLine(" gg").patternLine(" gg").patternLine("s  ").addCriterion("has_glass", hasItem(Blocks.GLASS)).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ItemInit.CHALK.get()).key('d', Items.DIORITE).patternLine("d ").patternLine(" d").addCriterion("has_stone", hasItem(Blocks.DIORITE)).build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(BlockInit.MAGICAL_PLANKS.get(), 4).addIngredient(BlockInit.MAGICAL_LOG.get()).addCriterion("has_magical_log", hasItem(BlockInit.MAGICAL_LOG.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ItemInit.MAGICAL_STICK.get(), 4).key('w', BlockInit.MAGICAL_PLANKS.get()).patternLine("w").patternLine("w").addCriterion("has_magical_log", hasItem(BlockInit.MAGICAL_LOG.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(BlockInit.CRACK_STONE.get(), 4).key('s', Blocks.SMOOTH_STONE).patternLine("ss").patternLine("ss").addCriterion("has_smooth_stone", hasItem(Blocks.SMOOTH_STONE)).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ItemInit.WARD_COMPASS.get(), 1).key('i', Items.IRON_INGOT).key('d', Blocks.DIORITE).key('s', Blocks.STONE).patternLine("  s").patternLine(" i ").patternLine("d  ").addCriterion("has_iron", hasItem(Items.IRON_INGOT)).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ItemInit.WARD_WAND.get(), 1).key('s', ItemInit.MAGICAL_STICK.get()).key('d', Items.DIAMOND).key('e', Items.EMERALD).patternLine(" ds").patternLine(" se").patternLine("s  ").addCriterion("has_diamond", hasItem(Items.DIAMOND)).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ItemInit.MAGICAL_SWORD.get(), 1).key('s', ItemInit.MAGICAL_STICK.get()).key('p', BlockInit.MAGICAL_PLANKS.get()).patternLine(" p ").patternLine(" p ").patternLine(" s ").addCriterion("has_magical_planks", hasItem(ItemInit.MAGICAL_STICK.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ItemInit.MANA_HOLDER.get(), 1).key('g', Items.GLASS).patternLine("g g").patternLine("g g").patternLine("ggg").addCriterion("has_glass", hasItem(Items.GLASS)).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ItemInit.BOOK_OF_THE_WARD_MAGICIAN.get(), 1).key('b', Items.BOOK).key('p', BlockInit.MAGICAL_PLANKS.get()).patternLine("ppp").patternLine("pbp").patternLine("ppp").addCriterion("has_magical_planks", hasItem(BlockInit.MAGICAL_PLANKS.get())).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(BlockInit.WARD_ALTAR.get(), 1).key('a', Items.ANDESITE).key('i', Items.IRON_INGOT).patternLine("aaa").patternLine(" i ").patternLine("aaa").addCriterion("has_iron", hasItem(Items.IRON_INGOT)).build(consumer);
    }
}
