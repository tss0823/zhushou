/**
 *
 * Created by shan on 2016/4/7.
 */
function jcallback(param) {
    debugger;
}
function callbackparam(param) {
    debugger;
}

(function (YT) {
    if (!YT.deploy.reqContent) {
        YT.deploy.reqContent = {};
    }

    var common = {

        route_callback: function (d, data) {
            console.log("reqContent bind  after render call");
            //组件初始化之后
            var appList = YT.deploy.data.appList;
            YT.deploy.util.initSelect(appList, "name", "name", "appName", data.appName);

            var enums = YT.deploy.data.enums;

            //moduleType
            YT.deploy.util.initEnumSelect(enums.moduleType, "module", data.module);

            $(document).on("change", "#paramType", function () {
                var type = $(this).val();
                // debugger;
                $(this).parent().find("input[id='paramValue']").attr("data", 0);
                $(this).parent().find("input[type='" + type + "']").attr("data", 1);
                if (type == "file") {
                    $(this).nextAll(".btn-file").show();
                    $(this).nextAll("#fileNameText").show();
                    $(this).nextAll("input").hide();
                } else {
                    $(this).nextAll(".btn-file").hide();
                    $(this).nextAll("#fileNameText").hide();
                    $(this).nextAll("input[type='" + type + "']").show();
                }
            });

            //文件选择
            $(document).on("change", ":file[id='paramValue']", function () {
                // debugger;
                var file = $(this).prop("files")[0];
                $(this).parent().next("#fileNameText").html(file.name);
            });


            //添加header
            $("button[id='btnAddHeader']").click(function () {
                var $tr = $("#tbReqHeader").find("tr[name='dataItem']").first().clone();
                $tr.show();
                $("#tbReqHeader").append($tr);
            });

            //添加reqData
            $("button[id='btnAddReqData']").click(function () {
                var $tr = $("#tbReqData").find("tr[name='dataItem']").first().clone();
                $tr.show();
                $("#tbReqData").append($tr);
            });

            //上移
            $(document).on("click", "a[name='itemArrowUp']", function () {
                var $thisTr = $(this).parents("tr");
                var $prevTr = $thisTr.prev();
                $thisTr.after($prevTr);
                // $prevTr.before($thisTr);
            });

            //下移
            $(document).on("click", "a[name='itemArrowDown']", function () {
                var $thisTr = $(this).parents("tr");
                var $nextTr = $thisTr.next();
                $nextTr.after($thisTr);
            });


            //删除
            $(document).on("click", "a[name='itemRemove']", function () {
                // debugger;
                // if (!confirm("您确要删除吗？")) {
                //     return false;
                // }
                if ($("#tbContent").find("tr[name='dataItem']").length == 1) {
                    alert("必须保留一条记录");
                    return;
                }
                $(this).parents("tr").remove();
            });


            $("button[id='btnRequest']").click(function () {

                // var params = YT.deploy.util.getFormParams("#reqContentForm");

                var reqUrl = $("#reqUrl").val();
                //get headers
                var headers = {};
                var headerList = [];
                $("#tbReqHeader").find("tr[name='dataItem']").each(function (index, item) {
                    var code = $(item).find("input[id='headerKey']").val();
                    // debugger;
                    if (!code) {
                        return true;
                    }
                    var value = $(item).find("input[id='headerValue']").val();
                    var dataMap = {key:code,value:value};
                    headerList.push(dataMap);
                });
                //end

                //get reqParam
                // var reqDatas = {};
                var formData = new FormData();
                var dataList = [];
                $("#tbReqData").find("tr[name='dataItem']").each(function (index, item) {
                    var code = $(item).find("input[id='paramKey']").val();
                    // debugger;
                    if (!code) {
                        return true;
                    }

                    var $paramValue = $(item).find("input[id='paramValue'][data = '1']");
                    var value = null;
                    if ($paramValue.attr("type") == "file") {  //文件
                        value = $paramValue.prop("files")[0];
                        formData.append(code, value);
                    } else {
                        value = $paramValue.val();
                        formData.append(code, value);
                    }
                    var dataMap = {key:code,value:value};
                    dataList.push(dataMap);
                });
                //end


                var data = {url:reqUrl,headerList:headerList,dataList:dataList};
                debugger;
                // jQuery.ajaxSettings.traditional = true;
                $.ajax({
                    url: '/reqContent/execute',
                    type: 'POST',
//            data:{pageNum:5,pageSize:100},
//                     data: data,
                    data: formData,
                    // headers: headers,
                    async: false,
                    cache: false,
                    contentType: false,
                    processData: false,
                    success: function (data, status, xhr) {
                        debugger;
                    },
                    error: function (xhr, status, err) {
                        debugger;
                    },

                });

                $.extend({
                });

                $("#tbReqParam").find("tr[name='dataItem']").each(function (index, item) {
                    var code = $(item).find("input[id='code']").val();
                    // debugger;
                    if (!code) {
                        return true;
                    }
                    params["paramList[" + index + "].code"] = code;
                    params["paramList[" + index + "].name"] = $(item).find("input[id='name']").val();
                    params["paramList[" + index + "].memo"] = $(item).find("input[id='memo']").val();
                    params["paramList[" + index + "].rule"] = $(item).find("input[id='rule']").val();
                });


                // if(params["id"]){  //update
                //     YT.deploy.util.reqPost("/idocUrl/update", params, function (d) {
                //         if (d.success) {
                //             alert("修改成功");
                //             YT.deploy.idocList.query(1,20);
                //         } else {
                //             alert("修改失败,err=" + d.message);
                //         }
                //     });
                //
                // }else{ //save
                //     YT.deploy.util.reqPost("/idocUrl/save", params, function (d) {
                //         if (d.success) {
                //             alert("保存成功");
                //             YT.deploy.idocList.query(1,20);
                //         } else {
                //             alert("保存失败,err=" + d.message);
                //         }
                //     });
                // }
            });

        },
    }
    $.extend(YT.deploy, common);


    YT.deploy.reqContent = {

        success_register: function (param) {
            debugger;
        }
    }

})(window.YunTao);

