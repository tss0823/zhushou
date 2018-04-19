//文件清单
deploy.sh   //发布部署入口脚本
package.sh   //编译打包脚本
install.sh   //安装复制脚本
expectInstall.sh   //远程安装复制脚本
expectRestart.sh   //远程重启脚本
expectRollback.sh   //远程回滚脚本
branch_list.sh     //获取分支脚本


//功能操作
1、编译打包命令    ./deploy.sh package,branch_name,model
branch_name //分支名称
model       //模式，有 test,prod 两种
例子：  打包测试分支 fixBug    ./deploy.sh package,fixBug,test


2、发布命令       ./deploy.sh run appName ip
appName //应用名称，目前有 biz proxy user web task bos
ip  内网ip,多个可以逗号隔开
例子： 发布bos 到测试机    ./deploy.sh run bos 10.174.107.140


3、重启命令【同2,将run替换成restart】

4、安装复制命令【同2，将run替换成install】

5、回滚命令【同2，将run替换成rollback】



