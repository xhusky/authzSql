
- 定义权限为“所有”的情况
- 存在“所有”权限时处理，不需要OR
- 参考foxmail过滤器
- placeholder 比如格式 'K2AUTH/windFarmId/a.windFarmId'='K2AUTH/windFarmId/a.windFarmId'
- 梳理健康风场使用场景

------
- 资源resource 仅返回 LIKE:xx 格式
- 占位符简化 'K2AUTH/windFarmId/a.windFarmId'<>''
- IN,LIKE,=  逻辑运算为OR, NOT IN, NOT LIKE, <> 逻辑运算为AND
- 下载,删除,修改,执行,取消  等操作（关注分页）
- 单个资源操作的权限验证，比如delete（拦截器）
- K2AUTH/windFarm/view/a.wfId
- K2AUTH/windFarm/delete/a.wfId


    VIEW, EDIT, DELETE, EXECUTE, CANCEL, DOWNLOAD


SELECT
    id,
    windFarmId,
    windFarmName,
    'K2AUTH/EDIT/windFarmId' AS "edit",
    'K2AUTH/DELETE/windFarmId' AS "delete",
    'K2AUTH/EXECUTE/windFarmId' AS "execute",
    'K2AUTH/CANCEL/windFarmId' AS "cancel",
    'K2AUTH/DOWNLOAD/windFarmId' AS "download"
FROM
    gw_basic_windfarm

SELECT
    id,
    windFarmId,
    windFarmName,
    (SELECT count(1) from dual where windFarmId like '320%' or windFarmId like '122%'),
    (SELECT count(1) from dual where windFarmId like '320%' or windFarmId like '122%'),
    (SELECT count(1) from dual where windFarmId like '320%' or windFarmId like '122%') AS download
FROM
    gw_basic_windfarm


SELECT
    id,
    windFarmId,
    windFarmName,
    CONCAT(
        (SELECT count(1) from dual where windFarmId like '320%' or windFarmId like '122%'),
        (SELECT count(1) from dual where windFarmId like '320%' or windFarmId like '122%'),
        (SELECT count(1) from dual where windFarmId like '320%' or windFarmId like '122%')
    ) AS operation
FROM
    gw_basic_windfarm
    
-----

- 多字段筛选
- 多数据类型支持
- 权限缓存回调

- 查询权限无结果时 处理
- 所有权限时 处理

- plugin 