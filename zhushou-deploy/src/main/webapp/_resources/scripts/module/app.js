/**
 * 
 * Created by shan on 2016/4/7.
 */
(function(YT){
    if (!YT.deploy.app) {
        YT.deploy.app = {};
    }

    var common = {

        route_callback : function () {
            console.log("app list after render call");
            //组件初始化之后
            //注册事件
            $("a[id='enterDeploy']").each(function(index,item){
                $(this).click(function(){
                    var appId = $(item).attr("appId");
                    var model = $(item).attr("model");
                    var appName = $(item).attr("appName");
                    var data = {title:"发布节点",titleColor:$(item).attr("color"),msTitle:" 【 "+appName+" 】 > "+model};
                    data["model"] = model;
                    data["appName"] = appName;
                    YT.deploy.route("/host/getListByAppAndModel",{appId:appId,model:model},"appHost.html",data);
                });
            });

        },
    }
    $.extend(YT.deploy, common);


    YT.deploy.app = {
        
    }
    
})(window.YunTao);
