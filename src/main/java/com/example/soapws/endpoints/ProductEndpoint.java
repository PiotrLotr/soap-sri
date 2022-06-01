package com.example.soapws.endpoints;


import com.example.soapws.SoapWSConfig;
import com.example.soapws.model.Product;
import com.example.soapws.repo.ProductRepository;
import edu.pja.sri.pwrona.soapws.products.*;
import lombok.RequiredArgsConstructor;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Endpoint
@RequiredArgsConstructor
public class ProductEndpoint {

    private final ProductRepository productRepository;

    @PayloadRoot(namespace = SoapWSConfig.PRODUCT_NAMESPACE, localPart = "getProductsRequest")
    @ResponsePayload
    public GetProductsResponse getProducts(@RequestPayload GetProductsRequest req){
        List<Product> productList = productRepository.findAll();
        List<ProductDto> dtoList = productList.stream()
                .map( this::convertToDto)
                .collect(Collectors.toList());
        GetProductsResponse res = new GetProductsResponse();
        res.getProducts().addAll(dtoList);
        return res;
    }

    @PayloadRoot(namespace = SoapWSConfig.PRODUCT_NAMESPACE, localPart = "getReportRequest")
    @ResponsePayload
    public GetReportResponse generateInventoryReport(@RequestPayload GetReportRequest req){
        List<Product> productList = productRepository.findAll();
        List<Product> inventoryList = new ArrayList<>();

        for(int i=0; i < productList.size(); i++){
            if (productList.get(i).getAmount() <= 5){
                inventoryList.add(productList.get(i));
            }
        }
        //TODO save report in json format

        List<ProductDto> dtoList = inventoryList.stream()
                .map( this::convertToDto )
                .collect(Collectors.toList());

        GetReportResponse res = new GetReportResponse();
        res.getProducts().addAll(dtoList);
        return res;
    }

    private ProductDto convertToDto(Product p){
        if(p == null){
            return null;
        }
        ProductDto dto = new ProductDto();
        dto.setId(new BigDecimal(p.getId()));
        dto.setName(p.getName());
        dto.setAmount(p.getAmount());
        dto.setBrand(p.getBrand());
        dto.setPrice(p.getPrice());

        return dto;
    }

    private Product convertToEntity(ProductDto dto){
        return Product.builder()
                .id(dto.getId() != null ? dto.getId().longValue() : null)
                .name(dto.getName())
                .brand(dto.getBrand())
                .amount(dto.getAmount())
                .price(dto.getPrice())
                .build();
    }




}
