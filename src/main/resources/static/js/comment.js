/**
 * 提交回复
 */
function post() {
    var questionId = $("#question_id").val();
    var content = $(".layui-textarea").val();
    comment2target(questionId,questionId,questionId,1,content);
}

function comment(e) {
    var questionId = $("#question_id").val();
    var parentId = e.getAttribute("data-parentid");
    var targetId = e.getAttribute("data-targetid");
    var commentType = e.getAttribute("data-type");
    var content = $("#input-"+parentId).val();
    comment2target(questionId,parentId,targetId,commentType,content);
}

function comment2target(questionId,parentId,targetId, type, content) {
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
                    "questionId":questionId,
                    "parentId":parentId,
                    "targetId":targetId,
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
                                window.open("/login");
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

    var inputComments2 = $("#input-" + id);
    var btnComments2 = $("#btn-" + id);
    inputComments2.attr('placeholder','评论一下...');
    btnComments2.attr('data-parentId',id);
    btnComments2.attr('data-targetId',id);
    btnComments2.attr('data-type',2);
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
                        "href":"/user/"+comment.user.id
                    }).append($("<img/>", {
                        src: comment.user.avatarUrl
                    }));

                    var commentUser = $("<div/>",{
                        "class":"tree-detail-user"
                    }).append($("<a/>", {
                        "class":"tree-link",
                        "href":"/user/"+comment.user.id
                    }).append($("<cite/>", {
                        "html": comment.user.nickName
                    })));

                    var commentHits = $("<div/>",{
                        "class":"detail-hits"
                    }).append($("<span/>", {
                        "html":moment(comment.gmtCreate).format("YYYY-MM-DD HH:mm")
                    })).append($("<span/>", {
                        "class":"rightLike"+(comment.likeStatus == 1?' liked':''),
                        style:"cursor: pointer;",
                        "data-id": comment.id,
                        "id": "likeCom-"+comment.id,
                        "onclick" :"like_comment(this);"
                    }).append($("<i/>", {
                        "class":"iconfont icon-zan2",
                        style:"font-size: 19px;"
                    })).append($("<em/>", {
                        "html":comment.likeCount,
                        "id": "likeCountCom-"+comment.id,
                        style:"font-size: 15px;font-style: normal;"
                    }))).append($("<span/>", {
                        "class":"rightbtn",
                        style:"cursor: pointer;",
                        "data-id": comment.id,
                        "id": "comment-"+comment.id,
                        "onclick" :"getCommentUser("+id+","+comment.id+",'"+comment.user.nickName+"');"
                    }).append($("<i/>", {
                        "class":"iconfont icon-huifu"
                    })));

                    var a = $("<a/>",{
                        "href":"/user"+comment.targetUserId,
                        "class":"tree-link",
                        html:comment.targetUserName
                    });
                    var commentJieDaBody = $("<div/>",{
                        "class":"detail-body jieda-body"
                    }).append(comment.targetUserId != null?"回复 ":"")
                        .append(comment.targetUserId != null?a:"")
                        .append(comment.targetUserId != null?" :"+comment.content:comment.content);

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

function getCommentUser(upId,subId,subName) {
    var inputComments = $("#input-" + upId);
    var btnComments = $("#btn-" + upId);
    var upName = '回复 '+subName+' ：';

    inputComments.attr('placeholder',upName);
    btnComments.attr('data-parentId',upId);
    btnComments.attr('data-targetId',subId);
    btnComments.attr('data-type',3);
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

function like_question(e) {
    var questionId = e.getAttribute("data-id");
    var likeQu = $("#likeQu-"+questionId);
    var status = likeQu.hasClass("liked")?1:0;
    liketarget(questionId, 1, status);
}

function like_comment(e) {
    var commentId = e.getAttribute("data-id");
    var likeCom = $("#likeCom-"+commentId);
    var status = likeCom.hasClass("liked")?1:0;
    liketarget(commentId, 2, status);
}

function liketarget(targetId, type, status){
    $.ajax({
        type:"post",
        url:"/like",
        contentType:"application/json",
        data:JSON.stringify({
            "likedUserId":targetId,
            "type":type,
            "status":status
        }),
        success:function (result) {
            if(result.code == 200){
                if(type == 1){
                    if(status == 0){
                        var likeQu = $("#likeQu-"+targetId);
                        likeQu.addClass("liked");
                        var likeCountQu = $("#likeCountQu-"+targetId);
                        likeCountQu.html(parseInt(likeCountQu.text())+1);
                    }else if(status == 1){
                        var likeQu = $("#likeQu-"+targetId);
                        likeQu.removeClass("liked");
                        var likeCountQu = $("#likeCountQu-"+targetId);
                        likeCountQu.html(parseInt(likeCountQu.text())-1);
                    }
                }else if(type == 2){
                    if(status == 0){
                        var likeCom = $("#likeCom-"+targetId);
                        likeCom.addClass("liked");
                        var likeCountCom = $("#likeCountCom-"+targetId);
                        likeCountCom.html(parseInt(likeCountCom.text())+1);
                    }else if(status == 1){
                        var likeCom = $("#likeCom-"+targetId);
                        likeCom.removeClass("liked");
                        var likeCountCom = $("#likeCountCom-"+targetId);
                        likeCountCom.html(parseInt(likeCountCom.text())-1);
                    }
                }
            }else{
                if(result.code == 2000){
                    layer.confirm(result.message, {
                        btn: ['确定','取消'] //按钮
                    }, function(index){
                        window.open("/login");
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