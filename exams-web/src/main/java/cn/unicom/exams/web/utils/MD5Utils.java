package cn.unicom.exams.web.utils;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * @author 王长何
 * @create 2020-01-13 16:19
 */
public class MD5Utils {
    public static String getAuthenticationInfo(String pwd,String salt){
        String hashAlgorithmName = "MD5";
        int hashInteractions = 1;
        String result = new SimpleHash(hashAlgorithmName, pwd, ByteSource.Util.bytes(salt), hashInteractions).toHex();
        return result;
    }
}
