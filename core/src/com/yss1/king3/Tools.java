/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yss1.king3;

import com.yss1.lib_jm.ToolsBase;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 *
 * @author ys
 */
public class Tools extends ToolsBase {
    
//    private static Vector3f tr=new Vector3f();
//private static Quaternion rot=new Quaternion();
//private static Vector3f scale = new Vector3f();
//public static Transform getLocalTransformToPreserveWorldTransform(Transform PT, Transform CT) {
//  //располагает child в парент без изменения положения      
//  //PT parent world transform, CT child world transform     
//  scale.set(CT.getScale().divide(PT.getScale()));
//  rot.set(PT.getRotation().inverse().multLocal( CT.getRotation()));
//  tr.set(PT.getRotation().inverse().multLocal( CT.getTranslation().subtract( PT.getTranslation())).divideLocal(PT.getScale()));
//  return new Transform(tr, rot, scale);
//
//}

//   public static Texture makeTexture(String font, String capt, int W, int H)
//   {
//       Texture2D T2D;
//       ByteBuffer buff = ByteBuffer.allocateDirect(W*H*2);
//       BitmapFont bmf;
//       //BitmapText bmt=bmf.createLabel("RRR");
//       //ImagePainter ip;
//       for (int i=0;i<W*H;i++) {buff.put((byte)255);buff.put((byte)240);};
//       Image Im = new Image(Image.Format.ARGB4444,W,H,buff);
//       //Bitmap bitmap = Bitmap.createBitmap(W, H, Bitmap.Config.ARGB_8888); 
//       T2D=new Texture2D(Im);
//       return T2D;
//   }

//   public static Mesh make2planes(float w, float h, float t, Vector2f[] tx, Vector2f[] tx2,VectorsPool VP)
//       {//полуширина полувысота полутолщина текстурные координаты, лайтмэп текстура
//       Vector3f[] vertices = new Vector3f[8];      
//       
//       vertices[0]=VP.getV3(-w,-h, t);
//       vertices[1]=VP.getV3(w, -h,t);
//       vertices[2]=VP.getV3(-w,h, t);
//       vertices[3]=VP.getV3(w,h, t);
//       
//       vertices[4]=VP.getV3(-w,-h, -t);
//       vertices[5]=VP.getV3(w, -h,-t);
//       vertices[6]=VP.getV3(-w, h, -t);
//       vertices[7]=VP.getV3(w,h, -t);
//       
//       Vector3f[] normals = new Vector3f[8];
//       for (int i=0;i<4;i++) normals[i]=VP.getV3(0,0,1);
//       for (int i=4;i<normals.length;i++) normals[i]=VP.getV3(0,0,-1);
//       
//       Mesh Msh = new Mesh();
//       Msh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
//       if (tx!=null) Msh.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(tx));
//       if (tx2!=null) Msh.setBuffer(VertexBuffer.Type.TexCoord2, 2, BufferUtils.createFloatBuffer(tx2));
//       Msh.setBuffer(VertexBuffer.Type.Normal, 3, BufferUtils.createFloatBuffer(normals));
//       Msh.setBuffer(VertexBuffer.Type.Index, 3, BufferUtils.createShortBuffer(Sett.indexes2p));
//       Msh.updateBound();
//       for (int i=0;i<vertices.length;i++) VP.freeV3(vertices[i]);
//       for (int i=0;i<normals.length;i++) VP.freeV3(normals[i]);
//       return Msh;
//    }
//   
//   public static Geometry [] make_framed_geometries(float w, float h, float t, float texH,String name,Main ap)
//       {//полуширина полувысота полутолщина текстурные угол,край, середина
//       Geometry [] gmt=new Geometry[9];
//       Mesh msh;
//       //ap.VPool.getV3(0,0,1);
//       Vector3f[] vertices = new Vector3f[4];      
//       //float tH=texH;
//       //float pxH=ap.getScrH();
//       
//       float pxOnUnitH=ap.getScrH()/Sett.deskHh/2f;
//       
//       float pxOnUnitW=ap.getScrW()/Sett.deskHw/2f;
//       
//       float H0=texH/pxOnUnitH;
//       //float W0=H0;//w*2f*pxOnUnitW/texH;//т.к. текстура квадратная
//       float W0=texH/pxOnUnitW;
//       
//       
//       float xrpt=2f*w/W0-2f;
//       float yrpt=2f*h/H0-2f;
//      // System.out.println("h="+h+ " w="+w+ " H0="+H0+ " W0="+W0+ " yrpt="+yrpt+" xrpt="+xrpt);
//       //левый верхний
//       vertices[0]=ap.VPool.getV3(-w   ,h-H0,t);
//       vertices[1]=ap.VPool.getV3(-w+W0,h-H0,t);
//       vertices[2]=ap.VPool.getV3(-w   ,h   ,t);
//       vertices[3]=ap.VPool.getV3(-w+W0,h   ,t);
//       
//                     
//       Vector3f[] normals = new Vector3f[4];
//       for (int i=0;i<4;i++) normals[i]=ap.VPool.getV3(0,0,1);
//       //for (int i=4;i<normals.length;i++) normals[i]=VP.getV3(0,0,-1);
//       Vector2f[] tex = new Vector2f[4];      
//       tex[0]=ap.VPool.getV2(0, 0);
//       tex[1]=ap.VPool.getV2(1, 0);
//       tex[2]=ap.VPool.getV2(0, 1);
//       tex[3]=ap.VPool.getV2(1, 1);
//       
//       msh = new Mesh();
//       msh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
//       msh.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(tex));
//       msh.setBuffer(VertexBuffer.Type.Normal, 3, BufferUtils.createFloatBuffer(normals));
//       msh.setBuffer(VertexBuffer.Type.Index, 3, BufferUtils.createShortBuffer(Sett.indexes1p));
//       msh.updateBound();
//       gmt[0]=new Geometry(name+"_LU",msh);
//       
//       //верхняя полоса
//       vertices[0].set(-w+W0,h-H0,t);
//       vertices[1].set( w-W0,h-H0,t);
//       vertices[2].set(-w+W0,h,   t);
//       vertices[3].set( w-W0,h,   t);
//       
//       tex[0].set(0, 0);
//       tex[1].set(1, 0);
//       tex[2].set(0, 1);
//       tex[3].set(1, 1);
//       
//       msh = new Mesh();
//       msh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
//       msh.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(tex));
//       msh.setBuffer(VertexBuffer.Type.Normal, 3, BufferUtils.createFloatBuffer(normals));
//       msh.setBuffer(VertexBuffer.Type.Index, 3, BufferUtils.createShortBuffer(Sett.indexes1p));
//       msh.updateBound();
//       msh.scaleTextureCoordinates(new Vector2f(xrpt,1));
//       gmt[1]=new Geometry(name+"_UP",msh);
//       
//       //правый верхний угол
//       vertices[0].set(w-W0,h-H0,t);
//       vertices[1].set(w   ,h-H0,t);
//       vertices[2].set(w-W0,h   ,t);
//       vertices[3].set(w   ,h   ,t);
//       
//       tex[0].set(1,0);
//       tex[1].set(1, 1);
//       tex[2].set(0, 0);
//       tex[3].set(0, 1);
//       
//       msh = new Mesh();
//       msh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
//       msh.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(tex));
//       msh.setBuffer(VertexBuffer.Type.Normal, 3, BufferUtils.createFloatBuffer(normals));
//       msh.setBuffer(VertexBuffer.Type.Index, 3, BufferUtils.createShortBuffer(Sett.indexes1p));
//       msh.updateBound();
//       gmt[2]=new Geometry(name+"_RU",msh);
//       
//       //левая полоса
//       vertices[0].set(-w   ,-h+H0,t);
//       vertices[1].set(-w+W0,-h+H0,t);
//       vertices[2].set(-w   ,h-H0,   t);
//       vertices[3].set(-w+W0,h-H0,   t);
//       
//       tex[0].set(0, 1);
//       tex[1].set(0, 0);
//       tex[2].set(1, 1);
//       tex[3].set(1, 0);
//       
//       msh = new Mesh();
//       msh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
//       msh.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(tex));
//       msh.setBuffer(VertexBuffer.Type.Normal, 3, BufferUtils.createFloatBuffer(normals));
//       msh.setBuffer(VertexBuffer.Type.Index, 3, BufferUtils.createShortBuffer(Sett.indexes1p));
//       msh.updateBound();
//       msh.scaleTextureCoordinates(new Vector2f(yrpt,1));
//       gmt[3]=new Geometry(name+"_LP",msh);
//       
//       //центр
//       vertices[0].set(-w+W0,-h+H0,t);
//       vertices[1].set( w-W0,-h+H0,t);
//       vertices[2].set(-w+W0, h-H0,t);
//       vertices[3].set( w-W0, h-H0,t);
//       
//       tex[0].set(0, 0);
//       tex[1].set(1, 0);
//       tex[2].set(0, 1);
//       tex[3].set(1,1);
//       
//       msh = new Mesh();
//       msh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
//       msh.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(tex));
//       msh.setBuffer(VertexBuffer.Type.Normal, 3, BufferUtils.createFloatBuffer(normals));
//       msh.setBuffer(VertexBuffer.Type.Index, 3, BufferUtils.createShortBuffer(Sett.indexes1p));
//       msh.updateBound();
//       msh.scaleTextureCoordinates(new Vector2f(xrpt,yrpt));
//       gmt[4]=new Geometry(name,msh);
//       
//       //правая полоса
//       vertices[0].set(w-W0,-h+H0,t);
//       vertices[1].set(w   ,-h+H0,t);
//       vertices[2].set(w-W0, h-H0,t);
//       vertices[3].set(w   , h-H0,t);
//       
//       tex[0].set(1, 0);
//       tex[1].set(1, 1);
//       tex[2].set(0, 0);
//       tex[3].set(0, 1);
//       
//       msh = new Mesh();
//       msh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
//       msh.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(tex));
//       msh.setBuffer(VertexBuffer.Type.Normal, 3, BufferUtils.createFloatBuffer(normals));
//       msh.setBuffer(VertexBuffer.Type.Index, 3, BufferUtils.createShortBuffer(Sett.indexes1p));
//       msh.updateBound();
//       msh.scaleTextureCoordinates(new Vector2f(yrpt,1));
//       gmt[5]=new Geometry(name+"_RP",msh);
//       
//       //левый нижний угол
//       vertices[0].set(-w   ,-h   ,t);
//       vertices[1].set(-w+W0,-h   ,t);
//       vertices[2].set(-w   ,-h+H0,t);
//       vertices[3].set(-w+W0,-h+H0,t);
//       
//       tex[0].set(0, 1);
//       tex[1].set(0, 0);
//       tex[2].set(1, 1);
//       tex[3].set(1, 0);
//       
//       msh = new Mesh();
//       msh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
//       msh.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(tex));
//       msh.setBuffer(VertexBuffer.Type.Normal, 3, BufferUtils.createFloatBuffer(normals));
//       msh.setBuffer(VertexBuffer.Type.Index, 3, BufferUtils.createShortBuffer(Sett.indexes1p));
//       msh.updateBound();
//       gmt[6]=new Geometry(name+"_LD",msh);
//       
//       //нижняя полоса
//       vertices[0].set(-w+W0,-h   ,t);
//       vertices[1].set( w-W0,-h   ,t);
//       vertices[2].set(-w+W0,-h+H0,t);
//       vertices[3].set( w-W0,-h+H0,t);
//       
//       tex[0].set(1, 1);
//       tex[1].set(0, 1);
//       tex[2].set(1, 0);
//       tex[3].set(0, 0);
//       
//       msh = new Mesh();
//       msh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
//       msh.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(tex));
//       msh.setBuffer(VertexBuffer.Type.Normal, 3, BufferUtils.createFloatBuffer(normals));
//       msh.setBuffer(VertexBuffer.Type.Index, 3, BufferUtils.createShortBuffer(Sett.indexes1p));
//       msh.updateBound();
//       msh.scaleTextureCoordinates(new Vector2f(xrpt,1));
//       gmt[7]=new Geometry(name+"_DP",msh);
//       
//       //правый верхний угол
//       vertices[0].set(w-W0,-h   ,t);
//       vertices[1].set(w   ,-h   ,t);
//       vertices[2].set(w-W0,-h+H0,t);
//       vertices[3].set(w   ,-h+H0,t);
//       
//       tex[0].set(1, 1);
//       tex[1].set(0, 1);
//       tex[2].set(1, 0);
//       tex[3].set(0, 0);
//       
//       msh = new Mesh();
//       msh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
//       msh.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(tex));
//       msh.setBuffer(VertexBuffer.Type.Normal, 3, BufferUtils.createFloatBuffer(normals));
//       msh.setBuffer(VertexBuffer.Type.Index, 3, BufferUtils.createShortBuffer(Sett.indexes1p));
//       msh.updateBound();
//       gmt[8]=new Geometry(name+"_RD",msh);
//       
//       for (int i=0;i<vertices.length;i++) ap.VPool.freeV3(vertices[i]);
//       for (int i=0;i<normals.length;i++) ap.VPool.freeV3(normals[i]);
//       for (int i=0;i<tex.length;i++) ap.VPool.freeV2(tex[i]);
//       return gmt;
//    }
//   
//     
//    public static Mesh makePlane(float w, float h, Vector2f[] tx, Vector2f[] tx2,VectorsPool VP)
//       {//полуширина полувысота полутолщина текстурные координаты, лайтмэп текстура
//           
//       Vector3f[] vertices = new Vector3f[4];
//       vertices[0]=VP.getV3(-w,-h, 0);
//       vertices[1]=VP.getV3(w, -h,0);
//       vertices[2]=VP.getV3(-w,h, 0);
//       vertices[3]=VP.getV3(w,h, 0);
//
//       Vector3f[] normals = new Vector3f[4];
//       for (int i=0;i<4;i++) normals[i]=VP.getV3(0,0,1);
//       
//       Mesh Msh = new Mesh();
//       Msh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
//       if (tx!=null) Msh.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(tx));
//       if (tx2!=null) Msh.setBuffer(VertexBuffer.Type.TexCoord2, 2, BufferUtils.createFloatBuffer(tx2));
//       Msh.setBuffer(VertexBuffer.Type.Normal, 3, BufferUtils.createFloatBuffer(normals));
//       Msh.setBuffer(VertexBuffer.Type.Index, 3, BufferUtils.createShortBuffer(Sett.indexes1p));
//       Msh.updateBound();
//       
//       for (int i=0;i<vertices.length;i++) VP.freeV3(vertices[i]);
//       for (int i=0;i<normals.length;i++) VP.freeV3(normals[i]);
//       
//       return Msh;
//    }
 
   
   
//    private static Random rnd = new Random((new Date()).getTime());
//    public static float fluctuate(float DELTA)
//    {//дает результат в диапазоне -DELTA..+DELTA
//         return DELTA*(2f*rnd.nextFloat()-1);
//    }
//    
//    public static int rand(int max)
//    {//дает результат в диапазоне [0..max]
//       return rnd.nextInt(max+1);
//    }
//    
//    public static boolean rand()
//    {
//       return rnd.nextBoolean();
//    }
//    
//    public static Transform getTRfromStr(String s)
//    {
//        Transform res=new Transform();
//        ArrayList<Float> fa = new ArrayList<Float>();
//        Pattern p = Pattern.compile("([E\\-\\d\\.]{2,})");
//        Matcher m = p.matcher(s); 
//        try
//        {
//        while (m.find()) fa.add(Float.parseFloat(m.group()));
//        }
//        catch (Exception Exx)
//        {
//            return null;
//        }
//        if (fa.size()!=10) return null;
//        res.setTranslation(fa.get(0), fa.get(1), fa.get(2));
//        res.setRotation(new Quaternion().set(fa.get(3),fa.get(4), fa.get(5), fa.get(6)));
//        res.setScale(fa.get(7), fa.get(8), fa.get(9));
//        return res;
//    }
    
//    public static String tstTRfromStr(String s)
//    {
//        //Transform res=new Transform();
//        ArrayList<Float> fa = new ArrayList<Float>();
//        System.out.println(s);
//        Pattern p = Pattern.compile("([E\\-\\d\\.]{2,})");
//        System.out.println("-------/--------");
//        Matcher m = p.matcher(s); 
//        String res="WWW";
//        System.out.println(res);
//        int k=0;
//        try
//        {
//        while (m.find()) {
//            res=res+"{}"+m.group();k++;
//            System.out.println(res);
//        }
//        }
//        catch (Exception Exx)
//        {
//            
//            return res=res+"ERROR["+m.group()+"]";
//            //System.out.println(res);
//        }
//        if (k<9) res=res+"ERROR[k="+k+"]";
//                    
//            System.out.println("LAST "+res);
//        return res;
//    }
    
    
    
