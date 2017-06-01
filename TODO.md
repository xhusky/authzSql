
- 定义权限为“所有”的情况
- 存在“所有”权限时处理，不需要OR
- 参考foxmail过滤器
- placeholder 比如格式 'K2AUTH/windFarmId/a.windFarmId'='K2AUTH/windFarmId/a.windFarmId'
- 梳理健康风场使用场景

------
- 资源resource 仅返回 LIKE:xx 格式
- 占位符简化
- IN,LIKE,=  逻辑运算为OR, NOT IN, NOT LIKE, <> 逻辑运算为AND
- 下载,删除,修改,执行,取消  等操作

- K2AUTH/windFarm/view/a.wfId
- K2AUTH/windFarm/delete/a.wfId

-----

- 多字段筛选
- 权限缓存回调
