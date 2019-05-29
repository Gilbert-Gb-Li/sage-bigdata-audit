# MiniLisp
Java外部DSL，输入为event:Map[String,Any]

DSL以(开头，否则是普通字符串。

+ 数据
    + app=uxin
    + table=car_info
    + timestamp='March 11th 2019, 02:18:33.168'	
    
| 字段   |      类型      |  值 |
|-----------|:-------------:|------:|
| app       | 文本    | uxin      |
| table     | 文本    | car-info  |
| timestamp | 日期    | 2019-03-16 17:19:21   |
| price     | 小数    | 150000.10 |
| age       | 整数    | 3         |
| car_id    | 文本    | 123456    |


## field函数
+ 配置
```lisp
(field 'app')
```
+ 结果：
```
uxin
```
## date函数

+ 配置
```lisp
(date-format (field 'timestamp') 'yyyy-MM-dd')
```
+ 结果：
```
2019-03-16
```
## concat函数
```lisp
(concat 
     (field 'app') '_' 
     (field 'table') '_' 
     (date-format (field 'timestamp') 'yyyy-MM-dd') '_' 
     (field 'car_id')
 )
```
+ 结果：
```
uxin_car-info_2019-03-16_123456
```
## nil函数
+ 配置
```lisp
()
```
+ 结果：
```
null
```

## static函数
直接量，可以省略函数名

+ 配置
```lisp
(static 'xx')
(static 1234)
'xy'
234
```
+ 结果：
```
xx
1234
xy
234
```