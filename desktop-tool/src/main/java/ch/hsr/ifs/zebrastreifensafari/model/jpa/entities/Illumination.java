/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.ifs.zebrastreifensafari.model.jpa.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author aeugster
 */
@Entity
@Table(name = "crossing.illumination")
@NamedQueries({
        @NamedQuery(name = "Illumination.findAll", query = "SELECT i FROM Illumination i"),
        @NamedQuery(name = "Illumination.findById", query = "SELECT i FROM Illumination i WHERE i.id = :id"),
        @NamedQuery(name = "Illumination.findByValue", query = "SELECT i FROM Illumination i WHERE i.value = :value")
})
public class Illumination implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "value")
    private String value;
    @OneToMany(mappedBy = "illuminationId", fetch = FetchType.LAZY)
    private List<Rating> ratingList;

    public Illumination() {
    }

    public Illumination(Integer id) {
        this.id = id;
    }

    public Illumination(Integer id, String value) {
        this.id = id;
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<Rating> getRatingList() {
        return ratingList;
    }

    public void setRatingList(List<Rating> ratingList) {
        this.ratingList = ratingList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Illumination[ id=" + id + " ]";
    }

}
