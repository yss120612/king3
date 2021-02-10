/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yss1.king3;

import com.yss1.lib_jm.IGameRules;

/**
 *
 * @author ys
 */
public class GameRules implements IGameRules {

    private boolean kingBoy;
    private boolean kingBoyWork;
    private boolean kingBoyBackup;
    private int dummy;
    private int dummyWork;
    private int dummyBackup;
    private boolean rulesChanged;
    private final String separator = "->";
    private final String parametrSeparator = "_";

    public GameRules() {
        kingBoy = true;
        dummy = 0;
        commit(false);
    }

    @Override
    public String getPacketRules() {
        StringBuilder sb = new StringBuilder();
        sb.append("RA");
        sb.append(separator);
        sb.append(kingBoy);
        sb.append(parametrSeparator);
        sb.append("RB");
        sb.append(separator);
        sb.append(dummy);
        return sb.toString();
    }

    @Override
    public void applyPacketRules(String as) {
        String[] rsa = as.split(parametrSeparator);
        if (rsa.length == 0) {
            return;
        }
        backupRules();
        boolean error = false;
        for (int i = 0; i < rsa.length; i++) {
            String[] rs = rsa[i].split(separator);
            if (rs.length != 2) {
                error = true;
            } else {
                try {
                    switch (rs[0]) {
                        case "RA":
                            kingBoy = Boolean.parseBoolean(rs[1]);
                            break;
                        case "RB":
                            dummy = Integer.parseInt(rs[1]);
                            break;
                    }
                } catch (NumberFormatException ex) {
                    error = true;
                }
            }
            if (error) {
                break;
            }
        }
        if (error) {
            restoreRules();
        }
        commit(false);
    }

    @Override
    public void backupRules() {
        kingBoyBackup = kingBoy;
        dummyBackup = dummy;
    }

    @Override
    public void restoreRules() {
        kingBoy = kingBoyBackup;
        dummy = dummyBackup;
        commit(false);
    }

    public boolean isKingBoy() {
        return kingBoyWork;
    }

    public void setKingBoy(boolean kingBoy) {
        if (this.kingBoy == kingBoy) {
            return;
        }
        this.kingBoy = kingBoy;
        rulesChanged = true;
    }

    public int getDummy() {
        return dummyWork;

    }

    public void setDummy(int dummy) {
        if (this.dummy == dummy) {
            return;
        }
        this.dummy = dummy;
        rulesChanged = true;
    }

    @Override
    public void commit(boolean checkChanges) {
        if (checkChanges && !rulesChanged) {
            return;
        }
        kingBoyWork = kingBoy;
        dummyWork = dummy;
        rulesChanged = false;
    }

}
