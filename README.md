# authzSql

```json
{
    "code":0,
    "message":"",
    "body":{
        "group":{
            "id":"1",
            "name":"abc",
            "permissions":[
                {
                    "resource":"LIKE:20014%",
                    "operation":"VIEW"
                },
                {
                    "resource":"IN:200141,200142,200143",
                    "operation":"VIEW"
                },
                {
                    "resource":"EQUAL:200146",
                    "operation":"VIEW"
                },
                {
                    "resource":"NOT_LIKE:20013%",
                    "operation":"VIEW"
                },
                {
                    "resource":"NOT_IN:200141,200142,200143",
                    "operation":"VIEW"
                },
                {
                    "resource":"UNEQUAL:200145",
                    "operation":"VIEW"
                },
                {
                    "resource":"LIKE:20014%",
                    "operation":"DELETE"
                },
                {
                    "resource":"IN:200141,200142,200143",
                    "operation":"DELETE"
                }
            ]
        }
    }
}
```
- 权限数据提供者 （获取用户权限串）
- 权限数据转换及存储 （将用户权限串转换为特定格式存储）
- 解析原始SQL （解析原始SQL， 提取需要替换的信息）
- 转化原始SQL （从权限数据中解析出SQL条件，替换原始SQL中的占位符）
- 操作解析（部分情况才会使用，比如风场列表需要，但是风场下拉列表就不需要）