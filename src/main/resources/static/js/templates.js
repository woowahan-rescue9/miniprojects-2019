const templates = (() => {
  class Templates {
    escapeHtml(string) {
      return string.replace(/</gi, "&lt;").replace(/>/gi, "&gt;").replace(/\n/gi, "<br>")
    }

    articleTemplate(input) {
      return `<div id="article-${input.id}" class="card widget-feed padding-15">
        <div class="feed-header">
          <ul class="list-unstyled list-info">
            <li>
              <img class="thumb-img img-circle" src="/images/profile/${input.user.coverUrl}" alt="${input.user.name}">
              <div class="info">
                <a href="/users/${input.user.id}" class="title no-pdd-vertical text-semibold inline-block">${input.user.name}</a>
                <span>님이 게시물을 작성하였습니다.</span>
                <span class="sub-title">${input.date}</span>
                <a class="pointer absolute top-0 right-0" data-toggle="dropdown" aria-expanded="false">
                  <span class="btn-icon text-dark">
                    <i class="ti-more font-size-16"></i>
                  </span>
                </a>
                <ul class="dropdown-menu">
                  <li>
                    <a href="javascript:App.editArticle(${input.id})" class="pointer">
                      <i class="ti-pencil pdd-right-10 text-dark"></i>
                      <span>게시글 수정</span>
                    </a>
                    <a href="javascript:App.removeArticle(${input.id})" class="pointer">
                      <i class="ti-trash pdd-right-10 text-dark"></i>
                      <span>게시글 삭제</span>
                    </a>
                  </li>
                </ul>
              </div>
            </li>
          </ul>
        </div>
        <div id="article-${input.id}-content" class="feed-body no-pdd">` +
          input.images.map(image => `<img class="vertical-align" src="/${image.path}">`) +
          `<p>
            <span> ${this.escapeHtml(input.content)} </span> 
          </p>
        </div>
          <ul class="feed-action pdd-btm-5 border bottom">
            <li>
              <i class="fa fa-thumbs-o-up text-info font-size-16 mrg-left-5"></i>
              <span id="count-of-like-${input.id}" class="font-size-14 lh-2-1"> 0</span>
            </li>
            <li class="float-right mrg-right-15">
              <span class="font-size-13">댓글 <span id="count-of-comment-${input.id}">0</span>개</span>
            </li>
          </ul>
          <ul class="feed-action border bottom d-flex">
            <li class="text-center flex-grow-1">
              <button id="article-like-${input.id}" onclick="App.likeArticle(${input.id})" class="btn btn-default no-border pdd-vertical-0 no-mrg width-100">
                <i class="fa fa-thumbs-o-up font-size-16"></i>
                <span class="font-size-13"> 좋아요</span>
              </button>
            </li>
            <li class="text-center flex-grow-1">
              <button class="btn btn-default no-border pdd-vertical-0 no-mrg width-100">
                <i class="fa fa-comment-o font-size-16"></i>
                <span class="font-size-13"> 댓글</span>
              </button>
            </li>
          </ul>
          <div class="feed-footer">
            <div class="comment">
            <ul id="comments-${input.id}" class="list-unstyled list-info"></ul>
              <div class="add-comment">
                <textarea id="new-comment-${input.id}" rows="1" class="form-control" placeholder="댓글을 입력하세요." onkeydown="App.writeComment(event, ${input.id})"></textarea>
              </div>
            </div>
          </div>
        </div>`
    }

    commentTemplate(input) {
        return `<li class="comment-item">
          <img class="thumb-img img-circle" src="/${input.user.profileImage.path}" alt="${input.user.name}">
          <div class="info">
            <div class="bg-lightgray border-radius-18 padding-10 max-width-100">
              <a href="/users/${input.user.id}" class="title text-bold inline-block text-link-color">${input.user.name}</a>
              <span> ${this.escapeHtml(input.content)}</span>
            </div>
            <div class="font-size-12 pdd-left-10 pdd-top-5">
              <span class="pointer text-link-color">좋아요</span>
              <span> · </span>
              <span>${input.date}</span>
            </div>
          </div>
        </li>`
    }

    friendTemplate(input) {
      return `<div class="friend">
          <img src="/${input.profileImage}" alt="${input.name}">
          <a href="/users/${input.id}"><span class="friend-name">${input.name}</span></a>
        </div>`
    }
  }

  return new Templates()
})()