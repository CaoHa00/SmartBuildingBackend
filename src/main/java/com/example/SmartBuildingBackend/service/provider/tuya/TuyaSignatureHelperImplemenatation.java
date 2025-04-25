package com.example.SmartBuildingBackend.service.provider.tuya;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.stereotype.Service;

@Service
public class TuyaSignatureHelperImplemenatation implements TuyaSignatureHelper {

    @Override
    public String generateNonce() {

        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder nonce = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            int randomIndex = (int) (Math.random() * chars.length());
            nonce.append(chars.charAt(randomIndex));
        }
        return nonce.toString();
    }

    @Override
    public String sha256(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error generating SHA-256 hash", e);
        }
    }

    @Override
    public String generateSignatureWithAccessToken(String clientId, String accessToken, String secret, long timestamp,
            String nonce, String method, String body, String url) {

        String contentSha256 = sha256(body);
        String stringToSign = method.toUpperCase() + "\n" + contentSha256 + "\n" + "\n" + url;
        String signData = clientId + accessToken + timestamp + nonce + stringToSign;

        return new HmacUtils("HmacSHA256", secret).hmacHex(signData).toUpperCase();
    }

    @Override
    public String generateSignature(String clientId, String secret, long timestamp, String nonce, String method,
            String body, String url) {
        String contentSha256 = sha256(body);
        String stringToSign = method.toUpperCase() + "\n" + contentSha256 + "\n" + "\n" + url;
        String signData = clientId + timestamp + nonce + stringToSign;

        return new HmacUtils("HmacSHA256", secret).hmacHex(signData).toUpperCase();
    }

}
