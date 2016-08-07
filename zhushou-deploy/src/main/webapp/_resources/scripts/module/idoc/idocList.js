/**
 *
 * Created by shan on 2016/4/7.
 */
(function (YT) {
    if (!YT.deploy.idocList) {
        YT.deploy.idocList = {};
    }

    var common = {

        route_callback: function (d, data) {
            console.log("app list after render call");
            //组件初始化之后
            var appList = YT.deploy.data.appList;
            YT.deploy.util.initSelect(appList, "name", "name", "appName", data.appName);

            var enums = YT.deploy.data.enums;

            //moduleType
            YT.deploy.util.initEnumSelect(enums.moduleType, "module", data.module);

            //tab
            $( "div[id='tabs']" ).tabs();

            //注册事件
            $("#btnQuery").click(function () {
                var pageNum = 1;  //查询触发从第一页开始
                var pageSize = $("#pageSize").val();
                $(this).html("查询中...");
                $(this).attr("disabled", "true");
                YT.deploy.idocList.query(pageNum, pageSize);
            });

            //分页信息init
            YT.deploy.util.paginationInit(d.data, YT.deploy.idocList.query);
            
            //创建接口
            $("#btnNewTemplate").click(function () {
                YT.deploy.routeTpl("/idoc/bind.html",{title:"接口新建"});
            });

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

            $("a[name='btnDel']").click(function () {
                if (!confirm("您确认需要删除吗？")) {
                    return;
                }

                var id = $(this).attr("data");
                YT.deploy.util.reqPost("/idocUrl/deleteById", {id:id}, function (d) {
                    if (d.success) {
                        alert("删除成功");
                        YT.deploy.route("/idocUrl/list", params, "/log/idoc.html", {});
                    } else {
                        alert("删除失败,err=" + d.message);
                    }
                });
            });
            
        },
    }
    
    $.extend(YT.deploy, common);


    YT.deploy.idocList = {
        query: function (pageNum, pageSize) {
            var params = YT.deploy.util.getFormParams("#idocListForm");
            params["pageNum"] = pageNum;
            params["pageSize"] = pageSize;
            var ext_data = $.extend(params, {title: "接口文档"});
            YT.deploy.route("/idocUrl/list", params, "/idoc/list.html", ext_data);
        },


    }

})(window.YunTao);
