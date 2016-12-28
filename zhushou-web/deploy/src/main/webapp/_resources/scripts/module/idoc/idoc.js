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
            console.log("idoc bind  after render call");
            //组件初始化之后
            var appList = YT.deploy.data.appList;
            YT.deploy.util.initSelect(appList, "name", "name", "appName", data.appName);

            var enums = YT.deploy.data.enums;

            //moduleType
            YT.deploy.util.initEnumSelect(enums.moduleType, "module", data.module);


            //添加
            $("button[id='btnAdd']").click(function () {
                var $tr = $("#tbContent").find("tr[name='dataItem']").first().clone();
                $tr.show();
                $("#tbContent").append($tr) ;
            });

            //上移
            $(document).on("click","a[name='itemArrowUp']",function(){
                var $thisTr = $(this).parents("tr");
                var $prevTr = $thisTr.prev();
                $thisTr.after($prevTr);
                // $prevTr.before($thisTr);
            });

            //下移
            $(document).on("click","a[name='itemArrowDown']",function(){
                var $thisTr = $(this).parents("tr");
                var $nextTr = $thisTr.next();
                $nextTr.after($thisTr);
            });

            
            //删除
            $(document).on("click","a[name='itemRemove']",function(){
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
                    // debugger;
                    if(!code){
                        return true;
                    }
                    params["paramList[" + index + "].code"] = code;
                    params["paramList[" + index + "].name"] = $(item).find("input[id='name']").val();
                    params["paramList[" + index + "].memo"] = $(item).find("input[id='memo']").val();
                    params["paramList[" + index + "].rule"] = $(item).find("input[id='rule']").val();
                });
                
                if(params["id"]){  //update
                    YT.deploy.util.reqPost("/idocUrl/update", params, function (d) {
                        if (d.success) {
                            alert("修改成功");
                            YT.deploy.idocList.query(1);
                        } else {
                            alert("修改失败,err=" + d.message);
                        }
                    });
                    
                }else{ //save
                    YT.deploy.util.reqPost("/idocUrl/save", params, function (d) {
                        if (d.success) {
                            alert("保存成功");
                            YT.deploy.idocList.query(1);
                        } else {
                            alert("保存失败,err=" + d.message);
                        }
                    });
                }
            });


            $("button[id='btnSubDocSave']").click(function () {
                var params = {jsonDoc:$("#jsonDoc").val()};
                YT.deploy.util.reqPost("/idocUrl/submitDoc", params, function (d) {
                    if (d.success) {
                        alert("提交成功");
                        YT.deploy.idocList.query(1);
                    } else {
                        alert("提交失败,err=" + d.message);
                    }
                });

            });

            $("button[id='btnSubEnumSave']").click(function () {
                var params = {appName:"member",jsonEnum:$("#jsonEnum").val()};
                YT.deploy.util.reqPost("/idocUrl/submitEnum", params, function (d) {
                    if (d.success) {
                        alert("提交成功");
                        YT.deploy.idocList.query(1);
                    } else {
                        alert("提交失败,err=" + d.message);
                    }
                });

            });




        },
    }
    $.extend(YT.deploy, common);


    YT.deploy.idoc = {

    }

})(window.YunTao);
