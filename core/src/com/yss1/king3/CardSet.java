/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yss1.king3;

import com.yss1.lib_jm.Card;
import com.yss1.lib_jm.CardSetBase;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.Arrays;

/**
 *
 * @author ys
 */
public class CardSet extends CardSetBase {

    private boolean need_clear;
    private final int[] suits;

    public CardSet(Main m, Node n) {
        super(m, n);
        suits = new int[6];
    }

    public boolean isNeedClear() {
        return need_clear;
    }

    public void setNeedClear(boolean need_clear) {
        this.need_clear = need_clear;
    }
    private boolean end_party;

    public boolean isEndParty() {
        return end_party;
    }

    public void setEndParty(boolean end_party) {
        this.end_party = end_party;
    }
    private int tricks;

    public int getTricks() {
        return tricks;
    }

    public void setTricks(int tricks) {
        this.tricks = tricks;
    }

    public void incTricks(int i) {
        tricks += i;
    }

    public void putStock() {//ровненько (с флуктуациями) колодой рубашкой вверх в середине экрана
        for (int i = 0; i < cards.size(); i++) {
            Vector3f Vm = Tools.vPool.getV3(0, 0, Sett.h0 + Sett.dh * i);
            Vector3f Vs = Tools.vPool.getV3(1f, 1f, 1f);
            Quaternion Q1 = Tools.vPool.getQt(FastMath.PI, 0, Tools.fluctuate(Sett.fluctA));
            cards.get(i).getGe().setLocalTransform(new Transform(Vm,
                    Q1,
                    Vs));
            cards.get(i).setState(Card.State.BACK, true);
            Tools.vPool.freeV3(Vs);
            Tools.vPool.freeV3(Vm);
            Tools.vPool.freeQt(Q1);
        }
    }

    public void putRound() {
        if (cards.size() < 2) {
            return;
        }
        float A0 = FastMath.TWO_PI / cards.size();
        float A;
        float L = 2;
        Vector3f Vm = Tools.vPool.getV3();
        Vector3f Vs = Tools.vPool.getV3(1f, 1f, 1f);
        Quaternion Q1 = Tools.vPool.getQt();
        for (int i = 0; i < cards.size(); i++) {
            cards.get(i).reset();
            A = A0 * i + FastMath.HALF_PI;
            cards.get(i).getGe().setLocalTranslation(Vm.set(L * FastMath.cos(A), L * FastMath.sin(A) - Sett.dtTrans.y, Sett.h0 + Sett.dh * (cards.size() - i)));
            cards.get(i).getGe().setLocalRotation(Q1.fromAngles(FastMath.PI, 0, A0 * i));
            cards.get(i).getGe().setLocalScale(Vs);
            cards.get(i).setState(Card.State.BACK, true);
        }
        Tools.vPool.freeV3(Vs);
        Tools.vPool.freeV3(Vm);
        Tools.vPool.freeQt(Q1);
    }

    public void generateEndpointDT(Card cr) {
        cr.setEndpointS(Sett.scaleDesk);
        float Y;
        if (Sett.playersCount == 3) {
            Y = Sett.Y13dt;
        } else {
            Y = Sett.Y14dt;
        }

        float A = 0;
        if (Sett.is_fluctuate) {
            A = Tools.fluctuate(Sett.fluctA);
        }
        float ratio = Sett.CARD_RATIO;//ap.GAME.getRatio();

        float cH = Sett.cardW() * ratio / 2f;
        float cW = Sett.cardW() / 2f;
        float delta = Sett.cardW() / 4;
        float dH=(cards.indexOf(cr)+1) * Sett.dh;
        switch (cr.getOwner()) {
            case 'D':
                cr.setEndpointM(0f, -cW - delta, Sett.h0 + dH);
                cr.setEndpointRv(FastMath.PI * 2, 0, A);
                break;
            case 'L':
                cr.setEndpointM(-cW - delta, 0, Sett.h0 + dH);
                cr.setEndpointRv(FastMath.PI * 2, 0, FastMath.HALF_PI * 3 + A);
                break;
            case 'U':
                cr.setEndpointM(0, cW + delta, Sett.h0 + dH);
                cr.setEndpointRv(FastMath.PI * 2, 0, FastMath.PI + A);
                break;
            case 'R':
                cr.setEndpointM(cW + delta, 0, Sett.h0 + dH);
                cr.setEndpointRv(FastMath.PI * 2, 0, FastMath.HALF_PI + A);
                break;
        }


    }

