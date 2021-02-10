/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yss1.king3;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.yss1.king3.GameLogic.GAME_KIND;
import com.yss1.lib_jm.ButtonListener;
import com.yss1.lib_jm.Card;
import com.yss1.lib_jm.CardSetBase;
import com.yss1.lib_jm.IPUowner;
import com.yss1.lib_jm.NetPacket;
import com.yss1.lib_jm.Stock;
import com.yss1.lib_jm.UserBase;
import com.yss1.lib_jm.Wnd;
import com.yss1.lib_jm.Wnd.PU_TYPE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.yss1.lib_jm.NetPacket.PType.DESK_CLEARED;
import static com.yss1.lib_jm.NetPacket.PType.GAME_FINISHED;
import static com.yss1.lib_jm.NetPacket.PType.IM_LEAVE;
import static com.yss1.lib_jm.NetPacket.PType.INFO_ALIVE;
import static com.yss1.lib_jm.NetPacket.PType.MOVE_MAKE;
import static com.yss1.lib_jm.NetPacket.PType.MOVE_NEED;
import static com.yss1.lib_jm.NetPacket.PType.NEXT_GAME;
import static com.yss1.lib_jm.NetPacket.PType.NEXT_PARTY;
import static com.yss1.lib_jm.NetPacket.PType.PARTY_FINISHED;
import static com.yss1.lib_jm.NetPacket.PType.RASKLAD;
import static com.yss1.lib_jm.WaiterElement.WAITERTYPE.BANNER_VISIBLE;
import static com.yss1.lib_jm.WaiterElement.WAITERTYPE.SERVER_WAIT;

/**
 *
 * @author ys
 */
