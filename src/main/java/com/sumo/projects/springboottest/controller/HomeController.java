package com.sumo.projects.springboottest.controller;


import com.sumo.projects.springboottest.data.ImageService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

@RestController
public class HomeController {

    public static final String BASE_PATH = "/images";
    public static final String FILE_NAME = "{filename:.+}";


    private final ImageService imageService;


    public HomeController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping(value = BASE_PATH + "/" + FILE_NAME + "/raw", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public Mono<ResponseEntity<?>> oneRawImage(@PathVariable String fileName){
        return imageService.findOneImage(fileName).map(resource -> {
            try {
                return ResponseEntity.ok().contentLength(resource.contentLength())
                        .body(new InputStreamResource(resource.getInputStream()));
            } catch (IOException e) {
                return ResponseEntity.badRequest().body("Could not find filename" + fileName + " +>" + e.getMessage());
            }
        });
    }

    @PostMapping(value = BASE_PATH)
    public Mono<String> createFile(@RequestParam(name = "file")Flux<FilePart> files){
        return imageService.createImage(files).then(Mono.just("redirect:/"));
    }

    @DeleteMapping(BASE_PATH + "/" + FILE_NAME)
    public Mono<String> deleteFile(@PathVariable String file){
        return imageService.deleteImage(file).then(Mono.just("redirect:/"));
    }

    @GetMapping("/")
    public Mono<String> indeModel(Model model){
        model.addAttribute("images" , imageService.findAllImages());
        return Mono.just("index");
    }

}
