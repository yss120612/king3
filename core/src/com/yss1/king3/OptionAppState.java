/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yss1.king3;

import com.yss1.lib_jm.ButtonListener;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.yss1.lib_jm.IExecutor;
import com.yss1.lib_jm.IPUowner;
import com.yss1.lib_jm.InfoPlane;
import com.yss1.lib_jm.WaiterElement;
import static com.yss1.lib_jm.WaiterElement.WAITERTYPE.CLICK_BUTTON1;
import com.yss1.lib_jm.Wnd;

/**
 *
 * @author ys
 */
public class OptionAppState extends AbstractAppState implements  
        AnimEventListener, 
        ButtonListener, 
        IPUowner,
        IExecutor
{
    
private Node options;
private Main ap;
private int currentRub;
private String currentName;
private boolean currentKingBoy;
private boolean currentScreenOn;
private boolean autoclear;

public OptionAppState(Main a) {
        ap = a;
        options = new Node("optionsNode");
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);

        ap.getRootNode().attachChild(options);

        //screen on
        InfoPlane ip = ap.UIM.getPlane('I');
        if (ip != null) {
            options.attachChild(ip.getNodePlane());
        }

        //king is boy
        ip = ap.UIM.getPlane('H');
        if (ip != null) {
            options.attachChild(ip.getNodePlane());
            if (Sett.is_net) {
                ip.setText1Color(ColorRGBA.Gray);
            } else {
                ip.setText1Color(ColorRGBA.Green);
            }
        }

        //rubsel
        ip = ap.UIM.getPlane('E');
        if (ip != null) {
            options.attachChild(ip.getNodePlane());
        }

        //autoclear
        ip = ap.UIM.getPlane('J');
        if (ip != null) {
            options.attachChild(ip.getNodePlane());
        }

        ap.UIM.attachMyButtons(this);//обязательно после ^

        //name editor
        currentName = Sett.youName;
        ip = ap.UIM.getPlane('G');
        if (ip != null) {
            options.attachChild(ip.getNodePlane());
        }
        setUserNameInput(currentName);

        autoclear = Sett.autoClear;
        currentRub = ap.GAME.getBK();
        currentKingBoy = ap.GAME.RULES.isKingBoy();
        currentScreenOn = Sett.screen_on;
        ap.UIM.initRadioGroup("btn_back_" + currentRub, 2);
        ap.UIM.initCheckButton("btn_king_boy", currentKingBoy);
        ap.UIM.getButton("btn_king_boy").setEnabled(!Sett.is_net);
        ap.UIM.initCheckButton("btn_autoclear", autoclear);
        ap.UIM.initCheckButton("btn_screen_on", currentScreenOn);
        ap.UIM.getPopup('A').setMyOwner(this);
    }

    
    @Override
    public void setEnabled(boolean enabled) {
          super.setEnabled(enabled); //To change body of generated methods, choose Tools | Templates.
            }
    
    @Override
    public void update(float tpf) {
        
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        ap.UIM.detachMyButtons(this);
        ap.getRootNode().detachChild(options);
        ap.UIM.detachMyButtons(this);
        InfoPlane ip=ap.UIM.getPlane('E');
         if (ip!=null) 
         {
             
             options.detachChild(ip.getNodePlane());
         }
         
         ip=ap.UIM.getPlane('G');
         if (ip!=null) 
         {
             options.detachChild(ip.getNodePlane());
         }
         
         ip=ap.UIM.getPlane('H');
         if (ip!=null) 
         {
             options.detachChild(ip.getNodePlane());
         }
         ip=ap.UIM.getPlane('I');
         if (ip!=null) 
         {
             options.detachChild(ip.getNodePlane());
         }
         
        //System.out.println("OPTION CLEANUP");
    }

    public void setUserNameInput(String un)
   {
        InfoPlane IP=ap.UIM.getPlane('G');
        
        if (IP!=null && un!=null && !un.isEmpty())
        {
          un.replace('[', '{');
          un.replace(']', '}');
          currentName=un;  
          IP.setText1(Tools.getText(302)+" [ "+un+" ]");
          IP.setText1ColorA(ColorRGBA.Yellow,"(?<=\\[)(.+)(?=\\])");
        }
   }

     public String getUserNameInput() {
            return currentName;
    }
       
@Override
    public void clickButton(String name) {
        if (name.contains("btn_ok"))
        {
            ap.GAME.setBK(currentRub);
            ap.GAME.RULES.setKingBoy(currentKingBoy);
            
            Sett.screen_on=currentScreenOn;
            Sett.autoClear=autoclear;
            if (!currentName.equals(Sett.youName))
            {
                Sett.youName=currentName;
                if (!Sett.is_net) ap.USERS.initLocalUsers(Tools.initPlayers());
                //Tools.initPlayers();
                //ap.UIM.setText2(Sett.youName, 'D');
            }
        }
        else if (name.contains("btn_cancel"))
        {
        }
        else
        {
            return;
        }
        Tools.waiters.initWaiter(this,CLICK_BUTTON1,"Button pressed");
        ap.UIM.closePopup('A');
    }
    
@Override
    public void selectRadio(String name) {
        if (name.contains("btn_back_"))
        {
            try
            {
               int R=Integer.parseInt(name.substring(name.length()-1));
               currentRub =  R;
            }
            catch(Exception E)
            {

            }
        }
    }

@Override
    public Node getParentNode(String n)
    {
        if (n.contains("_back_")) return (Node)options.getChild("pln_rubsel_node");
        else if (n.equals("btn_king_boy")) return (Node)options.getChild("pln_tgt_king_boy_node");
        else if (n.equals("btn_screen_on")) return (Node)options.getChild("pln_tgt_screen_on_node");
        else if (n.equals("btn_autoclear")) return (Node)options.getChild("pln_tgt_autoclear_node");
        else return options;
    }
    
@Override
    public void changeCheckbox(String name, boolean state) {
        if (name.contains("btn_screen_on"))
        {
            currentScreenOn=state;
        }
        else if (name.contains("btn_king_boy"))
        {
            currentKingBoy=state;
            if (ap.GAME.LOGIC.isBoysDepend() && state!=ap.GAME.RULES.isKingBoy())
            {
             ap.UIM.showPopup('A',Tools.getText(307),Tools.getText(308),"",Tools.getText(1),"", Wnd.PU_TYPE.YES_ONLY);
            }
        }
        
        else if (name.contains("btn_autoclear"))
        {
            autoclear=state;
        }
    }

@Override
    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
        channel.setAnim("Idle");
        channel.setLoopMode(LoopMode.DontLoop);
        Tools.waiters.checkActions(this);
    }

@Override
    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
    }
    
@Override
    public void popUpClosing(Wnd W,String result)
    {//нажата кнопка
        
    }
    
@Override
    public void popUpClosed(Wnd W,String nm)
    {//закрыто
    }

    @Override
    public void execute(WaiterElement par) {
        switch (par.getType())
        {
            case CLICK_BUTTON1:
                ap.getStateManager().detach(this);
                this.setEnabled(false);
                ap.getStateManager().attach(ap.MENU);
                ap.MENU.setEnabled(true);         
            break;
        }
    }

    @Override
    public AnimEventListener getAEL() {
        return this;
    }
    
}
