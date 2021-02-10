/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yss1.king3;

import com.jme3.asset.AssetManager;
import com.yss1.lib_jm.RessBase;

/**
 *
 * @author ys
 */
public class Ress extends RessBase{
 
 public Ress(AssetManager a)   
 {
     super(a);
 }

    @Override
    protected void initMoreTextures() {
        //Здесь разместим доп. текстуры
     //tex.put("ДостиженияЗеленый", ap.loadTexture("Textures/acv_green.png"));      
    }

    @Override
    protected void initMoreMaterials() {
        //Здесь разместим доп. материалы
       //   mat.put("ДостиженияЗеленый", MakeMaterial(ColorRGBA.White,tex.get("ДостиженияЗеленый"),true));
    }
}
 
