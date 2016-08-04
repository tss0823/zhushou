/**
 *
 * Created by shan on 2016/4/7.
 */
(function (YT) {
    if (!YT.deploy.idoc) {
        YT.deploy.idoc = {};
    }

    var common = {

        route_callback: function (d, data) {
            console.log("app list after render call");
            //组件初始化之后
            var appData = YT.deploy.data.appData;
            var appList = appData.appList;
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
                YT.deploy.idoc.query(pageNum, pageSize);
            });

            //分页信息init
            YT.deploy.util.paginationInit(d.data, YT.deploy.idoc.query);

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

            $("a[name='btnDelete']").click(function () {
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

            //添加
            $("button[id='btnAdd']").click(function () {
                var $tr = $("#tbContent").find("tr[name='dataItem']").first().clone();
                $("#tbContent").append($tr) ;
            });
            
            //删除
            $(document).on("click","div[name='itemRemove']",function(){
                if (!confirm("您确要删除吗？")) {
                    return false;
                }
                if($("#tbContent").find("tr[name='dataItem']").length == 1){
                    alert("必须保留一条记录");
                    return;
                }
                $(this).parents("tr").remove();
            });


            $("button[id='btnSave']").click(function () {

                var params = YT.deploy.util.getFormParams("#idocForm");
                $("#tbReqParam").find("tr[name='dataItem']").each(function (index, item) {
                    var code = $(item).find("input[id='code']").val();
                    if(!code){
                        return true;
                    }
                    params["paramList[" + index + "].code"] = code;
                    params["paramList[" + index + "].name"] = $(item).find("input[id='name']").val();
                    params["paramList[" + index + "].memo"] = $(item).find("input[id='memo']").val();
                    params["paramList[" + index + "].rule"] = $(item).find("input[id='rule']").val();
                });
                YT.deploy.util.reqPost("/idocUrl/save", params, function (d) {
                    if (d.success) {
                        alert("保存成功");
                        YT.deploy.route("/idocUrl/list", params, "/log/idoc.html", {});
                    } else {
                        alert("保存失败,err=" + d.message);
                    }
                });
            });

        },
    }
    $.extend(YT.deploy, common);


    YT.deploy.idoc = {
        query: function (pageNum, pageSize) {
            var params = YT.deploy.util.getFormParams("#idocForm");
            params["pageNum"] = pageNum;
            params["pageSize"] = pageSize;
            var ext_data = $.extend(params, {title: "接口文档"});
            YT.deploy.route("/idocUrl/list", params, "/idoc/list.html", ext_data);
        },


    }

})(window.YunTao);