    public void reorderDT()
    {
        for (Card ca: cards)
        {
            ca.setIdleAnim();
            generateEndpointDT(ca);
            ca.applyEndpoint();
        }
    }
    
    
    public void clearDT(char whoTake) {
        if (!myNode.getName().equals("pDesk") || cards.size() < 1) {
            return;
        }
        int i = 0;
        float far_point_mult = 10f;
        Quaternion Q;
        float[] Aarr;
        float A;
        for (Card Cr : cards) {
            Cr.setEndpointS(1);
            switch (whoTake) {
                case 'D':
                    Cr.setEndpointM(0, -Sett.deskHh * 1.3f, Sett.h0 * far_point_mult + i * Sett.dh);
                    switch (Cr.getOwner()) {
                        case 'D':
                            Cr.setEndpointRv(0, 0, 0);
                            break;
                        case 'L':
                            Cr.setEndpointRv(0, 0, FastMath.HALF_PI);
                            break;
                        case 'U':
                            Cr.setEndpointRv(0, 0, FastMath.PI);
                            break;
                        case 'R':
                            Cr.setEndpointRv(0, 0, -FastMath.HALF_PI);
                            break;
                    }
                    break;
                case 'L':
                    Cr.setEndpointM(-Sett.deskHw * 1.3f, 0, Sett.h0 * far_point_mult + i * Sett.dh);
                    switch (Cr.getOwner()) {
                        case 'D':
                            Cr.setEndpointRv(0, 0, -FastMath.HALF_PI);
                            break;
                        case 'L':
                            Cr.setEndpointRv(0, 0, 0);
                            break;
                        case 'U':
                            Cr.setEndpointRv(0, 0, FastMath.HALF_PI);
                            break;
                        case 'R':
                            Cr.setEndpointRv(0, 0, FastMath.PI);
                            break;
                    }

                    break;
                case 'U':
                    Cr.setEndpointM(0, Sett.deskHh * 1.3f, Sett.h0 * far_point_mult + i * Sett.dh);
                    switch (Cr.getOwner()) {
                        case 'D':
                            Cr.setEndpointRv(0, 0, FastMath.PI);
                            break;
                        case 'L':
                            Cr.setEndpointRv(0, 0, -FastMath.HALF_PI);
                            break;
                        case 'U':
                            Cr.setEndpointRv(0, 0, 0);
                            break;
                        case 'R':
                            Cr.setEndpointRv(0, 0, FastMath.HALF_PI);
                            break;
                    }
                    break;
                case 'R':
                    Cr.setEndpointM(Sett.deskHw * 1.3f, 0, Sett.h0 * far_point_mult + i * Sett.dh);
                    switch (Cr.getOwner()) {
                        case 'D':
                            Cr.setEndpointRv(0, 0, FastMath.HALF_PI);
                            break;
                        case 'L':
                            Cr.setEndpointRv(0, 0, FastMath.PI);
                            break;
                        case 'U':
                            Cr.setEndpointRv(0, 0, -FastMath.HALF_PI);
                            break;
                        case 'R':
                            Cr.setEndpointRv(0, 0, 0);
                            break;
                    }
                    break;
            }
            i++;
            //Cr.goOutAnim();
            animator.animate2endpointPreUp(Cr, Sett.animTime / 2f, 12, "ClearDT", 0.5f);
        }
    }

