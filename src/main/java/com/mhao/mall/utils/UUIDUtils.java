package com.mhao.mall.utils;

import java.util.UUID;

/**
 * Created by FightingHao on 2020/5/24
 */
public class UUIDUtils {

    /**
     * 去除UUID的"-"
     * @return
     */
    public static String getUUID() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("\\-", "");
    }

}
