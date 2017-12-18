/**
 * Created by shan on 2016/4/7.
 */

$(document).ready(function () {
    /**
     * 对日期进行格式化，
     * @param date 要格式化的日期
     * @param format 进行格式化的模式字符串
     *     支持的模式字母有：
     *     y:年,
     *     M:年中的月份(1-12),
     *     d:月份中的天(1-31),
     *     h:小时(0-23),
     *     m:分(0-59),
     *     s:秒(0-59),
     *     S:毫秒(0-999),
     *     q:季度(1-4)
     * @return String
     * @author yanis.wang
     * @see http://yaniswang.com/frontend/2013/02/16/dateformat-performance/
     */
    template.helper('dateFormat', function (time, format) {
        if(!time){
            return null;
        }

        ////debugger;
        var date = parseInt(time);

        date = new Date(date); //新建日期对象
        // alert(date.getMonth());
        /*日期字典*/
        var map = {
            "M": date.getMonth() + 1, //月份
            "d": date.getDate(), //日
            "h": date.getHours(), //小时
            "m": date.getMinutes(), //分
            "s": date.getSeconds(), //秒
            "q": Math.floor((date.getMonth() + 3) / 3), //季度
            "S": date.getMilliseconds() //毫秒
        };

        /*正则替换*/
        format = format.replace(/([yMdhmsqS])+/g, function(all, t){
            var v = map[t];
            if(v !== undefined){
                if(all.length > 1){
                    v = '0' + v;
                    v = v.substr(v.length-2);
                }
                return v;
            }
            else if(t === 'y'){
                return (date.getFullYear() + '').substr(4 - all.length);
            }
            return all;
        });

        /*返回自身*/
        return format;
    });



    template.helper('text', function (key,type){
        var enums = YunTao.deploy.data.enums;
        var typeDataList = enums[type];
        if(!typeDataList){
            return null;
        }
        for (var i = 0; i < typeDataList.length; i++) {
            var data = typeDataList[i];
            if(data.code == key){
                return data.description;
            }
        }
    });

    template.helper('moneyFormat', function (money){
        if(money){
            return (money/100).toFixed(2);
        }

    });

    template.helper('remote', function (id,param){
        if(!id){
            return null;
        }
        var url = "";
        var params = param.split(",");
        var urlType = params[0];
        var showText = params[1];
        if(urlType == "findUserById"){
            url = "/user/findById";
        }else if(urlType == "findTemplateById"){
            url = "/atTemplate/findById";
        }else{
            return null;
        }
        var result = null;
        $.ajaxSetup({ async: false });
        YunTao.deploy.util.reqGet(url,{id:id},function(d){
            var data = d.data;
            if(data){
                result = data[showText];
            }
        });
        $.ajaxSetup({ async: true });
        return result;
    });

    // window.alert = function(msg,fun,closeAll){
    //     //debugger;
    //     // closeAll = closeAll | false;
    //     bootbox.alert({
    //         message: msg,
    //         callback: function(){
    //             if(fun instanceof Function){
    //                 fun();
    //             }
    //             if(closeAll){
    //                 bootbox.hideAll();
    //             }
    //         }
    //     });
    // };
    //
    // window.confirm = function(msg,fun_confirm){
    //     bootbox.confirm({
    //         message: msg,
    //         buttons: {
    //             confirm: {
    //                 label: '确认',
    //                 className: 'btn-success'
    //             },
    //             cancel: {
    //                 label: '取消',
    //                 className: 'btn-danger'
    //             }
    //         },
    //         callback: function(result){
    //             if(fun_confirm instanceof Function){
    //                 if(result){
    //                     fun_confirm();
    //                 }
    //             }
    //         }
    //     })
    //     //event.preventDefault();
    // }


});