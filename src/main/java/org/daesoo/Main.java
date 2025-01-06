package org.daesoo;

import org.daesoo.rabbitmq.Publisher;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        try {
            MBeanServerConnection mBeanServerConnection = ManagementFactory.getPlatformMBeanServer();

            Set<ObjectName> mBeans = mBeanServerConnection.queryNames(null, null);
            for (ObjectName mBean : mBeans) {
                MBeanInfo mBeanInfo = mBeanServerConnection.getMBeanInfo(mBean);
                for (MBeanAttributeInfo attrInfo : mBeanInfo.getAttributes()) {
                    if (attrInfo.isReadable()) {
                        try {
                            Object value = mBeanServerConnection.getAttribute(mBean, attrInfo.getName());
                            String metricData = String.format(
                                    "MBean: %s, Attribute: %s, Value: %s",
                                    mBean.getCanonicalName(),
                                    attrInfo.getName(),
                                    value
                            );

                            // Step 3: RabbitMQ에 전송
                            Publisher.sendToRabbitMQ(metricData);
                        } catch (Exception e) {
                            System.err.printf("Unable to read attribute %s from MBean %s: %s%n",
                                    attrInfo.getName(), mBean.getCanonicalName(), e.getMessage());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}