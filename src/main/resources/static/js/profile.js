const BASE_URL = `http://${window.location.host}`
const url = window.location.pathname
const userId = url.replace("/users/", "")

App.showFriends(userId)
App.showArticles(userId)