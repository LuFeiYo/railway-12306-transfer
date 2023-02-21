package com.lhw.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lufei.lhw
 * @date 2023/2/21 12:16
 * @description 操作系统枚举
 */
@Getter
@AllArgsConstructor
public enum OperationSystemEnum {

    LINUX("Linux"),
    WINDOWS("Windows"),
    MACOS("macOS");

    /**
     * 操作系统名称
     */
    private String systemName;
}
