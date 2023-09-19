
package com.icsd.demo.repositories;

import com.icsd.demo.models.CommentModel;
import com.icsd.demo.models.CommentEnum;
import com.icsd.demo.models.NewsEnum;
import com.icsd.demo.models.NewsModel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface CommentsRepository extends JpaRepository<CommentModel,Integer>{
 
    List<CommentModel> findByPeriexomeno(String periexomeno);
    List<CommentModel>  findByDimiourgos(String creator);
    ArrayList<NewsModel> findByCommentstate(CommentEnum state);
    ArrayList<NewsModel> findByImerDimiourgias(Date imerominiaDimiourgias);
    
   
}
