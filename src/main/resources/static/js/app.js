const App = (() => {
  "use strict"

  const BASE_URL = "http://" + window.location.host

  class Api {
    get(path) {
      return axios.get(BASE_URL + path)
    }

    post(path, data) {
      return axios.post(BASE_URL + path, data)
    }

    put(path, data) {
      return axios.post(BASE_URL + path, data)
    }

    delete(path) {
      return axios.delete(BASE_URL + path)
    }
  }

  class Service {
    constructor(api) {
      this.api = api
      this.editBackup = {}
    }

    isEnterKey(e) {
      const ENTER = 13

      return e.keyCode === ENTER || e.which === ENTER || e.key === ENTER
    }

    formatDate(dateString) {
      const MINUTE = 1000 * 60
      const HOUR = 60 * MINUTE
      const DAY = 24 * HOUR
      const WEEK = 7 * DAY

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

  class ArticleService extends Service {
    async write() {
      const textbox = document.getElementById("new-article")
      const content = textbox.value.trim()
      if (content.length != 0) {
        try {
          const req = new FormData()
          req.append("content", content)
          const files = document.getElementById("attachment").files;
          if (files.length > 0) {
            req.append("files", files[0])
          }
          const article = (await axios.post(BASE_URL + "/api/articles", req)).data
          textbox.value = ""
          document.getElementById("articles").insertAdjacentHTML(
              "afterbegin",
              templates.articleTemplate({
                "id": article.id,
                "content": article.content,
                "date": super.formatDate(article.recentDate),
                "user": article.userOutline,
                "images": article.attachments
              })
          )
          document.getElementById("attachment").value = ""
        } catch (e) {
        }
      }
    }

    edit(id) {
      const contentArea = document.getElementById("article-" + id + "-content")
      const originalContent = contentArea.firstElementChild.firstElementChild.innerText
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
        contentArea.insertAdjacentHTML("afterbegin", "<p><span> " + templates.escapeHtml(result) + " </span></p>")
        super.editBackup[id] = undefined
      }
    }

    async remove(id) {
      try {
        await axios.delete(BASE_URL + "/api/articles/" + id)
        document.getElementById("article-" + id).remove()
      } catch (e) {
      }
    }

    async like(id) {
      try {
        await axios.post(BASE_URL + "/api/articles/" + id + "/like")
        const likeButton = document.getElementById("article-like-" + id)
        likeButton.classList.toggle('liked')
        document.getElementById("count-of-like-" + id).innerText = " " + (await axios.get(BASE_URL + "/api/articles/" + id + "/like/count")).data
      } catch (e) {
      }
    }
  }

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
              templates.commentTemplate({
                "id": comment.id,
                "content": comment.content,
                "date": super.formatDate(comment.createdDate),
                "user": comment.userOutline
              })
          )
          document.getElementById("count-of-comment-" + id).innerText = (await axios.get(BASE_URL + "/api/articles/" + id + "/comments/count")).data
        } catch (e) {
        }
      }
    }

    async remove(id) {
      try {
        await axios.delete(BASE_URL + "/api/comments/" + id)
        document.getElementById("comments-" + id).remove()
        document.getElementById("count-of-comment-" + id).innerText = (await axios.get(BASE_URL + "/api/articles/" + id + "/comments/count")).data
      } catch (e) {
      }
    }
  }

  class FriendService extends Service {
    constructor() {
      if (typeof document.getElementById('user-id') === 'undefined'
          || document.getElementById('user-id') === null) {
        super();
        return;
      }

      const userId = document.getElementById('user-id').value;
      const friendId = document.getElementById('friend-id').value;

      if (typeof userId !== 'undefined' && userId !== friendId) {

        if (document.getElementById('already-friend').value === 'true') {
          // toggleButton
          const addFriend = document.getElementById('add-friend');
          addFriend.classList.toggle('already-friend');

          const removeFriend = document.getElementById('remove-friend');
          removeFriend.classList.toggle('already-friend');
        }
      }
      super();
    }

    async makeFriend(friendId) {
      try {
        const req = {friendId: friendId}

        // 일단 성공한다고 가정
        await axios.post(BASE_URL + "/api/friendships", req);

        this.toggleButton();
      } catch (e) {
      }
    }

    async breakWithFriend(friendId) {
      try {
        // 일단 성공한다고 가정
        await axios.delete(BASE_URL + "/api/friendships?friendId=" + friendId)

        this.toggleButton();
      } catch (e) {
      }
    }

    toggleButton() {
      const addFriend = document.getElementById('add-friend');
      addFriend.classList.toggle('already-friend');

      const removeFriend = document.getElementById('remove-friend');
      removeFriend.classList.toggle('already-friend');
    }
  }

  class SearchService {
    constructor() {
      const autoComplete = document.getElementById("auto-complete")
      const findUsernamesByKeyword = async keyword => {
        const result = (await axios.get(BASE_URL + "/api/users/" + keyword)).data
        autoComplete.innerHTML = ""
        for (let i = 0; i < result.length; i++) {
          autoComplete.innerHTML += `<span class="dropdown-item" onclick="App.visitResult('${result[i].name}', ${result[i].id})">${result[i].name}&nbsp;&nbsp;
                                      <span style="color:grey"> (${result[i].email}) </span></span>`
        }
      }
      document.getElementById("search").addEventListener("keyup", event => {
        const keyword = event.target.value
        if (keyword.trim().length === 0) {
          autoComplete.style.display = "none"
          autoComplete.innerHTML = ""
        } else {
          autoComplete.style.display = "block"
          findUsernamesByKeyword(keyword)
        }
      })
    }

    visitResult(name, id) {
      document.getElementById("search").value = name
      document.getElementById("search-form").setAttribute("action", "/users/" + id)
      document.getElementById("auto-complete").style.display = "none"
    }
  }

  class Controller {
    constructor(articleService, commentService, friendService, searchService) {
      this.articleService = articleService
      this.commentService = commentService
      this.friendService = friendService
      this.searchService = searchService
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

    makeFriend(friendId) {
      this.friendService.makeFriend(friendId);
    }

    breakWithFriend(friendId) {
      this.friendService.breakWithFriend(friendId)
    }

    visitResult(name, id) {
      this.searchService.visitResult(name, id)
    }
  }

  const attachmentModal = document.getElementById("attachment-modal")
  if (attachmentModal != null) {
    attachmentModal.addEventListener("click", event => {
      if (event.target != document.getElementById("attachment")) {
        attachmentModal.style.display = "none"
      }
    })
  document.getElementById("attachment-open").addEventListener("click", () => attachmentModal.style.display = "block")
  document.getElementById("attachment-close").addEventListener("click", () => attachmentModal.style.display = "none")
}

  const editProfileButton = document.getElementById("edit-profile")
  if (editProfileButton != null) {
    const editProfileModal = document.getElementById("edit-profile-modal")
    // editProfileModal.addEventListener("click", event => {
    //   if (event.target != document.getElementById("edit-profile-form")) {
    //     editProfileModal.style.display = "none"
    //   }
    // })
    editProfileButton.addEventListener("click", () => editProfileModal.style.display = "block")
    document.getElementById("edit-profile-close").addEventListener("click", () => editProfileModal.style.display = "none")
  }

  const api = new Api()
  return new Controller(new ArticleService(api), new CommentService(api), new FriendService(), new SearchService())
})()