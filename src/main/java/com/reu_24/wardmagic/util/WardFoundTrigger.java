package com.reu_24.wardmagic.util;

import com.google.gson.JsonObject;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.util.ResourceLocation;

public class WardFoundTrigger extends AbstractCriterionTrigger<WardFoundTrigger.Instance> {
    private static final ResourceLocation ID = new ResourceLocation("ward_found");

    @Override
    protected WardFoundTrigger.Instance deserializeTrigger(JsonObject json, EntityPredicate.AndPredicate entityPredicate, ConditionArrayParser conditionsParser) {
        return new Instance(ID, entityPredicate);
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public void trigger(ServerPlayerEntity player, PlayerInventory inventory, ItemStack stack, int full, int empty, int occupied) {
        triggerListeners(player, (instance) -> true);
    }

    public static class Instance extends CriterionInstance {
        public Instance(ResourceLocation criterion, EntityPredicate.AndPredicate playerCondition) {
            super(criterion, playerCondition);
        }
    }
}
