package com.reu_24.wardmagic.capability;

import java.util.Set;

public interface IWards {
    Set<Integer> getWards();
    void addWard(int wardId);
    boolean hasWard(int wardId);
    void setWards(Set<Integer> wards);
}
