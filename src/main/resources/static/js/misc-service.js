import api from "./api.js"

class FriendService {
  constructor() {
    if (document.getElementById("user-id") === null) {
      return
    }
    const friendId = document.getElementById("friend-id").value
    if (document.getElementById("user-id").value != friendId) {
      if (document.getElementById("already-friend").value === "true") {
        this.toggleButton()
      }
      document.getElementById("add-friend").addEventListener("click", () => this.makeFriend(friendId))
      document.getElementById("remove-friend").addEventListener("click", () => this.breakWithFriend(friendId))
    }
  }

  async makeFriend(friendId) {
    try {
      await api.post("/api/friendships", {
        "friendId": friendId
      })
      this.toggleButton()
    } catch (e) {}
  }

  async breakWithFriend(friendId) {
    try {
      await axios.delete(`/api/friendships?friendId=${friendId}`)
      this.toggleButton()
    } catch (e) {}
  }

  toggleButton() {
    document.getElementById("add-friend").classList.toggle("already-friend")
    document.getElementById("remove-friend").classList.toggle("already-friend")
  }

  async makeFriendAndToggleTarget(friendId, target) {
    try {
      await api.post("/api/friendships", {
        "friendId": friendId
      })
      this.toggleTarget(target)
    } catch (e) {}
  }

  toggleTarget(target) {
    target.classList.toggle("already-friend")
  }
}

class ProfileService {
  constructor() {
    if (document.getElementById("user-id") != null) {
      this.showFriends(document.getElementById("friend-id").value)
      if (document.getElementById("user-id").value === document.getElementById("friend-id").value) {
        document.getElementById("edit-profile").addEventListener("click", () => this.userInfo())
        document.getElementById("edit-profile-button").addEventListener("click", () => this.edit())
        document.getElementById("profile-attachment").addEventListener("change", event => this.profileImagePreview(event.target))
      }
    }
  }

  async showFriends(userId) {
    try {
      const friends = await api.get(`/api/friendships/${userId}`)
      document.getElementById("friend-list").innerHTML = ""
      friends.forEach(friend => {
        document.getElementById("friend-list").insertAdjacentHTML(
          "beforeend",
          templates.friendTemplate({
            "id": friend.id,
            "name": friend.name,
            "profileImage": friend.profileImage.path
          })
        )
      })
    } catch (e) {}
  }

  async edit() {
    const introduction = document.getElementById("edit-introduction").value
    const name = document.getElementById("edit-name").value
    const password = document.getElementById("edit-password").value
    const check_password = document.getElementById("edit-password-confirm").value
    if (password != check_password) {
      return
    }
    try {
      const req = new FormData()
      const files = document.getElementById("profile-attachment").files
      const uri = document.getElementsByClassName("user-profile")[0].firstElementChild.getAttribute("href")
      if (files.length > 0) {
        req.append("profileImage", files[0])
      }
      req.append("introduction", introduction.trim())
      req.append("name", name.trim())
      if (password.length > 0) {
        req.append("password", password)
      }
      await api.put(`/api${uri}`, req)
      document.getElementById("edit-profile-modal").style.display = "none"
      window.location.reload()
    } catch (e) {}
  }

  profileImagePreview(input) {
    if (input.files && input.files[0]) {
      const reader = new FileReader()
      reader.addEventListener("load", event => document.getElementById("profile-image").setAttribute("src", event.target.result))
      reader.readAsDataURL(input.files[0])
      document.getElementById("profile-image").style.visibility = "visible"
    }
  }

  async userInfo() {
    const uri = document.getElementsByClassName("user-profile")[0].firstElementChild.getAttribute("href")
    const userInfo = await api.get(`/api${uri}/info`)
    document.getElementById("edit-introduction").setAttribute("value", userInfo.introduction)
    document.getElementById("edit-name").setAttribute("value", userInfo.name)
  }
}

class SearchService {
  constructor() {
    const autoComplete = document.getElementById("auto-complete")
    const findUsernamesByKeyword = async keyword => {
      const result = await api.get(`/api/users/${keyword}`)
      autoComplete.innerHTML = ""
      for (let i = 0; i < result.length; i++) {
        autoComplete.innerHTML +=
          `<span class="dropdown-item" onclick="App.visitResult("${result[i].name}", ${result[i].id})">
            ${result[i].name}&nbsp;&nbsp;<span style="color:grey"> (${result[i].email}) </span>
          </span>`
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
    document.getElementById("search-form").setAttribute("action", `/users/${id}`)
    document.getElementById("auto-complete").style.display = "none"
  }
}

export { FriendService, ProfileService, SearchService }