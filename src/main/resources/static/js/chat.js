import templates from "./templates.js"
import api from "./api.js"

const WEB_SOCKET_URI = "/websocket"
const MESSAGE_BROKER_URI = "/api/chatting"
const REFRESH_INTERVAL = 150

new class ChatService {
    constructor() {
        if (document.getElementById("user-id") != null) {
            this.fromUserId = document.getElementById("user-id").value
            this.toUserId = document.getElementById("friend-id").value
            document.getElementById("messenger").addEventListener("click", async () => {
                this.connect()
                this.renderMessage(await api.get(`/api/chats/${this.toUserId}?first=false`))
            })
            document.getElementById("messenger-close").addEventListener("click", () => this.stompClient.disconnect())
            document.getElementById("send-message").addEventListener("click", () => {
                const content = document.getElementById("message-content").value.trim()
                if (content.length > 0) {
                    document.getElementById("message-content").value = ""
                    this.sendMessage(content)
                    setTimeout(async () => this.renderMessage(await api.get(`/api/chats/${this.toUserId}?first=false`)), REFRESH_INTERVAL)
                }
            })
            window.addEventListener("click", event => {
                if (event.target == document.getElementById("messenger-modal")) {
                    this.stompClient.disconnect()
                }
            })
        }
    }

    connect() {
        this.stompClient = Stomp.over(new SockJS(WEB_SOCKET_URI))
        this.stompClient.debug = () => {}
        this.stompClient.connect({}, frame => {
            this.stompClient.subscribe(MESSAGE_BROKER_URI, async message => {
                const res = JSON.parse(message.body)
                if (res[0].userId.toString() === this.fromUserId || res[0].userId.toString() === this.toUserId) {
                    this.renderMessage(await api.get(`/api/chats/${this.toUserId}?first=false`))
                }
            })
        })
    }

    async sendMessage(content) {
        await api.post("/api/chats", {
            "userId": this.toUserId,
            "content": content
        })
        this.renderMessage(await api.get(`/api/chats/${this.toUserId}?first=true`))
    }

    renderMessage(messages) {
        document.getElementById("message-area").innerHTML = ""
        for (let i = 0; i < messages.length; i++) {
            const pos = !(messages[i].userId.toString() === this.fromUserId)
            const isRead = (messages[i].read) ? "" : "1"
            this.addMessage(pos, messages[i].userName, isRead, messages[i].content)
        }
    }

    addMessage(type, name, isRead, content) {
        const message = (() => {
            if (type) {
                return templates.yourMessage(name, document.getElementById("profileimg").src, content)
            } else {
                return templates.myMessage(isRead, content)
            }
        })()
        const messageArea = document.getElementById("message-area")
        messageArea.insertAdjacentHTML("beforeend", message)
        messageArea.scrollTop = messageArea.scrollHeight
    }
}()