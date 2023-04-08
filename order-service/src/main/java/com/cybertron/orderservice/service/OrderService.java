package com.cybertron.orderservice.service;

import com.cybertron.orderservice.dto.InventoryResponse;
import com.cybertron.orderservice.dto.OrderLineItemsDto;
import com.cybertron.orderservice.dto.OrderRequest;
import com.cybertron.orderservice.model.Order;
import com.cybertron.orderservice.model.OrderLineItems;
import com.cybertron.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient webClient;
    public void placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList().stream()
                .map(this::mapToDto)
                        .toList();
        order.setOrderLineItemsList(orderLineItems);
        List<String> skuCodes = order.getOrderLineItemsList()
                .stream().map(OrderLineItems::getSkuCode).toList();
        //call inventory service and place order if product is in stock
       InventoryResponse[] inventoryResponsesArray = webClient.get()
                .uri("http://localhost:8082/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode",skuCodes).build())
                        .retrieve()
                                .bodyToMono(InventoryResponse[].class)
                                        .block();

       boolean allProductInStock = Arrays.stream(inventoryResponsesArray)
               .allMatch(InventoryResponse::getIsInStock);
       if(allProductInStock){
           orderRepository.save(order);
       }
       else
           throw new IllegalArgumentException("Product is OutOfStock");


    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
