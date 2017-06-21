package com.jovezhao.nest.kafka;

import com.jovezhao.nest.ddd.event1.AbstractChannelProvider;
import com.jovezhao.nest.ddd.event1.IEventHandler;

/**
 * Created by Jove on 2016/7/27.
 */

public class KafkaChannelProvider extends AbstractChannelProvider {


    private String zkconnect;
    private String brokers;
    private int prefetchCount;
    private volatile boolean status;


    public String getZkconnect() {
        return zkconnect;
    }

    public void setZkconnect(String zkconnect) {
        this.zkconnect = zkconnect;
    }

    public String getBrokers() {
        return brokers;
    }

    public void setBrokers(String brokers) {
        this.brokers = brokers;
    }

    public int getPrefetchCount() {
        return prefetchCount;
    }

    public void setPrefetchCount(int prefetchCount) {
        this.prefetchCount = prefetchCount;
    }

    @Override
    public void publish(String eventName, Object data) {
        KafkaProducer producer = new KafkaProducer();
        producer.setZkconnect(zkconnect);
        producer.setBrokers(brokers);
        EventDataDto dto = new EventDataDto(data);
        producer.publish(eventName, dto);

    }

    KafkaConsumer consumer;

    @Override
    public void subscribe(String eventName, IEventHandler handler) {
        status = true;
        EventWork work = new EventWork(eventName, handler);

        consumer = new KafkaConsumer(zkconnect, work, prefetchCount);

        Thread workThread = new Thread(consumer);
        workThread.start();

    }

    @Override
    public void stop() {
        if (consumer != null)
            consumer.stop();
    }
}