public class GamePlayAppState extends AbstractAppState 
implements 
        ButtonListener, 
        AnimEventListener , 
        IPUowner{

   
    public enum GameState {
        INGAME,INGAME_TAKEWAIT, PARTYOVER, GAMEOVER, ASKGAMEOVER
    };
    
    //private enum SRV_WAIT{SW_MOVE,SW_CLEAR};
    
    private GameState gameState;
    private GameState oldGameState;
    private Node nroot, ndp, nlp, nup, nrp, ndt, nbt;
    private com.yss1.king3.Main ap;
    public GameLogic LOGIC;
    private int clearCounter;
    
    private Card cardInFly;
    private char whoMove;
    private char whoFirst;
    private char whoFirstGame;
    
    private Stock stock;
    private final Map<Character, CardSet> cardSets=new HashMap<>();
    private AppStateManager ASM;
    private boolean loaded;
    private boolean i_server;
    
    private String endNetwork;
    private float waitClear;
    
    
    private final List<Character> chPlayer;
    private final List<Character> chAll;    
    
    public GameRules RULES;
    
    public GamePlayAppState(Application app) {
        ap = (Main) app;
        RULES=new GameRules();
        chPlayer= new ArrayList<>();
         chPlayer.add('D');
         chPlayer.add('L');
         chPlayer.add('U');
         chPlayer.add('R');
         chAll=new ArrayList<>(chPlayer);
         chAll.add('T');
         chAll.add('B');
        nroot = ap.getRootNode();
        ndp = new Node("pDown");
        nrp = new Node("pRight");
        nlp = new Node("pLeft");
        nup = new Node("pUp");
        ndt = new Node("pDesk");
        
        nbt = new Node("pBito");
        //nbt.setCullHint(Spatial.CullHint.Always);
        
        Vector3f V1=Tools.vPool.getV3(0,0,1);
        Quaternion Q =Tools.vPool.getQt();
        Q.fromAngleAxis(FastMath.HALF_PI, V1);
        Sett.rTrans.x=Sett.rTrans.x/1.777f*Sett.getSCRatio();
        nrp.setLocalTranslation(Sett.rTrans);
        nrp.setLocalRotation(Q);

        Q.fromAngleAxis(FastMath.PI, V1);
        nup.setLocalTranslation(Sett.uTrans);
        nup.setLocalRotation(Q);

        Q.fromAngleAxis(FastMath.HALF_PI * 3,V1);
        Sett.lTrans.x=Sett.lTrans.x/1.777f*Sett.getSCRatio();
        nlp.setLocalTranslation(Sett.lTrans);
        nlp.setLocalRotation(Q);

        ndp.setLocalTranslation(Sett.dTrans);

        ndt.setLocalScale(Sett.scaleDesk);
        ndt.setLocalTranslation(Sett.dtTrans);
        nbt.setLocalTranslation(Sett.dtTrans);

        Tools.vPool.freeQt(Q);
        Tools.vPool.freeV3(V1);

        gameState = GameState.GAMEOVER;
        oldGameState = GameState.GAMEOVER;
        loaded=false;//признак - в игру зашли (игровое окно открывалось)
        //Sett.is_net=false;
        whoFirstGame='N';
        
    }
    
     @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app); //To change body of generated methods, choose Tools | Templates.
        //ASM = stateManager;
        //if (stock==null) stock = new Stock(ap, Sett.MinRank, ap.loadSettInt("CARDS_BACK", 1), Sett.has_jockers);
        
        nroot = ap.getRootNode();
        nroot.attachChild(nup);
        nroot.attachChild(ndp);
        nroot.attachChild(nlp);
        nroot.attachChild(nrp);
        nroot.attachChild(ndt);
        nroot.attachChild(nbt);
        if (!loaded) readState();
        showNames();
        initGameScreen(0);
        ap.DESK.attachPlanes();
        if (gameState!=GameState.GAMEOVER) ap.DESK.attachCurrPlanes();
        if (Sett.screen_on) ap.setScrOFF(false);
        ap.UIM.getPopup('A').setMyOwner(this);
        ap.UIM.getPopup('B').setMyOwner(this);
        ap.getUIM2().setTextGame(ap.GAME.LOGIC.getGameKind().ordinal());
    }
    
    public float getRatio()
    {
        return stock.getRatio();
    }
    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
    

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled); //To change body of generated methods, choose Tools | Templates.
        if (enabled)
        {
            //if (!loaded) {showPanelCard('X',stock.getCard("9D")); readState();}
        }
           else
        {
            
        }
        
        
    }

    @Override
    public boolean isEnabled() {
        return super.isEnabled(); //To change body of generated methods, choose Tools | Templates.
    }

    public Stock getStock() {
        return stock;
    }
    
    public void prepareGame()
    {
        stock = new Stock(ap, Sett.MinRank, ap.loadSettInt("CARDS_BACK", 1), Sett.has_jockers);
        cardSets.put('D', new CardSet(ap, ndp));
        cardSets.put('L', new CardSet(ap, nlp));
        cardSets.put('U', new CardSet(ap, nup));
        cardSets.put('R', new CardSet(ap, nrp));
        cardSets.put('T', new CardSet(ap, ndt));
        cardSets.put('B', new CardSet(ap, nbt));
         LOGIC = new GameLogic(
                new HashMap<Character,CardSetBase>(){{
                put('D',cardSets.get('D'));
                put('L',cardSets.get('L'));
                put('R',cardSets.get('R'));
                put('U',cardSets.get('U'));
                put('B',cardSets.get('B'));
                put('T',cardSets.get('T'));
                }});
        LOGIC.setRules(RULES);
    }
    
    public void initGameScreen(int tex)
    {
        if (gameState==GameState.PARTYOVER) 
        {
            
            if (loaded) ap.UIM.showPopup('A',Tools.getText(5),Tools.getText(11),Tools.getText(4),Tools.getText(3),"", PU_TYPE.YES_ONLY);
            return;
        }
        if (gameState!=GameState.GAMEOVER) return;

        stock.mix();
        getCS('T').clear();
        get2('T', stock.workSize());
        if (ap.DESK!=null) 
        {
            ap.DESK.idleCoin();
            ap.DESK.showCoin(new Vector3f(0, 0, 3f),tex);
        }
        getCS('T').putRound();
    }

    public boolean validCard(Card c) {
        return LOGIC.getLegalMovs() != null && LOGIC.getLegalMovs().contains(c) && whoMove == 'D' && cardInFly == null;
    }

    public CardSet getCS(char cs) {
        return cardSets.get(cs);
    }

    public void clear() {
        for (Character cr : chAll) {
            getCS(cr).clear();
        }
    }

    private void startGame() {
        
        LOGIC.newGame();
        ap.DESK.attachCurrPlanes();
        showScore('A');
    }

    public void startNetwork(boolean wait, boolean sendRules)
    {
        Sett.is_net = true;
        endNetwork="";
        gameState = GameState.GAMEOVER;
        
        if (!wait)
        {
            if (ap.USERS.isIServer())
            {
                whoFirst='D';
                ap.USERS.setNewServer(ap.USERS.getUser('L').getUid());
                send2All(NEXT_GAME,makeInitPacket(sendRules));
                startNetGame();
            }
            return;
        }
        
        if (ap.USERS.isINewServer())//присваивается тому кто создал рум
                {
                  ap.USERS.setServer(ap.USERS.getI().getUid());
                  whoFirst='D';
                  ap.USERS.setNewServer(ap.USERS.getUser('L').getUid());
                  send2All(NEXT_GAME,makeInitPacket(sendRules));
                  startNetGame();
                }
                else
                {
                   //если все оказались клиенты (через квикстарт), то через 15 сек. сервером
                   //становиться 0й юзер и начинаем без ожиданий 
                   initGameScreen(1);
                   Tools.waiters.initWaiter(ap, SERVER_WAIT, "wait server", 15);
                }
    }
    
     public void  startNetGame() {
        ap.sendEventGA("NEW_NET_GAME");
        i_server=ap.USERS.isIServer();
        startGame();
        startNetParty();
    }
     
     //формируем пакет инициализирующий игру
    //создается сервером и рассылается клиентам
    private String makeInitPacket(boolean packRules) {
        if (!ap.USERS.isIServer()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("U:");
        sb.append(ap.USERS.getI().getUid());
        sb.append("&");
        sb.append("R:");
        if (packRules)
        {
        RULES.backupRules();
        sb.append(RULES.getPacketRules());
        }
        else
        {
         sb.append("NONE");   
        }
        return sb.toString();
    }

    private String applyInitPacket(String data) {
        String result = "";
        String[] as = data.split("&");
        if (as.length == 0) {
            return result;
        }
        for (String si : as) {
            String[] pair = si.split(":");
            if (pair.length != 2) {
                continue;
            }
            switch (pair[0]) {
                case "U":
                    result = pair[1];
                case "R":
                    if (!pair[1].contains("NONE"))
                    {
                    RULES.applyPacketRules(pair[1]);
                    }
            }
        }
        return result;
    }

    
    public void  startLocalGame() {
        if (!isEnabled()) {
            return;
        }
        Sett.is_net = false;
        if (gameState != GameState.GAMEOVER) {
            oldGameState = gameState;
            gameState = GameState.ASKGAMEOVER;
            ap.UIM.showPopup('A', Tools.getText(9), Tools.getText(10), Tools.getText(2), Tools.getText(1), "", PU_TYPE.OK_CANCEL);
            return;
        }

        ap.sendEventGA("NEW_GAME");
        if (whoFirstGame=='N')
        {
            whoFirst = chPlayer.get(Tools.rand(chPlayer.size()-1));
        }
        else
        {
            whoFirst = whoFirstGame;
        }
        startGame();
        startLocalParty();
    }


    private void startParty()    {
        LOGIC.newParty();
        
        ap.getUIM2().setTextGame(ap.GAME.LOGIC.getGameKind().ordinal());
        gameState = GameState.INGAME;
        ap.DESK.idleCoin();
        clear();

        stock.mix();
        for (Character ch:chPlayer)
        {
        get2(ch,8);
        getCS(ch).distribute(false);
        }
        
        ap.SOUND.playSound("Замес");

        whoMove = whoFirst;
        whoFirst=next(whoFirst);
        
        armEndParty(false);
        armCleared(false);
        
        cardInFly = null;
        
    }

    public void startNetParty() {
        if (ap.QNET == null) {
            return;
        }

        if (i_server) {
            startParty();
            whoMove = 'D';
            StringBuilder Sb = cardSets.get('R').forNetSend(cardSets.get('U').forNetSend(cardSets.get('L').forNetSend(cardSets.get('D').forNetSend(null, 'D'), 'L'), 'U'), 'R');
            ap.QNET.packetSend(RASKLAD, Sb);
            updateTricks();
            nextMove(true);
        } else {
            LOGIC.newParty();
            ap.getUIM2().setTextGame(ap.GAME.LOGIC.getGameKind().ordinal());
            gameState = GameState.INGAME;
            ap.DESK.idleCoin();
            clear();
            armEndParty(false);
            armCleared(false);
        }
    }

    public  void startLocalParty() {
        if (!isEnabled()) {
            return;
        } 
        i_server=false;
        
        startParty();
        nextMove(true);
    }

    
    private void nextMove(boolean andMove) {
        int nDT = cardSets.get('T').length();
        if (nDT == Sett.playersCount) {
            if (Sett.is_net) {
                armCleared(true);
            } else {
                getCS('D').setNeedClear(true);
            }
            LOGIC.clearLegalMovies();
            updateTricks();
            takeWait();
            return;
        } else if (nDT > 0) {
            nextPlayer();
            updateTricks();
        } else {
            if (Sett.is_net) {
            } else {
            }
        }
        if (andMove)
        {
          move();
        }
    }

   
    
    public void move() {
        //если бот запрашиваем карту и ходим если Я обновляем массив легальных карт
        if (gameState != GameState.INGAME) {
            return;
        }
        Card C;
        if (whoMove != 'D') {
            if (Sett.is_net) {
                LOGIC.clearLegalMovies();
            } else {
                C = LOGIC.getMove(whoMove);
                if (C == null) {
                    System.out.println("Error CARD is null");
                } else {
                    move2dt(C);
                }
            }
        } else {
            LOGIC.refreshLegalMovies('D');
            //ap.writeAndroidLog(writeDiag());

        }
    }
    
    private void updateTricks() {
        boolean positive = LOGIC.isTakeGood();
        int trk;
        UImanager uim = (UImanager) ap.getUIM();
        trk = cardSets.get('R').getTricks();
        boolean alive = ap.USERS.getUser('R').isActive();
        boolean inGame = gameState == GameState.INGAME;
        if (!positive) {
            trk = -trk;
        }
        if (!alive) {
            trk = 999;//text = ""
        }
        uim.setTextTricks(trk, 'R', inGame && whoMove == 'R', Sett.is_net && showEye('R'), alive);

        trk = cardSets.get('U').getTricks();
        alive = ap.USERS.getUser('U').isActive();
        if (!positive) {
            trk = -trk;
        }
        if (!alive) {
            trk = 999;
        }
        uim.setTextTricks(trk, 'U', inGame && whoMove == 'U', Sett.is_net && showEye('U'), alive);

        trk = cardSets.get('L').getTricks();
        alive = ap.USERS.getUser('L').isActive();
        if (!positive) {
            trk = -trk;
        }
        if (!alive) {
            trk = 999;
        }
        uim.setTextTricks(trk, 'L', inGame && whoMove == 'L', Sett.is_net && showEye('L'), alive);

        trk = cardSets.get('D').getTricks();
        alive = ap.USERS.getUser('D').isActive();
        if (!positive) {
            trk = -trk;
        }
        if (!alive) {
            trk = 999;
        }
        uim.setTextTricks(trk, 'D', inGame && whoMove == 'D', showEye('D'), alive);

    }
    
    private boolean showEye(char ch)
    {
        CardSet cs=cardSets.get(ch);
        return cs.isNeedClear() || !cs.isEndParty() && (gameState==GameState.PARTYOVER||gameState==GameState.GAMEOVER);
    }
    
    private void armCleared(boolean v) {
        for (Character ch : chPlayer) {
            getCS(ch).setNeedClear(v);
        }
    }

    private void armEndParty(boolean v) {
        for (Character ch : chPlayer) {
            getCS(ch).setEndParty(v);
        }
    }
    
    
    
    private void takeWait()
    {
        
        gameState=GameState.INGAME_TAKEWAIT;
        waitClear=0; 
    }
    
    
    public void processClickDT() {
        gameState = GameState.INGAME;
        whoMove = LOGIC.whoTake();
        clearCounter = 0;
        showScore(whoMove);
        getCS('T').clearDT(whoMove);
        for (Card C: getCS('T').getCards()) getCS('B').add(C);
        getCS('T').clear();
        //getCS('B').add(cardSets.get('T').remove(cardSets.get('T').find(control.getSpatial().getName())));
    }
    
     private void processClearTrick(boolean partyover) {
        cardSets.get('D').setNeedClear(false);
        updateTricks();
        if (Sett.is_net) {//уведомление all, что my стол чист
            send2All(DESK_CLEARED, ap.USERS.getI2S('D') + "");
        }
        
        if (partyover) {
            //armEndParty(true);
        } else {
            if (Sett.is_net) {
                if (i_server) {
                    if (allCleared())  {
                        if (whoMove == 'D') {
                            nextMove(true);
                        } else {
                            send2All(MOVE_NEED, whoMove + "");
                        }
                    }
                }
            } else {
                nextMove(true);
            }
        }
    }
    
    private boolean allCleared() {
        boolean nc = getCS('D').isNeedClear()
                    || getCS('L').isNeedClear()
                    || getCS('U').isNeedClear()
                    || getCS('R').isNeedClear();
        return !nc;
    }

    private boolean allPartyAgree() {
        boolean nc = getCS('D').isEndParty()
                    && getCS('L').isEndParty()
                    && getCS('U').isEndParty()
                    && getCS('R').isEndParty();
        return nc;
    }

    
    private void send2All(NetPacket.PType T, String dt)
    {
      ap.QNET.packetSend(T, dt);
    }
    
    private boolean checkPartyOver() {

        //if (Sett.is_net && !i_server) return false;
        
        if (!LOGIC.partyIsOver()) {
            return false;
        }

        whoMove = 'N';
        updateTricks();
        boolean GameOVER=LOGIC.gameIsOver();
        
        checkAchivements(GameOVER);
        if (!GameOVER) {//party is over
            gameState = GameState.PARTYOVER;
            ap.UIM.showPopup('A',Tools.getText(5), String.format(Tools.getText(6), Tools.getText(479+LOGIC.Next().ordinal())),Tools.getText(1),Tools.getText(3),"", PU_TYPE.YES_ONLY);
        } else {//game is over
            ap.DESK.detachCurrPlanes();
            gameState = GameState.GAMEOVER;
            String heroes = "";
            for (int i = 0; i < LOGIC.getWinners().size(); i++) {
                if (i > 0) {
                    heroes += ", ";
                }
                heroes += ap.USERS.getUser(LOGIC.getWinners().get(i)).getName();
            }
            int SC=LOGIC.getMyPoints();
            int REC=ap.HBOARD.regRecord(SC);
            if (REC>0)
            {
              ap.UIM.showPopup('A',Tools.getText(7),"",Tools.getText(4),Tools.getText(3),"", PU_TYPE.OK_CANCEL,0.5f);             
              ap.UIM.getPopup('A').setTableMessage(String.format(Tools.getText(12), heroes, (LOGIC.getWinnerPoints()),SC,REC)+"\n"+ap.HBOARD.getTable(), 0.29f, ap.HBOARD.getReg());
            }
            else
            {
              ap.UIM.showPopup('A',Tools.getText(7),String.format(Tools.getText(8), heroes, (LOGIC.getWinnerPoints())),Tools.getText(4),Tools.getText(3),"", PU_TYPE.OK_CANCEL);
            }
            
            //ap.UIM.showPopup('A',Tools.getText(7),String.format(Tools.getText(8), heroes, (LOGIC.getWinnerPoints())),Tools.getText(4),Tools.getText(3),"", PU_TYPE.OK_CANCEL);
            //showPopup(Tools.getText(7), String.format(Tools.getText(8), heroes, (LOGIC.getWinnerPoints() - Sett.startMoney)), 1);
        }
        return true;
    }

    void checkAchivements(boolean GOver) {
       boolean HALF=LOGIC.Next()==GAME_KIND.NO_ALL;
       int SC= LOGIC.getMyPoints();
       if (HALF) LOGIC.setHalfMyPoint(SC);
       if (!ap.isSignedIn()) return;
       boolean IWin;

       if (HALF) 
       {
            if (SC==0) ap.unlockAchivment("CgkIz7XS19MJEAIQBA");
       }
       
       if (GOver)
       {
       IWin=LOGIC.getWinners().contains('D');
       ap.submitScore("CgkIz7XS19MJEAIQAQ",SC);
       if (SC>=75) ap.unlockAchivment("CgkIz7XS19MJEAIQAg");
       if (SC>=100) ap.unlockAchivment("CgkIz7XS19MJEAIQAw");
       if (SC>=192) ap.unlockAchivment("CgkIz7XS19MJEAIQBw");
       if (SC-LOGIC.getHalfMyPoint()>=192) ap.unlockAchivment("CgkIz7XS19MJEAIQBQ");
       if (IWin) ap.incrementAchivment("CgkIz7XS19MJEAIQCA", 1);
       }
    }

    private char next(char old)  {
        char result='D';
        switch (old) {
            case 'D':
                result = 'L';
                break;
            case 'L':
                result = 'U';
                break;
            case 'U':
                result = 'R';
                break;
            case 'R':
                result = 'D';
                break;
        }
        return result;
    }
    
    private char nextPlayer() {
        whoMove=next(whoMove);
        return whoMove;
    }

    public void move2dt(Card C) {
        
        Transform TR = C.getGe().getWorldTransform();
        if (cardSets.get(C.getOwner()).isMy(C.getName())) {
            cardSets.get(C.getOwner()).remove(C);
            cardSets.get('T').add(C);
            cardSets.get('T').generateEndpointDT(C);
            C.getGe().setLocalTransform(Tools.getLocalTransformToPreserveWorldTransform(C.getGe().getParent().getWorldTransform(), TR));
            cardSets.get(C.getOwner()).distribute(true);

            cardInFly = C;
            if (C.getOwner() =='D') {
                ap.getAnimator().moveOnDesk(C,Sett.animTime,24,"Fly",1.5f);
                if (Sett.is_net)
                {
                 send2All(MOVE_MAKE,"C:"+C.getName()+"_O:"+ap.USERS.getI2S('D'));
                }
            }
            else
            {
                ap.getAnimator().moveOnDeskPreUp(C,Sett.animTime,24,"Fly",1.5f,0.7f);
            }
            LOGIC.update(C);
            nextMove(false);
        }
    }

    private void get2(char who, int n) {
        Card Cr;
        if (!cardSets.containsKey(who)) {
            throw new UnsupportedOperationException("Card Set " + who + " not exists");
        }
        for (int i = 0; i < n; i++) {
            Cr = stock.pop();
            if (Cr == null) {
                throw new UnsupportedOperationException("stock is empty");
            }
            cardSets.get(who).add(Cr);
            Cr.setOwner(who);
        }
    }

    @Override
    public void update(float tpf) {
        super.update(tpf); //To change body of generated methods, choose Tools | Templates.
        if (Sett.autoClear && gameState==GameState.INGAME_TAKEWAIT)
        {
            waitClear+=tpf;
            if (waitClear>Sett.originalClearTime) processClickDT();
        }
    }
   
    @Override
    public void cleanup() {
        super.cleanup(); 
        nroot.detachChild(nup);
        nroot.detachChild(ndp);
        nroot.detachChild(nlp);
        nroot.detachChild(nrp);
        nroot.detachChild(ndt);
        nroot.detachChild(nbt);
        ap.UIM.closePopup('A');
        ap.UIM.closePopup('B');
        ap.DESK.idleCoin();
        ap.DESK.detachPlanes();
        ap.DESK.detachCurrPlanes();
        if (Sett.screen_on) ap.setScrOFF(true);
    }

    void setBK(int mu) {
        stock.setBackground(mu);
    }

    int getBK() {
        return stock.getBackground();
    }

    @Override
    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
        
        boolean waitMove = false;
        channel.setAnim("Idle");
        channel.setLoopMode(LoopMode.DontLoop);
        boolean pover;
        if (animName.equals("ClearDT")) {
            
            if (++clearCounter == 4) {
                clearCounter = 0;
                cardInFly = null;
                pover=checkPartyOver();
                processClearTrick(pover);
            }
            return;
        }
        
        if (animName.equals("Fly")) {
          ap.SOUND.playSound("Хлоп");
          if (cardInFly!=null && Sett.showSparks)  
          {
           int si=LOGIC.isSignificant(cardInFly);   
           if (si!=0) ap.flash(si>0?ColorRGBA.Red:ColorRGBA.Blue,cardInFly.getGe().getLocalTranslation());
          }
          cardInFly = null;
          waitMove=true;
        }
        
        if (waitMove) {
            move();
        }
    }

    @Override
    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
        
    }

    public void showNames() {
          //ap.showAndroidMessage(ap.USERS.getI().getName(),ap.USERS.getUser('D').getName());
          ap.UIM.setText2(ap.USERS.getUser('D').getShortName(),'D');
          ap.UIM.setText2(ap.USERS.getUser('L').getShortName(),'L');
          ap.UIM.setText2(ap.USERS.getUser('U').getShortName(),'U');
          ap.UIM.setText2(ap.USERS.getUser('R').getShortName(),'R');
    }

   
    
    public void showScore(char ch) {
        CardSet CS;
        if (ch=='A')
        {
         CS = getCS('D');
         if (CS!=null) ap.UIM.setText1(CS.getPoints()+"", 'D');   
         CS = getCS('L');
         if (CS!=null) ap.UIM.setText1(CS.getPoints()+"", 'L');   
         CS = getCS('U');
         if (CS!=null) ap.UIM.setText1(CS.getPoints()+"", 'U');   
         CS = getCS('R');
         if (CS!=null) ap.UIM.setText1(CS.getPoints()+"", 'R');   
        }
        else
        {
         CS = getCS(ch);
         if (CS!=null) ap.UIM.setText1(CS.getPoints()+"", ch);   
        }
         
        }
        
    public void readState()
    {
         loaded = true;
        if (!ap.isAndroid() || Sett.is_net) {
            return;
        }
        
        if (gameState == GameState.GAMEOVER) {
            return;
        }

        LOGIC.setGameKind(com.yss1.king3.GameLogic.GAME_KIND.valueOf(ap.loadSettStr("GAME_KIND", "NOP")));

        cardSets.get('D').setPoints(ap.loadSettInt("ScoreD", 0));
        showScore('D');
        cardSets.get('L').setPoints(ap.loadSettInt("ScoreL", 0));
        showScore('L');
        cardSets.get('R').setPoints(ap.loadSettInt("ScoreR", 0));
        showScore('R');
        cardSets.get('U').setPoints(ap.loadSettInt("ScoreU", 0));
        showScore('U');

        cardSets.get('D').setTricks(ap.loadSettInt("TricksD", 0));
        cardSets.get('L').setTricks(ap.loadSettInt("TricksL", 0));
        cardSets.get('R').setTricks(ap.loadSettInt("TricksR", 0));
        cardSets.get('U').setTricks(ap.loadSettInt("TricksU", 0));
        whoFirst = ap.loadSettStr("WhoFirst", "D").charAt(0);

        if (gameState == GameState.PARTYOVER) {
            startParty();
            return;
        }
        clear();
        if (ap.DESK != null) {
            ap.DESK.idleCoin();
        }
        whoMove = ap.loadSettStr("WhoMove", "D").charAt(0);

        char X;
        String S;

        Pattern p = Pattern.compile("C:([\\w]{1})_O:([\\w]{1})_S:([FACEBK]{4})_T:([\\(\\)\\.\\d,\\-E ]+)_W:([-\\d]+)_Y");
        Matcher m;

        for (Card cr : stock.getCards()) {
            S = ap.loadSettStr(cr.getName(), "");
            if (S.isEmpty()) {
                continue;
            }
            m = p.matcher(S);
            if (m.find()) {
                X = m.group(1).charAt(0);
                if (X != 'X' && cardSets.containsKey(X)) {
                    cardSets.get(X).add(cr);
                } else {
                    continue;
                }
                cr.setStateStr(m.group(2), m.group(3), m.group(4), m.group(5));
            } else {
                gameState = GameState.GAMEOVER;
                initGameScreen(0);
                return;
            }
        }

        cardSets.get('T').sortByWeight();
        cardSets.get('T').reorderDT();

        if (gameState != GameState.INGAME_TAKEWAIT) {
            move();
        } else {
            updateTricks();
            takeWait();
        }
    }
    
    
    public void saveState()
    {
     
         if (!ap.isAndroid() || Sett.is_net) {
            return;
        }
        Map<String, Object> HM = new HashMap<>();
        HM.put("LANGUAGE", Sett.lang);
        HM.put("AUTOCLEAR", Sett.autoClear);
        HM.put("CARDS_BACK", getBK());
        HM.put("SOUND_ON", Sett.sound_on);
        HM.put("SCREEN_ON", Sett.screen_on);
        HM.put("PLAYER_NAME", Sett.youName);
        HM.put("RULES",RULES.getPacketRules());
        if (!loaded) {
            ap.saveSettingsMap(HM);
            return;
        }

        HM.put("GAME_KIND", LOGIC.getGameKind().toString());
        HM.put("GAME_STATE", gameState.toString());
        HM.put("WhoMove", whoMove + "");
        HM.put("WhoFirst", whoFirst + "");

        HM.put("ScoreD", cardSets.get('D').getPoints());
        HM.put("ScoreL", cardSets.get('L').getPoints());
        HM.put("ScoreR", cardSets.get('R').getPoints());
        HM.put("ScoreU", cardSets.get('U').getPoints());

        HM.put("TricksD", cardSets.get('D').getTricks());
        HM.put("TricksL", cardSets.get('L').getTricks());
        HM.put("TricksR", cardSets.get('R').getTricks());
        HM.put("TricksU", cardSets.get('U').getTricks());

        if (getGameState() == GameState.PARTYOVER) {
            ap.saveSettingsMap(HM);
            return;
        }

        cardSets.get('T').setWeightToN();
        HM = cardSets.get('B').forSaveCards(cardSets.get('D').forSaveCards(cardSets.get('T').forSaveCards(cardSets.get('U').forSaveCards(cardSets.get('L').forSaveCards(cardSets.get('R').forSaveCards(HM, 'R'), 'L'), 'U'), 'T'), 'D'), 'B');
        ap.saveSettingsMap(HM);
    }

    @Override
    public void clickButton(String name) {
   
    }

    
    @Override
    public Node getParentNode(String n)
    {
      return nroot;
    }
    
    @Override
    public void selectRadio(String name) {
     
    }

    @Override
    public void changeCheckbox(String name, boolean state) {
    }

    public void prepareBanner(boolean isVisible){
        ap.UIM.getPlane('U').setMyPlace(isVisible?-1.526f*Sett.getSCRatio()/1.777f:0,
                ap.UIM.getPlane('U').getMyPlace().getY(),
                ap.UIM.getPlane('U').getMyPlace().getZ());
    }
    
    @Override
    public void popUpClosed(Wnd W,String nm) {
        switch (nm) {
            case "pubtn_menu00":
                startLocalGame();
                break;
            case "pubtn_menu01":

                ap.showInterstitial();
                ap.sendEventGA("SHOW_AD");
                //ap.showAd();
                //ap.showAndroidMessage("diagnostics",writeDiag());
                break;
            case "pubtn_menu02":
                ap.showAndroidMessage("diagnostics",writeDiag());
                //ap.rate("king2");
                break;
            case "pubtn_menu03":
                //ap.loadAd();
                ap.showBanner(!ap.isBannerVisible());
                Tools.waiters.initWaiter(ap, BANNER_VISIBLE, "banner visible", 2);


                //prepareBanner(ap.isBannerVisible());
                //showLocalLeaderBoard();
                break;
            case "pubtn_menu04":
                ap.showAndroidMessage("Banner visible","Banner visible="+ap.isBannerVisible());
                break;
            case "pubtn_menu05":
                ap.backPressed();
                break;
        }
     }
     
    @Override
    public void popUpClosing(Wnd W, String res) {
        //System.out.println(res);

        if (W.getPu_type() == Wnd.PU_TYPE.CANCEL_ONLY) {
            return;
        }

        if (res.contains("pubtn_left")) {

            if (gameState == GameState.ASKGAMEOVER) {
                gameState = oldGameState;
            } else if (gameState == GameState.GAMEOVER) {
                if (Sett.is_net) {
                    if (!endNetwork.isEmpty()) {
                        stopNetworkGame(endNetwork);
                        return;
                    } else {
                        leaveGame();
                    }
                }
                initGameScreen(0);
                ap.backPressed();
            }

        } else if (res.contains("pubtn_right")) {
            if (gameState == GameState.ASKGAMEOVER) {
                gameState = GameState.GAMEOVER;
                startLocalGame();
            } else if (gameState == GameState.GAMEOVER) {
                whoFirstGame = LOGIC.getWinners().get(0);
                if (!LOGIC.getWinners().contains('D')) {
                    if (Sett.is_net) {
                        ap.sendEventGA("NET_LOSE");
                        if (ap.isInterstitialLoadedLoaded()) {
                            //ap.showAndroidMessage("AD","AD HERE");
                            ap.showInterstitial();//если не выиграл - реклама
                        } else {
                            if (Sett.is_net) {
                                tryNextGame('D');
                            }
                        }
                    } else {
                        ap.sendEventGA("SINGLE_LOSE");
                        if (ap.isInterstitialLoadedLoaded()) {
                            ap.showInterstitial();//если не выиграл - реклама
                        } else {
                            startLocalGame();
                        }
                    }
                } else//I WIN !
                {
                    if (Sett.is_net) {
                        if (Sett.is_net) {
                            tryNextGame('D');
                        }
                    } else {
                        ap.sendEventGA("SINGLE_WIN");
                        startLocalGame();
                    }
                }
            } else if (gameState == GameState.PARTYOVER) {
                if (Sett.is_net) {
                    tryNextParty('D');
                } else {
                    startLocalParty();
                }
            }
        }
    }
    
      
     public void adClosed()
    {
       if (this.gameState==GameState.GAMEOVER) 
       {
           if (Sett.is_net)
           {
             tryNextGame('D');
           }
           else
           {
             initGameScreen(0);
           }
       }
       
    }
     

    //инициализирует кард сеты из принятой строки
    public void setCardsFromNet(String S) {
        clear();
        Matcher m;
        Pattern p;
        String[] SA = S.split("&");

        char CH, CHmy, CHOW, CHOWmy;
        Card CR;
        p = Pattern.compile("N:([0-9AQKJRDCHS]{2,3})_C:([\\w]{1})_O:([\\w]{1})_S:([FACEBK]{4})_Y");
        for (int i = 0; i < SA.length; i++) {
            m = p.matcher(SA[i]);
            if (m.find()) {
                CH = m.group(2).charAt(0);
                CHmy = ap.USERS.getS2I(CH);

                CHOW = m.group(3).charAt(0);
                CHOWmy = ap.USERS.getS2I(CHOW);

                CR = stock.getCard(m.group(1));
                if (cardSets.containsKey(CHmy) && CR != null) {
                    CR.setOwner(CHOWmy);
                    cardSets.get(CHmy).add(CR);
                }
            }
        }
        
        whoMove = ap.USERS.getS2I('D');
        ap.SOUND.playSound("Замес");
        for (Character ch : chPlayer) {
            getCS(ch).distribute(false);
        }
        updateTricks();
    }
    
    
    
    public void showLocalLeaderBoard() {
        ap.getUIM().showTablePopup('A', Tools.getText(13), "\n" + ap.HBOARD.getTable(), ap.HBOARD.getReg(), Tools.getText(1), 0.29f);
    }
    
    void dataReceived() {
        if (ap.QNET == null) {
            return;
        }
        NetPacket NP = ap.QNET.beginRead();
        if (NP == null) {
            return;
        }
        String contragent=NP.getContragent();
        UserBase from=ap.USERS.getUser(contragent);
        if (from==null)
        {
            ap.showAndroidMessage("Error", "Network message author not found");
            return;
        }
        String S = NP.getContent();
        Pattern p;
        Matcher m;
        Card crr;
        CardSet CSS;
        char Wh;
        int L;
       //ap.writeAndroidLog(NP.getPacketType()+" "+NP.getContent());
        switch (NP.getPacketType()) {
            
            case RASKLAD://приходит расклад в начале партии
                setCardsFromNet(S);
                break;
                
            case MOVE_MAKE://кто то сделал ход    
                p = Pattern.compile("C:([0-9AQKJRDCHS]{2,3})_O:([\\w]{1})");
                m = p.matcher(S);
                if (m.find()){// && (i_server || srv2my.containsKey(m.group(2).charAt(0)))) {
                    whoMove = ap.USERS.getS2I(m.group(2).charAt(0));
                    crr = cardSets.get(whoMove).find(m.group(1));
                    if (crr != null) {
                        move2dt(crr);
                    }
                }
                break;
            case DESK_CLEARED:
                Wh = S.charAt(0);
                Wh = ap.USERS.getS2I(Wh);
                CSS = cardSets.get(Wh);
                CSS.setNeedClear(false);
                updateTricks();
                if (i_server) {
                    if (allCleared()) {
                        if (gameState==GameState.GAMEOVER || gameState==GameState.PARTYOVER) 
                        {
                            return;
                        }
                        if (whoMove == 'D') {
                            nextMove(true);
                        } else {
                            send2All(MOVE_NEED, whoMove + "");//client move need
                        }
                    }
                }
                break;
            case MOVE_NEED:
                if (!i_server && whoMove == 'D') {
                    nextMove(true);
                }
                break;
            case PARTY_FINISHED:
                Wh = ap.USERS.getS2I(S.charAt(0));
                tryNextParty(Wh);
                break;
            case GAME_FINISHED:
                Wh = ap.USERS.getS2I(S.charAt(0));
                tryNextGame(Wh);
                break;
            case NEXT_PARTY:
                ap.USERS.setServer(S);
                i_server = ap.USERS.isIServer();
                startNetParty();
                break;
            case NEXT_GAME:
                //если ждали сервака то уже не надо, он нашелся
                Tools.waiters.resetWaiter(ap, SERVER_WAIT);
                ap.USERS.setServer(applyInitPacket(S));
                //и следующий слева от сервера
                ap.USERS.setNewServer(ap.USERS.getUser(ap.USERS.getS2I('L')).getUid());
                startNetGame();
                break;
            case IM_LEAVE:
                if (ap.getUIM().getPopup('A').isActive()) {
                    endNetwork = S;
                } else {
                    stopNetworkGame(S);
                }
                break;
            case IM_PAUSED:
                from.setActive(false);
                updateTricks();
                break;        
            case IM_ALIVE://player returned from pause
                //информируем только его о не активных юзерах
                from.setActive(true);
                updateTricks();
                //если ч младший активный информирую о ситуации воскресшего
                if (ap.USERS.isImLowActive())
                {
                 ap.QNET.packetOneSend(INFO_ALIVE, ap.USERS.getNAusers(), from.getUid());
                }
                break;            
            case INFO_ALIVE://receive information about activity
                ap.USERS.setActivity(S);
                updateTricks();
                break;            
        }
        ap.QNET.finishRead(NP);
    }
    
    private void tryNextParty(char wh) {
        cardSets.get(wh).setEndParty(true);
        if (wh == 'D') {
            send2All(PARTY_FINISHED,ap.USERS.getI2S('D')+"");
        }
        updateTricks();
        
        if (i_server) {
            if (allPartyAgree()) {
                String Sid = ap.USERS.getUser('L').getUid();
                send2All(NEXT_PARTY, Sid);
                ap.USERS.setServer(Sid);
                i_server = false;
                startNetParty();
            }
        }
    }

    private void tryNextGame(char wh) {
        cardSets.get(wh).setEndParty(true);
        if (!endNetwork.isEmpty())
                    {
                       stopNetworkGame(endNetwork);
                       return;
                    }
        if (wh == 'D') {
            ap.DESK.attachCurrPlanes();
            send2All(GAME_FINISHED, ap.USERS.getI2S(wh)+"");
        }
        updateTricks();
            if (allPartyAgree()) {
                startNetwork(true,false);
            }
//        }
    }
    
    //я вышел, отправляю уведомление
    void leaveGame() {
       if (!Sett.is_net) return;
          UserBase U=ap.USERS.getI(); 
          Sett.is_net=false;
          send2All(IM_LEAVE,U!=null?U.getUid():"NAME");
          ap.USERS.initLocalUsers(Tools.initPlayers());
          RULES.restoreRules();
          //ap.gameLeaved(); 
    }
    
    //кто то другой вышел, закрываем лавочку
    void stopNetworkGame(String S) {
       if (!Sett.is_net) return;
          ap.showAndroidMessage(Tools.getText(150),String.format(Tools.getText(151), ap.USERS.getUser(S).getName()));
          ap.gameLeaved(); 
          gameState=GameState.GAMEOVER;
          ap.GAME.LOGIC.setGameKind(GAME_KIND.NOP);
          Sett.is_net=false;
          ap.USERS.initLocalUsers(Tools.initPlayers());
          RULES.restoreRules();
          if (ap.getStateManager().hasState(ap.GAME))
          {
           initGameScreen(0);
           ap.backPressed();
          }
          else
          {
            ap.getUIM2().setGameButton(true);
          }
    }
    
    @Override
    public AnimEventListener getAEL() {
        return this;
    }
    
    private String writeDiag()
    {
        StringBuilder sb=new StringBuilder();
        sb.append("GameState=");
        sb.append(gameState);
        sb.append(" who=");
        sb.append(whoMove);
        sb.append(" Legal=");
        sb.append(LOGIC.getLegalMovs().size());
        sb.append(" iSERV=");
        sb.append(i_server);
        sb.append(" InFly=");
        sb.append(cardInFly==null?"NULL":cardInFly.getName());
        sb.append("\n");
        for (Character cr: chAll)
        {
            sb.append(cr);
            sb.append(":");
        sb.append(cardSets.get(cr).writeDiag());
        }
        return sb.toString();
    }
    
    
}