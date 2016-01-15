/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amerika.swe.dao;

import java.util.List;

/**
 *
 * @author estudiante.proyectos
 */
public interface Historico<T> {
    
    public T consultar_ultimo(Integer...id);
    public List<T> consultar_historial(Integer...id);
    
}
