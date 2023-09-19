package com.icsd.demo.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="news")
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name="news")
public class NewsModel implements Serializable{
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @javax.persistence.Id
    @XmlAttribute(name="id")
    private int id;

    @Column (nullable=false)
    @XmlElement(name = "titlos")
    private String titlos;
    
    @Column (nullable=false)
    @XmlElement(name = "periexomeno")
    private String periexomeno;
    
    @Column
    @XmlElement(name = "imerDimiourgias")
    private LocalDateTime imerDimiourgias;
    
    @Column
    @XmlElement(name = "imerDimosieusis")
    private LocalDateTime imerDimosieusis;
    
    @Column
    @XmlElement(name = "state")
    private NewsEnum state = NewsEnum.CREATED;
    
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinTable(name = "NewsTopic", joinColumns = @JoinColumn(name = "news_id", referencedColumnName = "id"), 
            inverseJoinColumns = @JoinColumn(name = "topic_id", referencedColumnName = "id"))
    @XmlElement(name="topic")
    private Collection<TopicModel> topic;
    
    
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<CommentModel> comments;
        
}
