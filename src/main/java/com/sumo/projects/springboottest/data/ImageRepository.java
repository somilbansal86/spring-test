package com.sumo.projects.springboottest.data;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;


public interface ImageRepository extends ReactiveCrudRepository<Image, String>{

    Mono<Image> findByName(String name);

}
