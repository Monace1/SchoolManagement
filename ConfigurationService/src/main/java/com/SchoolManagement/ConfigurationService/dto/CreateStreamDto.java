package com.SchoolManagement.ConfigurationService.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateStreamDto {
    private String streamName;

    public String getStreamName() {
        return streamName;
    }

    public void setStreamName(String streamName) {
        this.streamName = streamName;
    }
}

