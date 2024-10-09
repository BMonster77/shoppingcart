package com.shoppingcart.springboot.repository;

import com.shoppingcart.springboot.model.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    // 根据代金券代码查找代金券
    Optional<Voucher> findByVoucherCode(String voucherCode);
}
