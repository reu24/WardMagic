package com.reu_24.wardmagic.damagesource;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class DeadlyMatterDamageSource extends DamageSource {
    public DeadlyMatterDamageSource() {
        super("deadly_matter");
    }

    @Override
    public ITextComponent getDeathMessage(LivingEntity entityLivingBaseIn) {
        return new TranslationTextComponent("death.attack.deadly_matter.message", entityLivingBaseIn.getDisplayName());
    }
}
