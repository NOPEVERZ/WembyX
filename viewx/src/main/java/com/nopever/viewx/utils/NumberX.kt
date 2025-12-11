package com.nopever.viewx.utils

import java.math.BigDecimal
import java.math.RoundingMode

/**
 * 转为四舍五入double
 */
fun BigDecimal.real(): Double {
    return this.setScale(2, RoundingMode.HALF_UP).toDouble()
}
