package com.yss1.king3;

import com.jme3.animation.AnimEventListener;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.yss1.lib_jm.AppBase;
import com.yss1.lib_jm.Button3d;
import com.yss1.lib_jm.ButtonListener;
import com.yss1.lib_jm.Card;
import com.yss1.lib_jm.InfoPlane;
import com.yss1.lib_jm.InputEvent;
import com.yss1.lib_jm.Qnet;
import com.yss1.lib_jm.ToolsBase;
import com.yss1.lib_jm.Users;
import com.yss1.lib_jm.WaiterElement;
import com.yss1.lib_jm.Wnd;

import java.util.ArrayList;
import java.util.HashMap;

import static com.yss1.king3.GamePlayAppState.GameState;
import static com.yss1.lib_jm.NetPacket.PType.IM_PAUSED;
import static com.yss1.lib_jm.WaiterElement.WAITERTYPE.AD_NEED;
import static com.yss1.lib_jm.WaiterElement.WAITERTYPE.SEND_ALIVE;
import static com.yss1.lib_jm.WaiterElement.WAITERTYPE.STOP_FLASH;
/**
 * test
 *
 * @author normenhansen
 */
public class Main extends AppBase {

    

    public GamePlayAppState GAME;
    public MainMenu MENU;
    public OptionAppState OPTION;
    public Desk DESK;
    
    private DirectionalLight sun;

    public static void main(String[] args) {
        Main app = new Main();
        app.start();

    }
    
    
    
    @Override
    public void simpleInitApp() {
        super.simpleInitApp();
        USERS=new Users(new ArrayList<Character>(){{add('D');add('L');add('U');add('R');}});
        USERS.initLocalUsers(Tools.initPlayers());
        
        Sett.autoClear = loadSettBool("AUTOCLEAR", false);
        //Sett.original_kingIsBoy = loadSettBool("ORIG_KING_BOY", true);
        //Sett.kingIsBoy = loadSettBool("KING_BOY", Sett.original_kingIsBoy);

        RES=new Ress(getAssetManager());
        RES.initFonts(new HashMap<String,String>(){{
                put("Колибри","Calibri");//F0
                put("Большой","Centuru");//F1
                put("Веселый","Segoe");//F2
                put("Моно","ac");//F3
                }});
        
        INPUT = new GameInput(this);
        INPUT.init();

 //       SOUND=new GameSound(getAssetManager());
//        SOUND.add("Хлоп","vjik");
//        SOUND.add("Замес","dist");
//        SOUND.add("Клик","piu");
//        SOUND.add("КнопкаВниз","bdown");
//        SOUND.add("КнопкаВверх","bup");


         SOUND.init(new HashMap<String,String>(){{
            put("Хлоп","vjik");
            put("Замес","dist");
            put("Клик","piu");
            put("КнопкаВниз","bdown");
            put("КнопкаВверх","bup");}});

        animator=new CardAnimator();
         
        GAME = new GamePlayAppState(this);
        GAME.setGameState(GameState.valueOf(loadSettStr("GAME_STATE","GAMEOVER")));
        GAME.RULES.applyPacketRules(loadSettStr("RULES","RA->true_RB->1"));
        GAME.prepareGame();
        //GAME.RULES.applyPacketRules(loadSettStr("RULES", "RA:true_RB:7"));
        
        MENU = new MainMenu(this);
        
        OPTION = new OptionAppState(this);
        
        DESK = new Desk(this);
        DESK.init();
      
        UIM=new UImanager(this); 
        
        QNET=new Qnet(this);
//        sun = new DirectionalLight();
//        sun.setColor(ColorRGBA.Blue);
//        sun.setDirection(new Vector3f(3f,3f,-2f));
//        rootNode.addLight(sun);
        
//        AmbientLight ambient = new AmbientLight();
//        ambient.setColor(ColorRGBA.Green);
//        rootNode.addLight(ambient);
        
        OPTION.setEnabled(false);
        GAME.setEnabled(false);
        rootNode.attachChild(DESK.getMyNode());
        stateManager.attach(MENU);
        MENU.setEnabled(true);
        

        
        float c_perspect=2.415f;
        cam.setFrustum(1f,Sett.deskHh*2f*c_perspect,-Sett.getSCRatio()/c_perspect,Sett.getSCRatio()/c_perspect,1f/c_perspect,-1f/c_perspect);

        initFlash();
        setDisplayFps(true);
        setDisplayFps(false);
        
       
        setDisplayStatView(false);   
        
        Tools.waiters.initWaiter(this, AD_NEED,"AD loader", Sett.adLoad);
        
        UIM.setGPmaterial(isSignedIn());
        getUIM2().UpdateMoreCaptions(GAME.getGameState()==GamePlayAppState.GameState.GAMEOVER,GAME.LOGIC.getGameKind().ordinal());
        
       }




