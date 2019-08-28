(() => {
  "use strict"

  const WEB_SOCKET_URI = "/websocket"
  const MESSAGE_BROKER_URI = "/api/notification"
  const REFRESH_INTERVAL = 600

  class ChatService {
    notify(message) {
      alert(message.sourceUser.name + " : " + message.content)
    }
  }

  class NotificationService {
    notify(message) {
      alert(message.sourceUser.name + " 님이 " + 
      (() => {
        switch(message.type) {
          case "FRIEND_REQUEST":
            return "친구 요청을 보냈습니다."
          case "COMMENT":
            return "'" + message.content + "' 글에 댓글을 남겼습니다."
          case "LIKE":
            return "'" + message.content + "' 글에 좋아요를 눌렀습니다."
          default:
        }
      })())
    }
  }

  new class {
    constructor(chatService, notificationService) {
      this.chatService = chatService
      this.notificationService = notificationService
      this.init()
      this.connect()
      setInterval(() => this.connect(), REFRESH_INTERVAL * 1000)
    }

    init() {
      this.stompClient = Stomp.over(new SockJS(WEB_SOCKET_URI))
    }

    async connect() {
      const channelAddress = (await axios.get("http://" + window.location.host + MESSAGE_BROKER_URI)).data.address
      this.stompClient.connect(
        {},
        frame => this.stompClient.subscribe(MESSAGE_BROKER_URI + "/" + channelAddress, message => this.dispatch(JSON.parse(message.body)))
      )
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
})()