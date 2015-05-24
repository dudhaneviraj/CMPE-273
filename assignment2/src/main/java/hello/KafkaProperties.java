package main.java.hello;
public interface KafkaProperties
{
  final static String zkConnect = "localhost:2181";
  final static  String groupId = "group1";
  final static String topic = "cmpe273-topic";
  final static String kafkaServerURL = "54.149.84.25";
  final static int kafkaServerPort = 9092;
  final static int kafkaProducerBufferSize = 64*1024;
  final static int connectionTimeOut = 100000;
  final static int reconnectInterval = 10000;
}