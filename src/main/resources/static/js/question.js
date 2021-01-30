/**
 * 提交回复
 */
function post() {
    var content = $("#comment_content").val();
    var questionId = $("#question_id").val();
    commentToTarget(questionId,content,1)
}

function commentToTarget(parentId, content, type) {
    if (!content) {
        alert("输入不能为空");
        return;
    }
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType:"application/json",
        dataType: "json",
        data: JSON.stringify({
            "parentId":parentId,
            "content":content,
            "type":type
        }),
        success: function (response) {
            if (response.code == 200) {
                window.location.reload();
            }else{
                if (response.code == 2001) {
                    var isAccepted = confirm(response.message);
                    if (isAccepted) {
                        window.open("https://github.com/login/oauth/authorize?client_id=c852f7cf5c9f136c7a82&redirect_uri=http://localhost:8887/callback&scope=user&state=1");
                        window.localStorage.setItem("closable",true);
                    }
                }else {
                    alert(response.message);
                }
            }
        }
    });
}
/**
 * comment to comment
 * @param e
 */
function comment(e) {
    var commentId = e.getAttribute("data-id");
    var content=$('#input-'+commentId).val();
    commentToTarget(commentId,content,2);
}

/**
 * 展开二级评论
 * @param e
 */
function collapseComments(e) {
    var id = e.getAttribute("data-id");
    var comments = $('#comment-'+id);
    // var collapse=e.getAttribute("data-collapse");

    //获取二级评论展开状态
    // if (collapse) {
    //     //关闭
    //     comments.removeClass("in");
    //     e.removeAttribute("data-collapse");
    //     e.classList.remove("active");
    // }else {
    //     //展开二级评论
    //     comments.addClass("in");
    //     e.setAttribute("data-collapse","in");
    //     e.classList.add("active");
    // }
    //重构 toggleClass() 检查每个元素中指定的类。如果不存在则添加类，如果已设置则删除之
    comments.toggleClass("in");
    $(e).toggleClass("active");
    if (comments.children().length == 1) {
        $.getJSON( "/comment/"+id, function( data ) {
            // console.log(data);
            var items = [];
            $.each( data.data, function(index,comment ) {
                var html="<div class=\"comments col-lg-12\">\n" +
                    "                                <div class=\"media\">\n" +
                    "                                    <div class=\"media-left\">\n" +
                    "                                         <span>\n" +
                    "                                                 <img class=\"media-object avatar \" src=\""+comment.user.avatarUrl+"\" alt=\"...\">\n" +
                    "                                         </span>\n" +
                    "                                    </div>\n" +
                    "                                    <div class=\"media-body\">\n" +
                    "                                        <h5>"+comment.user.name+"</h5>\n" +
                    "                                        <span>"+comment.content+"</span>\n" +
                    "                                        <div class=\"menu\">\n" +
                    "                                            <span class=\"pull-right\"\n" +
                    "                                                >"+moment(comment.gmtCreate).format('YYYY-MM-DD')+"</span>\n" +
                    "                                        </div>\n" +
                    "                                    </div>\n" +
                    "                                </div>\n" +
                    "                            </div>";
                items.push(html );
            });
            comments.prepend(items);
        });
    }

}