package com.icsd.demo.models;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name="comment")
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name="comment")
public class CommentModel implements Serializable{
    
     private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @javax.persistence.Id
    @XmlAttribute(name="id")
    private int id;
    
    @Column (nullable=false)
    @XmlElement(name = "periexomeno")
    private String periexomeno;
    
    @Column
    @XmlElement(name = "imerDimiourgias")
    private Date imerDimiourgias;
    
    @Column
    @XmlElement(name = "dimiourgos")
    private String dimiourgos;
    
    @Column
    @XmlElement(name = "commentstate")
    private CommentEnum commentstate = CommentEnum.CREATED;
    
    @ManyToOne
    @JoinColumn(name="news_id", nullable=true)
    @XmlElement(name = "news_id")
    private NewsModel news_id;
     
    
}
