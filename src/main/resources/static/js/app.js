const App = (() => {
  "use strict"

  const BASE_URL = "http://localhost:8080"
  const ARTICLE_TEMPLATE_HTML =
    '<div id="article-{{id}}" class="card widget-feed padding-15">' +
      '<div class="feed-header">' +
        '<ul class="list-unstyled list-info">' +
          '<li>' +
            '<img class="thumb-img img-circle" src="images/profile/{{user.coverUrl}}" alt="{{user.name}}">' +
            '<div class="info">' +
              '<a href="/users/{{user.id}}" class="title no-pdd-vertical text-semibold inline-block">{{user.name}}</a>' +
              '<span>님이 글을 작성하였습니다.</span>' +
              '<a class="pointer absolute top-0 right-0" data-toggle="dropdown" aria-expanded="false">' +
                '<span class="btn-icon text-dark">' +
                  '<i class="ti-more font-size-16"></i>' +
                '</span>' +
              '</a>' +
              '<ul class="dropdown-menu">' +
                '<li>' +
                  '<a href="javascript:App.removeArticle({{id}})" class="pointer">' +
                    '<i class="ti-trash pdd-right-10 text-dark"></i>' +
                    '<span>게시글 삭제</span>' +
                  '</a>' +
                '</li>' +
              '</ul>' +
            '</div>' +
          '</li>' +
        '</ul>' +
      '</div>' +
      '<div class="feed-body no-pdd">' +
        '<p>' +
          '<span> {{content}} </span>' + 
        '</p>' +
      '</div>' +
      '<ul class="feed-action pdd-btm-5 border bottom">' +
        '<li>' +
          '<i class="fa fa-thumbs-o-up text-info font-size-16 mrg-left-5"></i>' +
          '<span class="font-size-14 lh-2-1"> 0</span>' +
        '</li>' +
        '<li class="float-right mrg-right-15">' +
          '<span class="font-size-13">댓글 0개</span>' +
        '</li>' +
      '</ul>' +
      '<ul class="feed-action border bottom d-flex">' +
        '<li class="text-center flex-grow-1">' +
          '<button class="btn btn-default no-border pdd-vertical-0 no-mrg width-100">' +
            '<i class="fa fa-thumbs-o-up font-size-16"></i>' +
            '<span class="font-size-13">좋아요</span>' +
          '</button>' +
        '</li>' +
        '<li class="text-center flex-grow-1">' +
          '<button class="btn btn-default no-border pdd-vertical-0 no-mrg width-100">' +
            '<i class="fa fa-comment-o font-size-16"></i>' +
            '<span class="font-size-13">댓글</span>' +
          '</button>' +
        '</li>' +
      '</ul>' +
      '<div class="feed-footer">' +
        '<div class="comment">' +
        '<ul id="comments-{{id}}" class="list-unstyled list-info"></ul>' +
          '<div class="add-comment">' +
            '<textarea id="new-comment-{{id}}" rows="1" class="form-control" placeholder="댓글을 입력하세요." onkeydown="App.writeComment({{id}})"></textarea>' +
          '</div>' +
        '</div>' +
      '</div>' +
    '</div>'
  const COMMENT_TEMPLATE_HTML =
    '<li class="comment-item">' +
      '<img class="thumb-img img-circle" src="images/profile/{{user.coverUrl}}" alt="{{user.name}}">' +
      '<div class="info">' +
        '<div class="bg-lightgray border-radius-18 padding-10 max-width-100">' +
          '<a href="/users/{{user.id}}" class="title text-bold inline-block text-link-color">{{user.name}}</a>' +
          '<span> {{content}}</span>' +
        '</div>' +
        '<div class="font-size-12 pdd-left-10 pdd-top-5">' +
          '<span class="pointer text-link-color">좋아요</span>' +
          '<span>·</span>' +
          '<span class="pointer">{{createdDate}}</span>' +
        '</div>' +
      '</div>' +
    '</li>'
  const articleTemplate = Handlebars.compile(ARTICLE_TEMPLATE_HTML)
  const commentTemplate = Handlebars.compile(COMMENT_TEMPLATE_HTML)

  const Controller = function() {
    const articleService = new ArticleService()
    const commentService = new CommentService()

    const writeArticle = () => {
      articleService.write()
    }

    const removeArticle = id => {
      articleService.remove(id)
    }

    const writeComment = id => {
      commentService.write(id)
    }

    const removeComment = id => {
      commentService.remove(id)
    }

    return {
      "writeArticle": writeArticle,
      "removeArticle": removeArticle,
      "writeComment": writeComment,
      "removeComment": removeComment
    }
  }

  const ArticleService = function() {
    const ENTER = 13

    const write = async () => {
      const textbox = document.getElementById("new-article")
      const content = textbox.value.trim()
      if (content.length != 0 && (event.keyCode === ENTER || event.which === ENTER || event.key === ENTER)) {
        try {
          textbox.value = ""
          const article = (await axios.post(BASE_URL + "/articles", {
            "content": content
          })).data
          document.getElementById("articles").insertAdjacentHTML(
            "afterbegin",
            articleTemplate({
              "id": article.id,
              "content": article.content,
              "user": article.userOutline
            })
          )
        } catch(e) {}
      }
    }

    const remove = async id => {
      try {
        await axios.delete(BASE_URL + "/articles/" + id)
        document.getElementById("article-" + id).remove()
      } catch(e) {}
    }

    return {
      "write": write,
      "remove": remove
    }
  }

  const CommentService = function() {
    const ENTER = 13

    const write = async id => {
      const textbox = document.getElementById("new-comment-" + id)
      const content = textbox.value.trim()
      if (content.length != 0 && (event.keyCode === ENTER || event.which === ENTER || event.key === ENTER)) {
        try {
          textbox.value = ""
          const comment = (await axios.post(BASE_URL + "/articles/" + id + "/comments", {
            "content": content
          })).data
          document.getElementById("comments-" + id).insertAdjacentHTML(
            "beforeend",
            commentTemplate({
              "id": comment.id,
              "content": comment.content,
              "createdDate": comment.createdDate,
              "user": comment.userOutline
            })
          )
        } catch(e) {}
      }
    }

    const remove = async id => {
      try {
        await axios.delete(BASE_URL + "/comments/" + id)
        document.getElementById("comment-" + id).remove()
      } catch(e) {}
    }

    return {
      "write": write,
      "remove": remove
    }
  }

  const controller = new Controller()

  return {
    "writeArticle": controller.writeArticle,
    "removeArticle": controller.removeArticle,
    "writeComment": controller.writeComment,
    "removeComment": controller.removeComment
  }
})()