package com.reu_24.wardmagic.tileentity;

import com.reu_24.wardmagic.WardMagic;
import com.reu_24.wardmagic.capability.ManaProvider;
import com.reu_24.wardmagic.container.WardAltarContainer;
import com.reu_24.wardmagic.init.ModContainerTypes;
import com.reu_24.wardmagic.init.ModTileEntityTypes;
import com.reu_24.wardmagic.util.BaseItemHandler;
import com.reu_24.wardmagic.util.ModPacketHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;

public class WardAltarTileEntity extends TileEntity implements INamedContainerProvider {

    protected ITextComponent customName;
    protected BaseItemHandler inventory;
    protected float manaRequired = 0;
    protected ItemStack enchantedItem;

    public static final String CUSTOM_NAME = "CustomName";
    public static final int MANA_HOLDERS = 4;

    public WardAltarTileEntity() {
        super(ModTileEntityTypes.WARD_ALTAR.get());

        inventory = new BaseItemHandler(5);
    }

    @Override
    public ITextComponent getDisplayName() {
        return getName();
    }

    public void setCustomName(ITextComponent name) {
        customName = name;
    }

    public ITextComponent getName() {
        return customName != null ? customName : getDefaultName();
    }

    private ITextComponent getDefaultName() {
        return new TranslationTextComponent("container." + WardMagic.MOD_ID + ".ward_altar");
    }


    @Override
    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);
        if (compound.contains(CUSTOM_NAME, Constants.NBT.TAG_STRING)) {
            customName = ITextComponent.Serializer.getComponentFromJson(compound.getString(CUSTOM_NAME));
        }
        NonNullList<ItemStack> inv = NonNullList.withSize(inventory.getSlots(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, inv);
        inventory.setNonNullList(inv);

        manaRequired = compound.getFloat("manaRequired");
        if (compound.contains("enchantedItem")) {
            enchantedItem = ItemStack.read(compound.getCompound("enchantedItem"));
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        if (customName != null) {
            compound.putString("CustomName", ITextComponent.Serializer.toJson(this.customName));
        }

        ItemStackHelper.saveAllItems(compound, inventory.toNonNullList());

        compound.putFloat("manaRequired", manaRequired);
        if (enchantedItem != null) {
            CompoundNBT enchantedItemNbt = new CompoundNBT();
            enchantedItem.write(enchantedItemNbt);
            compound.put("enchantedItem", enchantedItemNbt);
        }
        return compound;
    }

    public BaseItemHandler getInventory() {
        return inventory;
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT nbt = new CompoundNBT();
        this.write(nbt);
        return new SUpdateTileEntityPacket(this.pos, 0, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        this.read(getBlockState(), pkt.getNbtCompound());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT nbt = new CompoundNBT();
        this.write(nbt);
        return nbt;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        read(state, tag);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> inventory));
    }

    public void markDirty() {
        super.markDirty();
        if (world != null) {
            world.notifyBlockUpdate(getPos(), getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
        }
    }

    @Override
    public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity player) {
        return new WardAltarContainer(windowId, playerInventory, this, ModContainerTypes.WARD_ALTAR);
    }

    public ItemStack getItemToEnchant() {
        return inventory.getStackInSlot(4);
    }

    public void initForRitual(float manaRequired, ItemStack itemToEnchant) {
        this.enchantedItem = itemToEnchant;
        inventory.removeStackFromSlot(4);
        setManaRequired(manaRequired);
    }

    protected void setManaRequired(float manaRequired) {
        this.manaRequired = manaRequired;

        for (int slot = 0; slot < MANA_HOLDERS; slot++) {
            inventory.getStackInSlot(slot).getCapability(ManaProvider.MANA).ifPresent(mana -> {
                addMana(mana.removeMana(this.manaRequired));
            });
        }
    }

    public void addMana(float mana) {
        manaRequired -= mana;
        if (hasEnoughMana() && enchantedItem != null) {
            world.addEntity(new ItemEntity(world, pos.getX(), pos.up().getY(), pos.getZ(), enchantedItem));
            ModPacketHandler.sendToAllClients("ritual_finished", getPos());
            enchantedItem = null;
        }
    }

    public boolean hasEnoughMana() {
        return manaRequired <= 0;
    }
}
