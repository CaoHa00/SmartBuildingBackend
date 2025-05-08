package com.example.SmartBuildingBackend.dto.tuyaResponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TuyaResponse {
    private boolean success;
    private Result result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {
        private List<TuyaProperty> properties;

        public List<TuyaProperty> getProperties() {
            return properties;
        }

        public void setProperties(List<TuyaProperty> properties) {
            this.properties = properties;
        }
    }
}