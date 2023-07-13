package com.reu_24.wardmagic.util;

import net.minecraft.client.audio.TickableSound;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;

public class WindSound extends TickableSound {

    private final PlayerEntity player;
    private int time;

    public WindSound(PlayerEntity player) {
        super(SoundEvents.ITEM_ELYTRA_FLYING, SoundCategory.PLAYERS);
        this.player = player;
        this.repeat = true;
        this.repeatDelay = 0;
        this.volume = 0.1F;
    }

    @Override
    public void tick() {
        ++this.time;
        if (!this.player.removed && (this.time <= 20 || !this.player.isOnGround())) {
            this.x = (double)((float)this.player.getPosX());
            this.y = (double)((float)this.player.getPosY());
            this.z = (double)((float)this.player.getPosZ());
            float f = (float)this.player.getMotion().lengthSquared();
            if ((double)f >= 1.0E-7D) {
                this.volume = MathHelper.clamp(f / 4.0F, 0.0F, 1.0F);
            } else {
                this.volume = 0.0F;
            }

            float f1 = 0.8F;
            if (this.volume > 0.8F) {
                this.pitch = 1.0F + (this.volume - 0.8F);
            } else {
                this.pitch = 1.0F;
            }

        } else {
            this.finishPlaying();
        }
    }
}
