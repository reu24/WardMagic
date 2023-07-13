package com.reu_24.wardmagic.util;

import com.reu_24.wardmagic.data.Annotations;
import com.reu_24.wardmagic.init.BlockInit;
import com.reu_24.wardmagic.init.ItemInit;
import com.reu_24.wardmagic.init.ModParticleTypes;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;

import java.lang.reflect.Field;
import java.util.function.BiFunction;

public final class ReflectionHelper {
    private ReflectionHelper() {}

    public static void forEachBlock(BiFunction<Annotations.BlockData, Field, Void> forEach) {
        for (Field field : BlockInit.class.getFields()) {
            Annotations.BlockData blockData = field.getAnnotation(Annotations.BlockData.class);
            if (blockData != null) {
                forEach.apply(blockData, field);
            }
        }
    }

    public static void forEachItem(BiFunction<Annotations.ItemData, Field, Void> forEach) {
        for (Field field : ItemInit.class.getFields()) {
            Annotations.ItemData itemData = field.getAnnotation(Annotations.ItemData.class);
            if (itemData != null) {
                forEach.apply(itemData, field);
            }
        }
    }

    public static void forEachParticle(BiFunction<Annotations.ParticleData, Field, Void> forEach) {
        for (Field field : ModParticleTypes.class.getFields()) {
            Annotations.ParticleData particleData = field.getAnnotation(Annotations.ParticleData.class);
            if (particleData != null) {
                forEach.apply(particleData, field);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static Block getBlock(Field field) {
        try {
            return ((RegistryObject<Block>) field.get(null)).get();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static Item getItem(Field field) {
        try {
            return ((RegistryObject<Item>) field.get(null)).get();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}
