package com.reu_24.wardmagic.capability;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Wards implements IWards {

    protected Set<Integer> wards = new HashSet<>();

    @Override
    public Set<Integer> getWards() {
        return wards;
    }

    @Override
    public void addWard(int wardId) {
        wards.add(wardId);
    }

    @Override
    public boolean hasWard(int wardId) {
        return wards.contains(wardId);
    }

    @Override
    public void setWards(Set<Integer> wards) {
        this.wards = wards;
    }
}
