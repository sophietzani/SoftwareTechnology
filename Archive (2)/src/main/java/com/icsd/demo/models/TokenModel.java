package com.icsd.demo.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="token")
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "users")
public class TokenModel {
    @Id
    @XmlElement(name="code")
    private String code;

    @Column(nullable=false)
    @XmlElement(name="username")
    private String username; // User to whom it belongs
    
    @Column(nullable=false)
    @XmlElement(name="password")
    private String password; // User to whom it belongs
    
    @Column(nullable=false)
    @XmlElement(name="creationtime")
    private LocalDateTime creationtime;
    
    @Column(nullable=false)
    @XmlElement(name="expiration")
    private LocalDateTime expiration;

    
}
