package com.reu_24.wardmagic.util;

import com.reu_24.wardmagic.WardMagic;
import net.minecraft.util.ResourceLocation;

public class ModResourceLocation extends ResourceLocation {

    public ModResourceLocation(String resourceName) {
        super(WardMagic.MOD_ID, resourceName);
    }


}
