/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yss1.king3;

import com.yss1.lib_jm.Card;
import com.yss1.lib_jm.CardSetBase;
import com.yss1.lib_jm.GameLogicBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ys
 */
public class GameLogic extends GameLogicBase{

    
    
public enum GAME_KIND{NOP,NO_TRICKS,NO_HEARTS,NO_BOYS,NO_GIRLS,NO_2LAST,NO_KING,NO_ALL,YES_TRICKS,YES_HEARTS,YES_BOYS,YES_GIRLS,YES_2LAST,YES_KING,YES_ALL};        

private final byte [] suit_on_hand;
//ArrayList<Card> legalMovs;
private final ArrayList<Card> OPPO;
private GAME_KIND gameKind;        
private int TAKE,NOTAKE;
private int myHALF;
protected GameRules rules;
    
public GameLogic(HashMap<Character, CardSetBase> csMAP)
{
    super(csMAP);    
    gameKind=GAME_KIND.NOP;
    suit_on_hand=new byte [] {15,15,15,15};
    OPPO=new ArrayList<>();
}


    public void setRules(GameRules rules) {
        this.rules = rules;
    }
    
void setHalfMyPoint(int SC) {
        myHALF=SC;
    }

int getHalfMyPoint() {
        return myHALF;
    }

    
public int isSignificant(Card cr) {
  switch (gameKind)  
  {//для пускания искр
      case NO_TRICKS: return 0;    
      case NO_HEARTS: if (cr.getSuit()==4) return -1;else return 0;    
      case NO_BOYS: if (cr.getRank()==11 ||rules.isKingBoy() && cr.getRank()==13) return -1;else return 0;
      case NO_GIRLS: if (cr.getRank()==12) return -1;else return 0;         
      case NO_2LAST: return 0;
      case NO_KING: if (cr.getRank()==13 && cr.getSuit()==4) return -1;else return 0;
      case NO_ALL: if (cr.getSuit()==4 || cr.getRank()==12 || cr.getRank()==11 || cr.getRank()==13 && rules.isKingBoy()) return -1; else return 0;
      case YES_TRICKS: return 0;    
      case YES_HEARTS: if (cr.getSuit()==4) return 1;else return 0;    
      case YES_BOYS: if (cr.getRank()==11 || rules.isKingBoy() && cr.getRank()==13) return 1; else return 0;   
      case YES_GIRLS: if (cr.getRank()==12) return 1;else return 0;      
      case YES_2LAST: return 0;                   
      case YES_KING: if (cr.getRank()==13 && cr.getSuit()==4) return 1;else return 0;
      case YES_ALL: if (cr.getSuit()==4 || cr.getRank()==12 || cr.getRank()==11 || cr.getRank()==13 && rules.isKingBoy()) return 1; else return 0;
      default:    
      return 0;                                 
  }
 }

public boolean isHeartsDepend()
{
          return (gameKind==GAME_KIND.NO_HEARTS || 
                  gameKind==GAME_KIND.NO_KING || 
                  gameKind==GAME_KIND.NO_ALL || 
                  gameKind==GAME_KIND.YES_HEARTS || 
                  gameKind==GAME_KIND.YES_KING|| 
                  gameKind==GAME_KIND.YES_ALL);
        }

public boolean isBoysDepend()
{
          return (gameKind==GAME_KIND.NO_BOYS || 
                  gameKind==GAME_KIND.NO_ALL || 
                  gameKind==GAME_KIND.YES_BOYS|| 
                  gameKind==GAME_KIND.YES_ALL);
        }

public boolean isTakeGood()
{
          return (gameKind==GAME_KIND.YES_TRICKS ||
                  gameKind==GAME_KIND.YES_HEARTS ||
                  gameKind==GAME_KIND.YES_BOYS ||
                  gameKind==GAME_KIND.YES_GIRLS ||
                  gameKind==GAME_KIND.YES_2LAST ||
                  gameKind==GAME_KIND.YES_KING ||
                  gameKind==GAME_KIND.YES_ALL);
                          
        }

public GAME_KIND getGameKind() {
        return gameKind;
    }

public char whoTake()
{
    CardSet DTcs=(CardSet)csets.get('T');
    if (DTcs==null || DTcs.length()!=4) return 'N';
    
    Card Cr=DTcs.get(0);
    Card Cu=Cr;
    for (Card c: DTcs.getCards())
        {
            if (c.getSuit()==Cu.getSuit() && c.getRank()>Cu.getRank()) Cu=c;
        }
    
    Character result=Cu.getOwner();
    CardSet WinnerSet=(CardSet)csets.get(result);
    if (WinnerSet==null) return 'N';
    
    int point=0;
    int tricks=0;
    int TMP;
    int TMP1;
    switch (gameKind) 
    {
     case NO_ALL:
         tricks++;
     case NO_TRICKS: 
         point-=2;
     if (gameKind!=GAME_KIND.NO_ALL) {tricks++;break;}
     case NO_HEARTS: 
         TMP=DTcs.howSuits()[3];
         point-=2*TMP;
     if (gameKind!=GAME_KIND.NO_ALL) {tricks+=TMP;break;}
     case NO_BOYS: 
         TMP1=0;
         TMP=DTcs.howRank(11);
         if (rules.isKingBoy()) 
         {
             TMP1=DTcs.howRank(13);
         }
         point-=(TMP1*2+(rules.isKingBoy()?2:4)*TMP);
     if (gameKind!=GAME_KIND.NO_ALL) {tricks+=(TMP+TMP1);break;}
     case NO_GIRLS: 
         TMP=DTcs.howRank(12);
         point-=4*TMP;
     if (gameKind!=GAME_KIND.NO_ALL) {tricks+=TMP;break;}
     case NO_2LAST: 
         TMP=(csets.get('D').length()>1?0:1);
         point-=8*TMP;
     if (gameKind!=GAME_KIND.NO_ALL) {tricks+=TMP;break;}
     case NO_KING: 
         TMP=(DTcs.find("KH")==null?0:1);
         point-=16*TMP;
         if (gameKind!=GAME_KIND.NO_ALL) {tricks+=TMP;}
     break;
         
     case YES_ALL:   
          tricks++;
     case YES_TRICKS: 
         point+=2;
     if (gameKind!=GAME_KIND.YES_ALL) {tricks++;break;}
     case YES_HEARTS: 
         TMP=DTcs.howSuits()[3];
         point+=2*TMP;
     if (gameKind!=GAME_KIND.YES_ALL) {tricks+=TMP;break;}
     case YES_BOYS: 
         TMP1=0;
         TMP=DTcs.howRank(11);
         if (rules.isKingBoy()) 
         {
             TMP1=DTcs.howRank(13);
         }
         point+=(TMP1*2+(rules.isKingBoy()?2:4)*TMP);
     if (gameKind!=GAME_KIND.YES_ALL) {tricks+=(TMP+TMP1);break;}
     case YES_GIRLS: 
         TMP=DTcs.howRank(12);
         point+=4*TMP;
     if (gameKind!=GAME_KIND.YES_ALL) {tricks+=TMP;break;}
     case YES_2LAST: 
         TMP=(csets.get('D').length()>1?0:1);
         point+=8*TMP;
     if (gameKind!=GAME_KIND.YES_ALL) {tricks+=TMP;break;}
     case YES_KING: 
         TMP=(DTcs.find("KH")==null?0:1);
         point+=16*TMP;
         if (gameKind!=GAME_KIND.YES_ALL) {tricks+=TMP;}
         break;
 }
    if (point!=0)
    {
    WinnerSet.addPoints(point);
    WinnerSet.incTricks(tricks);
    }
    return result;
}

