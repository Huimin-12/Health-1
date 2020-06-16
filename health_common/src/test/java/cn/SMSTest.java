package cn;

import cn.utils.SMSUtils;
import com.aliyuncs.exceptions.ClientException;

public class SMSTest {
    public static void main(String[] args) {
        try {
            SMSUtils.sendShortMessage("SMS_193242694","17773374971","966051");
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}
