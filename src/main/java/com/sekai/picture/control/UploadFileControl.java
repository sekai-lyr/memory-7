package com.sekai.picture.control;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Controller
public class UploadFileControl {

    private final List<Path> uploadRoots = List.of(
            Path.of("uploads").toAbsolutePath().normalize(),
            Path.of(System.getProperty("user.dir"), "uploads").toAbsolutePath().normalize(),
            Path.of("B:/Sekai/Sekai/sekai/memory-7/uploads").toAbsolutePath().normalize()
    );

    @GetMapping("/uploads/{*filePath}")
    public ResponseEntity<Resource> uploadFile(@PathVariable String filePath) throws IOException {
        return serve("", filePath);
    }

    @GetMapping("/wallpaper-image/{*filePath}")
    public ResponseEntity<Resource> wallpaperImage(@PathVariable String filePath) throws IOException {
        return serve("wallpapers", filePath);
    }

    private ResponseEntity<Resource> serve(String rootChild, String filePath) throws IOException {
        if (filePath == null || filePath.isBlank()) {
            return ResponseEntity.notFound().build();
        }

        String relativePath = filePath.startsWith("/") ? filePath.substring(1) : filePath;
        for (Path uploadRoot : uploadRoots) {
            Path normalizedRoot = rootChild.isBlank()
                    ? uploadRoot.toAbsolutePath().normalize()
                    : uploadRoot.resolve(rootChild).toAbsolutePath().normalize();
            Path file = normalizedRoot.resolve(relativePath).normalize();
            if (file.startsWith(normalizedRoot) && Files.isRegularFile(file)) {
                String contentType = Files.probeContentType(file);
                if (contentType == null) {
                    contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
                }

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .body(new UrlResource(file.toUri()));
            }
        }

        return ResponseEntity.notFound().build();
    }
}

