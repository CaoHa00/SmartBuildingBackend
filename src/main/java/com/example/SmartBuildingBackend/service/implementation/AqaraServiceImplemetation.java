package com.example.SmartBuildingBackend.service.implementation;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.SmartBuildingBackend.entity.AqaraConfig;
import com.example.SmartBuildingBackend.repository.AqaraConfigRepository;

import com.example.SmartBuildingBackend.dto.EquipmentDto;
import com.example.SmartBuildingBackend.dto.LogValueDto;

import com.example.SmartBuildingBackend.service.AqaraService;
import com.example.SmartBuildingBackend.service.EquipmentService;
import com.example.SmartBuildingBackend.service.LogValueService;
import com.example.SmartBuildingBackend.service.ValueService;
import com.example.SmartBuildingBackend.service.WeatherService;
import com.example.SmartBuildingBackend.utils.CreateSign;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.gson.JsonObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import lombok.AllArgsConstructor;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

@Service
@AllArgsConstructor
public class AqaraServiceImplemetation implements AqaraService {
    private final RestTemplate restTemplate = new RestTemplate();

    private final AqaraConfigRepository aqaraConfigRepository;
    private final Gmail gmailService;

    private LogValueService logValueService;
    private final ValueService valueService;
    private static int DEFAULT_TEMPERATURE = 0;
    private final WeatherService weatherService;
    private EquipmentService equipmentService;

    @Override
    public String sendRequestToAqara(Map<String, Object> requestBody) throws Exception {
        return sendAqaraRequest(requestBody);
    }

    @Override
    public String queryDetailsAttributes(Map<String, Object> requestBody, String model, String resourceId)
            throws Exception {
        requestBody.put("intent", "query.resource.info");
        Map<String, Object> data = new HashMap<>();
        data.put("model", model);

        if (resourceId != null) {
            data.put("resourceId", resourceId);
        }

        requestBody.put("data", data);

        // Send the request
        return sendAqaraRequest(requestBody);
    }

    private String sendAqaraRequest(Map<String, Object> requestBody) throws Exception {
        AqaraConfig aqaraConfig = aqaraConfigRepository.findFirstByOrderByAqaraConfigIdDesc() // Get the latest config
                .orElseThrow(() -> new RuntimeException("No Aqara configuration found in the database"));

        String nonce = UUID.randomUUID().toString().replace("-", "");
        String time = String.valueOf(System.currentTimeMillis());
        // Generate Sign
        String sign = CreateSign.createSign(
                aqaraConfig.getAccessToken(),
                aqaraConfig.getAppId(),
                aqaraConfig.getKeyId(),
                nonce, time,
                aqaraConfig.getAppKey());
        // Create Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Accesstoken", aqaraConfig.getAccessToken().trim());
        headers.set("Appid", aqaraConfig.getAppId().trim());
        headers.set("Keyid", aqaraConfig.getKeyId().trim());
        headers.set("Nonce", nonce.trim());
        headers.set("Time", time.trim());
        headers.set("Sign", sign);
        headers.set("Lang", "en");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        // Send Request
        ResponseEntity<String> response = restTemplate.exchange(aqaraConfig.getUrl(), HttpMethod.POST, entity,
                String.class);
        return response.getBody();
    }

    @Override
    public String queryTemparatureAttributes(Long equipmentId)
            throws Exception {
        EquipmentDto equipmentDto = equipmentService.getEquipmentById(equipmentId);
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("intent", "query.resource.value");

        // Create a single resource entry
        Map<String, Object> resource = new HashMap<>();
        resource.put("subjectId", equipmentDto.getDeviceId());

        // Add resourceIds as a List
        List<String> resourceIds = new ArrayList<>();
        resourceIds.add("0.1.85");
        resourceIds.add("0.2.85");
        resource.put("resourceIds", resourceIds);

        // Wrap resource in a List
        List<Map<String, Object>> resources = Collections.singletonList(resource);

        // Create data object and add resources list
        Map<String, Object> data = new HashMap<>();
        data.put("resources", resources);

        // Add data to requestBody
        requestBody.put("data", data);

        // Send the request
        return sendAqaraRequest(requestBody);
    }

