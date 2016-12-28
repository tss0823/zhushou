$(document).ready(function () {

    // $("#btnSearch").click(function () {
    //     $("#listForm").submit();
    // });


    $(document).on("click", "#btnClear", function () {
        var jqFormObj = $("form:first");
        if (jqFormObj == null) {
            jqFormObj = $(this).attr("form-id");
        }
        $(jqFormObj).find(":text").each(function (index, item) {
            $(item).val("");
        });
        $(jqFormObj).find(":hidden").each(function (index, item) {
            $(item).val("");
        });
        $(jqFormObj).find("select").each(function (index, item) {
            $(item).find("option:first").prop("selected", true);
        });
        //清空个性数据 //TODO 目前只针对查询
        var actionId = jqFormObj.attr("actionId");
        // debugger;
        var authRes = appData.authMap[actionId];
        YunTao.deploy.userDataProcess.clear(authRes.tplUrl+"_queryData");

    });


    //ener触发
    // $(document).keypress(function (e) {
    //     if (e.which == 13) {
    //         $("input[enter='true']").trigger("click");
    //     }
    // });

    // basePath = $("#basePath").val();
    $(document).on("click", "#chkRow", function () {
        var checked = $(this).find(":checkbox").prop("checked");
        $(this).find(":checkbox").prop("checked", !checked);
    });

    $(document).on("click", "#chkFormAll", function () {
        var checked = $(this).prop("checked");
        $(":checkbox[id='chkForm']").each(function (index, item) {
            $(item).prop("checked", checked);
        });
    });


});


//获得提交表单数据
function getFormParams(id) {
    var sel = id || "form";
    var paramsArray = $(sel).serializeArray();
    var params = {};
    while (paramsArray.length > 0) {
        var data = paramsArray.pop();
        var oldValue = params[data.name];
        if (typeof(oldValue) != "undefined") {  //存在数据
            if (typeof(oldValue) == "object") {
                oldValue.push(data.value); //add
            } else {
                var arr = [];
                arr.push(oldValue);
                arr.push(data.value);
                oldValue = arr;
            }
            params[data.name] = oldValue;
        } else {
            params[data.name] = data.value;
        }

    }
    return params;
}
