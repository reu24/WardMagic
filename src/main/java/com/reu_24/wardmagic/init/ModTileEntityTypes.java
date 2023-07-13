package com.reu_24.wardmagic.init;

import com.reu_24.wardmagic.WardMagic;
import com.reu_24.wardmagic.tileentity.CrackStoneTileEntity;
import com.reu_24.wardmagic.tileentity.DeadlyMatterTileEntity;
import com.reu_24.wardmagic.tileentity.WardAltarTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntityTypes {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPE = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, WardMagic.MOD_ID);

    public static final RegistryObject<TileEntityType<CrackStoneTileEntity>> CRACK_STONE = TILE_ENTITY_TYPE.register("crack_stone", () -> TileEntityType.Builder.create(CrackStoneTileEntity::new, BlockInit.CRACK_STONE.get()).build(null));
    public static final RegistryObject<TileEntityType<WardAltarTileEntity>> WARD_ALTAR = TILE_ENTITY_TYPE.register("ward_altar", () -> TileEntityType.Builder.create(WardAltarTileEntity::new, BlockInit.WARD_ALTAR.get()).build(null));
    public static final RegistryObject<TileEntityType<DeadlyMatterTileEntity>> DEADLY_MATTER = TILE_ENTITY_TYPE.register("deadly_matter", () -> TileEntityType.Builder.create(DeadlyMatterTileEntity::new, BlockInit.DEADLY_MATTER.get()).build(null));
}
