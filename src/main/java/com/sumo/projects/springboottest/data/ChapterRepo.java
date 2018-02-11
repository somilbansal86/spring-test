package com.sumo.projects.springboottest.data;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;


public interface ChapterRepo extends ReactiveCrudRepository<Chapter, String>{
}
