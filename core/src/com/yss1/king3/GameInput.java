/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yss1.king3;

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.event.TouchEvent;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.yss1.lib_jm.Button3d;
import com.yss1.lib_jm.Card;
import com.yss1.lib_jm.GameInputBase;
import com.yss1.lib_jm.InputEvent;
import com.yss1.lib_jm.Wnd;

/**
 *
 * @author ys
 */
public class GameInput extends GameInputBase {

    private Main app;
    private Card ACR;
    private Button3d BUTTON;
    private Wnd WND;

    public GameInput(Main a) {
        super(a);
        app = (Main) ap;
    }

    @Override
    public void init() {
        ACR = null;
        BUTTON = null;
        super.init();
    }

    @Override
    protected void processTouches(float x, float y, TouchEvent.Type TT) {
        CollisionResults results = Tools.vPool.getColl();
        Ray R = getRay(x, y);
        ap.getRootNode().collideWith(R, results);
        CollisionResult nearest = (results.size() > 0) ? results.getClosestCollision() : null;
        boolean fin = false;
        String NAME;
        Vector3f VV = Tools.vPool.getV3();
        boolean succ;
        if (TT == TouchEvent.Type.DOWN) {
            Tools.inputEvents.putEvent(InputEvent.EVENTTYPE.MOUSE_DOWN);
            for (CollisionResult RES : results) {
                NAME = RES.getGeometry().getName();
                if (NAME.contains("btn_")) {
                    BUTTON = app.UIM.getButton(NAME);
                    if (BUTTON != null) {
                        Tools.inputEvents.putEvent(InputEvent.EVENTTYPE.BUTTON_DOWN, BUTTON);
                        fin = true;
                    }
                    
                } else if (NAME.contains("pln_tgt")) {
                    BUTTON = app.UIM.getAliasButton(NAME);
                    if (BUTTON != null) {
                        Tools.inputEvents.putEvent(InputEvent.EVENTTYPE.BUTTON_DOWN, BUTTON);
                    }
                    fin = true;
                } else if (NAME.contains("_pu_ge")) {
                    WND = app.UIM.getPopup(RES.getGeometry());
                    if (WND != null) {
                        WND.setDiff(app.DESK.getTrPlane(), RES.getContactPoint());
                    }
                    fin = true;
                } else if (NAME.contains("Coin")) {

                    Tools.inputEvents.putEvent(InputEvent.EVENTTYPE.CLICK_COIN);
                    fin = true;
                } else if (NAME.contains("pln_menu")) {

                    Tools.inputEvents.putEvent(InputEvent.EVENTTYPE.CLICK_MENU);
                    fin = true;
                } else if (NAME.contains("pln_google_play")) {
                    VV.set(RES.getContactPoint());
                    RES.getGeometry().getWorldTransform().transformInverseVector(VV, VV);
                    Tools.inputEvents.putEvent(InputEvent.EVENTTYPE.CLICK_MULTIPANEL, VV, NAME);
                    fin = true;
                } else if (NAME.contains("pln_namesel")) {
                    Tools.inputEvents.putEvent(InputEvent.EVENTTYPE.CLICK_NAME);
                    fin = true;
                }
                
                if (fin) {
                    break;
                }
                
                ACR = app.GAME.getCS('T').findIdle(NAME);
                if (ACR != null) {
                    Tools.inputEvents.putEvent(InputEvent.EVENTTYPE.CLICK_DT, ACR);
                    ACR = null;
                    fin = true;
                }
            }
            if (!fin && nearest != null) {
                if (nearest.getGeometry().getName().equals("Desk")) {
                    Tools.inputEvents.putEvent(InputEvent.EVENTTYPE.CLICK_DT);
                    return;
                }
                ACR = app.GAME.getCS('D').findIdle(nearest.getGeometry().getName());
                if (ACR != null) {

                    ACR.saveTransform();
                    //ACR.getGe().move(0, 0, 0.3f);
                    ACR.setDiff(app.DESK.getTrPlane(), nearest.getContactPoint(), app.getCamera().getLocation(), true);
                    Tools.inputEvents.putEvent(InputEvent.EVENTTYPE.CARD_DOWN, ACR);
                }
            }
        } else if (TT == TouchEvent.Type.UP) {
            if (ACR != null) {
                ACR.getGe().getParent().detachChildNamed("move_plane");
                Tools.inputEvents.putEvent(InputEvent.EVENTTYPE.CARD_UP, ACR);
                ACR = null;
                fin = true;
            }
            succ = false;
            if (!fin) {
                if (BUTTON != null) {
                    Button3d B;
                    for (CollisionResult RES : results) {
                        NAME = RES.getGeometry().getName();
                        if (NAME.contains("btn_")) {
                            B = app.UIM.getButton(NAME);
                            if (BUTTON.equals(B)) {
                                succ = true;
                                break;
                            }
                        }
                        if (NAME.contains("pln_tgt")) {//проверяем хозяйскую панель
                            B = app.UIM.getAliasButton(NAME);
                            if (BUTTON.equals(B)) {
                                succ = true;
                                break;
                            }
                        }
                    }
                }
            }
            if (WND != null) {
                WND.getWndNode().getParent().detachChildNamed("move_plane");
                WND = null;
            }
            if (BUTTON != null) {
                Tools.inputEvents.putEvent(InputEvent.EVENTTYPE.BUTTON_UP, BUTTON, succ);
                BUTTON = null;
            }
        } else if (TT == TouchEvent.Type.MOVE && (ACR != null || WND != null)) {
            CollisionResult MP = null;
            for (CollisionResult RES : results) {
                if (RES.getGeometry().getName().contains("move_plane")) {
                    MP = RES;
                    break;
                }
            }
            if (MP != null) {
                Vector3f V0 = Tools.vPool.getV3(MP.getContactPoint());
                //V0.z+=0.5f;
                if (ACR != null) {
                    Transform TR = ACR.getGe().getParent().getLocalTransform();
                    TR.transformInverseVector(V0, V0);//Где сейчас мышь в локальных координатах (ноды CardSet-а)
                    V0 = V0.subtract(ACR.getDiff());
                    float A = FastMath.atan2(V0.x, V0.y + Sett.veerPoint);//повернем относительно центра веера
                    float[] Aold = ACR.getGe().getLocalRotation().toAngles(null);//повернем по з по остальным осям сохраним
                    ACR.getGe().setLocalTranslation(V0);
                    Quaternion Q0 = Tools.vPool.getQt(Aold[0], Aold[1], -A);
                    ACR.getGe().setLocalRotation(Q0);
                    Tools.vPool.freeQt(Q0);
                } else if (WND != null) {
                    V0 = V0.subtract(WND.getDiff());
                    WND.getWndNode().setLocalTranslation(V0);
                }
                Tools.vPool.freeV3(V0);
            }//MP==null
        }
        Tools.vPool.freeRay(R);
        Tools.vPool.freeV3(VV);
        Tools.vPool.freeColl(results);
    }
}
