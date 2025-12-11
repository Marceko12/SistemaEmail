package com.system.emails.model.dto;

import lombok.Data;

@Data
public class EmailRequest {

    private String to;
    private String subject;
    private String message;
    
}
