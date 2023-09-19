
package com.icsd.demo.repositories;

import com.icsd.demo.models.NewsEnum;
import com.icsd.demo.models.NewsModel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface NewsRepository extends JpaRepository<NewsModel,Integer>{
 
    NewsModel findByTitlos(String titlos);
    NewsModel findByPeriexomeno(String periexomeno);
    ArrayList<NewsModel> findByState(NewsEnum state);
    ArrayList<NewsModel> findByImerDimosieusis(Date imerominiaDimosieusis);
    
    @Query("FROM NewsModel u JOIN FETCH u.topic")
    List<NewsModel> getAllNewsAndFetchNewsTopic();
    
}
