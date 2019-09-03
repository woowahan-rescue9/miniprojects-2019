import { ArticleService, CommentService } from "./content-service.js"
import { FriendService, ProfileService, SearchService } from "./misc-service.js"

window.App = (() => {
  class Controller {
    constructor(articleService, commentService, friendService, profileService, searchService) {
      this.articleService = articleService
      this.commentService = commentService
      this.friendService = friendService
      this.profileService = profileService
      this.searchService = searchService
    }

    showNewsfeed() {
      this.articleService.showNewsfeed()
    }

    showArticles(userId) {
      this.articleService.showArticles(userId)
    }

    likeArticle(id) {
      this.articleService.like(id)
    }

    showComments(articleId) {
      this.commentService.show(articleId)
    }

    writeComment(event, id) {
      this.commentService.write(event, id)
    }

    removeComment(id) {
      this.commentService.remove(id)
    }

    likeComment(id) {
      this.commentService.like(id)
    }

    makeFriendAndToggleTarget(friendId, target) {
      this.friendService.makeFriendAndToggleTarget(friendId, target)
    }

    visitResult(name, id) {
      this.searchService.visitResult(name, id)
    }

    userInfo() {
      this.profileService.userInfo()
    }

    profileImagePreview(input) {
      this.profileService.profileImagePreview(input)
    }
  }

  const commentService = new CommentService()
  return new Controller(
    new ArticleService(commentService),
    commentService,
    new FriendService(),
    new ProfileService(),
    new SearchService()
  )
})()