package com.icsd.demo.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
@Table(name="topic")
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name="topic")
public class TopicModel implements Serializable{
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    //@javax.persistence.Id
    @XmlAttribute(name="id")
    @javax.persistence.Id
    private int id;
    
    
    @Column(nullable=false)
    @XmlElement(name = "onoma")
    private String onoma;
    
    @Column(nullable=true)
    @XmlElement(name = "imer_dimiourgias")
    private LocalDateTime imer_dimiourgias;

    @Column(nullable=false)
    @XmlElement(name = "status")
    private TopicEnum status = TopicEnum.CREATED;
   
    @JsonBackReference
    @ManyToOne(cascade=CascadeType.MERGE)
    @JoinColumn(name="parentTopic")
    private TopicModel parentTopic;

    @OneToMany(mappedBy="parentTopic")
    private Set<TopicModel> subtopics = new HashSet<>();

    
}
