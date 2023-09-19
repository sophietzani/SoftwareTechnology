/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.icsd.demo.controllers;

import static com.fasterxml.jackson.databind.type.LogicalType.DateTime;
import com.icsd.demo.models.NewsEnum;
import com.icsd.demo.models.NewsModel;
import com.icsd.demo.models.TopicEnum;
import com.icsd.demo.models.TopicModel;
import com.icsd.demo.models.UserModel;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
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
import com.icsd.demo.repositories.TopicRepository;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "topic")
@Path("/topic")
public class TopicController {
    
    @Autowired
    TopicRepository topicrepo;
    
    @POST
    @Consumes("application/json")   
    //@RolesAllowed({"dimosiografos","epimelitis", "admin"})
    @PermitAll
    public Response createThema(TopicModel topic) throws URISyntaxException {
        if (topic.getOnoma() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Onoma missing")
                    .build();
        }
        
        //System.out.println(topic.toString());
        
        if (topic.getParentTopic()!= null && topic.getParentTopic().getId() != 0) {
            TopicModel parentTopic = topicrepo.findById(topic.getParentTopic().getId()).orElse(null);
            if (parentTopic != null) {
                topic.setParentTopic(parentTopic);
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Wrong father topic")
                    .build();
            }
        }
        else {
            System.out.println("empty parent topic");
        }
        
        //trexousa imerominia kai wra
        topic.setImer_dimiourgias(LocalDateTime.now());
        
        //save the topic to the database
        topicrepo.save(topic);
        
        return Response.status(Response.Status.CREATED)
                .entity("Topic created!")
                .build();
    }
    
    @GET
    //@RolesAllowed({"epimelitis", "admin"})
    @PermitAll
    @Produces("application/json")
    public List<TopicModel> retrieveTopicss() {
        ArrayList<TopicModel> topics = new ArrayList<>(topicrepo.findAll());
        return topics;
    }
    
    @PATCH
    @Path("/approve/{id}")
    @RolesAllowed({"epimelitis", "admin"})
    @Produces("application/json")
    public Response approveTopic(@PathParam("id") int id) throws URISyntaxException {
        Optional<TopicModel> topicretrieved = topicrepo.findById(id);

        TopicModel tmodel = new TopicModel();
       
        //retrieve the news
        if (topicretrieved.isPresent()) {
            tmodel = topicretrieved.get();
            //only if it is created
            if (tmodel.getStatus() == TopicEnum.CREATED) {
                tmodel.setStatus(TopicEnum.APPROVED);
            }
            else{
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Topic not created")
                    .build();
            }
            //apothikeuei tis allages
            topicrepo.save(tmodel);

            return Response.status(Response.Status.OK).entity("Topic approved").build();

        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Topic not found")
                    .build();
        }

    }
    
    
}