    public static String Tst(String s)
    {
    Pattern p = Pattern.compile("([-\\d\\.]+)");
        Matcher m = p.matcher(s); 
        String res =m.matches()+" s="+s;
                //+" G1="+m.group(1)+" "+s;
        int i=0;
        //m.find();
        while (m.find()) res=res+" G"+(++i)+"="+m.group(1);
        //for (int i=1;i<=m.groupCount();i++) res=res+" G"+(i)+"="+m.group(i);
            
        //if (m.find()) res=res+" G2="+m.group(1);
        //if (m.find()) res=res+" G3="+m.group(1);
        return res;
    }
        
   
    
    public static ArrayList<String> initPlayers()
    {
        ArrayList<String> players = new ArrayList<String>(Sett.playersCount);
        //if (players==null) players=new ArrayList(4);
        //else players.clear();

        switch (Sett.lang)    
            {
            case 1:
                 if (Sett.youName.equals(Sett.youName1)||Sett.youName.equals(Sett.youName2)) Sett.youName=Sett.youName1;
                 players.add(Sett.youName);
                 players.add("Левый");
                 players.add("Верхний");
                 players.add("Правый");
                break;
                case 2:
                 if (Sett.youName.equals(Sett.youName1)||Sett.youName.equals(Sett.youName2)) Sett.youName=Sett.youName2;
                 players.add(Sett.youName);
                 players.add("Left");
                 players.add("Up");
                 players.add("Right");
                break;     
        }
        return players;
    }
    
