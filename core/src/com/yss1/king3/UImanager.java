/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yss1.king3;

import com.jme3.font.BitmapFont;
import com.jme3.font.Rectangle;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.yss1.lib_jm.Button3d;
import com.yss1.lib_jm.Button3dBuilder;
import com.yss1.lib_jm.FaceState;
import com.yss1.lib_jm.InfoPlane;
import com.yss1.lib_jm.InfoPlaneBuilder;
import com.yss1.lib_jm.RessKeeper;
import com.yss1.lib_jm.UImanagerBase;
import com.yss1.lib_jm.Wnd;

import java.util.ArrayList;
import java.util.List;

import static com.jme3.font.BitmapFont.Align;
import static com.yss1.lib_jm.Button3d.BSIZE;
import static com.yss1.lib_jm.Button3d.BTYPE;

/**
 *
 * @author ys
 */
public class UImanager extends UImanagerBase{
   //private Main app;
   public  UImanager(RessKeeper a)
   {
     super(a); 
   }
   
   @Override
   protected void init()
   {
   //app=(Main)rKeeper;    
   }
  
  
    
   
    @Override
    protected void makePlanes() {
        super.makePlanes();

        float X1, X2, X3, X4, X5, X6, X7, X8,X9,X10;
        float Y1, Y2;
        float pH, YY, XX, HH, YY1;
        float shiftY, shiftX;
        //ПАНЕЛИ с очками
        //Табло с общим счетом
        X1 = rKeeper.getRess().getTextX(Sett.CARD_W * 12);
        X2 = rKeeper.getRess().getTextX(Sett.CARD_W * 12 + 147);
        Y2 = rKeeper.getRess().getTextY(Sett.CARD_H * 3);
        Y1 = rKeeper.getRess().getTextY(Sett.CARD_H * 3 + 89);

        pH = 0.55f;
        YY = 3.5f;
        YY1 = 2.55f;
        XX = 5.8f * Sett.getSCRatio() / 1.777f;
        HH = 0.15f;

        InfoPlaneBuilder builder = new InfoPlaneBuilder(rKeeper).setContainer(planes);

        List<FaceState> fsList = new ArrayList<>();
        FaceState faceState = new FaceState(rKeeper).init("ОбщийПрозрачный", new float[]{X1, Y1, X2, Y1, X1, Y2, X2, Y2});
        fsList.add(faceState);

        builder.setFaces(fsList).
                setDimY(pH).
                setFontNo(1).
                setFontColor(ColorRGBA.Blue).
                setFontSize(pH * 0.7f).
                //setFontY(pH / 1.5f);
                setFontY(pH / 4f);

        builder.setPlace(0, -YY, HH).build('D', "pln_scoreDr");
        builder.setPlace(XX, YY1, HH).build('R', "pln_scoreR");
        builder.setPlace(-XX, YY1, HH).build('L', "pln_scoreL");
        builder.setPlace(0, YY, HH).build('U', "pln_scoreU");
        //КОНЕЦ Табло с общим счетом


        //Индикатор текущего состояния (5 текстур кружочком)

        XX = Sett.CARD_W * 4 + 278;
        X1 = rKeeper.getRess().getTextX(XX);
        X2 = rKeeper.getRess().getTextX(XX + 48 - 1);
        X3 = rKeeper.getRess().getTextX(XX + 48);
        X4 = rKeeper.getRess().getTextX(XX + 48 * 2 - 1);
        X5 = rKeeper.getRess().getTextX(XX + 48 * 2);
        X6 = rKeeper.getRess().getTextX(XX + 48 * 3 - 1);
        X7 = rKeeper.getRess().getTextX(XX + 48 * 3);
        X8 = rKeeper.getRess().getTextX(XX + 48 * 4 - 1);
        X9 = rKeeper.getRess().getTextX(XX + 48 * 4);
        X10 = rKeeper.getRess().getTextX(XX + 48 * 5 - 1);
        Y2 = rKeeper.getRess().getTextY(Sett.CARD_H * 4);
        Y1 = rKeeper.getRess().getTextY(Sett.CARD_H * 4 + 48 - 1);

        pH = 0.37f;
        YY1 = 1.4f;
        HH = 0.7f;
        XX = 4.5f * Sett.getSCRatio() / 1.777f;

        fsList = new ArrayList<>();
        fsList.add(new FaceState(rKeeper).init("ОбщийПрозрачный", new float[]{X3, Y1, X4, Y1, X3, Y2, X4, Y2}));
        fsList.add(new FaceState(rKeeper).init("ОбщийПрозрачный", new float[]{X1, Y1, X2, Y1, X1, Y2, X2, Y2}));
        fsList.add(new FaceState(rKeeper).init("ОбщийПрозрачный", new float[]{X7, Y1, X8, Y1, X7, Y2, X8, Y2}));
        fsList.add(new FaceState(rKeeper).init("ОбщийПрозрачный", new float[]{X5, Y1, X6, Y1, X5, Y2, X6, Y2}));
        fsList.add(new FaceState(rKeeper).init("ОбщийПрозрачный", new float[]{X9, Y1, X10, Y1, X9, Y2, X10, Y2}));

        builder.setDimY(pH).
                setFaces(fsList).
                setFontNo(1).
                setFontColor(ColorRGBA.Red).
                setFontSize(0.35f).
                //setFontY(pH / 1.7f);
                setFontY(0);

        builder.setPlace(2.8f, -1.3f, HH).setDimY(pH).build('A', "pln_infoA");
        builder.setPlace(-XX, YY1, HH).build('B', "pln_infoL");
        builder.setPlace(XX, YY1, HH).build('C', "pln_infoR");
        builder.setPlace(-1.8f, 2.1f, HH).build('F', "pln_infoU");
        //КОНЕЦ Индикатор текущего состояния (5 текстур кружочком)
        
        //корона
        float coinR = 0.5f;
        fsList = new ArrayList<>();
        X1= rKeeper.getRess().getTextX(896);
        X2= rKeeper.getRess().getTextX(896+Sett.COIN_D-1);
        Y2= rKeeper.getRess().getTextY(384);
        Y1= rKeeper.getRess().getTextY(384+Sett.COIN_D-1);
        fsList.add(new FaceState(rKeeper).init("ОбщийПрозрачный", new float[]{X1, Y1, X2, Y1, X1, Y2, X2, Y2}));
        
//        X1= rKeeper.getRess().getTextX(685);
//        X2= rKeeper.getRess().getTextX(685+42);
//        Y2= rKeeper.getRess().getTextY(464);
//        Y1= rKeeper.getRess().getTextY(464+42);
//        fsList.add(new FaceState(rKeeper).init("ОбщийПрозрачный", new float[]{X1, Y1, X2, Y1, X1, Y2, X2, Y2}));
        builder.setFaces(fsList).setDimY(coinR).build('O', "Coin");
        //КОНЕЦ корона
        
        
        
        
        //заголовок партии
        YY = 0.3f;
        XX = 2.4f * Sett.getSCRatio() / 1.77f;
        pH = 0.03f;
        float texWH = rKeeper.getRess().getTexW2HMaterial("СтолТемный");
        float xrep = 3;

        fsList = new ArrayList<>();
        fsList.add(new FaceState(rKeeper).init("СтолТемный", Tools.singleTex).scale(xrep, xrep * YY / XX * texWH));

        builder.setDim(XX, YY).
                setFaces(fsList).
                setFontNo(2).
                setFontAlign(Align.Left).
                setFontSize(0.5f).
                setFontX(-XX + XX / 10).
                setFontY(YY).
                setFontRectangle(new Rectangle(0, 0, 2 * XX - XX / 20f, YY * 2)).
                setFontColor(ColorRGBA.Green);

        builder.setPlace(-4.7f * Sett.getSCRatio() / 1.77f, 4f - YY, pH).build('T', "pln_current");

        //выбор рубахи
        YY = 1.0f;
        XX = 2.4f;

        fsList = new ArrayList<>();
        fsList.add(new FaceState(rKeeper).init("СтолТемный", Tools.singleTex).scale(xrep, xrep * YY / XX * texWH));

        builder.setDim(XX, YY).
                setFaces(fsList).
                setFontSize(0.4f).
                setFontRectangle(new Rectangle(0, 0, 2 * XX - XX / 20f, YY / 7f)).
                setFontX(-XX + XX / 10).
                setFontY(YY);

        shiftY = 3.5f;
        shiftX = 4.0f * Sett.getSCRatio() / 1.77f;
        builder.setPlace(-shiftX, shiftY - YY, pH).build('E', "pln_rubsel");
        distributeRadioGroupOnPlane(2, 'E');

        //выбор имени
        YY = 0.4f;
        fsList = new ArrayList<>();
        fsList.add(new FaceState(rKeeper).init("СтолТемный", Tools.singleTex).scale(xrep, xrep * YY / XX * texWH));

        builder.setDim(XX, YY).
                setFaces(fsList).
                setFontRectangle(new Rectangle(0, 0, 2 * XX - XX / 20f, YY * 2f)).
                setFontY(YY);

        builder.setPlace(-shiftX, shiftY - 2 - YY - 0.3f, pH).build('G', "pln_namesel");

        //подложка радиобуттона king is boy
        YY = 0.6f;
        fsList = new ArrayList<>();
        fsList.add(new FaceState(rKeeper).init("СтолТемный", Tools.singleTex).scale(xrep, xrep * YY / XX * texWH));
        builder.setDim(XX, YY).
                setFaces(fsList).
                setFontY(YY);

        builder.setPlace(shiftX, shiftY - YY, pH).
                setTarget(getButton("btn_king_boy")).
                build('H', "pln_tgt_king_boy");
        distributeOnPlane(BitmapFont.Align.Right, BitmapFont.VAlign.Center, 'H', "btn_king_boy");


        //подложка радиобуттона гасить экран
        builder.setPlace(shiftX, shiftY - 1.2f - YY - 0.3f, pH).
                setTarget(getButton("btn_screen_on")).
                build('I', "pln_tgt_screen_on");
        distributeOnPlane(BitmapFont.Align.Right, BitmapFont.VAlign.Center, 'I', "btn_screen_on");


        //подложка радиобуттона убирать карту
        builder.setPlace(shiftX, shiftY - 1.2f * 2 - YY - 0.3f * 2, pH).
                setTarget(getButton("btn_autoclear")).
                build('J', "pln_tgt_autoclear");
        distributeOnPlane(BitmapFont.Align.Right, BitmapFont.VAlign.Center, 'J', "btn_autoclear");
        
//        InfoPlane ip=builder.setPlace(7,0,pH).
//                setTarget(null).
//                build('X', "pln_dummy");
//                ip.setText1("Loading");
//                ip.setText2("...");
        
    }
   
