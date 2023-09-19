package com.icsd.demo;

import com.icsd.demo.controllers.TokenService;
import com.icsd.demo.models.TokenModel;
import com.icsd.demo.repositories.UserRepository;
import com.icsd.demo.models.UserModel;
import com.icsd.demo.repositories.TokenRepository;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import org.springframework.beans.factory.annotation.Autowired;

@Provider
public class SecurityFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    UserRepository userrepo;

    private static final String AUTHORIZATION_PROPERTY = "Authorization";
    private static final String AUTHENTICATION_SCHEME = "Basic";
    private Response ACCESS_FORBIDEN = Response.status(Response.Status.FORBIDDEN).build();
    private Response UNAUTHORIZED = Response.status(Response.Status.UNAUTHORIZED).build();
    private Response UNAUTHORIZED2 = Response.status(Response.Status.UNAUTHORIZED).build();
    private Response NOT_ACCEPTABLE = Response.status(Response.Status.NOT_ACCEPTABLE).build();
    
    private Response EXPIRED_TOKEN = Response.status(Response.Status.NOT_ACCEPTABLE)
                                        .entity("EXPIRED_TOKEN")
                                        .build();
    
    private Response WRONG_TOKEN = Response.status(Response.Status.NOT_ACCEPTABLE)
                                        .entity("WRONG_TOKEN")
                                        .build();

    @Override
    public void filter(ContainerRequestContext crc) throws IOException {

        Method method = resourceInfo.getResourceMethod();

        //ean den einai energi i apodoxi olwn twn xristwn
        if (!method.isAnnotationPresent(PermitAll.class)) {
            if (method.isAnnotationPresent(DenyAll.class)) {

                crc.abortWith(ACCESS_FORBIDEN);
                return;
            }
            //get request headers
            final MultivaluedMap<String, String> headers = crc.getHeaders();

            final List<String> authorization = headers.get(AUTHORIZATION_PROPERTY);

            if (authorization == null || authorization.isEmpty()) {
                crc.abortWith(UNAUTHORIZED);
                return;
            }

            //svinei to Basic authorization scheme
            final String encodedUserPassword = authorization.get(0).replaceFirst(AUTHENTICATION_SCHEME + " ", "");

           
            Optional<TokenModel> tokmod = tokenRepository.findByCode(encodedUserPassword);
            TokenModel token = null;
            if (tokmod.isPresent()) {
                token = tokmod.get();
            } else {
                crc.abortWith(Response.status(Response.Status.NOT_ACCEPTABLE)
                                        .entity("WRONG_TOKEN")
                                        .build());
                return;
            }

            if (token == null){
                crc.abortWith(Response.status(Response.Status.NOT_ACCEPTABLE)
                                        .entity("WRONG_TOKEN")
                                        .build());
                return;
            }
            if (token.getUsername() == null){
                crc.abortWith(Response.status(Response.Status.NOT_ACCEPTABLE)
                                        .entity("WRONG_TOKEN")
                                        .build());
                return;
            }
            
             boolean validation = tokenService.validateToken(encodedUserPassword);
            if (!validation){
                crc.abortWith(Response.status(Response.Status.NOT_ACCEPTABLE)
                                        .entity("EXPIRED_TOKEN")
                                        .build());
                return;
            }
            
            if (method.isAnnotationPresent(RolesAllowed.class)) {
                RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);

                Set<String> rolesSet = new HashSet<String>(Arrays.asList(rolesAnnotation.value()));

                if (!isUserAllowed(token.getUsername(),token.getPassword(), rolesSet)) {
                   crc.abortWith(Response.status(Response.Status.NOT_ACCEPTABLE)
                                        .entity("UNAUTHORIZED")
                                        .build());
                    return;
                }

            }
        }

    }

    private boolean isUserAllowed(final String username, final String password, final Set<String> roleSet) {
        boolean isAllowed = false;

        UserModel user = userrepo.findByUsernameAndPassword(username, password);

        if (user == null) {
            return false;
        }

        if (!user.getPassword().equals(password)) {
            return false;
        }

        String userRole = user.getRole();

        if (roleSet.contains(userRole)) {
            isAllowed = true;
        }

        return isAllowed;
    }
}
