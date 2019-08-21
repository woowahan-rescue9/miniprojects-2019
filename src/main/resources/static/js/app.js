const App = (() => {
  "use strict"

  const BASE_URL = "http://" + window.location.host
  const ENTER = 13
  const MINUTE = 1000 * 60
  const HOUR = 60 * MINUTE
  const DAY = 24 * HOUR
  const WEEK = 7 * DAY
  class Service {
    constructor() {
      this.editBackup = {}
    }

    isEnterKey(e) {
      return e.keyCode === ENTER || e.which === ENTER || e.key === ENTER
    }

    formatDate(dateString) {
      const date = new Date(dateString)
      const difference = (new Date()).getTime() - date.getTime()
      if (difference < 10 * MINUTE) {
        return "방금 전"
      }
      if (difference < HOUR) {
        return Math.floor(difference / MINUTE) + "분 전"
      }
      if (difference < DAY) {
        return Math.floor(difference / HOUR) + "시간 전"
      }
      if (difference < WEEK) {
        return Math.floor(difference / DAY) + "일 전"
      }
      return date.format("yyyy-MM-dd hh:mm")
    }
  }

  const ARTICLE_TEMPLATE_HTML =
    '<div id="article-{{id}}" class="card widget-feed padding-15">' +
      '<div class="feed-header">' +
        '<ul class="list-unstyled list-info">' +
          '<li>' +
            '<img class="thumb-img img-circle" src="images/profile/{{user.coverUrl}}" alt="{{user.name}}">' +
            '<div class="info">' +
              '<a href="/users/{{user.id}}" class="title no-pdd-vertical text-semibold inline-block">{{user.name}}</a>' +
              '<span>님이 게시물을 작성하였습니다.</span>' +
              '<span class="sub-title">{{date}}</span>' +
              '<a class="pointer absolute top-0 right-0" data-toggle="dropdown" aria-expanded="false">' +
                '<span class="btn-icon text-dark">' +
                  '<i class="ti-more font-size-16"></i>' +
                '</span>' +
              '</a>' +
              '<ul class="dropdown-menu">' +
                '<li>' +
                  '<a href="javascript:App.editArticle({{id}})" class="pointer">' +
                    '<i class="ti-pencil pdd-right-10 text-dark"></i>' +
                    '<span>게시글 수정</span>' +
                  '</a>' +
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
      '<div id="article-{{id}}-content" class="feed-body no-pdd">' +
        '<p>' +
          '<span> {{content}} </span>' + 
        '</p>' +
        '{{#each image}}' +
      '<img class="vertical-align" src="{{this.path}}">' +
        '{{/each}}' +
      '</div>' +
      '<ul class="feed-action pdd-btm-5 border bottom">' +
        '<li>' +
          '<i class="fa fa-thumbs-o-up text-info font-size-16 mrg-left-5"></i>' +
          '<span id="count-of-like-{{id}}" class="font-size-14 lh-2-1"> 0</span>' +
        '</li>' +
        '<li class="float-right mrg-right-15">' +
          '<span class="font-size-13">댓글 <span id="count-of-comment-{{id}}">0</span>개</span>' +
        '</li>' +
      '</ul>' +
      '<ul class="feed-action border bottom d-flex">' +
        '<li class="text-center flex-grow-1">' +
          '<button id="article-like-{{id}}" onclick="App.likeArticle({{id}})" class="btn btn-default no-border pdd-vertical-0 no-mrg width-100">' +
            '<i class="fa fa-thumbs-o-up font-size-16"></i>' +
            '<span class="font-size-13"> 좋아요</span>' +
          '</button>' +
        '</li>' +
        '<li class="text-center flex-grow-1">' +
          '<button class="btn btn-default no-border pdd-vertical-0 no-mrg width-100">' +
            '<i class="fa fa-comment-o font-size-16"></i>' +
            '<span class="font-size-13"> 댓글</span>' +
          '</button>' +
        '</li>' +
      '</ul>' +
      '<div class="feed-footer">' +
        '<div class="comment">' +
        '<ul id="comments-{{id}}" class="list-unstyled list-info"></ul>' +
          '<div class="add-comment">' +
            '<textarea id="new-comment-{{id}}" rows="1" class="form-control" placeholder="댓글을 입력하세요." onkeydown="App.writeComment(event, {{id}})"></textarea>' +
          '</div>' +
        '</div>' +
      '</div>' +
    '</div>'
  const articleTemplate = Handlebars.compile(ARTICLE_TEMPLATE_HTML)
  class ArticleService extends Service {
    async write() {
      const textbox = document.getElementById("new-article")
      const content = textbox.value.trim()
      if (content.length != 0) {
        try {
          const req = new FormData()
          req.append("content", content)
          const files = document.getElementById("attachment").files;
          if(files.length > 0) {
            req.append("files", files[0])
          }
          const article = (await axios.post(BASE_URL + "/api/articles", req)).data
          textbox.value = ""
          document.getElementById("articles").insertAdjacentHTML(
            "afterbegin",
            articleTemplate({
              "id": article.id,
              "content": article.content,
              "date": super.formatDate(article.recentDate),
              "user": article.userOutline,
              "image": article.resources
            })
          )
          document.getElementById("attachment").value=""
        } catch (e) {}
      }
    }

    edit(id) {
      const contentArea = document.getElementById("article-" + id + "-content")
      const originalContent = contentArea.firstChild.firstChild.innerHTML.trim()
      contentArea.innerHTML = ""
      contentArea.insertAdjacentHTML(
        "afterbegin",
        '<textarea class="resize-none form-control border bottom resize-none" onkeydown="App.confirmEditArticle(event, ' + id + ')">' + originalContent + '</textarea>'
      )
      super.editBackup[id] = originalContent
    }

    async confirmEdit(event, id) {
      event = event || window.event
      const contentArea = document.getElementById("article-" + id + "-content")
      const editedContent = contentArea.firstChild.value.trim()
      if (editedContent.length != 0 && super.isEnterKey(event)) {
        const result = await (async () => {
          try {
            return (await axios.put(BASE_URL + "/api/articles/" + id, {
              "content": editedContent
            })).data.content
          } catch (e) {
            return super.editBackup[id]
          }
        })()
        contentArea.innerHTML = ""
        contentArea.insertAdjacentHTML("afterbegin", "<p><span> " + result + " </span></p>")
        super.editBackup[id] = undefined
      }
    }

    async remove(id) {
      try {
        await axios.delete(BASE_URL + "/api/articles/" + id)
        document.getElementById("article-" + id).remove()
      } catch (e) {}
    }

    async like(id) {
      try {
        await axios.post(BASE_URL + "/api/articles/" + id + "/like")
        const likeButton = document.getElementById("article-like-" + id)
        likeButton.classList.toggle('text-info')

        const countOfLike = (await axios.get(BASE_URL + "/api/articles/" + id + "/like/count")).data
        document.getElementById("count-of-like-" + id).innerText = " " + countOfLike
      } catch (e) {}
    }
  }
  
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
          '<span> · </span>' +
          '<span>{{date}}</span>' +
        '</div>' +
      '</div>' +
    '</li>'
  const commentTemplate = Handlebars.compile(COMMENT_TEMPLATE_HTML)
  class CommentService extends Service {
    async write(event, id) {
      event = event || window.event
      const textbox = document.getElementById("new-comment-" + id)
      const content = textbox.value.trim()
      if (content.length != 0 && super.isEnterKey(event)) {
        try {
          const comment = (await axios.post(BASE_URL + "/api/articles/" + id + "/comments", {
            "content": content
          })).data
          textbox.value = ""
          document.getElementById("comments-" + id).insertAdjacentHTML(
            "beforeend",
            commentTemplate({
              "id": comment.id,
              "content": comment.content,
              "date": super.formatDate(comment.createdDate),
              "user": comment.userOutline
            })
          )
        } catch (e) {}
      }
    }

    async remove(id) {
      try {
        await axios.delete(BASE_URL + "/api/comments/" + id)
        document.getElementById("comments-" + id).remove()
      } catch (e) {}
    }
  }

  class Controller {
    constructor(articleService, commentService) {
      this.articleService = articleService
      this.commentService = commentService
    }

    writeArticle(event) {
      this.articleService.write(event)
    }

    editArticle(id) {
      this.articleService.edit(id)
    }

    confirmEditArticle(event, id) {
      this.articleService.confirmEdit(event, id)
    }

    removeArticle(id) {
      this.articleService.remove(id)
    }

    likeArticle(id) {
      this.articleService.like(id)
    }

    writeComment(event, id) {
      this.commentService.write(event, id)
    }

    removeComment(id) {
      this.commentService.remove(id)
    }
  }

  return new Controller(new ArticleService(), new CommentService())
})()