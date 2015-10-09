/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.zebrastreifensafari.model;

import java.util.ArrayList;
import java.util.List;

import ch.hsr.zebrastreifensafari.jpa.entities.*;
import ch.hsr.zebrastreifensafari.service.DataServiceLoader;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author aeugster
 */
public class Model{
    
    private boolean ratingMod; //True: List of Zebras; False: List of Ratings
    private List<Zebracrossing> zebras;
    private List<Rating> ratings;

    public Model() {
        ratingMod = true;
        zebras = DataServiceLoader.getZebraData().getZebracrossings();
        ratings = new ArrayList<Rating>();
    }
    
    public Rating getRatingById(int id){
        for(Rating r: ratings){
            if(r.getRatingId() == id){
                return r;
            }
        }

        return null;
    }
    
    public Zebracrossing getZebrasById(int id){
        for(Zebracrossing z: zebras){
            if(z.getZebracrossingId()== id){
                return z;
            }
        }

        return null;
    }
    

    /**
     * @return the zebras
     */
    public List<Zebracrossing> getZebras() {
        return zebras;
    }

    /**
     * @param zebras the zebras to set
     */
    public void setZebras(List<Zebracrossing> zebras) {
        this.zebras = zebras;
    }

    /**
     * @return the ratings
     */
    public List<Rating> getRatings() {
        return ratings;
    }

    /**
     * @param ratings the ratings to set
     */
    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    /**
     * @return the zebraB
     */
    public boolean isRatingMod() {
        return ratingMod;
    }

    /**
     * @param zebraB the zebraB to set
     */
    public void setRatingMod(boolean ratingMod) {
        this.ratingMod = ratingMod;
    }   
}
