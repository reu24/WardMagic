package com.reu_24.wardmagic.object.item;

import com.reu_24.wardmagic.WardMagic;
import com.reu_24.wardmagic.capability.IWards;
import com.reu_24.wardmagic.capability.WardsProvider;
import com.reu_24.wardmagic.init.BlockInit;
import com.reu_24.wardmagic.init.WardInit;
import com.reu_24.wardmagic.object.block.WardBlock;
import com.reu_24.wardmagic.util.ModPacketHandler;
import com.reu_24.wardmagic.util.ModResourceLocation;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = WardMagic.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MagnifyingGlassItem extends Item {

    public static final int WARD_TICKS = 200;

    protected static int wardFound = -1;
    protected static int ticksRemaining = 0;
    protected static boolean newWard = false;
    protected static ClientWorld world = null;

    public MagnifyingGlassItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        if (!context.getWorld().isRemote()) {
            BlockState blockState = context.getWorld().getBlockState(context.getPos());
            if (blockState.getBlock() == BlockInit.WARD_BLOCK.get()) {
                PlayerEntity player = context.getPlayer();
                IWards wards = player.getCapability(WardsProvider.WARDS).orElseThrow(NullPointerException::new);
                int wardId = blockState.get(WardBlock.WARD_ID);
                if (!wards.hasWard(wardId)) {

                    wards.addWard(wardId);
                    ModPacketHandler.sendToClient("ward", (ServerPlayerEntity)player, wardId, true);
                } else {
                    ModPacketHandler.sendToClient("ward", (ServerPlayerEntity)player, wardId, false);
                }
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }

    @SubscribeEvent
    public static void attachCapability(AttachCapabilitiesEvent<Entity> e) {
        e.addCapability(new ModResourceLocation("wards"), new WardsProvider());
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event)
    {
        if (event.isWasDeath()){
            PlayerEntity player = event.getPlayer();
            IWards wards = player.getCapability(WardsProvider.WARDS).orElseThrow(NullPointerException::new);
            IWards oldWards = event.getOriginal().getCapability(WardsProvider.WARDS, null).orElseThrow(NullPointerException::new);

            wards.setWards(oldWards.getWards());
        }
    }

    @SubscribeEvent
    public static void tick(TickEvent.PlayerTickEvent e) {
        if (e.player.world.isRemote()) {
            ticksRemaining--;
        }
    }

    public static void handleWard(World world, PlayerEntity player, Object... a) {
        wardFound = (Integer)a[0];
        newWard = (Boolean)a[1];
        ticksRemaining = WARD_TICKS;
        MagnifyingGlassItem.world = (ClientWorld) world;
    }

    @SubscribeEvent
    public static void onRenderOverlay(RenderGameOverlayEvent.Pre e) {
        Minecraft mc = Minecraft.getInstance();
        if(!e.isCanceled() && e.getType() == RenderGameOverlayEvent.ElementType.TEXT && ticksRemaining > 0 && mc.world == world) {
            int posX = e.getWindow().getScaledWidth();
            int posY = e.getWindow().getScaledHeight() / 8;
            String wardName = WardInit.getWards().get(wardFound).getName(true);
            String message;

            if (newWard) {
                message = new TranslationTextComponent("magnifyingGlass." + WardMagic.MOD_ID + ".new", wardName).getString();
            } else {
                message = new TranslationTextComponent("magnifyingGlass." + WardMagic.MOD_ID + ".old", wardName).getString();
            }

            mc.fontRenderer.drawString(e.getMatrixStack(), message, posX / 2 - mc.fontRenderer.getStringWidth(message) / 2, posY, 14737632);
        }
    }
}
