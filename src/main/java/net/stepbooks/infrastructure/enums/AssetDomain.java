package net.stepbooks.infrastructure.enums;

import lombok.Getter;

@Getter
public enum AssetDomain {
    BOOK("book/"),
    COURSE("course/"),
    PRODUCT("product/"),
    USER("user/"),
    ADVERTISEMENT("advertisement/"),
    DEFAULT("default/");

    private final String path;
    AssetDomain(String path) {
        this.path = path;
    }
}
