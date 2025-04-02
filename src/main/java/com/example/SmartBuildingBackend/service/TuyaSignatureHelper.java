package com.example.SmartBuildingBackend.service;

public interface TuyaSignatureHelper {
    String generateNonce();

    String sha256(String data);

    String generateSignatureWithAccessToken(String clientId, String accessToken, String secret,
            long timestamp, String nonce, String method,
            String body, String url);

    String generateSignature(String clientId, String secret, long timestamp, String nonce, String method,
            String body, String url);
}
