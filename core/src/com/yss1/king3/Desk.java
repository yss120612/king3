/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yss1.king3;

import com.jme3.math.Vector3f;
import com.yss1.lib_jm.AppBase;
import com.yss1.lib_jm.DeskBase;
import com.yss1.lib_jm.InfoPlane;

/**
 *
 * @author ys
 */
public class Desk extends DeskBase {

    public Desk(AppBase a) {
        super(a);
    }

    @Override
    public void init() {
        super.init();
//        float cW = Sett.COI1N_D / (float) app.getRess().getTX_W();
//        float cH = Sett.COIN_D / (float) app.getRess().getTX_H();
//        float coinR = 0.5f;
//
//        coin = new Geometry("Coin", Tools.makeSimplePlane(coinR, coinR, new float[]{1f - cW, 0, 1f, 0, 1f - cW, cH, 1f, cH}));
//        
//        acontrol = new AnimControl();
//        acontrol.addListener(app.getGameAEL());
//        coin.addControl(acontrol);
//        acontrol.addAnim(new Animation("Idle", 0));
//
//        achannel = acontrol.createChannel();
//        achannel.setLoopMode(LoopMode.DontLoop);
//
//        coin.setMaterial(app.getRess().getMaterial("ОбщийПрозрачный"));

    }

    @Override
    protected void initPlanesLetters() {
        main_pannels = new char[]{'D', 'L', 'R', 'U', 'T', 'M'};
        more_pannels = new char[]{'A', 'B', 'C', 'F'};
    }

    public void attachPlanes() {
        enablePannels(main_pannels, true);
        if (app.flash != null) {
            myNode.attachChild(app.flash);
        }
    }

    public void detachPlanes() {
        enablePannels(main_pannels, false);
        if (app.flash != null) {
            myNode.detachChild(app.flash);
        }
    }

    public void attachCurrPlanes() {
        enablePannels(more_pannels, true);
    }

    public void detachCurrPlanes() {
        enablePannels(more_pannels, false);
    }
    
    @Override
    public void idleCoin() {
        InfoPlane ip = app.getUIM().getPlane('O');
        myNode.detachChild(ip.getNodePlane());
    }

    /**
     *
     * @param v
     */
    @Override
    public void showCoin(Vector3f v) {
        showCoin(v,0);
    }
    
    public void showCoin(Vector3f v,int tx) {
        InfoPlane ip = app.getUIM().getPlane('O');
        ip.setMyPlace(v.x, v.y, v.z);
        ip.applyTexture(tx);
        myNode.attachChild(ip.getNodePlane());
    }
    
    
}
