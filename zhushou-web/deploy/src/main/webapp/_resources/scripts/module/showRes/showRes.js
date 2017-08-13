/**
 *
 * Created by shan on 2016/4/7.
 */
(function (YT) {
    if (!YT.deploy.showRes) {
        YT.deploy.showRes = {};
    }

    var common = {

        route_callback: function (d, data) {
            console.log("app list after render call");
            //组件初始化之后
            var appList = YT.deploy.data.appList;
            YT.deploy.util.initSelect(appList, "name", "name", "appName", data.appName);

            var enums = YT.deploy.data.enums;

            //moduleType
            YT.deploy.util.initEnumSelect("moduleType", "module", data.module);

            //tab
            $( "div[id='tabs']" ).tabs();

            //注册事件
            $("#btnQuery").click(function () {
                var pageNum = 1;  //查询触发从第一页开始
                $(this).html("查询中...");
                $(this).attr("disabled", "true");
                YT.deploy.showRes.query(pageNum);
            });


            //分页信息init
            YT.deploy.util.paginationInit(d.data, YT.deploy.showRes.query);
            

            //tab
            $( "div[id='tabs']" ).tabs();

            //调出详情
            $("a[name='enterView']").click(function () {
                var dataId = $(this).attr("data");
                var item = $("tr[data='trChildItem_" + dataId + "']").first();
                var state = ($(item).css("display") == "none");
                //所有的hide
                $("tr[name='trChildItem']").hide();
                if (state) {
                    $(item).show();
                } else {
                    $(item).hide();
                }
            });

        },
    }
    
    $.extend(YT.deploy, common);


    YT.deploy.showRes = {
        query: function (pageNum) {
            var params = YT.deploy.util.getFormParams("#showResForm");
            params["pageNum"] = pageNum;
            var pageSize = $("#pageSize").val();
            var ext_data = $.extend(params, {title: "代理列表"});
            YT.deploy.route("/proxy/list", params, "/proxy/list.html", ext_data);
        },


    }

})(window.YunTao);