    public void setGameKind(GAME_KIND gameKind) {
        this.gameKind = gameKind;
    }

@Override
public void newGame()
{
    gameKind=GAME_KIND.NOP;
    myHALF=0;
    for (Map.Entry<Character, CardSetBase> entry : csets.entrySet()) {
            
                entry.getValue().setPoints(0);
            }
}
        
@Override
public void newParty()
{
    rules.commit(true);
    gameKind=Next();
    for (Map.Entry<Character, CardSetBase> entry : csets.entrySet()) {
                ((CardSet)entry.getValue()).setTricks(0);
            }
    suit_on_hand[0]=15;
    suit_on_hand[1]=15;
    suit_on_hand[2]=15;
    suit_on_hand[3]=15;
}

private void noIhaveSuit(char who,int su)
{//отмечаем факт точного отсутствия масти у игрока
    int idx;
    byte mask=(byte)(1<<(su-1));
    if (who=='D') idx=0;
    else if (who=='L') idx=1;
    else if (who=='U') idx=2;
    else if (who=='R') idx=3;
    else return;
    mask=(byte)((~mask)&15);
    suit_on_hand[idx]&=mask;
}

private boolean haveSuit(char who,int su)
{
    int idx;
    byte mask=(byte)(1<<(su-1));
    if (who=='D') idx=0;
    else if (who=='L') idx=1;
    else if (who=='U') idx=2;
    else if (who=='R') idx=3;
    else return false;
    return (suit_on_hand[idx] & mask) > 0;
}

public GAME_KIND Next()
{
 // if (1==1) return GAME_KIND.YES_ALL;
  switch (gameKind)  
  {
      
      case NOP: return GAME_KIND.NO_TRICKS;
      case NO_TRICKS: return GAME_KIND.NO_HEARTS;    
      case NO_HEARTS: return GAME_KIND.NO_BOYS;    
      case NO_BOYS: return GAME_KIND.NO_GIRLS;    
      case NO_GIRLS: return GAME_KIND.NO_2LAST;         
      case NO_2LAST: return GAME_KIND.NO_KING;                   
      case NO_KING: return GAME_KIND.NO_ALL;                             
      case NO_ALL: return GAME_KIND.YES_TRICKS;
      case YES_TRICKS: return GAME_KIND.YES_HEARTS;    
      case YES_HEARTS: return GAME_KIND.YES_BOYS;    
      case YES_BOYS: return GAME_KIND.YES_GIRLS;    
      case YES_GIRLS: return GAME_KIND.YES_2LAST;         
      case YES_2LAST: return GAME_KIND.YES_KING;                   
      case YES_KING: return GAME_KIND.YES_ALL;                             
      default:    
      return GAME_KIND.NOP;                                 
  }
}

@Override
public boolean gameIsOver()
{
   if (Next() == GAME_KIND.NOP) 
   {
       initWinners();
       return true; 
   }
   return false;
}

@Override
public boolean partyIsOver()
{
 //System.out.println("LGH="+Bcs.length());
 CardSet Bcs=(CardSet)csets.get('B');
 
 switch (gameKind) 
 {
     case NO_TRICKS: 
     case YES_TRICKS:
     case NO_ALL:
     case YES_ALL:
     case NO_2LAST:
     case YES_2LAST:    
         //return (Bcs.length()==12);
         return (Bcs.length()==32);
     case NO_HEARTS:
     case YES_HEARTS:
         return (Bcs.howSuits()[3]==8);
     case NO_GIRLS:
     case YES_GIRLS:
         return (Bcs.howRank(12)==4);
     case NO_KING:
     case YES_KING:
         return (Bcs.find("KH")!=null);
                 //(Bcs.length()==12);//(Bcs.find("KH")!=null);
     case NO_BOYS:
     case YES_BOYS:
         if (rules.isKingBoy())
         {
             return (Bcs.howRank(11)+Bcs.howRank(13)==8);
         }
         else
         {
             return (Bcs.howRank(11)==4);
         }
 }
 return false;
}

//private ArrayList<Integer> winners;

