# Points
A paper plugin, some useful points

功能

    /here -> 高亮自身 公屏发坐标
    /where -> 得到某个玩家的坐标
    /death message -> 死亡的时候发送死亡位置
    /death log -> 记录死亡信息

防爆 anti-boom
- 床 -> bed
- 苦力怕 -> creeper
- 末影水晶 -> ender-crystal
- 恶魂 -> ghast 
- tnt矿车 -> minecart-tnt 
- TNT -> tnt
- 凋零 -> wither

指令和权限枚举

权限支持 * 通配符

    /here
    广播自己坐标
    points.command.here

    /where
    获取自己坐标
    points.command.where.self
    
    /where Shacha086
    获取玩家坐标
    points.command.where.other
    points.command.where.Shacha086 //对于特定玩家

    /death message
    切换是否发送死亡坐标
    points.command.death.message

    是否发送死亡坐标 监听器管理
    points.listener.death.message
    
    /death log
    获取自己的死亡历史
    points.command.death.log.self

    /death log Shacha086
    获取玩家死亡历史
    points.command.death.log.other
    points.command.death.log.Shacha086 //对于特定玩家


    /points reload
    重载插件
    points.reload
 