package cn.jboost.springboot.autoconfig.redis.clients;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import java.util.Map;

public class RedisClientsConfigurationRegistrar implements ImportBeanDefinitionRegistrar {
  @Override
  public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
    Map<String, Object> attrs = metadata.getAnnotationAttributes(
        RedisClients.class.getName(), true);
    if (attrs != null && attrs.containsKey("value")) {
      AnnotationAttributes[] clients = (AnnotationAttributes[]) attrs.get("value");
      for (AnnotationAttributes client : clients) {
        registerClientConfiguration(registry, getClientName(client),
            client.get("configuration"));
      }
    }
    if (attrs != null && attrs.containsKey("defaultConfiguration")) {
      String name;
      if (metadata.hasEnclosingClass()) {
        name = "default." + metadata.getEnclosingClassName();
      } else {
        name = "default." + metadata.getClassName();
      }
      registerClientConfiguration(registry, name,
          attrs.get("defaultConfiguration"));
    }
    Map<String, Object> client = metadata.getAnnotationAttributes(
        RedisClient.class.getName(), true);
    String name = getClientName(client);
    if (name != null) {
      registerClientConfiguration(registry, name, client.get("configuration"));
    }
  }

  private String getClientName(Map<String, Object> client) {
    if (client == null) {
      return null;
    }
    String value = (String) client.get("value");
    if (!StringUtils.hasText(value)) {
      value = (String) client.get("name");
    }
    if (StringUtils.hasText(value)) {
      return value;
    }
    throw new IllegalStateException(
        "Either 'name' or 'value' must be provided in @RedisClient");
  }

  private void registerClientConfiguration(BeanDefinitionRegistry registry,
                                           Object name, Object configuration) {
    BeanDefinitionBuilder builder = BeanDefinitionBuilder
        .genericBeanDefinition(RedisClientSpecification.class);
    builder.addConstructorArgValue(name);
    builder.addConstructorArgValue(configuration);
    registry.registerBeanDefinition(name + ".RedisClientSpecification",
        builder.getBeanDefinition());
  }
}
