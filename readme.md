# 敏捷：
+ 最快实现（MVP）
+ 快速演进

# 功能

## 手工审计
数仓分析结束后，回调程序API，进行审计运算。
## 自动审计
每天定时（8点）进行审计

# 审计任务
## 获取指标值
## 条件判断




# 使用手册
## 基础配置
### APP配置
+ 建表
```SQL
CREATE TABLE `cep_app` (
  `id` varchar(64) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;
```
+ 添加数据
```SQL
INSERT INTO `sage_bigdata`.`cep_app` (`id`, `name`) VALUES ('uxin', '优信');
```
### 组件
```SQL
CREATE TABLE `cep_component` (
  `id` varchar(64) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `auditInfos` longtext,
  `create_date` datetime DEFAULT NULL,
  `modify_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;
```
+ 添加组件
```SQL
INSERT INTO `sage_bigdata`.`cep_component` (`id`, `name`, `auditInfos`, `create_date`, `modify_date`) VALUES ('es1', '业务ES', '{\r\n  \"type\": \"es\",\r\n  \"clusterName\": \"NX_ES\",\r\n  \"hosts\": [\r\n    {\r\n      \"host\": \"mvp-hadoop40\",\r\n      \"port\": 9300\r\n    },\r\n    {\r\n      \"host\": \"mvp-hadoop41\",\r\n      \"port\": 9300\r\n    },\r\n    {\r\n      \"host\": \"mvp-hadoop42\",\r\n      \"port\": 9300\r\n    }\r\n  ]\r\n}', '2019-05-07 17:48:11', '2019-05-10 20:21:34');
INSERT INTO `sage_bigdata`.`cep_component` (`id`, `name`, `auditInfos`, `create_date`, `modify_date`) VALUES ('kafka1', '业务KAFKA', '{\r\n  \"type\": \"kafka\",\r\n  \"hosts\": [\r\n    {\r\n      \"host\": \"172.16.208.67\",\r\n      \"port\": 6667\r\n    }\r\n  ]\r\n}', '2019-05-07 17:48:11', '2019-05-16 16:21:12');
INSERT INTO `sage_bigdata`.`cep_component` (`id`, `name`, `auditInfos`, `create_date`, `modify_date`) VALUES ('smtp', 'SMTP服务器', '{\r\n  \"type\": \"smtp\",\r\n  \"ssl\": true,\r\n  \"host\": \"smtp.exmail.qq.com\",\r\n  \"port\": 465,\r\n  \"auth\": true,\r\n  \"user\" : \"ningguanyi@haima.me\",\r\n  \"password\" : \"8N8ahQUii92RfpAK\",\r\n}', '2019-05-16 16:20:41', '2019-05-18 15:58:00');
```
#### ES组件
```JSON
{
  "type": "es",
  "clusterName": "NX_ES",
  "hosts": [
    {
      "host": "mvp-hadoop40",
      "port": 9300
    },
    {
      "host": "mvp-hadoop41",
      "port": 9300
    },
    {
      "host": "mvp-hadoop42",
      "port": 9300
    }
  ]
}
```
#### KAFKA组件
```JSON
{
	"type": "kafka",
	"hosts": [{
			"host": "MVP-HADOOP07",
			"port": 6667
		},
		{
			"host": "MVP-HADOOP07",
			"port": 6667
		},
		{
			"host": "MVP-HADOOP07",
			"port": 6667
		},
		{
			"host": "MVP-HADOOP07",
			"port": 6667
		},
		{
			"host": "MVP-HADOOP07",
			"port": 6667
		}
	]
}
```
#### SMTP组件
```JSON
{
  "type": "smtp",
  "ssl": true,
  "host": "smtp.exmail.qq.com",
  "port": 465,
  "auth": true,
  "user" : "ningguanyi@haima.me",
  "password" : "8N8ahQUii92RfpAK"
}
```
### 通知
+  建表
```SQL
CREATE TABLE `cep_notice` (
  `id` int(11) NOT NULL,
  `type` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `auditInfos` longtext,
  `create_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `modify_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```
+ 添加
```SQL
INSERT INTO `sage_bigdata`.`cep_notice` (`id`, `type`, `name`, `auditInfos`, `create_date`, `modify_date`) VALUES ('1', 'mail', '大数据研发', '{\r\n  \"component\" : \"smtp\",\r\n  \"from\" : \"ningguanyi@haima.me\",\r\n  \"password\" : \"8N8ahQUii92RfpAK\",\r\n  \"to\" : \"deepinsight@haima.me\"\r\n}', '2019-05-17 22:19:59', '2019-05-17 22:19:59');
```

### 指标
+ 建表
```SQL
CREATE TABLE `cep_indicator` (
  `id` varchar(64) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `auditInfos` longtext,
  `unit` varchar(255) DEFAULT NULL,
  `param` int(11) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `modify_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;
```
+ 添加
```SQL
INSERT INTO `sage_bigdata`.`cep_indicator` (`id`, `name`, `auditInfos`, `unit`, `param`, `create_date`, `modify_date`) VALUES ('UXIN02', '已售车源数量', ' {\r\n  \"type\": \"es-count\",\r\n  \"component\": \"es1\",\r\n  \"indices\": [\r\n    \"(concat \'used_car_r_\' (date-format (field \'timestamp\') \'yyyyMMdd\')\"\r\n  ],\r\n  \"query\": {\r\n    \"bool\": {\r\n      \"should\": [\r\n        {\r\n          \"match_phrase\": {\r\n            \"meta_table_name\": {\r\n              \"query\": \"car_had_sale\"\r\n            }\r\n          }\r\n        },\r\n        {\r\n          \"match_phrase\": {\r\n            \"meta_table_name.keyword\": {\r\n              \"query\": \"car_had_sale\"\r\n            }\r\n          }\r\n        }\r\n      ]\r\n    }\r\n  }\r\n}', 'day', '0', '2019-05-07 17:48:11', '2019-05-14 14:20:31');
```
#### es-count
```JSON
{
  "type": "es-count",
  "component": "es1",
  "indices": [
    "(concat 'used_car_r_' (date-format (field 'timestamp') 'yyyyMMdd')"
  ],
  "query": {
    "bool": {
      "should": [
        {
          "match_phrase": {
            "meta_table_name": {
              "query": "car_had_sale"
            }
          }
        },
        {
          "match_phrase": {
            "meta_table_name.keyword": {
              "query": "car_had_sale"
            }
          }
        }
      ]
    }
  }
}
```

### 审计规则
```SQL
CREATE TABLE `cep_audit_rule` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `auditInfos` longtext,
  `app_id` varchar(64) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `modify_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;
```

### 告警规则
```SQL
CREATE TABLE `cep_alarm_rule` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `auditInfos` longtext,
  `app_id` varchar(64) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `modify_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;
```

