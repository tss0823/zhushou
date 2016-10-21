(function (YT) {
    if (!YT.deploy.reqContent) {
        YT.deploy.reqContent = {};
    }

    var common = {

        route_callback: function (d, data) {
            debugger;
            console.log("reqContent bind  after render call");
            //组件初始化之后

            //初始化右侧栏
            //ajax同步
            $.ajaxSetup({ async: false });
            var reqContentInitData = YT.deploy.data.reqContentInitData;
            YT.deploy.reqContent.initLeftPanel(reqContentInitData);
            $.ajaxSetup({ async: true });
            //end

            var appList = YT.deploy.data.appList;
            debugger;
            YT.deploy.util.initSelect(appList,"name","name","appName",reqContentInitData.appName);

            var enums = YT.deploy.data.enums;

            var dataList = data.dataList;
            $.extend(YT.deploy.data,{reqContentData:dataList});

            //modelType
            YT.deploy.util.initEnumSelect(enums.logModel, "model", reqContentInitData.model);

            //


            $(document).off("change", "#paramType");
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
            $(document).off("change", ":file[id='paramValue']");
            $(document).on("change", ":file[id='paramValue']", function () {
                // debugger;
                var file = $(this).prop("files")[0];
                $(this).parent().next("#fileNameText").html(file.name);
            });


            //添加header
            $(document).off("click", "button[id='btnAddHeader']");
            $(document).on("click","button[id='btnAddHeader']",function () {
                var $tr = $("#tbReqHeader").find("tr[name='dataItem']").first().clone();
                $tr.show();
                $("#tbReqHeader").append($tr);
            });

            //添加reqData
            $(document).off("click", "button[id='btnAddReqData']");
            $(document).on("click","button[id='btnAddReqData']",function () {
                var $tr = $("#tbReqData").find("tr[name='dataItem']").first().clone();
                $tr.show();
                $("#tbReqData").append($tr);
            });

            //上移
            $(document).off("click", "a[name='itemArrowUp']");
            $(document).on("click", "a[name='itemArrowUp']", function () {
                var $thisTr = $(this).parents("tr");
                var $prevTr = $thisTr.prev();
                $thisTr.after($prevTr);
                // $prevTr.before($thisTr);
            });

            //下移
            $(document).off("click", "a[name='itemArrowDown']");
            $(document).on("click", "a[name='itemArrowDown']", function () {
                var $thisTr = $(this).parents("tr");
                var $nextTr = $thisTr.next();
                $nextTr.after($thisTr);
            });


            //删除
            $(document).off("click", "a[name='itemRemove']");
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

            //左侧点击
            $(document).off("click", "a[name='itemUrl']");
            $(document).on("click","a[name='itemUrl']",function(){
               var dataId = $(this).attr("data");
                var dataList = YT.deploy.data.reqContentData;
                for(var key in dataList){
                    var data = dataList[key];
                    if(data.id == dataId){
                        YT.deploy.reqContent.initLeftPanel(data);
                        debugger;
                        break;
                    }
                }
            });

            //请求
            $(document).off("click", "button[id='btnRequest']");
            $(document).on("click","button[id='btnRequest']",function () {

                // var params = YT.deploy.util.getFormParams("#reqContentForm");

                var reqUrl = $("#reqUrl").val();
                //get headers
                var headers = {};
                var headerList = [];
                var formData = new FormData();
                $("#tbReqHeader").find("tr[name='dataItem']").each(function (index, item) {
                    var code = $(item).find("input[id='headerKey']").val();
                    // debugger;
                    if (!code) {
                        return true;
                    }
                    var value = $(item).find("input[id='headerValue']").val();
                    // debugger;
                    formData.append("headerList["+(index-1)+"].key",code);
                    formData.append("headerList["+(index-1)+"].value",value);
                    // formData.append("headerList[0]."+code, value);
                    var dataMap = {key:code,value:value};
                    headerList.push(dataMap);
                });
                //end

                //get reqParam
                // var reqDatas = {};
                var dataList = [];
                var itemIndex = 0;
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
                    } else {
                        value = $paramValue.val();
                    }
                    formData.append("dataList["+itemIndex+"].key",code);
                    formData.append("dataList["+itemIndex+"].value",value);
                    itemIndex++;
                    var dataMap = {key:code,value:value};
                    dataList.push(dataMap);
                });
                //end


                formData.append("url",reqUrl);
                formData.append("appName",$("#appName").val());
                formData.append("model",$("#model").val());
                // formData.append("headerList",headerList);
                // formData.append("dataList",dataList);
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
                        var d = data.data;
                        $.get("/req/resBlock.html", function (source) {
                            var render = template.compile(source);
                            var resHeaderList = [];
                            for(var key in d.resHeaderList){
                                var val = d.resHeaderList[key];
                                resHeaderList.push({key:key,value:val});
                            }
                            d.resHeaderList = resHeaderList;
                            var html = render(d);
                            $("#resBlock").html(html);
                        });
                        // debugger;
                    },
                    error: function (xhr, status, err) {
                        debugger;
                    },

                });

            });

        },
    }
    $.extend(YT.deploy, common);


    YT.deploy.reqContent = {

        success_register: function (param) {
            debugger;
        },


        initLeftPanel:function(data){
            debugger;
            var d = data || {};
            var reqHeaderList = [];
            if(d.reqHeader){
                var jsonObjReq = JSON.parse(d.reqHeader);
                for(var key in jsonObjReq){
                    var val = jsonObjReq[key];
                    reqHeaderList.push({key:key,value:val});
                }
            }
            d.reqHeaderList = reqHeaderList;

            var reqDataList = [];
            if(d.reqData){
                var jsonObjReq = JSON.parse(d.reqData);
                for(var key in jsonObjReq){
                    var val = jsonObjReq[key];
                    reqDataList.push({key:key,value:val});
                }
            }
            d.reqDataList = reqDataList;

            $.get("/req/reqBlock.html", function (source) {
                var render = template.compile(source);
                var html = render(d);
                $("#reqBlock").html(html);
            });
            

            if(d.resHeader){
                var resHeaderList = [];
                var jsonObjReq = JSON.parse(d.resHeader);
                for(var key in jsonObjReq){
                    var val = jsonObjReq[key];
                    resHeaderList.push({key:key,value:val});
                }
                d.resHeaderList = resHeaderList;
            }
            $.get("/req/resBlock.html", function (source) {
                var render = template.compile(source);
                var html = render(d);
                $("#resBlock").html(html);
            });
        }
    }

})(window.YunTao);

