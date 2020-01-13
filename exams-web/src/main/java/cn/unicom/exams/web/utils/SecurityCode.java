package cn.unicom.exams.web.utils;

import java.util.Arrays;

public class SecurityCode {
	private enum SecurityCodeLevel {
		Simple, Medium, Hard
	};

	/**
	 * 
	 * @param length
	 *            验证码长度默认为4个长度 。
	 * @param codeLevel
	 *            验证码难易等级,如果验证码为Simple：则取数字部分，如为Medium，取数字与小写字母部分，如为Hard则取数字、小写、大写字母。
	 * @param isCanRepeat
	 *            验证码是否可以重复
	 * @return
	 */
	public static String getSecurityCode(int length, SecurityCodeLevel codeLevel, boolean isCanRepeat) {
		char[] securityCodes = { //'1', '2', '3', '4', '5', '6', '7', '8', '9',//
				'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',//
				'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };//
		int len = length;
		StringBuilder sbCode = new StringBuilder();
		if (len > securityCodes.length && isCanRepeat) {
			throw new RuntimeException(String.format("调用SecurityCode.getSecurityCode(%1$s,%2$s,%3$s)出现异常，当isCanRepeat为%3$s时，传入参数length的值%1$s不能大于%4$s",//
					len, codeLevel, isCanRepeat, securityCodes.length));
		}
		if (codeLevel == SecurityCodeLevel.Simple) {
			securityCodes = Arrays.copyOfRange(securityCodes, 0, 9);
		} else if (codeLevel == SecurityCodeLevel.Medium) {
			securityCodes = Arrays.copyOfRange(securityCodes, 0, 33);
			System.out.println(securityCodes[33]);
		}
		int codeLength = securityCodes.length;
		if (isCanRepeat) {
			for (int i = 0; i < len; i++) {
				int index = (int) (Math.random() * codeLength);
				sbCode.append(securityCodes[index]);
			}
		} else {
			for (int i = 0; i < len; i++) {
				int index = (int) (Math.random() * codeLength);
				sbCode.append(securityCodes[index]);
				securityCodes[index]=securityCodes[codeLength-1];
				codeLength--;
				
			}
		}

		return sbCode.toString();
	}
	/**
	 * 返回默认验证码，长度为6个字符。
	 * @return
	 */
	public static String getSecurityCode(){
		return getSecurityCode(6,SecurityCodeLevel.Simple,true);
	}

}
