package com.example.bussystem.reservation.service;

import com.example.bussystem.common.config.RabbitmqConfigs;
import com.example.bussystem.reservation.event.StockDecreaseEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StockDecreaseEventHandler {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void publish(StockDecreaseEvent event){
        rabbitTemplate.convertAndSend(RabbitmqConfigs.STOCK_DECREASE_QUE, event);
    }

//    @Transactional
//    @RabbitListener(queues = RabbitmqConfigs.STOCK_DECREASE_QUE)
//    public void listen(){
//
//    }
}
