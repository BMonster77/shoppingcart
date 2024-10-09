package com.shoppingcart.springboot.service;

import com.shoppingcart.springboot.model.Order;
import com.shoppingcart.springboot.model.Voucher;

import java.util.Optional;

public interface VoucherService {
    // 根据代金券代码查找代金券
    Optional<Voucher> findVoucherByCode(String voucherCode);

    // 应用代金券折扣到订单
    double applyVoucherDiscount(Order order, Voucher voucher);
}
