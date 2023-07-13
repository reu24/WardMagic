package com.reu_24.wardmagic.data;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Annotations {

    @Retention(RetentionPolicy.RUNTIME)
    public @interface BlockData {
        BlockStateType blockState() default BlockStateType.SIMPLE;
        LootTableType lootTableType() default LootTableType.SELF;
        String lootId() default "";
        boolean generateItem() default true;
        String name();
        String id();
    }

    public enum BlockStateType {
        SIMPLE,
        WARD,
        LOG,
        CROSS,
        CRACK,
        SMALL
    }

    public enum LootTableType {
        NONE,
        SELF,
        SIMPLE,
        LEAVE
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface ItemData {
        ItemParent itemParent() default ItemParent.ITEM;
        boolean addTexture() default true;
        String name();
        String id();
    }

    public enum ItemParent {
        ITEM("item/generated"),
        HAND_HELD("item/handheld"),
        SPAWN_EGG("item/template_spawn_egg");

        protected String path;

        ItemParent(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface ParticleData {
        String id();
        String[] textures();
    }
}
