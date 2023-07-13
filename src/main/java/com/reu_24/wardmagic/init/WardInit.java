package com.reu_24.wardmagic.init;

import com.reu_24.wardmagic.ward.*;

import java.util.ArrayList;

public class WardInit {
    protected static ArrayList<AbstractWard> wards = new ArrayList<>();

    public static void registerWards() {
        wards.add(new StrengthWard());
        wards.add(new ManaStealWard());
        wards.add(new CapacityWard());
        wards.add(new LifeStealWard());
        wards.add(new SoulExtractorWard());
        wards.add(new TeleportationWard());
        wards.add(new FireWard());
        wards.add(new RangeWard());
        wards.add(new WindWard());
        wards.add(new ExplosionWard());
        wards.add(new SafetyWard());
    }

    public static ArrayList<AbstractWard> getWards() {
        return wards;
    }
}
