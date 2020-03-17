package cn.unicom.exams.web.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class EncryptUtils {
    
    /**
     * 加密
     *
     * @param content 需要加密的内容
     * @param key 加密密码 ,需要16位
     * @param md5Key 是否对key进行md5加密
     * @param iv 加密向量
     * @return 加密后的字节数据
     */
    public static String aesEncrypt(String scontent, String skey, boolean md5Key, String siv) {
        try {
         byte[] key=skey.getBytes();
         byte[] iv=siv.getBytes();
         
         String base64data = scontent;
         byte[] content=base64data.getBytes();
         //System.out.println("data="+scontent+"    key="+skey);
         //System.out.println("base64data="+base64data);
         
            if (md5Key) {
                MessageDigest md = MessageDigest.getInstance("MD5");
                key = md.digest(key);
            }
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/ISO10126Padding"); //"算法/模式/补码方式"
            //Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ivps = new IvParameterSpec(iv);//使用CBC模式，需要一个向量iv，可增加加密算法的强度
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivps);
            
            byte[] byteRresult = cipher.doFinal(content);
            
            StringBuffer sb = new StringBuffer();
    
           for (int i = 0; i < byteRresult.length; i++) {
            String hex = Integer.toHexString(byteRresult[i] & 0xFF);
            if (hex.length() == 1) {
             hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
           }
           return sb.toString();
   
        } catch (Exception ex) {
            //logger.error(ex.getLocalizedMessage());
             ex.getLocalizedMessage();
             ex.printStackTrace();
        }
        return null;
    }
 
     
    public static String aesDecrypt(String scontent,String skey, boolean md5Key, String siv) {
         if (scontent.length() < 1) return null;
          byte[] byteRresult = new byte[scontent.length() / 2];
          for (int i = 0; i < scontent.length() / 2; i++) {
           int high = Integer.parseInt(scontent.substring(i * 2, i * 2 + 1), 16);
           int low = Integer.parseInt(scontent.substring(i * 2 + 1, i * 2 + 2), 16);
           byteRresult[i] = (byte) (high * 16 + low);
          }
        try {
         byte[] key=skey.getBytes();
         byte[] iv=siv.getBytes();
            if (md5Key) {
                MessageDigest md = MessageDigest.getInstance("MD5");
                key = md.digest(key);
            }
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/ISO10126Padding"); //"算法/模式/补码方式"
            //Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ivps = new IvParameterSpec(iv);//使用CBC模式，需要一个向量iv，可增加加密算法的强度
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivps);
            
            byte[] result = cipher.doFinal(byteRresult);
            
            return new String(result);
            
        } catch (Exception ex) {
            //logger.error(ex.getLocalizedMessage());
             ex.getLocalizedMessage();
             ex.printStackTrace();
        }
        return null;
    }
    
}