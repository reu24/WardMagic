package com.reu_24.wardmagic.object.item;

import com.reu_24.wardmagic.container.BookOfTheWardMagicianContainer;
import com.reu_24.wardmagic.init.ModContainerTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class BookOfTheWardMagicianItem extends Item {
    public BookOfTheWardMagicianItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (!worldIn.isRemote) {
            NetworkHooks.openGui((ServerPlayerEntity)playerIn, new SimpleNamedContainerProvider(
                    (id, unused1, unused2) -> new BookOfTheWardMagicianContainer(id, ModContainerTypes.BOOK_OF_THE_WARD_MAGICIAN),
                    playerIn.getHeldItem(handIn).getDisplayName()));
        }
        return ActionResult.resultSuccess(playerIn.getHeldItem(handIn));
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        if (!context.getWorld().isRemote) {
            NetworkHooks.openGui((ServerPlayerEntity)context.getPlayer(), new SimpleNamedContainerProvider(
                            (id, unused1, unused2) -> new BookOfTheWardMagicianContainer(id, ModContainerTypes.BOOK_OF_THE_WARD_MAGICIAN),
                            context.getItem().getDisplayName()));
        }
        return ActionResultType.SUCCESS;
    }
}
