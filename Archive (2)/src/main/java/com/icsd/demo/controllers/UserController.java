package com.icsd.demo.controllers;

import com.icsd.demo.models.TokenModel;
import com.icsd.demo.repositories.UserRepository;
import com.icsd.demo.models.UserModel;
import com.icsd.demo.repositories.TokenRepository;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import org.springframework.beans.factory.annotation.Autowired;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "users")
@Path("/users")
public class UserController {

   
    @Autowired
    UserRepository userrepository;
    
    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private TokenService tokenService;


    @POST
    @Consumes("application/json")
    @PermitAll
    @Path("/register")
    public Response createUser(UserModel user) throws URISyntaxException {
        //check if all the required fields are in the json object
        if (user.getUsername() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Username missing")
                    .build();
        }
        if (user.getPassword()== null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Password missing")
                    .build();
        }
        if (user.getRole()== null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Role missing")
                    .build();
        }
        if (user.getName()== null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Name missing")
                    .build();
        }
        if (user.getSurname()== null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Surname missing")
                    .build();
        }

        if (!(user.getRole().equals("epimelitis") || user.getRole().equals("dimosiografos"))) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Role not acceptable")
                    .build();
        }

        //apothikeusi tou xristi sti vasi   
        userrepository.save(user);

        return Response.status(Response.Status.CREATED)
                .entity("User Created!")
                .build();
    }
    
    @GET
    @RolesAllowed("admin")
    //@PermitAll
    @Produces("application/json")
    public List<UserModel> retrieveUsers() {
        ArrayList<UserModel> users = new ArrayList<>(userrepository.findAll());
        for (UserModel um : users) {
             um.setPassword("hidden");
        }
        return users;
    }
    
    
    @GET
    @Path("/login")
    @PermitAll
    @Produces("application/json")
    public Response loginUser(UserModel user) {
        //anaktisi xristwn tis vasis
        List<UserModel> optionalUser = userrepository.findAll();
        
        for (UserModel us : optionalUser) {
            if (us.getUsername().equals(user.getUsername())) {
                if (us.getPassword().equals(user.getPassword())) {
                    
                    //dimiourgia kai epistrofi tou token ston xristi
                    TokenModel newtoken = tokenService.generateToken(us.getUsername(), us.getPassword());
                    
                    return Response.status(Response.Status.OK)
                            .entity(newtoken)
                            .build();
                }
                else {
                    //ean vrethei o xristis alla me allo kwdiko
                    return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Wrong Password")
                        .build();
                }
            }
        }

        return Response.status(Response.Status.BAD_REQUEST)
                .entity("User not found")
                .build();

    }

    //euresi tou xristi me to id tou
    @GET
    @Path("/{id}")
    @RolesAllowed("admin")
    @Produces("application/json")
    public Response getUserById(@PathParam("id") int id) throws URISyntaxException {
        Optional<UserModel> optionalUser = userrepository.findById(id);

        UserModel fUser;
        //apoktisi tou xristi apo tin vasi kai elegxos ean mporei na anaktithei
        if (optionalUser.isPresent()) {
            fUser = optionalUser.get();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("User not found")
                    .build();
        }

        return Response.status(200).entity(fUser).build();
    }



}
