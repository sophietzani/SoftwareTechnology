
package com.icsd.demo.repositories;

import com.icsd.demo.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<UserModel,Integer>{
 
    
    UserModel findByUsernameAndPassword(String username, String password);
    
}
