Date.prototype.format = function (format) {
    var o = {
        "M+": this.getMonth() + 1, //month
        "d+": this.getDate(), //day
        "h+": this.getHours(), //hour
        "m+": this.getMinutes(), //minute
        "s+": this.getSeconds(), //second
        "q+": Math.floor((this.getMonth() + 3) / 3), //quarter
        "S": this.getMilliseconds()
        //millisecond
    };
    if (/(y+)/.test(format))
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(format))
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
    return format;
};

Date.prototype.formatHM = function () {
    return this.format('h:mm');

};

Date.prototype.formatYMD = function () {
    return this.format('yyyy-MM-dd');

};
Date.prototype.formatYM = function () {
    return this.format('yyyy-MM');

};
Date.prototype.formatYMDHMS = function () {
    return this.format('yyyy-MM-dd hh:mm:ss');

};
$(function () {
    /**
     * 将form表单元素的值序列化成对象
     */
    $.getAnd = function () {
        return "&";
    };
    /**
     * 将form表单元素的值序列化成对象
     */
    $.serializeObject = function (form) {
        var o = {};
        $.each(form.serializeArray(), function (index) {
            if (o[this['name']]) {
                o[this['name']] = o[this['name']] + "," + this['value'];
            } else {
                o[this['name']] = this['value'];
            }
        });
        return o;
    };

    (function () {
        function fullScreen() {
            var h = window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight;
            var content = document.getElementById("biz_container");
            if (content) {
                content.style.minHeight = (h - 240) + "px";
            }
        }

        fullScreen();
        $(window).resize(function () {
            fullScreen();
        });
    })();

    (function (jQuery) {
        var $ = jQuery;
        SingleSelect = function () {
        };
        SingleSelect.prototype = {
            constructor: SingleSelect,
            setSelected: function ($which, checked) {
                if (checked) {
                    $which.closest(".option_group").find(".input_option").removeClass("radio_on").addClass("radio_off");
                    $which.removeClass("radio_off").addClass("radio_on");
                    $which.closest(".option_group").find("option:selected").removeAttr("selected");
                    $which.closest(".option_group").find("option[value='" + $which.attr("option_value") + "']").attr("selected", true);
                } else {
                    $which.removeClass("text_radio_off").removeClass("radio_on").addClass("radio_off");
                    $which.closest(".option_group").find("option[value='" + $which.attr("radio_value") + "']").removeAttr("selected");
                }
            },
            init: function ($thiz, option) {
                singleSelect = this;
                this.option = option;
                $option_group = $thiz;
                if ($option_group.closest(".jQuery_template").size() > 0 || $option_group.attr("inited")) {
                    return;
                }
                $option_group.attr("inited", "1");
                var selectHtml = [];
                var selectName = $option_group.attr("selectName");
                selectHtml.push("<select name='" + selectName + "' class= 'hide'>");
                $option_group = $thiz;
                $option_group.find(".input_option").each(
                    function (index, input_option) {
                        var $input_option = $(input_option);
                        var radio_value = $input_option.attr("option_value") || '';
                        var radio_text = $input_option.attr("option_text") || '';
                        var selected = $input_option.attr("selected") ? true : false;
                        selectHtml.push("<option value='" + radio_value + "'   " + (selected ? "selected='selected'" : '') + ">" + radio_text + "</option>");
                        singleSelect.setSelected($input_option, selected);
                        $input_option.click(function () {
                            singleSelect.setSelected($(this), true);
                        });
                    });
                selectHtml.push("</select>");
                $option_group.append(selectHtml.join(''));
            }
        };
        $.fn.singleSelect = function (option, val) {
            var defaults = {};
            var opts = $.extend(defaults, option);
            var methodReturn, //
                $set = this.each(function () {
                    var $this = $(this), data = $this.data('singleSelect'), options = typeof option === 'object' && option;
                    if (!data) {
                        $this.data('singleSelect', (data = new SingleSelect()));
                        data.init($(this), $.extend({}, $.fn.singleSelect.defaults, options));
                    }
                    if (typeof option === 'string')
                        methodReturn = data[option](val);
                });
            return (methodReturn === undefined) ? $set : methodReturn;
        };
        $.fn.singleSelect.defaults = {};
    })(jQuery);


    (function (jQuery) {
        var $ = jQuery;
        SingleTextSelect = function () {
        };
        SingleTextSelect.prototype = {
            constructor: SingleTextSelect,
            setSelected: function ($which, selected) {
                if (selected) {
                    $which.closest(".text_option_group").find(".text_option").removeClass("text_radio_on").addClass("text_radio_off");
                    $which.closest(".text_option_group").find("option:selected").removeAttr("selected");
                    $which.removeClass("text_radio_off").addClass("text_radio_on");
                    $which.closest(".text_option_group").find("option[value='" + $which.attr("option_value") + "']").attr("selected", true);
                } else {
                    $which.removeClass("text_radio_off").removeClass("text_radio_on").addClass("text_radio_off");
                    $which.closest(".text_option_group").find("option[value='" + $which.attr("option_value") + "']").removeAttr("selected");
                }
            },
            init: function ($thiz, option) {
                singleTextSelect = this;
                this.option = option;
                $radio_group = $thiz;
                if ($radio_group.closest(".jQuery_template").size() > 0 || $radio_group.attr("inited")) {
                    return;
                }
                $radio_group.attr("inited", "1");
                var selectHtml = [];
                var selectName = $radio_group.attr("selectName");
                selectHtml.push("<select name='" + selectName + "' class= 'hide'>");
                $radio_group = $thiz;
                $radio_group.find(".text_option").each(
                    function (index, input_option) {
                        var $input_option = $(input_option);
                        var option_value = $input_option.attr("option_value") || '';
                        var option_text = $input_option.attr("option_text") || '';
                        var selected = $input_option.attr("selected") ? true : false;
                        selectHtml.push("<option value='" + option_value + "' " + (selected ? "selected='selected'" : '') + ">" + option_text + "</option>");
                        singleTextSelect.setSelected($input_option, selected);
                        $input_option.click(function () {
                            singleTextSelect.setSelected($(this), true);
                        });
                    });
                selectHtml.push("</select>");
                $radio_group.append(selectHtml.join(''));
            }
        };
        $.fn.singleTextSelect = function (option, val) {
            var defaults = {};
            var opts = $.extend(defaults, option);
            var methodReturn, //
                $set = this.each(function () {
                    var $this = $(this), data = $this.data('singleTextSelect'), options = typeof option === 'object' && option;
                    if (!data) {
                        $this.data('singleTextSelect', (data = new SingleTextSelect()));
                        data.init($(this), $.extend({}, $.fn.singleTextSelect.defaults, options));
                    }
                    if (typeof option === 'string')
                        methodReturn = data[option](val);
                });
            return (methodReturn === undefined) ? $set : methodReturn;
        };
        $.fn.singleTextSelect.defaults = {};
    })(jQuery);

    (function (jQuery) {
        var $ = jQuery;
        MultiSelect = function () {
        };
        MultiSelect.prototype = {
            constructor: MultiSelect,
            setChecked: function ($which, checked) {
                if (checked) {
                    $which.removeClass("checkbox_off").addClass("checkbox_on");
                    $which.append("<input class='hide' type='text' name='" + $which.attr("toggleInput_name") + "'  value='" + $which.attr("toggleInput_value") + "' />")
                } else {
                    $which.removeClass("checkbox_on").addClass("checkbox_off");
                    $which.find("[type=text]").remove();
                }
            },
            init: function ($thiz, option) {
                multiSelect = this;
                var $toggleSelect = $thiz;
                this.option = option;
                if ($toggleSelect.closest(".jQuery_template").size() > 0 || $toggleSelect.attr("inited")) {
                    return;
                }
                $toggleSelect.attr("inited", "1");
                var toggleInput_name = $toggleSelect.attr("toggleInput_name") || '';
                var toggleInput_value = $toggleSelect.attr("toggleInput_value") || '';
                var selected = $toggleSelect.attr("selected") ? true : false;
                multiSelect.setChecked($toggleSelect, selected);
                $toggleSelect.click(function () {
                    multiSelect.setChecked($(this), $(this).find("[type=text]").size() > 0 ? false : true);
                });
            }
        };
        $.fn.multiSelect = function (option, val) {
            var defaults = {};
            var opts = $.extend(defaults, option);
            var methodReturn, //
                $set = this.each(function () {
                    var $this = $(this), data = $this.data('multiSelect'), options = typeof option === 'object' && option;
                    if (!data) {
                        $this.data('multiSelect', (data = new MultiSelect()));
                        data.init($(this), $.extend({}, $.fn.multiSelect.defaults, options));
                    }
                    if (typeof option === 'string')
                        methodReturn = data[option](val);
                });
            return (methodReturn === undefined) ? $set : methodReturn;
        };
        $.fn.multiSelect.defaults = {};
    })(jQuery);


    $(".option_group").singleSelect({});
    $(".text_option_group ").singleTextSelect({});
    $(".toggleSelect").multiSelect({});

    $.iphone = function (html) {
        if ($("#iphone_preview").size() == 0) {
            $(document.body).append('<style type="text/css"> #iphone_preview_content p:first-child {margin-top:0px;} #iphone_preview_content p:last-child {margin-bottom:0px;} </style>');
            $(document.body).append('<div id="iphone_preview" style=\'z-index: 99999999999;background: no-repeat url("_resources/images/busniess/iphone6.png");display: inline-block;height: 613px; background-position: 1px -118px;width: 306px;position: fixed;top: 30px;left: 50%;  margin-left: -150px;\'><div onclick="$(\'#iphone_preview\').hide();" style=\'-webkit-border-radius: 500px;-moz-border-radius: 500px;border-radius: 500px;background: no-repeat url("_resources/images/busniess/closed-pop.png") white;height: 30px;width: 30px;position: relative;top: 0px;float: right;right: -22px;cursor: pointer;\'></div><div id="iphone_preview_content" class="custom_scroll" style=\'padding:4px;background: white; height: 458px; width: 248px; display: inline-block; position: relative; top: 77px; left: 25px; overflow-x: hidden; overflow-y: auto; word-break: break-all; word-wrap: break-word;\'> </div> </div>');
        }
        $("#iphone_preview").show();
        $("#iphone_preview_content").html(html);
    };
    $.joinAttr = function (objs, attrName) {
        var attrValue = [];
        objs.each(function (i, obj) {
            attrValue.push($(obj).attr(attrName));
        });
        return attrValue.join(',');
    };
    $.joinText = function (objs) {
        var attrValue = [];
        objs.each(function (i, obj) {
            attrValue.push($(obj).text());
        });
        return attrValue.join(',');
    };

    $.ajaxMultiForm = function (options) {
        var data = new FormData();
        data.append("token", window.token);
        for (var key in options.data) {
            data.append(key, options.data[key]);
        }
        $.ajax(jQuery.extend(options, {
            url: options.url,
            type: 'POST',
            contentType: false,
            processData: false,
            data: data
        }));
    };


    $.initUploadImag = function ($me) {
        var image_url = $me.attr("image_url") || '';
        var image_key = $me.attr("image_key") || '';

        if ($me.attr("inited")) {
            return;
        }
        $me.attr("inited", 1);
        $me.html('');
        if (image_url) {
            $me.removeClass("ic_upload_img");
            $me.append('<img src="' + image_url + '"  style="max-height: 100px;" /><input type="file"  class="upload_img_file_empty"/><input type="text"  value="' + image_key + '" name="' + ($me.attr("key_name") || '') + '"  class="input_key hide" /><input type="text"  value="' + image_url + '" name="' + $me.attr("image_name") + '"  class="hide input_url ' + ($me.attr("addtionClass") || '' ) + '"/>').wrap('<span class="upload_img_container" style="display: block;"></span>');
        } else {
            $me.append('<img src=""  style="max-height: 100px;" /><input type="file" class="upload_img_file"  /><input type="text"  value="' + image_key + '" name="' + ($me.attr("key_name") || '') + '" class="input_key hide" /><input type="text" name="' + $me.attr("image_name") + '"  class="hide input_url ' + ($me.attr("addtionClass") || '' ) + '" />').wrap('<span class="upload_img_container"  style="display: block;"></span>');
        }

        $('.upload_image_tag img').live('click', function () {
            var $container = $(this).closest(".upload_img_container");
            var $input = $container.find(".input_url");
            var $inputKey = $container.find(".input_key");

            var $viewImg = $(this);
            var $displayer = $container.find(".upload_image_tag");
            var $files = $container.find("[type=file]");

            $files.addClass("upload_img_file").removeClass("upload_img_file_empty");
            $input.val('');
            $inputKey.val('');
            $displayer.addClass("ic_upload_img");
            $viewImg.get(0).src = '';
            $viewImg.attr("title", '')
        });

        $('.upload_image_tag [type=file]').live('change', function () {
            var $files = $(this);
            var files = $(this).get(0).files;
            var $container = $(this).closest(".upload_img_container");
            var $input = $container.find(".input_url");
            var $inputKey = $container.find(".input_key");

            var $displayer = $(this).closest(".ic_upload_img");

            var $viewImg = $container.find('img');
            var file = files[0];
            if (files.length <= 0) {
                return;
            }
            $.ajaxMultiForm({
                url: Server_Info.rootPath + 'uploadImage?_csrf=' + $("#_csrf").val(),
                data: {
                    'imag': file
                },
                beforeSend: function () {
                    $("#dialogMask").show();
                    $("#loading").show();
                },
                complete: function () {
                    $("#dialogMask").hide();
                    $("#loading").hide();
                },
                success: function (response) {
                    if (response.success) {
                        if (response.data && response.data.url) {
                            $input.val(response.data.url);
                            $inputKey.val(response.data.id);
                            $viewImg.attr("src", response.data.url);
                            $files.removeClass("upload_img_file").addClass("upload_img_file_empty");
                            $displayer.removeClass("ic_upload_img");
                            //$files.get(0).style.position = 'relative';
                        }
                    } else {
                        if (response.message) {
                            alert(response.message);
                        }
                    }
                }
            });
            $viewImg.attr("title", file.name);
            /* var reader = new FileReader();
             reader.onload = function(evt) {
             $viewImg.get(0).src = evt.target.result;
             $files.removeClass("upload_img_file").addClass("upload_img_file_empty");
             $displayer.removeClass("ic_upload_img");
             }
             reader.readAsDataURL(file);*/

        });
    };
    $(".upload_image_tag").each(function (index, d) {
        $.initUploadImag($(d));
    });
    if ($.validator && $.validator.setDefaults && typeof $.validator.setDefaults == 'function') {
        $.validator.setDefaults({
            errorClass: 'formValidFailLineStyle',//formValidFailFloatStyle or formValidFailLineStyle
            errorElement: "span"
        });
    }
});
