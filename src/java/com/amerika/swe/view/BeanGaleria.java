/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amerika.swe.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;



/**
 *
 * @author estudiante.proyectos
 */
@ManagedBean
@ViewScoped
public class BeanGaleria implements Serializable{

    
    private List<String> images; 
    
    public BeanGaleria() {
        images = new ArrayList<String>();  
        String [] nombresImgs={"slider1.jpg","slider2.jpg","slider3.jpg","slider4.jpg"};        
        images.addAll(Arrays.asList(nombresImgs));
    }

    /**
     * @return the images
     */
    public List<String> getImages() {
        return images;
    }

    /**
     * @param images the images to set
     */
    public void setImages(List<String> images) {
        this.images = images;
    }
}
