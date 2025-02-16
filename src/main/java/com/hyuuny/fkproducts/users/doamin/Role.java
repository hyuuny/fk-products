package com.hyuuny.fkproducts.users.doamin;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ADMIN("관리자"),
    CUSTOMER("고객");

    private final String title;
}