    @Override
    public ButtonListener getButtonListener(String s) {
        switch (s) {
            case "MENU":
                return MENU;
            case "OPTION":
                return OPTION;
            case "GAME":
                sendEventGA("GEME_PRESSED");
                return GAME;
        }
        return null;
    }

    @Override
    public void start() {
        super.start(); //To change body of generated methods, choose Tools | Templates.
    }
    
    
       
    public UImanager getUIM2() {
        return (UImanager)UIM;
    }
//        
//
//     @Override
    public Ress getRess2() {
        return (Ress)RES;
    }

    @Override
    public AnimEventListener getGameAEL() {
        return GAME;
    }
     
    @Override
    protected void initFlash() {
        flash = new ParticleEmitter("Flash", ParticleMesh.Type.Triangle, 30);
        flash.setMaterial(RES.getMaterial("Spark"));
        flash.setImagesX(10);
        flash.setImagesY(10); // 3x3 texture animation
        flash.setRotateSpeed(4);
       // flash.setQueueBucket(RenderQueue.Bucket.Transparent);
        flash.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 0, 4));
        flash.setGravity(0f, 0f, 2f);
        flash.getParticleInfluencer().setVelocityVariation(1f);
        flash.setRandomAngle(true);
        renderManager.preloadScene(flash);
        flash.setEnabled(false);
    }

    
    @Override
    public void flash(ColorRGBA COL, Vector3f v) {
        if (flash == null) {
            return;
        }
        stopFlash();
        flash.setStartColor(COL);
        flash.setLocalTranslation(v);
        flash.setEnabled(true);
        flash.emitAllParticles();
        Tools.waiters.initWaiter(this, STOP_FLASH, "Flash breaker", 1.5f);
    }
     
    @Override
    protected void stopFlash() {
        flash.killAllParticles();
        flash.setEnabled(false);
    }
    
    
    public void f10Pressed() {
    if (!stateManager.hasState(GAME)) return;
    UIM.switchPopup('B', "", "Left", "Middle", "Right", Wnd.PU_TYPE.YES_ONLY);
    }
    
    public void backPressed() {
         if (stateManager.hasState(GAME))
        {
            stateManager.detach(GAME);
            stateManager.attach(MENU);
        }
        else if (stateManager.hasState(OPTION))
        {
            stateManager.detach(OPTION);
            stateManager.attach(MENU);
        }
        else
        {
            if (MENU.getCurrent_section()==2 && !MENU.isQuit_button_pressed())
            {
                MENU.switchSection(1);
                return;
            }
            GAME.leaveGame();
            stateManager.detach(MENU);
            stop();
        }
    }

     @Override
    public void appStop() {
        GAME.saveState();
        super.appStop();
    }
    
    @Override
    public void stringFromDialog(int dType,String s)
    {
        switch(dType)
        {
            case 0:    
             if (OPTION==null) return;
             OPTION.setUserNameInput(s);
            break;
        }
    }
    
    @Override
    protected void gp_Menu(float X, String LB) {
        if (androidIF == null) {
            return;
        }
        boolean conn = isSignedIn();
        float borders[]={-0.37f,0.14f,0.68f};
        if (X < borders[0]) {
            if (conn) {

                disconnect();
                InfoPlane ip = getUIM().getPlane('P');
                if (ip != null) {
                    ip.applyTexture(1);
                }
            } else {
                runOnConnect = "";
                connect();
            }
            return;
        }
        if (X < borders[1]) {
            if (!conn) {
                runOnConnect = "Achivements";
                connect();
            } else {
                androidIF.gp_ShowAchivements();
            }
        } else if (X < borders[2]) {
            if (!conn) {
                runOnConnect = LB;
                connect();
            } else {
                androidIF.gp_ShowLeaderboard(LB);
            }
        } else {
            if (!conn) {
                runOnConnect = "Multiplayer";
                connect();
            } else {
                androidIF.mp_InvitationInbox();
            }

        }
    }
    
    @Override
    public void simpleUpdate(float tpf) {

        Tools.waiters.checkActions(this, tpf);

        InputEvent iE = Tools.inputEvents.getEvent();
        while (iE != null) {
            
            switch (iE.geteType()) {
                case MOUSE_DOWN:
                    stopFlash();
                    break;
                case BUTTON_DOWN:
                    ((Button3d) iE.getEventObject()).touchDown();
                    break;
                case BUTTON_UP:
                    ((Button3d) iE.getEventObject()).touchUp(iE.isBoolParam());
                    break;
                case CLICK_COIN:
                    if (GAME.getGameState() == GamePlayAppState.GameState.GAMEOVER) {
                        GAME.startLocalGame();
                    }
                    break;
                case CLICK_MENU:
                    if (GAME.isEnabled()) {
                        f10Pressed();
                    }
                    break;
                case CLICK_DT:
                    if (GAME.getGameState() == GamePlayAppState.GameState.INGAME_TAKEWAIT) {
                        GAME.processClickDT();
                    }
                    break;
                case CLICK_NAME:
                    //Sett.forInputDialog = Sett.youName;
                    getInput(0,Tools.getText(610),OPTION.getUserNameInput());
                    break;
                case CLICK_MULTIPANEL:
                    if (iE.getName().contains("pln_google_play")) {
                        gp_Menu(((Vector3f) iE.getEventObject()).x, "CgkIz7XS19MJEAIQAQ");
                    }
                    break;
                case CARD_DOWN:
                    Card ACR2 = (Card) (iE.getEventObject());
                    if (GAME.getGameState() == com.yss1.king3.GamePlayAppState.GameState.INGAME && GAME.validCard(ACR2)) {
                        ACR2.setHi(true, true);
                    } else {
                        ACR2.setHi(true, false);
                    }
                    break;
                case CARD_UP:
                    Card ACR1 = (Card) (iE.getEventObject());
                    ACR1.setHi(false);

                    Vector3f V0 = Tools.vPool.getV3(ACR1.getEndpointM());
                    Vector3f V1 = Tools.vPool.getV3(ACR1.getGe().getLocalTranslation());

                    boolean iMove = ACR1.isValid() && ((FastMath.abs(V0.x) > FastMath.abs(V1.x)) || (Math.abs(V0.x - V1.x) < Sett.maxXmove)) && ((V1.y - V0.y) > Sett.minYmove);
                    if (iMove) {
                        GAME.move2dt(ACR1);
                    } else {
                        if (ACR1.getDistance() < Sett.woAnim) {
                            ACR1.applyEndpoint();
                        } else {
                            getAnimator().animate2endpoint(ACR1, Sett.animTime / 2f, 20, "Distrib");
                        }
                    }
                    Tools.vPool.freeV3(V0);
                    Tools.vPool.freeV3(V1);
                    break;
                case NEW_GAME:
                    break;
                case BACK:
                    backPressed();
                    break;
                case F10:
                    f10Pressed();
                    break;
                default:
            }
            iE=Tools.inputEvents.getEvent();
        }
    }

    @Override
    public void sendEventGA(String s) {
        if (androidIF != null) {
            androidIF.sendEventGA("King3_android", s);
        }
    }

    @Override
    public void execute(WaiterElement par) {

        switch (par.getType()) {
            case START_NET_GAME:
                QNET.reset();
                if (getStateManager().hasState(MENU)) {
                    getStateManager().detach(MENU);
                    MENU.setEnabled(false);
                }
                if (!getStateManager().hasState(GAME)) {
                    getStateManager().attach(GAME);
                    GAME.setEnabled(true);
                }
                //стартуем с ожиданием, сервер выбираем
                GAME.startNetwork(true,true);
                break;
            case DATA_RECEIVED:
                GAME.dataReceived();
                break;

            case SERVER_WAIT:
                //стартуем без ожидания, сервер не выбираем, он нулевой в массиве юзеров
                GAME.startNetwork(false,true);
                break;

            case SUCCESS_SEND:
                QNET.sucessSend(par.getInfo());
                break;
            case ERROR_SEND:
                QNET.reSend(par.getInfo());
                break;
            case AD_LOADED:
                //showAndroidMessage("AD CALL", "AD LOADED");
                break;
            case AD_CLOSED:
                GAME.adClosed();
                break;
            case BANNER_VISIBLE:
                GAME.prepareBanner(isBannerVisible());
                sendEventGA("Banner visible is "+isBannerVisible());

                break;
            case STOP_FLASH:
                stopFlash();
                break;
            case AD_NEED:
                if (androidIF != null) {
                    androidIF.loadAd();
                }
                Tools.waiters.initWaiter(this, AD_NEED, "AD loader", Sett.adLoad);
                break;
            case SEND_ALIVE:
                //QNET.packetSend(IM_ALIVE, "Alive");
                break;
        }
    }

    @Override
    public void loseFocus() {
        //writeAndroidLog("reset="+Tools.waiters.resetWaiter(this, AD_NEED));
        if (Sett.is_net && isSignedIn()) {
            QNET.packetSend(IM_PAUSED, "focus lost");
        }
        super.loseFocus();
    }

    @Override
    public void gainFocus() {
        super.gainFocus();
        //Tools.waiters.initWaiter(this, AD_NEED, "AD loader", Sett.adLoad);
        if (Sett.is_net) {
            ToolsBase.waiters.initWaiter(this, SEND_ALIVE, "Alive", 0.7f);
        }
    }
    
    
    
    
}
