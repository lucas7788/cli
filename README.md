# cli
auth manage cli


# 脚本使用说明

1. 安装jdk8或以上版本
2. 替换Java安全策略文件，从jdk官网下载local_policy.jar and US_export_policy.jar文件，
替换${java_home}/jre/lib/security目录下面的local_policy.jar and US_export_policy.jar。
jar包下载地址：
http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html
3. 分配authManager.sh可执行权限
4. 将ontid管理员钱包adminOntId.dat和支付交易费用的钱包payer.dat拷贝到当前目下。
5. 如果执行设置角色的功能，
执行的命令是
```
authManager.sh assignRole
```

该命令会读取assignRoleConfig.json，请修改该文件配置信息。

参数配置说明如下：
```
"url":"http://polaris1.ont.io",      // 连接的节点ip
"restPort":"20334",                  //rest端口号
"websocketPort":"20335",             //websocket端口号，暂不支持
"rpcPort":"20336",                   //rpc端口号
"defaultPort":"restPort",       //设置默认的连接方式（目前支持restPort或rpcPort），如果未设置，默认使用rest方式
"functionName":"registerCandidate",//要分配角色的函数名
"role":"role1",                  //分配的角色名
"gaslimit":20000,                //交易gaslimit
"gasprice":0,                    //交易gasprice
"wallets":[
  "adminOntId.dat",             //管理员ontid钱包
  "payer.dat"                   //支付交易费用的payer钱包
],
"passwords":[
  "passwordtest",              //管理员ontid钱包密码
  "111111"                     //支付交易费用的payer钱包密码
]
```


如果执行为某个ontid分配权限的功能，
执行的命令是
```
authManager.sh assignAuth
```

该命令会读取assignAuthConfig.json文件，请修改该文件配置信息。

参数配置说明如下：
```
"url":"http://polaris1.ont.io",      // 连接的节点ip
"restPort":"20334",                  //rest端口号
"websocketPort":"20335",             //websocket端口号，暂不支持
"rpcPort":"20336",                   //rpc端口号
"defaultPort":"restPort",       //设置默认的连接方式（目前支持restPort或rpcPort），如果未设置，默认使用rest方式
"ontid":"did:ont:AXhCbmhEahpuV1z4unTCn7vVdfUcuzGzMY",//要分配权限的ontid
"role":"role1",                 //要分配的角色名
"gaslimit":20000,                //交易gaslimit
"gasprice":0,                    //交易gasprice
"wallets":[
  "adminOntId.dat",             //管理员ontid钱包
  "payer.dat"                   //支付交易费用的payer钱包
],
"passwords":[
  "passwordtest",              //管理员ontid钱包密码
  "111111"                     //支付交易费用的payer钱包密码
]
```

