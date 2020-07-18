layui.config({
	base : "js/"
}).extend({
	'cryptoJs': 'crypto-js'
})
layui.define(['layer','cryptoJs'],function(exports){
    var cryptojs=layui.cryptoJs;
    //console.log(cryptojs);
	encrypt={

		/**
		 * 加密数据
		 * @param {type} data 待加密的字符串
		 * @param {type} keyStr 秘钥
		 * @param {type} ivStr 向量
		 * @returns {unresolved} 加密后的数据
		 */
	  aesEncryptJava : function(data, keyStr, ivStr) {
			var sendData = CryptoJS.enc.Utf8.parse(data);
			var key = CryptoJS.enc.Utf8.parse(keyStr);
			var iv  = CryptoJS.enc.Utf8.parse(ivStr);
			var encrypted = CryptoJS.AES.encrypt(sendData, key,{iv:iv,mode:CryptoJS.mode.CBC,padding:CryptoJS.pad.Iso10126});

			var encryptedStr = encrypted.ciphertext.toString();
			// 输出：'44971e715853a821c79e589bcd3ca9cee0ef1bc923582fa8b7c26ec5655d2e06'
			return encryptedStr;
		},
	  aesEncrypt : function(data, keyStr, ivStr) {
		var sendData = CryptoJS.enc.Utf8.parse(data);
		var key = CryptoJS.enc.Utf8.parse(keyStr);
		var iv  = CryptoJS.enc.Utf8.parse(ivStr);
		var encrypted = CryptoJS.AES.encrypt(sendData, key,{iv:iv,mode:CryptoJS.mode.CBC,padding:CryptoJS.pad.Iso10126});

		var encryptedBase64Str = encrypted.toString();
		// 输出：'RJcecVhTqCHHnlibzTypzuDvG8kjWC+ot8JuxWVdLgY='
		return encryptedBase64Str;
	},
	/**
	 *
	 * @param {type} data BASE64的数据
	 * @param {type} key 解密秘钥
	 * @param {type} iv 向量
	 * @returns {undefined}
	 */
	  aesDecrypt : function(data, keyStr, ivStr) {
			var key = CryptoJS.enc.Utf8.parse(keyStr);
			var iv  = CryptoJS.enc.Utf8.parse(ivStr);
			//解密的是基于BASE64的数据，此处data是BASE64数据
			var decrypted = CryptoJS.AES.decrypt(data, key, {iv: iv, mode: CryptoJS.mode.CBC, padding: CryptoJS.pad.Iso10126});
			return decrypted.toString(CryptoJS.enc.Utf8);
		}
	}


	exports("encrypt",encrypt);
})