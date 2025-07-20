package com.pizzeria.service;

import jakarta.enterprise.context.ApplicationScoped;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.Base64;

@ApplicationScoped
public class ImageSaveService {

    private static final String IMAGE_FOLDER = "src/main/resources/META-INF/resources/figure/";

    public String saveImageFromBase64(String base64Image, String originalFileName) throws Exception {
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);

        // Gera hash para verificar se já existe
        String hash = generateHash(imageBytes);
        String extension = getFileExtension(originalFileName);
        String fileName = hash + extension;
        File imageFile = new File(IMAGE_FOLDER + fileName);


        if (!imageFile.exists()) {
            saveFile(imageFile, imageBytes);
        }


        return "/figure/" + fileName;
    }

    private String generateHash(byte[] data) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(data);
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private String getFileExtension(String fileName) {
        int index = fileName.lastIndexOf('.');
        return (index > 0) ? fileName.substring(index) : ".jpg"; // Default .jpg
    }

    private void saveFile(File file, byte[] data) throws IOException {
        // Garante que o diretório existe
        Files.createDirectories(file.getParentFile().toPath());
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(data);
        }
    }
}
