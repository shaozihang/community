/*!
 * Cropper v3.0.0
 */

layui.config({
    base: '/cropper/' //layui自定义layui组件目录
}).define(['jquery','layer','cropper'],function (exports) {
    var $ = layui.jquery
        ,layer = layui.layer;
    var html =
        "<div class=\"layui-fluid showImgEdit\" style=\"display: none;\">\n" +
        "    <div class=\"layui-row layui-col-space15\">\n" +
        "        <div class=\"layui-col-xs12\" style='margin-top: 10px'>\n" +
        "            <div class=\"readyimg\" style=\"height:504px;background-color: rgb(247, 247, 247);\">\n" +
        "                <img src=\"\" >\n" +
        "            </div>\n" +
        "        </div>\n" +
        "    </div>\n" +
        "    <div class=\"layui-row layui-col-space15\">\n" +
        "        <div class=\"\" style='float: right;'>\n" +
        "           <div class=\"layui-input-inline layui-btn-container\" style=\"width: auto;\">\n" +
        "               <label for=\"cropper_avatarImgUpload\" class=\"layui-btn layui-btn-normal\">选择图片\n" +
        "               </label>\n" +
        "               <input class=\"layui-upload-file\" id=\"cropper_avatarImgUpload\" type=\"file\" accept=\"image/*\" value=\"选择图片\" name=\"file\">\n" +
        "               <button class=\"layui-btn saveImg\" cropper-event=\"confirmSave\" type=\"button\" style='margin-right: 0px;'> 保存修改</button>\n" +
        "           </div>\n" +
        "        </div>\n" +
        "    </div>\n" +
        "\n" +
        "</div>";
    var obj = {
        render: function(e){
            $('body').append(html);
            var self = this,
                elem = e.elem,
                saveW = e.saveW,
                saveH = e.saveH,
                mark = e.mark,
                area = e.area,
                url = e.url,
                done = e.done;

            var content = $('.showImgEdit')
                ,image = $(".showImgEdit .readyimg img")
                ,preview = '.showImgEdit .img-preview'
                ,file = $(".showImgEdit input[name='file']")
                , options = {aspectRatio: mark,preview: preview,viewMode:1};

            $(elem).on('click',function () {
                layer.open({
                    type: 1
                    , content: content
                    , area: area
                    , title:'请选择合适的区域作为头像'
                    , skin: 'openAvatar'
                    , success: function () {
                        image.cropper(options);
                    }
                    , cancel: function (index) {
                        layer.close(index);
                        image.cropper('destroy');
                    }
                });
            });
            $(".saveImg").on('click',function () {
                var event = $(this).attr("cropper-event");
                var imgVal = $(".readyimg img:first-child").attr('src');
                if(!imgVal){
                    return done(-1);
                }
                //监听确认保存图像
                if(event === 'confirmSave'){
                    image.cropper("getCroppedCanvas",{
                        width: saveW,
                        height: saveH
                    }).toBlob(function(blob){
                        var formData=new FormData();
                        formData.append('file',blob,'head.jpg');
                        $.ajax({
                            method:"post",
                            url: url, //用于文件上传的服务器端请求地址
                            data: formData,
                            processData: false,
                            contentType: false,
                            success:function(result){
                                if(result.code == 200){
                                    return done(200);
                                }else if(result.code == 0){
                                    return done(0);
                                }else if(result.code == -1){
                                    return done(-1);
                                }
                            }
                        });
                    },"image/jpeg",0.8);
                }
            });
            //文件选择
            file.change(function () {
                var r= new FileReader();
                var f=this.files[0];
                if(f == undefined){
                    return;
                }
                var size = f.size;
                var name = f.name;
                var index= name.lastIndexOf(".");
                var type = name.substr(index+1);
                if(!/(gif|GIF|jpg|JPG|jpeg|JPEG|png|PNG)$/.test(type)){
                    layer.msg("选择的图片包含不支持的格式",{icon: 2});
                    $("#cropper_avatarImgUpload").val("");
                }else if(size > 1024*1024){
                    layer.msg("图片不能超过1.00MB",{icon: 2});
                    $("#cropper_avatarImgUpload").val("");
                }else {
                    r.readAsDataURL(f);
                    r.onload=function (e) {
                        image.cropper('destroy').attr('src', this.result).cropper(options);
                    };
                }
            });
        }

    };
    exports('croppers', obj);
});