    @Override
    protected void makeButtons() {
        float X1, X2, X3, X4;
        float Y1, Y2, Y3, Y4;

        //WOOD buttons
        X1 = rKeeper.getRess().getTextX(Sett.CARD_W * 4 + 90 + 64);
        X2 = rKeeper.getRess().getTextX(Sett.CARD_W * 4 + 90 + 64 + 124);
        Y1 = rKeeper.getRess().getTextY(Sett.CARD_H * 4 + 76);
        Y2 = rKeeper.getRess().getTextY(Sett.CARD_H * 4);

        Button3dBuilder builder = new Button3dBuilder(rKeeper).setBtnList(buttons);
        //List<FaceState> imgList = new ArrayList<>();
        //imgList.add(new FaceState(rKeeper).init("ДостиженияЗеленый", Tools.singleTex));
        
        List<FaceState> fsList = new ArrayList<>();
        fsList.add(new FaceState(rKeeper).init("ОбщийПрозрачный",X1, Y1, X2, Y1, X1, Y2, X2, Y2));
        Y1 = 2.5f;
        Y2 = Sett.BTN_H * 1.4f * 2.2f;

        Button3d b3d=builder.setFaces(fsList).
                setPlace(0f, Y1, 1f).
                setIf(rKeeper.getButtonListener("MENU")).
                setSection(1).
                build("btn_game");

        builder.setSection(2).build("btn_quick");
        builder.setPlace(0f, Y1 - Y2, 1f).setSection(1).build("btn_multiplayer");
        builder.setSection(2).build("btn_select");
        //builder.setPlace(0f, Y1 - Y2 * 2, 1f).setSection(1).build("btn_help");
        //builder.setPlace(0f, Y1 - Y2 * 2, 1f).setSection(1).setImages(imgList,0.3f).build("btn_help");
        builder.setPlace(0f, Y1 - Y2 * 2, 1f).setSection(1).build("btn_help");
        
        builder.setSection(2).setImages(null,0).build("btn_invitations");
        builder.setPlace(0f, Y1 - Y2 * 3, 1f).setSection(1).build("btn_options");
        builder.setSection(2).build("btn_home");
        builder.setPlace(0f, Y1 - Y2 * 4, 1f).setSection(0).build("btn_quit");

        Y2 = b3d.getDx() * 2f;
        builder.setPlace(Y2, -2.5f, 0.5f).setIf(rKeeper.getButtonListener("OPTION")).build("btn_ok");
        builder.setPlace(-Y2, -2.5f, 0.5f).build("btn_cancel");


        X1 = rKeeper.getRess().getTextX(Sett.CARD_W * 4 + 90);
        X2 = rKeeper.getRess().getTextX(Sett.CARD_W * 4 + 90 + 64 - 1);
        Y2 = rKeeper.getRess().getTextY(Sett.CARD_H * 4 + 64 * 2 - 1);
        Y1 = rKeeper.getRess().getTextY(Sett.CARD_H * 4 + 64);
        Y3 = rKeeper.getRess().getTextY(Sett.CARD_H * 4);
        Y4 = rKeeper.getRess().getTextY(Sett.CARD_H * 4 + 64 - 1);

        fsList = new ArrayList<>();
        fsList.add(new FaceState(rKeeper).init("ОбщийПрозрачный", X1, Y1, X2, Y1, X1, Y2, X2, Y2, X2, Y3, X1, Y3, X2, Y4, X1, Y4));

        builder.setPlace(3f, -1f, 1f).
                setFaces(fsList).
                setType(BTYPE.CHECK).
                setIf(rKeeper.getButtonListener("MENU")).
                setSize(BSIZE.MEDIUM).
                build("btn_sound");


        X1 = rKeeper.getRess().getTextX(Sett.CARD_W * 4);
        X2 = rKeeper.getRess().getTextX(Sett.CARD_W * 4 + 90 - 1);
        Y2 = rKeeper.getRess().getTextY(Sett.CARD_H * 4);
        Y1 = rKeeper.getRess().getTextY(Sett.CARD_H * 4 + 52 - 1);

        fsList = new ArrayList<>();
        fsList.add(new FaceState(rKeeper).init("Общий",X1, Y1, X2, Y1, X1, Y2, X2, Y2));
        fsList.add(new FaceState(rKeeper).init("ОбщийПлохо",X1, Y1, X2, Y1, X1, Y2, X2, Y2));

        builder.setPlace(3f, 2f, 1f).
                setFaces(fsList).
                setSize(BSIZE.SMALL).
                setAndInitRadio(1, false).
                build("btn_lang_en");


        X1 = rKeeper.getRess().getTextX(Sett.CARD_W * 4);
        X2 = rKeeper.getRess().getTextX(Sett.CARD_W * 4 + 90 - 1);
        Y2 = rKeeper.getRess().getTextY(Sett.CARD_H * 4 + 52 + 1);
        Y1 = rKeeper.getRess().getTextY(Sett.CARD_H * 4 + 52 * 2 - 1);

        fsList = new ArrayList<>();
        fsList.add(new FaceState(rKeeper).init("Общий", X1, Y1, X2, Y1, X1, Y2, X2, Y2));
        fsList.add(new FaceState(rKeeper).init("ОбщийПлохо", X1, Y1, X2, Y1, X1, Y2, X2, Y2));
        builder.setFaces(fsList).setPlace(3f, 1.3f, 1f).build("btn_lang_ru");


        X1 = rKeeper.getRess().getTextX(Sett.CARD_W * 0);
        X2 = rKeeper.getRess().getTextX(Sett.CARD_W * 0 + Sett.CARD_W);
        Y2 = rKeeper.getRess().getTextY(Sett.CARD_H * 4);
        Y1 = rKeeper.getRess().getTextY(Sett.CARD_H * 5);

        fsList = new ArrayList<>();
        fsList.add(new FaceState(rKeeper).init("Общий",X1, Y1, X2, Y1, X1, Y2, X2, Y2));
        fsList.add(new FaceState(rKeeper).init("ОбщийПлохо", X1, Y1, X2, Y1, X1, Y2, X2, Y2));

        builder.setPlace(-4f, 2.5f, 1f).
                setFaces(fsList).
                setAndInitRadio(2, false).
                setIf(rKeeper.getButtonListener("OPTION")).
                setSize(BSIZE.LARGE).
                build("btn_back_1");


        X1 = rKeeper.getRess().getTextX(Sett.CARD_W * 1);
        X2 = rKeeper.getRess().getTextX(Sett.CARD_W * 1 + Sett.CARD_W);

        fsList = new ArrayList<>();
        fsList.add(new FaceState(rKeeper).init("Общий",X1, Y1, X2, Y1, X1, Y2, X2, Y2));
        fsList.add(new FaceState(rKeeper).init("ОбщийПлохо",X1, Y1, X2, Y1, X1, Y2, X2, Y2));
        builder.setFaces(fsList).setPlace(-3f, 2.5f, 1f).build("btn_back_2");


        X1 = rKeeper.getRess().getTextX(Sett.CARD_W * 2);
        X2 = rKeeper.getRess().getTextX(Sett.CARD_W * 2 + Sett.CARD_W);

        fsList = new ArrayList<>();
        fsList.add(new FaceState(rKeeper).init("Общий",X1, Y1, X2, Y1, X1, Y2, X2, Y2));
        fsList.add(new FaceState(rKeeper).init("ОбщийПлохо",X1, Y1, X2, Y1, X1, Y2, X2, Y2));
        builder.setFaces(fsList).setPlace(-2f, 2.5f, 1f).build("btn_back_3");

        X1 = rKeeper.getRess().getTextX(Sett.CARD_W * 3);
        X2 = rKeeper.getRess().getTextX(Sett.CARD_W * 3 + Sett.CARD_W);

        fsList = new ArrayList<>();
        fsList.add(new FaceState(rKeeper).init("Общий", X1, Y1, X2, Y1, X1, Y2, X2, Y2));
        fsList.add(new FaceState(rKeeper).init("ОбщийПлохо",X1, Y1, X2, Y1, X1, Y2, X2, Y2));
        builder.setFaces(fsList).setPlace(-1f, 2.5f, 1f).build("btn_back_4");

        X3 = rKeeper.getRess().getTextX(Sett.CARD_W * 4 + 253 + 48);
        X4 = rKeeper.getRess().getTextX(Sett.CARD_W * 4 + 253 + 48 * 2 - 1);
        Y2 = rKeeper.getRess().getTextY(Sett.CARD_H * 4 + 78);
        Y1 = rKeeper.getRess().getTextY(Sett.CARD_H * 4 + 78 + 48 - 1);
        X1 = rKeeper.getRess().getTextX(Sett.CARD_W * 4 + 253);
        X2 = rKeeper.getRess().getTextX(Sett.CARD_W * 4 + 253 + 48 - 1);

        fsList = new ArrayList<>();
        fsList.add(new FaceState(rKeeper).init("ОбщийПрозрачный", X1, Y1, X2, Y1, X1, Y2, X2, Y2, X4, Y1, X3, Y1, X4, Y2, X3, Y2));
        builder.setPlace(3f, 1f, 1f).
                setFaces(fsList).
                setType(BTYPE.CHECK).
                setSize(BSIZE.MEDIUM).
                build("btn_king_boy");


        X1 = rKeeper.getRess().getTextX(Sett.CARD_W * 4 + 154);
        X2 = rKeeper.getRess().getTextX(Sett.CARD_W * 4 + 154 + 48 - 1);
        Y2 = rKeeper.getRess().getTextY(Sett.CARD_H * 4 + 76);
        Y1 = rKeeper.getRess().getTextY(Sett.CARD_H * 4 + 76 + 48 - 1);
        X3 = rKeeper.getRess().getTextX(Sett.CARD_W * 4 + 154 + 48);
        X4 = rKeeper.getRess().getTextX(Sett.CARD_W * 4 + 154 + 48 * 2 - 1);

        fsList = new ArrayList<>();
        fsList.add(new FaceState(rKeeper).init("ОбщийПрозрачный",   X1, Y1,
                                                                    X2, Y1,
                                                                    X1, Y2,
                                                                    X2, Y2,
                                                                    X4, Y1,
                                                                    X3, Y1,
                                                                    X4, Y2,
                                                                    X3, Y2));
        builder.setFaces(fsList).setPlace(0, 0, 0).build("btn_screen_on");

        X1 = rKeeper.getRess().getTextX(Sett.CARD_W * 4 + 350);
        X2 = rKeeper.getRess().getTextX(Sett.CARD_W * 4 + 350 + 48 - 1);
        Y2 = rKeeper.getRess().getTextY(Sett.CARD_H * 4 + 77);
        Y1 = rKeeper.getRess().getTextY(Sett.CARD_H * 4 + 77 + 48 - 1);
        X3 = rKeeper.getRess().getTextX(Sett.CARD_W * 4 + 350 + 48);
        X4 = rKeeper.getRess().getTextX(Sett.CARD_W * 4 + 350 + 48 * 2 - 1);

        fsList = new ArrayList<>();
        fsList.add(new FaceState(rKeeper).init("ОбщийПрозрачный",X1, Y1, X2, Y1, X1, Y2, X2, Y2, X4, Y1, X3, Y1, X4, Y2, X3, Y2));
        builder.setFaces(fsList).setPlace(0, 0, 0).build("btn_autoclear");
    }
   
