package com.sumo.projects.springboottest.data;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

@Configuration
public class LoadDB {

    @Bean
    CommandLineRunner init(ChapterRepo chapterRepo){
        return args -> {
            Flux.just(new Chapter("Quick repo test"),
            new Chapter("Reactive mongo Test"),
            new Chapter("lorem ipsum"))
                    .flatMap(chapterRepo::save)
                    .subscribe(System.out::println);
        };
    }
}
