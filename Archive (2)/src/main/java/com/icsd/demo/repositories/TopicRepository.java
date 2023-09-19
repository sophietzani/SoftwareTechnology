package com.icsd.demo.repositories;

import com.icsd.demo.models.TopicModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<TopicModel,Integer>{
    
}
