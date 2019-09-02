"use strict";

const notification = (() => {
  const WEB_SOCKET_URI = "/websocket"
  const MESSAGE_BROKER_URI = "/api/notification"
  const REFRESH_INTERVAL = 600
  const NOTIFICATION_INTERVAL = .75
  const SAME_NOTIFICATION_INTERVAL = 3
  const MESSAGE_TYPE = {
    CHAT: "CHAT",
    FRIEND_REQUEST: "FRIEND_REQUEST",
    COMMENT: "COMMENT",
    LIKE: "LIKE"
  }

  class NotificationService {
    constructor() {
      this.notificationQueue = []
      this.lastNotifiedTime = {}
      setInterval(() => this.notify(), NOTIFICATION_INTERVAL * 1000)
      setInterval(() => this.collectGarbage(), REFRESH_INTERVAL * 1000)
    }

    dispatch(message) {
      const srcUserName = `${message.srcUser.name}  님`
      switch(message.type) {
        case MESSAGE_TYPE.CHAT:
          this.pushToQueue(`${message.srcUser.name}  : ${message.content}`)
          break
        case MESSAGE_TYPE.FRIEND_REQUEST:
          this.filterMessage(message.type, `${srcUserName}과 친구가 되었습니다.`, message.srcUser.id)
          break
        case MESSAGE_TYPE.COMMENT:
          this.pushToQueue(`${srcUserName}께서 '${message.srcSummary}' 글에 댓글을 남겼습니다 : ${message.content}`)
          break
        case MESSAGE_TYPE.LIKE:
          this.filterMessage(message.type, `${srcUserName}께서 '${message.srcSummary}' 글에 좋아요를 눌렀습니다.`, message.srcUser.id)
          break
        default:
      }
    }

    pushToQueue(notificationMessage) {
      this.notificationQueue.push(notificationMessage)
    }

    notify() {
      if (this.notificationQueue.length > 0) {
        document.body.insertAdjacentHTML(
          "beforeend",
          `<p class="notification chat" data-close="self" role="alert">${this.notificationQueue.shift()}</p>`
        )
      }
    }

    filterMessage(messageType, notificationMessage, srcUserId) {
      if (typeof this.lastNotifiedTime[srcUserId] === "undefined"
        || this.lastNotifiedTime[srcUserId].type != messageType
        || new Date() - this.lastNotifiedTime[srcUserId].time > SAME_NOTIFICATION_INTERVAL * 1000) {
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
      return (await axios.get(`http://${window.location.host}${MESSAGE_BROKER_URI}`)).data.address
    }

    connect(channelAddress) {
      this.stompClient = Stomp.over(new SockJS(WEB_SOCKET_URI))
      this.stompClient.debug = () => {}
      this.stompClient.connect(
        {},
        frame => this.stompClient.subscribe(`${MESSAGE_BROKER_URI}/${channelAddress}`, message => this.notificationService.dispatch(JSON.parse(message.body)))
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