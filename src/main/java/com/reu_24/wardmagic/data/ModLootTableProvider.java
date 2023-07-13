package com.reu_24.wardmagic.data;

import com.reu_24.wardmagic.WardMagic;
import com.reu_24.wardmagic.util.ReflectionHelper;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class ModLootTableProvider implements IDataProvider {
    private DataGenerator gen;

    public ModLootTableProvider(DataGenerator gen) {
        this.gen = gen;
    }

    @Override
    public void act(DirectoryCache cache) {
        ReflectionHelper.forEachBlock((blockData, field) -> {
            switch (blockData.lootTableType()) {
                case SIMPLE:
                    addLootTable(cache, getRealFileName("blocks/" + blockData.id()), dropToJson(blockData.lootId()));
                    break;
                case SELF:
                    addLootTable(cache, getRealFileName("blocks/" + blockData.id()), dropToJson(WardMagic.MOD_ID + ":" + blockData.id()));
                    break;
                case LEAVE:
                    addLootTable(cache, getRealFileName("blocks/" + blockData.id()), leaveToJson(WardMagic.MOD_ID + ":" + blockData.id(), WardMagic.MOD_ID + ":" + blockData.lootId()));
                    break;
            }
            return null;
        });
    }

    protected void addLootTable(DirectoryCache cache, Path fileName, String json) {
        String hash = IDataProvider.HASH_FUNCTION.hashUnencodedChars(json).toString();
        if (!Objects.equals(cache.getPreviousHash(fileName), hash) || !Files.exists(fileName)) {
            try {
                Files.createDirectories(fileName.getParent());

                try (BufferedWriter bufferedwriter = Files.newBufferedWriter(fileName)) {
                    bufferedwriter.write(json);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        cache.recordHash(fileName, hash);
    }

    protected Path getRealFileName(String fileName) {
        return gen.getOutputFolder().resolve("data/" + WardMagic.MOD_ID + "/loot_tables/" + fileName + ".json");
    }

    protected String dropToJson(String drop) {
        return String.format("{\n" +
                "  \"type\": \"minecraft:block\",\n" +
                "  \"pools\": [\n" +
                "    {\n" +
                "      \"rolls\": 1,\n" +
                "      \"entries\": [\n" +
                "        {\n" +
                "          \"type\": \"minecraft:item\",\n" +
                "          \"functions\": [\n" +
                "            {\n" +
                "              \"function\": \"minecraft:copy_name\",\n" +
                "              \"source\": \"block_entity\"\n" +
                "            }\n" +
                "          ],\n" +
                "          \"name\": \"%s\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"conditions\": [\n" +
                "        {\n" +
                "          \"condition\": \"minecraft:survives_explosion\"\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}", drop);
    }

    protected String leaveToJson(String leaves, String sapling) {
        return String.format("{\n" +
                "  \"type\": \"minecraft:block\",\n" +
                "  \"pools\": [\n" +
                "    {\n" +
                "      \"rolls\": 1,\n" +
                "      \"entries\": [\n" +
                "        {\n" +
                "          \"type\": \"minecraft:alternatives\",\n" +
                "          \"children\": [\n" +
                "            {\n" +
                "              \"type\": \"minecraft:item\",\n" +
                "              \"conditions\": [\n" +
                "                {\n" +
                "                  \"condition\": \"minecraft:alternative\",\n" +
                "                  \"terms\": [\n" +
                "                    {\n" +
                "                      \"condition\": \"minecraft:match_tool\",\n" +
                "                      \"predicate\": {\n" +
                "                        \"item\": \"minecraft:shears\"\n" +
                "                      }\n" +
                "                    },\n" +
                "                    {\n" +
                "                      \"condition\": \"minecraft:match_tool\",\n" +
                "                      \"predicate\": {\n" +
                "                        \"enchantments\": [\n" +
                "                          {\n" +
                "                            \"enchantment\": \"minecraft:silk_touch\",\n" +
                "                            \"levels\": {\n" +
                "                              \"min\": 1\n" +
                "                            }\n" +
                "                          }\n" +
                "                        ]\n" +
                "                      }\n" +
                "                    }\n" +
                "                  ]\n" +
                "                }\n" +
                "              ],\n" +
                "              \"name\": \"%s\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"type\": \"minecraft:item\",\n" +
                "              \"conditions\": [\n" +
                "                {\n" +
                "                  \"condition\": \"minecraft:survives_explosion\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"condition\": \"minecraft:table_bonus\",\n" +
                "                  \"enchantment\": \"minecraft:fortune\",\n" +
                "                  \"chances\": [\n" +
                "                    0.05,\n" +
                "                    0.0625,\n" +
                "                    0.083333336,\n" +
                "                    0.1\n" +
                "                  ]\n" +
                "                }\n" +
                "              ],\n" +
                "              \"name\": \"%s\"\n" +
                "            }\n" +
                "          ]\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"rolls\": 1,\n" +
                "      \"entries\": [\n" +
                "        {\n" +
                "          \"type\": \"minecraft:item\",\n" +
                "          \"conditions\": [\n" +
                "            {\n" +
                "              \"condition\": \"minecraft:table_bonus\",\n" +
                "              \"enchantment\": \"minecraft:fortune\",\n" +
                "              \"chances\": [\n" +
                "                0.02,\n" +
                "                0.022222223,\n" +
                "                0.025,\n" +
                "                0.033333335,\n" +
                "                0.1\n" +
                "              ]\n" +
                "            }\n" +
                "          ],\n" +
                "          \"functions\": [\n" +
                "            {\n" +
                "              \"function\": \"minecraft:set_count\",\n" +
                "              \"count\": {\n" +
                "                \"min\": 1.0,\n" +
                "                \"max\": 2.0,\n" +
                "                \"type\": \"minecraft:uniform\"\n" +
                "              }\n" +
                "            },\n" +
                "            {\n" +
                "              \"function\": \"minecraft:explosion_decay\"\n" +
                "            }\n" +
                "          ],\n" +
                "          \"name\": \"minecraft:stick\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"conditions\": [\n" +
                "        {\n" +
                "          \"condition\": \"minecraft:inverted\",\n" +
                "          \"term\": {\n" +
                "            \"condition\": \"minecraft:alternative\",\n" +
                "            \"terms\": [\n" +
                "              {\n" +
                "                \"condition\": \"minecraft:match_tool\",\n" +
                "                \"predicate\": {\n" +
                "                  \"item\": \"minecraft:shears\"\n" +
                "                }\n" +
                "              },\n" +
                "              {\n" +
                "                \"condition\": \"minecraft:match_tool\",\n" +
                "                \"predicate\": {\n" +
                "                  \"enchantments\": [\n" +
                "                    {\n" +
                "                      \"enchantment\": \"minecraft:silk_touch\",\n" +
                "                      \"levels\": {\n" +
                "                        \"min\": 1\n" +
                "                      }\n" +
                "                    }\n" +
                "                  ]\n" +
                "                }\n" +
                "              }\n" +
                "            ]\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"rolls\": 1,\n" +
                "      \"entries\": [\n" +
                "        {\n" +
                "          \"type\": \"minecraft:item\",\n" +
                "          \"conditions\": [\n" +
                "            {\n" +
                "              \"condition\": \"minecraft:survives_explosion\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"condition\": \"minecraft:table_bonus\",\n" +
                "              \"enchantment\": \"minecraft:fortune\",\n" +
                "              \"chances\": [\n" +
                "                0.005,\n" +
                "                0.0055555557,\n" +
                "                0.00625,\n" +
                "                0.008333334,\n" +
                "                0.025\n" +
                "              ]\n" +
                "            }\n" +
                "          ],\n" +
                "          \"name\": \"minecraft:apple\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"conditions\": [\n" +
                "        {\n" +
                "          \"condition\": \"minecraft:inverted\",\n" +
                "          \"term\": {\n" +
                "            \"condition\": \"minecraft:alternative\",\n" +
                "            \"terms\": [\n" +
                "              {\n" +
                "                \"condition\": \"minecraft:match_tool\",\n" +
                "                \"predicate\": {\n" +
                "                  \"item\": \"minecraft:shears\"\n" +
                "                }\n" +
                "              },\n" +
                "              {\n" +
                "                \"condition\": \"minecraft:match_tool\",\n" +
                "                \"predicate\": {\n" +
                "                  \"enchantments\": [\n" +
                "                    {\n" +
                "                      \"enchantment\": \"minecraft:silk_touch\",\n" +
                "                      \"levels\": {\n" +
                "                        \"min\": 1\n" +
                "                      }\n" +
                "                    }\n" +
                "                  ]\n" +
                "                }\n" +
                "              }\n" +
                "            ]\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}", leaves, sapling);
    }

    @Override
    public String getName() {
        return "ModLootTables";
    }
}
