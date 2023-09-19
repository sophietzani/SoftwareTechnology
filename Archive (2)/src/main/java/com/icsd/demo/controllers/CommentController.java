package com.icsd.demo.controllers;

import com.icsd.demo.models.CommentEnum;
import com.icsd.demo.models.CommentModel;
import com.icsd.demo.models.NewsModel;
import com.icsd.demo.models.NewsEnum;
import com.icsd.demo.models.TopicEnum;
import com.icsd.demo.models.TopicModel;
import com.icsd.demo.repositories.CommentsRepository;
import com.icsd.demo.repositories.NewsRepository;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import javax.ws.rs.Path;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import java.util.Optional;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.DELETE;
import javax.ws.rs.PATCH;
import javax.ws.rs.PathParam;
import org.springframework.beans.factory.annotation.Autowired;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "comments")
@Path("/comments")
public class CommentController {
    
    private static HashMap<Integer, CommentModel> MyCommentDB = new HashMap<>();
    @Autowired
    CommentsRepository commentrepo;
    @Autowired
    NewsRepository newsrepository;
    
    @GET
    @Produces("application/json")
    public List<CommentModel> getAllComments(){
        List<CommentModel> comments = new ArrayList<>(MyCommentDB.values());
        return comments;
    }
    
    @POST
    @Consumes("application/json")
    public Response createComment(CommentModel comment) throws URISyntaxException{
        if (comment.getPeriexomeno()== null){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Comment does not contain text")
                    .build();
        }
        
        Optional<NewsModel> optionalNews = newsrepository.findById(comment.getNews_id().getId());

        NewsModel fetchedNews = new NewsModel();
        //apoktisi tou xristi apo tin vasi kai elegxos ean mporei na anaktithei
        if (!optionalNews.isPresent()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("News not found")
                    .build();
        }
        
       
        //stin arxi to comment einai apla dimiourgimeno kai oxi approved 
        comment.setCommentstate(CommentEnum.CREATED);
        comment.setImerDimiourgias(new Date());
        
        comment.setId(MyCommentDB.values().size() + 1);
        //prosthetoume sto map ton neo xristi
        MyCommentDB.put(comment.getId(), comment);
        
        commentrepo.save(comment);
        
        return Response.status(Response.Status.CREATED)
                .entity("Comment was added!")
                .build();
    }
    
    @DELETE
    @Path("/reject/{id}")
    @RolesAllowed({"epimelitis", "admin"})
    @Produces("application/json")
    public Response rejectComment(@PathParam("id") int id) throws URISyntaxException {
        Optional<CommentModel> topicretrieved = commentrepo.findById(id);

        CommentModel cmodel = new CommentModel();
       
        //retrieve the news
        if (topicretrieved.isPresent()) {
            cmodel = topicretrieved.get();
            //only if it is created
            if (cmodel.getCommentstate() == CommentEnum.CREATED) {
                commentrepo.delete(cmodel);
            }
            else{
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Topic not created")
                    .build();
            }
            commentrepo.save(cmodel);

            return Response.status(Response.Status.OK).entity("Comment rejected").build();

        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Topic not found")
                    .build();
        }

    }
    
    @PATCH
    @Path("/approve/{id}")
    @RolesAllowed({"epimelitis", "admin"})
    @Produces("application/json")
    public Response approveComment(@PathParam("id") int id) throws URISyntaxException {
        Optional<CommentModel> topicretrieved = commentrepo.findById(id);

        CommentModel cmodel = new CommentModel();
       
        //retrieve the news
        if (topicretrieved.isPresent()) {
            cmodel = topicretrieved.get();
            //only if it is created
            if (cmodel.getCommentstate() == CommentEnum.CREATED) {
                cmodel.setCommentstate(CommentEnum.APPROVED);
            }
            else{
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Comment not created")
                    .build();
            }
            commentrepo.save(cmodel);

            return Response.status(Response.Status.OK).entity("Comment approved").build();

        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Topic not found")
                    .build();
        }

    }
    
    
    
    
    
    
    
}
