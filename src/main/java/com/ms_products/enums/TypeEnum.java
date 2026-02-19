package com.ms_products.enums;

import lombok.Getter;

@Getter
public enum TypeEnum {
    Egress("EGRESS"),
    Income("INCOME");

    private final String value;

    TypeEnum(String value) {
        this.value = value;
    }


}
