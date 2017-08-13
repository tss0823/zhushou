/**
 *
 * Created by shan on 2016/4/7.
 */
(function (YT) {
    if (!YT.deploy.cache) {
        YT.deploy.cache = {};
    }

    var common = {

        route_callback: function (d, data) {
            console.log("cache bind  after render call");
            //组件初始化之后
            var appList = YT.deploy.data.appList;
            YT.deploy.util.initSelect(appList, "name", "name", "appName", data.appName);

            var enums = YT.deploy.data.enums;

            //cacheType
            YT.deploy.util.initEnumSelect("cacheType", "type", data.type);

            $("button[id='btnSearch']").click(function () {
                var type = $("#type").val();
                if(!type){
                    type = "byt";
                }
                var appName = $("appName").val();
                var key = $("#key").val();
                if(appName){
                    key = appName+"_"+key;
                }
                var params = {key:key,field:$("#field").val(),type:type};
                YT.deploy.util.reqPost("/cache/getCache", params, function (d) {
                    if (d.success) {
                        $("#result").html(d.data);
                    } else {
                        alert("查询失败,err=" + d.message);
                    }
                });

            });




        },
    }
    $.extend(YT.deploy, common);


    YT.deploy.cache = {

    }

})(window.YunTao);