    @Override
    public void initWinners() {
        int X = -1000;
        for (Map.Entry<Character, CardSetBase> entry : csets.entrySet()) {
            if ((entry.getKey().equals('U')||entry.getKey().equals('L')||entry.getKey().equals('R')||entry.getKey().equals('D'))&& entry.getValue().getPoints() > X) {
                X = entry.getValue().getPoints();
            }
        }
        winners.clear();
        for (Map.Entry<Character, CardSetBase> entry : csets.entrySet()) {
            if ((entry.getKey().equals('U')||entry.getKey().equals('L')||entry.getKey().equals('R')||entry.getKey().equals('D'))&&entry.getValue().getPoints() == X) {
                winners.add(entry.getKey());
            }
        }
        
       
    }

//    public int getPlayerN(Character CH) {
//        if (CH.equals('D')) {
//            return 0;
//        } else if (CH.equals('L')) {
//            return 1;
//        } else if (CH.equals('U')) {
//            return 2;
//        } else if (CH.equals('R')) {
//            return 3;
//        } else {
//            return -1;
//        }
//    }

//@Override
//    public ArrayList<Character> getWinners() {
//        winners.clear();
//        int max = csets.g.getPoints();
//        if (Lcs.getPoints() > max) {
//            max = Lcs.getPoints();
//        }
//        if (Rcs.getPoints() > max) {
//            max = Rcs.getPoints();
//        }
//        if (Ucs.getPoints() > max) {
//            max = Ucs.getPoints();
//        }
//        if (Mycs.getPoints() == max) {
//            winners.add(0);
//        }
//        if (Lcs.getPoints() == max) {
//            winners.add(1);
//        }
//        if (Ucs.getPoints() == max) {
//            winners.add(2);
//        }
//        if (Rcs.getPoints() == max) {
//            winners.add(3);
//        }
//        return winners;
//    }


//public int getMyPoints(){
//        return Mycs.getPoints();
//    }

//public int getWinnerPoints(){
//        if (winners.isEmpty()) return 0;
//        switch (winners.get(0))
//        {
//            case 0:
//                return Mycs.getPoints();
//            case 1:
//                return Lcs.getPoints();
//            case 2:
//                return Ucs.getPoints();                
//           case 3:
//                return Rcs.getPoints();                
//        }
//        return 0;
//    }

//private CardSet getCS(char w)
//{
//    return ap.GAME.getCS(w);
//}

private boolean is_first(boolean post)
{//post после хода смотрим или перед
    if (post)
    return csets.get('T').length()<2;
    else
    return csets.get('T').length()<1;    
}

public void update(Card C)
{
  if (csets.get('T').length()>0 && csets.get('T').get(0).getSuit()!=C.getSuit()) noIhaveSuit(C.getOwner(),csets.get('T').get(0).getSuit());
}

@Override
public ArrayList<Card> refreshLegalMovies(Character cH)
{
  //ArrayList<Card> res=new ArrayList<Card>();
  legalMovs.clear();
  if (!csets.containsKey(cH)) return legalMovs;
  
  Card CR;
  int SW []=((CardSet)csets.get(cH)).howSuits();
  if (is_first(false))
  {
      if (!isHeartsDepend() || (SW[5]==1 && SW[3]>0))
      {
          legalMovs.addAll(csets.get(cH).getCards());
      }
      else
      {
          for (Card C:csets.get(cH).getCards()) if (C.getSuit()!=4) legalMovs.add(C);
      }
  }
  else
  {
      CR=csets.get('T').get(0);
      
      if (SW[CR.getSuit()-1]==0)
      {
        legalMovs.addAll(csets.get(cH).getCards());
      }
      else
      {
        for (Card C:csets.get(cH).getCards()) if (C.getSuit()==CR.getSuit()) legalMovs.add(C);
      }
  }
  
//  System.out.println("---Lgh="+DTcs.length()+" SW[0]="+SW[0]+" SW[1]="+SW[1]+" SW[2]="+SW[2]+" SW[3]="+SW[3]+" SW[5]="+SW[5]);
//  for (Card C:res) System.out.println(C.getName());
//  System.out.println("---");
  return legalMovs;
}

public void refreshOPPO(char c)
{//карты на чужих руках
 //вычисляется запоминанием выщедших без мухляжа
    OPPO.clear();
    switch (c)
    {
        case 'U':
            OPPO.addAll(csets.get('R').getCards());
            OPPO.addAll(csets.get('L').getCards());
            OPPO.addAll(csets.get('D').getCards());
            break;
        case 'D':
            OPPO.addAll(csets.get('R').getCards());
            OPPO.addAll(csets.get('L').getCards());
            OPPO.addAll(csets.get('U').getCards());
            break;    
        case 'L':
            OPPO.addAll(csets.get('U').getCards());
            OPPO.addAll(csets.get('R').getCards());
            OPPO.addAll(csets.get('D').getCards());
            break;    
        case 'R':
            OPPO.addAll(csets.get('U').getCards());
            OPPO.addAll(csets.get('L').getCards());
            OPPO.addAll(csets.get('D').getCards());
            break;    
    }
}

//public ArrayList<Card> refreshLegalMovies(char Who)
//{
//  CardSet cs=getCS(Who);
//  return refreshLegalMovies(cs);
//  
//}

private char next(char who)
{//список ходящих после меня
    switch (who)
    {
        case 'D':return 'L';
        case 'L':return 'U';
        case 'U':return 'R';
        case 'R':return 'D';
    }
    return 'N';
}

private int suitAhead(int suit, char whoI)
{//у скольких далее эта масть есть (предположительно, основываясЬ на точно известном факте, что у когото нет)
    int counter=0;
    int onDesk=csets.get('T').length();
    char w=whoI;
    for (int i=onDesk+1;i<4;i++)
    {
        w=next(w);
        if (haveSuit(w,suit)) counter++;
    }
    return counter;
}

public int isMY(Card C)
{//10 точно мая, 0 точно не моя, 1..9 - степень вероятности, что моя
    int S=C.getSuit();
    int R=C.getRank();
    //rateCard(C);//update rating array
    CardSet DTcs=(CardSet)csets.get('T');
    SA=suitAhead(S,C.getOwner());
    int LG=DTcs.length();
    if (LG==0)
    {
      if (C.rating[1]==0) return 10;//старше просто нет
      if (C.rating[0]==0) return 0;//младше просто нет (но есть старше иначе выходим выше)
      return (C.rating[0]*10)/(C.rating[0]+C.rating[1]);    
    }
    else
    {
      if (DTcs.get(0).getSuit()!=S) return 0;//не в масть
      //дальше только в масть
      if (DTcs.hiCard(S).getRank() > C.getRank()) return 0;// не старшую на столе
      //дальше только в масть и старшую
      if (SA==0) return 10;//ложу в масть старше на столе нет впереди (по накопленной точной инфе) нет этой масти
      if (C.rating[1]==0) return 10;//старше на руках просто нет
    }
    return (C.rating[0]*10)/(C.rating[0]+C.rating[1]);    
}

private int SA;
//private int [] rating;
private void rateCard(Card C)
{//ra[0] сколько младше, 
 //ra[1] сколько старше,
 //ra[2] младший ранк,
 //ra[3] старший ранк,
 //ra[4] ближайший младший ранк,
 //ra[5] ближайший старший ранк
 //ra[6] всего этой масти у других
    int suit=C.getSuit();
    int rank=C.getRank();
    int R;
    C.rating[0]=0;
    C.rating[1]=0;
    C.rating[2]=rank;
    C.rating[3]=rank;
    C.rating[4]=0;
    C.rating[5]=20;

    for (Card CA:OPPO)
    {
        if (suit==CA.getSuit())
        {
         R=CA.getRank();
         if (R>rank) {
             C.rating[1]++;
             if (R>C.rating[3]) C.rating[3]=R;
             if (R<C.rating[5]) C.rating[5]=R;
            }
         if (R<rank) {
             C.rating[0]++;
             if (R<C.rating[2]) C.rating[2]=R;
             if (R>C.rating[4]) C.rating[4]=R;
            }
        }
    }
    if (C.rating[2]==rank) C.rating[2]=0;
    if (C.rating[3]==rank) C.rating[3]=0;
    if (C.rating[5]==20) C.rating[5]=0;
    C.rating[6]=C.rating[1]+C.rating[0];
}

private float otdach(CardSet CS,Card WO)
{//OPPO при заходе должна быть актуальна (WO карта, которая не учитывается (мб null))
 //количество отдач с захода хозяина CS. Если 100% отдача, то ++1 к результату
 //иначе ++ вероятность 0..1 где 0 100% взятка
    float res=0f;
    for (Card CR:CS.getCards())
    {
      //if (CR==null) throw new RuntimeException("CR==0");
      //if (WO==null) throw new RuntimeException("WO==0");
      if (CR.equals(WO)) continue;
      if (CR.rating[1]!=0) //имеет смысл только если есть младшие на чужих руках
      {
         res+=CR.rating[1]/((float)(CR.rating[0]+CR.rating[1]));
      }
    }
    return res;
}

private float isEndedParty(Card CR)
{//OPPO при заходе должна быть актуальна (WO карта, которая не учитывается (мб null))
 //показывает какой процент значимых карт с учетом положенной CR (мб null) выбран
    CardSet DTcs=(CardSet)csets.get('T');
    CardSet Bcs=(CardSet)csets.get('B');
    float res=1f;
    switch (gameKind)
    {
        case NO_TRICKS:
        case NO_ALL:    
        case YES_TRICKS:
        case YES_ALL:            
            res=(Bcs.length()+DTcs.length()+(CR!=null?1f:0f))/32f;
        break;    
        case NO_HEARTS:
        case YES_HEARTS:    
            res=(Bcs.howSuits()[3]+DTcs.howSuits()[3]+((CR!=null&&CR.getSuit()==4)?1f:0f))/8f;
        break;    
        case NO_BOYS:
        case YES_BOYS:    
            if (rules.isKingBoy())
            {
             res=(Bcs.howRank(11)+DTcs.howRank(11)+Bcs.howRank(13)+DTcs.howRank(13)+((CR!=null&&(CR.getRank()==11||CR.getRank()==13))?1f:0f))/8f;   
            }
            else
            {
             res=(Bcs.howRank(11)+DTcs.howRank(11)+((CR!=null&&CR.getRank()==11)?1f:0f))/4f;
            }
        break;    
        case NO_GIRLS:
        case YES_GIRLS:
            res=(Bcs.howRank(12)+DTcs.howRank(12)+((CR!=null&&CR.getRank()==12)?1f:0f))/4f;
        break;    
        case NO_2LAST:
        case YES_2LAST:
            if (Bcs.length()<24) res=0f;
            else if (Bcs.length()<28) res=0.5f;
            else res=1f;
        break;    
        case NO_KING:
        case YES_KING:    
            if (DTcs.find(13,4)!=null || CR!=null && CR.getRank()==13 && CR.getSuit()==4 || Bcs.length()==28) res=1f;
            else res=0f;
        break;    
    }
    return res;
}



