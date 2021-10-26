package com.yss1.king3;

import com.yss1.lib_jm.NetPacket;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void netpacked_isCorrect() {
        NetPacket np= new NetPacket();
        //np.set_send_to(NetPacket.AddressType.ALLNOTME);
        np.set_receiver(NetPacket.AddressType.ALLNOTME);
        np.set_sender(NetPacket.AddressType.DOWN_USER);
        //np.setContentType(NetPacket.PType.IM_ALIVE);
        np.setContent("HUUU");
        //np.prepare2sendPacket().get("RECPT")
        assertEquals((String)np.prepare2sendPacket().get("RCPT"), "A");
    }
}