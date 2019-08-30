"use strict";

const notification = (() => {
  const WEB_SOCKET_URI = "/websocket"
  const MESSAGE_BROKER_URI = "/api/notification"
  const REFRESH_INTERVAL = 600
  const NOTIFICATION_INTERVAL = .56789
  const SAME_NOTIFICATION_INTERVAL = 2.5

  class NotificationService {
    constructor() {
      this.notificationQueue = []
      this.isNotifying = null
      this.lastNotifiedTime = {}
      setInterval(() => this.collectGarbage(), REFRESH_INTERVAL * 1000)
    }

    dispatch(message) {
      const srcUserName = message.srcUser.name + " 님"
      switch(message.type) {
        case "CHAT":
          this.pushToQueue(message.srcUser.name + " : " + message.content)
          break
        case "FRIEND_REQUEST":
          this.checkInterval(message.type, srcUserName + "과 친구가 되었습니다.", message.srcUser.id)
          break
        case "COMMENT":
          this.pushToQueue(srcUserName + "이 '" + message.srcSummary + "' 글에 댓글을 남겼습니다 : " + message.content)
          break
        case "LIKE":
          this.checkInterval(message.type, srcUserName + "이 '" + message.srcSummary + "' 글에 좋아요를 눌렀습니다.", message.srcUser.id)
          break
        default:
      }
    }

    pushToQueue(notificationMessage) {
      this.notificationQueue.push(notificationMessage)
      if (this.notificationQueue.length === 1) {
        this.isNotifying = setInterval(() => this.notify(), NOTIFICATION_INTERVAL * 1000)
      }
    }

    notify() {
      document.body.insertAdjacentHTML(
        "beforeend",
        '<p class="notification chat" data-close="self" role="alert">' + this.notificationQueue.shift() + "</p>"
      )
      if (this.notificationQueue.length === 0) {
        clearInterval(this.isNotifying)
      }
    }

    checkInterval(messageType, notificationMessage, srcUserId) {
      if (!this.lastNotifiedTime[srcUserId] || (new Date() - this.lastNotifiedTime[srcUserId].time > SAME_NOTIFICATION_INTERVAL * 1000)) {
        this.pushToQueue(notificationMessage)
        this.lastNotifiedTime[srcUserId] = {
          "type": messageType,
          "time": new Date()
        }
      }
    }

    collectGarbage() {
      for (id in Object.keys(this.lastNotifiedTime)) {
        if (new Date() - this.lastNotifiedTime[id] > REFRESH_INTERVAL * 1000) {
          delete this.lastNotifiedTime[id]
        }
      }
    }
  }

  new class Controller {
    constructor(notificationService) {
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
        frame => this.stompClient.subscribe(MESSAGE_BROKER_URI + "/" + channelAddress, message => this.notificationService.dispatch(JSON.parse(message.body)))
      )
    }

    refresh() {
      this.stompClient.disconnect(async () => this.connect(await this.requestNewChannelAddress()))
    }
  }(new NotificationService())

  return new Notifications(".notification", {
    startTopPosition: 50
  })
})()
notification.init()