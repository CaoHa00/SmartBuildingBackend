package com.example.SmartBuildingBackend.dto;

import java.util.List;

import lombok.Data;

public class DeviceInfoResponseDto {
     private int code;
    private String requestId;
    private String message;
    private List<Device> result;
    private int totalCount;

    @Data
    public static class Device {
        private String did;
        private String parentDid;
        private String model;
        private int modelType;
        private int state;
        private String timeZone;
        private String firmwareVersion;
        private long createTime;
        private long updateTime;
        private String deviceName;
    }
}
