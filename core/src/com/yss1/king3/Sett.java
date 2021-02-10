/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yss1.king3;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.yss1.lib_jm.SettBase;
/**
 *
 * @author ys
 */
public class Sett extends SettBase {
//    final static float BTN_H=0.4f;
//    final static float BTN_W=0.7f;
//    final static float BTN_T=0.01f;
    
    //final static int CARD_H=96;
   // final static int CARD_W=71;
    final static int COIN_D=128;
    //final static int DTOP_W=128;
    //final static int DTOP_H=128;
    //final static float cardW()=1f;//ширина карты GL (высота вычисляется пропорционально текстуре)
    final static boolean has_jockers=false;
    final static int MinRank=7;
    //static int lang=1;
    
    
    //static ArrayList<String> players;
//    static String youName1="Ты";
//    static String youName2="You";
//    public static String youName=youName1;
    static boolean showMove=true;
    final static int version=1;
    //final static float th=0.005f;//толщина карты
    
    static boolean autoClear=false;
    final static float originalClearTime=1.2f;
//    
//    final static short [] indexes1p = { 2, 0, 1, 1, 3, 2};
//    final static short [] indexes2p = { 2, 0, 1, 1, 3, 2, 7, 5, 4, 4, 6, 7};
    final static float h0=0.01f;//Начальная высота над столом 1й карты в CardSet
    final static float dh=0.01f;//Расстояние по высоте между соседними картами  в CardSet
    static Vector3f rTrans=new Vector3f(5.4f,-0.2f,0.02f); //смещение правого CardSet
    static Vector3f lTrans=new Vector3f(-5.4f,-0.2f,0.02f);//смещение левого CardSet
    static Vector3f uTrans=new Vector3f(0.0f,2.6f,0.02f); //смещение верхнего CardSet
    static Vector3f dTrans=new Vector3f(0,-2.0f,0.02f);//смещение нижнего CardSet
    static Vector3f dtTrans=new Vector3f(0f,0.5f,0.03f);//смещение взятки CardSet

    //static float deskHw=7.6f;   //половинные размеры стола
    //final static  float deskHh=4.15f;//половинные размеры стола
   // final static  float deskT=0.02f;//толщина стола
    
    final static  float camH=9.0f;   //Координаты камеры по Z в проекционном режиме (в обычном 10)
    final static  float camY=-4.6f;  //Координаты камеры по Y в проекционном режиме (в обычном 0)
    final static  float camA=-0.4f;  //угол наклона камеры (по Х) в проекционном режиме (в обычном 0)
    
    final static int playersCount=4; //на сколько игроков расчитана игра
    
    //положения карт (центр) на столе (активная взятка)
    //Ux,Dx
    final static  float X0dt=0f;
    //-Lx,Rx (УМНОЖАЕМ НА ПОЛОВИНУ ВЫСОТЫ КАРТЫ)
    final static  float X1dt=1f;
    //-Dy, Uy (УМНОЖАЕМ НА ПОЛОВИНУ ВЫСОТЫ КАРТЫ)
    final static  float Y0dt=1f;
    //Ly,Ry при 3 игроках (УМНОЖАЕМ НА ПОЛОВИНУ ШИРИНЫ КАРТЫ)
    final static  float Y13dt=1f;
    //Ly,Ry при 4 игроках (УМНОЖАЕМ НА ПОЛОВИНУ ШИРИНЫ КАРТЫ)
    final static  float Y14dt=0f;
    //случайное отклонение от ровного угла (max)
    final static float fluctA=FastMath.PI/18;
    static boolean is_fluctuate=true;
    //случайное отклонение от ровного положения (max)
    final static float fluctM=cardW()/10;
    //точка вращения веера карт (отклонение по у от 0)
    final static float veerPoint=6.5f;
    
//    //подсветка карты если все НЕ ОК
//    public static final ColorRGBA selClrBad=new ColorRGBA(1.0f,0.6f,0.6f,1);
//    //подсветка карты если все ОК
//    public static final ColorRGBA selClr=new ColorRGBA(0.6f,1,0.6f,1);
    
    //время анимации сек.
    static float amimTime = 0.6f;
//    static float amimBtnTime = 0.3f;
    //время анимации для настройки слайдера сек.
    final static float amimTimeMin = 0.2f;
    final static float amimTimeMax = 1.4f;
    
    //static boolean sound_on=false;
    //static boolean screen_on=false;
    static boolean sep_swits=true;
    
    //static Material trMat;
    
    //для того чтобы считать намерения юзера сходить этой картой
    //отклонение относительно начального положения в веере по у должно превышать 
    final static float minYmove = 0.4f;
    //одновременно по Х не должно превашать +-
    final static float maxXmove = 10.0f;
    //на такие (и меньше) рассоояния двигаемся без анимации
    final static float woAnim = 1.5f;
    //через сколько ходим за банером
    final static float adLoad=60*3;
    
    //Масштабирование
    final static float scaleMy=1.2f;
    final static float scaleOther=0.7f;
    final static float scaleDesk=1.0f;
    //static int startMoney = 100;
    //static boolean original_kingIsBoy=true;
    //static boolean kingIsBoy=original_kingIsBoy;
    static boolean showSparks=true;
    
    final static float defaultDelay = 1.5f;//2sec
    
}