   @Override
   public void updateCaptions() {
        for (Button3d b : buttons)
        {
            if (b.getName().equals("btn_game")) 
            {
//                if (go)
//                 b.setText(Tools.getText(104));
//                else
//                 b.setText(Tools.getText(100));
            }
            else if (b.getName().equals("btn_options")) 
            {
                b.setText(Tools.getText(101));
            }
            else if (b.getName().equals("btn_help")) 
            {
                b.setText(Tools.getText(105));
            }
            else if (b.getName().equals("btn_quit")) 
            {
                b.setText(Tools.getText(102));
            }
            else if (b.getName().equals("btn_multiplayer")) 
            {
                b.setText(Tools.getText(106));
            }
            else if (b.getName().equals("btn_quick")) 
            {
                b.setText(Tools.getText(107));
            }
            else if (b.getName().equals("btn_select")) 
            {
                b.setText(Tools.getText(108));
            }
            else if (b.getName().equals("btn_invitations")) 
            {
                b.setText(Tools.getText(109));
            }
            else if (b.getName().equals("btn_home")) 
            {
                b.setText(Tools.getText(110));
            }
            else if (b.getName().equals("btn_ok")) 
            {
                b.setText(Tools.getText(303));
            }
            else if (b.getName().equals("btn_cancel")) 
            {
                b.setText(Tools.getText(304));
            }
            else if (b.getName().equals("pubtn_yes")) 
            {
                b.setText(Tools.getText(1));
            }
            else if (b.getName().equals("pubtn_no")) 
            {
                b.setText(Tools.getText(2));
            }
        }
        
        InfoPlane IP=planes.get('E');
        if (IP!=null)
        {
            IP.setText1(Tools.getText(301));
        }
        
//        IP=planes.get('T');
//        if (IP!=null)
//        {
//            IP.setText1(Tools.getText(gn));
//        }
        
        
        IP=planes.get('H');
        if (IP!=null)
        {
            IP.setText1(Tools.getText(305));
        }
        
        IP=planes.get('I');
        if (IP!=null)
        {
            IP.setText1(Tools.getText(306));
        }

        IP=planes.get('J');
        if (IP!=null)
        {
            IP.setText1(Tools.getText(309));
        }
        
        Wnd wn=popups.get('B');
        int i=0;
        if (wn!=null)
        {//контекстное меню
            wn.setCaption("");
            for (Button3d B3:wn.getButtons())
            {
                try
                {
                 i=Integer.parseInt(B3.getName().substring(B3.getName().length()-1,B3.getName().length()));
                 B3.setText(Tools.getText(500+i));
                }
                catch (Exception E)
                {
                }
            }
        }
        
    }

