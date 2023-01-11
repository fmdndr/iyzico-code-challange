package com.iyzico.challenge.service;

import com.iyzico.challenge.entity.Payment;
import com.iyzico.challenge.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * <strong>IyzicoPaymentService</strong>  class'ının tepesinde <strong>@Transactional</strong> anotation vardı. <br />
 * <strong>IyzicoPaymentServiceTest.java</strong> bu class aslında bir integration test olduğu için bütün katmaları çalıştırıp <br />
 * bir çıktı elde ediyordu. Sorunun oluştuğu nokta ise zaten API call yapıldığın da bir transaction başlıyordu.<br />
 * Burada tekrardan <strong> @Transactional </strong> anotasyonu ile istek  işlem yapıldıktan sonra kapanmıyor ve kapanmadığı  için <br />
 * yeni bir transactionda başlatamıyordu. <br/>
 * <br/>
 * <strong>@Transactional</strong> annotation'ını sildiğimde method her çağırıldığında yeni bir transactionı ve connectionı <br/>
 * düzgün şekilde yönetmeye başladığını düşünüyorum.
 **/

@Service
public class IyzicoPaymentService {

    private Logger logger = LoggerFactory.getLogger(IyzicoPaymentService.class);
    private BankService bankService;
    private PaymentRepository paymentRepository;

    public IyzicoPaymentService(BankService bankService, PaymentRepository paymentRepository) {
        this.bankService = bankService;
        this.paymentRepository = paymentRepository;
    }


    public void pay(BigDecimal price) {
        //pay with bank
        BankPaymentRequest request = new BankPaymentRequest();
        request.setPrice(price);
        BankPaymentResponse response = bankService.pay(request);

        logger.info("response -> " + response.getResultCode());

        //insert records
        Payment payment = new Payment();
        payment.setBankResponse(response.getResultCode());
        payment.setPrice(price);
        paymentRepository.save(payment);
        logger.info("Payment saved successfully!");

    }
}