    private void haveTake() {//анализирует legal и возвращает количестао точно беруших и точнот не берущих
        //OPPO должно быть инициировано   
        TAKE = 0;
        NOTAKE = 0;
        if (legalMovs.size() < 1) {
            return;
        }
        if (csets.get('T').length() > 0 && csets.get('T').get(0).getSuit() != legalMovs.get(0).getSuit()) {
            TAKE = 0;
            NOTAKE = legalMovs.size();
            return;
        }
        Card H;
        for (Card C : legalMovs) {
            H = ((CardSet) csets.get('T')).hiCard(C.getSuit());
            SA = suitAhead(C.getSuit(), C.getOwner());
            if ((H != null && H.getRank() < C.getRank() || H == null) && (csets.get('T').length() == 3 || (C.rating[1] == 0 && SA > 0 || SA == 0))) {
                TAKE++;
            }
            if (H != null && H.getRank() > C.getRank() || csets.get('T').length() < 3 && C.rating[0] == 0 && SA > 0) {
                NOTAKE++;
            }
        }
    }
 



@Override
public Card getMove(Character le)
{
    Card res=null;
    CardSet cs=(CardSet)csets.get(le);
    refreshLegalMovies(le);
    CardSet DTcs=(CardSet)csets.get('T');
    if (legalMovs.size()==1) 
    {
        return legalMovs.get(0);
    }
    refreshOPPO(le);
    for (Card CR: legalMovs) rateCard(CR);
    haveTake();//calc TAKE && NOTAKE variables
    int hs[]=cs.howSuits();
    int SU;
    int RA;
    int TGT;
    int RAT;
    int HD;
    int HU;
    int OST;
    int wTGT,wTGT1;
    int ONDESK=DTcs.length();
    int AHEAD=3-ONDESK;
    int thisSuit;
    
    boolean isTGT;
    boolean inSuit= ONDESK==0 || DTcs.get(0).getSuit()==legalMovs.get(0).getSuit();
    float OTD,OTD0;
    switch(gameKind)
    {
        case NO_TRICKS:
            for (Card crr:legalMovs)
            {
            SU=crr.getSuit();
            RA=crr.getRank();
            crr.setWeight(0);
            RAT=isMY(crr);
            OTD=otdach(cs,crr);
            
            if (!inSuit)
            {
             RAT=7+crr.rating[0]-crr.rating[1];
            }
            else
            {
            switch (ONDESK)
            {
                case 0:
                    RAT=10-RAT;
                    break;
                case 1:
                case 2:    
                    if (RAT==0)
                    {
                        RAT=7+crr.rating[0]-crr.rating[1];
                    }
                    else if (RAT==10)
                    {
                        RAT=1;
                        if (OTD<1f) RAT=0;
                    }
                    else
                    {
                      RAT=10-RAT;  
                    }
                    break;    
                case 3: 
                    if (RAT==0)
                    {
                        RAT=7+crr.rating[0]-crr.rating[1];
                    }
                    else
                    {//RAT==10
                     RAT=1;
                        if (OTD<1f) RAT=0;
                    }
                    break;
            }
            }
            crr.setWeight(RAT);
            }
        break;
            
        case NO_HEARTS:
            TGT=DTcs.howSuits()[3];
            
            for (Card crr:legalMovs)
            {
            isTGT=crr.getSuit()==4;    
            SU=crr.getSuit();
            RA=crr.getRank();
            crr.setWeight(0);
            OTD=otdach(cs,crr);
            RAT=isMY(crr);
            if (!inSuit)
            {
                if (TGT>0 || isTGT) {RAT=9+TGT+(isTGT?1:0)+crr.rating[0]-crr.rating[1];}
                else if (crr.rating[0]>crr.rating[1]) {RAT=crr.rating[0]-crr.rating[1];}    
            }
            else
            {
            switch (ONDESK)
            {
                case 0:
                    if (isTGT) if (RAT==0) {RAT=12;break;} else {RAT--;}
                    RAT=10-RAT;
                    if (AHEAD==3) RAT++;
                    break;
                case 1:
                case 2:    
                    if (RAT==0)
                    {
                        if (isTGT || TGT>0)   {RAT=11; break;}
                        RAT=crr.rating[0];
                    }
                    else if (RAT==10)
                    {
                        if (isTGT || TGT>0 || AHEAD>SA)   {RAT=0; break;}
                        if (OTD<0.5f)  {RAT=0; break;}
                    }
                    else
                    {
                       if (isTGT || TGT>0 || AHEAD>SA)   {RAT=0; break;}
                       RAT=10-RAT; 
                    }
                    break;    
                case 3: 
                    if (RAT==10)//либо 0 либо 10
                    {
                        if (isTGT || TGT>0) {RAT=4-TGT-(isTGT?1:0);break;}
                        if (OTD<0.7f) {RAT=4;break;}
                    }
                    else
                    {//RAT==0
                         if (!isTGT && TGT==0 && OTD>1f) {RAT=6;break;}
                         RAT=5+crr.rating[0];
                         if (isTGT || TGT>0) RA+=3;
                     }
                    break;
            }
            }
            
//            if (is_first(false))
//            {//хожу первым
//              if (RAT==0) RAT=1;//не самой маленькой, а с большой вероятностью отдачи
//              else if (RAT<3) RAT=0;
//            }
//            else
//            {//хожу 2.3.4
//                if (legalMovs.get(0).getSuit()==DTcs.get(0).getSuit())
//                {//ложу в масть (есть шанс взять)
//                  if (ONDESK==3)
//                  {//тут RAT либо 0 либо 10
//                   if (TGT==0 && OTD>2f) if (RAT==0) RAT=10; else RAT=0;
//                  }
//                  else
//                  {
//                     //нет червей на столе + эта масть по инфе от 2 источников впереди есть то слегка увеличиваем вероятность хода 
//                     if (TGT==0 && suitAhead(crr.getSuit(),le)==AHEAD && (crr.rating[0]+crr.rating[1])>=AHEAD && RAT==10) RAT=5;
//                  }
//                }
//                else
//                {//ложу не в масть здесь всегда RAT=0;
//                   if (isTGT) RAT=10-crr.getRank()+4;
//                   else RAT=10-crr.rating[0];
//                }
//            }
            crr.setWeight(RAT);
            }
        break;    
        case NO_BOYS:
            TGT=DTcs.howRank(11)+(rules.isKingBoy()?DTcs.howRank(13):0);
            for (Card crr:legalMovs)
            {
            SU=crr.getSuit();
            RA=crr.getRank();
            crr.setWeight(0);
            OTD=otdach(cs,crr);
            RAT=isMY(crr);
            wTGT=whereIs(cs,11,SU);
            if (rules.isKingBoy()) wTGT1=whereIs(cs,13,SU); else wTGT1=0;
            isTGT=RA==11 || rules.isKingBoy() && RA==13;
            
            if (!inSuit)
            {
                if (TGT>0 || isTGT) {RAT=13+TGT+(isTGT?1:0)+crr.rating[0]-crr.rating[1];}
                else if (wTGT1<0 && RA>13)  {RAT=11;}
                else if (wTGT<0 && RA>11)   {RAT=10;}
                else if (crr.rating[0]>crr.rating[1]) {RAT=crr.rating[0]-crr.rating[1];}    
            }
            else
            {
            switch (ONDESK)
            {
                case 0:
                    if (isTGT) if (RAT==0) {RAT=12;break;} else {RAT--;}
                    RAT=10-RAT;
                    if (AHEAD==3 &&!isTGT) RAT++;
                    if (wTGT<0 && RA<11) RAT++;
                    if (wTGT1<0 && RA<13 && RA!=11) RAT++;
                    break;
                case 1:
                case 2:    
                    if (RAT==0)
                    {
                        if (isTGT || TGT>0)   {RAT=13; break;}
                        RAT=crr.rating[0];
                        if (wTGT1<0 && RA>13) {RAT+=2; break;}
                        if (wTGT <0 && RA>11) {RAT+=1; break;}
                    }
                    else if (RAT==10)
                    {
                        if (isTGT || TGT>0 || AHEAD>SA)   {RAT=0; break;}
                        if (OTD<0.5f)  {RAT=0; break;}
                        if (wTGT1<0 && RA>13) {RAT=2; break;}
                        if (wTGT <0 && RA>11) {RAT=1; break;}
                        
                    }
                    else
                    {//пока что эта самая старшая
                       if (isTGT || TGT>0 || AHEAD>SA)   {RAT=0; break;}
                       RAT=10-RAT; 
                    }
                    //if ((isTGT || TGT>0) && RAT==0) {RAT=11;break;}
                    //RAT=10-RAT; 
                    
                    //if ((isTGT || TGT>0)&& RAT>0) RAT--; 
                    break;    
                case 3: 
                    if (RAT==10)//либо 0 либо 10
                    {
                        if (isTGT || TGT>0) {RAT=4-TGT-(isTGT?1:0);break;}
                        if (OTD<0.7f) {RAT=4;break;}
                    }
                    else
                    {//RAT==0
                         if (!isTGT && TGT==0 && OTD>1f) {RAT=6;break;}
                         RAT=5+crr.rating[0];
                         if (isTGT || TGT>0) RA+=3;
                         if (wTGT1<0 && RA>13)  {RA+=1;}
                         if (wTGT <0 && RA>11)  {RA+=1;}
                     }
                    break;
            }
            }
            crr.setWeight(RAT);
            }
        break;    
            
        case NO_GIRLS:
            TGT=DTcs.howRank(12);
            for (Card crr:legalMovs)
            {
            SU=crr.getSuit();
            RA=crr.getRank();
            crr.setWeight(0);
            OTD=otdach(cs,crr);
            RAT=isMY(crr);
            isTGT=crr.getRank()==12;
            wTGT=whereIs(cs,12,SU);
            thisSuit=cs.howLo(12,SU);
            
           if (!inSuit)
            {
                if (TGT>0 || isTGT) {RAT=13+TGT+(isTGT?1:0)+crr.rating[0]-crr.rating[1];}
                else if (wTGT<0 && RA>12)   {RAT=11;}
                else if (crr.rating[0]>crr.rating[1]) {RAT=crr.rating[0]-crr.rating[1];}    
            }
            else
            {
            switch (ONDESK)
            {
                case 0:
                    if (isTGT) if (RAT==0) {RAT=12;break;} else {RAT--;}
                    RAT=10-RAT;
                    if (AHEAD==3 &&!isTGT) RAT++;
                    if (wTGT<0 && RA<12) RAT++;
                    break;
                case 1:
                case 2:    
                    if (RAT==0)
                    {
                        if (isTGT || TGT>0)   {RAT=13; break;}
                        RAT=crr.rating[0];
                        if (wTGT <0 && RA>12) {RAT+=1; break;}
                    }
                    else if (RAT==10)
                    {
                        if (isTGT || TGT>0 || AHEAD>SA)   {RAT=0; break;}
                        if (OTD<0.5f)  {RAT=0; break;}
                        if (wTGT <0 && RA>12) {RAT=2; break;}
                        
                    }
                    else
                    {//пока что эта самая старшая
                       if (isTGT || TGT>0 || AHEAD>SA)   {RAT=0; break;}
                       RAT=10-RAT; 
                    }
                    //if ((isTGT || TGT>0) && RAT==0) {RAT=11;break;}
                    //RAT=10-RAT; 
                    
                    //if ((isTGT || TGT>0)&& RAT>0) RAT--; 
                    break;    
                case 3: 
                    if (RAT==10)//либо 0 либо 10
                    {
                        if (isTGT || TGT>0) {RAT=4-TGT-(isTGT?1:0);break;}
                        if (OTD<0.7f) {RAT=4;break;}
                    }
                    else
                    {//RAT==0
                         if (!isTGT && TGT==0 && OTD>1f) {RAT=6;break;}
                         RAT=5+crr.rating[0];
                         if (isTGT || TGT>0) RA+=3;
                         if (wTGT <0 && RA>12)  {RA+=1;}
                     }
                    break;
            }
            }
            crr.setWeight(RAT);
            }
        break;    
        case NO_2LAST:
            if (csets.get('B').length()>=24) TGT=1;
            else TGT=0;
            for (Card crr:legalMovs)
            {
            SU=crr.getSuit();
            RA=crr.getRank();
            crr.setWeight(0);
            OTD=otdach(cs,crr);
            RAT=isMY(crr);
            switch (ONDESK)
            {
                case 0:
                    if (TGT>0)
                    {
                      RAT=10-RAT;  
                    }
                    else
                    {
                         RAT=7+crr.rating[0]-crr.rating[1];
                         //if (crr.rating[1]==0) RAT=0;
                    }
                    break;
                case 1:
                case 2:
                    if (TGT>0)
                    {
                        RAT=10-RAT;  
                    }
                    else
                    {
                        RAT=7+crr.rating[0]-crr.rating[1];
                        //if (crr.rating[1]==0 && crr.rating[0]>0) RAT=0;
                    }
                    break;
                case 3:
                    if (TGT>0)
                    {
                        RAT=10-RAT;
                    }
                    else
                    {
                        RAT=7+crr.rating[0]-crr.rating[1];
                        //if (crr.rating[1]==0) RAT=0;
                    }
                    break;
            }
            
            crr.setWeight(RAT);
            }
        break;    
        case NO_KING:
            wTGT=whereIs(cs,4,13);
            OTD0=otdach(cs,null);//отдач для хода
            for (Card crr:legalMovs)
            {
            SU=crr.getSuit();
            RA=crr.getRank();
            crr.setWeight(0);
            OTD=otdach(cs,crr);
            RAT=isMY(crr);
            isTGT=RA==13&&SU==4;
            if (!inSuit)
            {
               if (SU==4) {
                 if (RA<13) RAT=0; else RAT=10+(RA==13?1:0);
               }
               else
               {
                 RAT=7+crr.rating[0]-crr.rating[1];
               }
            }
            else
            {
            switch (ONDESK)
            {
                case 0:
                  if (RAT==10)  
                  {
                  if (SU==4)    
                  {
                      if (RA>12) {RAT=0;break;}
                  }
                  else
                  {//su!=4
                    RAT=(SA<3?0:1);                 
                  }//su!=4
                  }//RAT=10
                  else if (RAT==0)
                  {
                     if (SU==4) {if (RA>12) {RAT=11+(RA==13?1:0);break;} else break;}
                     RAT=7+crr.rating[0]-crr.rating[1];
                  }
                  else
                  {
                     if (wTGT>1){if (OTD>1f) {break;}else{RAT=0;break;}}
                     else if (isTGT) {RAT=0;break;}
                     else RAT=10-RAT+(SA<3?0:1); 
                  }
                break;
                case 1:
                case 2:
                 if (RAT==10)   
                 {
                   if (SU==4) {if (RA>12) {RAT=1-(RA==13||wTGT==1?1:0);break;} else {if (OTD<1f) {RAT=0;}break;}}
                  else
                  {//su!=4
                    if (wTGT>1){if (OTD>1f) {break;}else{RAT=0;break;}}
                     else if (wTGT==1||isTGT) {RAT=0;break;}
                     else RAT=10-RAT+(SA<3?0:1); 
                  }//su!=4
                 }
                 else if (RAT==0)
                 {
                    if (SU==4) {if (RA>12) {RAT=12+(isTGT?1:0);break;} else {break;}} 
                    if (isTGT || wTGT==1) {RAT=12;break;}
                    RAT=7+crr.rating[0]-crr.rating[1];
                 }
                 else
                 {
                   if (isTGT || wTGT==1) {RAT=0;break;}  
                   RAT=10-RAT+(SA<AHEAD?0:1);   
                 }
                break;
                case 3://RAT==0 || RAT==10 ONLY !!!
                 if (RAT==0)
                 {//неберучая карта
                     if (SU==4) {if (RA<13) {break;} else {RAT=11+(isTGT||wTGT==1?1:0);break;}}
                     RAT=7+crr.rating[0]-crr.rating[1];//чем больше младших на чужих руках, тем вероятнее отдача
                 }
                 else
                 {//эта карта (crr) берет
                     if (isTGT || wTGT==1) {RAT=0;break;}
                     if (OTD<0.5f)
                     {//почти совсем нету отдач
                        RAT=10-RAT;  
                     }
                     else
                     {
                       RAT=7+crr.rating[0]-crr.rating[1];//чем больше младших на чужих руках, тем вероятнее перехват
                     }
                 }
                break;
            }
            }
            crr.setWeight(RAT);
            }//loop
        break;    
            
        case NO_ALL:
            wTGT=2;//за то что взятка
            OTD=otdach(cs,null);
            for(Card C:cs.getCards()) 
            {
                SU=C.getSuit();
                RA=C.getRank();
                wTGT+=(SU==4?2:0)+(RA==12?4:0)+(rules.isKingBoy()?RA==11||RA==13?2:0:RA==11?4:0)+(RA==13&&SU==4?16:0);
            }
            wTGT+=(csets.get('B').length()>=24?8:0);
            OTD0=otdach(cs,null);//отдач для хода
            for (Card crr:legalMovs)
            {
            SU=crr.getSuit();
            RA=crr.getRank();
            crr.setWeight(0);
            OTD=otdach(cs,crr);
            RAT=isMY(crr);
            
            //isTGT=RA==13&&SU==4;
            TGT=(SU==4?2:0)+(RA==12?4:0)+(rules.isKingBoy()?RA==11||RA==13?2:0:RA==11?2:0)+(RA==13&&SU==4?16:0);
           // wTGT1=TGT*20/100;
            //System.out.println(crr.getName()+" wTGT1="+wTGT1);
            if (!inSuit)
            {
              RAT=7+crr.rating[0]-crr.rating[1]+TGT; 
            }
            else
            {
            switch (ONDESK)
            {
                case 0:
                    if (RAT==0)
                    {
                       RAT=7+crr.rating[0]-crr.rating[1]+TGT; 
                    }
                    else if (RAT==10)
                    {
                        RAT=0;
                    }
                    else
                    {
                        RAT=10-RAT;
                       //RAT=7+crr.rating[0]-crr.rating[1]; 
                    }
                break;
                case 1:
                case 2:
                 if (RAT==0)
                    {
                       RAT=7+crr.rating[0]-crr.rating[1]+TGT; 
                       if (RAT<0) RAT=0;
                    }
                    else if (RAT==10)
                    {
                        RAT=0;
                    }
                    else
                    {
                       if (NOTAKE>0) RAT=1;
                       else RAT=10-RAT; 
                       //RAT=7+crr.rating[0]-crr.rating[1]; 
                    }
                break;
                case 3://RAT==0 || RAT==10 ONLY !!!
                 if (RAT==0)
                    {
                       RAT=7+crr.rating[0]-crr.rating[1]+TGT; 
                       if (RAT<0) RAT=0;
                    }
                    else
                    {
                        if ((wTGT+TGT)<4 && OTD>1f) RAT=7+crr.rating[0]-crr.rating[1]; 
                        else RAT=0;
                    }
                break;
            }
            }
            crr.setWeight(RAT);
            }//loop
        break;
            
        case YES_TRICKS:
            OTD0=otdach(cs,null);//отдач для хода
            for (Card crr:legalMovs)
            {
            SU=crr.getSuit();
            RA=crr.getRank();
            crr.setWeight(0);
            OTD=otdach(cs,crr);
            RAT=isMY(crr);
            if (!inSuit)
            {
               //if (crr.rating[1]<1 &&) 
               RAT=crr.rating[1]-crr.rating[0];
               if (RAT<0) RAT=0;
               
            }
            else
            {
            switch (ONDESK)
            {
                case 0:
                  if (RAT==0)
                  {
                      RAT=crr.rating[1]-crr.rating[0];
                      if (RAT<0) RAT=0;
                  }
                  else if (RAT==10)   
                  {
                      RAT=9;
                  }
                  else
                  {
                      if (RAT>7) RAT=0;
                  }
                break;
                    
                case 1:
                case 2:
                  if (RAT==0)
                  {
                      RAT=crr.rating[1]-crr.rating[0];
                      if (RAT<0) RAT=0;
                  }
                  else if (RAT==10)   
                  {
                      
                  }
                  else
                  {
                      if (RAT>8) RAT=10;
                  }  
                break;
                    
                case 3://RAT==0 || RAT==10 ONLY !!!
                 if (RAT==0)
                 {//неберучая карта
                      RAT=crr.rating[1]-crr.rating[0];
                      if (RAT<0) RAT=0;
                 }
                 else
                 {//эта карта (crr) берет
                     if (OTD<0.5f)
                     {//почти совсем нету отдач
                          
                     }
                     else
                     {
                     }
                 }
                break;
            }
            }
            crr.setWeight(RAT);
            }//loop
        break;
        case YES_HEARTS:
            OTD0=otdach(cs,null);//отдач для хода
            TGT=DTcs.howSuits()[3];
            for (Card crr:legalMovs)
            {
            SU=crr.getSuit();
            RA=crr.getRank();
            crr.setWeight(0);
            OTD=otdach(cs,crr);
            RAT=isMY(crr);
            isTGT=SU==4;
            if (!inSuit)
            {
                RAT=crr.rating[1]-crr.rating[0];
                if (isTGT) RAT-=2;
                if (RAT<0) RAT=0;
            }
            else
            {
            switch (ONDESK)
            {
                case 0:
                  if (RAT==0)
                  {
                     RAT=crr.rating[1]-crr.rating[0];
                     if (isTGT) RAT-=2;
                     if (RAT<0) RAT=0;
                  }
                  else if (RAT==10)
                  {
                     if (!isTGT) RAT-=OTD;
                     if (RAT<0) RAT=0;
                  }
                  else
                  {
                      
                  }
                break;
                    
                case 1:
                case 2:
                  if (RAT==0)
                  {
                      
                  }
                  else if (RAT==10)
                  {
                     if (TGT>0) RAT+=TGT;
                     if (isTGT) RAT++;
                  }
                  else
                  {
                    if (ONDESK==3) if (TGT>0 || isTGT) RAT++; else RAT=0;
                  }  
                break;
                    
                case 3://RAT==0 || RAT==10 ONLY !!!
                 if (RAT==0)
                 {//неберучая карта
                    RAT=crr.rating[1]-crr.rating[0];
                    if (isTGT) RAT-=2;
                    if (RAT<0) RAT=0;         
                 }
                 else
                 {//эта карта (crr) берет
                  //если там нет червей - не нужна
                     if (TGT==0) 
                     {
                     RAT=0;
                     if (OTD<1f)
                     {//почти совсем нету отдач
                          
                     }
                     else
                     {

                     }
                     }
                     else RAT+=TGT;
                 }
                break;
            }
            }
            crr.setWeight(RAT);
            }//loop
        break;
        case YES_BOYS:
            OTD0=otdach(cs,null);//отдач для хода
            TGT=DTcs.howRank(11);
            if (rules.isKingBoy()) TGT+=DTcs.howRank(13);
            
            for (Card crr:legalMovs)
            {
            SU=crr.getSuit();
            RA=crr.getRank();
            crr.setWeight(0);
            OTD=otdach(cs,crr);
            RAT=isMY(crr);
            wTGT=whereIs(cs,SU,11);
            wTGT1=(rules.isKingBoy()?whereIs(cs,SU,13):0);
            HD=cs.holeDown(crr);
            HU=cs.holeUp(crr);
            isTGT= RA==11 || rules.isKingBoy()&& RA==13;
            if (!inSuit)
            {
              RAT=5+crr.rating[1]-crr.rating[0];
              if (HD!=-1 && HD>crr.rating[3]) RAT--;
              if (HU!=-1 && HU>crr.rating[3]) RAT--;
              if (RAT<0) RAT=0;
              if (!isTGT) RAT++;
            }
            else
            {
            switch (ONDESK)
            {
                case 0:
                  if (RAT==0)
                  {
                      if (isTGT) RAT=0;
                  }
                  else if (RAT==10)
                  {
                      if (isTGT) {RAT++;break;}
                      if ((wTGT1==-1 || wTGT==-1) && RA==14) if (crr.rating[6]<3) RAT++; else RAT--;
                      if (wTGT==-1  && RA==12) if (crr.rating[6]<3 && crr.rating[2]==11) RAT++;else RAT--;
                  }
                  else
                  {
                      if (isTGT) {RAT=0;break;}
                      RAT=crr.rating[1]-crr.rating[0];
                      if (RAT<0) RAT=0;
                  }
                  //if (isTGT && RAT!=10) {RAT=0;break;}  
                  //if (RAT>8 && crr.rating[0]>3) RAT=0;
                break;
                    
                case 1:
                case 2:
                    if (RAT==0)
                    {
                      RAT=crr.rating[1]-crr.rating[0];
                      if (!isTGT) RAT++;
                      if (RAT<0) RAT=0;
                    }
                    else if (RAT==10)
                    {
                       if (TGT>0||isTGT) {RAT+=(TGT+(isTGT?1:0));break;} 
                       if (RA==14 && crr.rating[6]>4) RAT=5;
                    }
                    else
                    {
                        
                    }
                    
                    //if (RAT==10&&(TGT>0||isTGT)) {RAT+=(TGT+(isTGT?1:0));break;}
                    //if (RAT==0 && !isTGT){RAT=7-crr.rating[0];}
                break;
                    
                case 3://RAT==0 || RAT==10 ONLY !!!
                 if (RAT==0)
                 {//неберучая карта
                     RAT=crr.rating[1]-crr.rating[0];
                      if (!isTGT) RAT++;
                      if (RAT<0) RAT=0;
                 }
                 else
                 {//эта карта (crr) берет
                  //если там нет червей - не нужна
                     if (TGT==0 && !isTGT && OTD>1) {RAT=0;break;}
                     if (TGT>0||isTGT) {RAT+=(TGT+(isTGT?1:0));break;}
                     
                 }
                break;
            }
            }
            crr.setWeight(RAT);
            }//loop
        break;    
        case YES_GIRLS:
            OTD0=otdach(cs,null);//отдач для хода
            TGT=DTcs.howRank(12);
            
            for (Card crr:legalMovs)
            {
            SU=crr.getSuit();
            RA=crr.getRank();
            crr.setWeight(0);
            OTD=otdach(cs,crr);
            RAT=isMY(crr);
            isTGT= RA==12;
            HD=cs.holeDown(crr);
            HU=cs.holeUp(crr);
            wTGT=whereIs(cs,SU,12);
            
            if (!inSuit)
            {
              RAT=5+crr.rating[1]-crr.rating[0];
              if (HD!=-1 && HD>crr.rating[3]) RAT--;
              if (HU!=-1 && HU>crr.rating[3]) RAT--;
              if (RAT<0) RAT=0;
              if (!isTGT) RAT+=2;
            }
            else
            {
            switch (ONDESK)
            {
                case 0:
                  if (RAT==0)
                  {
                      if (isTGT) RAT=0;
                  }
                  else if (RAT==10)
                  {
                      if (isTGT) {RAT++;break;}
                      //if ((wTGT1==-1 || wTGT==-1) && RA==14) if (crr.rating[6]<3) RAT++; else RAT--;
                      if (wTGT==-1  && RA>12) if (crr.rating[6]<3 && crr.rating[2]==11) RAT++;else RAT--;
                  }
                  else
                  {
                      if (isTGT) {RAT=0;break;}
                      RAT=crr.rating[1]-crr.rating[0];
                      if (RAT<0) RAT=0;
                  }
                  //if (isTGT && RAT!=10) {RAT=0;break;}  
                  //if (RAT>8 && crr.rating[0]>3) RAT=0;
                break;
                    
                case 1:
                case 2:
                    if (RAT==0)
                    {
                      RAT=crr.rating[1]-crr.rating[0];
                      if (!isTGT) RAT++;
                      if (RAT<0) RAT=0;
                    }
                    else if (RAT==10)
                    {
                       if (TGT>0||isTGT) {RAT+=(TGT+(isTGT?1:0));break;} 
                       if (RA==14 && crr.rating[6]>3) RAT=5;
                    }
                    else
                    {
                        
                    }
                    
                    //if (RAT==10&&(TGT>0||isTGT)) {RAT+=(TGT+(isTGT?1:0));break;}
                    //if (RAT==0 && !isTGT){RAT=7-crr.rating[0];}
                break;
                    
                case 3://RAT==0 || RAT==10 ONLY !!!
                 if (RAT==0)
                 {//неберучая карта
                     RAT=crr.rating[1]-crr.rating[0];
                      if (!isTGT) RAT++;
                      if (RAT<0) RAT=0;
                 }
                 else
                 {//эта карта (crr) берет
                  //если там нет червей - не нужна
                     if (TGT==0 && !isTGT && OTD>1) {RAT=0;break;}
                     if (TGT>0||isTGT) {RAT+=(TGT+(isTGT?1:0));break;}
                     
                 }
                break;
            }
            }
            crr.setWeight(RAT);
            }//loop
        break;    
        case YES_2LAST:
            OTD0=otdach(cs,null);//отдач для хода
            TGT=(csets.get('B').length()>=24?1:0);
            
            for (Card crr:legalMovs)
            {
            SU=crr.getSuit();
            RA=crr.getRank();
            crr.setWeight(0);
            OTD=otdach(cs,crr);
            RAT=isMY(crr);
            isTGT= TGT>0;
            
            if (!inSuit)
            {
               RAT=5+crr.rating[1]-crr.rating[0]; 
               if (RAT<0) RAT=0;
            }
            else
            {
            switch (ONDESK)
            {
                case 0:
                  if (RAT==0) 
                  {
                      
                      if (!isTGT) RAT=3+crr.rating[1]-crr.rating[0];
                      if (RAT<0) RAT=0;
                  }
                  else if (RAT==10)
                  {
                      if (!isTGT) RAT=5+crr.rating[1]-crr.rating[0];
                      if (RAT<0) RAT=0;
                      if (OTD<0.05f) RAT=10;
                  }
                  else
                  {
                      if (!isTGT) RAT=4+crr.rating[1]-crr.rating[0];
                      if (RAT<0) RAT=0;
                  }
                break;
                    
                case 1:
                case 2:
                  if (RAT==0) 
                  {
                      if (!isTGT) RAT=3+crr.rating[1]-crr.rating[0];
                      if (RAT<0) RAT=0;
                  }
                  else if (RAT==10)
                  {
                      if (!isTGT) RAT=5+crr.rating[1]-crr.rating[0];
                      if (RAT<0) RAT=0;
                      if (OTD<0.05f) RAT=10;
                  }
                  else
                  {
                      if (!isTGT) RAT=4+crr.rating[1]-crr.rating[0];
                      if (RAT<0) RAT=0;
                  }
                break;
                    
                case 3://RAT==0 || RAT==10 ONLY !!!
                    if (RAT==0) 
                  {
                      if (!isTGT) RAT=3+crr.rating[1]-crr.rating[0];
                      if (RAT<0) RAT=0;
                  }
                  else
                  {
                      if (!isTGT) RAT=5+crr.rating[1]-crr.rating[0];
                      if (RAT<0) RAT=0;
                      if (OTD<0.05f) RAT=10;
                  }
                break;
            }
            }
             crr.setWeight(RAT);
            }//loop
        break;   
            
        case YES_KING:
            OTD0=otdach(cs,null);//отдач для хода
            TGT=DTcs.find(13,4)!=null?1:0;
            
            for (Card crr:legalMovs)
            {
            SU=crr.getSuit();
            RA=crr.getRank();
            crr.setWeight(0);
            OTD=otdach(cs,crr);
            RAT=isMY(crr);
            isTGT= SU==4 && RA==13;
            
            if (!inSuit)
            {
              RAT=5-crr.rating[0]+crr.rating[1];  
              if (SU==4 && RA<13) RAT+=2;
              if (isTGT || TGT>0) {RAT=0;}
              if (RAT<0) RAT=0;
            }
            else
            {
            switch (ONDESK)
            {
                case 0:
                    if (RAT==0)
                    {
                      RAT=5-crr.rating[0]+crr.rating[1];  
                      if (SU==4 && RA<13) RAT+=2;
                      if (isTGT || TGT>0) {RAT=0;}
                      if (RAT<0) RAT=0;
                    }
                    else if (RAT==10)
                    {
                      if (isTGT)  RAT+=2;
                      else if (SU==4&&RA==14&&SA>2) RAT+=1;
                      else RAT=5-crr.rating[0]+crr.rating[1];  
                      if (RAT<0) RAT=0;
                      break;
                    }
                    else
                    {
                      if (isTGT || TGT>0) RAT=RAT>0.8f && SA==3?10:0;
                        else RAT=10-RAT;
                    }
                break;
                    
                case 1:
                case 2:
                    if (RAT==0)
                    {
                      RAT=5-crr.rating[0]+crr.rating[1];  
                      if (SU==4 && RA<13) RAT+=2;  
                      if (isTGT || TGT>0) {RAT=0;}
                      if (RAT<0) RAT=0;
                    }
                    else if (RAT==10)
                    {
                        if (TGT>0||isTGT) RAT+=2;
                        else RAT=5-crr.rating[0]+crr.rating[1];  
                        if (RAT<0) RAT=0;
                    }
                    else
                    {
                        if (isTGT || TGT>0) RAT=RAT>0.8f && SA==(3-ONDESK)?10:0;
                        else RAT=10-RAT;
                        
                    }
                break;
                    
                case 3://RAT==0 || RAT==10 ONLY !!!
                 if (RAT==0)
                    {
                      RAT=5-crr.rating[0]+crr.rating[1];  
                      if (SU==4 && RA<13) RAT+=2; 
                      if (isTGT || TGT>0) {RAT=0;}
                    }
                    else
                    {
                     if (TGT>0 || isTGT) {RAT+=2;break;}
                     if (OTD<0.05f) RAT+=1;
                     else if (RA==14 && SU==4) RAT=0;
                     else RAT=4;   
                    }   
                break;
            }
            }
            crr.setWeight(RAT);
            }//loop
        break;    
        case YES_ALL:
            OTD0=otdach(cs,null);//отдач для хода
            TGT=DTcs.find(13,4)!=null?8:0;
            TGT+=DTcs.howSuits()[3];
            TGT+=DTcs.howRank(12)*2;
            if (rules.isKingBoy())
            {
             TGT+=(DTcs.howRank(11)+DTcs.howRank(13));   
            }
            else
            {
             TGT+=DTcs.howRank(11)*2;    
            }
            if (csets.get('B').length()>=24) TGT+=3;
            
            for (Card crr:legalMovs)
            {
            SU=crr.getSuit();
            RA=crr.getRank();
            crr.setWeight(0);
            OTD=otdach(cs,crr);
            RAT=isMY(crr);

            wTGT=SU==4?1:0;
            if (RA==12) wTGT+=2;
            
            OST=whereIs(cs,12,SU)<0?2:0;
            OST=OST+whereIs(cs,11,SU)<0?rules.isKingBoy()?1:2:0;
            OST=OST+(rules.isKingBoy()?whereIs(cs,13,SU)<0?SU==4?9:1:0:0);
            if (SU==4) OST+=crr.rating[6];
            
            if (rules.isKingBoy())
            {
             wTGT+=RA==11||RA==13?1:0;   
            }
            else
            {
             wTGT+=RA==11?2:0;    
            }
            
            wTGT+=RA==13&&SU==4?4:0;
            isTGT= wTGT>=2;//начиная с девочки, червового мальчика етс
            wTGT1=TGT+wTGT;
            
            if (!inSuit)
            {
                if (SU==4 && RA==13) RAT=0; 
                else if (RA==12 || RA==11 && !rules.isKingBoy() || RA==14 && OST>2) RAT=1;
                else if (rules.isKingBoy() && (RA==11 || RA==13) || SU==4) RAT=2;
                else RAT=7+crr.rating[1]-crr.rating[0];
                //if (SU==4) RAT--;
                if (RAT<0) RAT=0;
             
            }
            else
            {
            switch (ONDESK)
            {
                case 0:
                  if (RAT==0)  
                  {
                      if (csets.get('B').length()<17&&crr.rating[1]>0) {RAT=crr.rating[1];break;}      
                  }
                  else if (RAT==10)
                  {
                      if (OST>2) RAT+=wTGT;
                      else RAT=0;
                      //else RAT-=crr.rating[0];
                  }
                  else
                  {
                    RAT=4-crr.rating[0]+crr.rating[1];  
                    if (RAT<0) RAT=0;
                  }
                break;
                    
                case 1:
                case 2:
                    if (RAT==0)
                    {
                    if (crr.rating[0]<2&&csets.get('B').length()<17&&crr.rating[1]>0) {RAT=crr.rating[1];break;}
                    }
                    else if (RAT==10)
                    {
                    if (wTGT1>2 || OST>2 && crr.rating[6]<4) RAT+=wTGT;
                    else RAT=1;
                    }
                    else
                    {
                     if (wTGT>2 && RAT<8) RAT=0;
                    }
                break;
                    
                case 3://RAT==0 || RAT==10 ONLY !!!
                 if (RAT==0)
                 {//неберучая карта
                     if (crr.rating[0]==0&&csets.get('B').length()<17&&crr.rating[1]>0) {RAT=crr.rating[1];break;}
                 }
                 else
                 {//эта карта (crr) берет
                     if (wTGT1>2) {RAT=10+wTGT1-2;break;}
                     else if (OST>3) {RAT=1;break;}
                 }
                break;
            }
            }
            crr.setWeight(RAT);
            }//loop
        break;         
    
    }
    boolean hi=!isTakeGood();
    //if (inSuit) hi=!hi;
    res=selectCard(hi);
    //String s=res.getName()+" WGH="+res.getWeight()+ " [";
    //for (Card CA:legalMovs) s=s+CA.getName()+"="+CA.getWeight()+",";
    //s+="] inSuit="+inSuit;        
    //System.out.println(s);
    //if (DTcs.length()==3) System.out.println("------------");
    return res;
}



private int whereIs(CardSet cs,int su, int ra)
{//3:у меня в legalMovs, 2:у меня, 1:на столе 0:вышла, -1:у соперников на руках
  if (csets.get('T').find(ra, su)!=null) return 1;
  if (csets.get('B').find(ra, su)!=null) return 0;
  if (cs.find(ra, su)!=null) 
  {
      for (Card C:legalMovs) if (C.getSuit()==su&&C.getRank()==ra) return 3;
      return 2;
  }
  return -1;
}

private Card selectCard(boolean hi)
{//hi - поведение при одинаковом рейтинге
 //true-выбирается самая старшая с самым высоким рейтингом
 //false-выбирается самая младшая с самым высоким рейтингом
 if (legalMovs.size()==0) return null;
 Card TMP=null;
 int RATING=-1;
 for (Card CR:legalMovs)
 {
     if (CR.getWeight()>RATING){
         RATING=CR.getWeight();
         TMP=CR;}
     else if (CR.getWeight()==RATING && TMP!=null)
     {
         if (hi)
         {
            if (CR.getRank()>TMP.getRank()) TMP=CR;
         }
         else
         {
            if (CR.getRank()<TMP.getRank()) TMP=CR; 
         }
     }
 }
 return TMP;
}




}
