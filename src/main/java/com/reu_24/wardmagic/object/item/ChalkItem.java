package com.reu_24.wardmagic.object.item;

import com.reu_24.wardmagic.WardMagic;
import com.reu_24.wardmagic.capability.WardsProvider;
import com.reu_24.wardmagic.client.gui.ChalkScreen;
import com.reu_24.wardmagic.container.ChalkContainer;
import com.reu_24.wardmagic.init.ModContainerTypes;
import com.reu_24.wardmagic.util.ClientEventBusSubscriber;
import com.reu_24.wardmagic.util.ModPacketHandler;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.List;

@Mod.EventBusSubscriber(modid = WardMagic.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ChalkItem extends Item {
    public ChalkItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        BlockState blockState = context.getWorld().getBlockState(context.getPos());
        if (isInvalidPosition(context, blockState)) {
            return ActionResultType.FAIL;
        }
        if (!context.getWorld().isRemote) {
            int[] knownWards = context.getPlayer().getCapability(WardsProvider.WARDS).orElseThrow(NullPointerException::new).getWards().stream().mapToInt(i -> i).toArray();
            NetworkHooks.openGui((ServerPlayerEntity)context.getPlayer(), new SimpleNamedContainerProvider(
                    (id, unused1, unused2) -> new ChalkContainer(id, ModContainerTypes.CHALK, knownWards, context.getPos()),
                    context.getItem().getDisplayName()),
                    buffer -> {
                        buffer.writeVarIntArray(knownWards);
                        buffer.writeBlockPos(context.getPos());
                    });
        }
        return ActionResultType.SUCCESS;
    }

    protected static boolean isInvalidPosition(ItemUseContext context, BlockState blockState) {
        return context.getFace() != Direction.UP || !blockState.isSolidSide(context.getWorld(), context.getPos(), Direction.UP) ||
                !context.getWorld().getBlockState(context.getPos().up()).isReplaceable(new BlockItemUseContext(context));
    }

    protected static boolean isValidPosition(BlockRayTraceResult rayTraceResult, BlockState blockState) {
        return !isInvalidPosition(new ItemUseContext(Minecraft.getInstance().world, Minecraft.getInstance().player, null, Minecraft.getInstance().player.getHeldItemMainhand(), rayTraceResult), blockState);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (ClientEventBusSubscriber.getPlaceWardKeyBinding() != null) {
            tooltip.add(new TranslationTextComponent("item." + WardMagic.MOD_ID + ".chalk_tooltip",
                    ClientEventBusSubscriber.getPlaceWardKeyBinding().getKey().func_237520_d_().getString().toUpperCase()));
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onKeyBindPressed(InputEvent.KeyInputEvent event)
    {
        if (ClientEventBusSubscriber.getPlaceWardKeyBinding().isPressed()) {
            if (Minecraft.getInstance().objectMouseOver instanceof BlockRayTraceResult) {
                if (Minecraft.getInstance().player.getHeldItemMainhand().getItem() instanceof ChalkItem
                || Minecraft.getInstance().player.getHeldItemOffhand().getItem() instanceof ChalkItem) {
                    BlockRayTraceResult rayTraceResult = (BlockRayTraceResult) Minecraft.getInstance().objectMouseOver;
                    BlockPos pos = rayTraceResult.getPos();
                    if (isValidPosition(rayTraceResult, Minecraft.getInstance().world.getBlockState(pos))) {
                        int lastWardId = ChalkScreen.getLastWardId();
                        if (lastWardId != -1) {
                            ModPacketHandler.sendToServer("place_ward", lastWardId, pos.up());
                        }
                    }
                }
            }
        }
    }
}
