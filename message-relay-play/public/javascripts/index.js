window.onload = function() {
  window.setInterval(function() {
    var target = document.getElementById("jumbotron-content");
    getNewMessages(target.childElementCount)
      .then(function(data) {
        data.messages.forEach(function(message){
          var isScrolledToBottom = target.scrollHeight - target.clientHeight <= target.scrollTop + 1;
          var messageDiv = document.createElement("div");
          messageDiv.innerHTML = message;
          target.appendChild(messageDiv);
          if(isScrolledToBottom) {
            target.scrollTop = target.scrollHeight - target.clientHeight;
          }
        });
      });
  }, 2000)
};


function getNewMessages(lastIndex) {
  return fetch("/messages?index=" + lastIndex)
  .then(function(response) { return response.json()})
}

function broadcastMessage() {
  messageElem = document.getElementById("broadcast-text");
  message = messageElem.value;
  messageElem.value = "";
  sendto = document.getElementById("sendto-text").value.split(",");

  return fetch("/send", {
      method: "POST",
      body: JSON.stringify({"message": message, "sendto": sendto}),
      headers: {'Content-Type': 'application/json'}
  }).then(res => res.json())
    .then(response => console.log('Success:', JSON.stringify(response)))
    .catch(error => console.error('Error:', error));
}

