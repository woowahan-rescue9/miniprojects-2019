import templates from "./templates.js"
import api from "./api.js"

const ENTER = 13
const TIME = {
  "MINUTE" : 1000 * 60
}
TIME.HOUR = 60 * TIME.MINUTE
TIME.DAY = 24 * TIME.HOUR
TIME.WEEK = 7 * TIME.DAY

class ContentService {
  constructor() {
    ContentService.prototype.editBackup = {}
  }

  isEnterKey(e) {
    return e.keyCode === ENTER || e.which === ENTER || e.key === ENTER
  }

  formatDate(dateString) {
    const date = new Date(dateString + "Z")
    const difference = (new Date()).getTime() - date.getTime()
    if (difference < 10 * TIME.MINUTE) {
      return "방금 전"
    }
    if (difference < TIME.HOUR) {
      return Math.floor(difference / TIME.MINUTE) + "분 전"
    }
    if (difference < TIME.DAY) {
      return Math.floor(difference / TIME.HOUR) + "시간 전"
    }
    if (difference < TIME.WEEK) {
      return Math.floor(difference / TIME.DAY) + "일 전"
    }
    return date.format("yyyy-MM-dd hh:mm")
  }
}

class ArticleService extends ContentService {
  constructor(commentService) {
    super()
    this.commentService = commentService
    if (document.getElementById("user-id") === null || (document.getElementById("user-id").value === document.getElementById("friend-id").value)) {
      document.getElementById("write-article").addEventListener("click", () => this.write())
      document.getElementById("articles").addEventListener("click", event => {
        if (event.target.classList.contains("edit-article")) {
          this.edit(event.target.dataset.id)
        }
        if (event.target.classList.contains("remove-article")) {
          this.remove(event.target.dataset.id)
        }
      })
      document.getElementById("articles").addEventListener("keydown", event => {
        if (event.target.classList.contains("confirm-edit-article") && super.isEnterKey(event)) {
          this.confirmEdit(event.target.dataset.id)
        }
      })
    }
    if (document.getElementById("friend-id") === null) {
      this.show("/api/articles")
    } else {
      this.show(`/api/users/${document.getElementById("friend-id").value}/articles`)
    }
  }

  async show(uri) {
    const articles = await api.get(uri)
    document.getElementById("articles").innerHTML = ""
    articles.forEach(article => {
      document.getElementById("articles").insertAdjacentHTML(
        "beforeend",
        templates.articleTemplate({
          "id": article.articleResponse.id,
          "content": article.articleResponse.content,
          "date": super.formatDate(article.articleResponse.createdDate),
          "user": article.articleResponse.userOutline,
          "images": article.articleResponse.attachments,
          "countOfComment": article.countOfComment,
          "countOfLike": article.countOfLike
        })
      )
      this.checkAuthor(article)
      this.checkLike(article.articleResponse.id)
      this.commentService.show(article.articleResponse.id)
    })
  }

  checkAuthor(article) {
    const userId = document.getElementById("user-profile").getAttribute("href").replace("/users/", "")
    if (article.articleResponse.userOutline.id != userId) {
      document.getElementById(`article-dropdown-menu-${article.articleResponse.id}`).style.display = "none"
    }
  }

  async checkLike(articleId) {
    if ((await api.get(`/api/articles/${articleId}/like`)) === true) {
      document.getElementById(`article-like-${articleId}`).classList.toggle("liked")
    }
  }

  async write() {
    const textbox = document.getElementById("new-article")
    const content = textbox.value.trim()
    const req = new FormData()
    req.append("content", content)
    const files = document.getElementById(
      "attachment").files;
    if (files.length > 0) {
      req.append("files", files[0])
    }
    if (!(content.length === 0 && files.length === 0)) {
      try {
        const article = await api.post("/api/articles", req)
        textbox.value = ""
        document.getElementById("articles").insertAdjacentHTML(
          "afterbegin",
          templates.articleTemplate({
            "id": article.id,
            "content": article.content,
            "date": super.formatDate(article.createdDate),
            "user": article.userOutline,
            "images": article.attachments,
            "countOfComment": 0,
            "countOfLike": 0
          })
        )
        document.getElementById("attachment").value = ""
      } catch (e) {}
    }
  }

  edit(id) {
    const contentAreaContainer = document.getElementById(`article-${id}-content`)
    if (contentAreaContainer.dataset.isEditing === "false") {
      contentAreaContainer.dataset.isEditing = "true"
      const contentArea = contentAreaContainer.lastElementChild
      const originalContent = contentArea.firstElementChild.innerText
      contentArea.innerHTML = ""
      contentArea.insertAdjacentHTML(
        "beforeend",
        `<textarea class="confirm-edit-article resize-none form-control border bottom resize-none" data-id="${id}">${originalContent}</textarea>`
      )
      super.editBackup[id] = originalContent
    }
  }

