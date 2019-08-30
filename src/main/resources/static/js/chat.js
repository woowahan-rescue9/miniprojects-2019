async function connect() {
  let socket = new SockJS('/websocket');
  stompClient = Stomp.over(socket);

  stompClient.connect({}, function (frame) {
    console.log('Connected: ' + frame);

    stompClient.subscribe('/api/chatting', function (messages) {
        const fromUserId = document.getElementById('user-id').value;
        const toUserId = document.getElementById('friend-id').value;
        let json = JSON.parse(messages.body);

        if(json[0].userId == toUserId || json[0].userId == fromUserId){
        fetch(BASE_URL + "/api/chats/" + toUserId + "?first=" + false, {
                              method : "GET",
                              headers: {
                                 "Accept": "application/json"
                              }
              })
       .then(response => response.json())
       .then(json => {
            document.getElementById("messagearea").innerHTML="";
            for (let i = 0; i < json.length; i++) {
                  let pos = !(json[i].userId == fromUserId);
                  let read = (json[i].read) ? "" : "1";
                  jstalktheme_addmsg(pos, json[i].userName, read, json[i].content);
            }
       });
        }
    });

  });
}

// Get the modal
let modal = document.getElementById('myModal');

// Get the button that opens the modal
let btn = document.getElementById("myBtn");

// Get the <span> element that closes the modal
let span = document.getElementsByClassName("close")[0];

// When the user clicks on the button, open the modal
btn.onclick = function() {
    modal.style.display = "block";
    const fromUserId = document.getElementById('user-id').value;
    const toUserId = document.getElementById('friend-id').value;

    fetch(BASE_URL + "/api/chats/" + toUserId + "?first=" + true, {
        method : "GET",
        headers: {
            "Accept": "application/json"
        }
    })
    .then(response => response.json())
    .then(json => {
        document.getElementById("messagearea").innerHTML="";
        for (let i = 0; i < json.length; i++) {
            let pos = !(json[i].userId == fromUserId);
            let read = (json[i].read) ? "" : "1";
            jstalktheme_addmsg(pos, json[i].userName, read, json[i].content);
        }
    });
    connect();
}

// When the user clicks on <span> (x), close the modal
span.onclick = function() {
    stompClient.disconnect();
    modal.style.display = "none";
}

// When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
    if (event.target == modal) {
        stompClient.disconnect();
        modal.style.display = "none";
    }
}

const closeMessage = function closeMessage() {
    document.getElementById('messageclose').addEventListener("click", function() {
                  document.getElementById('messagecontent').value = "";
    });
}();

const sendMessage = function sendMessage() {
    document.getElementById('sendmessage').addEventListener("click", function () {
       const fromUserId = document.getElementById('user-id').value;
       const toUserId = document.getElementById('friend-id').value;

       const content = document.getElementById('messagecontent').value;
       document.getElementById('messagecontent').value = "";

       const chatRequest = {
           userId: toUserId,
           content: content
       };

       fetch(BASE_URL + "/api/chats", {
               method : "POST",
               headers: {
                  "Content-Type": "application/json; charset=utf-8"
               },
               body: JSON.stringify(chatRequest)
       }).then(response => {
        fetch(BASE_URL + "/api/chats/" + toUserId + "?first=" + true, {
                              method : "GET",
                              headers: {
                                 "Accept": "application/json"
                              }
              })
       .then(response => response.json())
       .then(json => {
            document.getElementById("messagearea").innerHTML="";
            for (let i = 0; i < json.length; i++) {
                  let pos = !(json[i].userId == fromUserId);
                  let read = (json[i].read) ? "" : "1";
                  jstalktheme_addmsg(pos, json[i].userName, read, json[i].content);
            }
       });
       }).
       then(response => {setTimeout(function(){
         fetch(BASE_URL + "/api/chats/" + toUserId + "?first=" + true, {
                               method : "GET",
                               headers: {
                                  "Accept": "application/json"
                               }
               })
        .then(response => response.json())
        .then(json => {
             document.getElementById("messagearea").innerHTML="";
             for (let i = 0; i < json.length; i++) {
                   let pos = !(json[i].userId == fromUserId);
                   let read = (json[i].read) ? "" : "1";
                   jstalktheme_addmsg(pos, json[i].userName, read, json[i].content);
             }
        });}, 100);
       });
})
}();

function jstalktheme_addmsg(type, name, time, msg)
{
	let ocontainer = document.getElementById("jstalktheme_test");
    let ocontainer_msg = ocontainer.getElementsByClassName("msg")[0];

    let onewmsg = document.createElement("div");
    let onewblank = document.createElement("div");

    if(type)
    {
        let imgsrc = document.getElementById('profileimg').src;
        onewmsg.className="othertalk";
        onewmsg.innerHTML =
        "<div class=\"profile_image\" style=\"background: url(" + imgsrc + ") no-repeat; background-size: cover;\">\n"+
        "</div>\n"+
        "<div class=\"box\">\n"+
        "<div class=\"profile_name\">\n"+
        name+"\n"+
        "</div>\n"+
        "<div class=\"a\">\n"+
        "</div>\n"+
        "<div class=\"b\">\n"+
        msg+"\n"+
        "</div>\n" +
        "<div class=\"time\">\n"+
        time+"\n"+
        "</div>\n"+
        "</div>\n";
    }else{
        onewmsg.className="mytalk";
        onewmsg.innerHTML =
        "<div class=\"b\">\n"+
        "</div>\n"+
        "<div class=\"a\">\n"+
        msg+"\n"+
        "</div>\n"+
        "<div class=\"time\">\n"+
        time+"\n"+
        "</div>\n" +
        "";
    }

	onewmsg.innerHTML +=
    "<div class=\"clear\">\n"+
    "</div>";

	onewblank.className="blank";

	ocontainer_msg.appendChild(onewmsg);
	ocontainer_msg.appendChild(onewblank);

	ocontainer_msg.scrollTop = ocontainer_msg.scrollHeight;

}


function jstalktheme_testfunc()
{
	let otxtmsg = document.getElementById("jstalktheme_testmsg");

	let d = new Date();
	let ampm = (d.getHours()>12 ?  "PM" : "AM");
	let h = (d.getHours()>12 ? d.getHours()-12 : d.getHours());
	let m = d.getMinutes();

	test_type ^= 0x01;
	jstalktheme_addmsg(test_type, "아무개", ampm+" "+h+":"+m, otxtmsg.value.replace("\n","<br />\n"));
}