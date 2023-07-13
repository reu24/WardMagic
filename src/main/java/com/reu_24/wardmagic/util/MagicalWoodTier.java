package com.reu_24.wardmagic.util;

import com.reu_24.wardmagic.init.BlockInit;
import com.reu_24.wardmagic.init.ItemInit;
import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;

import java.util.function.Supplier;

public enum MagicalWoodTier implements IItemTier {

    MAGICAL_WOOD(0, 300, 2.0f, 0.0f, () -> {
        return Ingredient.fromItems(BlockInit.MAGICAL_PLANKS.get());
    });

    private final int harvestLevel;
    private final int maxUses;
    private final float efficiency;
    private final float attackDamage;
    private final LazyValue<Ingredient> repairMaterial;

    MagicalWoodTier(int harvestLevel, int maxUses, float efficiency, float attackDamage, Supplier<Ingredient> repairMaterial) {
        this.harvestLevel = harvestLevel;
        this.maxUses = maxUses;
        this.efficiency = efficiency;
        this.attackDamage = attackDamage;
        this.repairMaterial = new LazyValue<>(repairMaterial);
    }

    @Override
    public int getMaxUses() {
        return maxUses;
    }

    @Override
    public float getEfficiency() {
        return efficiency;
    }

    @Override
    public float getAttackDamage() {
        return attackDamage;
    }

    @Override
    public int getHarvestLevel() {
        return harvestLevel;
    }

    @Override
    public int getEnchantability() {
        return 0;
    }

    @Override
    public Ingredient getRepairMaterial() {
        return repairMaterial.getValue();
    }
}
