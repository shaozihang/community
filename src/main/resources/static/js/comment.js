/**
 * 提交回复
 */
function post() {
    var questionId = $("#question_id").val();
    var content = $(".layui-textarea").val();
    comment2target(questionId,1,content);
}

function comment(e) {
    var commentId = e.getAttribute("data-id");
    var content = $("#input-"+commentId).val();
    comment2target(commentId,2,content);
}

function comment2target(targetId, type, content) {
    layui.use('layer',function () {
        var layer = layui.layer;
        if(!content){
            layer.msg("请输入回复内容！");
        }
        else{
            $.ajax({
                type:"post",
                url:"/comment",
                contentType:"application/json",
                data:JSON.stringify({
                    "parentId":targetId,
                    "content":content,
                    "type":type
                }),
                success:function (result) {
                    if(result.code == 200){
                        window.location.reload();
                    }else{
                        if(result.code == 2000){
                            layer.confirm(result.message, {
                                btn: ['确定','取消'] //按钮
                            }, function(index){
                                window.open("http://localhost:8887/login");
                                window.localStorage.setItem("closable",true);
                                layer.close(index);
                            });
                        }else{
                            layer.msg(result.message);
                        }
                    }
                },
                dataType:"json"
            });
        }
    });
}

/**
 *展开二级评论
 */
function collapseComments(e) {
    var id = e.getAttribute("data-id");
    var comments = $('#comment-' + id);
    //判断二级评论的状况
    if(comments.hasClass("in")){
        //折叠二级评论
        comments.removeClass("in");
        e.classList.remove("active");
    }else{
        var subCommentContainer = $('#comment-' + id);
        if(subCommentContainer.children().length != 1){
            //展开二级评论
            comments.addClass("in");
            e.classList.add("active");
        }else{
            $.getJSON("/comment/" + id, function (data) {
                $.each(data.data.reverse(),function (index, comment) {

                    var commentAvatar = $("<a/>",{
                        "class":"tree-avatar",
                        "href":"#"
                    }).append($("<img/>", {
                        src: comment.user.avatarUrl
                    }));

                    var commentUser = $("<div/>",{
                        "class":"tree-detail-user"
                    }).append($("<a/>", {
                        "class":"tree-link",
                        "href":"#"
                    }).append($("<cite/>", {
                        "html": comment.user.nickName
                    })));

                    var commentHits = $("<div/>",{
                        "class":"detail-hits"
                    }).append($("<span/>", {
                        "html":moment(comment.gmtCreate).format("YYYY-MM-DD HH:mm")
                    })).append($("<span/>", {
                        "class":"rightbtn",
                        style:"cursor: pointer;"
                    }).append($("<i/>", {
                        "class":"iconfont icon-huifu"
                    })));

                    var commentJieDaBody = $("<div/>",{
                        "class":"detail-body jieda-body",
                        "html": comment.content
                    });

                    var commentElement = $("<div/>",{
                        "class":"detail-about detail-about-reply"
                    }).append(commentAvatar).append(commentUser).append(commentHits).append(commentJieDaBody);
                    subCommentContainer.prepend(commentElement);
                });
                //展开二级评论
                comments.addClass("in");
                e.classList.add("active");
            });
        }
    }
}

/**
 *选择标签
 */
function selectTag(e) {
    var value = e.getAttribute("data-tag");
    var previous = $("#tag").val();
    if(previous.split(",").indexOf(value) == -1){
        if(previous){
            $("#tag").val(previous + ',' + value);
        }else{
            $("#tag").val(value);
        }
    }
}

function showSelectTag() {
    $("#select-tag").show();
}