    public void distribute(boolean fast) {
        if (cards.size() < 1) {
            return;
        }
        int[] su_arr = howSuits();
        //UNREM || true DOWN when PRODUCTION
        boolean is_my = myNode.getName().equals("pDown");
        if (is_my) {
            sort();
        }
        
        boolean sep_suits = is_my && cards.size() > 4 && Sett.sep_swits;
        int wholes = su_arr[5];
        if (!sep_suits) {
            wholes = 0;
        } else {
            wholes -= 1;
        }

        float angle0 = Math.min(cards.size() > 1 ? FastMath.PI / 3 / (cards.size() - 1 + wholes) : 0, FastMath.PI / 18);
        float angle_start = angle0 * (cards.size() - 1 + wholes) / 2f;
        int i = -1;
        float L = Sett.veerPoint / 2;
        if (is_my) {
            L = Sett.veerPoint;
        }
        float A, B;
        int old_suit = 0;


        for (Card CR : cards) {
            if (sep_suits && old_suit > 0 && old_suit != CR.getSuit()) {
                i += 2;
            } else {
                i++;
            }
            old_suit = CR.getSuit();
            if (!fast) {
                CR.reset();
                if (!is_my) {
                    CR.setState(Card.State.BACK);
                }
            }

            A = angle_start - angle0 * i;
            CR.setEndpointM(L * FastMath.cos(FastMath.HALF_PI + A), L * FastMath.sin(FastMath.HALF_PI + A) - L, Sett.h0 + (i) * Sett.dh);

            if (CR.getState() == Card.State.BACK) {
                B = FastMath.PI;
            } else {
                B = 0;
            }
            CR.setEndpointRv(B, 0, A);
            if (is_my) {
                CR.setEndpointS(Sett.scaleMy);
            } else {
                CR.setEndpointS(Sett.scaleOther);
            }
            animator.animate2endpoint(CR, Sett.animTime / 2f, 12, "Distrib");
        }
    }

    public int[] howSuits() {//массив кол-во карт по мастям (последняя позиция - кол-во мастей)
        Arrays.fill(suits, 0);

        int idx;
        for (Card CR : cards) {
            idx = CR.getRank() != 15 ? CR.getSuit() : 5;
            if (suits[idx - 1] == 0) {
                suits[5]++;
            }
            suits[idx - 1]++;
        }
        return suits;
    }

    public int howRank(int r) {//количество карт этой величины
        int ra = 0;
        for (Card CR : cards) {
            if (CR.getRank() == r) {
                ra++;
            }
        }
        return ra;
    }

    public int howLo(int r, int s) {//количество младших карт в масти
        int ra = 0;
        for (Card CR : cards) {
            if (CR.getRank() < r && CR.getSuit() == s) {
                ra++;
            }
        }
        return ra;
    }

    public int howHi(int r, int s) {//количество старших карт в масти
        int ra = 0;
        for (Card CR : cards) {
            if (CR.getRank() > r && CR.getSuit() == s) {
                ra++;
            }
        }
        return ra;
    }

    public Card loCard(int sw) {//младшая в масти
        Card res = null;
        int tmp = 20;
        for (Card c : cards) {
            if (c.getSuit() == sw && c.getRank() < tmp) {
                tmp = c.getRank();
                res = c;
            }
        }
        return res;
    }

    public Card hiCard(int sw) {//старшая в масти
        Card res = null;
        int tmp = 0;
        for (Card c : cards) {
            if (c.getSuit() == sw && c.getRank() > tmp) {
                tmp = c.getRank();
                res = c;
            }
        }
        return res;
    }

    public Card nextLoCard(int sw, int ra) {//ближайшая меньшая в масти
        Card res = null;
        int tmp = 0;
        for (Card c : cards) {
            if (c.getSuit() == sw && c.getRank() < ra && c.getRank() > tmp) {
                res = c;
                tmp = c.getRank();
            }
        }
        return res;
    }

    public Card nextHiCard(int sw, int ra) {//ближайшая старшая в масти
        Card res = null;
        int tmp = 20;
        for (Card c : cards) {
            if (c.getSuit() == sw && c.getRank() > ra && c.getRank() < tmp) {
                res = c;
                tmp = c.getRank();
            }
        }
        //if (res!=null) System.out.println(res);else System.out.println("null");
        return res;
    }

    public int holeUp(Card Ca) {
        Card Cx = nextHiCard(Ca.getSuit(), Ca.getRank());
        if (Cx == null) {
            return -1;//старше вообше нет
        }
        return Cx.getRank() - Ca.getRank() - 1;
    }

    public int holeDown(Card Ca) {
        Card Cx = nextLoCard(Ca.getSuit(), Ca.getRank());
        if (Cx == null) {
            return -1;//старше вообше нет
        }
        return Ca.getRank() - Cx.getRank() - 1;
    }
}