    public static String getText(int id)
    {
        if (id>401 && id<470) return getTextBase(id);
        if (id>=600 && id<700) return getTextBase(id);
        switch (id)
        {
           case 1://popup window - ok button
            switch (Sett.lang)    
            {
                case 1:
                    return "да";
                case 2:
                    return "ok";
            }
            break;
         case 2://popup window - cancel button
            switch (Sett.lang)    
            {
                case 1:
                    return "нет";
                case 2:
                    return "cancel";
            }
            break;    
         case 3://popup window - next button
            switch (Sett.lang)    
            {
                case 1:
                    return "далее";
                case 2:
                    return "next";
            }
            break;
         case 4://popup window - quit button
            switch (Sett.lang)    
            {
                case 1:
                    return "выход";
                case 2:
                    return "exit";
            }
            break;        
         case 5://party over caption
            switch (Sett.lang)    
            {
                case 1:
                    return "Партия окончена";
                case 2:
                    return "Party over";
            }
            break;        
         case 6://party message
            switch (Sett.lang)    
            {
                case 1:
                    return "Следующая партия: %s";
                case 2:
                    return "Next party is %s";
            }
            break;        
           case 7://game over caption
            switch (Sett.lang)    
            {
                case 1:
                    return "Игра окончена!";
                case 2:
                    return "Game is over!";
            }
            break;        
         case 8://Объявляется победитель и не приводится табличка личных рекордов, т.к. не рекорд
             switch (Sett.lang)    
            {
                case 1:
                    return "%s всех победил, заработв %d очков!";
                case 2:
                    return "%s won! Gain %d points.";
            }
            break;        
           case 9://game not over new game caption
            switch (Sett.lang)    
            {
                case 1:
                    return "Игра не закончена!";
                case 2:
                    return "Game is not over!";
            }
            break;        
           case 10://game not over new game message
            switch (Sett.lang)    
            {
                case 1:
                    return "Начать новую игру?";
                case 2:
                    return "Start new game?";
            }
            break;         
           case 11://game not over new game message
            switch (Sett.lang)    
            {
                case 1:
                    return "Продолжим...";
                case 2:
                    return "Press next to continue...";
            }
            break;         
               case 12://Объявляется победитель и приводится табличка, т.к. это личный рекорд
            switch (Sett.lang)    
            {
                case 1:
                    return "%s всех победил, выиграв %d очков! Твой результат %s очков попал в тройку лучших на %s место!";
                case 2:
                    return "%s won! Gain %d points. Your result %s points won %s place in the top three!";
            }
               break;  
                case 13://game not over new game caption
            switch (Sett.lang)    
            {
                case 1:
                    return "Лучшие игры";
                case 2:
                    return "Personal records";
            }
            break;         
           case 100://main menu - play button
            switch (Sett.lang)    
            {
                case 1:
                    return "Играть";
                case 2:
                    return "Play";
            }
            break;        
            case 101://main menu - setting button
            switch (Sett.lang)    
            {
                case 1:
                    return "Настройки";
                case 2:
                    return "Settings";
            }
            break;        
          case 102://main menu - quit button
            switch (Sett.lang)    
            {
                case 1:
                    return "Выход";
                case 2:
                    return "Quit2";
            }
            break;        
          case 103://main menu caption
            switch (Sett.lang)    
            {
                case 1:
                    return "Девятка";
                case 2:
                    return "Nine";
            }
            break;        
          case 104://main menu - continue button
            switch (Sett.lang)    
            {
                case 1:
                    return "Доиграть";
                case 2:
                    return "Resume";
            }
            break;                      
          case 105://main menu - continue button
            switch (Sett.lang)    
            {
                case 1:
                    return "Помощь";
                case 2:
                    return "Help";
            }
            break;                      
         case 106:
            switch (Sett.lang)    
            {
                case 1:
                    return "Игра по\nсети";
                case 2:
                    return "Multi\nplayer";
            }
            break;                           
         case 107:
            switch (Sett.lang)    
            {
                case 1:
                    return "Быстрый\nстарт";
                case 2:
                    return "Quick\nstart";
            }
            break;                               
         case 108:
            switch (Sett.lang)    
            {
                case 1:
                    return "Создать";
                case 2:
                    return "Create";
            }
            break;                          
         case 109:
            switch (Sett.lang)    
            {
                case 1:
                    return "Пригла-\nшения";
                case 2:
                    return "Invitations";
            }
            break;                              
         case 110:
            switch (Sett.lang)    
            {
                case 1:
                    return "Назад";
                case 2:
                    return "Back";
            }
            break;   
             
         case 150://Leave game title
            switch (Sett.lang)    
            {
                case 1:
                    return "Сетевая игра завершена";
                case 2:
                    return "Network game is over";
            }
            break;   
         case 151://Leave game message
            switch (Sett.lang)    
            {
                case 1:
                    return "Причина: игрок %s покинул игру";
                case 2:
                    return "Reason: player %s left the game";
            }
            break;      
             
         case 300://settings window caption
            switch (Sett.lang)    
            {
                case 1:
                    return "Настройки";
                case 2:
                    return "Settings";
            }
            break;        
          case 301://settings rubaha selector block text
            switch (Sett.lang)    
            {
                case 1:
                    return "Рубашка";
                case 2:
                    return "Cards back";
            }
            break;        
          case 302://name label caption
            switch (Sett.lang)    
            {
                case 1:
                    return "Мое имя:";
                case 2:
                    return "My name:";
            }
            break;        
          case 303://settings apply button caption
            switch (Sett.lang)    
            {
                case 1:
                    return "Применить";
                case 2:
                    return "Apply";
            }
            break;      
          case 304://settings cancel button caption
            switch (Sett.lang)    
            {
                case 1:
                    return "Отмена";
                case 2:
                    return "Cancel";
            }
            break;          
              
           case 305://show move switch
            switch (Sett.lang)    
            {
                case 1:
                    return "Король тоже мальчик";
                case 2:
                    return "King is boy";
            }
            break;          
           case 306://scren on switch
            switch (Sett.lang)    
            {
                case 1:
                    return "Экран всегда включен";
                case 2:
                    return "Screen always on";
            }
            break;   
           case 307://scren on switch
            switch (Sett.lang)    
            {
                case 1:
                    return "Внимвние!";
                case 2:
                    return "Warning!";
            }
            break;   
           case 308://scren on switch
            switch (Sett.lang)    
            {
                case 1:
                    return "Изменения вступят в силу с началом следующей партии.";
                case 2:
                    return "The changes will take effect at the beginning of the next batch.";
            }
            break;   
               
               case 309://scren on switch
            switch (Sett.lang)    
            {
                case 1:
                    return "Убирать взятку через 1сек.";
                case 2:
                    return "AutoClear trick after 1 sec.";
            }
            break;   
                   
          case 400:
            switch (Sett.lang)    
            {
                case 1:
                    return "Пас";
                case 2:
                    return "Pass";
            }
            break;            
          
                
                
            case 479:
            switch (Sett.lang)    
            {
                case 1:
                    return "Игра не начата";
                case 2:
                    return "Out of Game";
            }
            break;                      
                    
            case 480:
            switch (Sett.lang)    
            {
                case 1:
                    return "Не брать взятки";
                case 2:
                    return "Don`t take tricks";
            }
            break;                      
            
            case 481:
            switch (Sett.lang)    
            {
                case 1:
                    return "Не брать червей";
                case 2:
                    return "Don`t take hearts";
            }
            break;                      
                
            case 482:
            switch (Sett.lang)    
            {
                case 1:
                    return "Не брать мальчиков";
                case 2:
                    return "Don`t take boys";
            }
            break;                          
            case 483:
            switch (Sett.lang)    
            {
                case 1:
                    return "Не брать девочек";
                case 2:
                    return "Don`t take girls";
            }
            break;  
                
            case 484:
            switch (Sett.lang)    
            {
                case 1:
                    return "Не брать 2 последние";
                case 2:
                    return "Don`t take last two";
            }
            break;                          
            
            case 485:
            switch (Sett.lang)    
            {
                case 1:
                    return "Не брать Кинга";
                case 2:
                    return "Don`t take King";
            }
            break;                          
           case 486:
            switch (Sett.lang)    
            {
                case 1:
                    return "Не брать все подряд";
                case 2:
                    return "Don`t take everything";
            }
            break;                          
            
            case 487:
            switch (Sett.lang)    
            {
                case 1:
                    return "Брать взятки";
                case 2:
                    return "Take tricks";
            }
            break;                      
            
            case 488:
            switch (Sett.lang)    
            {
                case 1:
                    return "Брать червей";
                case 2:
                    return "Take hearts";
            }
            break;                      
                
            case 489:
            switch (Sett.lang)    
            {
                case 1:
                    return "Брать мальчиков";
                case 2:
                    return "Take boys";
            }
            break;                          
            case 490:
            switch (Sett.lang)    
            {
                case 1:
                    return "Брать девочек";
                case 2:
                    return "Take girls";
            }
            break;  
                
            case 491:
            switch (Sett.lang)    
            {
                case 1:
                    return "Брать 2 последние";
                case 2:
                    return "Take last two";
            }
            break;                          
            
            case 492:
            switch (Sett.lang)    
            {
                case 1:
                    return "Брать Кинга";
                case 2:
                    return "Take King";
            }
            break;                          
           case 493:
            switch (Sett.lang)    
            {
                case 1:
                    return "Брать все подряд";
                case 2:
                    return "Take everything";
            }
            break;                          
                  
               
            case 500://контектсное меню игрового окна
            switch (Sett.lang)    
            {
                case 1:
                    return "Новая";
                case 2:
                    return "New";
            }
            break;                      
            case 509:
            switch (Sett.lang)    
            {
                case 1:
                    return "Помощь";
                case 2:
                    return "Help";
            }
            break;                 
            case 501:
            switch (Sett.lang)    
            {
                case 1:
                    return "Реклама";
                case 2:
                    return "Adver...";
            }
            break;                 
            case 502:
            switch (Sett.lang)    
            {
                case 1:
                    return "Оценить";
                case 2:
                    return "Rate";
            }
            break;  
                
            case 503:
            switch (Sett.lang)    
            {
                case 1:
                    return "Мои\nлучшие";
                case 2:
                    return "My\nBest";
            }
            break;  
                
                case 504:
            switch (Sett.lang)    
            {
                case 1:
                    return ">>>";
                case 2:
                    return ">>>";
            }
            break;             
                    
            case 505:
            switch (Sett.lang)    
            {
                case 1:
                    return "Выход";
                case 2:
                    return "Exit";
            }
            break;             
            
            
                
               
           
           case 700:
            switch (Sett.lang)    
            {
                case 1:
                    return "Помощь %d из %d";
                case 2:
                    return "Help %d of %d";
            }
            break;                 
               
           case 701:
            switch (Sett.lang)    
            {
                case 1:
                    return "Ближе";
                case 2:
                    return "Prev.";
            }
            break;              
           case 702:
            switch (Sett.lang)    
            {
                case 1:
                    return "Дальше";
                case 2:
                    return "Next";
            }
            break;              
            
            case 703:
            switch (Sett.lang)    
            {
                case 1:
                    return "Хватит";
                case 2:
                    return "Close";
            }
            break;   
            case 710:
            switch (Sett.lang)    
            {
                case 1:
                    return "Играют четверо. Игра состоит из 14 партий. В начале каждой партии колода из 32 карт (от 7 до туза) раздается игрокам (по 8 карт каждому).";
                case 2:
                    return "Play 4 players. The game consists of 14 parties. The deck of 32 cards (from 7 to Ace) is distributed to the players. 8 cards each.";
            }
            break;              
                
            case 711:
            switch (Sett.lang)    
            {
                case 1:
                    return "Первый ход в игре назначается случайно. Первый ход в партии передается по часовой стрелке. В процессе игры первым заходит тот, кто взял взятку.";
                case 2:
                    return "The first goes to the one who win a trick. The first move in the party is passed clockwise. The first move in the game is randomly assigned.";
            }
            break;              
                   
           case 712:
            switch (Sett.lang)    
            {
                case 1:
                    return "Заходить червями в партиях (брать/не брать) ЧЕРВЕЙ, КИНГА, ВСЕ ПОДРЯД нельзя пока есть другие масти.";
                case 2:
                    return "Make the 1st move on hearts in batches (to take/not take) HEARTS, KING, EVERYTHING is impossible until there are other suit.";
            }
            break;              
               
           case 713:
            switch (Sett.lang)    
            {
                case 1:
                    return "Далее, по часовой стрелке, игроки должны положить карту масти, в которую был заход. Если этой масти нет - ложится любая карта.";
                case 2:
                    return "Other must put a card of the suit, which went the first player. If such suit is missing then the player can put any card.";
            }
            break;  
               
         case 714:
            switch (Sett.lang)    
            {
                case 1:
                    return "Взятку берет игрок, положивший старшую карту в масти заходящего.";
                case 2:
                    return "Taking trick a player, who put the highest card in the suit played at the start of the trick.";
            }
            break;        
         case 715:
            switch (Sett.lang)    
            {
                case 1:
                    return "Взятка пересчитывается в очки в зависимости от типа партии. Эти очки начисляются взявшему взятку.";
                case 2:
                    return "Cards in trick converted into points depending on the type of party. These points are awarded tricks winner.";
            }
            break;        
        case 716:
            switch (Sett.lang)    
            {
                case 1:
                    return "Партия играется до тех пор пока не выбраны все значащие карты. Например, все Дамы в партии НЕ БРАТЬ ДЕВОЧЕК.";
                case 2:
                    return "The party plays until ran out of all meaningful card. For example all the Queens in the party DONT TAKE GIRLS.";
            }
            break;     
        case 717:
            switch (Sett.lang)    
            {
                case 1:
                    return "В семи из 14 партий надо что-то брать и в семи, соответственно, НЕ брать. Партии играются в следующей последовательности:";
                case 2:
                    return "In 7 of 14 parties need something to take and in 7, respectively, NOT to take. The parties are played in the following order:";
            }
            break;     
        case 718:
            switch (Sett.lang)    
            {
                case 1:
                    return "1-НЕ БРАТЬ ВЗЯТКИ - нужно взять как можно меньше взяток. За каждую взятку минус 2 очка.";
                case 2:
                    return "1-NO TRICKS - you need to take as few tricks as possible. For every trick minus 2 points.";
            }
            break;     
        
        case 719:
            switch (Sett.lang)    
            {
                case 1:
                    return "2-НЕ БРАТЬ ЧЕРВЕЙ - нужно взять как можно меньше взяток с червями внутри. За каждую черву минус 2 очка.";
                case 2:
                    return "2-NO HEARTS - you need to take as few tricks contains HEARTS as possible. For every hearts card minus 2 points.";
            }
            break;     
        case 720:
            switch (Sett.lang)    
            {
                case 1:
                    return "3-НЕ БРАТЬ МАЛЬЧИКОВ - нужно взять как можно меньше вальтов. За каждого мальчика минус 4 очка.";
                case 2:
                    return "3-NO BOYS dont take JACKS. For every BOY minus 4 points.";
            }
            break;     
       case 721:
            switch (Sett.lang)    
            {
                case 1:
                    return "Короля в настройках можно тоже сделать мальчиком. Тогда за каждого мальчика по минус 2 очка.";
                case 2:
                    return "King in the settings you can also make a boy. Then, for each BOY minus 2 points.";
            }
            break;              
    case 722:
            switch (Sett.lang)    
            {
                case 1:
                    return "4-НЕ БРАТЬ ДЕВОЧЕК - нужно взять как можно меньше дам. За каждую девочку минус 4 очка.";
                case 2:
                    return "4-NO GIRLS - Do not take Queens. Minus 4 points for each GIRL.";
            }
            break;     
     case 723:
            switch (Sett.lang)    
            {
                case 1:
                    return "5-НЕ БРАТЬ 2 ПОСЛЕДНИЕ - постараться не ухватить 2 последние (7-ю и 8-ю) взятки в партии. За каждую последнюю минус 8 очков.";
                case 2:
                    return "5-NO LAST TWO - dont take 2 last tricks in party. For every last minus 8 points.";
            }
            break;     
        case 724:
            switch (Sett.lang)    
            {
                case 1:
                    return "6-НЕ БРАТЬ КИНГА - не взять короля червей. За КИНГА минус 16 очков.";
                case 2:
                    return "6-NO KING - do not take King of Hearts. Minus 16 points for this card.";
            }
            break;     
        case 725:
            switch (Sett.lang)    
            {
                case 1:
                    return "7-НЕ БРАТЬ ВСЕ ПОДРЯД - все, что \"ценится\" в партиях с 1 по 6 имеет значение. На кону минус 96 очков.";
                case 2:
                    return "7-NO EVERYTHING - all that is significant in batches from 1 to 6 counts. At the pot minus 96 points.";
            }
            break;     
        case 726:
            switch (Sett.lang)    
            {
                case 1:
                    return "Следующие 7 партий оцениваются так же, как и первые 7 только со знаком плюс, поэтому, значащие карты желательно брать.";
                case 2:
                    return "Next 7 parties are estimated as well as the first 7 only with a plus sign so meaningful cards is desirable to take.";
            }
            break;     
         case 727:
            switch (Sett.lang)    
            {
                case 1:
                    return "В конце игры победил тот, у кого больше всего очков.";
                case 2:
                    return "At the end of the game won whoever has the most points.";
            }
            break;      
         case 728:
            switch (Sett.lang)    
            {
                case 1:
                    return "Сетевая игра. Для игры по сети нужно быть зарегистрированным в Google+";
                case 2:
                    return "Multiplayer. For online play you need to be registered in Google+";
            }
            break;          
         case 729:
            switch (Sett.lang)    
            {
                case 1:
                    return "Сетевую игру можно создать 'Игра по сети'->'Создать'. Откроется окно, где можно пригласить троих партнеров как знакомых (найдя их в списке), так и не знакомых (нажав 'автовабор').";
                case 2:
                    return "A network game can be created 'Multiplayer'->'Create'. A window will open where you can invite three partners as friends (finding them in the list) or unknown (by clicking 'avtomatch').";
            }
            break;    
         case 730:
            switch (Sett.lang)    
            {
                case 1:
                    return "Если вас пригласили сыграть по сети, и вы согласны - нажимайте 'Принять' и жите начала игры. Она начнется, когда соберутся все четыре партнера.";
                case 2:
                    return "If you are invited to play on the network and you agree, you may press 'Accept' and keep the start of the game. It will start when connected to all four partners.";
            }
            break;      
          case 731:
            switch (Sett.lang)    
            {
                case 1:
                    return "Если приглашения по каким то причинам не появляются или были пропущены, увидеть все приглашения можно нажав 'Игра по сети' -> 'Приглашения'";
                case 2:
                    return "If the invitation is for some reason not visible or omitted, to see all the invitations addressed to you, press 'network Game' -> 'Invitations'";
            }
            break;         
        case 732:
            switch (Sett.lang)    
            {
                case 1:
                    return "Быстро начать игру с незнакомыми партнерами, анонимно, можно нажав 'Игра по сети' -> 'Быстрый старт'. Садитесь за первый же свободный стол.";
                case 2:
                    return "You can quickly start playing with unfamiliar partners, anonymously, clicking 'Multiplayer' -> 'Quick start'. In this you Sit at the first available table.";
            }
            break;         
        case 733:
            switch (Sett.lang)    
            {
                case 1:
                    return "Игра ведется по правилам, которые выставлены у игрока, создавшего игру. Во время сетевой игры менять правила нельзя.";
                case 2:
                    return "The game is played according to the rules that are set up from the player who created the game. During a network game to change the rules is impossible.";
            }
            break;             
    }
         return "";
}
    
    

//    
//    public static int getSuit(String nm)
//    {
//        if (nm==null || nm.length()>3 || nm.length()<2) return 0;
//        switch (nm.toUpperCase().charAt(nm.length()-1))
//        {
//           case 'H':
//                return 4;
//           case 'D':
//                return 3;
//           case 'C':
//                return 2;
//           case 'S':
//                return 1;
//        }
//     return 0;   
//    }
//    
//    public static int getRank(String nm)
//    {
//        if (nm==null || nm.length()>3 || nm.length()<2) return 0;
//        String s1=nm.toUpperCase().substring(0,nm.length()-1);
//        int i=0;
//        try
//        {
//               i =Integer.parseInt(s1);
//               return i;
//        }
//        catch(NumberFormatException Ex)
//        {
//        switch (s1.charAt(0))
//        {
//           case 'J':
//                return 11;
//           case 'Q':
//                return 12;
//           case 'K':
//                return 13;
//           case 'A':
//                return 14;
//           case 'R'://jokeR бывают мастей С(черный) и D(красный)
//                return 15;
//        }    
//        }
//     return 0;   
//    }
    
    
}
