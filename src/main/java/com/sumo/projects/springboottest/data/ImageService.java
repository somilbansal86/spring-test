package com.sumo.projects.springboottest.data;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageService {
    private static String UPLOAD_ROOT = "upload-dir";
    private final ResourceLoader resourceLoader;

    public ImageService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Bean
    CommandLineRunner setUp(){
        return args -> {
            FileSystemUtils.deleteRecursively(new File(UPLOAD_ROOT));
            Files.createDirectory(Paths.get(UPLOAD_ROOT));
            FileCopyUtils.copy("Test File", new FileWriter(UPLOAD_ROOT + "/learning.jpg"));
            FileCopyUtils.copy("Test File2", new FileWriter(UPLOAD_ROOT + "/learning2.jpg"));
            FileCopyUtils.copy("Test File3", new FileWriter(UPLOAD_ROOT + "/learning3.jpg"));
            FileCopyUtils.copy("Test File4", new FileWriter(UPLOAD_ROOT + "/learning4.jpg"));
        };
    }

    public Flux<Image> findAllImages(){
        try{
            return Flux.fromIterable(Files.newDirectoryStream(Paths.get(UPLOAD_ROOT))).map(path ->
                new Image(String.valueOf(path.hashCode()), path.getFileName().toString()));
        } catch (IOException e) {
            return Flux.empty();
        }
    }

    public Mono<Resource> findOneImage(String fileName){
        return Mono.fromSupplier(() -> resourceLoader.getResource("file:" + UPLOAD_ROOT + "/" + fileName));
    }

    public Mono<Void> createImage(Flux<FilePart> files){
        return files.flatMap(file -> file.transferTo(Paths.get((UPLOAD_ROOT), file.filename()).toFile())).then();
    }

    public Mono<Void> deleteImage(String fileName) {
        return Mono.fromRunnable(() -> {
            try {
                 Files.deleteIfExists(Paths.get(UPLOAD_ROOT, fileName));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
