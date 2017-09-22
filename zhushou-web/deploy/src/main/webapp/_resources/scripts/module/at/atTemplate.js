/**
 *
 * Created by shan on 2016/4/7.
 */
(function (YT) {
    if (!YT.deploy.atTemplate) {
        YT.deploy.atTemplate = {};
    }

    var common = {

        formId: "templateForm",

        route_callback: function (d) {
            console.log("template list after render call");
            //组件初始化之后

            var data = d.data;

            this.formId = $("form:first").attr("id");

            YT.deploy.util.initEnumSelect("logModel", "model", data.model);

            //
            YT.deploy.atTemplate.init();

            //tab
            $("div[id='tabs']").tabs();

            //调出http 执行详情
            $("a[name='enterHttpExecView']").click(function () {
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
            $("div[name='enterHttpExecView']").click(function () {
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
            $("a[name='btnResDataView']").click(function () {
                var dataId = $(this).attr("data");
                if(!dataId){
                    alert("没有返回数据");
                    return;
                }
                var month = moment().format("YYYY.MM");
                YT.deploy.appLog.openResponseDialog(dataId,month,$("#model").val());
            });

            $("a[name='btnActiveDel']").click(function () {
                var dataId = $(this).attr("data");
                YT.deploy.atTemplate.delActive(dataId);
            });

            $("a[name='btnActiveInsert']").click(function () {
                var id = $("#templateId").val();
                var orderIndex = $(this).attr("orderIndex");
                // debugger;
                YT.deploy.atTemplate.openActiveCollectWin(id,parseInt(orderIndex)-1);
            });

            $("#btnQuery").click(function () {
                var pageNum = 1;
                // var pageSize = $("#pageSize").val();
                YT.deploy.atTemplate.query(pageNum);
            });

            $("#btnNewTemplate").click(function () {
                YT.deploy.atTemplate.openNewWin();
            });

            $("#btnVarQuery").click(function () {
                YT.deploy.atTemplate.queryVarList();
            });



            //保存
            $(document).off("click", "#btnSave");
            $(document).on("click", "#btnSave", function () {
                YT.deploy.atTemplate.save();
            });

            //保存活动采集
            $(document).off("click", "#btnSaveActiveCollect");
            $(document).on("click", "#btnSaveActiveCollect", function () {
                YT.deploy.atTemplate.saveActiveCollect();
            });

            // //保存活动列表
            // $(document).off("click", "#btnSaveActiveCollect");
            // $(document).on("click", "#btnSaveActiveCollect", function () {
            //     YT.deploy.atTemplate.saveActiveCollect();
            // });

            //活动执行
            $(document).off("click", "#btnActiveStart");
            $(document).on("click", "#btnActiveStart", function () {
                YT.deploy.atTemplate.activeStart();
            });

            //取消
            $(document).off("click", "#btnCancel");
            $(document).on("click", "#btnCancel", function () {
                $(".bootbox-close-button").trigger("click");
            });

            //enterEdit
            $("a[name='enterEdit']").click(function () {
                var id = $(this).attr("data");
                YT.deploy.atTemplate.openEditWin(id);
            });

            //enter collect active
            $("a[name='enterActiveCollect']").click(function () {
                var id = $(this).attr("data");
                YT.deploy.atTemplate.openActiveCollectWin(id);
            });

            //enter active edit
            $("a[name='enterActiveEdit']").click(function () {
                var id = $(this).attr("data");
                YT.deploy.atTemplate.enterActiveEditWin(id);
            });

            //enter collect start
            $("a[name='enterStart']").click(function () {
                var id = $(this).attr("data");
                YT.deploy.atTemplate.enterStartWin(id);
            });

            //enter active execute his
            $("a[name='enterHis']").click(function () {
                var id = $(this).attr("data");
                YT.deploy.atTemplate.queryProcessInstList(id);
            });

            $("a[name='btnDelTemplate']").click(function () {
                var id = $(this).attr("data");
                YT.deploy.atTemplate.delTemplate(id);
            });

            //分页信息init
            YT.deploy.util.paginationInit(d.data, YT.deploy.atTemplate.query);

            //活动编辑操作
            if (YT.deploy.formId == "activeEditForm") {

                var $form = $("#" + YT.deploy.formId);
                $form.find("select[name='dataType']").each(function (index, item) {
                    var id = $(this).prop("id");
                    var value = $(this).attr("data");
                    YT.deploy.util.initEnumSelect("paramDataType", id, value);
                });

                //list start
                //添加
                $(document).off("click", "button[id='btnAddActive']");
                $(document).on("click", "button[id='btnAddActive']", function () {
                    // debugger;
                    var id = $("#templateId").val();
                    var orderIndex = 1;
                    if(data.activeVoList){
                        orderIndex = data.activeVoList.length+1;
                    }
                    // debugger;
                    YT.deploy.atTemplate.openActiveCollectWin(id,orderIndex);
                });

                //上移
                $(document).off("click", "a[name='itemArrowUpList']");
                $(document).on("click", "a[name='itemArrowUpList']", function () {
                    var $thisTr = $(this).parents("tbody[name='tbContent']");
                    // debugger;
                    var $prevTr = $thisTr.prev();
                    $thisTr.after($prevTr);
                    // $prevTr.before($thisTr);
                });

                //下移
                $(document).off("click", "a[name='itemArrowDownList']");
                $(document).on("click", "a[name='itemArrowDownList']", function () {
                    var $thisTr = $(this).parents("tbody[name='tbContent']");
                    var $nextTr = $thisTr.next();
                    $nextTr.after($thisTr);
                });

                //保存排序
                $(document).off("click", "button[id='btnActiveOrderSave']");
                $(document).on("click", "button[id='btnActiveOrderSave']", function () {
                    // debugger;
                    var id = $("#templateId").val();
                    YT.deploy.atTemplate.saveActiveSort(id);
                });
                //list end

                //添加
                $(document).off("click", "button[id='btnAddParam']");
                $(document).on("click", "button[id='btnAddParam']", function () {
                    // debugger;
                    var id = $(this).attr("data");
                    var $table = $("#paramBlock_" + id).find("#tbParamContent");
                    var $tr = $table.find("tr[name='dataItem']").first().clone();
                    $tr.show();
                    $table.append($tr);
                });

                //上移
                $(document).off("click", "a[name='itemArrowUp']");
                $(document).on("click", "a[name='itemArrowUp']", function () {
                    var $thisTr = $(this).parents("tr[name='dataItem']");
                    var $prevTr = $thisTr.prev();
                    $thisTr.after($prevTr);
                    // $prevTr.before($thisTr);
                });

                //下移
                $(document).off("click", "a[name='itemArrowDown']");
                $(document).on("click", "a[name='itemArrowDown']", function () {
                    var $thisTr = $(this).parents("tr[name='dataItem']");
                    var $nextTr = $thisTr.next();
                    $nextTr.after($thisTr);
                });


                //删除
                $(document).off("click", "a[name='itemRemove']");
                $(document).on("click", "a[name='itemRemove']", function () {
                    if (!confirm("您确要删除吗？")) {
                        return false;
                    }
                    // var $table = $(this).parents("#tbParamContent");
                    // if($table.find("tr[name='dataItem']:visible").length == 1){
                    //     alert("必须保留一条记录");
                    //     return;
                    // }
                    $(this).parents("tr[name='dataItem']").remove();

                });


                $(document).off("click", "button[id='btnParamSave']");
                $(document).on("click", "button[id='btnParamSave']", function () {
                    // debugger;
                    var id = $(this).attr("data");
                    var $table = $("#paramBlock_" + id).find("#tbParamContent");
                    var templateId = $("#templateId").val();
                    var params = {templateId:templateId,id:id};
                    var arrIndex = 0;
                    $table.find("tr[name='dataItem']").each(function (index, item) {
                        var code = $(item).find("input[id='code']").val();
                        if (!code) {
                            return true;
                        }
                        params["parameterList[" + arrIndex + "].code"] = code;
                        params["parameterList[" + arrIndex + "].name"] = $(item).find("input[id='name']").val();
                        params["parameterList[" + arrIndex + "].dataValue"] = $(item).find("input[id='dataValue']").val();
                        params["parameterList[" + arrIndex + "].dataType"] = $(item).find("select[name='dataType']").val();
                        arrIndex++;
                    });

                    YT.deploy.util.reqPost("/atTemplate/updateActive", params, function (d) {
                        alert("保存成功");
                    });
                });


                //添加 header
                $(document).off("click", "button[id='btnAddHeader']");
                $(document).on("click", "button[id='btnAddHeader']", function () {
                    // debugger;
                    var id = $(this).attr("data");
                    var $table = $("#headerBlock_" + id).find("#tbParamContent");
                    var $tr = $table.find("tr[name='dataItem']").first().clone();
                    $tr.show();
                    $table.append($tr);
                });

                $(document).off("click", "button[id='btnHeaderSave']");
                $(document).on("click", "button[id='btnHeaderSave']", function () {
                    // debugger;
                    var id = $(this).attr("data");
                    var $table = $("#headerBlock_" + id).find("#tbParamContent");
                    var templateId = $("#templateId").val();
                    var params = {templateId:templateId,id:id};
                    var arrIndex = 0;
                    var headerObj = {};
                    // debugger;
                    $table.find("tr[name='dataItem']").each(function (index, item) {
                        var key = $(item).find("input[id='key']").val();
                        if (!key) {
                            return true;
                        }
                        var value = $(item).find("input[id='value']").val();
                        headerObj[key] = value;
                        arrIndex++;
                    });

                    var headerStr = JSON.stringify(headerObj);
                    params["headerRow"] = headerStr;
                    YT.deploy.util.reqPost("/atTemplate/updateActive", params, function (d) {
                        alert("保存成功");
                    });
                });


            }

        },

    }
    $.extend(YT.deploy, common);


    YT.deploy.atTemplate = {

        openWinObj: null,

        query: function (pageNum) {
            YT.deploy.formId = "templateForm";
            var params = YT.deploy.util.getFormParams("#templateForm");
            params["pageNum"] = pageNum;
            var pageSize = $("#pageSize").val();
            params["pageSize"] = pageSize;
            var ext_data = $.extend(params, {tp_title:"模板列表"});
            YT.deploy.route("/atTemplate/list", params, "/at/template.html", ext_data);
        },

        initNewEdit: function (data) {
            //type
            // var enums = YT.deploy.data.enums;

            var $form = $("#" + YT.deploy.formId);

            YT.deploy.util.initEnumSelect("logModel", "model", data.model);
            YT.deploy.util.initEnumSelect("collectType", "type", data.type);

            //组件初始化之后
            var appList = YT.deploy.data.appList;
            // var docList = YT.deploy.data.docList;
            YT.deploy.util.initSelect(appList, "name", "name", "appName", data.appName);

            $("#type").change(function(){
                if ($(this).val() == 0) {
                    $("#blockLogIds").show();
                    $("#blockTime").hide();
                }else{
                    $("#blockLogIds").hide();
                    $("#blockTime").show();
                }
            });


            $("#" + YT.deploy.formId).find("#startTime").datetimepicker({
                lang: 'ch',
                format: 'Y-m-d H:i:s',
                todayBtn: true,
                autoclose: true,
            });
            $("#" + YT.deploy.formId).find("#endTime").datetimepicker({
                lang: 'ch',
                format: 'Y-m-d H:i:s',
                todayBtn: true,
                autoclose: true,
            });

            $("#startTime").change(function () {
                // console.log($(this).val());
                YT.deploy.atTemplate.genEndTime($(this).val());
            });
            $("#startTime").keyup(function () {
                // console.log($(this).val());
                YT.deploy.atTemplate.genEndTime($(this).val());
            });
            $("#rangeMinute").keyup(function () {
                // console.log($(this).val());
                YT.deploy.atTemplate.genEndTime($("#startTime").val());
            });

            ;


        },

        openNewWin: function () {
            var param = {tp_title: "模版创建", dataList: null};
            $.get("/at/new.html", function (source) {
                var render = template.compile(source);
                var html = render(param);
                YT.deploy.atTemplate.openWinObj = bootbox.dialog({
                    message: html,
                    width: "800px",
                });
                $(".modal-dialog").prop("style", "width:70%;height:85%")
                YT.deploy.formId = "templateNewForm";
                YT.deploy.atTemplate.initNewEdit(param);
            });
        },

        queryVarList: function () {
            var params = {};
            var ext_data = $.extend(params, {tp_title: "变量列表"});
            YT.deploy.route("/atVariable/list", params, "/at/varList.html", ext_data);
            YT.deploy.formId = "variableForm";
            // YT.deploy.atTemplate.initNewEdit(ext_data);
        },

        queryProcessInstList: function (id) {
            var params = {templateId:id};
            var ext_data = $.extend(params, {tp_title: "活动运行列表"});
            YT.deploy.route("/atProcessInst/list", params, "/at/processInstList.html", ext_data);
            YT.deploy.formId = "processInstForm";
            // YT.deploy.atTemplate.initNewEdit(ext_data);
        },

        openEditWin: function (id) {
            var param = {tp_title: "模版修改", dataList: null};
            var params = {id: id};
            YT.deploy.util.reqGet("/atTemplate/detail", params, function (d) {
                // debugger;
                param["domain"] = d.data;
                $.get("/at/edit.html", function (source) {
                    var render = template.compile(source);
                    var html = render(param);
                    YT.deploy.atTemplate.openWinObj = bootbox.dialog({
                        message: html,
                        width: "800px",
                    });
                    $(".modal-dialog").prop("style", "width:70%;height:85%")
                    YT.deploy.formId = "templateNewForm";
                    YT.deploy.atTemplate.initNewEdit(d.data);
                });
            });
        },

        delTemplate:function(id){
            if (!confirm("您确认需要删除吗？")) {
                return;
            }

            YT.deploy.util.reqPost("/atTemplate/deleteTemplate", {id:id}, function (d) {
                alert("删除成功");
                YT.deploy.atTemplate.query(1);
            });
        },

        openActiveCollectWin: function (id,orderIndex) {
            var param = {tp_title: "活动采集", templateId: id,orderIndex:orderIndex};
            $.get("/at/activeCollect.html", function (source) {
                var render = template.compile(source);
                var html = render(param);
                YT.deploy.atTemplate.openWinObj = bootbox.dialog({
                    message: html,
                    width: "800px",
                });
                $(".modal-dialog").prop("style", "width:70%;height:85%")
                YT.deploy.formId = "activeCollectForm";
                YT.deploy.atTemplate.initNewEdit(param);
            });
        },
        enterActiveEditWin: function (id) {
            var params = {id: id};
            var ext_data = $.extend(params, {tp_title: "活动编辑"});
            YT.deploy.route("/atTemplate/detail", params, "/at/activeEdit.html", ext_data);
            YT.deploy.formId = "activeEditForm";
            // YT.deploy.atTemplate.initNewEdit(ext_data);
        },

        enterStartWin: function (id) {
            var params = {id: id};
            var ext_data = $.extend(params, {tp_title: "活动开始"});
            YT.deploy.formId = "activeEditForm";
            YT.deploy.route("/atTemplate/detail", params, "/at/activeStart.html", ext_data);

        },

        enterHisWin: function (id) {
            var param = {tp_title: "活动运行历史", templateId: id};
            $.get("/at/activeCollect.html", function (source) {
                var render = template.compile(source);
                var html = render(param);
                YT.deploy.atTemplate.openWinObj = bootbox.dialog({
                    message: html,
                    width: "800px",
                });
                $(".modal-dialog").prop("style", "width:70%;height:85%")
                YT.deploy.formId = "activeCollectForm";
                YT.deploy.atTemplate.initNewEdit(param);
            });
        },

        save: function () {
            var params = YT.deploy.util.getFormParams("#templateNewForm");
            if (params["id"]) {  //update
                YT.deploy.util.reqPost("/atTemplate/updateTemplate", params, function (d) {
                    alert("修改成功");
                    $(".bootbox-close-button").trigger("click");
                    YT.deploy.atTemplate.query(1);
                });

            } else { //save
                YT.deploy.util.reqPost("/atTemplate/saveTemplate", params, function (d) {
                    alert("保存成功");
                    $(".bootbox-close-button").trigger("click");
                    YT.deploy.atTemplate.query(1);
                });
            }
        },

        saveActiveCollect: function () {
            var params = YT.deploy.util.getFormParams("#activeCollectForm");
            YT.deploy.util.reqPost("/atTemplate/collect", params, function (d) {
                alert("保存成功");
                $(".bootbox-close-button").trigger("click");
                if($("#orderIndex").val()){
                    YT.deploy.atTemplate.enterActiveEditWin(params["id"]);
                }else{
                    YT.deploy.atTemplate.query(1);
                }
            });
        },
        activeStart: function () {
            if (!confirm("您确认要执行？")) {
                return;
            }
            var params = YT.deploy.util.getFormParams("#activeStartForm");
            YT.deploy.util.reqPost("/atTemplate/start", params, function (d) {
                $("#sample-table-1").find("td[name='activeExeStatus']").html("执行中...");
                // alert("执行开始");
            });
        },

        delActive: function (id) {
            if (!confirm("您确认需要删除吗？")) {
                return;
            }
            YT.deploy.util.reqPost("/atTemplate/deleteActive", {activeId:id}, function (d) {
                alert("删除成功");
                YT.deploy.routeStackProcess.refresh();
            });
        },

        saveActiveSort: function (templateId) {
            var activeIds = [];
            $("input[id='orderActiveId']").each(function(){
                var val = $(this).val();
                activeIds.push(val);
            });

            YT.deploy.util.reqPost("/atTemplate/saveActiveSort", {templateId:templateId,activeIds:activeIds.join(",")}, function (d) {
                alert("保存排序成功");
                YT.deploy.atTemplate.enterActiveEditWin(templateId);

            });
        },

        /**
         * 生成结束时间
         * @param startTimeVal
         */
        genEndTime: function (startTimeVal) {
            debugger;
            if (startTimeVal.length != 19) {
                return;
            }
            var rangeMinute = $("#rangeMinute").val();
            if (!rangeMinute) {
                return;
            }
            // var hour = startTimeVal.substr(11,2);
            // var minute = startTimeVal.substr(14,2);
            var endTimeObj = moment(startTimeVal, "YYYY-MM-DD HH:mm").add(rangeMinute,"m");
            var endTimeVal = endTimeObj.format("YYYY-MM-DD HH:mm");
            $("#endTime").val(endTimeVal);
        },

        init: function () {
            //注册服务状态监控事件
            YT.deploy.eventProcess.addListener("test_active_http_execute", function (msgObj) {
                // debugger;
                var dataObj = JSON.parse(msgObj.data);
                var activeId = dataObj.activeId;
                var success = dataObj.success;
                debugger;
                var errShowHtml = "<strong style='color:red'>ERROR</strong>";
                var successShowHtml = "<strong style='color:green'>OK</strong>";
                $("#activeExeStatus_" + activeId).html(success ? successShowHtml : errShowHtml);
                $("#trChildItem_" + activeId).find("#result").html(dataObj.result);
                $("#trChildItem_" + activeId).find("#errMsg").html(dataObj.errMsg);
                //end
            });
        }

    }

})(window.YunTao);
