async function connect() {
    let socket = new SockJS('/websocket');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);

        stompClient.subscribe('/api/chatting', function (messages) {
            const fromUserId = document.getElementById('user-id').value;
            const toUserId = document.getElementById('friend-id').value;
            let json = JSON.parse(messages.body);

            if (json[0].userId == toUserId || json[0].userId == fromUserId) {
                fetch(BASE_URL + "/api/chats/" + toUserId + "?first=" + false, {
                    method: "GET",
                    headers: {
                        "Accept": "application/json"
                    }
                })
                    .then(response => response.json())
                    .then(json => {
                        document.getElementById("message-area").innerHTML = "";
                        for (let i = 0; i < json.length; i++) {
                            let pos = !(json[i].userId == fromUserId);
                            let read = (json[i].read) ? "" : "1";
                            addMessage(pos, json[i].userName, read, json[i].content);
                        }
                    });
            }
        });
    });
}

const btn = document.getElementById("messenger");

// When the user clicks on the button, open the modal
btn.onclick = function () {
    const fromUserId = document.getElementById('user-id').value;
    const toUserId = document.getElementById('friend-id').value;

    fetch(BASE_URL + "/api/chats/" + toUserId + "?first=" + true, {
        method: "GET",
        headers: {
            "Accept": "application/json"
        }
    })
        .then(response => response.json())
        .then(json => {
            document.getElementById("message-area").innerHTML = "";
            for (let i = 0; i < json.length; i++) {
                let pos = !(json[i].userId == fromUserId);
                let read = (json[i].read) ? "" : "1";
                addMessage(pos, json[i].userName, read, json[i].content);
            }
        });
    connect();
}

const closeBtn = document.getElementById("messenger-close");

// When the user clicks on <span> (x), close the modal
closeBtn.onclick = function () {
    stompClient.disconnect();
}

window.onclick = function (event) {
    if (event.target == document.getElementById("messenger-modal")) {
        stompClient.disconnect();
    }
}

const sendMessage = function sendMessage() {
    document.getElementById("send-message").addEventListener("click", function () {
        const fromUserId = document.getElementById('user-id').value;
        const toUserId = document.getElementById('friend-id').value;

        const content = document.getElementById("message-content").value;
        if (content.length != 0) {
            document.getElementById("message-content").value = "";

            const chatRequest = {
                userId: toUserId,
                content: content
            };

            fetch(BASE_URL + "/api/chats", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json; charset=utf-8"
                },
                body: JSON.stringify(chatRequest)
            }).then(response => {
                fetch(BASE_URL + "/api/chats/" + toUserId + "?first=" + true, {
                    method: "GET",
                    headers: {
                        "Accept": "application/json"
                    }
                })
                    .then(response => response.json())
                    .then(json => {
                        document.getElementById("message-area").innerHTML = "";
                        for (let i = 0; i < json.length; i++) {
                            let pos = !(json[i].userId == fromUserId);
                            let read = (json[i].read) ? "" : "1";
                            addMessage(pos, json[i].userName, read, json[i].content);
                        }
                    });
            }).then(response => {
                setTimeout(function () {
                    fetch(BASE_URL + "/api/chats/" + toUserId + "?first=" + true, {
                        method: "GET",
                        headers: {
                            "Accept": "application/json"
                        }
                    })
                        .then(response => response.json())
                        .then(json => {
                            document.getElementById("message-area").innerHTML = "";
                            for (let i = 0; i < json.length; i++) {
                                let pos = !(json[i].userId == fromUserId);
                                let read = (json[i].read) ? "" : "1";
                                addMessage(pos, json[i].userName, read, json[i].content);
                            }
                        });
                }, 100);
            });
        }
    })
}();

function addMessage(type, name, read, msg) {
    let message;

    if (type) {
        const imageSrc = document.getElementById('profileimg').src;
        message = templates.yourMessage(name, imageSrc, read, msg);
    } else {
        message = templates.myMessage(read, msg);
    }

    const messageArea = document.getElementById("message-area");
    messageArea.insertAdjacentHTML("beforeend", message);
    messageArea.scrollTop = messageArea.scrollHeight;
}