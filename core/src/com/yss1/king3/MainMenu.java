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
import com.jme3.font.BitmapText;
import com.jme3.scene.Node;
import com.yss1.lib_jm.IExecutor;
import com.yss1.lib_jm.IPUowner;
import com.yss1.lib_jm.InfoPlane;
import com.yss1.lib_jm.WaiterElement;
import com.yss1.lib_jm.Wnd;
import java.util.ArrayList;
import java.util.List;
import static com.yss1.lib_jm.WaiterElement.WAITERTYPE.*;
/**
 *
 * @author ys
 */
public class MainMenu extends AbstractAppState implements 
        AnimEventListener, 
        ButtonListener, 
        IPUowner,
        IExecutor
        {

    private Node menu;
    private Main ap;
    private int slideNo;
    private int current_section;
    private boolean quit_button_pressed;
    private int totalSlides=24;
//    private boolean loaded;
    private List<BitmapText> btl=new ArrayList<>();
    public boolean isQuit_button_pressed() {
        return quit_button_pressed;
    }

    public MainMenu(Main a) {
        ap = a;
        menu = new Node("menuNode");
//        loaded=false;
    }
    
public int getCurrent_section() {
        return current_section;
    }
   
  public void attachGooglePlay(boolean att)
    {
        InfoPlane ip=ap.getUIM().getPlane('P');
        if (ip!=null) 
        {
            if (att)
            menu.attachChild(ip.getNodePlane());
            else
            menu.detachChild(ip.getNodePlane());
        }
    }
  
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app); 
         ap.getRootNode().attachChild(menu);
         current_section=1;
         
         ap.UIM.attachMyButtons(this);
         setSection();
         attachGooglePlay(true);
         ap.UIM.initCheckButton("btn_sound",Sett.sound_on);
         ap.UIM.initRadioGroup(Sett.lang==1?"btn_lang_ru":"btn_lang_eng",1);
         ap.getUIM2().setGameButton(ap.GAME.getGameState()==GamePlayAppState.GameState.GAMEOVER);
         ap.UIM.getPopup('A').setMyOwner(this);
         

    }


  
    public void switchSection(int st)
    {
        if (current_section==st) return;
        ap.UIM.flyOutButtons(this, current_section);
        current_section=st;
        ap.UIM.flyInButtons(this, current_section);
    }
    
    private void setSection()
    {
       switch (current_section)
       {
           case 1:
               ap.UIM.hideButtons(this, 2);
               ap.UIM.unHideButtons(this, 1);
               break;
           case 2:
               ap.UIM.hideButtons(this, 1);
               ap.UIM.unHideButtons(this, 2);
               break;    
       }
       
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
        quit_button_pressed=false;  
        } else {
        }
    }

    @Override
    public void cleanup() {
        super.cleanup(); 
        ap.UIM.detachMyButtons(this);
        attachGooglePlay(false);
        ap.getRootNode().detachChild(menu);
    }

    
    @Override
    public void clickButton(String name) {
        if (name.contains("btn_game")) {
            Tools.waiters.initWaiter(this,CLICK_BUTTON1,"Button pressed");
        } else if (name.contains("btn_options")) {
            Tools.waiters.initWaiter(this,CLICK_BUTTON2,"Button pressed");
            //JmeSystem.showErrorDialog(JmeSystem.getFullName());
//                SoftTextDialogInputListener dt = new SoftTextDialogInputListener() {
//        @Override
//        public void onSoftText(int action, String text) {
//            System.out.println("data="+text);
//        }
//    };
//    JmeSystem.getSoftTextDialogInput().requestDialog(SoftTextDialogInput.TEXT_ENTRY_DIALOG, "title", "qq", dt);
//

        }
        else if (name.contains("btn_help"))
        {
            slideNo=0;
            ap.UIM.showPopup('A',String.format(Tools.getText(700),slideNo+1,totalSlides),Tools.getText(710+slideNo),Tools.getText(701),Tools.getText(702),Tools.getText(703), Wnd.PU_TYPE.NEXT_ONLY_CLS,Sett.helpSize);
            ap.UIM.detachMyButtons(this);            
            attachGooglePlay(false);
        }else if (name.contains("btn_multiplayer")) {
            switchSection(2);
        }else if (name.contains("btn_home")) {
            switchSection(1);
        }else if (name.contains("btn_quick")) {
            ap.quickStart();
        }else if (name.contains("btn_select")) {
            ap.selectOpponents(true);
        }else if (name.contains("btn_invitations")) {
            ap.invitationInbox();
        }
         else {
            quit_button_pressed=true;
            ap.backPressed();
        }
    }


    @Override
    public void selectRadio(String name) {
        if (name.contains("btn_lang"))
        {
            if (name.contains("btn_lang_ru"))
            {
            Sett.lang=1;
        }
            else
            {
            Sett.lang=2;    
        }
            
            if (!Sett.is_net) ap.USERS.initLocalUsers(Tools.initPlayers());
            ap.UIM.updateCaptions();
            ap.getUIM2().UpdateMoreCaptions(ap.GAME.getGameState()==GamePlayAppState.GameState.GAMEOVER,ap.GAME.LOGIC.getGameKind().ordinal());
        }
    }

    @Override
    public void changeCheckbox(String name, boolean state) {
        if (name.contains("btn_sound")) {
            Sett.sound_on = state;
        
        }
    }
    
    @Override
    public Node getParentNode(String n)
    {
      return menu;
    }
    
    @Override
    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
        
        channel.setAnim("Idle");
        channel.setLoopMode(LoopMode.DontLoop);
        Tools.waiters.checkActions(this);
        if (animName.contains("flyOut"))
        {
        }
        else if (animName.contains("flyIn"))
        {
        }
    }

    @Override
    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
    }
    
    @Override
    public void popUpClosing(Wnd W,String res) {
        
        if (res.contains("pubtn_left")) {
            slideNo--;
            ap.UIM.showPopup('A',String.format(Tools.getText(700),slideNo+1,totalSlides),Tools.getText(710+slideNo),Tools.getText(701),Tools.getText(702),Tools.getText(703),  slideNo==0?Wnd.PU_TYPE.NEXT_ONLY_CLS:Wnd.PU_TYPE.PREV_NEXT_CLS,Sett.helpSize);
        } else if (res.contains("pubtn_right")) {
            slideNo++;
            ap.UIM.showPopup('A',String.format(Tools.getText(700),slideNo+1,totalSlides),Tools.getText(710+slideNo),Tools.getText(701),Tools.getText(702),Tools.getText(703), slideNo==(totalSlides-1)?Wnd.PU_TYPE.PREV_ONLY_CLS:Wnd.PU_TYPE.PREV_NEXT_CLS,Sett.helpSize);
        }
    }

    @Override
    public void popUpClosed(Wnd W,String s) {
      ap.UIM.attachMyButtons(this);
      attachGooglePlay(true);
     }

    @Override
    public void execute(WaiterElement par) {
        switch (par.getType())
        {
            case CLICK_BUTTON1:
                ap.getStateManager().detach(this);
                this.setEnabled(false);
                ap.getStateManager().attach(ap.GAME);
                ap.GAME.setEnabled(true);
                break;
            case CLICK_BUTTON2:
                ap.getStateManager().detach(this);
                this.setEnabled(false);
                ap.getStateManager().attach(ap.OPTION);
                ap.OPTION.setEnabled(true);
                break;    
        }
    }
    
    @Override
    public AnimEventListener getAEL() {
        return this;
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        Tools.waiters.checkActions(this,tpf);
    }
    
    
}
