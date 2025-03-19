package com.example.SmartBuildingBackend.dto;

import lombok.Data;
import java.util.List;
@Data
public class DeviceInforRequestDto {
    private String intend = "query.device.info";
    private DataPayload data;

    @Data
    public static class DataPayload {
        private List<String> dids;  // List of device IDs
        private String positionId = "";  // Position ID (optional)
        private int pageNum = 1;  // Default page number
        private int pageSize = 50;  // Default page size
    }
}
