package com.stepbook.domain.user.dto;

import lombok.Data;

@Data
public class FacebookUserDto {
    private String id;
    private String name;
    private String email;
    private Picture picture;

    @lombok.Data
    public static class Picture {
        private Data data;
    }

    @lombok.Data
    public static class Data {
        private long height;
        private boolean isSilhouette;
        private String url;
        private long width;
    }

}




