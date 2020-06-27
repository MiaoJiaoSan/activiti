package com.miaojiaosan.activiti.api.dto;

public class CreatProcessDefinitionDTO {

  String key;

  String name;

  String xml;

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getXml() {
    return xml;
  }

  public void setXml(String xml) {
    this.xml = xml;
  }
}
