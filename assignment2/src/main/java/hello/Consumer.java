package main.java.hello;


import java.awt.print.Printable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.JTable.PrintMode;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

public class Consumer extends Thread
{
  private final ConsumerConnector consumer;
  private final String topic;
  
  public Consumer(String topic)
  {
    consumer = kafka.consumer.Consumer.createJavaConsumerConnector(createConsumerConfig());
    this.topic = topic;
  }
  private static ConsumerConfig createConsumerConfig()
  {
    Properties props = new Properties();
    props.put("zookeeper.connect", "54.149.84.25:2181");
    props.put("group.id", KafkaProperties.groupId);
    props.put("zookeeper.session.timeout.ms", "40000");
    props.put("zookeeper.sync.time.ms", "200");
    props.put("auto.commit.interval.ms", "1000");
    return new ConsumerConfig(props);
  }
  public void run() {
    Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
    topicCountMap.put(topic, new Integer(1));
    Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
    KafkaStream stream =  consumerMap.get(topic).get(0);
    ConsumerIterator it = stream.iterator();
    while(it.hasNext()){
      System.out.println("HIIIIIIIIIIIIIIIIIIIIIIIIIIIIi"+it.next().topic()+it.next().message().toString());
    
    }
    }
}