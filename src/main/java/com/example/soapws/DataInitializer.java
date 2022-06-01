package com.example.soapws;

import com.example.soapws.model.Product;
import com.example.soapws.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class DataInitializer  implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(DataInitializer.class);
    private final ProductRepository productRepository;

    public void initData(){
        Product p1 = Product.builder()
                .name("JustDoIt")
                .brand("Nike")
                .price(90)
                .amount(37)
                .build();
        Product p2 = Product.builder()
                .name("754")
                .brand("NewBalance")
                .price(419.99f)
                .amount(23)
                .build();
        Product p3 = Product.builder()
                .name("Jordan II")
                .brand("Nike")
                .price(578.97f)
                .amount(3)
                .build();

        productRepository.saveAll(Arrays.asList(p1, p2 , p3));
        LOG.info("DATA INITIALIZED");
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        initData();
    }

}
