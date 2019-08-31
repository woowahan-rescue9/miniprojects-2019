const BASE_URL = `http://${window.location.host}`
const url = window.location.pathname
const userId = url.replace("/users/", "")

window.App.showFriends(userId)
window.App.showArticles(userId)