package org.daesoo.rabbitmq;

public enum RabbitMQProperties {
    HOST("192.168.13.50"),
    PORT("5672"),
    USERNAME("openmaru"),
    PASSWORD("openmaru"),
    MBEAN_METRICS("mbean.metrics");


    RabbitMQProperties(String value) {
        this.value = value;
    }

    public final String value;

    public String getValue() {
        return value;
    }

}
