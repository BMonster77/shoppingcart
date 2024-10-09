package com.shoppingcart.springboot.service;

import com.shoppingcart.springboot.model.Order;
import com.shoppingcart.springboot.model.Voucher;
import com.shoppingcart.springboot.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VoucherServiceImpl implements VoucherService {

    private final VoucherRepository voucherRepository;

    @Autowired
    public VoucherServiceImpl(VoucherRepository voucherRepository) {
        this.voucherRepository = voucherRepository;
    }

    @Override
    public Optional<Voucher> findVoucherByCode(String voucherCode) {
        // 根据代金券代码查找代金券
        return voucherRepository.findByVoucherCode(voucherCode);
    }

    @Override
    public double applyVoucherDiscount(Order order, Voucher voucher) {
        // 检查代金券是否有剩余数量
        if (voucher.getRemainingQuantity() > 0) {
            double discount = voucher.getVoucherDiscount();
            double discountedPrice = order.getFinalPrice() - discount;
            order.setFinalPrice(Math.max(discountedPrice, 0)); // 确保价格不为负值

            // 减少代金券的剩余数量
            voucher.setRemainingQuantity(voucher.getRemainingQuantity() - 1);

            // 更新数据库中的代金券信息
            voucherRepository.save(voucher);
        }
        return order.getFinalPrice();
    }
}
