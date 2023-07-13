package com.reu_24.wardmagic.data.client;

import com.reu_24.wardmagic.WardMagic;
import com.reu_24.wardmagic.util.ReflectionHelper;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;

public class ModParticleProvider implements IDataProvider {
    private DataGenerator gen;

    public ModParticleProvider(DataGenerator gen) {
        this.gen = gen;
    }

    @Override
    public void act(DirectoryCache cache) {
        ReflectionHelper.forEachParticle((particleData, field) -> {
            addParticle(cache, getRealFileName(particleData.id()), texturesToJson(particleData.textures()));
            return null;
        });
    }

    protected void addParticle(DirectoryCache cache, Path fileName, String json) {
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

    protected String texturesToJson(String[] textures) {
        StringBuilder texturesStr = new StringBuilder("[");
        for (String texture : textures) {
            texturesStr.append("\n      \"").append(texture).append("\",");
        }
        String texturesRes = texturesStr.toString();
        texturesRes = texturesRes.substring(0, texturesRes.length() - 1) + "\n  ]";

        return String.format("{\n" +
                "  \"textures\": %s\n" +
                "}", texturesRes);
    }

    protected Path getRealFileName(String fileName) {
        return gen.getOutputFolder().resolve("assets/" + WardMagic.MOD_ID + "/particles/" + fileName + ".json");
    }

    @Override
    public String getName() {
        return "ModParticle";
    }
}