   public void UpdateMoreCaptions(boolean go, int gn)
   {
       setTextGame(gn);
       setGameButton(go);
   }
   
   
   @Override
   protected void makePopups() {
        super.makePopups();
        //контекстное меню
        float pw_W=1.0f;
        Wnd popup = new Wnd(rKeeper,'B',"The caption window"," this is a message ant i may be too long...",pw_W, Sett.deskHh*0.89f,rKeeper.getRess().getTexture("Рамка1Центр").getImage().getHeight(),6);
        popup.setMyPlace(new Vector3f(Sett.deskHw-pw_W-0.45f*Sett.getSCRatio(),0,0.85f));
        popups.put('B', popup);
        
    }
   
   public void setTextTricks(int i,Character c,boolean act, boolean needClear, boolean live)
   {
       InfoPlane ip=getPlane(getWho(c));
       if (ip==null) return;
          String s=(i<0?-i:i)+"";
          if (i==999) 
          {
              i=0;
              s="";
          }
          ColorRGBA CL;
          if (i>0) 
          CL=ColorRGBA.Red;
          else if (i<0) CL=ColorRGBA.Blue;
          else CL=ColorRGBA.DarkGray;
          ip.setTextAndColor(s, CL);
          int tN=live?(act?1:0)+(needClear?2:0):4;
          ip.applyTexture(tN);
   }
   
   public void setTextGame(int i)
   {
       InfoPlane ip=getPlane('T');
       if (ip!=null) 
       {
          ColorRGBA CL;
          if (i==0) 
          CL=ColorRGBA.Green;
          else if (i<=7) CL=rKeeper.getRess().LightBlue;
          else CL=ColorRGBA.Red;
          ip.setTextAndColor(Tools.getText(479+i), CL);
       }
   }
   
   public void setGameButton(boolean isNew)
   {
       for (Button3d b : buttons)
        {
            if (b.getName().equals("btn_game")) 
            {
                if (!isNew)
                 b.setText(Tools.getText(104));
                else
                 b.setText(Tools.getText(100));
                break;
            }
        }
   }
   
   
   
   
   private Character getWho(Character X)
   {
       if (X=='D') return  'A';
       else if (X=='L') return  'B';
       else if (X=='R') return  'C';
       else if (X=='U') return  'F';
       else return 'Y';
   }
   
  
   
}
