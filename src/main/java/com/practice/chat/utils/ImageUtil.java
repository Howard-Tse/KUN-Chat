package com.practice.chat.utils;

import javafx.scene.image.Image;

import java.io.*;

public class ImageUtil {
    public static Image binaryStreamToImage(byte[] binary) {
        Image image = null;
        if (binary != null) {
            DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(binary));
            image = new Image(dataInputStream);
        }
        return image;
    }

    public static byte[] fileToBinaryStream(File file) {
        byte[] image = null;
        if (file.exists()) {
            try (FileInputStream inputStream = new FileInputStream(file)) {
                image = inputStream.readAllBytes();
            } catch (IOException ignored) {
            }
        }
        return image;
    }
}
