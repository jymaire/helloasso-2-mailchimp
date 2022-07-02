package re.jmai.service;

import app.bean.helloasso.HelloAssoPayment;
import org.springframework.stereotype.Service;
import re.jmai.entity.HelloAssoPaymentEntity;

@Service
public class ConverterService {

    public HelloAssoPaymentEntity helloAssoToEntity(HelloAssoPayment helloAssoPayment){
            HelloAssoPaymentEntity payment = HelloAssoPaymentEntity.PaymentBuilder.aPayment()
                    .withDate(helloAssoPayment.getDate())
                    .withEmail(helloAssoPayment.getPayer().getEmail())
                    .withPayerFirstName(helloAssoPayment.getPayer().getFirstName())
                    .withPayerLastName(helloAssoPayment.getPayer().getLastName())
                    .withId(helloAssoPayment.getId())
                    .withAmount((float) helloAssoPayment.getAmount()/(float) 100)
                    .build();
            return payment;
    }
}
