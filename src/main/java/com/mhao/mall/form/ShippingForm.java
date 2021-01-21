package com.mhao.mall.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Created by FightingHao on 2020/4/30
 */

@Data
public class ShippingForm {

    @NotBlank
    private String receiverName;

    private String receiverPhone;

    @NotBlank
    private String receiverMobile;

    @NotBlank
    private String receiverProvince;

    @NotBlank
    private String receiverCity;

    @NotBlank
    private String receiverDistrict;

    @NotBlank
    private String receiverAddress;

    @NotBlank
    private String receiverZip;
}
