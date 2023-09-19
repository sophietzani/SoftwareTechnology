/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.icsd.demo.controllers;

import com.icsd.demo.repositories.NewsRepository;
import com.icsd.demo.models.NewsEnum;
import com.icsd.demo.models.NewsModel;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "news")
@Path("/news")
public class NewsController {

    @Autowired
    NewsRepository newsrepo;

    @GET
    @PermitAll
    @Produces("application/json")
    public ResponseEntity<Object> getAllNews() {
        List<NewsModel> dbNews = newsrepo.getAllNewsAndFetchNewsTopic();
        if (dbNews.isEmpty()) {
            return new ResponseEntity<>("Empty list of news", HttpStatus.NOT_FOUND);
        }
        
        return new ResponseEntity<>(dbNews, HttpStatus.OK);
    }

    @GET
    @PermitAll
    @Path("/{id}")
    @Produces("application/json")
    public ResponseEntity<Object> getIDNews(@PathParam("id") int id) {
        
        if (id < 0 ){
            return new ResponseEntity<>("Negative id for News", HttpStatus.NOT_FOUND);
        }
        
        Optional<NewsModel> optionalNews = newsrepo.findById(id);

        NewsModel fetchedNews = new NewsModel();
        
        if (!optionalNews.isPresent()) {
            fetchedNews = optionalNews.get();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
  return new ResponseEntity<>(fetchedNews, HttpStatus.OK);
      
    }

    @POST
    @Consumes("application/json")
    @RolesAllowed({"dimosiografos", "epimelitis", "ADMIN"})
    public Response createNews(NewsModel news) throws URISyntaxException {
        if (news.getTitlos() == null || news.getPeriexomeno() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Title or content missing")
                    .build();
        }

        news.setImerDimiourgias(LocalDateTime.now());
        news.setImerDimosieusis(LocalDateTime.now());
        news.setState(NewsEnum.CREATED);

        //dimiourgia tis eidisis 
        newsrepo.save(news);

        return Response.status(Response.Status.CREATED)
                .entity("News created!")
                .build();
    }

    @PATCH
    @Path("/publish/{id}")
    @RolesAllowed({"dimosiografos", "epimelitis", "admin"})
    @Produces("application/json")
    public Response publishNews(@PathParam("id") int id) throws URISyntaxException {
        Optional<NewsModel> optionalNews = newsrepo.findById(id);

        NewsModel fetchedNews = new NewsModel();
        //apoktisi tou xristi apo tin vasi kai elegxos ean mporei na anaktithei
        if (optionalNews.isPresent()) {
            fetchedNews = optionalNews.get();
            if (fetchedNews.getState() == NewsEnum.SUBMITED) {
                fetchedNews.setState(NewsEnum.PUBLISHED);
               
                newsrepo.save(fetchedNews);

                return Response.status(Response.Status.OK).entity("OK").build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("News not SUBMITED")
                        .build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("News not found")
                    .build();
        }

    }
    
    @DELETE
    @Path("/reject/{id}")
    @RolesAllowed({"epimelitis", "admin"})
    @Produces("application/json")
    public Response reject(@PathParam("id") int id) throws URISyntaxException {
        
        if (id < 0 ){
           return Response.status(Response.Status.OK).entity("Wrong id for news").build();
        }
        
        Optional<NewsModel> optionalNews = newsrepo.findById(id);

        NewsModel fetchedNews = new NewsModel();
        
        if (optionalNews.isPresent()) {
            fetchedNews = optionalNews.get();
            if (fetchedNews.getState() == NewsEnum.SUBMITED) {
                 newsrepo.delete(fetchedNews);
            }else{
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity("News not in submited mode")
                    .build();
            }
           
            newsrepo.save(fetchedNews);
            return Response.status(Response.Status.OK).entity("OK").build();

        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("News not found")
                    .build();
        }

    }
    
    
    @PATCH
    @Path("/approve/{id}")
    @RolesAllowed({"epimelitis", "admin"})
    @Produces("application/json")
    public Response approve(@PathParam("id") int id) throws URISyntaxException {
        
        if (id < 0 ){
           return Response.status(Response.Status.OK).entity("Wrong id for news").build();
        }
        
        Optional<NewsModel> optionalNews = newsrepo.findById(id);

        NewsModel fetchedNews = new NewsModel();
        
        if (optionalNews.isPresent()) {
            fetchedNews = optionalNews.get();
            if (fetchedNews.getState() == NewsEnum.SUBMITED) {
                fetchedNews.setState(NewsEnum.APPROVED);
            }else{
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity("News not in submited mode")
                    .build();
            }
           
            newsrepo.save(fetchedNews);
            return Response.status(Response.Status.OK).entity("OK").build();

        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("News not found")
                    .build();
        }

    }

    @PATCH
    @Path("/submit/{id}")
    @RolesAllowed({"epimelitis", "admin"})
    @Produces("application/json")
    public Response submitNews(@PathParam("id") int id) throws URISyntaxException {
        
        if (id < 0 ){
           return Response.status(Response.Status.OK).entity("Wrong id for news").build();
        }
        
        Optional<NewsModel> optionalNews = newsrepo.findById(id);

        NewsModel fetchedNews = new NewsModel();
        //apoktisi tou xristi apo tin vasi kai elegxos ean mporei na anaktithei
        if (optionalNews.isPresent()) {
            fetchedNews = optionalNews.get();
            if (fetchedNews.getState() == NewsEnum.CREATED) {
                fetchedNews.setState(NewsEnum.SUBMITED);
            }else{
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity("News not in created mode")
                    .build();
            }
            //apothikeuei tis allages
            newsrepo.save(fetchedNews);
            return Response.status(Response.Status.OK).entity("OK").build();

        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("News not found")
                    .build();
        }

    }
    
    
}
