package org.yug.backend.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CommunityResponse {
    private UUID id;
    private String name;
    private String description;
    private String createdBy;
    private int memberCount;


    public CommunityResponse(UUID id, String name, String description, String createdBy, int memberCount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdBy = createdBy;
        this.memberCount = memberCount;

    }
}