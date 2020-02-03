/**
 * 提交回复
 */
function post() {
    var questionId = $("#question_id").val();
    var content = $(".comment-content").val();
    comment2target(questionId,1,content);
}

function comment(e) {
    var commentId = e.getAttribute("data-id");
    var content = $("#input-"+commentId).val();
    comment2target(commentId,2,content);
}

function comment2target(targetId, type, content) {
    if(!content){
        alert("请输入回复内容！");
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
                        var isAccepted = confirm(result.message);
                        if(isAccepted){
                            window.open("https://github.com/login/oauth/authorize?client_id=a7984ce94e8e4c8c796d&redirect_uri=http://localhost:8887/callback&scope=user&state=1");
                            window.localStorage.setItem("closable",true);
                        }
                    }else{
                        alert(result.message);
                    }
                }
            },
            dataType:"json"
        });
    }
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

                    var mediaLeftElement = $("<div/>",{
                        "class":"media-left"
                    }).append($("<img/>", {
                        "class": "media-object img-rounded",
                        src: comment.user.avatarUrl
                    }));

                    var mediaBodyElement = $("<div/>",{
                        "class":"media-body"
                    }).append($("<h5/>", {
                        "class": "media-heading",
                        "html": comment.user.name
                    })).append($("<div/>", {
                        "html": comment.content
                    })).append($("<div/>", {
                        "class":"menu"
                    }).append($("<span/>", {
                        "class":"pull-right",
                        "html":moment(comment.gmtCreate).format("YYYY-MM-DD")
                    })));

                    var mediaElement = $("<div/>",{
                        "class":"media"
                    }).append(mediaLeftElement).append(mediaBodyElement);

                    var commentElement = $("<div/>",{
                        "class":"col-lg-12 col-md-12 col-sm-12 col-xs-12 comments"
                    }).append(mediaElement);
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