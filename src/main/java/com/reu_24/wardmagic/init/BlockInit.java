package com.reu_24.wardmagic.init;

import com.reu_24.wardmagic.WardMagic;
import com.reu_24.wardmagic.data.Annotations;
import com.reu_24.wardmagic.itemgroup.WardMagicItemGroup;
import com.reu_24.wardmagic.object.block.*;
import com.reu_24.wardmagic.object.item.FurnaceBurnableBlockItem;
import com.reu_24.wardmagic.util.ClientEventBusSubscriber;
import com.reu_24.wardmagic.world.features.MagicalTree;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Direction;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class BlockInit {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, WardMagic.MOD_ID);

    @ClientEventBusSubscriber.Cutout
    @Annotations.BlockData(name = "Ward Block", id = "ward_block", lootTableType = Annotations.LootTableType.NONE, generateItem = false, blockState = Annotations.BlockStateType.WARD)
    public static final RegistryObject<Block> WARD_BLOCK = BLOCKS.register("ward_block",
            () -> new WardBlock(AbstractBlock.Properties.create(Material.SNOW).doesNotBlockMovement().zeroHardnessAndResistance()));

    @Annotations.BlockData(name = "Magical Log", id = "magical_log", blockState = Annotations.BlockStateType.LOG)
    public static final RegistryObject<Block> MAGICAL_LOG = registerBurnable("magical_log",
            () -> new RotatedPillarBlock(AbstractBlock.Properties.create(Material.WOOD, (state) -> state.get(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? MaterialColor.WOOD : MaterialColor.OBSIDIAN).hardnessAndResistance(2.0F).sound(SoundType.WOOD)),
            300);

    @Annotations.BlockData(name = "Magical Planks", id = "magical_planks")
    public static final RegistryObject<Block> MAGICAL_PLANKS = registerBurnable("magical_planks",
            () -> new Block(Block.Properties.from(Blocks.OAK_PLANKS)), 300);

    @Annotations.BlockData(name = "Magical Leaves", id = "magical_leaves", lootTableType = Annotations.LootTableType.LEAVE, lootId = "magical_sapling")
    public static final RegistryObject<Block> MAGICAL_LEAVES = register("magical_leaves",
            () -> new MagicalLeaveBlock(Block.Properties.from(Blocks.OAK_LEAVES)));

    @ClientEventBusSubscriber.Cutout
    @Annotations.BlockData(name = "Magical Sapling", id = "magical_sapling", blockState = Annotations.BlockStateType.CROSS, generateItem = false)
    public static final RegistryObject<Block> MAGICAL_SAPLING = register("magical_sapling",
            () -> new SaplingBlock(new MagicalTree(), Block.Properties.from(Blocks.OAK_SAPLING)));

    @Annotations.BlockData(name = "Crack Stone", id = "crack_stone", blockState = Annotations.BlockStateType.CRACK, generateItem = false)
    public static final RegistryObject<Block> CRACK_STONE = register("crack_stone",
            () -> new CrackStoneBlock(Block.Properties.from(Blocks.STONE_BRICKS)));

    public static final RegistryObject<Block> WARD_ALTAR = register("ward_altar",
            () -> new WardAltarBlock(Block.Properties.from(Blocks.ANVIL)));

    @Annotations.BlockData(name = "Deadly Matter", id = "deadly_matter", blockState = Annotations.BlockStateType.SMALL, generateItem = false, lootTableType = Annotations.LootTableType.NONE)
    public static final RegistryObject<Block> DEADLY_MATTER = BLOCKS.register("deadly_matter",
            () -> new DeadlyMatterBlock(AbstractBlock.Properties.create(Material.SNOW)));

    // ****************************************************************************************************************

    protected static RegistryObject<Block> register(String name, Supplier<Block> block) {
        RegistryObject<Block> result = BLOCKS.register(name, block);
        ItemInit.ITEMS.register(name, () -> new BlockItem(result.get(), new Item.Properties().group(WardMagicItemGroup.INSTANCE)));
        return result;
    }

    protected static RegistryObject<Block> registerBurnable(String name, Supplier<Block> block, int burnTime) {
        RegistryObject<Block> result = BLOCKS.register(name, block);
        ItemInit.ITEMS.register(name, () -> new FurnaceBurnableBlockItem(result.get(), new Item.Properties().group(WardMagicItemGroup.INSTANCE), burnTime));
        return result;
    }
}