    @Override
    public String convertToJson(Map<String, Object> request) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);
        } catch (Exception e) {
            return "Error converting to JSON: " + e.getMessage();
        }
    }

    @Override
    public String authorizationVerificationCode() throws Exception {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("intent", "config.auth.getAuthCode");
        Map<String, Object> data = new HashMap<>();
        data.put("account", "cao.ha@eiu.edu.vn");
        data.put("accountType", 0);
        data.put("accessTokenValidity", "1y");
        requestBody.put("data", data);
        return sendAqaraRequest(requestBody);
    }

    /**
     * Fetches the latest email from Aqara and extracts the verification code.
     * Lấy email mới nhất từ Aqara và trích xuất mã xác minh.
     * 
     * @return The verification code if found, otherwise null.
     */
    public String getVerificationCode() {
        try {
            ListMessagesResponse response = gmailService.users().messages()
                    .list("cao.ha@eiu.edu.vn")
                    .setQ("from:uc-system@sessystem.aqara.com")
                    .setMaxResults(1L)
                    .execute();

            List<Message> messages = response.getMessages();
            if (messages == null || messages.isEmpty()) {
                System.out.println("No emails found from Aqara.");
                return null;
            }

            for (Message msg : messages) {
                Message fullMessage = gmailService.users().messages()
                        .get("cao.ha@eiu.edu.vn", msg.getId()).setFormat("full").execute();

                String emailBody = extractEmailBody(fullMessage.getPayload());
                if (emailBody != null && !emailBody.isEmpty()) {
                    System.out.println("\n--- Extracted Email Body ---\n" + emailBody);

                    // Extract verification code (6-digit number)
                    Pattern pattern = Pattern.compile("\\b\\d{6}\\b");
                    Matcher matcher = pattern.matcher(emailBody);

                    if (matcher.find()) {
                        System.out.println("Found Verification Code: " + matcher.group());
                        return matcher.group();
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error fetching emails: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lấy nội dung email từ phần tin nhắn.
     * 
     * @param payload
     * @return
     */
    private String extractEmailBody(MessagePart payload) {
        StringBuilder emailBody = new StringBuilder();

        if (payload.getParts() != null) {
            for (MessagePart part : payload.getParts()) {
                String mimeType = part.getMimeType();
                if ("text/plain".equalsIgnoreCase(mimeType) || "text/html".equalsIgnoreCase(mimeType)) {
                    String content = decodeBase64(part.getBody().getData());
                    if ("text/html".equalsIgnoreCase(mimeType)) {
                        content = htmlToText(content); // Convert HTML to readable text
                    }
                    emailBody.append(content).append("\n");
                } else if (part.getParts() != null) {
                    emailBody.append(extractEmailBody(part));
                }
            }
        } else {
            emailBody.append(decodeBase64(payload.getBody().getData()));
        }

        return emailBody.toString().trim();
    }

    /**
     * Chuyển đổi nội dung HTML thành văn bản đọc được.
     * 
     * @param htmlContent
     * @return
     */
    private String htmlToText(String htmlContent) {
        Document doc = Jsoup.parse(htmlContent);
        return doc.text(); // Extract visible text
    }

    /**
     * Giải mã chuỗi Base64.
     * 
     * @param encodedText
     * @return
     */
    private String decodeBase64(String encodedText) {
        if (encodedText == null)
            return "";
        byte[] decodedBytes = Base64.getUrlDecoder().decode(encodedText);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }

    @Override
    public String createVirtualAccount() throws Exception {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("intent", "config.auth.createAccount");
        Map<String, Object> data = new HashMap<>();
        data.put("accountId", "752620960511351175824423735297");
        requestBody.put("data", data);
        return sendAqaraRequest(requestBody);
    }

    @Override
    public String ObtainAccessToken() throws Exception {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("intent", "config.auth.getToken");
        String code = getVerificationCode();
        System.out.println("code: " + code);
        Map<String, Object> data = new HashMap<>();
        data.put("authCode", code);
        data.put("account", "cao.ha@eiu.edu.vn");
        requestBody.put("data", data);
        return sendAqaraRequest(requestBody);
    }

    @Override
    public String refreshToken() throws Exception {
        AqaraConfig aqaraConfig = aqaraConfigRepository.findFirstByOrderByAqaraConfigIdDesc() // Get the latest config
                .orElseThrow(() -> new RuntimeException("No Aqara configuration found in the database"));
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("intent", "config.auth.refreshToken");

        Map<String, Object> data = new HashMap<>();
        data.put("refreshToken", aqaraConfig.getRefreshToken());
        requestBody.put("data", data);
        return sendAqaraRequest(requestBody);
    }

    // method to process API response from CHINA
    @Override
    public ObjectNode getJsonAPIFromServer(String response, Long equipmentId) {
        EquipmentDto equipmentDto = equipmentService.getEquipmentById(equipmentId);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode result = objectMapper.createObjectNode();
        try {
            JsonNode rootNode = objectMapper.readTree(response);
            ArrayNode resultArray = (ArrayNode) rootNode.get("result");

            LogValueDto logValueDto = new LogValueDto();
            for (JsonNode node : resultArray) {
                JsonNode valueNode = node.get("value");
                String resourceId = node.get("resourceId").asText();
                String timeStamp = node.get("timeStamp").asText();

                // Extract temperature value if the resourceId matches
                if (valueNode != null && resourceId.equals("0.1.85")) {
                    String temperature = valueNode.asText().substring(0, 2);
                    result.put("temperature", temperature);
                    DEFAULT_TEMPERATURE = Integer.parseInt(temperature);
                    // input LogValue to store value
                    logValueDto.setTimeStamp(node.get("timeStamp").asLong());
                    logValueDto.setValueResponse(node.get("value").asDouble());
                    Long valueId = valueService.getValueByName("temperature");
                    logValueService.addLogValue(equipmentDto.getEquipmentId(), valueId, logValueDto);

                }
                if (valueNode != null && resourceId.equals("0.2.85")) {
                    result.put("humidity", valueNode.asText().substring(0, 2));
                    // input LogValue to store value
                    logValueDto.setTimeStamp(node.get("timeStamp").asLong());
                    logValueDto.setValueResponse(node.get("value").asDouble());
                    Long valueId = valueService.getValueByName("humidity");
                    logValueService.addLogValue(equipmentDto.getEquipmentId(), valueId, logValueDto);
                }
                result.put("timeStamp", timeStamp);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error processing JSON response", e);
        }
        return result;
    }

    public JSONObject compareTemperature() {
        JSONObject json = new JSONObject();
        String message = "";
        int temperature = weatherService.getWeatherOutSide();
        if (temperature > DEFAULT_TEMPERATURE) {
            message = "Hello";
        }
        json.put("message", message);
        return json;
    }

}
