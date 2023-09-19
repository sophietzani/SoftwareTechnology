
package com.icsd.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="user")
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "users")
public class UserModel implements Serializable{
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @XmlAttribute(name="id")
    @javax.persistence.Id
    private int id;
    
    @Column(nullable=false)
    @XmlElement(name = "username")
    private String username;
    
    @Column(nullable=false)
    @XmlElement(name = "password")
    private String password;
    
     @Column(nullable=false)
    @XmlElement(name = "name")
    private String name;
     
    @Column(nullable=false)
    @XmlElement(name = "surname")
    private String surname;
    
    @Column(nullable=false)
    @XmlElement(name="role")
    private String role;
    
    @Column(nullable=false, columnDefinition = "boolean default false")
    @XmlElement(name="enabled")
    private boolean enabled;
    
}

