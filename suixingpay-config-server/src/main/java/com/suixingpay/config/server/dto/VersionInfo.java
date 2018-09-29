package com.suixingpay.config.server.dto;

import com.suixingpay.config.common.to.VersionDTO;
import lombok.Data;

/**
 * @author: tangqihua[tang_qh@suixingpay.com]
 * @date: 2018年09月11日 17时15分
 * @version: V1.0
 * @review: tangqihua[tang_qh@suixingpay.com]/2018年09月11日 17时15分
 */
@Data
public class VersionInfo {
    private VersionDTO version;
    private VersionDTO localVersion;
}
