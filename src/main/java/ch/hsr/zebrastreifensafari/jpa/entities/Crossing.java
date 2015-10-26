/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.zebrastreifensafari.jpa.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author aeugster
 */
@Entity
@Table(name = "crossing.crossing")
@NamedQueries({
    @NamedQuery(name = "Crossing.findAll", query = "SELECT c FROM Crossing c"),
    @NamedQuery(name = "Crossing.findById", query = "SELECT c FROM Crossing c WHERE c.id = :id"),
    @NamedQuery(name = "Crossing.findByOsmNodeId", query = "SELECT c FROM Crossing c WHERE c.osmNodeId = :osmNodeId"),
    @NamedQuery(name = "Crossing.findByStatus", query = "SELECT c FROM Crossing c WHERE c.status = :status")})
public class Crossing implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "osm_node_id")
    private long osmNodeId;
    @Basic(optional = false)
    @Column(name = "status")
    private int status;
    @OneToMany(mappedBy = "crossingId", fetch = FetchType.LAZY)
    private List<Rating> ratingList;

    public Crossing() {
    }

    public Crossing(Integer id) {
        this.id = id;
    }

    public Crossing(Integer id, long osmNodeId, int status) {
        this.id = id;
        this.osmNodeId = osmNodeId;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public long getOsmNodeId() {
        return osmNodeId;
    }

    public void setOsmNodeId(long osmNodeId) {
        this.osmNodeId = osmNodeId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Crossing)) {
            return false;
        }
        Crossing other = (Crossing) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ch.hsr.zebrastreifensafari.jpa.entities.Crossing[ id=" + id + " ]";
    }
    
}