  async confirmEdit(id) {
    const contentAreaContainer = document.getElementById(`article-${id}-content`)
    contentAreaContainer.dataset.isEditing = "false"
    const contentArea = contentAreaContainer.lastElementChild
    const editedContent = contentArea.firstElementChild.value.trim()
    if (editedContent.length != 0) {
      try {
        const editedArticle = await api.put(`/api/articles/${id}`, {
          "content": editedContent
        })
        document.getElementById(`article-${id}`).querySelector(".sub-title").innerText = super.formatDate(editedArticle.createdDate)
        contentArea.innerHTML = ""
        contentArea.insertAdjacentHTML("afterbegin", `<span> ${templates.escapeHtml(editedArticle.content)} </span>`)
      } catch (e) {
        contentArea.innerHTML = ""
        contentArea.insertAdjacentHTML("afterbegin", `<span> ${templates.escapeHtml(super.editBackup[id])} </span>`)
      }
      super.editBackup[id] = undefined
    }
  }

  async remove(id) {
    try {
      await api.delete(`/api/articles/${id}`)
      document.getElementById(`article-${id}`).remove()
    } catch (e) {}
  }

  async like(id) {
    try {
      await api.post(`/api/articles/${id}/like`)
      document.getElementById("article-like-" + id).classList.toggle("liked")
      document.getElementById("count-of-like-" + id).innerText = " " + await api.get(`/api/articles/${id}/like/count`)
    } catch (e) {}
  }
}

class CommentService extends ContentService {
  async show(articleId) {
    const comments = await api.get(`/api/articles/${articleId}/comments`)
    comments.forEach(comment => {
      document.getElementById("comments-" + articleId).insertAdjacentHTML(
        "beforeend",
        templates.commentTemplate({
          "id": comment.id,
          "content": comment.content,
          "date": super.formatDate(comment.createdDate),
          "user": comment.userOutline
        })
      )
      this.checkAuthor(comment)
      this.checkLike(comment.id)
    })
  }

  checkAuthor(comment) {
    const userId = document.getElementById("user-profile").getAttribute("href").replace("/users/", "")
    if (comment.userOutline.id != userId) {
      document.getElementById(`comment-remove-button-${comment.id}`).style.display = "none"
    }
  }

  async checkLike(commentId) {
    const countOfLike = await api.get(`/api/comments/${commentId}/like/count`)
    if (countOfLike > 0) {
      document.getElementById(`comment-item-${commentId}`).insertAdjacentHTML(
        "beforeend",
        templates.commentLikeTemplate({
          "id": commentId
        })
      )
      document.getElementById(`count-of-comment-like-${commentId}`).innerText = " " + countOfLike
    }
    if ((await api.get(`/api/comments/${commentId}/like`)) === true) {
      document.getElementById(`comment-like-${commentId}`).classList.toggle("liked")
    }
  }

  async write(event, id) {
    event = event || window.event
    const textbox = document.getElementById(`new-comment-${id}`)
    const content = textbox.value.trim()
    if (content.length != 0 && super.isEnterKey(event)) {
      try {
        const comment = await api.post(`/api/articles/${id}/comments`, {
          "content": content
        })
        textbox.value = ""
        document.getElementById(`comments-${id}`).insertAdjacentHTML(
          "beforeend",
          templates.commentTemplate({
            "id": comment.id,
            "content": comment.content,
            "date": super.formatDate(comment.createdDate),
            "user": comment.userOutline
          })
        )
        document.getElementById(`count-of-comment-${id}`).innerText = await api.get(`/api/articles/${id}/comments/count`)
      } catch (e) {}
    }
  }

  async remove(id) {
    try {
      await api.delete(`/api/comments/${id}`)
      const articleId = document.getElementById(`comment-item-${id}`).parentNode.getAttribute("id").substring(9)
      document.getElementById(`comment-item-${id}`).remove()
      document.getElementById(`count-of-comment-${articleId}`).innerText = await api.get(`/api/articles/${articleId}/comments/count`)
    } catch (e) {}
  }

  async like(id) {
    try {
      if ((await api.get(`/api/comments/${id}/like/count`)) === 0) {
        document.getElementById(`comment-item-${id}`).insertAdjacentHTML(
          "beforeend",
          templates.commentLikeTemplate({
            "id": id
          })
        )
      }
      await api.post(`/api/comments/${id}/like`)
      const countOfLike = await api.get(`/api/comments/${id}/like/count`)
      if (countOfLike === 0) {
        document.getElementById(`comment-like-${id}`).remove()
      }
      if (countOfLike > 0) {
        document.getElementById(`count-of-comment-like-${id}`).innerText = " " + await api.get(`/api/comments/${id}/like/count`)
        document.getElementById(`comment-like-${id}`).classList.toggle("liked")
      }
    } catch (e) {}
  }
}

export { ArticleService, CommentService }