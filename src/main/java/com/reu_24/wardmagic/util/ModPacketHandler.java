package com.reu_24.wardmagic.util;

import com.reu_24.wardmagic.entities.SoulEntity;
import com.reu_24.wardmagic.init.BlockInit;
import com.reu_24.wardmagic.init.ModParticleTypes;
import com.reu_24.wardmagic.object.block.WardBlock;
import com.reu_24.wardmagic.object.item.MagnifyingGlassItem;
import com.reu_24.wardmagic.tileentity.WardAltarTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ModPacketHandler {

    public static void register() {
        register("ward", "1", MagnifyingGlassItem::handleWard, Integer.class, Boolean.class);

        register("place_ward", "1", (world, player, a) -> {
            Integer wardId = (Integer) a[0];
            BlockPos placePos = (BlockPos) a[1];
            world.setBlockState(placePos, BlockInit.WARD_BLOCK.get().getDefaultState().with(WardBlock.WARD_ID, wardId));
        }, Integer.class, BlockPos.class);

        register("crack_stone", "1", (world, player, a) -> {
                    BlockPos pos = (BlockPos)a[0];
                    world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_SCAFFOLDING_BREAK, SoundCategory.BLOCKS, 1.0f, 1.0f, true);
                },
                BlockPos.class);

        register("start_ritual", "1", (world, player, a) -> {
            BlockPos pos = (BlockPos) a[0];
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof WardAltarTileEntity) {
                WardHelper.startRitual(world, player, pos, (WardAltarTileEntity) world.getTileEntity(pos));
            }
        }, BlockPos.class);

        register("ritual_finished", "1", (world, player, a) -> {
            BlockPos pos = (BlockPos) a[0];
            world.addParticle(ParticleTypes.EXPLOSION_EMITTER, pos.getX(), pos.getY(), pos.getZ(), 1.0D, 0.0D, 0.0D);
            world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 1.0f, 0.0f, true);
        }, BlockPos.class);
    }


    // *****************************************************************************************************

    public static class SimplePacket {
        protected Object[] values;
        protected IMessageConsumer consumer;

        public SimplePacket(PacketBuffer packetBuffer, IMessageConsumer consumer, Class<?>... types) {
            this.consumer = consumer;
            Object[] values = new Object[types.length];
            for (int i = 0; i < types.length; i++) {
                Class<?> type = types[i];
                if (type == BlockPos.class) {
                    values[i] = packetBuffer.readBlockPos();
                } else if (type == Integer.class) {
                    values[i] = packetBuffer.readInt();
                } else if (type == Double.class) {
                    values[i] = packetBuffer.readDouble();
                } else if (type == Boolean.class) {
                    values[i] = packetBuffer.readBoolean();
                } else {
                    throw new UnsupportedOperationException("Tried to decode type: " + type.getName());
                }
            }

            set(values);
        }

        public SimplePacket(Object[] values) {
            set(values);
        }

        protected void set(Object[] values) {
            this.values = values;
        }

        public void encode(PacketBuffer packetBuffer) {
            for (Object value : values) {
                if (value instanceof BlockPos) {
                    packetBuffer.writeBlockPos((BlockPos) value);
                } else if (value instanceof Integer) {
                    packetBuffer.writeInt((Integer) value);
                } else if (value instanceof Double) {
                    packetBuffer.writeDouble((Double) value);
                } else if (value instanceof Boolean) {
                    packetBuffer.writeBoolean((Boolean) value);
                } else {
                    throw new UnsupportedOperationException("Tried to encode type: " + value.getClass().getName());
                }
            }
        }

        public void handle(Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                if (ctx.get().getSender() == null) {
                    World world = Minecraft.getInstance().world;
                    consumer.consume(world, Minecraft.getInstance().player, values);
                } else {
                    World world = ctx.get().getSender().world;
                    if (!world.isRemote()) {
                        consumer.consume(world, ctx.get().getSender(), values);
                    }
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }

    public interface IMessageConsumer {
        void consume(World world, PlayerEntity player, Object[] obj);
    }

    protected static int id = 0;
    protected static Map<String, SimpleChannel> channelNameMap = new HashMap<>();

    public static void register(String name, String version, IMessageConsumer consumer, Class<?>... types) {
        SimpleChannel channel = NetworkRegistry.newSimpleChannel(
                new ModResourceLocation(name),
                () -> version,
                version::equals,
                version::equals);

        channel.registerMessage(id++, SimplePacket.class, SimplePacket::encode, (pb) -> new SimplePacket(pb, consumer, types), SimplePacket::handle);
        channelNameMap.put(name, channel);
    }

    public static void sendToServer(String name, Object... values) {
        channelNameMap.get(name).sendToServer(new SimplePacket(values));
    }

    public static void sendToClient(String name, ServerPlayerEntity player, Object... values) {
        channelNameMap.get(name).sendTo(new SimplePacket(values), player.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendToClient(String name, PacketDistributor.PacketTarget packetTarget, Object... values) {
        channelNameMap.get(name).send(packetTarget, new SimplePacket(values));
    }

    public static void sendToAllClients(String name, Object... values) {
        sendToClient(name, PacketDistributor.ALL.noArg(), values);
    }

    public static void sendToAllNearClients(String name, BlockPos pos, int radius, World world, Object... values) {
        sendToClient(name, PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(pos.getX(), pos.getY(), pos.getZ(), radius, world.getDimensionKey())), values);
    }
}
