"use strict";

const notification = (() => {
  const WEB_SOCKET_URI = "/websocket"
  const MESSAGE_BROKER_URI = "/api/notification"
  const REFRESH_INTERVAL = 600

  class ChatService {
    notify(message) {
      document.body.insertAdjacentHTML(
        "beforeend",
        `<p class="notification chat" data-close="self" role="alert">${message.srcUser.name} : ${message.content}</p>`
      )
    }
  }

  class NotificationService {
    notify(message) {
      const notificationText = message.srcUser.name + " 님" + (() => {
        switch(message.type) {
          case "FRIEND_REQUEST":
            return "과 친구가 되었습니다."
          case "COMMENT":
            return "이 '" + message.srcSummary + "' 글에 댓글을 남겼습니다 : " + message.content;
          case "LIKE":
            return "이 '" + message.content + "' 글에 좋아요를 눌렀습니다."
          default:
        }
      })()
      document.body.insertAdjacentHTML(
        "beforeend",
        '<p class="notification chat" data-close="self" role="alert">' + notificationText + "</p>"
      )
    }
  }

  new class Controller {
    constructor(chatService, notificationService) {
      this.chatService = chatService
      this.notificationService = notificationService
      this.init()
    }

    async init() {
      this.connect(await this.requestNewChannelAddress())
      setInterval(() => this.refresh(), REFRESH_INTERVAL * 1000)
    }

    async requestNewChannelAddress() {
      return (await axios.get("http://" + window.location.host + MESSAGE_BROKER_URI)).data.address
    }

    connect(channelAddress) {
      this.stompClient = Stomp.over(new SockJS(WEB_SOCKET_URI))
      this.stompClient.debug = () => {}
      this.stompClient.connect(
        {},
        frame => this.stompClient.subscribe(MESSAGE_BROKER_URI + "/" + channelAddress, message => this.dispatch(JSON.parse(message.body)))
      )
    }

    refresh() {
      this.stompClient.disconnect(async () => this.connect(await this.requestNewChannelAddress()))
    }

    dispatch(message) {
      switch(message.type) {
        case "CHAT":
          this.chatService.notify(message)
          break
        case "FRIEND_REQUEST":
          this.notificationService.notify(message)
          break
        case "COMMENT":
          this.notificationService.notify(message)
          break
        case "LIKE":
          this.notificationService.notify(message)
          break
        default:
      }
    }
  }(new ChatService(), new NotificationService())

  return new Notifications(".notification", {
    startTopPosition: 50
  })
})()
notification.init()