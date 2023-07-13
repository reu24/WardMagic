package com.reu_24.wardmagic.object.item;

import com.reu_24.wardmagic.capability.WardsProvider;
import com.reu_24.wardmagic.container.ChalkContainer;
import com.reu_24.wardmagic.container.WardCompassContainer;
import com.reu_24.wardmagic.init.ModContainerTypes;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraftforge.fml.network.NetworkHooks;

public class WardCompassItem extends Item {
    public WardCompassItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        if (context.getFace() != Direction.UP) {
            return ActionResultType.FAIL;
        }
        if (!context.getWorld().isRemote) {
            NetworkHooks.openGui((ServerPlayerEntity)context.getPlayer(), new SimpleNamedContainerProvider(
                            (id, unused1, unused2) -> new WardCompassContainer(id, ModContainerTypes.WARD_COMPASS, context.getPos()),
                            context.getItem().getDisplayName()),
                    buffer -> buffer.writeBlockPos(context.getPos()));
        }
        return ActionResultType.SUCCESS;
    }
}
